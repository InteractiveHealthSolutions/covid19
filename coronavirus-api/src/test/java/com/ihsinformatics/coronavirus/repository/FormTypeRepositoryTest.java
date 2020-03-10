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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.repository.FormTypeRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FormTypeRepositoryTest extends BaseRepositoryData {

    @Autowired
    private FormTypeRepository formTypeRepository;

    @Before
    public void reset() {
	super.reset();
    }

    @Test
    public void shouldDelete() {
	quidditchForm = entityManager.persist(quidditchForm);
	entityManager.flush();
	Integer id = quidditchForm.getFormTypeId();
	entityManager.detach(quidditchForm);
	formTypeRepository.delete(quidditchForm);
	FormType found = entityManager.find(FormType.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindByFormName() {
	quidditchForm = entityManager.persist(quidditchForm);
	entityManager.flush();
	entityManager.detach(quidditchForm);
	FormType found = formTypeRepository.findByFormName(quidditchForm.getFormName());
	assertNotNull(found);
	assertEquals(quidditchForm.getUuid(), found.getUuid());
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(quidditchForm);
	entityManager.flush();
	entityManager.detach(quidditchForm);
	Optional<FormType> found = formTypeRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	quidditchForm = entityManager.persist(quidditchForm);
	entityManager.flush();
	String uuid = quidditchForm.getUuid();
	entityManager.detach(quidditchForm);
	FormType found = formTypeRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldFindNonRetired() throws Exception {
	challengeForm.setIsRetired(true);
	for (FormType formType : Arrays.asList(quidditchForm, trainingForm, challengeForm)) {
	    entityManager.persist(formType);
	    entityManager.flush();
	    entityManager.detach(formType);
	}
	List<FormType> found = formTypeRepository.findNonRetired();
	assertNotNull(found);
	assertEquals(2, found.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldNotSave() {
	quidditchForm = formTypeRepository.save(quidditchForm);
	formTypeRepository.flush();
	FormType duplicate = FormType.builder().formName("Quidditch Participation").shortName("Duplicate").build();
	duplicate = formTypeRepository.save(duplicate);
	formTypeRepository.flush();
    }

    @Test
    public void shouldRetire() {
	quidditchForm = entityManager.persist(quidditchForm);
	entityManager.flush();
	Integer id = quidditchForm.getFormTypeId();
	formTypeRepository.softDelete(quidditchForm);
	entityManager.detach(quidditchForm);
	FormType found = entityManager.find(FormType.class, id);
	assertTrue(found.getIsRetired());
    }

    @Test
    public void shouldSave() {
	quidditchForm = formTypeRepository.save(quidditchForm);
	formTypeRepository.flush();
	FormType found = entityManager.find(FormType.class, quidditchForm.getFormTypeId());
	assertNotNull(found);
    }
}
