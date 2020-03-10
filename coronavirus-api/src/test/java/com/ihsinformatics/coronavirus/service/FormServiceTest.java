/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/
package com.ihsinformatics.coronavirus.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
/**
 * @author owais.hussain@ihsinformatics.com
 */
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyVararg;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hamcrest.Matchers;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.DataEntity;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.service.ValidationServiceImpl;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class FormServiceTest extends BaseServiceTest {

    @Mock
    protected ValidationServiceImpl validationService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#deleteFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     */
    @Test
    public void shouldDeleteFormData() {
	doNothing().when(formDataRepository).delete(any(FormData.class));
	formService.deleteFormData(ronData);
	verify(formDataRepository, times(1)).delete(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#deleteFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     */
    @Test
    public void shouldDeleteFormType() {
	doNothing().when(formTypeRepository).delete(any(FormType.class));
	formService.deleteFormType(quidditchForm);
	verify(formTypeRepository, times(1)).delete(any(FormType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormDataByDate(java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer, java.lang.String, boolean)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void shouldGetFormDataByDate() {
	Page<FormData> values = new PageImpl<>(Arrays.asList(ronData, harryData));
	when(formDataRepository.findByDateRange(anyVararg(), anyVararg(), anyVararg())).thenReturn(values);
	List<FormData> list = formService.getFormDataByDate(ronData.getFormDate(), harryData.getFormDate(), 1, 1,
		"formDate", Boolean.TRUE);
	assertThat(list, Matchers.containsInAnyOrder(ronData, harryData));
	verify(formDataRepository, times(1)).findByDateRange(anyVararg(), anyVararg(), anyVararg());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormDataById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetFormDataById() {
	Optional<FormData> ronDataObj = Optional.of(ronData);
	when(formDataRepository.findById(any(Integer.class))).thenReturn(ronDataObj);
	assertEquals(formService.getFormDataById(1), ronData);
	verify(formDataRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormDataByReferenceId(java.lang.String)}.
     */
    @Test
    public void shouldGetFormDataByReferenceId() {
	Optional<FormData> ronDataObj = Optional.of(ronData);
	when(formDataRepository.findByReference(any(String.class))).thenReturn(ronDataObj);
	assertEquals(formService.getFormDataByReferenceId(ronData.getReferenceId()), ronData);
	verify(formDataRepository, times(1)).findByReference(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormDataByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetFormDataByUuid() {
	when(formDataRepository.findByUuid(any(String.class))).thenReturn(ronData);
	assertEquals(formService.getFormDataByUuid(ronData.getUuid()), ronData);
	verify(formDataRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormTypeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetFormTypeById() {
	Optional<FormType> quidditchFormObj = Optional.of(quidditchForm);
	when(formTypeRepository.findById(any(Integer.class))).thenReturn(quidditchFormObj);
	assertEquals(formService.getFormTypeById(1), quidditchForm);
	verify(formTypeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormTypeByName(java.lang.String)}.
     */
    @Test
    public void shouldGetFormTypeByName() {
	when(formTypeRepository.findByFormName(any(String.class))).thenReturn(quidditchForm);
	assertEquals(formService.getFormTypeByName(quidditchForm.getFormName()), quidditchForm);
	verify(formTypeRepository, times(1)).findByFormName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormTypeByName(java.lang.String)}.
     */
    @Test
    public void shouldGetFormTypeByShortName() {
	when(formTypeRepository.findByShortName(any(String.class))).thenReturn(quidditchForm);
	assertEquals(formService.getFormTypeByName(quidditchForm.getShortName()), quidditchForm);
	verify(formTypeRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getFormTypeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetFormTypeByUuid() {
	when(formTypeRepository.findByUuid(any(String.class))).thenReturn(quidditchForm);
	assertEquals(formService.getFormTypeByUuid(quidditchForm.getUuid()), quidditchForm);
	verify(formTypeRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getAllFormTypes(boolean)}.
     */
    @Test
    public void shouldGetFormTypes() {
	when(formTypeRepository.findAll()).thenReturn(Arrays.asList(quidditchForm));
	assertEquals(1, formService.getAllFormTypes(true).size());
	verify(formTypeRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#getAllFormTypes(boolean)}.
     */
    @Test
    public void shouldGetFormTypesUnretired() {
	when(formTypeRepository.findNonRetired()).thenReturn(Arrays.asList(quidditchForm));
	assertEquals(1, formService.getAllFormTypes(false).size());
	verify(formTypeRepository, times(1)).findNonRetired();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#saveFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test(expected = HibernateException.class)
    public void shouldNotSaveFormData() throws HibernateException, ValidationException, IOException {
	when(formDataRepository.findByUuid(any(String.class))).thenReturn(null);
	when(formDataRepository.findByReference(any(String.class))).thenReturn(Optional.of(harryData));
	doNothing().when(validationService).validateFormData(any(FormData.class), any(DataEntity.class));
	formService.saveFormData(harryData);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#retireFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     */
    @Test
    public void shouldRetireFormType() {
	doNothing().when(formTypeRepository).softDelete(any(FormType.class));
	formService.retireFormType(quidditchForm);
	verify(formTypeRepository, times(1)).softDelete(any(FormType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#saveFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldSaveFormData() throws HibernateException, ValidationException, IOException {
	when(formDataRepository.findByUuid(any(String.class))).thenReturn(null);
	doNothing().when(validationService).validateFormData(any(FormData.class), any(DataEntity.class));
	when(formDataRepository.save(any(FormData.class))).thenReturn(harryData);
	assertThat(formService.saveFormData(harryData), is(harryData));
	verify(formDataRepository, times(1)).findByUuid(any(String.class));
	verify(validationService, times(1)).validateFormData(any(FormData.class), any(DataEntity.class));
	verify(formDataRepository, times(1)).save(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#saveFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     * 
     * @throws JSONException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldSaveFormType() throws HibernateException, ValidationException, JSONException {
	when(formTypeRepository.findByUuid(any(String.class))).thenReturn(null);
	when(validationService.validateFormType(any(FormType.class))).thenReturn(Boolean.TRUE);
	when(formTypeRepository.save(any(FormType.class))).thenReturn(quidditchForm);
	assertThat(formService.saveFormType(quidditchForm), is(quidditchForm));
	verify(formTypeRepository, times(1)).findByUuid(any(String.class));
	verify(validationService, times(1)).validateFormType(any(FormType.class));
	verify(formTypeRepository, times(1)).save(any(FormType.class));
	verifyNoMoreInteractions(formTypeRepository);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#searchFormData(com.ihsinformatics.coronavirus.model.FormType, com.ihsinformatics.coronavirus.model.Location, java.lang.Integer, java.lang.Integer, java.lang.String, boolean)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void shouldSearchFormData() {
	Page<FormData> values = new PageImpl<>(Arrays.asList(harryData, ronData));
	when(formDataRepository.search(any(FormType.class), any(Location.class), any(Date.class), any(Date.class), any(Pageable.class)))
		.thenReturn(values);
	List<FormData> list = formService.searchFormData(quidditchForm, hogwartz, DateTimeUtil.create(1, 1, 1995),
		DateTimeUtil.create(31, 12, 1995), 1, 10, "formDate", true);
	assertThat(list, Matchers.containsInAnyOrder(ronData, harryData));
	verify(formDataRepository, times(1)).search(any(FormType.class), any(Location.class), any(Date.class), any(Date.class), any(Pageable.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#unretireFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     * 
     * @throws JSONException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnretireFormType() throws HibernateException, ValidationException, JSONException {
	quidditchForm.setIsRetired(true);
	quidditchForm.setReasonRetired("Testing");
	when(validationService.validateFormType(any(FormType.class))).thenReturn(true);
	when(formTypeRepository.save(any(FormType.class))).thenReturn(quidditchForm);
	formService.unretireFormType(quidditchForm);
	verify(formTypeRepository, times(1)).save(any(FormType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#unvoidFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnvoidFormData() throws HibernateException, ValidationException, IOException {
	ronData.setIsVoided(true);
	ronData.setReasonVoided("Testing");
	doNothing().when(validationService).validateFormData(any(FormData.class), any(DataEntity.class));
	when(formDataRepository.save(any(FormData.class))).thenReturn(ronData);
	formService.unvoidFormData(ronData);
	verify(formDataRepository, times(1)).save(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#updateFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateFormData() throws HibernateException, ValidationException, IOException {
	doNothing().when(validationService).validateFormData(any(FormData.class), any(DataEntity.class));
	when(formDataRepository.save(any(FormData.class))).thenReturn(ronData);
	ronData = formService.updateFormData(ronData);
	assertNotNull(ronData.getDateUpdated());
	//verify(validationService, times(1)).validateFormData(any(FormData.class), any(DataEntity.class));
	verify(formDataRepository, times(1)).save(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#updateFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     * 
     * @throws JSONException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUpdateFormType() throws HibernateException, ValidationException, JSONException {
	when(validationService.validateFormType(any(FormType.class))).thenReturn(Boolean.TRUE);
	when(formTypeRepository.save(any(FormType.class))).thenReturn(quidditchForm);
	quidditchForm = formService.updateFormType(quidditchForm);
	assertNotNull(quidditchForm.getDateUpdated());
	verify(validationService, times(1)).validateFormType(any(FormType.class));
	verify(formTypeRepository, times(1)).save(any(FormType.class));
	verifyNoMoreInteractions(formDataRepository);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.FormServiceImpl#voidFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     */
    @Test
    public void shouldVoidFormData() {
	doNothing().when(formDataRepository).softDelete(any(FormData.class));
	formService.voidFormData(ronData);
	verify(formDataRepository, times(1)).softDelete(any(FormData.class));
    }
}
