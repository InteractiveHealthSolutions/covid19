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
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.repository.DefinitionRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DefinitionRepositoryTest extends BaseRepositoryData {

    @Autowired
    private DefinitionRepository definitionRepository;

    @Before
    public void reset() {
	initDefinitionTypes();
	country = entityManager.persistAndFlush(country);
	initDefinitions();
    }

    @Test
    public void shouldDelete() {
	scotland = entityManager.persist(scotland);
	entityManager.flush();
	Integer id = scotland.getDefinitionId();
	entityManager.detach(scotland);
	definitionRepository.delete(scotland);
	Definition found = entityManager.find(Definition.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(scotland);
	entityManager.flush();
	entityManager.detach(scotland);
	Optional<Definition> found = definitionRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByName() {
	for (Definition definition : Arrays.asList(england)) {
	    entityManager.persist(definition);
	    entityManager.flush();
	    entityManager.detach(definition);
	}
	List<Definition> found = definitionRepository.findByName(england.getDefinitionName());
	assertNotNull(found);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByShortName() {
	scotland = entityManager.persist(scotland);
	entityManager.flush();
	entityManager.detach(scotland);
	List<Definition> found = definitionRepository.findByShortName(scotland.getShortName());
	assertNotNull(found);
	assertEquals(1, found.size());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	scotland = entityManager.persist(scotland);
	entityManager.flush();
	String uuid = scotland.getUuid();
	entityManager.detach(scotland);
	Definition found = definitionRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	scotland = definitionRepository.save(scotland);
	definitionRepository.flush();
	Definition found = entityManager.find(Definition.class, scotland.getDefinitionId());
	assertNotNull(found);
    }
}
