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
import com.ihsinformatics.coronavirus.dto.ParticipantDesearlizeDto;
import com.ihsinformatics.coronavirus.model.BaseEntity;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.service.ParticipantService;
import com.ihsinformatics.coronavirus.service.PersonService;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.web.ParticipantController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class ParticipantControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private ParticipantService participantService;

    @Mock
    private LocationService locationService;
    
    @Mock
    private MetadataService metadataService;
    
    @Mock
    private PersonService personService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private DonorService donorService;

    @InjectMocks
    private ParticipantController participantController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(participantController).alwaysDo(MockMvcResultHandlers.print())
		.build();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#createParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldCreateParticipant() throws Exception {
	when(participantService.saveParticipant(any(Participant.class))).thenReturn(seeker);
	String content = BaseEntity.getGson().toJson(seeker);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "participant")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "participant/" + seeker.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(participantService, times(1)).saveParticipant(any(Participant.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#deleteParticipant(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldDeleteParticipant() throws Exception {
	when(participantService.getParticipantByUuid(any(String.class))).thenReturn(seeker);
	doNothing().when(participantService).voidParticipant(seeker);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "participant/{uuid}?reasonVoided=Test123", seeker.getUuid()));
	verify(participantService, times(1)).getParticipantByUuid(seeker.getUuid());
	verify(participantService, times(1)).voidParticipant(seeker);
	verifyNoMoreInteractions(participantService);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#getParticipant(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetParticipant() throws Exception {
	when(participantService.getParticipantByUuid(any(String.class))).thenReturn(seeker);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "participant/{uuid}", seeker.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(seeker.getUuid())));
	verify(participantService, times(1)).getParticipantByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#getPeopleByName(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetParticipantsByName() throws Exception {
	when(participantService.getParticipantsByName(any(String.class))).thenReturn(Arrays.asList(seeker));
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "participant/name/{name}", seeker.getPerson().getFirstName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(participantService, times(1)).getParticipantsByName(any(String.class));
	verifyNoMoreInteractions(participantService);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#getPeopleById(java.lang.Integer)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetParticipantsById() throws Exception {
	when(participantService.getParticipantById(any(Integer.class))).thenReturn(seeker);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "participant/id/{id}", 2));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(seeker.getUuid())));
	verify(participantService, times(1)).getParticipantById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#updateParticipant(java.lang.String, com.ihsinformatics.coronavirus.model.Participant)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUpdateParticipant() throws Exception {
	when(participantService.getParticipantByUuid(any(String.class))).thenReturn(seeker);
	when(participantService.updateParticipant(any(Participant.class))).thenReturn(seeker);
	String content = BaseEntity.getGson().toJson(seeker);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "participant/{uuid}", seeker.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(participantService, times(1)).getParticipantByUuid(any(String.class));
	verify(participantService, times(1)).updateParticipant(any(Participant.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#getParticipantByIdentifier(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetParticipantByIdentifier() throws Exception {
	when(participantService.getParticipantByIdentifier(any(String.class))).thenReturn(seeker);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "participant/identifier/{uuid}", seeker.getIdentifier()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(seeker.getUuid())));
	verify(participantService, times(1)).getParticipantByIdentifier(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#getParticipantsByLocation(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetParticipantsByLocation() throws Exception {
	when(locationService.getLocationByUuid(any(String.class))).thenReturn(hogwartz);
	when(participantService.getParticipantsByLocation(any(Location.class)))
		.thenReturn(Arrays.asList(seeker, keeper, chaser));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "participants/location/{uuid}", hogwartz.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
	verify(locationService, times(1)).getLocationByUuid(any(String.class));
	verify(participantService, times(1)).getParticipantsByLocation(any(Location.class));
	verifyNoMoreInteractions(participantService);
    }
    
   /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.LocationController#getParticipantDesearlizeDto(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetParticipantDesearlizeDto() throws Exception {
    	
    ParticipantDesearlizeDto partDto = new ParticipantDesearlizeDto(seeker.getParticipantId(), seeker.getUuid(), seeker.getLocation(), seeker.getIdentifier(), seeker.getPerson().getDob(), seeker.getPerson().getFirstName(), seeker.getPerson().getGender(), null);	
	when(participantService.getParticipantDesearlizeDtoUuid(any(String.class), any(LocationService.class), any(MetadataService.class), any(UserService.class), any(DonorService.class))).thenReturn(partDto);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "participant/full/{uuid}", seeker.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.participantId", Matchers.is(seeker.getParticipantId())));
	verify(participantService, times(1)).getParticipantDesearlizeDtoUuid(any(String.class),any(LocationService.class), any(MetadataService.class), any(UserService.class), any(DonorService.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ParticipantController#unvoidParticipant(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnvoidParticipant() throws Exception {
	when(participantService.getParticipantByUuid(any(String.class))).thenReturn(seeker);
	when(participantService.unvoidParticipant(any(Participant.class)))
	.thenReturn(seeker);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "participant/{uuid}", seeker.getUuid()));
	verify(participantService, times(1)).getParticipantByUuid(seeker.getUuid());
	verify(participantService, times(1)).unvoidParticipant(seeker);
	verifyNoMoreInteractions(participantService);
    }
}
