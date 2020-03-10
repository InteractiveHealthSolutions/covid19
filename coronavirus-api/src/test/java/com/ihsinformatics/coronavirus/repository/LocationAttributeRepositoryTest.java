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
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.repository.LocationAttributeRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LocationAttributeRepositoryTest extends BaseRepositoryData {

    @Autowired
    private LocationAttributeRepository locationAttributeRepository;

    @Before
    public void reset() {
	super.reset();
	try {
	    locationType = entityManager.persist(locationType);
	    entityManager.flush();
	    initDefinitions();
	    school = entityManager.persist(school);
	    market = entityManager.persist(market);
	    entityManager.flush();
	    initLocationAttributeTypes();
	    noOfStudents = entityManager.persist(noOfStudents);
	    noOfTeachers = entityManager.persist(noOfTeachers);
	    entityManager.flush();
	    initLocations();
	    hogwartz = entityManager.persist(hogwartz);
	    diagonalley = entityManager.persist(diagonalley);
	    entityManager.flush();
	    initLocationAttributes();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void shouldDelete() {
	noOfHogwartzStudents = entityManager.persist(noOfHogwartzStudents);
	entityManager.flush();
	Integer id = noOfHogwartzStudents.getAttributeId();
	entityManager.detach(noOfHogwartzStudents);
	locationAttributeRepository.delete(noOfHogwartzStudents);
	LocationAttribute found = entityManager.find(LocationAttribute.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByalues() {
	for (LocationAttribute attributes : Arrays.asList(noOfHogwartzStudents, noOfDiagonalleyTeachers,
		noOfHogwartzTeachers)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<LocationAttribute> found = locationAttributeRepository.findByValue("NO_VALUE");
	assertTrue(found.isEmpty());
	found = locationAttributeRepository.findByValue(noOfHogwartzStudents.getAttributeValueAsObject().toString());
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByAttributeType() {
	for (LocationAttribute attributes : Arrays.asList(noOfDiagonalleyTeachers, noOfHogwartzTeachers)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<LocationAttribute> found = locationAttributeRepository.findByAttributeType(noOfStudents);
	assertTrue(found.isEmpty());
	found = locationAttributeRepository.findByAttributeType(noOfTeachers);
	assertEquals(2, found.size());

    }

    @Test
    public void shouldFindByAttributeTypeAndValue() {
	for (LocationAttribute attributes : Arrays.asList(noOfHogwartzStudents, noOfDiagonalleyTeachers,
		noOfHogwartzTeachers)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<LocationAttribute> found = locationAttributeRepository.findByAttributeTypeAndValue(noOfStudents,
		"NOTHING");
	assertTrue(found.isEmpty());
	found = locationAttributeRepository.findByAttributeTypeAndValue(noOfStudents,
		noOfHogwartzStudents.getAttributeValueAsObject().toString());
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(noOfHogwartzStudents);
	entityManager.flush();
	entityManager.detach(noOfHogwartzStudents);
	Optional<LocationAttribute> found = locationAttributeRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByLocation() {
	for (LocationAttribute attributes : Arrays.asList(noOfHogwartzStudents, noOfHogwartzTeachers,
		noOfDiagonalleyTeachers)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<LocationAttribute> found = locationAttributeRepository.findByLocation(hogwartz);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindByLocationAndAttributeType() {
	for (LocationAttribute attributes : Arrays.asList(noOfHogwartzStudents, noOfDiagonalleyTeachers,
		noOfHogwartzTeachers)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<LocationAttribute> found = locationAttributeRepository.findByLocationAndAttributeType(diagonalley,
		noOfStudents);
	assertTrue(found.isEmpty());
	found = locationAttributeRepository.findByLocationAndAttributeType(hogwartz, noOfStudents);
	assertEquals(1, found.size());

    }

    @Test
    public void shouldFindByUuid() throws Exception {
	noOfHogwartzStudents = entityManager.persist(noOfHogwartzStudents);
	entityManager.flush();
	String uuid = noOfHogwartzStudents.getUuid();
	entityManager.detach(noOfHogwartzStudents);
	LocationAttribute found = locationAttributeRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	noOfHogwartzStudents = locationAttributeRepository.save(noOfHogwartzStudents);
	locationAttributeRepository.flush();
	LocationAttribute found = entityManager.find(LocationAttribute.class, noOfHogwartzStudents.getAttributeId());
	assertNotNull(found);
    }
}
