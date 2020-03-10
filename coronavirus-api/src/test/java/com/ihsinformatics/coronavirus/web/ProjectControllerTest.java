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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Project;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.web.ProjectController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private DonorService donorService;

    @InjectMocks
    private ProjectController projectController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(projectController).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#createProject(com.ihsinformatics.coronavirus.model.Project)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateProject() throws Exception {
	when(donorService.saveProject(any(Project.class))).thenReturn(triwizardTournament);
	String content = BaseEntity.getGson().toJson(triwizardTournament);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "project")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "project/" + triwizardTournament.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(donorService, times(1)).saveProject(any(Project.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#deleteProject(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteProject() throws Exception {
	when(donorService.getProjectByUuid(any(String.class))).thenReturn(triwizardTournament);
	doNothing().when(donorService).voidProject(triwizardTournament);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "project/{uuid}?reasonVoided=Test123", triwizardTournament.getUuid()));
	verify(donorService, times(1)).getProjectByUuid(triwizardTournament.getUuid());
	verify(donorService, times(1)).voidProject(triwizardTournament);
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProject(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProject() throws Exception {
	when(donorService.getProjectByUuid(any(String.class))).thenReturn(triwizardTournament);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "project/{uuid}", triwizardTournament.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(triwizardTournament.getShortName())));
	verify(donorService, times(1)).getProjectByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getProjectById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectById() throws Exception {
	when(donorService.getProjectById(any(Integer.class))).thenReturn(triwizardTournament);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "project/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(triwizardTournament.getShortName())));
	verify(donorService, times(1)).getProjectById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProjectByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectByShortName() throws Exception {
	when(donorService.getProjectByShortName(any(String.class))).thenReturn(triwizardTournament);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "project/shortname/{shortName}", triwizardTournament.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(triwizardTournament.getShortName())));
	verify(donorService, times(1)).getProjectByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProjects()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjects() throws Exception {
	when(donorService.getAllProjects()).thenReturn(Arrays.asList(triwizardTournament));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "projects"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getAllProjects();
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProjectsByDonor(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectsByDonor() throws Exception {
	when(donorService.getDonorByUuid(any(String.class))).thenReturn(ministry);
	when(donorService.getProjectsByDonor(any(Donor.class))).thenReturn(Arrays.asList(triwizardTournament));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "projects/donor/{uuid}", ministry.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getDonorByUuid(any(String.class));
	verify(donorService, times(1)).getProjectsByDonor(any(Donor.class));
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProjectsByDonor(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectsByDonorShortName() throws Exception {
	when(donorService.getDonorByShortName(any(String.class))).thenReturn(ministry);
	when(donorService.getProjectsByDonor(any(Donor.class))).thenReturn(Arrays.asList(triwizardTournament));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "projects/donor/{uuid}", ministry.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getDonorByShortName(any(String.class));
	verify(donorService, times(1)).getProjectsByDonor(any(Donor.class));
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getProjectsByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectsByName() throws Exception {
	when(donorService.getProjectsByName(any(String.class))).thenReturn(Arrays.asList(triwizardTournament));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "projects/name/{name}", triwizardTournament.getProjectName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getProjectsByName(any(String.class));
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#updateProject(java.lang.String, com.ihsinformatics.coronavirus.model.Project)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateProject() throws Exception {
	when(donorService.getProjectByUuid(any(String.class))).thenReturn(triwizardTournament);
	when(donorService.updateProject(any(Project.class))).thenReturn(triwizardTournament);
	String content = BaseEntity.getGson().toJson(dumbledore);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "project/{uuid}", triwizardTournament.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(donorService, times(1)).getProjectByUuid(any(String.class));
	verify(donorService, times(1)).updateProject(any(Project.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ProjectController#unvoidProject(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnvoidProject() throws Exception {
	when(donorService.getProjectByUuid(any(String.class))).thenReturn(triwizardTournament);
	when(donorService.unvoidProject(any(Project.class))).thenReturn(triwizardTournament);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "project/{uuid}", triwizardTournament.getUuid()));
	verify(donorService, times(1)).getProjectByUuid(triwizardTournament.getUuid());
	verify(donorService, times(1)).unvoidProject(triwizardTournament);
	verifyNoMoreInteractions(donorService);
    }
}
