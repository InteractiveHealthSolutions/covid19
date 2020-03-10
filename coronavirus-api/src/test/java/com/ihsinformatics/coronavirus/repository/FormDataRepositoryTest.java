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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.repository.FormDataRepository;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FormDataRepositoryTest extends BaseRepositoryData {

    @Autowired
    private FormDataRepository formDataRepository;

    @Before
    public void reset() {
	super.reset();
	quidditchForm = entityManager.persist(quidditchForm);
	entityManager.flush();
    }

    @Test
    public void shouldDelete() {
	harryData = entityManager.persist(harryData);
	entityManager.flush();
	Integer id = harryData.getFormId();
	entityManager.detach(harryData);
	formDataRepository.delete(harryData);
	FormData found = entityManager.find(FormData.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindAll() {
	Pageable pageable = PageRequest.of(1, 5, Sort.by("formDate"));
	FormData template = new FormData();
	Page<FormData> found = formDataRepository.findAll(Example.of(template), pageable);
	assertNotNull(found);
    }

    @Test
    public void shouldFindByDateRange() {
	hermioneData.setFormDate(DateTimeUtil.create(25, 7, 2019));
	harryData.setFormDate(DateTimeUtil.create(30, 7, 2019));
	ronData.setFormDate(DateTimeUtil.create(1, 1, 2019));
	for (FormData obj : Arrays.asList(harryData, hermioneData, ronData)) {
	    obj = entityManager.persist(obj);
	    entityManager.flush();
	    entityManager.detach(obj);
	}
	Pageable pageable = PageRequest.of(1, 5, Sort.by("formDate"));
	Page<FormData> found = formDataRepository.findByDateRange(hermioneData.getFormDate(), harryData.getFormDate(),
		pageable);
	assertNotNull(found);
	List<FormData> list = found.getContent();
	assertEquals(2, list.size());
    }
    
    @Test
    public void shouldFindByDateRangeNonPage() {
	hermioneData.setFormDate(DateTimeUtil.create(25, 7, 2019));
	harryData.setFormDate(DateTimeUtil.create(30, 7, 2019));
	ronData.setFormDate(DateTimeUtil.create(1, 1, 2019));
	for (FormData obj : Arrays.asList(harryData, hermioneData, ronData)) {
	    obj = entityManager.persist(obj);
	    entityManager.flush();
	    entityManager.detach(obj);
	}
	List<FormData> found = formDataRepository.findByDateRange(hermioneData.getFormDate(), harryData.getFormDate());
	assertNotNull(found);
	assertEquals(2, found.size());
    }
    
    @Test
    public void shouldSearchNonPageByFormType() {
	hermioneData.setFormDate(DateTimeUtil.create(25, 7, 2019));
	harryData.setFormDate(DateTimeUtil.create(30, 7, 2019));
	ronData.setFormDate(DateTimeUtil.create(1, 1, 2019));
	for (FormData obj : Arrays.asList(harryData, hermioneData, ronData)) {
	    obj = entityManager.persist(obj);
	    entityManager.flush();
	    entityManager.detach(obj);
	}
	List<FormData> found = formDataRepository.search(quidditchForm, null, null, hermioneData.getFormDate(), harryData.getFormDate());
	assertNotNull(found);
	assertEquals(2, found.size());
		
    }
    

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(harryData);
	entityManager.flush();
	entityManager.detach(harryData);
	Optional<FormData> found = formDataRepository.findById((Integer) id);
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	harryData = entityManager.persist(harryData);
	entityManager.flush();
	String uuid = harryData.getUuid();
	entityManager.detach(harryData);
	FormData found = formDataRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	harryData = formDataRepository.save(harryData);
	formDataRepository.flush();
	FormData found = entityManager.find(FormData.class, harryData.getFormId());
	assertNotNull(found);
    }

    @Test
    public void shouldVoid() {
	harryData = entityManager.persist(harryData);
	entityManager.flush();
	Integer id = harryData.getFormId();
	formDataRepository.softDelete(harryData);
	entityManager.detach(harryData);
	FormData found = entityManager.find(FormData.class, id);
	assertTrue(found.getIsVoided());
    }
}
