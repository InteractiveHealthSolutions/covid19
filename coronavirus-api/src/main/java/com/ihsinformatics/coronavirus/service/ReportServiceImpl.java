/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ihsinformatics.coronavirus.Context;
import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.opencsv.CSVWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class ReportServiceImpl extends BaseService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;

    @Value("${report.data.dir}")
    private String dataDirectory;

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public String generateJasperReport(String reportName, String extension, Map<String, String> params) throws JRException, SQLException {
	InputStream employeeReportStream = getClass().getResourceAsStream("/rpt/" + reportName + ".jrxml");
	JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
	// Save the report as Jasper to avaoid compilation in the future
	JRSaver.saveObject(jasperReport, reportName +".jasper");
	
	Map<String, Object> parameters = new HashMap<>();
	
	String startDate = params.get("start_date");
	String format = DateTimeUtil.detectDateFormat(startDate); 
	Date sDate = DateTimeUtil.fromString(startDate, format);
	parameters.put("start_date",sDate);
	
	String endDate = params.get("end_date");
	String format1 = DateTimeUtil.detectDateFormat(endDate); 
	Date eDate = DateTimeUtil.fromString(endDate, format1);
	parameters.put("end_date",eDate);
	  	
   	params.remove("start_date");
   	params.remove("end_date");
   	
   	Iterator it = params.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        parameters.put(pair.getKey().toString() , pair.getValue());
        it.remove(); // avoids a ConcurrentModificationException
    }
    
	String filePath = dataDirectory+reportName;
   	
    try(Connection connection = dataSource.getConnection()){
		// Fill report on provided data source
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
	
		
		if(extension.equals("html")){
			filePath = filePath+".html";
			exportAsHTML(jasperPrint, filePath);
		} else if(extension.equals("csv")){
			filePath = filePath+".csv";
			exportAsCSV(jasperPrint,  filePath);
		} else if(extension.equals("xls")) {
			filePath = filePath+".xls";
			exportAsXLS(jasperPrint,  filePath);
		} else if(extension.equals("pdf")){
			filePath = filePath+".pdf";
			exportAsPDF(jasperPrint,  filePath);
		}
    }
	
	return filePath;
    }

    public void exportAsHTML(JasperPrint jasperPrint, String filePath) throws JRException {
	HtmlExporter exporter = new HtmlExporter();
	// Set input
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// Set output
	exporter.setExporterOutput(new SimpleHtmlExporterOutput(filePath));
	// Report config
	SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
	exporter.setConfiguration(reportConfig);
	// Export config
	SimpleHtmlExporterConfiguration exportConfig = new SimpleHtmlExporterConfiguration();
	exporter.setConfiguration(exportConfig);
	exporter.exportReport();
    }

    public void exportAsCSV(JasperPrint jasperPrint, String filePath) throws JRException {
	JRCsvExporter exporter = new JRCsvExporter();
	// Set input
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// Set output
	exporter.setExporterOutput(new SimpleWriterExporterOutput(filePath));
	// Report config
	SimpleCsvReportConfiguration reportConfig = new SimpleCsvReportConfiguration();
	exporter.setConfiguration(reportConfig);
	// Export config
	SimpleCsvExporterConfiguration exportConfig = new SimpleCsvExporterConfiguration();
	exporter.setConfiguration(exportConfig);
	exporter.exportReport();
    }

    public void exportAsXLS(JasperPrint jasperPrint, String filePath) throws JRException {
	JRXlsxExporter exporter = new JRXlsxExporter();
	// Set input
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// Set output
	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
	// Report config
	SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
	exporter.setConfiguration(reportConfig);
	reportConfig.setSheetNames(new String[] { "Employee Data" });
	// Export config
	SimpleXlsxExporterConfiguration exportConfig = new SimpleXlsxExporterConfiguration();
	exporter.setConfiguration(exportConfig);
	exporter.exportReport();
    }

    public void exportAsPDF(JasperPrint jasperPrint, String filePath) throws JRException {
	JRPdfExporter exporter = new JRPdfExporter();
	// Set input
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	// Set output
	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
	// Report config
	SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
	reportConfig.setSizePageToContent(true);
	reportConfig.setForceLineBreakPolicy(false);
	exporter.setConfiguration(reportConfig);
	// Export config
	SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
	exportConfig.setMetadataAuthor("IHS");
	exportConfig.setEncrypted(true);
	exportConfig.setAllowedPermissionsHint("PRINTING");
	exporter.setConfiguration(exportConfig);
	exporter.exportReport();
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormType")
    public String generateFormTypesCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select ft.form_type_id, ft.uuid, ft.form_name, ft.short_name, ft.description, ft.form_schema, d.short_name as component, ft.version, ft.date_created, ft.retired FROM form_type as ft ");
	query.append("inner join definition as d on d.definition_id = ft.form_group ");
	makeDirectory();
	String fileName = "formtypes.csv";
	return writeToFile(fileName,query.toString());
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Metadata")
    public String generateLocationAttributeTypesCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
			"Select attribute_type_id, uuid, attribute_name, short_name, description, datatype, validation_regex, date_created, retired FROM location_attribute_type;");
	makeDirectory();
	String fileName = "locationAttributeTypes.csv";
	return writeToFile(fileName,query.toString());
    }
    
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public String generateLocationAttributesCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
			"select la.attribute_id, la.uuid, l.short_name as location, lat.short_name as attribute_type, la.attribute_value, c.username as created_by, la.date_created, la.voided from location_attribute la ");
	query.append("inner join location as l on l.location_id = la.location_id ");
	query.append("inner join location_attribute_type as lat on lat.attribute_type_id = la.attribute_type_id ");
	query.append("left join users as c on c.user_id = la.created_by ");
	makeDirectory();
	String fileName = "locationAttributes.csv";
	return writeToFile(fileName,query.toString());
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Metadata")
    public String generatePersonAttributeTypesCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
			"Select attribute_type_id, uuid, attribute_name, short_name, description, datatype, validation_regex, date_created, retired FROM person_attribute_type;");
	makeDirectory();
	String fileName = "personAttributeTypes.csv";
	return writeToFile(fileName,query.toString());
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View People")
    public String generatePersonAttributesCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
			"select pa.attribute_id, pa.uuid, pat.identifier as participant, pt.short_name as attribute_type, pa.attribute_value, pa.date_created, c.username as created_by, pa.voided from person_attribute pa ");
	query.append("inner join participant as pat on pat.person_id = pa.person_id ");
	query.append("inner join person_attribute_type as pt on pt.attribute_type_id = pa.attribute_type_id ");
	query.append("left join users as c on c.user_id = pa.created_by ");
	String fileName = "personAttributes.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Definition")
    public String generateDefinitionsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select d.definition_id, d.uuid, t.short_name as definition_type, d.definition, d.short_name, d.description, d.retired, d.date_created from definition as d ");
	query.append("inner join definition_type as t on t.definition_type_id = d.definition_type_id order by definition_id ");
	String fileName = "definitions.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Donor")
    public String generateDonorsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select d.donor_id, d.uuid, d.donor_name, d.short_name, c.username as created_by, d.date_created, d.voided from donor as d ");
	query.append("inner join users as c on c.user_id = d.created_by ");
	String fileName = "donors.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Element")
    public String generateElementsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select e.element_id, e.uuid, e.element_name, e.description, e.short_name, e.datatype, e.validation_regex, e.date_created, e.retired from element as e ");
	String fileName = "elements.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View FormData")
    public String generateFormDataCSV(String formTypeName) throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append("select t.short_name as form_type, l.short_name as location, f.* from _");
	query.append(formTypeName.toLowerCase());
	query.append(" as f ");
	query.append("inner join form_type as t on t.form_type_id = f.form_type_id ");
	query.append("left outer join location as l on l.location_id = f.location_id ");
	String fileName = "formdata-" + formTypeName + ".csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Location")
    public String generateLocationsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select l.location_id, l.uuid, l.location_name, l.short_name, l.description, d.short_name as category, p.short_name as parent_location, l.address1, l.address2, l.address3, l.city_village, l.state_province, l.country, l.email, l.landmark1, l.landmark2, l.latitude, l.longitude, l.postal_code, l.primary_contact, l.primary_contact_person, l.secondary_contact, l.secondary_contact_person, l.tertiary_contact, l.tertiary_contact_person, c.username as created_by, l.date_created, l.voided from location as l ");
	query.append("inner join definition as d on d.definition_id = l.category ");
	query.append("left outer join location as p on p.location_id = l.parent_location ");
	query.append("inner join users as c on c.user_id = l.created_by ");
	String fileName = "locations.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Project")
    public String generateProjectsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select p.project_id, p.uuid, p.project_name, p.short_name, d.short_name as donor, c.username as created_by, p.date_created, p.voided from project as p ");
	query.append("inner join donor as d on d.donor_id = p.donor_id ");
	query.append("left join users as c on c.user_id = p.created_by ");
	String fileName = "projects.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View People")
    public String generateParticipantsCSV() throws FileNotFoundException {
	StringBuilder query = new StringBuilder();
	query.append(
		"select pat.person_id, pat.uuid, pat.identifier, p.first_name as name, p.gender, p.dob,  l.short_name as location, c.username as created_by, pat.date_created, pat.voided from participant pat ");
	query.append("inner join person as p on p.person_id = pat.person_id ");
	query.append("inner join location as l on l.location_id = pat.location_id ");
	query.append("left join users as c on c.user_id = p.created_by ");
	String fileName = "participants.csv";
	makeDirectory();
	return writeToFile(fileName,query.toString());
    }

    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View User")
    public String generateUsersCSV() throws FileNotFoundException {
	String query = "select u.user_id, u.uuid, u.username, u.full_name, u.voided, u.date_created from users as u ";
	String fileName = "users.csv";
	makeDirectory();
	String filePath = getDataDirectory() + "dumps//" + fileName;
	writeToCsv(query, filePath);
	return filePath;
    }
    
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Role")
    public String generateRolesCSV() throws FileNotFoundException {
	String query = "select r.role_id, r.role_name, rp.privilege_name from role_privilege as rp " + 
			"inner join role as r on r.role_id = rp.role_id;";
	String fileName = "roles.csv";
	makeDirectory();
	String filePath = getDataDirectory() + "dumps//" + fileName;
	writeToCsv(query, filePath);
	return filePath;
    }

    /**
     * Returns {@link ResultSet} object of given query. The connection is fetched
     * from Session
     * 
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException {
	try(Connection conn = getSession().getSessionFactory().getSessionFactoryOptions().getServiceRegistry()
		.getService(ConnectionProvider.class).getConnection();){
		return getResultSet(sql, conn);
	}
    }

    /**
     * Returns {@link ResultSet} object of given query
     * 
     * @param query
     * @param conn
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String query, Connection conn) throws SQLException {
	try (ResultSet resultSet = conn.createStatement().executeQuery(query);) {
	    RowSetFactory factory = RowSetProvider.newFactory();
	    CachedRowSet crs = factory.createCachedRowSet();
	    crs.populate(resultSet);
	    return crs;
	}
    }
    
    /**
     * Returns data from given query as a List of String arrays
     * 
     * @param query the SQL query
     * @param page  page number to fetch, if null, the first page will be retrieved
     * @param size  the number of records to fetch, if null, then the limit will be
     *              restricted to the one defined in app settings
     * @return
     * @throws SQLException
     */
    public List<String[]> getTableData(String query, Integer page, Integer size) throws SQLException {
	List<String[]> data = new ArrayList<>();
	if (page != null && size != null) {
	    query = query + " limit " + (page - 1) * size + ", " + size;
	} else {
	    query = query + " limit " + Context.MAX_RESULT_SIZE;
	}
	ResultSet resultSet = getResultSet(query, dataSource.getConnection());
	int columns = resultSet.getMetaData().getColumnCount();
	while (resultSet.next()) {
	    String[] record = new String[columns];
	    for (int i = 0; i < columns; i++) {
		record[i] = resultSet.getString(i + 1);
	    }
	    data.add(record);
	}
	resultSet.close();
	return data;
    }
    
    /**
     * Returns data from given query as a List of String arrays
     * 
     * @param query the SQL query
     * 
     * @return
     * @throws SQLException
     */
    public List<String[]> getTableData(String query) throws SQLException {
	List<String[]> data = new ArrayList<>();
	ResultSet resultSet = getResultSet(query, dataSource.getConnection());
	int columns = resultSet.getMetaData().getColumnCount();
	while (resultSet.next()) {
	    String[] record = new String[columns];
	    for (int i = 0; i < columns; i++) {
		record[i] = resultSet.getString(i + 1);
	    }
	    data.add(record);
	}
	resultSet.close();
	return data;
    }
    
    /**
     * Returns data from given query as a {@link JSONArray} of {@link JSONObject}
     * 
     * @param query
     * @return
     * @throws SQLException
     */
    public JSONArray getTableDataAsJson(String query) throws SQLException, JSONException {
	JSONArray json = new JSONArray();
	try(Connection conn =  dataSource.getConnection();){
	ResultSet resultSet = getResultSet(query, conn);
	ResultSetMetaData metadata = resultSet.getMetaData();
	while (resultSet.next()) {
	    int numColumns = metadata.getColumnCount();
	    JSONObject obj = new JSONObject();
	    for (int i = 1; i < numColumns + 1; i++) {
		String columnName = metadata.getColumnName(i);
		String displayName = metadata.getColumnLabel(i);
		switch (metadata.getColumnType(i)) {
		case Types.ARRAY:
		    obj.put(displayName, resultSet.getArray(columnName));
		    break;
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
		    obj.put(displayName, resultSet.getInt(columnName));
		    break;
		case Types.BOOLEAN:
		    obj.put(displayName, resultSet.getBoolean(columnName));
		    break;
		case Types.DATE:
		case Types.TIMESTAMP:
		    obj.put(displayName, resultSet.getDate(columnName));
		    break;
		case Types.DOUBLE:
		case Types.FLOAT:
		    obj.put(displayName, resultSet.getDouble(columnName));
		    break;
		case Types.NVARCHAR:
		case Types.VARCHAR:
		    obj.put(displayName, resultSet.getString(i));
		    break;
		case Types.BLOB:
		    obj.put(displayName, resultSet.getBlob(columnName));
		    break;
		default:
		    obj.put(displayName, resultSet.getString(columnName));
		}
	    }
	    json.put(obj);
	}
	}
	return json;
    }

    /**
     * Writes the results from given query into the filePath
     * 
     * @param query
     * @param filePath
     * @throws FileNotFoundException
     */
    public void writeToCsv(String query, String filePath) throws FileNotFoundException {
	PrintWriter writer = new PrintWriter(filePath);
	try (CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
		CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);) {
	    ResultSet data = getResultSet(query, dataSource.getConnection());
	    csvWriter.writeAll(data, true);
	    data.close();
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	}
    }
    
    public void makeDirectory(){
    	
    	 File file = new File(getDataDirectory() + "dumps");
         if (!file.exists()) 
             file.mkdir();
             
    }

    /**
     * @return the dataDirectory
     */
    public String getDataDirectory() {
	return dataDirectory;
    }

    /**
     * @param dataDirectory the dataDirectory to set
     */
    public void setDataDirectory(String dataDirectory) {
	this.dataDirectory = dataDirectory;
    }
    
    public String writeToFile(String fileName, String query) throws FileNotFoundException{
    	String filePath = getDataDirectory() + "dumps//" + fileName;
    	PrintWriter writer = new PrintWriter(filePath);
    	try (CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
    			CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
    			Connection conn =  dataSource.getConnection();) {
    		    ResultSet data = getResultSet(query, conn);
    	    csvWriter.writeAll(data, true);
    	    data.close();
    	} catch (SQLException | IOException e) {
    	    LOG.error(e.getMessage());
    	}
    	return filePath;
    }
}
