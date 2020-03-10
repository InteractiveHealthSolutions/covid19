/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import com.ihsinformatics.coronavirus.model.BaseEntity;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.service.PersonService;
import com.ihsinformatics.coronavirus.web.PersonController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class PersonControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(personController).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#createPerson(com.ihsinformatics.coronavirus.model.Person)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreatePerson() throws Exception {
	when(personService.savePerson(any(Person.class))).thenReturn(harry);
	String content = BaseEntity.getGson().toJson(quidditchForm);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "person")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "person/" + harry.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(personService, times(1)).savePerson(any(Person.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#createPersonAttribute(com.ihsinformatics.coronavirus.model.PersonAttribute)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreatePersonAttribute() throws Exception {
	when(personService.savePersonAttribute(any(PersonAttribute.class))).thenReturn(ronHeight);
	String content = BaseEntity.getGson().toJson(ronHeight);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "personattribute")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "personattribute/" + ronHeight.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(personService, times(1)).savePersonAttribute(any(PersonAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#createPersonAttributeType(com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreatePersonAttributeType() throws Exception {
	when(personService.savePersonAttributeType(any(PersonAttributeType.class))).thenReturn(socialStatus);
	String content = BaseEntity.getGson().toJson(socialStatus);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "personattributetype")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "personattributetype/" + socialStatus.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(personService, times(1)).savePersonAttributeType(any(PersonAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#deleteLocationAttributeType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteLocationAttributeType() throws Exception {
	ResultActions actions = mockMvc
		.perform(delete(API_PREFIX + "personattributetype/{uuid}", socialStatus.getUuid()));
	actions.andExpect(status().isNotImplemented());
	Mockito.verifyZeroInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#deletePerson(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeletePerson() throws Exception {
	when(personService.getPersonByUuid(any(String.class))).thenReturn(harry);
	doNothing().when(personService).deletePerson(harry);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "person/{uuid}", harry.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(personService, times(1)).getPersonByUuid(harry.getUuid());
	verify(personService, times(1)).deletePerson(harry);
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#deletePersonAttribute(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeletePersonAttribute() throws Exception {
	when(personService.getPersonAttributeByUuid(any(String.class))).thenReturn(ronHeight);
	doNothing().when(personService).deletePersonAttribute(ronHeight);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "personattribute/{uuid}", ronHeight.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(personService, times(1)).getPersonAttributeByUuid(ronHeight.getUuid());
	verify(personService, times(1)).deletePersonAttribute(ronHeight);
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPeopleByAddress(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPeopleByAddress() throws Exception {
	when(personService.getPeopleByAddress(any(String.class), any(String.class), any(String.class),
		any(String.class))).thenReturn(Arrays.asList(harry, ron));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "people/address").param("address", "")
		.param("cityVillage", "").param("stateProvince", "").param("country", england.getDefinitionName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	actions.andExpect(jsonPath("$[0].uuid", Matchers.is(harry.getUuid())));
	verify(personService, times(1)).getPeopleByAddress(any(String.class), any(String.class), any(String.class),
		any(String.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPeopleByContact(java.lang.String, java.lang.Boolean)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPeopleByContact() throws Exception {
	when(personService.getPeopleByContact(any(String.class), any(Boolean.class)))
		.thenReturn(Arrays.asList(harry, ron));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "people/contact").param("contact", "447911123456")
		.param("primaryContactOnly", "false"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	actions.andExpect(jsonPath("$[0].uuid", Matchers.is(harry.getUuid())));
	verify(personService, times(1)).getPeopleByContact(any(String.class), any(Boolean.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPeopleByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPeopleByName() throws Exception {
	when(personService.getPeopleByName(any(String.class))).thenReturn(Arrays.asList(harry));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "people/name/{name}", harry.getFirstName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(personService, times(1)).getPeopleByName(any(String.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPerson(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPerson() throws Exception {
	when(personService.getPersonByUuid(any(String.class))).thenReturn(harry);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "person/{uuid}", harry.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(harry.getUuid())));
	verify(personService, times(1)).getPersonByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttribute(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttribute() throws Exception {
	when(personService.getPersonAttributeByUuid(any(String.class))).thenReturn(ronHeight);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "personattribute/{uuid}", ronHeight.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.attributeValue", Matchers.equalToIgnoringCase(ronHeight.getAttributeValue())));
	verify(personService, times(1)).getPersonAttributeByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributesByLocation(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributesByPerson() throws Exception {
	when(personService.getPersonByUuid(any(String.class))).thenReturn(ron);
	when(personService.getPersonAttributesByPerson(any(Person.class)))
		.thenReturn(Arrays.asList(ronHeight, ronSocialStatus));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "personattributes/person/{uuid}", ron.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(personService, times(1)).getPersonByUuid(any(String.class));
	verify(personService, times(1)).getPersonAttributesByPerson(any(Person.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributeType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributeType() throws Exception {
	when(personService.getAllPersonAttributeTypes()).thenReturn(Arrays.asList(height, socialStatus));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "personattributetypes"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(personService, times(1)).getAllPersonAttributeTypes();
	verifyNoMoreInteractions(personService);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributeTypeByUuid(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributeTypeByUuid() throws Exception {
	when(personService.getPersonAttributeTypeByUuid(any(String.class))).thenReturn(height);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "personattributetype/{uuid}", height.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.shortName", Matchers.is(height.getShortName())));
	actions.andExpect(jsonPath("$.attributeName", Matchers.is(height.getAttributeName())));
	verify(personService, times(1)).getPersonAttributeTypeByUuid(any(String.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributeTypeByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributeTypeByName() throws Exception {
	when(personService.getPersonAttributeTypeByName(any(String.class))).thenReturn(height);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "personattributetype/name/{name}", height.getAttributeName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.shortName", Matchers.is(height.getShortName())));
	actions.andExpect(jsonPath("$.attributeName", Matchers.is(height.getAttributeName())));
	verify(personService, times(1)).getPersonAttributeTypeByName(any(String.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributeTypeByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributeTypeByShortName() throws Exception {
	when(personService.getPersonAttributeTypeByShortName(any(String.class))).thenReturn(height);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "personattributetype/shortname/{shortName}", height.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.shortName", Matchers.is(height.getShortName())));
	actions.andExpect(jsonPath("$.attributeName", Matchers.is(height.getAttributeName())));
	verify(personService, times(1)).getPersonAttributeTypeByShortName(any(String.class));
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonAttributeTypes()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonAttributeTypes() throws Exception {
	when(personService.getAllPersonAttributeTypes()).thenReturn(Arrays.asList(height, socialStatus));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "personattributetypes"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(personService, times(1)).getAllPersonAttributeTypes();
	verifyNoMoreInteractions(personService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#getPersonById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPersonById() throws Exception {
	when(personService.getPersonById(any(Integer.class))).thenReturn(harry);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "person/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(harry.getUuid())));
	verify(personService, times(1)).getPersonById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#updatePerson(java.lang.String, com.ihsinformatics.coronavirus.model.Person)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdatePerson() throws Exception {
	when(personService.getPersonByUuid(any(String.class))).thenReturn(ron);
	when(personService.updatePerson(any(Person.class))).thenReturn(ron);
	String content = BaseEntity.getGson().toJson(ron);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "person/{uuid}", ron.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(personService, times(1)).getPersonByUuid(any(String.class));
	verify(personService, times(1)).updatePerson(any(Person.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#updatePersonAttribute(java.lang.String, com.ihsinformatics.coronavirus.model.PersonAttribute)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdatePersonAttribute() throws Exception {
	when(personService.getPersonAttributeByUuid(any(String.class))).thenReturn(ronSocialStatus);
	when(personService.updatePersonAttribute(any(PersonAttribute.class))).thenReturn(ronSocialStatus);
	String content = BaseEntity.getGson().toJson(ronSocialStatus);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "personattribute/{uuid}", ronSocialStatus.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(personService, times(1)).getPersonAttributeByUuid(any(String.class));
	verify(personService, times(1)).updatePersonAttribute(any(PersonAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.PersonController#updatePersonAttributeType(java.lang.String, com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdatePersonAttributeType() throws Exception {
	when(personService.getPersonAttributeTypeByUuid(any(String.class))).thenReturn(height);
	when(personService.updatePersonAttributeType(any(PersonAttributeType.class))).thenReturn(height);
	String content = BaseEntity.getGson().toJson(hogwartz);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "personattributetype/{uuid}", height.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(personService, times(1)).getPersonAttributeTypeByUuid(any(String.class));
	verify(personService, times(1)).updatePersonAttributeType(any(PersonAttributeType.class));
    }
}
