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

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.repository.DefinitionTypeRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DefinitionTypeRepositoryTest extends BaseRepositoryData {

    @Autowired
    private DefinitionTypeRepository definitionTypeRepository;

    @Before
    public void reset() {
	super.reset();
	house.setDefinitionTypeId(null);
	house.setShortName("HOUSE");
    }

    @Test
    public void shouldDelete() {
	house = entityManager.persist(house);
	entityManager.flush();
	Integer id = house.getDefinitionTypeId();
	entityManager.detach(house);
	definitionTypeRepository.delete(house);
	DefinitionType found = entityManager.find(DefinitionType.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(house);
	entityManager.flush();
	entityManager.detach(house);
	Optional<DefinitionType> found = definitionTypeRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByName() {
	house = entityManager.persist(house);
	entityManager.flush();
	entityManager.detach(house);
	List<DefinitionType> found = definitionTypeRepository.findByName(house.getTypeName());
	assertFalse(found.isEmpty());
	assertEquals(house, found.get(0));
    }

    @Test
    public void shouldFindByShortName() {
	house = entityManager.persist(house);
	entityManager.flush();
	entityManager.detach(house);
	DefinitionType found = definitionTypeRepository.findByShortName(house.getShortName());
	assertEquals(house, found);
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	house = entityManager.persist(house);
	entityManager.flush();
	String uuid = house.getUuid();
	entityManager.detach(house);
	DefinitionType found = definitionTypeRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	house = definitionTypeRepository.save(house);
	definitionTypeRepository.flush();
	DefinitionType found = entityManager.find(DefinitionType.class, house.getDefinitionTypeId());
	assertNotNull(found);
    }
}
