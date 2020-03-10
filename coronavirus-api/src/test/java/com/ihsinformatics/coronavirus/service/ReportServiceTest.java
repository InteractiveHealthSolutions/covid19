/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.service.ReportServiceImpl;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockDataSource;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockStatement;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 * @author owais.hussain@ihsinformatics.com
 */


@RunWith(SpringRunner.class)
@DataJpaTest
public class ReportServiceTest extends BaseServiceTest {
	
	@Mock
	DataSource ds = new MockDataSource();
	
	@Mock
	Connection conn = new MockConnection();
	
	@Mock
    Statement statement = new MockStatement(conn);

	@Mock
    private ReportServiceImpl reportServiceMock;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
	MockitoAnnotations.initMocks(this);
	reportService.setDataDirectory("../");

	when(ds.getConnection()).thenReturn(conn);
    when(conn.createStatement()).thenReturn(statement);
    
    }

    public String[] readLines(String filePath) {
	File file = new File(filePath);
	ArrayList<String> lines = new ArrayList<String>();
	try {
	    FileInputStream fis = new FileInputStream(file);
	    DataInputStream dis = new DataInputStream(fis);
	    BufferedReader br = new BufferedReader(new InputStreamReader(dis));
	    String strLine;
	    while ((strLine = br.readLine()) != null) {
		lines.add(strLine);
	    }
	    dis.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return lines.toArray(new String[] {});
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#exportAsHTML(net.sf.jasperreports.engine.JasperPrint, java.lang.String)}.
     * @throws JRException, SQLException 
     */
    @Test
    public void testExportAsHTML() throws JRException, SQLException {
	
    	String path = System.getProperty("user.dir");
    	
	   	File file = new File(path + "\\report.html");
	   	file.delete();
	
	   	InputStream employeeReportStream = getClass().getResourceAsStream("/rpt/Sample Report.jrxml");
	   	JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
	   	// Save the report as Jasper to avaoid compilation in the future
	   	JRSaver.saveObject(jasperReport, "employeeReport.jasper");
	   	// Attach parameters
	   	Map<String, Object> parameters = new HashMap<>();
	   	parameters.put("title", "Employee Report");
	   	parameters.put("minSalary", 15000.0);
	   	parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
	   	
	   	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds.getConnection());
	
	   	reportService.exportAsCSV(jasperPrint, "report.html");
	   	file = new File(path + "\\report.html");
	   	
	   	assertNotNull(file);
    	
    	
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#exportAsCSV(net.sf.jasperreports.engine.JasperPrint, java.lang.String)}.
     * @throws JRException 
     * @throws SQLException 
     */
    @Test
    public void testExportAsCSV() throws JRException, SQLException {
	
    	String path = System.getProperty("user.dir");
    	
	   	File file = new File(path + "\\report.csv");
	   	file.delete();
	
	   	InputStream employeeReportStream = getClass().getResourceAsStream("/rpt/Sample Report.jrxml");
	   	JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
	   	// Save the report as Jasper to avaoid compilation in the future
	   	JRSaver.saveObject(jasperReport, "employeeReport.jasper");
	   	// Attach parameters
	   	Map<String, Object> parameters = new HashMap<>();
	   	parameters.put("title", "Employee Report");
	   	parameters.put("minSalary", 15000.0);
	   	parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
	   	
	   	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds.getConnection());
	
	   	reportService.exportAsCSV(jasperPrint, "report.csv");
	   	file = new File(path + "\\report.csv");
	   	
	   	assertNotNull(file);
    	
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#exportAsXLS(net.sf.jasperreports.engine.JasperPrint, java.lang.String)}.
     * @throws JRException 
     * @throws SQLException 
     */
    @Test
    public void testExportAsXLS() throws JRException, SQLException {
	
    	String path = System.getProperty("user.dir");
    	
	   	File file = new File(path + "\\report.xls");
	   	file.delete();
	
	   	InputStream employeeReportStream = getClass().getResourceAsStream("/rpt/Sample Report.jrxml");
	   	JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
	   	// Save the report as Jasper to avaoid compilation in the future
	   	JRSaver.saveObject(jasperReport, "employeeReport.jasper");
	   	// Attach parameters
	   	Map<String, Object> parameters = new HashMap<>();
	   	parameters.put("title", "Employee Report");
	   	parameters.put("minSalary", 15000.0);
	   	parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
	   	
	   	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds.getConnection());
	
	   	reportService.exportAsXLS(jasperPrint, "report.xls");
	   	file = new File(path + "\\report.xls");
	   	
	   	assertNotNull(file);
   	
    	
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#exportAsPDF(net.sf.jasperreports.engine.JasperPrint, java.lang.String)}.
     * @throws JRException 
     * @throws SQLException 
     */
    @Test
    public void testExportAsPDF() throws JRException, SQLException {
    	
    	String path = System.getProperty("user.dir");
    	
    	File file = new File(path + "\\report.pdf");
    	file.delete();

    	InputStream employeeReportStream = getClass().getResourceAsStream("/rpt/Sample Report.jrxml");
    	JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
    	// Save the report as Jasper to avaoid compilation in the future
    	JRSaver.saveObject(jasperReport, "employeeReport.jasper");
    	// Attach parameters
    	Map<String, Object> parameters = new HashMap<>();
    	parameters.put("title", "Employee Report");
    	parameters.put("minSalary", 15000.0);
    	parameters.put("condition", " LAST_NAME ='Smith' ORDER BY FIRST_NAME");
    	
    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds.getConnection());

    	reportService.exportAsPDF(jasperPrint, "report.pdf");
    	file = new File(path + "\\report.pdf");
    	
    	assertNotNull(file);
    	
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateDefinitionsCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateDefinitionsCSV() throws SQLException, FileNotFoundException {

    	MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("definition_id", new Object[] { 1,2,3 });
        rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
        rs.addColumn("definition_type", new Object[] { "Location Type", "Location Type", "Country" });
        rs.addColumn("definition", new Object[] { "School", "Market", "England" });
        rs.addColumn("short_name", new Object[] { "SCHOOL","MARKET", "EN" });
        rs.addColumn("description", new Object[] { "School","Market Place","2019-10-01" });
        rs.addColumn("retired", new Object[] { false,false, false });
        rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
        	
        when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);

    	String filePath = reportService.generateDefinitionsCSV();
    	String[] lines = readLines(filePath);
    	assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    	
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateDonorsCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateDonorsCSV() throws SQLException, FileNotFoundException {
    	MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("donor_id", new Object[] { 1,2 });
        rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4" });
        rs.addColumn("donor_name", new Object[] { "Unknown Donor", "Ministry of Magic" });
        rs.addColumn("short_name", new Object[] { "UNKNOWN","MoM" });
        rs.addColumn("created_by", new Object[] { "admin","admin" });
        rs.addColumn("voided", new Object[] {false, false});

        when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
   
    	String filePath = reportService.generateDonorsCSV();
    	String[] lines = readLines(filePath);
    	assertEquals(2 + 1, lines.length);  // +1 for header/column-name
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateElementsCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateElementsCSV() throws SQLException, FileNotFoundException {
    	
    	MockResultSet rs = new MockResultSet("rs");
        rs.addColumn("element_id", new Object[] { 1,2,3 });
        rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
        rs.addColumn("element_name", new Object[] { "School Name", "House", "Date Joined" });
        rs.addColumn("description", new Object[] { "School Name", "Houses Name School students are divided into", "Teacher joining Date" });
        rs.addColumn("short_name", new Object[] { "SCHOOL","HOUSE", "JOIN_DATE" });
        rs.addColumn("data_type", new Object[] { "LOCATION","DEFINITION","DATE" });
        rs.addColumn("validation_regex", new Object[] { null, null, null });
        rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
        rs.addColumn("retired", new Object[] { false,false, false });

        	
        when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);

    	String filePath = reportService.generateElementsCSV();
    	String[] lines = readLines(filePath);
    	assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateFormDataCSV(java.lang.String)}.
     */
    /*@Test
    public void testGenerateFormDataCSV() {
	fail("Not yet implemented"); // TODO
    }*/

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateLocationsCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateLocationsCSV() throws SQLException, FileNotFoundException {
    	
	 	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("location_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("location_name", new Object[] { "Hogwarts School of Witchcraft and Wizardry", "Diagon Alley", "The Burrow" });
	    rs.addColumn("short_name", new Object[] { "HSWW", "DALLEY", "BURROW" });
	    rs.addColumn("description", new Object[] { "School for Witchcraft and Wizards","Market Place", "Home for Weasely" });
	    rs.addColumn("category", new Object[] { "SCHOOL","MARKET","HOME" });
	    rs.addColumn("parent_location", new Object[] {null, null, null });
	    rs.addColumn("address1", new Object[] { null, null, null });
	    rs.addColumn("address2", new Object[] { null, null, null});
	    rs.addColumn("address3", new Object[] { null, null, null });
	    rs.addColumn("city_village", new Object[] { null,null,"Ottery St Catchpole" });
	    rs.addColumn("state_province", new Object[] { null,null,"Devon" });
	    rs.addColumn("country", new Object[] { "Scotland", "England", "England" });
	    rs.addColumn("email", new Object[] { null,null,null });
	    rs.addColumn("landmark1", new Object[] { null,null,null  });
	    rs.addColumn("landmark2", new Object[] { null,null,null  });
	    rs.addColumn("latitude", new Object[] { null,null,null });
	    rs.addColumn("longitude", new Object[] { null,null,null });
	    rs.addColumn("postal_code", new Object[] { null, null, null });
	    rs.addColumn("primary_contact", new Object[] { "0300-7777777","0300-7777777","0300-7777777" });
	    rs.addColumn("primary_contact_person", new Object[] { "Vernon Dursley","Petunia Dursley","Dudley Dursley" });
	    rs.addColumn("secondary_contact", new Object[] { "0300-0000000","0300-7777777",null });
	    rs.addColumn("secondary_contact_person", new Object[] { "Peter Weasely","Hannah Abbott",null });
	    rs.addColumn("tertiary_contact", new Object[] { null,"0300-2992999",null });
	    rs.addColumn("tertiary_contact_person", new Object[] { null,"Hadi",null });
	    rs.addColumn("created_by", new Object[] { "admin","albus.dumbledore","severus.snape" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    rs.addColumn("voided", new Object[] { false,false,false });
	    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);

		String filePath = reportService.generateLocationsCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
		
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateProjectsCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateProjectsCSV() throws SQLException, FileNotFoundException {
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("project_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("project_name", new Object[] { "Triwizard Tournament", "Quidditch World Cup 1473", "Quidditch World Cup 1994" });
	    rs.addColumn("short_name", new Object[] { "MOM-TT-1994", "Q-WC-1473", "Q-WC-1994" });
	    rs.addColumn("donor", new Object[] { "MoM", "UNKNOWN", "MoM" });
	    rs.addColumn("created_by", new Object[] { "admin", "admin", "admin" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    rs.addColumn("voided", new Object[] { false,false, false });
	    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateProjectsCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generatePersonAttributesCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGeneratePersonAttributesCSV() throws SQLException, FileNotFoundException {
    	    	    	    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("attribute_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("participant", new Object[] { "SEEKER", "KEEPER", "CHASER" });
	    rs.addColumn("attribute_type", new Object[] { "HT", "STATUS", "STATUS" });
	    rs.addColumn("attribute_value", new Object[] { "6", "MIDDLE CLASS", "MIDDLE CLASS" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    rs.addColumn("created_by", new Object[] { "admin", "admin", "ron" });
	    rs.addColumn("voided", new Object[] { false, false, false });

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generatePersonAttributesCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generatePersonAttributeTypesCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGeneratePersonAttributeTypesCSV() throws SQLException, FileNotFoundException {
    	    	    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("attribute_type_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("attribute_name", new Object[] { "HEIGHT", "SOCIAL STATUS", "NATIONALITY" });
	    rs.addColumn("short_name", new Object[] { "HT", "STATUS", "NATIONALITY" });
	    rs.addColumn("description", new Object[] { "Height of Person", "Social Status of Person in society", "Nationalitu of the person" });
	    rs.addColumn("datatype", new Object[] { "INTEGER", "DEFINITION", "STRING" });
	    rs.addColumn("validation_regex", new Object[] { "range=1-19", null, null });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    rs.addColumn("retired", new Object[] { false,false, false });

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateRolesCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateRolesCSV()}.
     * @throws SQLException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testGenerateRolesCSV() throws SQLException, FileNotFoundException {
    	    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("role_id", new Object[] { 1,2,3 });
	    rs.addColumn("role_name", new Object[] { "Auror", "Auror", "Headmaster" });
	    rs.addColumn("privilege_name", new Object[] { "KILL", "ARREST", "USE MAGIC" });
	    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateRolesCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateUsersCSV()}.
     * 
     * @throws FileNotFoundException
     * @throws SQLException 
     */
    @Test
    public void testGenerateUsersCSV() throws FileNotFoundException, SQLException {
    		
	    MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("user_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("username", new Object[] { "albus.dumbledore", "severus.snape", "nymphadora.tonks" });
	    rs.addColumn("full_name", new Object[] { "Albus Dumbledore", "Severus Snape", "Nymphadora Tonks" });
	    rs.addColumn("voided", new Object[] { false,false, false });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateUsersCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateParticpantsCSV()}.
     * 
     * @throws FileNotFoundException
     * @throws SQLException 
     */
    @Test
    public void testGenerateParticpantsCSV() throws FileNotFoundException, SQLException {
    	    		
	    MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("person_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("identifier", new Object[] { "KEEPER", "SEEKER", "CHASER" });
	    rs.addColumn("name", new Object[] { "Ronald Weasely", "Harry POTTER", "Hermione Granger" });
	    rs.addColumn("gender", new Object[] { "Male","Male", "Female" });
	    rs.addColumn("dob", new Object[] { "1980-03-01","1980-07-31","1979-09-19" });
	    rs.addColumn("location", new Object[] { "HSWW","HSWW","DALLEY" });
	    rs.addColumn("created_by", new Object[] { "admin","admin","admin" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    rs.addColumn("voided", new Object[] { false, false, false });

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateParticipantsCSV();
		String[] lines = readLines(filePath);
		assertEquals(3 + 1, lines.length);  // +1 for header/column-name
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateLocationAttributesCSV()}.
     * 
     * @throws FileNotFoundException
     * @throws SQLException 
     */
    @Test
    public void testGenerateLocationAttributesCSV() throws FileNotFoundException, SQLException {
    	    	    		
	    MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("attribute_id", new Object[] { 1,2});
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4"});
	    rs.addColumn("location", new Object[] { "HSWW", "HSWW" });
	    rs.addColumn("attribute_type", new Object[] { "NO_STUDENTS", "NO_TEACHERS" });
	    rs.addColumn("attribute_value", new Object[] { "1000","14"});
	    rs.addColumn("created_by", new Object[] { "admin","admin" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01"});
	    rs.addColumn("voided", new Object[] { false, false });

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateLocationAttributesCSV();
		String[] lines = readLines(filePath);
		assertEquals(2 + 1, lines.length);  // +1 for header/column-name
    }
    

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateLocationAttributeTypesCSV()}.
     * 
     * @throws FileNotFoundException
     * @throws SQLException 
     */
    @Test
    public void testGenerateLocationAttributeTypesCSV() throws FileNotFoundException, SQLException {
    	    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("attribute_type_id", new Object[] { 1,2});
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("attribute_name", new Object[] { "Students Enrolled", "Teachers Registered" });
	    rs.addColumn("short_name", new Object[] { "NO_STUDENTS", "NO_TEACHERS" });
	    rs.addColumn("description", new Object[] { "Number of Students enrolled currently in the school", "Number of Teachers currently registered in school" });
	    rs.addColumn("datatype", new Object[] { "INTEGER", "INTEGER" });
	    rs.addColumn("validation_regex", new Object[] { "range=1-2000", "range=1-50" });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01"});
	    rs.addColumn("retired", new Object[] { false,false });
    	    	    		

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateLocationAttributeTypesCSV();
		String[] lines = readLines(filePath);
		assertEquals(2 + 1, lines.length);  // +1 for header/column-name
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#generateFormTypesCSV()}.
     * 
     * @throws FileNotFoundException
     * @throws SQLException 
     */
    @Test
    public void testGenerateFormTypesCSV() throws FileNotFoundException, SQLException {
    	    	    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("form_type_id", new Object[] { 1,2});
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("form_name", new Object[] { "Quidditch Participation", "Challenge Participation Form" });
	    rs.addColumn("short_name", new Object[] { "QP Form", "CHALLENGE" });
	    rs.addColumn("description", new Object[] { null, null });
	    rs.addColumn("form_schema", new Object[] { null, null });
	    rs.addColumn("component", new Object[] { "School", "School" });
	    rs.addColumn("version", new Object[] { "1","11"});
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01"});
	    rs.addColumn("retired", new Object[] { false,false });
    	    	    		

	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	
		String filePath = reportService.generateFormTypesCSV();
		String[] lines = readLines(filePath);
		assertEquals(2 + 1, lines.length);  // +1 for header/column-name
    }


    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#getResultSet(java.lang.String)}.
     * 
     * @throws SQLException
     */
    @Test
    public void testGetResultSet() throws SQLException {
    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("user_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("username", new Object[] { "albus.dumbledore", "severus.snape", "nymphadora.tonks" });
	    rs.addColumn("full_name", new Object[] { "Albus Dumbledore", "Severus Snape", "Nymphadora Tonks" });
	    rs.addColumn("voided", new Object[] { false,false, false });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);

	    ResultSet resultSet = reportService.getResultSet("select * from privileges", conn);
	    assertNotNull(resultSet);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#getTableData(java.lang.String, java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws SQLException
     */
    @Test
    public void testGetTableData() throws SQLException {
    	
    	MockResultSet rs = new MockResultSet("rs");
	    rs.addColumn("user_id", new Object[] { 1,2,3 });
	    rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	    rs.addColumn("username", new Object[] { "albus.dumbledore", "severus.snape", "nymphadora.tonks" });
	    rs.addColumn("full_name", new Object[] { "Albus Dumbledore", "Severus Snape", "Nymphadora Tonks" });
	    rs.addColumn("voided", new Object[] { false,false, false });
	    rs.addColumn("date_created", new Object[] { "2019-10-01","2019-10-01","2019-10-01" });
	    	
	    when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	   
	    List<String[]> data = reportService.getTableData("select * from privileges", null, null);
	    assertFalse(data.isEmpty());
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.ReportServiceImpl#getTableDataAsJson()}.
     * 
     * @throws SQLException
     */
    @Test
    public void testGetTableDataAsJson() throws SQLException, JSONException {

	MockResultSet rs = new MockResultSet("rs");
	rs.addColumn("user_id", new Object[] { 1, 2, 3 });
	rs.addColumn("uuid", new Object[] { "fed99db4-e41e-11e9-81b4-2a2ae2dbcce4",
		"fed9a002-e41e-11e9-81b4-2a2ae2dbcce4", "fed9a228-e41e-11e9-81b4-2a2ae2dbcce4" });
	rs.addColumn("username", new Object[] { "albus.dumbledore", "severus.snape", "nymphadora.tonks" });
	rs.addColumn("full_name", new Object[] { "Albus Dumbledore", "Severus Snape", "Nymphadora Tonks" });
	rs.addColumn("voided", new Object[] { false, false, false });
	rs.addColumn("date_created", new Object[] { "2019-10-01", "2019-10-01", "2019-10-01" });
	when(statement.executeQuery(Mockito.anyString())).thenReturn(rs);
	JSONArray data = reportService.getTableDataAsJson("select * from privileges");
	assertFalse(data.length() == 0);
	for (int i = 0; i < data.length(); i++) {
	    JSONObject obj = data.getJSONObject(i);
	    assertNotNull(obj.getInt("user_id"));
	    assertNotNull(obj.getString("uuid"));
	    assertNotNull(obj.getBoolean("voided"));
	}
    }

}
