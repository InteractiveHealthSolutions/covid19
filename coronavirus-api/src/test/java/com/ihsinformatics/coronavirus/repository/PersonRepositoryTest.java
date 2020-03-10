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
import static org.junit.Assert.assertFalse;
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
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.repository.PersonRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonRepositoryTest extends BaseRepositoryData {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void reset() {
	super.reset();
	height = entityManager.persist(height);
	socialStatus = entityManager.persist(socialStatus);
	entityManager.flush();
	initPeople();
	initPersonAttributeTypes();
	initPersonAttributes();
    }

    @Test
    public void shouldDelete() {
	harry = entityManager.persist(harry);
	entityManager.flush();
	Integer id = harry.getPersonId();
	entityManager.detach(harry);
	personRepository.delete(harry);
	Person found = entityManager.find(Person.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByAddress() {
	// Save some people
	harry.setAddress1("Under to stairs");
	harry.setStateProvince("Surrey");
	harry.setAddress2("The Burrows");
	hermione.setAddress1("Some muggle house");
	for (Person person : Arrays.asList(harry, ron, hermione)) {
	    entityManager.persist(person);
	    entityManager.flush();
	    entityManager.detach(person);
	}
	// Should be empty
	List<Person> found = personRepository.findByAddress("Ibhrahim Trade Towers", "HBL", "Karachi", "Sindh",
		"Pakistan");
	assertTrue(found.isEmpty());
	// Should return 2 objects
	found = personRepository.findByAddress(null, null, null, null, harry.getCountry());
	assertEquals(2, found.size());
	// Should return 1 object
	found = personRepository.findByAddress("muggle house", null, null, null, null);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByContacts() {
	// Save some people
	for (Person person : Arrays.asList(harry, ron, hermione)) {
	    entityManager.persist(person);
	    entityManager.flush();
	    entityManager.detach(person);
	}
	// Should be empty
	List<Person> found = personRepository.findByContact("0211234567", true);
	assertTrue(found.isEmpty());
	// Should return 1 object
	found = personRepository.findByContact(harry.getPrimaryContact(), true);
	assertEquals(1, found.size());
	// Should return 2 objects
	found = personRepository.findByContact(harry.getPrimaryContact(), false);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindByCountry() {
	// Save some people
	harry.setCountry(england.getDefinitionName());
	harry.setAddress1("Under to stairs");
	entityManager.persist(harry);
	entityManager.flush();
	entityManager.detach(harry);
	// Should return harry
	List<Person> found = personRepository.findByAddress("", null, null, null, harry.getCountry());
	assertFalse(found.isEmpty());
	Person first = found.get(0);
	assertEquals(harry.toString(), first.toString());
    }

    @Test
    public void shouldFindByFullName() {
	// Save some people
	for (Person person : Arrays.asList(harry, ron, hermione)) {
	    entityManager.persist(person);
	    entityManager.flush();
	    entityManager.detach(person);
	}
	// Should be empty
	List<Person> found = personRepository.findByPersonName(null, null, null);
	assertTrue(found.isEmpty());
	// Should return 1 object
	found = personRepository.findByPersonName("Ronald", null, null);
	assertEquals(1, found.size());
	// Should return 2 objects
	found = personRepository.findByPersonName("Hermione", "Weasley", null);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(harry);
	entityManager.flush();
	entityManager.detach(harry);
	Optional<Person> found = personRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	hermione = entityManager.persist(hermione);
	entityManager.flush();
	String uuid = hermione.getUuid();
	entityManager.detach(hermione);
	Person found = personRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	harry = personRepository.save(harry);
	personRepository.flush();
	Person found = entityManager.find(Person.class, harry.getPersonId());
	assertNotNull(found);
    }

    @Test
    public void shouldSaveWithAttributes() {
	harry = personRepository.save(harry);
	personRepository.flush();
	Person found = entityManager.find(Person.class, harry.getPersonId());
	assertNotNull(found);
    }
}
