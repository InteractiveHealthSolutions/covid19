/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.service.DatawarehouseService;
import com.ihsinformatics.coronavirus.service.FormService;
import com.ihsinformatics.coronavirus.service.ReportServiceImpl;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.jasperreports.engine.JRException;

/**
 * @author owais.hussain@ihsinformatics.com
 */

@RestController
@RequestMapping("/api")
@Api(value = "Report Controller")
public class ReportController extends BaseController {
	
	String provinceFilter = "and find_in_set(l.state_province, '<stateProvince>') ";
	String formDataProvinceFilter = "and find_in_set(fd.province, '<stateProvince>') ";
	String provincePlaceholder = "<stateProvince>";
	String districtFilter = "and find_in_set(l.city_village, '<cityVillage>') ";
	String formDataDistrictFilter = "and find_in_set(fd.district, '<cityVillage>') ";
	String districtPlaceholder = "<cityVillage>";
	
	String dateFilterToFrom = "date('<from>') and date('<to>') ";
	String andDateFilter = "and date(fd.form_date) between ";
	String whereDateFilter = "where date(fd.form_date) between ";
	String dateFromPlaceholder = "<from>";
	String dateToPlaceholder = "<to>";
	
	String schoolLevelAttribute = "inner join location_attribute as level on level.location_id = l.location_id and level.attribute_type_id = 14 ";
	String schoolTierAttribute = "inner join location_attribute as tier on tier.location_id = l.location_id and tier.attribute_type_id = 16 ";
	String schoolLevelDefinition = "inner join definition as level_name on level_name.definition_id = level.attribute_value ";
	String schoolTierDefinition = "inner join definition as tier_name on tier_name.definition_id = tier.attribute_value ";

	String locationJoinFormData = "inner join location as l on l.location_id = fd.location_id ";
	String formParticipntJoinFormData = "inner join form_participant as pt on pt.form_id = fd.form_id ";
	String participantJoinPerson = "inner join participant as p on p.person_id = pt.person_id ";
	
	String formDataVoidClause = "where fd.voided = 0 ";
	
	String union = "union ";
	String where = "where ";
	
	String downloadingData = "Downloading data.";
	
	
	
    @Autowired
    private FormService formService;
    
    @Autowired
    private DatawarehouseService datawarehouseService;

    @Autowired
    private ReportServiceImpl service;
    

    @ApiOperation(value = "Download FormData as CSV by UUID/Name/Short Name of the FormType")
    @GetMapping("/report/form/{uuid}")
    public ResponseEntity<?> downloadData(@PathVariable String uuid) {
	try {
	    FormType formType = uuid.matches(RegexUtil.UUID) ? formService.getFormTypeByUuid(uuid)
		    : formService.getFormTypeByName(uuid);
	    String filePath = service.generateFormDataCSV(formType.getShortName());
	    String fileName = "formdata-" + uuid + ".csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Definitions as CSV")
    @GetMapping("/report/definitions.csv")
    public ResponseEntity<?> downloadDefinitions() {
	try {
	    String filePath = service.generateDefinitionsCSV();
	    String fileName = "definitions.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Donors as CSV")
    @GetMapping("/report/donors.csv")
    public ResponseEntity<?> downloadDonors() {
	try {
	    String filePath = service.generateDonorsCSV();
	    String fileName = "donors.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Elements as CSV")
    @GetMapping("/report/elements.csv")
    public ResponseEntity<?> downloadElements() {
	try {
	    String filePath = service.generateElementsCSV();
	    String fileName = "elements.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Locaitions as CSV")
    @GetMapping("/report/locations.csv")
    public ResponseEntity<?> downloadLocations() {
	try {
	    String filePath = service.generateLocationsCSV();
	    String fileName = "locations.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Projects as CSV")
    @GetMapping("/report/projects.csv")
    public ResponseEntity<?> downloadProjects() {
	try {
	    String filePath = service.generateProjectsCSV();
	    String fileName = "projects.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }

    @ApiOperation(value = "Download list of all Users as CSV")
    @GetMapping("/report/users.csv")
    public ResponseEntity<?> downloadUsers() {
	try {
	    String filePath = service.generateUsersCSV();
	    String fileName = "users.csv";
	    return downloadResponse(filePath, fileName);
	} catch (IOException e) {
	    return exceptionFoundResponse(downloadingData, e);
	}
    }
    
    @ApiOperation(value = "Download report as csv")
    @GetMapping("/report/csv/{name}")
    public ResponseEntity<?> downloadCsvReport(@PathVariable String name, @RequestParam("start_date")String startDate, @RequestParam("end_date")String endDate,
   		 @RequestParam("province")String province, @RequestParam("city")String city, @RequestParam(required = false) Map<String, String> params) {
	try {
		String filePath = service.generateJasperReport(name, "csv", params);
		String fileName = name+".csv";
		return downloadResponse(filePath, fileName);	
	} catch (JRException | SQLException | IOException e) {
		return exceptionFoundResponse(downloadingData, e);
	} 
    }
    
    @ApiOperation(value = "Download report as html")
    @GetMapping("/report/html/{name}")
    public ResponseEntity<?> downloadHtmlReport(@PathVariable String name, @RequestParam("start_date")String startDate, @RequestParam("end_date")String endDate,
   		 @RequestParam("province")String province, @RequestParam("city")String city, @RequestParam(required = false) Map<String, String> params) {
	try {
		String filePath = service.generateJasperReport(name, "html", params);
		String fileName = name+".html";
		return downloadResponse(filePath, fileName);	
	} catch (JRException | SQLException | IOException e) {
		return exceptionFoundResponse(downloadingData, e);
	} 
    }
    
    @ApiOperation(value = "Download report as xls")
    @GetMapping("/report/xls/{name}")
    public ResponseEntity<?> downloadXlsReport(@PathVariable String name, @RequestParam("start_date")String startDate, @RequestParam("end_date")String endDate,
   		 @RequestParam("province")String province, @RequestParam("city")String city, @RequestParam(required = false) Map<String, String> params) {
	try {
		String filePath = service.generateJasperReport(name, "xls", params);
		String fileName = name+".xls";
		return downloadResponse(filePath, fileName);	
	} catch (JRException | SQLException | IOException e) {
		return exceptionFoundResponse(downloadingData, e);
	} 
    }
    
    @ApiOperation(value = "Download report as pdf")
    @GetMapping("/report/pdf/{name}")
    public ResponseEntity<?> downloadPdfReport(@PathVariable String name, @RequestParam("start_date")String startDate, @RequestParam("end_date")String endDate,
    		@RequestParam("province")String province, @RequestParam("city")String city, @RequestParam(required = false) Map<String, String> params) {
    try {
    	String filePath = service.generateJasperReport(name, "pdf", params);
    	String fileName = name+".pdf";
    	return downloadResponse(filePath, fileName);	
    } catch (JRException | SQLException | IOException e) {
    	return exceptionFoundResponse(downloadingData, e);
    }
    }

    @ApiOperation(value = "Run DWH")
    @GetMapping("/report/dwh")
    public ResponseEntity<?> runDwhProcess() {
    	datawarehouseService.executeTasks();
    	return ResponseEntity.ok().body("Datawarehouse proccess ended.");
    }

    /**
     * Returns single object from given native SQL query
     * 
     * @param query
     * @return
     */
    private ResponseEntity<?> getSingleResultObject(String query) {
	try {
	    Object obj = service.getSession().createNativeQuery(query).getSingleResult();
	    return ResponseEntity.ok().body(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse(query, e);
	}
    }

    @ApiOperation(value = "Get count of number of donors")
    @GetMapping(value = "/report/donorcount")
    public ResponseEntity<?> getDonorCount() throws HibernateException {
	return getSingleResultObject("select count(*) as total from donor where voided = 0");
    }

    @ApiOperation(value = "Get count of number of projects")
    @GetMapping(value = "/report/projectcount")
    public ResponseEntity<?> getProjectCount() throws HibernateException {
	return getSingleResultObject("select count(*) as total from project where voided = 0");
    }

    @ApiOperation(value = "Get count of number of locations by their category")
    @GetMapping(value = "/report/locationcount/category")
    public ResponseEntity<?> getLocationCountByCategory() throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder("select d.definition as category, count(*) total from location as l ");
	    query.append("inner join definition as d on d.definition_id = l.category ");
	    query.append("where l.voided = 0 ");
	    query.append("group by l.category ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getLocationCountByCategory", e);
	}
    }

    @ApiOperation(value = "Get count of number of locations by state/province")
    @GetMapping(value = "/report/locationcount/stateprovince")
    public ResponseEntity<?> getLocationCountByStateProvince() throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder("select ifnull(l.state_province, 'Unknown') as province, count(*) as total from location as l ");
	    query.append("where l.voided = 0 ");
	    query.append("group by l.state_province ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getLocationCountByStateProvince", e);
	}
    }

    @ApiOperation(value = "Get count of number of active users")
    @GetMapping(value = "/report/usercount")
    public ResponseEntity<?> getUserCount() throws HibernateException {
	return getSingleResultObject("select count(*) as total from users where voided = 0");
    }

    /* * * * * * * * * * * */
    /* Dashboard resources */
    /* * * * * * * * * * * */

    @ApiOperation(value = "Get data for Partner School report/chart (ref: D1 - Partner Schools by Province, Tier, Program)")
    @GetMapping(value = "/report/partnerschooldata")
    public ResponseEntity<?> getPartnerSchoolData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(l.state_province, 'Unknown') as state_province, partnership.attribute_value as date_of_partnership, level_name.definition as school_level, tier_name.definition as school_tier, count(*) as total from location as l ");
	    query.append("inner join location_attribute as partnership on partnership.location_id = l.location_id and partnership.attribute_type_id = 7 ");
	    query.append(schoolLevelAttribute);
	    query.append(schoolTierAttribute);
	    query.append(schoolLevelDefinition);
	    query.append(schoolTierDefinition);
	    query.append("where l.voided = 0 ");
	    query.append("and date(partnership.attribute_value) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by l.state_province, partnership.attribute_value, level.attribute_value, tier.attribute_value ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getPartnerSchoolData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Partner School by fiscal year, i.e. June to July (ref: D1 - Partner Schools by Year)")
    @GetMapping(value = "/report/partnerschooldata/year")
    public ResponseEntity<?> getPartnerSchoolDataByFiscalYear(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
		
		
		
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(l.state_province, 'Unknown') as state_province, (case partnership.attribute_value > 6 when 1 then year(partnership.attribute_value) else year(partnership.attribute_value) - 1 end) as fiscal_year, level_name.definition as school_level, count(*) as total from location as l ");
	    query.append("inner join location_attribute as partnership on partnership.location_id = l.location_id and partnership.attribute_type_id = 7 ");
	    query.append(schoolLevelAttribute);
	    query.append(schoolTierAttribute);
	    query.append(schoolLevelDefinition); 
	    query.append(schoolTierDefinition);
	    query.append("where l.voided = 0 ");
	    query.append("and tier_name.definition = 'New' ");
	    query.append("and year(partnership.attribute_value) between ");
	    query.append("year('"+ from + "') and year('" + to + "') " );
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by l.state_province, (case partnership.attribute_value > 6 when 1 then year(partnership.attribute_value) else year(partnership.attribute_value) - 1 end), level.attribute_value ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getPartnerSchoolDataByFiscalYear", e);
	}
    }
    
    @ApiOperation(value = "Get data for Teachers training summary (ref: D1 - Summary Teachers Trained)")
    @GetMapping(value = "/report/teacherstrainingdata")
    public ResponseEntity<?> getTeachersTrainings(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select date(fd.form_date) as form_date, ifnull(l.state_province, 'Unknown') as state_province, tier_name.definition as school_tier, lse.program_type as program, count(*) as total, level_name.definition as school_level from form_data as fd ");
	    query.append(formParticipntJoinFormData);
	    query.append(participantJoinPerson);
	    query.append("inner join location as l on l.location_id = p.location_id ");
	    query.append(schoolLevelAttribute);
	    query.append(schoolLevelDefinition);
	    query.append(schoolTierAttribute);
	    query.append(schoolTierDefinition);
	    query.append("inner join _lse_training_detail as lse on lse.form_id = fd.form_id ");
	    query.append(formDataVoidClause);
	    query.append("and fd.form_type_id = 1 ");
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by tier_name.definition_id, level_name.definition_id, l.state_province, date(fd.form_date) ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getTeachersTrainings", e);
	}
    }
    
    @ApiOperation(value = "Get data for Training summary (ref: D1 - Trainings Summary)")
    @GetMapping(value = "/report/trainingdata")
    public ResponseEntity<?> getTrainings(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(td.province, 'Unknown') as state_province, date(td.form_date) as form_date, upper(td.training_type) as training_type, count(*) as total from _lse_training_detail as td ");
	    query.append("where td.form_type_id = 1 ");
	    query.append("and date(td.form_date) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append("and find_in_set(td.province, '" + stateProvince + "') ");
	    if (cityVillage != null)
	    	query.append("and find_in_set(td.district, '" + cityVillage + "') ");
	    query.append("group by td.province, date(td.form_date), td.training_type ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getTrainings", e);
	}
    }
    
    @ApiOperation(value = "Get data for One Touch Training summary (ref: D1 - One-Touch Sessions Summary)")
    @GetMapping(value = "/report/onetouchdata")
    public ResponseEntity<?> getOneTouchSessionsData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(otd.province, 'Unknown') as state_province, year(otd.form_date) as training_year, otd.session_topic, ");
	    query.append("json_extract(event_attendant, '$.values') like '%teachers%' as teachers_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%students%' as students_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%parents%' as parents_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%school_staff%' as school_staff_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%call_agents%' as call_agents_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%other_professionals%' as other_professionals_attending, ");
	    query.append("json_extract(event_attendant, '$.values') like '%other%' as others_attending, ");
	    query.append("count(*) as total from _lse_one_touch_session_detail as otd ");
	    query.append("where date(otd.form_date) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append("and find_in_set(otd.province, '" + stateProvince + "') ");
	    if (cityVillage != null)
	    	query.append("and find_in_set(otd.district, '" + cityVillage + "') ");
	    query.append("group by otd.province, year(otd.form_date), otd.session_topic ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getOneTouchSessionsData", e);
	}
    }
    
    @ApiOperation(value = "Get data for School Monitoring scores (ref: D1 - School Monitoring Scores by Province/D1 - School Monitoring Scores by District)")
    @GetMapping(value = "/report/schoolmonitoringdata")
    public ResponseEntity<?> getSchoolMonitoringData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    // Primary monitoring
	    query.append("select 'Primary' as school_level, ifnull(l.state_province, 'Unknown') as state_province, ifnull(l.city_village, 'Unknown') as city_village, date(pm.form_date) as form_date, upper(pm.primary_grade) as class_grade, upper(pm.program_type) as program_type, pm.monitoring_score as score, pm.monitoring_score_pct as percentage from _lse_primary_monitoring as pm ");
	    query.append("inner join location as l on l.location_id = pm.location_id ");
	    query.append("where date(pm.form_date) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append("and find_in_set(l.state_province, '" + stateProvince + "') ");
	    if (cityVillage != null) 
	    	query.append("and find_in_set(l.city_village, '" + cityVillage + "') ");
	    // Secondary monitoring
	    query.append(union);
	    query.append("select 'Secondary' as school_level, ifnull(l.state_province, 'Unknown') as state_province, ifnull(l.city_village, 'Unknown') as city_village, date(sm.form_date) as form_date, upper(sm.secondary_grade) as class_grade, 'LSBE' as program_type, sm.monitoring_score as score, sm.monitoring_score_pct as percentage from _lse_secondary_monitoring as sm ");
	    query.append("inner join location as l on l.location_id = sm.location_id ");
	    query.append("where date(sm.form_date) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getSchoolMonitoringData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Exit school planning (ref: D1 - Exit Planning)")
    @GetMapping(value = "/report/exitschoolplanningdata")
    public ResponseEntity<?> getExitSchoolPlanningData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select year(fd.form_date) as year, ifnull(l.state_province, 'Unknown') as state_province, ifnull(l.city_village, 'Unknown') as district, srhr.srhr_policy_implemented, ps.parent_session_conducted, count(*) as total from form_data as fd ");
	    query.append(locationJoinFormData);
	    query.append("inner join location_attribute as la on la.location_id = fd.location_id and la.attribute_type_id = 19 ");
	    query.append("inner join _lse_srhr_policy as srhr on srhr.form_id = fd.form_id ");
	    query.append("inner join _lse_parent_sessions as ps on ps.location_id = l.location_id ");
	    query.append(formDataVoidClause);
	    query.append("and la.attribute_value = 26 ");
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by year(fd.form_date), l.state_province, l.city_village, srhr.srhr_policy_implemented, ps.parent_session_conducted ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getExitSchoolPlanningData", e);
	}
    }

    @ApiOperation(value = "Get data for Partner Institutions (ref: D2 - Partner Institutions by District)")
    @GetMapping(value = "/report/partnerinstitutiondata")
    public ResponseEntity<?> getPartnerInstitutionData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(l.state_province, 'Unknown') as state_province, ifnull(l.city_village, 'Unknown') as city_village, ");
	    // Sum all the flags extracted from the JSON array
	    query.append("sum(json_contains(b.institute_types, '32', '$')) as total_medical, ");
	    query.append("sum(json_contains(b.institute_types, '33', '$')) as total_nursing, ");
	    query.append("sum(json_contains(b.institute_types, '34', '$')) as total_midwifery, ");
	    query.append("sum(json_contains(b.institute_types, '35', '$')) as total_other from location as l ");
	    query.append("inner join location_attribute as partnership on partnership.location_id = l.location_id and partnership.attribute_type_id = 7 ");
	    // We join with location_attribute table, which combines all attribute_value elements into a JSON array
	    query.append("inner join (select la.location_id, json_array(json_extract(attribute_value, '$[0].definitionId'), json_extract(attribute_value, '$[1].definitionId'), json_extract(attribute_value, '$[2].definitionId'), json_extract(attribute_value, '$[3].definitionId')) as institute_types from location_attribute as la where la.attribute_type_id = 8 and la.location_id) as b ");
	    query.append("on b.location_id = l.location_id ");
	    query.append("where l.voided = 0 ");
	    query.append("and date(partnership.attribute_value) between ");
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by l.state_province, l.city_village ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getPartnerInstitutionData", e);
	}
    }

    @ApiOperation(value = "Get data for SRHM Participant Training summary (ref: D2 - Participants Trained)")
    @GetMapping(value = "/report/participanttrainingdata")
    public ResponseEntity<?> getParticipantTrainingData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select date(fd.form_date) as form_date, ifnull(l.state_province, 'Unknown') as state_province, d.definition as participant_type, count(*) as total, per.gender as gender from form_data as fd ");
	    query.append(locationJoinFormData);
	    query.append(formParticipntJoinFormData);
	    query.append(participantJoinPerson);
	    query.append("inner join person as per on p.person_id = per.person_id "); 
	    query.append("inner join person_attribute as pa on pa.person_id = p.person_id and pa.attribute_type_id = 10 ");
	    query.append("inner join definition as d on d.definition_id = pa.attribute_value ");
	    query.append(formDataVoidClause);
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("and fd.form_type_id = 17 ");
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by fd.form_date, l.state_province, pa.attribute_value, per.gender ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getParticipantTrainingData", e);
	}
    }

    @ApiOperation(value = "Get data for Individuals reached through SRHM Activities (ref: D2 - Individuals Reached)")
    @GetMapping(value = "/report/individualreachdata")
    public ResponseEntity<?> getIndividualReachData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
		String groupByClause = "group by fd.province, fd.district, fd.form_date ";
	    StringBuilder query = new StringBuilder();
	    // Healthcare provider reach
	    query.append("select 'HCP' as activity_type, ifnull(fd.province, 'Unknown') as state_province, ifnull(fd.district, 'Unknown') as city_village, date(fd.form_date) as form_date, ");
	    query.append("sum(ifnull(fd.male_count, 0)) as male_count, sum(ifnull(fd.female_count, 0)) as female_count, sum(ifnull(fd.other_sex_count, 0)) as other_sex_count from _srhm_health_care_provider_reach as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(formDataProvinceFilter.replace(provincePlaceholder,stateProvince));
	    if (cityVillage != null)
	    	query.append(formDataDistrictFilter.replace(districtPlaceholder,cityVillage));
	    query.append(groupByClause);
	    // Step down training
	    query.append(union);
	    query.append("select 'Step Down' as activity_type, ifnull(fd.province, 'Unknown') as state_province, ifnull(fd.district, 'Unknown') as city_village, date(fd.form_date) as form_date, ");
	    query.append("sum(ifnull(fd.male_count, 0)) as male_count, sum(ifnull(fd.female_count, 0)) as female_count, sum(ifnull(fd.other_sex_count, 0)) as other_sex_count from _srhm_general_step_down_training_details as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(formDataProvinceFilter.replace(provincePlaceholder,stateProvince));
	    if (cityVillage != null)
	    	query.append(formDataDistrictFilter.replace(districtPlaceholder,cityVillage));
	    query.append(groupByClause);
	    // Amplify change step down training by Students
	    query.append(union);
	    query.append("select 'AC - Students' as activity_type, ifnull(fd.province, 'Unknown') as state_province, ifnull(fd.district, 'Unknown') as city_village, date(fd.form_date) as form_date, ");
	    query.append("sum(ifnull(fd.male_count, 0)) as male_count, sum(ifnull(fd.female_count, 0)) as female_count, sum(ifnull(fd.other_sex_count, 0)) as other_sex_count from _srhm_amplify_change_step_down_training_details as fd ");
	    query.append("inner join participant as pt on pt.identifier = fd.participant_id ");
	    query.append("inner join person_attribute as pa on pa.person_id = pt.person_id and pa.attribute_type_id = 10 and pa.attribute_value = 65 ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(formDataProvinceFilter.replace(provincePlaceholder,stateProvince));
	    if (cityVillage != null)
	    	query.append(formDataDistrictFilter.replace(districtPlaceholder,cityVillage));
	    query.append(groupByClause);
	    // Amplify change step down training by Teachers
	    query.append(union);
	    query.append("select 'AC - Teachers' as activity_type, ifnull(fd.province, 'Unknown') as state_province, ifnull(fd.district, 'Unknown') as city_village, date(fd.form_date) as form_date, ");
	    query.append("sum(ifnull(fd.male_count, 0)) as male_count, sum(ifnull(fd.female_count, 0)) as female_count, sum(ifnull(fd.other_sex_count, 0)) as other_sex_count from _srhm_amplify_change_step_down_training_details as fd ");
	    query.append("inner join participant as pt on pt.identifier = fd.participant_id ");
	    query.append("inner join person_attribute as pa on pa.person_id = pt.person_id and pa.attribute_type_id = 10 and pa.attribute_value = 66 ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(formDataProvinceFilter.replace(provincePlaceholder,stateProvince));
	    if (cityVillage != null)
	    	query.append(formDataDistrictFilter.replace(districtPlaceholder,cityVillage));
	    query.append(groupByClause);
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getIndividualReachData", e);
	}
    }
    
    @ApiOperation(value = "Get data for SRHM Amplify Change Participant (Students) Training summary (ref: D2 - Amplify Change Trained Participants - Students)")
    @GetMapping(value = "/report/amplifychangeparticipantdata/students")
    public ResponseEntity<?> getStudensAmplifyChangeParticipantData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select date(fd.form_date) as form_date, ifnull(l.state_province, 'Unknown') as state_province, l.city_village as city_village, d.definition as education_level, count(*) as total, per.gender as gender from form_data as fd "); 
	    query.append(locationJoinFormData);  
	    query.append(formParticipntJoinFormData);  
	    query.append(participantJoinPerson);  
	    query.append("inner join person as per on p.person_id = per.person_id "); 
	    query.append("inner join person_attribute as pa on pa.person_id = pt.person_id and pa.attribute_type_id = 10 and pa.attribute_value = 65 ");
	    query.append("inner join person_attribute as pa2 on pa2.person_id = p.person_id and pa2.attribute_type_id = 14 ");
	    query.append("inner join definition as d on d.definition_id = pa2.attribute_value ");
	    query.append(formDataVoidClause);
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("and fd.form_type_id = 15 ");
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by fd.form_date, l.state_province, pa2.attribute_value, per.gender ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getAmplifyChangeParticipantData", e);
	}
    }
    
    @ApiOperation(value = "Get data for SRHM Amplify Change Participant (Teachers) Training summary (ref: D2 - Amplify Change Trained Participants - Teachers)")
    @GetMapping(value = "/report/amplifychangeparticipantdata/teachers")
    public ResponseEntity<?> getTeachersAmplifyChangeParticipantData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select date(fd.form_date) as form_date, ifnull(l.state_province, 'Unknown') as state_province, l.city_village as city_village, d.definition as education_level, count(*) as total, per.gender as gender from form_data as fd "); 
	    query.append(locationJoinFormData);  
	    query.append(formParticipntJoinFormData);  
	    query.append(participantJoinPerson);  
	    query.append("inner join person as per on p.person_id = per.person_id "); 
	    query.append("inner join person_attribute as pa on pa.person_id = pt.person_id and pa.attribute_type_id = 10 and pa.attribute_value = 66 ");
	    query.append("inner join person_attribute as pa2 on pa2.person_id = p.person_id and pa2.attribute_type_id = 7 ");
	    query.append("inner join definition as d on d.definition_id = pa2.attribute_value ");
	    query.append(formDataVoidClause);
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("and fd.form_type_id = 15 ");
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by fd.form_date, l.state_province, pa2.attribute_value, per.gender ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getAmplifyChangeParticipantData", e);
	}
    }
    
    @ApiOperation(value = "Get data for SRHM Amplify Change Participant Training summary (ref: D2 - Amplify Change Trained Participants)")
    @GetMapping(value = "/report/amplifychangeparticipantdata")
    public ResponseEntity<?> getAmplifyChangeParticipantData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select date(fd.form_date) as form_date, ifnull(l.state_province, 'Unknown') as state_province, l.city_village as city_village, d.definition as participant_type, count(*) as total, per.gender as gender from form_data as fd "); 
	    query.append(locationJoinFormData);  
	    query.append(formParticipntJoinFormData);  
	    query.append(participantJoinPerson);  
	    query.append("inner join person as per on p.person_id = per.person_id "); 
	    query.append("inner join person_attribute as pa on pa.person_id = p.person_id and pa.attribute_type_id = 10 ");  
	    query.append("inner join definition as d on d.definition_id = pa.attribute_value ");  
	    query.append(formDataVoidClause);
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("and fd.form_type_id = 15 ");
	    if (stateProvince != null) 
	    	query.append(provinceFilter.replace(provincePlaceholder,stateProvince)); // "and find_in_set(l.state_province, '<stateProvince>') "
	    if (cityVillage != null)
	    	query.append(districtFilter.replace(districtPlaceholder,cityVillage));   // "and find_in_set(l.cityVillage, '<cityVillage>') "
	    query.append("group by fd.form_date, l.state_province, pa.attribute_value, per.gender ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getAmplifyChangeParticipantData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Social Media Traffic (ref: D3 - Daily Social Media Traffic)")
    @GetMapping(value = "/report/socialmediatraffic")
    public ResponseEntity<?> getSocialMediaTrafficData(@RequestParam("from") String from, @RequestParam("to") String to) throws HibernateException {
	try {
		String platformScoresNotNullFilter = "platform_scores is not null having platform is not null";
	    StringBuilder query = new StringBuilder();
	    query.append("select dayname(fd.form_date) as day_name, a.platform, ");
	    query.append("sum(a.likes) as likes, sum(a.shares) as shares, ifnull(sum(a.boosted_count), 0) as boosted_count ");
	    query.append("from _comms_social_media_details as fd ");
	    query.append("inner join ( ");
	    query.append("select form_id, replace(json_extract(platform_scores, '$[0].post_platform'), '\"', '') as platform, ");
	    query.append("json_extract(platform_scores, '$[0].post_likes_count') as likes, ");
	    query.append("json_extract(platform_scores, '$[0].post_shares_count') as shares, ");
	    query.append("json_extract(platform_scores, '$[0].post_boosted_count') as boosted_count from _comms_social_media_details ");
	    query.append(where + platformScoresNotNullFilter + " "); //"platform_scores is not null having platform is not null"
	    query.append(union);
	    query.append("select form_id, replace(json_extract(platform_scores, '$[1].post_platform'), '\"', '') as platform, ");
	    query.append("json_extract(platform_scores, '$[1].post_likes_count') as likes, ");
	    query.append("json_extract(platform_scores, '$[1].post_shares_count') as shares, ");
	    query.append("json_extract(platform_scores, '$[1].post_boosted_count') as boosted_count from _comms_social_media_details ");
	    query.append(where + platformScoresNotNullFilter + " "); //"platform_scores is not null having platform is not null"
	    query.append(union);
	    query.append("select form_id, replace(json_extract(platform_scores, '$[2].post_platform'), '\"', '') as platform, ");
	    query.append("json_extract(platform_scores, '$[2].post_likes_count') as likes, ");
	    query.append("json_extract(platform_scores, '$[2].post_shares_count') as shares, ");
	    query.append("json_extract(platform_scores, '$[2].post_boosted_count') as boosted_count from _comms_social_media_details ");
	    query.append(where + platformScoresNotNullFilter + " "); //"platform_scores is not null having platform is not null"
	    query.append(union);
	    query.append("select form_id, replace(json_extract(platform_scores, '$[3].post_platform'), '\"', '') as platform, ");
	    query.append("json_extract(platform_scores, '$[3].post_likes_count') as likes, ");
	    query.append("json_extract(platform_scores, '$[3].post_shares_count') as shares, ");
	    query.append("json_extract(platform_scores, '$[3].post_boosted_count') as boosted_count from _comms_social_media_details ");
	    query.append(where + platformScoresNotNullFilter + ") as a on a.form_id = fd.form_id "); //"platform_scores is not null having platform is not null"
	    query.append(andDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("group by dayname(fd.form_date), a.platform ");
	    query.append("order by weekday(fd.form_date) ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getSocialMediaTrafficData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Radio Listenership and Live calls by day (ref: D3 - Radio Listenership by Day)")
    @GetMapping(value = "/report/radiocalldata")
    public ResponseEntity<?> getRadioLiveCallData(@RequestParam("from") String from, @RequestParam("to") String to) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select dayname(fd.form_date) as day_name, fd.radio_channel_frequency, fd.radio_channel_name, ifnull(fd.city, 'Unknown') as city, sum(fd.listener_count) as listener_count, sum(fd.live_call_count) as live_call_count from _comms_radio_appearance as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("group by fd.city, dayname(fd.form_date), fd.radio_channel_frequency, fd.radio_channel_name ");
	    query.append("order by weekday(fd.form_date) ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getRadioLiveCallData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Cinema and Theatre (ref: D3 - Mobile Cinema/Theatres by District)")
    @GetMapping(value = "/report/mobilecinemadata")
    public ResponseEntity<?> getMobileCinemaData(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam(required = false, name = "state_province") String stateProvince, @RequestParam(required = false, name = "city_village") String cityVillage) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(fd.province, 'Unknown') as state_province, ifnull(fd.district, 'Unknown') as city_village, fd.screening_type, date(fd.form_date) as form_date, count(*) as total from _comms_mobile_cinema_theatre_details as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    if (stateProvince != null) 
	    	query.append(formDataProvinceFilter.replace(provincePlaceholder,stateProvince));
	    if (cityVillage != null)
	    	query.append(formDataDistrictFilter.replace(districtPlaceholder,cityVillage));
	    query.append("group by fd.province, fd.district, fd.screening_type, date(fd.form_date) ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getMobileCinemaData", e);
	}
    }
    
    @ApiOperation(value = "Get data for various Material Distribution in Communications (ref: D3 - IEC Material Distribution)")
    @GetMapping(value = "/report/materialdistributiondata")
    public ResponseEntity<?> getMaterialDistributionData(@RequestParam("from") String from, @RequestParam("to") String to) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(fd.partner_components, 'Unknown') as partner_component, fd.distribution_location as distribution_location, date(fd.form_date) as form_date, ");	    
	    query.append("sum(ifnull(fd.annual_report_count, 0)) as annual_report_count, ");
	    query.append("sum(ifnull(fd.aahung_profile_count, 0)) as aahung_profile_count, ");
	    query.append("sum(ifnull(fd.phamplet_count, 0)) as pamphlet_count, ");
	    query.append("sum(ifnull(fd.booklet_count, 0)) as booklet_count, ");
	    query.append("sum(ifnull(fd.report_count, 0)) as report_count, ");
	    query.append("sum(ifnull(fd.aahung_mugs_count, 0)) as aahung_mugs_count, ");
	    query.append("sum(ifnull(fd.aahung_folders_count, 0)) as aahung_folders_count, ");
	    query.append("sum(ifnull(fd.aahung_notebooks_count, 0)) as aahung_notebooks_count, ");
	    query.append("sum(ifnull(fd.other_material_count, 0)) as other_material_count, ");
	    query.append("sum(ifnull(fd.aahung_info_count, 0)) as aahung_info_count, ");
	    query.append("sum(ifnull(fd.nikkah_nama_count, 0)) as nikkah_nama_count, ");
	    query.append("sum(ifnull(fd.puberty_count, 0)) as puberty_count, ");
	    query.append("sum(ifnull(fd.rti_count, 0)) as rti_count, ");
	    query.append("sum(ifnull(fd.ungei_count, 0)) as ungei_count, ");
	    query.append("sum(ifnull(fd.sti_count, 0)) as sti_count, ");
	    query.append("sum(ifnull(fd.sexual_health_count, 0)) as sexual_health_count, ");
	    query.append("sum(ifnull(fd.premarital_info_count, 0)) as premarital_info_count, ");
	    query.append("sum(ifnull(fd.pac_count, 0)) as pac_count, ");
	    query.append("sum(ifnull(fd.maternal_health_count, 0)) as maternal_health_count, ");
	    query.append("sum(ifnull(fd.other_topic_count, 0)) as other_topic_count, ");
	    query.append("count(*) as total from _comms_distribution_of_communication_material as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    query.append("group by fd.partner_components, fd.distribution_location, date(fd.form_date) ");
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getMaterialDistributionData", e);
	}
    }
    
    @ApiOperation(value = "Get data for Communications Trainings (ref: D3 - Communications Training)")
    @GetMapping(value = "/report/communicationstrainingdata")
    public ResponseEntity<?> getCommunicationsTrainingData(@RequestParam("from") String from, @RequestParam("to") String to) throws HibernateException {
	try {
	    StringBuilder query = new StringBuilder();
	    query.append("select ifnull(fd.city, 'Unknown') as city_village, date(fd.form_date) as form_date, ");
	    query.append("json_extract(fd.topic_covered, '$.values') like '%srhr%' as covered_srhr, ");
	    query.append("json_extract(fd.topic_covered, '$.values') like '%agency_choice%' as covered_agency_choice, ");
	    query.append("json_extract(fd.topic_covered, '$.values') like '%gender_sensitization%' as covered_gender_sensitization, ");
	    query.append("json_extract(fd.topic_covered, '$.values') like '%other%' as covered_other, ");
	    query.append("ifnull(fd.journalist_count, 0) as journalist_count, ");
	    query.append("ifnull(fd.blogger_count, 0) as blogger_count, ");
	    query.append("ifnull(fd.screenwriter_count, 0) as screenwriter_count, ");
	    query.append("ifnull(fd.other_media_count, 0) as other_media_count, ");
	    query.append("ifnull(fd.other_attendant_count, 0) as other_attendant_count from _comms_training_details_communications as fd ");
	    query.append(whereDateFilter);
	    query.append(dateFilterToFrom.replace(dateFromPlaceholder, from).replace(dateToPlaceholder, to));
	    JSONArray data = service.getTableDataAsJson(query.toString());
	    return ResponseEntity.ok().body(data.toString());
	} catch (SQLException | JSONException e) {
	    return exceptionFoundResponse("Executing getMobileCinemaData", e);
	}
    }
}
