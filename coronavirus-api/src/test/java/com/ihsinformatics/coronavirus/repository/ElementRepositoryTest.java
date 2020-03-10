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
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.repository.ElementRepository;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ElementRepositoryTest extends BaseRepositoryData {

    @Autowired
    private ElementRepository roleRepository;

    @Before
    public void reset() {
	super.reset();
    }

    @Test
    public void shouldDelete() {
	schoolElement = entityManager.persist(schoolElement);
	entityManager.flush();
	Integer id = schoolElement.getElementId();
	entityManager.detach(schoolElement);
	roleRepository.delete(schoolElement);
	Element found = entityManager.find(Element.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByDataType() {
	for (Element element : Arrays.asList(schoolElement, houseElement, broomstickElement, captainElement)) {
	    entityManager.persist(element);
	    entityManager.flush();
	    entityManager.detach(element);
	}
	List<Element> found = roleRepository.findByDataType(DataType.DEFINITION);
	assertNotNull(found);
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(schoolElement);
	entityManager.flush();
	entityManager.detach(schoolElement);
	Optional<Element> found = roleRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByName() {
	for (Element element : Arrays.asList(schoolElement, houseElement, captainElement)) {
	    entityManager.persist(element);
	    entityManager.flush();
	    entityManager.detach(element);
	}
	List<Element> found = roleRepository.findByName("Name");
	assertNotNull(found);
	// Should return 2 objects
	assertEquals(2, found.size());
    }

    @Test
    public void shouldFindByShortName() {
	schoolElement = entityManager.persist(schoolElement);
	entityManager.flush();
	entityManager.detach(schoolElement);
	Element found = roleRepository.findByShortName(schoolElement.getShortName());
	assertNotNull(found);
	assertEquals(schoolElement, found);
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	schoolElement = entityManager.persist(schoolElement);
	entityManager.flush();
	String uuid = schoolElement.getUuid();
	entityManager.detach(schoolElement);
	Element found = roleRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	schoolElement = roleRepository.save(schoolElement);
	roleRepository.flush();
	Element found = entityManager.find(Element.class, schoolElement.getElementId());
	assertNotNull(found);
    }
}
