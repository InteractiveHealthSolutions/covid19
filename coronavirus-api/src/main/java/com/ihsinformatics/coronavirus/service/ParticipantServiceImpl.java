/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.dto.ParticipantDesearlizeDto;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.RegexUtil;
import com.ihsinformatics.coronavirus.util.SearchCriteria;
import com.ihsinformatics.coronavirus.util.SearchQueryCriteriaConsumer;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class ParticipantServiceImpl extends BaseService implements ParticipantService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.ParticipantService#deleteParticipant
     * (com. ihsinformatics.cidemoapp.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Delete People")
    public void deleteParticipant(Participant obj) {
	participantRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.ParticipantService#
     * getParticipantById(java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View People")
    public Participant getParticipantById(Integer id) throws HibernateException {
	Optional<Participant> found = participantRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.ParticipantService#
     * getParticipantByIdentifier(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View People")
    public Participant getParticipantByIdentifier(String name) {
	return participantRepository.findByIdentifier(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.ParticipantService#getParticipant(
     * java.lang.Long)
     */
    @Override
    @CheckPrivilege(privilege = "View People")
    public Participant getParticipantByUuid(String uuid) {
	return participantRepository.findByUuid(uuid);
    }

    @Override
    @CheckPrivilege(privilege = "View People")
    public List<Participant> getParticipantsByLocation(Location location) {
	return participantRepository.findByLocation(location);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.ParticipantService#getParticipants(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View People")
    public List<Participant> getParticipantsByName(String name) {
	if (name.toLowerCase().matches("admin|administrator")) {
	    return Collections.emptyList();
	}
	List<Person> people = personRepository.findByPersonName(name, name, name);
	List<Participant> participants = new ArrayList<>();
	for (Person person : people) {
	    Optional<Participant> participant = Optional.of(participantRepository.findById(person.getPersonId())).get();
	    if (participant.isPresent()) {
		participants.add(participant.get());
	    }
	}
	return participants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.ParticipantService#saveParticipant(
     * com.ihsinformatics. cidemoapp.model.Participant)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add People")
    public Participant saveParticipant(Participant obj) {
	if (getParticipantByIdentifier(obj.getIdentifier()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate Participant!");
	}
	Optional<Location> location = locationRepository.findById(obj.getLocation().getLocationId());
	if (location.isPresent()) {
	    obj.setLocation(location.get());
	}
	Person person = personRepository.findByUuid(obj.getPerson().getUuid());
	if (person != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate Person!");
	}
	person = personRepository.save(obj.getPerson());
	obj = (Participant) setCreateAuditAttributes(obj);
	obj.getPerson().setCreatedBy(obj.getCreatedBy());
	for (PersonAttribute attribute : obj.getPerson().getAttributes()) {
	    attribute.setCreatedBy(obj.getCreatedBy());
	}
	obj.setPerson(person);
	return participantRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.ParticipantService#
     * searchParticipants(java.util.List)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View People")
    public List<Participant> searchParticipants(List<SearchCriteria> params) {
	CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
	CriteriaQuery<Participant> query = builder.createQuery(Participant.class);
	Root<Participant> r = query.from(Participant.class);
	Predicate predicate = builder.conjunction();
	SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, builder, r);
	params.stream().forEach(searchConsumer);
	predicate = searchConsumer.getPredicate();
	query.where(predicate);
	List<Participant> result = getEntityManager().createQuery(query).getResultList();
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.ParticipantService#updateParticipant
     * (com. ihsinformatics.cidemoapp.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Edit People")
    public Participant updateParticipant(Participant obj) {
	obj = (Participant) setUpdateAuditAttributes(obj);
	obj.getPerson().setUpdatedBy(obj.getUpdatedBy());
	obj.getPerson().setDateUpdated(obj.getDateUpdated());
	for (PersonAttribute attribute : obj.getPerson().getAttributes()) {
	    attribute.setUpdatedBy(obj.getUpdatedBy());
	    attribute.setDateUpdated(obj.getDateUpdated());
	}
	Person person = personRepository.save(obj.getPerson());	
	//obj.setPerson(person);
	return participantRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationByUuid(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Participant")
    public ParticipantDesearlizeDto getParticipantDesearlizeDtoUuid(String uuid, LocationService locationService, MetadataService metadataService, UserService userService, DonorService donorService) {
	Participant part = null; 
	 if (uuid.matches(RegexUtil.UUID)) {
		 part =  participantRepository.findByUuid(uuid);
    } else {
    	part = participantRepository.findById(Integer.parseInt(uuid)).get();
    }
	if(part != null){
		return  new ParticipantDesearlizeDto(part, locationService, metadataService, userService, donorService);
	}
	return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#voidParticipant(com.
     * ihsinformatics.coronavirus.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Void Participant")
    @Transactional
    public void voidParticipant(Participant obj) throws HibernateException {
	obj = (Participant) setSoftDeleteAuditAttributes(obj);
	obj.setIsVoided(Boolean.TRUE);
	Person person = (Person) setSoftDeleteAuditAttributes(obj.getPerson());
	person.setIsVoided(Boolean.TRUE);
	person.setReasonVoided(obj.getReasonVoided() + " (Participant voided)");
	for (PersonAttribute attribute : obj.getPerson().getAttributes()) {
		if(Boolean.FALSE.equals(attribute.getIsVoided())){
			attribute.setIsVoided(Boolean.TRUE);
			attribute.setVoidedBy(obj.getVoidedBy());
			attribute.setDateVoided(obj.getDateVoided());
			attribute.setReasonVoided(obj.getReasonVoided() + " (Participant voided)");
			personAttributeRepository.softDelete(attribute);
		}
	}
	personRepository.softDelete(person);
	participantRepository.softDelete(obj);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#unvoidParticipant(com.
     * ihsinformatics.coronavirus.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Void Participant")
    @Transactional
    public Participant unvoidParticipant(Participant obj) throws HibernateException, ValidationException, IOException {
	if (obj.getIsVoided()) {
	    obj.setIsVoided(Boolean.FALSE);
	    if (obj.getReasonVoided() == null) {
		obj.setReasonVoided("");
	    }
	    String voidedReason = obj.getReasonVoided();
	    obj.setReasonVoided(obj.getReasonVoided() + "(Unvoided on "
		    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
	    
	    Person person = obj.getPerson();
	    if(Boolean.TRUE.equals(person.getIsVoided()) && person.getReasonVoided().equals(voidedReason + " (Participant voided)")){
			person.setIsVoided(Boolean.FALSE);
			if (person.getReasonVoided() == null) {
				person.setReasonVoided("");
			 }
			person.setReasonVoided(person.getReasonVoided() + "(Participant unvoided on "
				    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
			personService.updatePerson(person);
		}
	    
	    for (PersonAttribute attribute : obj.getPerson().getAttributes()) {
			if(Boolean.TRUE.equals(attribute.getIsVoided()) && attribute.getReasonVoided().equals(voidedReason + " (Participant voided)")){
				attribute.setIsVoided(Boolean.FALSE);
				if (attribute.getReasonVoided() == null) {
					attribute.setReasonVoided("");
				 }
				attribute.setReasonVoided(attribute.getReasonVoided() + "(Participant unvoided on "
					    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
				personService.updatePersonAttribute(attribute);
			}
		}
	    
	    return updateParticipant(obj);
	}
	 return obj;
    }
}
