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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hamcrest.Matchers;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.service.ValidationServiceImpl;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class ParticipantServiceTest extends BaseServiceTest {

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
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#deleteParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     */
    @Test
    public void shouldDeleteParticipant() {
	when(participantRepository.findById(any(Integer.class))).thenReturn(null);
	doNothing().when(participantRepository).delete(any(Participant.class));
	participantService.deleteParticipant(seeker);
	verify(participantRepository, times(1)).delete(any(Participant.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetParticipantById() {
	Optional<Participant> attributeObj = Optional.of(seeker);
	when(participantRepository.findById(any(Integer.class))).thenReturn(attributeObj);
	assertThat(participantService.getParticipantById(1), is(seeker));
	verify(participantRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantByIdentifier(java.lang.String)}.
     */
    @Test
    public void shouldGetParticipantByIdentifier() {
	when(participantRepository.findByIdentifier(any(String.class))).thenReturn(seeker);
	assertThat(participantService.getParticipantByIdentifier(seeker.getIdentifier()), is(seeker));
	verify(participantRepository, times(1)).findByIdentifier(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetParticipantByUuid() {
	when(participantRepository.findByUuid(any(String.class))).thenReturn(seeker);
	assertThat(participantService.getParticipantByUuid(seeker.getUuid()), is(seeker));
	verify(participantRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantsByLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldGetParticipantsByLocation() {
	when(participantRepository.findByLocation(any(Location.class))).thenReturn(Arrays.asList(seeker, keeper));
	assertThat(participantService.getParticipantsByLocation(hogwartz), Matchers.containsInAnyOrder(seeker, keeper));
	verify(participantRepository, times(1)).findByLocation(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetParticipantsByName() {
	seeker.getPerson().setPersonId(100);
	when(participantRepository.findById(any(Integer.class))).thenReturn(Optional.of(seeker));
	when(personRepository.findByPersonName(any(String.class), any(String.class), any(String.class)))
		.thenReturn(Arrays.asList(seeker.getPerson()));
	List<Participant> list = participantService.getParticipantsByName(seeker.getPerson().getFirstName());
	assertEquals(1, list.size());
	assertThat(list, Matchers.contains(seeker));
	verify(personRepository, times(1)).findByPersonName(any(String.class), any(String.class), any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#getParticipantsByName(java.lang.String)}.
     */
    @Test
    public void shouldNotGetParticipantsByName() {
	assertThat(participantService.getParticipantsByName("admin"), is(Collections.emptyList()));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#saveParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     */
    @Test
    public void shouldSaveParticipant() {
	when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(hogwartz));
	when(personRepository.findByUuid(any(String.class))).thenReturn(null);
	when(personRepository.save(any(Person.class))).thenReturn(harry);
	when(participantRepository.findByIdentifier(any(String.class))).thenReturn(null);
	hogwartz.setLocationId(1);
	seeker.setLocation(hogwartz);
	when(participantRepository.save(any(Participant.class))).thenReturn(seeker);
	assertThat(participantService.saveParticipant(seeker), is(seeker));
	verify(locationRepository, times(1)).findById(any(Integer.class));
	verify(personRepository, times(1)).findByUuid(any(String.class));
	verify(personRepository, times(1)).save(any(Person.class));
	verify(participantRepository, times(1)).save(any(Participant.class));
	verify(participantRepository, times(1)).findByIdentifier(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#updateParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     */
    @Test
    public void shouldUpdateParticipant() {
	when(participantRepository.save(any(Participant.class))).thenReturn(seeker);
	seeker = participantService.updateParticipant(seeker);
	assertNotNull(seeker.getDateUpdated());
	verify(participantRepository, times(1)).save(any(Participant.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantImpl#unvoidParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnvoidParticipant() throws HibernateException, ValidationException, IOException {
	seeker.setIsVoided(true);
	seeker.setReasonVoided("Testing");
	
	List<PersonAttribute> personAttributes = Arrays.asList(ronHeight, ronSocialStatus);
	ron.setAttributes(personAttributes);
	seeker.setPerson(ron);
	
	when(participantRepository.save(any(Participant.class))).thenReturn(seeker);
	participantService.unvoidParticipant(seeker);
	verify(participantRepository, times(1)).save(any(Participant.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ParticipantServiceImpl#voidParticipant(com.ihsinformatics.coronavirus.model.Participant)}.
     */
    @Test
    public void shouldVoidParticipant() {
	doNothing().when(participantRepository).softDelete(any(Participant.class));
	List<PersonAttribute> personAttributes = Arrays.asList(ronHeight, ronSocialStatus);
	ron.setAttributes(personAttributes);
	seeker.setPerson(ron);
	participantService.voidParticipant(seeker);
	verify(participantRepository, times(1)).softDelete(any(Participant.class));
    }

}
