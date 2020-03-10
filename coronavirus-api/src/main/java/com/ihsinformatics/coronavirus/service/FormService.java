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
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.dto.FormDataDesearlizeDto;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Service
public interface FormService {

    /**
     * Caution! This is not recommended, use voidFormData method instead
     * 
     * @param obj
     * @throws HibernateException
     */
    void deleteFormData(FormData obj) throws HibernateException;

    /**
     * @param obj
     * @throws HibernateException
     */
    void deleteFormType(FormType obj) throws HibernateException;

    /**
     * Returns list of all {@link FormType} objects
     * 
     * @param includeRetired
     * @return
     * @throws HibernateException
     */
    List<FormType> getAllFormTypes(boolean includeRetired) throws HibernateException;

    /**
     * Returns list of {@link FormData} objects by given date range
     * 
     * @param from: starting range of {@link Date} object
     * @param to: ending range of {@link Date} object
     * @param page: page number to retrieve
     * @param pageSize: number of objects in the page
     * @param sortByField: name of the field to sort the data by
     * @param includeVoided: whether to include voided records or not
     * @return
     * @throws HibernateException
     */
    List<FormData> getFormDataByDate(Date from, Date to, Integer page, Integer pageSize, String sortByField,
	    Boolean includeVoided) throws HibernateException;
    
    /**
     * Returns list of {@link FormData} objects by given date range
     * 
     * @param from: starting range of {@link Date} object
     * @param to: ending range of {@link Date} object
     * @return
     * @throws HibernateException
     */
    List<FormData> getFormDataByDate(Date from, Date to) throws HibernateException;

    /**
     * Returns {@link FormData} object by given ID
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    FormData getFormDataById(Integer id) throws HibernateException;

    /**
     * Returns list of {@link FormData} objects by matching {@link Location} object
     * 
     * @param location
     * @return
     * @throws HibernateException
     */
    List<FormData> getFormDataByLocation(Location location) throws HibernateException;

    /**
     * Returns {@link FormData} object by matching given reference ID
     * 
     * @param referenceId
     * @return
     * @throws HibernateException
     */
    FormData getFormDataByReferenceId(String referenceId) throws HibernateException;

    /**
     * Returns {@link FormData} object by given UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    FormData getFormDataByUuid(String uuid) throws HibernateException;

    /**
     * Returns {@link FormType} object by given ID
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    FormType getFormTypeById(Integer id) throws HibernateException;

    /**
     * Returns {@link FormType} object matching given form name. This method first
     * searches for both full name, then short name if not found
     * 
     * @param name
     * @return
     * @throws HibernateException
     */
    FormType getFormTypeByName(String name) throws HibernateException;

    /**
     * Returns {@link FormType} object by given UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    FormType getFormTypeByUuid(String uuid) throws HibernateException;

    /**
     * Retire (soft delete) the {@link FormType} object
     * 
     * @param obj
     * @throws HibernateException
     */
    void retireFormType(FormType obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    @MeasureProcessingTime
    FormData saveFormData(FormData obj) throws HibernateException, ValidationException, IOException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     * @throws JSONException
     * @throws ValidationException
     */
    FormType saveFormType(FormType obj) throws HibernateException, ValidationException, JSONException;

    /**
     * Returns list of {@link FormData} objects by matching all the non-null
     * parameters
     * 
     * @param formType: the {@link FormType} object
     * @param location: the {@link Location} object
     * @param from: starting range of {@link Date} object
     * @param to: ending range of {@link Date} object
     * @param page: page number to retrieve
     * @param pageSize: number of objects in the page
     * @param sortByField: name of the field to sort the data by
     * @param includeVoided: whether to include voided records or not
     * @return
     * @throws HibernateException
     */
    List<FormData> searchFormData(FormType formType, Location location, Date from, Date to, Integer page,
	    Integer pageSize, String sortByField, Boolean includeVoided) throws HibernateException;
    
    /**
     * Returns list of {@link FormData} objects by matching all the non-null
     * parameters
     * 
     * @param formType: the {@link FormType} object
     * @param location: the {@link Location} object
     * @param from: starting range of {@link Date} object
     * @param to: ending range of {@link Date} object
     * @param page: page number to retrieve
     * @param sortByField: name of the field to sort the data by
     * @param includeVoided: whether to include voided records or not
     * @return
     * @throws HibernateException
     */
    List<FormData> searchFormData(FormType formType, Location location,  Definition formGroup, Date from, Date to, String sortByField, Boolean includeVoided) throws HibernateException;

    /**
     * Restore the {@link FormType} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws JSONException
     * @throws ValidationException
     */
    void unretireFormType(FormType obj) throws HibernateException, ValidationException, JSONException;

    /**
     * Restore the voided {@link FormData} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    FormData unvoidFormData(FormData obj) throws HibernateException, ValidationException, IOException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    @MeasureProcessingTime
    FormData updateFormData(FormData obj) throws HibernateException, ValidationException, IOException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     * @throws JSONException
     * @throws ValidationException
     */
    FormType updateFormType(FormType obj) throws HibernateException, ValidationException, JSONException;

    /**
     * Void (soft delete) the {@link FormData} object
     * 
     * @param obj
     * @throws HibernateException
     */
    void voidFormData(FormData obj) throws HibernateException;
    
    /**
     * @param uuid
     * @return
     * @throws HibernateException
     */
    FormDataDesearlizeDto getFormDataDesearlizeDtoUuid(String uuid, LocationService locationService, ParticipantService participantService, MetadataService metadataService, UserService userService, DonorService donorService);

}
