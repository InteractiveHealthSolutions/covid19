/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.repository.ParticipantRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ParticipantRepositoryTest extends BaseRepositoryData {

    @Autowired
    private ParticipantRepository participantRepository;

    @Before
    public void reset() {
	super.reset();
	locationType = entityManager.persist(locationType);
	entityManager.flush();
	initDefinitions();
	school = entityManager.persist(school);
	entityManager.flush();
	initLocations();
	hogwartz = entityManager.persist(hogwartz);
	entityManager.flush();
	harry = entityManager.persist(harry);
	ron = entityManager.persist(ron);
	entityManager.flush();
	initParticipants();
    }

    @Test
    public void shouldDelete() {
	seeker = entityManager.persist(seeker);
	entityManager.flush();
	Integer id = seeker.getParticipantId();
	entityManager.detach(seeker);
	participantRepository.delete(seeker);
	Participant found = entityManager.find(Participant.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(seeker);
	entityManager.flush();
	entityManager.detach(seeker);
	Optional<Participant> found = participantRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByIdentifier() {
	seeker = entityManager.persist(seeker);
	entityManager.flush();
	entityManager.detach(seeker);
	Participant found = participantRepository.findByIdentifier(seeker.getIdentifier());
	assertNotNull(found);
	assertEquals(found.getIdentifier(), seeker.getIdentifier());
    }

    @Test
    public void shouldFindByLocation() {
	for (Participant participant : Arrays.asList(seeker, keeper)) {
	    entityManager.persist(participant);
	    entityManager.flush();
	    entityManager.detach(participant);
	}
	List<Participant> found = participantRepository.findByLocation(hogwartz);
	assertNotNull(found);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	seeker = entityManager.persist(seeker);
	entityManager.flush();
	String uuid = seeker.getUuid();
	entityManager.detach(seeker);
	Participant found = participantRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	seeker = participantRepository.save(seeker);
	participantRepository.flush();
	Participant found = entityManager.find(Participant.class, seeker.getParticipantId());
	assertNotNull(found);
    }
}
