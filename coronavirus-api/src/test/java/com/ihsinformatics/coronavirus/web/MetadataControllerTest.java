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
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.web.MetadataController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class MetadataControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private MetadataService metadataService;

    @InjectMocks
    private MetadataController metadataController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(metadataController).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#createDefinition(com.ihsinformatics.coronavirus.model.Definition)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateDefinition() throws Exception {
	when(metadataService.saveDefinition(any(Definition.class))).thenReturn(scotland);
	String content = BaseEntity.getGson().toJson(scotland);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "definition")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "definition/" + scotland.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(metadataService, times(1)).saveDefinition(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#createDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateDefinitionType() throws Exception {
	when(metadataService.saveDefinitionType(any(DefinitionType.class))).thenReturn(country);
	String content = BaseEntity.getGson().toJson(scotland);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "definitiontype")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "definitiontype/" + country.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(metadataService, times(1)).saveDefinitionType(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#createElement(com.ihsinformatics.coronavirus.model.Element)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateElement() throws Exception {
	when(metadataService.saveElement(any(Element.class))).thenReturn(schoolElement);
	String content = BaseEntity.getGson().toJson(scotland);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "element")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "element/" + schoolElement.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(metadataService, times(1)).saveElement(any(Element.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#deleteDefinition(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteDefinition() throws Exception {
	when(metadataService.getDefinitionByUuid(any(String.class))).thenReturn(scotland);
	doNothing().when(metadataService).deleteDefinition(scotland);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "definition/{uuid}", scotland.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(metadataService, times(1)).getDefinitionByUuid(any(String.class));
	verify(metadataService, times(1)).deleteDefinition(any(Definition.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#deleteDefinitionType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteDefinitionType() throws Exception {
	when(metadataService.getDefinitionByUuid(any(String.class))).thenReturn(scotland);
	doNothing().when(metadataService).deleteDefinition(scotland);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "definition/{uuid}", scotland.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(metadataService, times(1)).getDefinitionByUuid(any(String.class));
	verify(metadataService, times(1)).deleteDefinition(any(Definition.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#deleteElement(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteElement() throws Exception {
	when(metadataService.getElementByUuid(any(String.class))).thenReturn(schoolElement);
	doNothing().when(metadataService).deleteElement(any(Element.class));
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "element/{uuid}", schoolElement.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(metadataService, times(1)).getElementByUuid(any(String.class));
	verify(metadataService, times(1)).deleteElement(any(Element.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinition(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinition() throws Exception {
	when(metadataService.getDefinitionByUuid(any(String.class))).thenReturn(scotland);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definition/{uuid}", scotland.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(scotland.getShortName())));
	verify(metadataService, times(1)).getDefinitionByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionById() throws Exception {
	when(metadataService.getDefinitionById(any(Integer.class))).thenReturn(scotland);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definition/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(scotland.getShortName())));
	verify(metadataService, times(1)).getDefinitionById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionByShortName() throws Exception {
	when(metadataService.getDefinitionByShortName(any(String.class))).thenReturn(Arrays.asList(scotland));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "definition/shortname/{shortName}", scotland.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getDefinitionByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionsByDefinitionType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionsByDefinitionType() throws Exception {
	when(metadataService.getDefinitionTypeByUuid(any(String.class))).thenReturn(country);
	when(metadataService.getDefinitionsByDefinitionType(any(DefinitionType.class)))
		.thenReturn(Arrays.asList(scotland));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "definitions/definitiontype/{uuid}", country.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getDefinitionTypeByUuid(any(String.class));
	verify(metadataService, times(1)).getDefinitionsByDefinitionType(any(DefinitionType.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionsByDefinitionType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionsByDefinitionTypeShortName() throws Exception {
	when(metadataService.getDefinitionTypeByShortName(any(String.class))).thenReturn(country);
	when(metadataService.getDefinitionsByDefinitionType(any(DefinitionType.class)))
		.thenReturn(Arrays.asList(scotland));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "definitions/definitiontype/{uuid}", country.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getDefinitionTypeByShortName(any(String.class));
	verify(metadataService, times(1)).getDefinitionsByDefinitionType(any(DefinitionType.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionsByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionsByName() throws Exception {
	when(metadataService.getDefinitionsByName(any(String.class))).thenReturn(Arrays.asList(scotland));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "definitions/name/{name}", scotland.getDefinitionName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getDefinitionsByName(any(String.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionType(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionType() throws Exception {
	when(metadataService.getDefinitionTypeByUuid(any(String.class))).thenReturn(country);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definitiontype/{uuid}", country.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(country.getShortName())));
	verify(metadataService, times(1)).getDefinitionTypeByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionTypeById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionTypeById() throws Exception {
	when(metadataService.getDefinitionTypeById(any(Integer.class))).thenReturn(country);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definitiontype/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(country.getShortName())));
	verify(metadataService, times(1)).getDefinitionTypeById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionTypeByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionTypeByShortName() throws Exception {
	when(metadataService.getDefinitionTypeByShortName(any(String.class))).thenReturn(country);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "definitiontype/shortname/{shortName}", country.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(country.getShortName())));
	verify(metadataService, times(1)).getDefinitionTypeByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionTypes()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionTypes() throws Exception {
	when(metadataService.getAllDefinitionTypes()).thenReturn(Arrays.asList(country, locationType));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definitiontypes"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(metadataService, times(1)).getAllDefinitionTypes();
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDefinitionTypesByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDefinitionTypesByName() throws Exception {
	when(metadataService.getDefinitionTypesByName(any(String.class))).thenReturn(Arrays.asList(country));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "definitiontypes/name/{name}", country.getTypeName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getDefinitionTypesByName(any(String.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getElement(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetElement() throws Exception {
	when(metadataService.getElementByUuid(any(String.class))).thenReturn(schoolElement);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "element/{uuid}", schoolElement.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(schoolElement.getShortName())));
	verify(metadataService, times(1)).getElementByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getElementById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetElementById() throws Exception {
	when(metadataService.getElementById(any(Integer.class))).thenReturn(schoolElement);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "element/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(schoolElement.getShortName())));
	verify(metadataService, times(1)).getElementById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getElementByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetElementByShortName() throws Exception {
	when(metadataService.getElementByShortName(any(String.class))).thenReturn(schoolElement);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "element/shortname/{shortName}", schoolElement.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(schoolElement.getShortName())));
	verify(metadataService, times(1)).getElementByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getElements()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetElements() throws Exception {
	when(metadataService.getAllElements()).thenReturn(Arrays.asList(schoolElement, broomstickElement));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "elements"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(metadataService, times(1)).getAllElements();
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getElementsByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetElementsByName() throws Exception {
	when(metadataService.getElementsByName(any(String.class))).thenReturn(Arrays.asList(schoolElement));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "elements/name/{name}", schoolElement.getElementName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(metadataService, times(1)).getElementsByName(any(String.class));
	verifyNoMoreInteractions(metadataService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#updateDefinition(java.lang.String, com.ihsinformatics.coronavirus.model.Definition)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateDefinition() throws Exception {
	when(metadataService.getDefinitionByUuid(any(String.class))).thenReturn(scotland);
	when(metadataService.updateDefinition(any(Definition.class))).thenReturn(scotland);
	String content = BaseEntity.getGson().toJson(scotland);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "definition/{uuid}", scotland.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(metadataService, times(1)).getDefinitionByUuid(any(String.class));
	verify(metadataService, times(1)).updateDefinition(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#updateDefinitionType(java.lang.String, com.ihsinformatics.coronavirus.model.DefinitionType)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateDefinitionType() throws Exception {
	when(metadataService.getDefinitionTypeByUuid(any(String.class))).thenReturn(country);
	when(metadataService.updateDefinitionType(any(DefinitionType.class))).thenReturn(country);
	String content = BaseEntity.getGson().toJson(country);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "definitiontype/{uuid}", country.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(metadataService, times(1)).getDefinitionTypeByUuid(any(String.class));
	verify(metadataService, times(1)).updateDefinitionType(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#updateElement(java.lang.String, com.ihsinformatics.coronavirus.model.Element)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateElement() throws Exception {
	when(metadataService.getElementByUuid(any(String.class))).thenReturn(schoolElement);
	when(metadataService.updateElement(any(Element.class))).thenReturn(schoolElement);
	String content = BaseEntity.getGson().toJson(schoolElement);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "element/{uuid}", schoolElement.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(metadataService, times(1)).getElementByUuid(any(String.class));
	verify(metadataService, times(1)).updateElement(any(Element.class));
    }
}
