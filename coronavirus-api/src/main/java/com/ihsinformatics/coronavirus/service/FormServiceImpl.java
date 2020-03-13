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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.dto.FormDataDesearlizeDto;
import com.ihsinformatics.coronavirus.model.DataEntity;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.RegexUtil;


/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class FormServiceImpl extends BaseService implements FormService {

    @Autowired
    private ValidationService validationService;

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#deleteFormData(com.
     * ihsinformatics.coronavirus.model.FormData)
     */
    @Override
    @CheckPrivilege(privilege = "Delete FormData")
    public void deleteFormData(FormData obj) throws HibernateException {
	formDataRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#deleteFormType(com.
     * ihsinformatics.coronavirus.model.FormType)
     */
    @Override
    @CheckPrivilege(privilege = "Delete FormType")
    public void deleteFormType(FormType obj) throws HibernateException {
	formTypeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormTypes(boolean)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormType")
    public List<FormType> getAllFormTypes(boolean includeRetired) throws HibernateException {
	if (!includeRetired) {
	    return formTypeRepository.findNonRetired();
	}
	return formTypeRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataByDate(java.
     * util.Date, java.util.Date, java.lang.Integer, java.lang.Integer,
     * java.lang.String, boolean)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public List<FormData> getFormDataByDate(Date from, Date to, Integer page, Integer pageSize, String sortByField,
	    Boolean includeVoided) throws HibernateException {
	if (sortByField == null) {
	    sortByField = "formDate";
	}
	Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));
	Page<FormData> list = formDataRepository.findByDateRange(from, to, pageable);
	return list.getContent();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataByDate(java.
     * util.Date, java.util.Date)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public List<FormData> getFormDataByDate(Date from, Date to) throws HibernateException {
	List<FormData> list = formDataRepository.findByDateRange(from, to);
	return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataById(java.
     * lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View FormData")
    public FormData getFormDataById(Integer id) throws HibernateException {
	Optional<FormData> found = formDataRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataByLocation(
     * com.ihsinformatics.coronavirus.model.Location)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public List<FormData> getFormDataByLocation(Location location) throws HibernateException {
	return formDataRepository.findByLocation(location);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataByReferenceId
     * (java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View FormData")
    public FormData getFormDataByReferenceId(String referenceId) throws HibernateException {
	Optional<FormData> found = formDataRepository.findByReference(referenceId);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormDataByUuid(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View FormData")
    public FormData getFormDataByUuid(String uuid) throws HibernateException {
	return formDataRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormTypeById(java.
     * lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View FormType")
    public FormType getFormTypeById(Integer id) throws HibernateException {
	Optional<FormType> found = formTypeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormTypeByName(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View FormType")
    public FormType getFormTypeByName(String name) throws HibernateException {
	FormType found = formTypeRepository.findByFormName(name);
	if (found == null) {
	    found = formTypeRepository.findByShortName(name);
	}
	return found;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#getFormTypeByUuid(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View FormType")
    public FormType getFormTypeByUuid(String uuid) throws HibernateException {
	return formTypeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#retireFormData(com.
     * ihsinformatics.coronavirus.model.FormType)
     */
    @Override
    @CheckPrivilege(privilege = "Void FormType")
    public void retireFormType(FormType obj) throws HibernateException {
	obj = (FormType) setSoftDeleteAuditAttributes(obj);
	obj.setIsRetired(Boolean.TRUE);
	formTypeRepository.softDelete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#saveFormData(com.
     * ihsinformatics.coronavirus.model.FormData)
     */
    @Override
    @MeasureProcessingTime
/*    @CheckPrivilege(privilege = "Add FormData")
*/    
    public FormData saveFormData(FormData obj) throws HibernateException, ValidationException, IOException {
	FormData found = formDataRepository.findByUuid(obj.getUuid());
	if (found != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate FormData object!");
	}
	Optional<FormData> byReference = formDataRepository.findByReference(obj.getReferenceId());
	if (byReference.isPresent()) {
	    throw new HibernateException("A FormData object with similar ReferenceID already exists!");
	}
	validationService.validateFormData(obj, new DataEntity());
	obj = (FormData) setCreateAuditAttributes(obj);
	return formDataRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#saveFormType(com.
     * ihsinformatics.coronavirus.model.FormType)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add FormType")
    public FormType saveFormType(FormType obj) throws HibernateException, ValidationException, JSONException {
	FormType found = formTypeRepository.findByUuid(obj.getUuid());
	if (found != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate FormType object!");
	}
	if (validationService.validateFormType(obj)) {
	    return formTypeRepository.save(obj);
	} else {
	    throw new ValidationException(
		    "Unable to validate FormType schema. Please check logs for detailed message.");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#searchFormData(com.
     * ihsinformatics.coronavirus.model.FormType,
     * com.ihsinformatics.coronavirus.model.Location, java.lang.Integer,
     * java.lang.Integer, java.lang.String, boolean)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public List<FormData> searchFormData(FormType formType, Location location, Date from, Date to, Integer page,
	    Integer pageSize, String sortByField, Boolean includeVoided) throws HibernateException {
	Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortByField));
	Page<FormData> list = formDataRepository.search(formType, location, from, to, pageable);
	return list.getContent();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#searchFormData(com.
     * ihsinformatics.coronavirus.model.FormType,
     * com.ihsinformatics.coronavirus.model.Location, java.lang.Integer,
     * java.lang.Integer, java.lang.String, boolean)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public List<FormData> searchFormData(FormType formType, Location location, Definition formGroup, Date from, Date to,
	   String sortByField, Boolean includeVoided) throws HibernateException {
	return formDataRepository.search(formType, location, formGroup, from, to);
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.FormService#unretireFormType(com.
     * ihsinformatics.coronavirus.model.FormType)
     */
    @Override
    @CheckPrivilege(privilege = "Void FormType")
    public void unretireFormType(FormType obj) throws HibernateException, ValidationException, JSONException {
	if (obj.getIsRetired()) {
	    obj.setIsRetired(Boolean.FALSE);
	    if (obj.getReasonRetired() == null) {
		obj.setReasonRetired("");
	    }
	    obj.setReasonRetired(obj.getReasonRetired() + "(Unretired on "
		    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
	    updateFormType(obj);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#unvoidFormData(com.
     * ihsinformatics.coronavirus.model.FormData)
     */
    @Override
    @CheckPrivilege(privilege = "Void FormData")
    @Transactional
    public FormData unvoidFormData(FormData obj) throws HibernateException, ValidationException, IOException {
	if (obj.getIsVoided()) {
	    obj.setIsVoided(Boolean.FALSE);
	    if (obj.getReasonVoided() == null) {
		obj.setReasonVoided("");
	    }
	    obj.setReasonVoided(obj.getReasonVoided() + "(Unvoided on "
		    + DateTimeUtil.toSqlDateTimeString(Calendar.getInstance().getTime()) + ")");
	    return updateFormData(obj);
	}
	return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#updateFormData(com.
     * ihsinformatics.coronavirus.model.FormData)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Edit FormData")
    public FormData updateFormData(FormData obj) throws HibernateException, ValidationException, IOException {
	//validationService.validateFormData(obj, new DataEntity());
	obj = (FormData) setUpdateAuditAttributes(obj);
	return formDataRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#updateFormType(com.
     * ihsinformatics.coronavirus.model.FormType)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Edit FormType")
    public FormType updateFormType(FormType obj) throws HibernateException, ValidationException, JSONException {
	if (validationService.validateFormType(obj)) {
	    obj = (FormType) setUpdateAuditAttributes(obj);
	    return formTypeRepository.save(obj);
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#voidFormData(com.
     * ihsinformatics.coronavirus.model.FormData)
     */
    @Override
    @CheckPrivilege(privilege = "Void FormData")
    @Transactional
    public void voidFormData(FormData obj) throws HibernateException {
	obj = (FormData) setSoftDeleteAuditAttributes(obj);
	obj.setIsVoided(Boolean.TRUE);
	formDataRepository.softDelete(obj);
    }

	
	@Override
	@CheckPrivilege(privilege = "View FormData")
	public FormDataDesearlizeDto getFormDataDesearlizeDtoUuid(String uuid, LocationService locationService,
			ParticipantService participantService, MetadataService metadataService, UserService userService,
			DonorService donorService) {
		 FormData formData = null; 
		 if (uuid.matches(RegexUtil.UUID)) {
 	    	formData =  formDataRepository.findByUuid(uuid);
 	     } else {
 	    	formData = formDataRepository.findById(Integer.parseInt(uuid)).get();
 	     }
		 if(formData != null){
			return  new FormDataDesearlizeDto(formData, locationService, participantService, metadataService, userService, donorService);
		 }
		return null;
	}
}
