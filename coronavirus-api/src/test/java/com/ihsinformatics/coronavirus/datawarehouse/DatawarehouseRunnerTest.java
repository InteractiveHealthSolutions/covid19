/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.datawarehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.service.BaseService;
import com.ihsinformatics.coronavirus.service.ValidationServiceImpl;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
public class DatawarehouseRunnerTest extends BaseTestData {

    @Mock
    private BaseService baseService;

    @Mock
    private ValidationServiceImpl validationService;

    @InjectMocks
    private DatawarehouseRunner dw;

    @Before
    public void reset() {
	MockitoAnnotations.initMocks(this);
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner#generateCreateTableQuery(com.ihsinformatics.coronavirus.model.FormType, java.lang.String)}.
     * 
     * @throws JSONException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldGenerateCreateTableQuery() throws HibernateException, ValidationException, JSONException {
	when(validationService.validateFormType(any(FormType.class))).thenReturn(true);
	List<Element> elements = Arrays.asList(schoolElement, houseElement, roleElement, numberElement, genderElement,
		heightElement);
	for (Element element : elements) {
	    when(validationService.findElementByIdentifier(element.getShortName().toLowerCase())).thenReturn(element);
	}
	trainingForm = FormType.builder().formName("Training Registration Form").shortName("TRAINING").build();
	trainingForm.setFormSchema(
		"{\"lang\":\"en\",\"fields\":[{\"page\": 1,\"order\": 1,\"element\": \"school\"},{\"page\": 1,\"order\": 2,\"element\": \"house\"},{\"page\": 1,\"order\": 3,\"element\": \"role\"},{\"page\": 1,\"order\": 4,\"element\": \"no\"},{\"page\": 1,\"order\": 5,\"element\": \"gender\"},{\"page\": 1,\"order\": 6,\"element\": \"height\"}]}");
	String query = dw.generateCreateTableQuery(trainingForm, trainingForm.getShortName());
	assertTrue(query.startsWith("create table if not exists " + trainingForm.getShortName()));
	for (Element element : elements) {
	    assertTrue(query.contains(element.getShortName().toLowerCase()));
	}
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner#generateUpdateTableQuery(com.ihsinformatics.coronavirus.model.FormType, java.lang.String)}.
     * 
     * @throws JSONException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldGenerateUpdateTableQuery() throws HibernateException, ValidationException, JSONException {
	Query mockQuery = mock(Query.class);
	EntityManager em = mock(EntityManager.class);
	List<Object> jsonList = new ArrayList<>();
	baseService.setEntityManager(em);
	when(baseService.getEntityManager()).thenReturn(em);
	when(em.createNativeQuery(any(String.class))).thenReturn(mockQuery);
	when(mockQuery.getResultList()).thenReturn(jsonList);
	when(validationService.validateFormType(any(FormType.class))).thenReturn(true);

	jsonList.add(
		"[\"trainer\", \"district\", \"province\", \"date_start\", \"program_type\", \"school_level\", \"training_days\", \"training_type\", \"training_venue\", \"participant_scores\"]");
	List<Element> elements = Arrays.asList(schoolElement, houseElement, roleElement, numberElement, genderElement,
		heightElement);
	for (Element element : elements) {
	    when(validationService.findElementByIdentifier(element.getShortName().toLowerCase())).thenReturn(element);
	}
	trainingForm = FormType.builder().formName("Training Registration Form").shortName("TRAINING").build();
	trainingForm.setFormSchema(
		"{\"lang\":\"en\",\"fields\":[{\"page\": 1,\"order\": 1,\"element\": \"school\"},{\"page\": 1,\"order\": 2,\"element\": \"house\"},{\"page\": 1,\"order\": 3,\"element\": \"role\"},{\"page\": 1,\"order\": 4,\"element\": \"no\"},{\"page\": 1,\"order\": 5,\"element\": \"gender\"},{\"page\": 1,\"order\": 6,\"element\": \"height\"}]}");
	String query = dw.generateUpdateTableQuery(trainingForm, trainingForm.getShortName());
	assertTrue(query.startsWith("insert into " + trainingForm.getShortName()));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner#filterKeySetList(java.util.List)}.
     * @throws JSONException 
     */
    @Test
    public void shouldFilterKeySetList() throws JSONException {
	List<String> keySetList = new ArrayList<>();
	keySetList.add("[\"school\", \"broomstick\", \"house\", \"date_joined\", \"height\", \"titles\"]");
	keySetList.add("[\"school\", \"house\", \"gender\"]");
	Set<String> keySet = dw.filterKeySetList(keySetList);
	assertTrue(keySet.contains("school"));
	assertTrue(keySet.contains("gender"));
	assertEquals(7, keySet.size());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner#filterKeySetList(java.util.List)}.
     * @throws JSONException 
     */
    @Test
    public void shouldReturnEmptySet() throws JSONException {
	assertEquals(Collections.emptySet(), dw.filterKeySetList(null));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseRunner#getSqlDataType(com.ihsinformatics.coronavirus.model.Element)}.
     */
    @Test
    public void shouldGetSqlDataType() {
	assertTrue(dw.getSqlDataType(schoolElement).toLowerCase().startsWith("varchar"));
	assertTrue(dw.getSqlDataType(houseElement).toLowerCase().startsWith("varchar"));
	assertTrue(dw.getSqlDataType(captainElement).toLowerCase().startsWith("varchar"));
	assertTrue(dw.getSqlDataType(numberElement).toLowerCase().startsWith("int"));
	assertTrue(dw.getSqlDataType(heightElement).toLowerCase().startsWith("float"));
	assertTrue(dw.getSqlDataType(genderElement).toLowerCase().startsWith("char"));
	assertTrue(dw.getSqlDataType(dateJoinedElement).toLowerCase().startsWith("date"));
	assertTrue(dw.getSqlDataType(titlesElement).toLowerCase().startsWith("text"));
    }
}
