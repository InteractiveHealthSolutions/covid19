/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.hibernate.Session;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.FormService;
import com.ihsinformatics.coronavirus.service.ReportServiceImpl;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.web.ReportController;

import net.minidev.json.JSONObject;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ReportControllerTest extends BaseRepositoryData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private FormService formService;

    @Mock
    private DonorService donorService;

    @Mock
    private ReportServiceImpl service;

    @InjectMocks
    private ReportController reportController;

    private String fileName;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(reportController).alwaysDo(MockMvcResultHandlers.print()).build();
	ClassLoader classLoader = getClass().getClassLoader();
	File file = new File(classLoader.getResource("application.properties").getFile());
	fileName = file.getPath();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadData(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadData() throws Exception {
	when(formService.getFormTypeByName(any(String.class))).thenReturn(quidditchForm);
	when(service.generateFormDataCSV(any(String.class))).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/form/{uuid}", quidditchForm.getShortName()));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateFormDataCSV(any(String.class));
	verify(formService, times(1)).getFormTypeByName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadDefinitions()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadDefinitions() throws Exception {
	when(service.generateDefinitionsCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/definitions.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateDefinitionsCSV();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadDonors()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadDonors() throws Exception {
	when(service.generateDonorsCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/donors.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateDonorsCSV();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadElements()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadElements() throws Exception {
	when(service.generateElementsCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/elements.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateElementsCSV();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadLocations()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadLocations() throws Exception {
	when(service.generateLocationsCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/locations.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateLocationsCSV();

    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadProjects()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadProjects() throws Exception {
	when(service.generateProjectsCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/projects.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateProjectsCSV();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadUsers()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadUsers() throws Exception {
	when(service.generateUsersCSV()).thenReturn(fileName);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/users.csv"));
	actions.andExpect(status().isOk());
	byte[] expected = Files.readAllBytes(Paths.get(fileName));
	actions.andExpect(content().bytes(expected));
	verify(service, times(1)).generateUsersCSV();
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadCSVReports()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadCSVReports() throws Exception {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("start_date", "2019-10-01");
    	params.put("end_date", "2019-10-29");
    	params.put("province", "Balochistan");
    	params.put("city", "Awaran");
    	params.put("level_program", "Primary");
    	when(service.generateJasperReport("school_summary", "csv", params)).thenReturn(fileName);
	    ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/csv/school_summary?start_date=2019-10-01&end_date=2019-10-29&province=Balochistan&city=Awaran&level_program=Primary"));
	    actions.andExpect(status().isOk());
	    byte[] expected = Files.readAllBytes(Paths.get(fileName));
		actions.andExpect(content().bytes(expected));
	    verify(service, times(1)).generateJasperReport("school_summary", "csv", params);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadHTMLReports()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadHTMLReports() throws Exception {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("start_date", "2019-10-01");
    	params.put("end_date", "2019-10-29");
    	params.put("province", "Balochistan");
    	params.put("city", "Awaran");
    	params.put("level_program", "Primary");
    	when(service.generateJasperReport("school_summary", "html", params)).thenReturn(fileName);
	    ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/html/school_summary?start_date=2019-10-01&end_date=2019-10-29&province=Balochistan&city=Awaran&level_program=Primary"));
	    actions.andExpect(status().isOk());
	    byte[] expected = Files.readAllBytes(Paths.get(fileName));
		actions.andExpect(content().bytes(expected));
	    verify(service, times(1)).generateJasperReport("school_summary", "html", params);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadXLSReports()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadXLSReports() throws Exception {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("start_date", "2019-10-01");
    	params.put("end_date", "2019-10-29");
    	params.put("province", "Balochistan");
    	params.put("city", "Awaran");
    	params.put("level_program", "Primary");
    	when(service.generateJasperReport("school_summary", "xls", params)).thenReturn(fileName);
	    ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/xls/school_summary?start_date=2019-10-01&end_date=2019-10-29&province=Balochistan&city=Awaran&level_program=Primary"));
	    actions.andExpect(status().isOk());
	    byte[] expected = Files.readAllBytes(Paths.get(fileName));
		actions.andExpect(content().bytes(expected));
	    verify(service, times(1)).generateJasperReport("school_summary", "xls", params);
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#downloadPDFReports()}.
     * 
     * @throws Exception
     */
    @Test
    public void testDownloadPDFReports() throws Exception {
    	
    	Map<String, String> params = new HashMap<>();
    	params.put("start_date", "2019-10-01");
    	params.put("end_date", "2019-10-29");
    	params.put("province", "Balochistan");
    	params.put("city", "Awaran");
    	params.put("level_program", "Primary");
    	when(service.generateJasperReport("school_summary", "pdf", params)).thenReturn(fileName);
	    ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/pdf/school_summary?start_date=2019-10-01&end_date=2019-10-29&province=Balochistan&city=Awaran&level_program=Primary"));
	    actions.andExpect(status().isOk());
	    byte[] expected = Files.readAllBytes(Paths.get(fileName));
		actions.andExpect(content().bytes(expected));
	    verify(service, times(1)).generateJasperReport("school_summary", "pdf", params);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getDonorCount()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetDonorCount() throws Exception {
	when(service.getSession()).thenReturn(entityManager.getEntityManager().unwrap(Session.class));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/donorcount", 1));
	actions.andExpect(status().isOk());
	verify(service, times(1)).getSession();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getProjectCount()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetProjectCount() throws Exception {
	when(service.getSession()).thenReturn(entityManager.getEntityManager().unwrap(Session.class));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/projectcount", 1));
	actions.andExpect(status().isOk());
	verify(service, times(1)).getSession();
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getUserCount()}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetUserCount() throws Exception {
	when(service.getSession()).thenReturn(entityManager.getEntityManager().unwrap(Session.class));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/usercount", 1));
	actions.andExpect(status().isOk());
	verify(service, times(1)).getSession();
    }

    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getPartnerSchoolData(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPartnerSchoolData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("date_of_partnership", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("school_tier", "New");
	obj.put("total", 1);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/partnerschooldata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getPartnerSchoolDataByFiscalYear()}.
     */
    @Test
    public void shouldGetPartnerSchoolDataByFiscalYear() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("date_of_partnership", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("school_tier", "New");
	obj.put("total", 1);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/partnerschooldata/year")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getTeachersTrainings(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetTeachersTrainings() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("date_of_partnership", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("school_tier", "New");
	obj.put("program_type", "Running");
	obj.put("total", 2);
	jsonData.put(obj);
	obj = new JSONObject();
	obj.put("state_province", "Unknown");
	obj.put("date_of_partnership", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("school_tier", "New");
	obj.put("program_type", "Exit");
	obj.put("total", 1);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/teacherstrainingdata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getTrainings(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetTrainings() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("form_date", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("training_type", "Running");
	obj.put("total", 2);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/trainingdata")
		.param("city_village", hogwartz.getStateProvince())
		.param("state_province", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getOneTouchSessionsData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetOneTouchSessionsData() throws Exception {
        JSONArray jsonData = new JSONArray();
        JSONObject obj = new JSONObject();
        hogwartz.setStateProvince("Unknown");
        obj.put("state_province", hogwartz.getStateProvince());
        obj.put("training_year", 2019);
        obj.put("session_topic", "PUBERTY");
        obj.put("teachers_attending", 5);
        obj.put("students_attending", 50);
        obj.put("parents_attending", 12);
        obj.put("school_staff_attending", 7);
        obj.put("call_agents_attending", 3);
        obj.put("others_attending", 0);
        obj.put("total", 80);
        jsonData.put(obj);
        obj = new JSONObject();
        obj.put("state_province", "Unknown");
        obj.put("training_year", 2019);
        obj.put("session_topic", "OTHER");
        obj.put("teachers_attending", 5);
        obj.put("students_attending", 50);
        obj.put("parents_attending", 12);
        obj.put("school_staff_attending", 7);
        obj.put("call_agents_attending", 3);
        obj.put("others_attending", 0);
        obj.put("total", 80);
        jsonData.put(obj);
        when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
        ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/onetouchdata")
        	.param("state_province", hogwartz.getStateProvince())
        	.param("city_village", hogwartz.getStateProvince())
        	.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
        	.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
        actions.andExpect(status().isOk());
        actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
        verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getSchoolMonitoringData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetSchoolMonitoringData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("city_village", "Unknown");
	obj.put("form_date", hogwartz.getDateCreated());
	obj.put("school_level", "Primary");
	obj.put("program_type", "GENDER");
	obj.put("class_grade", "ONE");
	obj.put("total", 2);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/schoolmonitoringdata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getExitSchoolPlanningData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetExitSchoolPlanningData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("year", 2019);
	obj.put("srhr_policy_implemented", 0);
	obj.put("parent_session_conducted", 4);
	obj.put("total", 4);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/exitschoolplanningdata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getParticipantTrainingData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetParticipantTrainingData() throws Exception {
    	JSONArray jsonData = new JSONArray();
    	JSONObject obj = new JSONObject();
    	obj.put("state_province", hogwartz.getStateProvince());
    	obj.put("city_village", hogwartz.getCityVillage());
    	obj.put("participant_type", "Pre-service provider");
    	obj.put("total", 3);
    	obj.put("gender", "Female");
    	obj.put("form_date", "2019-09-09");
    	jsonData.put(obj);
    	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
    	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/participanttrainingdata")
    		.param("state_province", hogwartz.getStateProvince())
    		.param("city_village", hogwartz.getStateProvince())
    		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
    		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
    	actions.andExpect(status().isOk());
    	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
    	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getIndividualReachData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetIndividualReachData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("city_village", hogwartz.getCityVillage());
	obj.put("activity_type", "health_care_provider_reach");
	obj.put("year", 2019);
	obj.put("male_count", 1);
	obj.put("female_count", 0);
	obj.put("other_sex_count", 4);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/individualreachdata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getAmplifyChangeParticipantData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetAmplifyChangeParticipantData() throws Exception {
    	JSONArray jsonData = new JSONArray();
    	JSONObject obj = new JSONObject();
    	obj.put("state_province", hogwartz.getStateProvince());
    	obj.put("city_village", hogwartz.getCityVillage());
    	obj.put("participant_type", "Student");
    	obj.put("total", 3);
    	obj.put("gender", "Female");
    	obj.put("form_date", "2019-09-09");
    	jsonData.put(obj);
    	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
    	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/amplifychangeparticipantdata")
    		.param("state_province", hogwartz.getStateProvince())
    		.param("city_village", hogwartz.getStateProvince())
    		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
    		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
    	actions.andExpect(status().isOk());
    	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
    	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getSocialMediaTrafficData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetSocialMediaTrafficData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("platform", "twitter");
	obj.put("day_name", "Monday");
	obj.put("week_day", 2);
	obj.put("likes", 5);
	obj.put("shares", 8);
	obj.put("boosted_count", 1);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/socialmediatraffic")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getRadioLiveCallData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetRadioLiveCallData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("day_name", "Monday");
	obj.put("radio_channel_name", "FM 100");
	obj.put("week_day", 2);
	obj.put("radio_channel_frequency", 100.0);
	obj.put("listener_count", 8);
	obj.put("live_call_count", 99);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/radiocalldata")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getMobileCinemaData(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetMobileCinemaData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("city_village", hogwartz.getCityVillage());
	obj.put("form_date", hogwartz.getDateCreated());
	obj.put("screening_type", "cinema");
	obj.put("total", 4);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/mobilecinemadata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getMaterialDistributionData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetMaterialDistributionData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("form_date", hogwartz.getDateCreated());
	obj.put("partner_component", "comms");
	obj.put("distribution_location", "aahung_office");
	obj.put("annual_report_count", 10);
	obj.put("aahung_profile_count", 20);
	obj.put("pamphlet_count", 30);
	obj.put("booklet_count", 20);
	obj.put("report_count", 10);
	obj.put("aahung_mugs_count", 0);
	obj.put("aahung_folders_count", 12);
	obj.put("aahung_notebooks_count", 14);
	obj.put("other_material_count", 16);
	obj.put("aahung_info_count", 12);
	obj.put("nikkah_nama_count", 4);
	obj.put("puberty_count", 0);
	obj.put("rti_count", 0);
	obj.put("ungei_count", 0);
	obj.put("sti_count", 0);
	obj.put("sexual_health_count", 0);
	obj.put("premarital_info_count", 0);
	obj.put("pac_count", 0);
	obj.put("maternal_health_count", 0);
	obj.put("other_topic_count", 0);
	obj.put("total", 100);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/materialdistributiondata")
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getCommunicationsTrainingData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetCommunicationsTrainingData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("city_village", hogwartz.getCityVillage());
	obj.put("form_date", hogwartz.getDateCreated());
	obj.put("covered_srhr", 1);
	obj.put("covered_agency_choice", 0);
	obj.put("covered_gender_sensitization", 1);
	obj.put("covered_other", 0);
	obj.put("journalist_count", 1);
	obj.put("blogger_count", 2);
	obj.put("screenwriter_count", 3);
	obj.put("other_media_count", 4);
	obj.put("other_attendant_count", 5);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/communicationstrainingdata")
		.param("state_province", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getPartnerInstitutionData(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldGetPartnerInstitutionData() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put("state_province", hogwartz.getStateProvince());
	obj.put("date_of_partnership", hogwartz.getDateCreated());
	obj.put("school_level", "Secondary");
	obj.put("school_tier", "New");
	obj.put("total", 1);
	jsonData.put(obj);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/partnerinstitutiondata")
		.param("state_province", hogwartz.getStateProvince())
		.param("city_village", hogwartz.getStateProvince())
		.param("from", DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2010)))
		.param("to", DateTimeUtil.toSqlDateString(DateTimeUtil.create(31, 12, 2020))));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getLocationCountByCategory()}.
     */
    @Test
    public void shouldGetLocationCountByCategory() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj1 = new JSONObject();
	obj1.put("total", 4);
	obj1.put("category","School");
	jsonData.put(obj1);
	JSONObject obj2 = new JSONObject();
	obj2.put("total", 2);
	obj2.put("category","Institution");
	jsonData.put(obj2);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/locationcount/category"));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.ReportController#getLocationCountByStateProvince()}.
     */
    @Test
    public void shouldGetLocationCountByStateProvince() throws Exception {
	JSONArray jsonData = new JSONArray();
	JSONObject obj1 = new JSONObject();
	obj1.put("total", 4);
	obj1.put("stateprovince","Sindh");
	jsonData.put(obj1);
	JSONObject obj2 = new JSONObject();
	obj2.put("total", 2);
	obj2.put("stateprovince","Balochistan");
	jsonData.put(obj2);
	when(service.getTableDataAsJson(any(String.class))).thenReturn(jsonData);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "report/locationcount/stateprovince"));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(service, times(1)).getTableDataAsJson(any(String.class));
    }
    
}
