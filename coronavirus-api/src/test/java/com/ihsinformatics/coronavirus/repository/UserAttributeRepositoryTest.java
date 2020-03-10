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
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.repository.UserAttributeRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserAttributeRepositoryTest extends BaseRepositoryData {

    @Autowired
    private UserAttributeRepository userAttributeRepository;

    @Before
    public void reset() {
	super.reset();
	snape = entityManager.persist(snape);
	tonks = entityManager.persist(tonks);
	lily = entityManager.persist(lily);
	blood = entityManager.persist(blood);
	occupation = entityManager.persist(occupation);
	patronus = entityManager.persist(patronus);
	entityManager.flush();
	initUserAttributes();
    }

    @Test
    public void shouldDelete() {
	snapeBlood = entityManager.persist(snapeBlood);
	entityManager.flush();
	Integer id = snapeBlood.getAttributeId();
	entityManager.detach(snapeBlood);
	userAttributeRepository.delete(snapeBlood);
	UserAttribute found = entityManager.find(UserAttribute.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByAttributeType() {
	for (UserAttribute attributes : Arrays.asList(snapeBlood, tonksBlood)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<UserAttribute> found = userAttributeRepository.findByAttributeType(occupation);
	assertTrue(found.isEmpty());
	found = userAttributeRepository.findByAttributeType(blood);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindByAttributeTypeAndValue() {
	for (UserAttribute attributes : Arrays.asList(snapeBlood, tonksBlood, tonksPatronus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<UserAttribute> found = userAttributeRepository.findByAttributeTypeAndValue(blood, "Vampire Blood");
	assertTrue(found.isEmpty());
	found = userAttributeRepository.findByAttributeTypeAndValue(patronus, tonksPatronus.getAttributeValue());
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(snapeBlood);
	entityManager.flush();
	entityManager.detach(snapeBlood);
	Optional<UserAttribute> found = userAttributeRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByUser() {
	for (UserAttribute attributes : Arrays.asList(snapeBlood, tonksBlood)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<UserAttribute> found = userAttributeRepository.findByUser(lily);
	assertEquals(0, found.size());
	found = userAttributeRepository.findByUser(snape);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByUserAndAttributeType() {
	for (UserAttribute attributes : Arrays.asList(snapeBlood, tonksBlood, tonksPatronus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<UserAttribute> found = userAttributeRepository.findByUserAndAttributeType(snape, patronus);
	assertTrue(found.isEmpty());
	found = userAttributeRepository.findByUserAndAttributeType(tonks, patronus);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	snapeBlood = entityManager.persist(snapeBlood);
	entityManager.flush();
	String uuid = snapeBlood.getUuid();
	entityManager.detach(snapeBlood);
	UserAttribute found = userAttributeRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldFindByValues() {
	for (UserAttribute attributes : Arrays.asList(snapeBlood, tonksBlood, tonksPatronus)) {
	    entityManager.persist(attributes);
	    entityManager.flush();
	    entityManager.detach(attributes);
	}
	List<UserAttribute> found = userAttributeRepository.findByValue("Bear");
	assertTrue(found.isEmpty());
	found = userAttributeRepository.findByValue(snapeBlood.getAttributeValue());
	assertEquals(2, found.size());
    }

    @Test
    public void shouldSave() {
	snapeBlood = userAttributeRepository.save(snapeBlood);
	userAttributeRepository.flush();
	UserAttribute found = entityManager.find(UserAttribute.class, snapeBlood.getAttributeId());
	assertNotNull(found);
    }
}
