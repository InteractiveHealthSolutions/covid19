/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyVararg;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.dto.FormDataDesearlizeDto;
import com.ihsinformatics.coronavirus.model.BaseEntity;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.FormService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.service.ParticipantService;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.web.FormController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class FormControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private FormService formService;

    @Mock
    private LocationService locationService;
    
    @Mock
    private ParticipantService participantService;
    
    @Mock
    private MetadataService metadataService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private DonorService donorService;
    
    @InjectMocks
    private FormController formController;

    private FormData quidditch95, quidditch98, drinkingChallenge, reverseFlightTraining;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(formController).alwaysDo(MockMvcResultHandlers.print()).build();

	List<Participant> participants = new ArrayList<>();
	participants.add(seeker);
	participants.add(keeper);
	quidditch95 = FormData.builder().formType(quidditchForm).location(hogwartz)
		.formDate(DateTimeUtil.create(15, 1, 1995)).referenceId("1995").formParticipants(participants).build();
	participants.add(chaser);
	quidditch98 = FormData.builder().formType(quidditchForm).location(hogwartz)
		.formDate(DateTimeUtil.create(1, 2, 1998)).referenceId("1998").formParticipants(participants).build();
	drinkingChallenge = FormData.builder().formType(challengeForm).location(diagonalley)
		.formDate(DateTimeUtil.create(20, 6, 1995)).referenceId("DALLEY_CH_24").build();
	reverseFlightTraining = FormData.builder().formType(trainingForm).location(diagonalley)
		.formDate(DateTimeUtil.create(1, 2, 1998)).referenceId("DALLEY_TR_144").formParticipants(participants)
		.build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#createFormData(com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateFormData() throws Exception {
	when(formService.saveFormData(any(FormData.class))).thenReturn(quidditch95);
	String content = BaseEntity.getGson().toJson(quidditch95);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "formdata")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "formdata/" + quidditch95.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(formService, times(1)).saveFormData(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#createFormType(com.ihsinformatics.coronavirus.model.FormType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateFormType() throws Exception {
	when(formService.saveFormType(any(FormType.class))).thenReturn(quidditchForm);
	String content = BaseEntity.getGson().toJson(quidditchForm);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "formtype")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "formtype/" + quidditchForm.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(formService, times(1)).saveFormType(any(FormType.class));
    }

    @Test
    public void shouldCreateReferenceId() throws Exception {
	dumbledore.setUserId(100);
	hogwartz.setLocationId(100);
	String referenceId = formController.createReferenceId(dumbledore, hogwartz, quidditch95.getFormDate());
	assertEquals("100-100-" + DateTimeUtil.toString(quidditch95.getFormDate(), DateTimeUtil.SQL_TIMESTAMP),
		referenceId);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormData(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormData() throws Exception {
	when(formService.getFormDataByUuid(any(String.class))).thenReturn(quidditch95);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/{uuid}", quidditch95.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(quidditch95.getUuid())));
	verify(formService, times(1)).getFormDataByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormDataByDateRange(java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("deprecation")
    public void shouldGetFormDataByDatePaging() throws Exception {
	when(formService.getFormDataByDate(anyVararg(), anyVararg(), anyInt(), anyInt(), anyString(), anyBoolean()))
		.thenReturn(Arrays.asList(reverseFlightTraining, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/date")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 1998)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 1998))).param("page", "1")
		.param("size", "10"));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(formService, times(1)).getFormDataByDate(anyVararg(), anyVararg(), anyInt(), anyInt(), anyString(),
		anyBoolean());
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormDataByDateRange(java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    @SuppressWarnings("deprecation")
    public void shouldGetFormDataByDate() throws Exception {
	when(formService.getFormDataByDate(anyVararg(), anyVararg()))
		.thenReturn(Arrays.asList(reverseFlightTraining, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/list/date")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 1998)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 1998))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(formService, times(1)).getFormDataByDate(anyVararg(), anyVararg());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormData(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormDataById() throws Exception {
	when(formService.getFormDataById(any(Integer.class))).thenReturn(quidditch95);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(quidditch95.getUuid())));
	verify(formService, times(1)).getFormDataById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormDataByLocation(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormDataByLocation() throws Exception {
	when(locationService.getLocationByUuid(any(String.class))).thenReturn(hogwartz);
	when(formService.getFormDataByLocation(any(Location.class)))
		.thenReturn(Arrays.asList(quidditch95, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/location/{uuid}", hogwartz.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(locationService, times(1)).getLocationByUuid(any(String.class));
	verify(formService, times(1)).getFormDataByLocation(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormDataByLocation(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormDataByLocationShortName() throws Exception {
	when(locationService.getLocationByShortName(any(String.class))).thenReturn(hogwartz);
	when(formService.getFormDataByLocation(any(Location.class)))
		.thenReturn(Arrays.asList(quidditch95, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/location/{uuid}", hogwartz.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(locationService, times(1)).getLocationByShortName(any(String.class));
	verify(formService, times(1)).getFormDataByLocation(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormDataByReferenceId(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormDataByReferenceId() throws Exception {
	when(formService.getFormDataByReferenceId(any(String.class))).thenReturn(drinkingChallenge);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "formdata/referenceid/{referenceId}", drinkingChallenge.getReferenceId()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.referenceId", Matchers.is(drinkingChallenge.getReferenceId())));
	verify(formService, times(1)).getFormDataByReferenceId(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormType() throws Exception {
	when(formService.getFormTypeByUuid(any(String.class))).thenReturn(quidditchForm);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formtype/{uuid}", quidditchForm.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(quidditchForm.getShortName())));
	verify(formService, times(1)).getFormTypeByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormTypeById() throws Exception {
	when(formService.getFormTypeById(any(Integer.class))).thenReturn(quidditchForm);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formtype/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(quidditchForm.getUuid())));
	verify(formService, times(1)).getFormTypeById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormTypeByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormTypeByShortName() throws Exception {
	when(formService.getFormTypeByName(any(String.class))).thenReturn(quidditchForm);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formtype/name/{name}", quidditchForm.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(quidditchForm.getShortName())));
	verify(formService, times(1)).getFormTypeByName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#getFormTypes()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormTypes() throws Exception {
	when(formService.getAllFormTypes(true)).thenReturn(Arrays.asList(quidditchForm, challengeForm, trainingForm));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formtypes"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
	verify(formService, times(1)).getAllFormTypes(true);
	verifyNoMoreInteractions(formService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#retireFormType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldRetireFormType() throws Exception {
	when(formService.getFormTypeByUuid(any(String.class))).thenReturn(challengeForm);
	doNothing().when(formService).retireFormType(challengeForm);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "formtype/{uuid}", challengeForm.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(formService, times(1)).getFormTypeByUuid(challengeForm.getUuid());
	verify(formService, times(1)).retireFormType(challengeForm);
	verifyNoMoreInteractions(formService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#searchFormData(com.ihsinformatics.coronavirus.model.FormType, com.ihsinformatics.coronavirus.model.Location, java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void shouldSearchFormData() throws Exception {
	when(formService.getFormTypeByUuid(anyString())).thenReturn(quidditchForm);
	when(locationService.getLocationByUuid(anyString())).thenReturn(hogwartz);
	when(formService.searchFormData(anyVararg(), anyVararg(), anyVararg(), anyVararg(), anyInt(), anyInt(),
		anyString(), anyBoolean())).thenReturn(Arrays.asList(quidditch95, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/search")
		.param("formType", quidditchForm.getUuid()).param("location", hogwartz.getUuid())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 1995)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 1998))).param("page", "1")
		.param("size", "10"));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(formService, times(1)).getFormTypeByUuid(anyString());
	verify(locationService, times(1)).getLocationByUuid(anyString());
	verify(formService, times(1)).searchFormData(anyVararg(), anyVararg(), anyVararg(), anyVararg(), anyInt(),
		anyInt(), anyString(), anyBoolean());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#searchFormData(com.ihsinformatics.coronavirus.model.FormType, com.ihsinformatics.coronavirus.model.Location, java.util.Date, java.util.Date, java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    @Test
    public void shouldSearchFormDataNonPaging() throws Exception {
	when(formService.getFormTypeByUuid(anyString())).thenReturn(quidditchForm);
	when(locationService.getLocationByUuid(anyString())).thenReturn(hogwartz);
	when(formService.searchFormData(anyVararg(), anyVararg(), anyVararg(), anyVararg(), anyVararg(),
		anyString(), anyBoolean())).thenReturn(Arrays.asList(quidditch95, quidditch98));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/list/search")
		.param("formType", quidditchForm.getUuid()).param("location", hogwartz.getUuid())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 1995)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 1998))).param("page", "1")
		.param("size", "10"));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(formService, times(1)).getFormTypeByUuid(anyString());
	verify(locationService, times(1)).getLocationByUuid(anyString());
	verify(formService, times(1)).searchFormData(anyVararg(), anyVararg(), anyVararg(), anyVararg(), anyVararg(), anyString(), anyBoolean());
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#unretireFormType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnretireFormType() throws Exception {
	when(formService.getFormTypeByUuid(any(String.class))).thenReturn(quidditchForm);
	doNothing().when(formService).unretireFormType(quidditchForm);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "formtype/{uuid}", quidditchForm.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(formService, times(1)).getFormTypeByUuid(quidditchForm.getUuid());
	verify(formService, times(1)).unretireFormType(quidditchForm);
	verifyNoMoreInteractions(formService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#unvoidFormData(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnvoidFormData() throws Exception {
	when(formService.getFormDataByUuid(any(String.class))).thenReturn(quidditch95);
	when(formService.unvoidFormData(any(FormData.class))).thenReturn(quidditch95);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "formdata/{uuid}", quidditch95.getUuid()));
	verify(formService, times(1)).getFormDataByUuid(quidditch95.getUuid());
	verify(formService, times(1)).unvoidFormData(quidditch95);
	verifyNoMoreInteractions(formService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#updateFormData(java.lang.String, com.ihsinformatics.coronavirus.model.FormData)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateFormData() throws Exception {
	when(formService.getFormDataByUuid(any(String.class))).thenReturn(quidditch95);
	when(formService.updateFormData(any(FormData.class))).thenReturn(quidditch95);
	String content = BaseEntity.getGson().toJson(quidditch95);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "formdata/{uuid}", quidditch95.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(formService, times(1)).getFormDataByUuid(any(String.class));
	verify(formService, times(1)).updateFormData(any(FormData.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#updateFormType(java.lang.String, com.ihsinformatics.coronavirus.model.FormType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateFormType() throws Exception {
	when(formService.updateFormType(any(FormType.class))).thenReturn(quidditchForm);
	String content = BaseEntity.getGson().toJson(quidditchForm);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "formtype/{uuid}", quidditchForm.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(formService, times(1)).updateFormType(any(FormType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.FormController#voidFormData(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldVoidFormData() throws Exception {
	when(formService.getFormDataByUuid(any(String.class))).thenReturn(quidditch95);
	doNothing().when(formService).voidFormData(quidditch95);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "formdata/{uuid}?reasonVoided=Test123", quidditch95.getUuid()));
	verify(formService, times(1)).getFormDataByUuid(quidditch95.getUuid());
	verify(formService, times(1)).voidFormData(quidditch95);
	verifyNoMoreInteractions(formService);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.LocationController#getLocationDesearlizeDto(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetFormDataDesearlizeDto() throws Exception {
    	
    FormDataDesearlizeDto fdDto = new FormDataDesearlizeDto(45, quidditch95.getUuid(), quidditch95.getFormType(), quidditch95.getLocation(), quidditch95.getFormDate(),
    		quidditch95.getReferenceId(), null, null);	
    
	when(formService.getFormDataDesearlizeDtoUuid(any(String.class), any(LocationService.class), any(ParticipantService.class), any(MetadataService.class), any(UserService.class), any(DonorService.class))).thenReturn(fdDto);
	
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "formdata/full/{uuid}", quidditch95.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(quidditch95.getUuid())));
	actions.andExpect(jsonPath("$.formId", Matchers.is(45)));
	verify(formService, times(1)).getFormDataDesearlizeDtoUuid(any(String.class), any(LocationService.class), any(ParticipantService.class), any(MetadataService.class), any(UserService.class), any(DonorService.class));
    
    }
}
