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
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.repository.PersonAttributeRepository;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonAttributeRepositoryTest extends BaseRepositoryData {

    @Autowired
    private PersonAttributeRepository personAttributeRepository;

    @Before
    public void reset() {
	super.reset();
	height = entityManager.persist(height);
	socialStatus = entityManager.persist(socialStatus);
	initPeople();
	harry = entityManager.persist(harry);
	ron = entityManager.persist(ron);
	entityManager.flush();
	initPersonAttributes();
    }

    @Test
    public void shouldDelete() {
	ronHeight = entityManager.persist(ronHeight);
	entityManager.flush();
	Integer id = ronHeight.getAttributeId();
	entityManager.detach(ronHeight);
	personAttributeRepository.delete(ronHeight);
	PersonAttribute found = entityManager.find(PersonAttribute.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByalues() {
	for (PersonAttribute attributes : Arrays.asList(ronHeight, ronSocialStatus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<PersonAttribute> found = personAttributeRepository.findByValue("Pure Blood");
	assertTrue(found.isEmpty());
	found = personAttributeRepository.findByValue(ronSocialStatus.getAttributeValue());
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByAttributeType() {
	PersonAttributeType weight = PersonAttributeType.builder().dataType(DataType.FLOAT).attributeName("Weight")
		.shortName("WT").validationRegex("range=1-199").build();
	weight = entityManager.persist(weight);
	entityManager.flush();
	for (PersonAttribute attributes : Arrays.asList(ronHeight, ronSocialStatus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<PersonAttribute> found = personAttributeRepository.findByAttributeType(weight);
	assertTrue(found.isEmpty());
	found = personAttributeRepository.findByAttributeType(height);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByAttributeTypeAndValue() {
	for (PersonAttribute attributes : Arrays.asList(ronHeight, ronSocialStatus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<PersonAttribute> found = personAttributeRepository.findByAttributeTypeAndValue(socialStatus, "None");
	assertTrue(found.isEmpty());
	found = personAttributeRepository.findByAttributeTypeAndValue(socialStatus, "Married");
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(ronHeight);
	entityManager.flush();
	entityManager.detach(ronHeight);
	Optional<PersonAttribute> found = personAttributeRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByUser() {
	for (PersonAttribute attributes : Arrays.asList(ronHeight, ronSocialStatus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<PersonAttribute> found = personAttributeRepository.findByPerson(harry);
	assertTrue(found.isEmpty());
	found = personAttributeRepository.findByPerson(ron);
	assertEquals(2, found.size());

    }

    @Test
    public void shouldFindByUserAndAttributeType() {
	for (PersonAttribute attributes : Arrays.asList(ronHeight, ronSocialStatus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<PersonAttribute> found = personAttributeRepository.findByPersonAndAttributeType(harry, height);
	assertTrue(found.isEmpty());
	found = personAttributeRepository.findByPersonAndAttributeType(ron, socialStatus);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	ronHeight = entityManager.persist(ronHeight);
	entityManager.flush();
	String uuid = ronHeight.getUuid();
	entityManager.detach(ronHeight);
	PersonAttribute found = personAttributeRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	ronHeight = personAttributeRepository.save(ronHeight);
	personAttributeRepository.flush();
	PersonAttribute found = entityManager.find(PersonAttribute.class, ronHeight.getAttributeId());
	assertNotNull(found);
    }
}
