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
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.web.DonorController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class DonorControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private DonorService donorService;

    @InjectMocks
    private DonorController donorController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(donorController).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#createDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateDonor() throws Exception {
	when(donorService.saveDonor(any(Donor.class))).thenReturn(ministry);
	String content = BaseEntity.getGson().toJson(ministry);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "donor")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "donor/" + ministry.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(donorService, times(1)).saveDonor(any(Donor.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#deleteDonor(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteDonor() throws Exception {
	when(donorService.getDonorByUuid(any(String.class))).thenReturn(ministry);
	doNothing().when(donorService).voidDonor(ministry);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "donor/{uuid}?reasonVoided=Test123", ministry.getUuid()));
	verify(donorService, times(1)).getDonorByUuid(ministry.getUuid());
	verify(donorService, times(1)).voidDonor(ministry);
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getDonor(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonor() throws Exception {
	when(donorService.getDonorByUuid(any(String.class))).thenReturn(ministry);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "donor/{uuid}", ministry.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(ministry.getShortName())));
	verify(donorService, times(1)).getDonorByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.MetadataController#getDonorById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonorById() throws Exception {
	when(donorService.getDonorById(any(Integer.class))).thenReturn(ministry);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "donor/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(ministry.getShortName())));
	verify(donorService, times(1)).getDonorById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getDonorByShortName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonorByShortName() throws Exception {
	when(donorService.getDonorByShortName(any(String.class))).thenReturn(ministry);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "donor/shortname/{shortName}", ministry.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(ministry.getShortName())));
	verify(donorService, times(1)).getDonorByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getDonors()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonors() throws Exception {
	when(donorService.getAllDonors()).thenReturn(Arrays.asList(ministry));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "donors"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getAllDonors();
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#getDonorsByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonorsByName() throws Exception {
	when(donorService.getDonorsByName(any(String.class))).thenReturn(Arrays.asList(ministry));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "donors/name/{name}", ministry.getDonorName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(donorService, times(1)).getDonorsByName(any(String.class));
	verifyNoMoreInteractions(donorService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#updateDonor(java.lang.String, com.ihsinformatics.coronavirus.model.Donor)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateDonor() throws Exception {
	when(donorService.getDonorByUuid(any(String.class))).thenReturn(ministry);
	when(donorService.updateDonor(any(Donor.class))).thenReturn(ministry);
	String content = BaseEntity.getGson().toJson(dumbledore);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "donor/{uuid}", ministry.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(donorService, times(1)).getDonorByUuid(any(String.class));
	verify(donorService, times(1)).updateDonor(any(Donor.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.DonorController#unvoidDonor(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnvoidDonor() throws Exception {
	when(donorService.getDonorByUuid(any(String.class))).thenReturn(ministry);
	when(donorService.unvoidDonor(any(Donor.class))).thenReturn(ministry);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "donor/{uuid}", ministry.getUuid()));
	verify(donorService, times(1)).getDonorByUuid(ministry.getUuid());
	verify(donorService, times(1)).unvoidDonor(ministry);
	verifyNoMoreInteractions(donorService);
    }
}
