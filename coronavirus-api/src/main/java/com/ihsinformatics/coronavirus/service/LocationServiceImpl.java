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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.dto.LocationDesearlizeDto;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.RegexUtil;
import com.ihsinformatics.coronavirus.util.SearchCriteria;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@Component
public class LocationServiceImpl extends BaseService implements LocationService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#deleteLocation(com.
     * ihsinformatics.coronavirus.model.Location, boolean)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Location")
    public void deleteLocation(Location obj, boolean force) throws HibernateException {
	// Check dependencies first
	if (!obj.getAttributes().isEmpty()) {
	    if (force) {
		for (LocationAttribute attribute : obj.getAttributes()) {
		    deleteLocationAttribute(attribute);
		}
	    } else {
		throw new HibernateException(
			"One or more LocationAttribute objects depend on this Location. Please delete the dependent objects (by setting the force parameter true) first.");
	    }
	}
	locationRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * deleteLocationAttribute(com.ihsinformatics.coronavirus.model.
     * LocationAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Location")
    public void deleteLocationAttribute(LocationAttribute obj) throws HibernateException {
	locationAttributeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * deleteLocationAttributeType(com.ihsinformatics.coronavirus.model.
     * LocationAttributeType, boolean)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Metadata")
    public void deleteLocationAttributeType(LocationAttributeType obj, boolean force) throws HibernateException {
	List<LocationAttribute> attributesByType = getLocationAttributesByType(obj);
	if (!attributesByType.isEmpty()) {
	    if (force) {
		for (LocationAttribute locationAttribute : attributesByType) {
		    deleteLocationAttribute(locationAttribute);
		}
	    } else {
		throw new HibernateException(
			"One or more LocationAttribute objects depend on this LocationAttributeType. Please delete the dependent objects (by setting the force parameter true) first.");
	    }
	}
	locationAttributeTypeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getAllLocationAttributeTypes()
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Metadata")
    public List<LocationAttributeType> getAllLocationAttributeTypes() throws HibernateException {
	return locationAttributeTypeRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getAllLocations()
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getAllLocations() throws HibernateException {
	return locationRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeById(java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public LocationAttribute getLocationAttributeById(Integer id) throws HibernateException {
	Optional<LocationAttribute> found = locationAttributeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeByUuid(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public LocationAttribute getLocationAttributeByUuid(String uuid) throws HibernateException {
	return locationAttributeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributes(com.ihsinformatics.coronavirus.model.Location,
     * com.ihsinformatics.coronavirus.model.LocationAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<LocationAttribute> getLocationAttributes(Location location, LocationAttributeType attributeType)
	    throws HibernateException {
	return locationAttributeRepository.findByLocationAndAttributeType(location, attributeType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributesByLocation(com.ihsinformatics.coronavirus.model.
     * Location)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<LocationAttribute> getLocationAttributesByLocation(Location location) throws HibernateException {
	return locationAttributeRepository.findByLocation(location);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributesByType(com.ihsinformatics.coronavirus.model.
     * LocationAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public List<LocationAttribute> getLocationAttributesByType(LocationAttributeType attributeType)
	    throws HibernateException {
	return locationAttributeRepository.findByAttributeType(attributeType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributesByValue(com.ihsinformatics.coronavirus.model.
     * LocationAttributeType, java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<LocationAttribute> getLocationAttributesByTypeAndValue(LocationAttributeType attributeType,
	    String attributeValue) throws HibernateException {
	return locationAttributeRepository.findByAttributeTypeAndValue(attributeType, attributeValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributesByValue(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<LocationAttribute> getLocationAttributesByValue(String attributeValue) throws HibernateException {
	return locationAttributeRepository.findByValue(attributeValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeTypeById(java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public LocationAttributeType getLocationAttributeTypeById(Integer id) throws HibernateException {
	Optional<LocationAttributeType> found = locationAttributeTypeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeTypeByName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public LocationAttributeType getLocationAttributeTypeByName(String name) throws HibernateException {
	return locationAttributeTypeRepository.findByAttributeName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeTypeByShortName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public LocationAttributeType getLocationAttributeTypeByShortName(String shortName) throws HibernateException {
	return locationAttributeTypeRepository.findByShortName(shortName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationAttributeTypeByUuid(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public LocationAttributeType getLocationAttributeTypeByUuid(String uuid) throws HibernateException {
	return locationAttributeTypeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationById(java
     * .lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public Location getLocationById(Integer id) throws HibernateException {
	Optional<Location> found = locationRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationByShortName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public Location getLocationByShortName(String shortName) throws HibernateException {
	return locationRepository.findByShortName(shortName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationByUuid(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public LocationDesearlizeDto getLocationDesearlizeDtoUuid(String uuid, LocationService locationService, MetadataService metadataService, UserService userService, DonorService donorService)  {
	Location loc = null; 
	 if (uuid.matches(RegexUtil.UUID)) {
		 loc =  locationRepository.findByUuid(uuid);
     } else {
    	 loc = locationRepository.findById(Integer.parseInt(uuid)).get();
     }
	if(loc != null){
		return new LocationDesearlizeDto(loc, locationService, metadataService, userService, donorService);
	}
	return null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationByUuid(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public Location getLocationByUuid(String uuid) throws HibernateException {
	return locationRepository.findByUuid(uuid);
    }

    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getLocationsByAddress(String address, String cityVillage, String stateProvince,
	    String country) throws HibernateException {
	return locationRepository.findByAddress(address, address, cityVillage, stateProvince, country);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationsByCategory(com.ihsinformatics.coronavirus.model.Definition)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getLocationsByCategory(Definition definition) throws HibernateException {
	return locationRepository.findByCategory(definition);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * getLocationsByContact(java.lang.String, java.lang.Boolean)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getLocationsByContact(String contact, Boolean primaryContactOnly) throws HibernateException {
	return locationRepository.findByContact(contact, primaryContactOnly);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationByName(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getLocationsByName(String name) throws HibernateException {
	return locationRepository.findByLocationName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#getLocationsByParent
     * (com.ihsinformatics.coronavirus.model.Location)
     */
    @Override
    @CheckPrivilege(privilege = "View Location")
    public List<Location> getLocationsByParent(Location parentLocation) throws HibernateException {
	return locationRepository.findByParentLocation(parentLocation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#saveLocation(com.
     * ihsinformatics.coronavirus.model.Location)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add Location")
    public Location saveLocation(Location obj) throws HibernateException {
	if (getLocationByShortName(obj.getShortName()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate Location!");
	}
	obj = (Location) setCreateAuditAttributes(obj);
	obj = locationRepository.save(obj);
	return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * saveLocationAttribute(com.ihsinformatics.coronavirus.model.
     * LocationAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Add Location")
    public LocationAttribute saveLocationAttribute(LocationAttribute obj) throws HibernateException {
	obj = (LocationAttribute) setCreateAuditAttributes(obj);
	return locationAttributeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * saveLocationAttributes(java.util.List)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add Location")
    public List<LocationAttribute> saveLocationAttributes(List<LocationAttribute> attributes)
	    throws HibernateException {
	for (LocationAttribute obj : attributes) {
	    obj = (LocationAttribute) setCreateAuditAttributes(obj);
	}
	return locationAttributeRepository.saveAll(attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * saveLocationAttributeType(com.ihsinformatics.coronavirus.model.
     * LocationAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "Add Metadata")
    public LocationAttributeType saveLocationAttributeType(LocationAttributeType obj) throws HibernateException {
	return locationAttributeTypeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#searchLocation(java.
     * util.List)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public List<Location> searchLocations(List<SearchCriteria> params) {
	if (params == null) {
	    params = new ArrayList<>();
	}
	if (params.isEmpty()) {
	    return new ArrayList<>();
	}
	return locationRepository.search(params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.LocationService#updateLocation(com.
     * ihsinformatics.coronavirus.model.Location)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Edit Location")
    public Location updateLocation(Location obj) throws HibernateException {
	obj = (Location) setUpdateAuditAttributes(obj);
	return locationRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * updateLocationAttribute(com.ihsinformatics.coronavirus.model.
     * LocationAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Location")
    public LocationAttribute updateLocationAttribute(LocationAttribute obj) throws HibernateException {
	obj = (LocationAttribute) setUpdateAuditAttributes(obj);
	return locationAttributeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.LocationService#
     * updateLocationAttributeType(com.ihsinformatics.coronavirus.model.
     * LocationAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Metadata")
    public LocationAttributeType updateLocationAttributeType(LocationAttributeType obj) throws HibernateException {
	obj = (LocationAttributeType) setUpdateAuditAttributes(obj);
	return locationAttributeTypeRepository.save(obj);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#voidParticipant(com.
     * ihsinformatics.coronavirus.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Void Location")
    @Transactional
    public void voidLocation(Location obj) throws HibernateException, ValidationException, IOException {
	obj = (Location) setSoftDeleteAuditAttributes(obj);
	obj.setIsVoided(Boolean.TRUE);
	for (LocationAttribute attribute : obj.getAttributes()) {
		if(Boolean.FALSE.equals(attribute.getIsVoided())){
			attribute.setIsVoided(Boolean.TRUE);
			attribute.setVoidedBy(obj.getVoidedBy());
			attribute.setDateVoided(obj.getDateVoided());
			attribute.setReasonVoided(obj.getReasonVoided() + " (Location voided)");
			locationAttributeRepository.softDelete(attribute);
		}
	}
	List<FormData> forms = formDataRepository.findByLocation(obj);
	for (FormData form : forms) {
		if(Boolean.FALSE.equals(form.getIsVoided())){
			form.setIsVoided(Boolean.TRUE);
			form.setVoidedBy(obj.getVoidedBy());
			form.setDateVoided(obj.getDateVoided());
			form.setReasonVoided(obj.getReasonVoided() + " (Location voided)");
			formDataRepository.softDelete(form);
		}
	}
	List<Participant> participants = participantRepository.findByLocation(obj);
	for (Participant participant : participants) {
		participant.setReasonVoided(obj.getReasonVoided() + " (Location voided)");
		participantService.voidParticipant(participant);
	}
	locationRepository.softDelete(obj);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#unvoidParticipant(com.
     * ihsinformatics.coronavirus.model.Participant)
     */
    @Override
    @CheckPrivilege(privilege = "Void Location")
    @Transactional
    public Location unvoidLocation(Location obj) throws HibernateException, ValidationException, IOException {
	if (obj.getIsVoided()) {
	    obj.setIsVoided(Boolean.FALSE);
	    if (obj.getReasonVoided() == null) {
		obj.setReasonVoided("");
	    }
	    String voidedReason = obj.getReasonVoided();
	    obj.setReasonVoided(obj.getReasonVoided() + "(Unvoided on "
		    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
	    
	    for (LocationAttribute attribute : obj.getAttributes()) {
			if(Boolean.TRUE.equals(attribute.getIsVoided()) && attribute.getReasonVoided().equals(voidedReason + " (Location voided)")){
				attribute.setIsVoided(Boolean.FALSE);
				if (attribute.getReasonVoided() == null) {
					attribute.setReasonVoided("");
				 }
				attribute.setReasonVoided(attribute.getReasonVoided() + "(Location unvoided on "
					    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
				updateLocationAttribute(attribute);
			}
		}
	    
	    List<FormData> forms = formDataRepository.findByLocation(obj);
		for (FormData form : forms) {
			if(Boolean.TRUE.equals(form.getIsVoided()) && form.getReasonVoided().equals(voidedReason + " (Location voided)")){
				form.setIsVoided(Boolean.FALSE);
				if (form.getReasonVoided() == null) {
					form.setReasonVoided("");
				 }
				form.setReasonVoided(form.getReasonVoided() + "(Location unvoided on "
					    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
				formService.updateFormData(form);
			}
		}
		
		List<Participant> participants = participantRepository.findByLocation(obj);
		for (Participant participant : participants) {
			if(Boolean.TRUE.equals(participant.getIsVoided()) && participant.getReasonVoided().equals(obj.getReasonVoided() + " (Location voided)")){
				participant.setIsVoided(Boolean.FALSE);
				if (participant.getReasonVoided() == null) {
					participant.setReasonVoided("");
				 }
				
				voidedReason = participant.getReasonVoided();
						
				participant.setReasonVoided(participant.getReasonVoided() + "(Location unvoided on "
					    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
				participantService.updateParticipant(participant);
				
				Person person = participant.getPerson();
			    if(Boolean.TRUE.equals(person.getIsVoided()) && person.getReasonVoided().equals(voidedReason + " (Participant voided)")){
					person.setIsVoided(Boolean.FALSE);
					if (person.getReasonVoided() == null) {
						person.setReasonVoided("");
					 }
					person.setReasonVoided(person.getReasonVoided() + "(Participant unvoided on "
						    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
					personService.updatePerson(person);
				}
			    
			    for (PersonAttribute attribute : participant.getPerson().getAttributes()) {
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
				
			}
		}
	    
	    return updateLocation(obj);
	}
	return obj;
    }
}
