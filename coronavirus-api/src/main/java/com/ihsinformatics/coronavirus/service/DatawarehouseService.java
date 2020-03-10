/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.datawarehouse.DatawarehouseTask;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.repository.FormTypeRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Service
@Transactional 
public class DatawarehouseService  {

    public enum RunMode {
	BERRY, // Berry Alan. Consume all system resources to execute a task
	FORREST, // Forrest Gump. Run on single thread fully occupied
	RABBIT // Consume all resources immediately available
    }

    private RunMode RUN_MODE = RunMode.FORREST;

    @Autowired
    private ValidationServiceImpl validationService;

    @Autowired
    private FormServiceImpl formService;
    
    @Autowired
    private BaseService baseService;
    
    @Autowired
    private FormTypeRepository formTypeRepository;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private List<Queue<String>> queryTasks;

    @MeasureProcessingTime
    public void executeTasks() {
    	
    	queryTasks  = new ArrayList<>();
    	List<FormType> formTypes = formTypeRepository.findAll();
	    for (FormType formType : formTypes) {
		queryTasks.add(genereateTaskQueue(formType));
	    }
	    for (Queue<String> queue : queryTasks) {
		    LOG.info("Executing task [{}] in main thread", Arrays.toString(queue.toArray()));
		    DatawarehouseTask dwTask = new DatawarehouseTask(null, baseService.getEntityManager());
		    try {
			for (String sqlQuery : queue) {
			    List<Object> result = dwTask.executeSQL(sqlQuery, false);
			    LOG.info(result.get(0) + " rows affected.");
			}
		    } catch (Exception e) {
			LOG.error(
				String.format("Exception occurred while executing query: Message: {}", e.getMessage()));
		    }
		}
    	
    }

    /**
     * Generate CREATE TABLE tableName query for given {@link FormType} object
     * 
     * @param formType
     * @param tableName
     * @return
     * @throws JSONException 
     */
    public String generateCreateTableQuery(FormType formType, String tableName) throws JSONException {
	JSONObject json = new JSONObject(formType.getFormSchema());
	JSONArray fields = new JSONArray();
	Object obj = json.get("fields");
	fields = new JSONArray(obj.toString());
	StringBuilder createQuery = new StringBuilder();
	createQuery.append("create table if not exists ");
	createQuery.append(tableName);
	createQuery.append(" ( ");
	createQuery.append("`form_id` int(11), ");
	createQuery.append("`uuid` varchar(38), ");
	createQuery.append("`date_processed` datetime, ");
	createQuery.append("`reference_id` varchar(50), ");
	createQuery.append("`location_id` int(11), ");
	createQuery.append("`form_type_id` int(11), ");
	createQuery.append("`form_date` datetime, ");
	createQuery.append("`created_by` int(11), ");
	createQuery.append("`date_created` datetime, ");
	// Make user of Sorted Map
	SortedMap<Integer, Element> elements = new TreeMap<>();
	for (int i = 0; i < fields.length(); i++) {
	    JSONObject field = new JSONObject(fields.get(i).toString());
	    int order = field.getInt("order");
	    String elementId = field.getString("element");
	    Element element = validationService.findElementByIdentifier(elementId);
	    if (element == null) {
		LOG.error(String.format("Element against ID/Name " + elementId + " does not exist."));
	    } else {
		elements.put(order, element);
	    }
	}
	for (Entry<Integer, Element> entry : elements.entrySet()) {
	    createQuery.append("`");
	    createQuery.append(entry.getValue().getShortName().toLowerCase().replace(" ", "_"));
	    createQuery.append("` ");
	    createQuery.append(getSqlDataType(entry.getValue()));
	    createQuery.append(", ");
	}
	createQuery.append("`blank` text, ");
	createQuery.append("PRIMARY KEY (`form_id`), ");
	createQuery.append("UNIQUE KEY `idx_uuid` (`uuid`), ");
	createQuery.append("KEY `idx_reference_id` (`reference_id`), ");
	createQuery.append("KEY `idx_location_id` (`location_id`), ");
	createQuery.append("KEY `idx_form_type_id` (`form_type_id`), ");
	createQuery.append("KEY `idx_user_id` (`created_by`), ");
	createQuery.append("KEY `idx_form_date` (`form_date`) ");
	createQuery.append(") ENGINE=MyISAM;");
	return createQuery.toString();
    }

    /**
     * Generate INSERT TABLE tableName query for given {@link FormType} object
     * 
     * @param formType
     * @param tableName
     * @return
     */
    public String generateUpdateTableQuery(FormType formType, String tableName) {
	Set<String> fields;
	try {
	    String keysQuery = "select distinct json_keys(json_unquote(data)) from form_data where form_type_id="
		    + formType.getFormTypeId();
	    Query query = baseService.getEntityManager().createNativeQuery(keysQuery);
	    List<?> keySetList = query.getResultList();
	    fields = filterKeySetList(keySetList);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return null;
	}
	StringBuilder postPart = new StringBuilder(
		"select `form_id`, `uuid`, current_timestamp() as `date_processed`, `reference_id`, `location_id`, `form_type_id`, `form_date`, `created_by`, `date_created`, \r\n");
	List<String> qualifiedFields = new ArrayList<>();
	// Make user of Sorted Map
	for (Iterator<String> iter = fields.iterator(); iter.hasNext();) {
	    String elementId = iter.next();
	    Element element = validationService.findElementByIdentifier(elementId);
	    if (element == null) {
		LOG.error(String.format("Element against ID/Name " + elementId + " does not exist."));
	    } else {
		qualifiedFields.add(element.getShortName());
		postPart.append("json_unquote(");
		postPart.append("json_extract(json_unquote(data), \"");
		postPart.append("$.");
		postPart.append(element.getShortName());
		postPart.append("\")");
		postPart.append(") as `");
		postPart.append(element.getShortName());
		postPart.append("`, \r\n");
	    }
	}
	postPart.append("'' as blank ");
	postPart.append("from form_data ");
	postPart.append("where 1=1 and voided=0 ");
	postPart.append("and form_type_id=");
	postPart.append(formType.getFormTypeId());
	StringBuilder prePart = new StringBuilder("insert into ");
	prePart.append(tableName);
	prePart.append("(");
	prePart.append(
		"`form_id`, `uuid`, `date_processed`, `reference_id`, `location_id`, `form_type_id`, `form_date`, `created_by`, `date_created`, ");
	for (String field : qualifiedFields) {
	    prePart.append("`");
	    prePart.append(field);
	    prePart.append("`, ");
	}
	prePart.append("`blank`) \r\n");
	String insertSelectQuery = prePart.append(postPart).toString();
	return insertSelectQuery;
    }

    /**
     * Unpacks {@link JSONObject} objects in passed as parameter and returns a set
     * of unique strings of keys
     * 
     * @param keySetList
     * @return
     * @throws JSONException 
     */
    public Set<String> filterKeySetList(List<?> keySetList) throws JSONException {
	Set<String> keySet = new HashSet<>();
	if (keySetList != null) {
	    for (Object jsonObj : keySetList) {
		JSONArray json = new JSONArray(jsonObj.toString());
		for (int i = 0; i < json.length(); i++) {
		    keySet.add(json.getString(i));
		}
	    }
	}
	return keySet;
    }

    /**
     * Returns the right SQL data type which maps with the data type property of
     * {@link Element} object
     * 
     * @param element
     * @return
     */
    public String getSqlDataType(Element element) {
	switch (element.getDataType()) {
	case BOOLEAN:
	    return "bit(1)";
	case CHARACTER:
	    return "char(1)";
	case DATE:
	case TIME:
	case DATETIME:
	    return "datetime";
	case DEFINITION:
	case LOCATION:
	case STRING:
	case USER:
	    return "varchar(255)";
	case FLOAT:
	    return "decimal";
	case INTEGER:
	    return "int(11)";
	case JSON:
	    return "text";
	case UNKNOWN:
	    return "varchar(255)";
	}
	return "varchar(255)";
    }

    /**
     * Returns a Queue of inter-dependent queries from given {@link FormType} object
     * schema
     * 
     * @param formType
     */
    public Queue<String> genereateTaskQueue(FormType formType) {
	Queue<String> queue = new LinkedList<>();
	// Prepare a table from schema
	try {
	    String tableName = "_" + formType.getShortName().toLowerCase().replace(" ", "_");
	    //queue.add("drop table if exists " + tableName);
	    queue.add(generateCreateTableQuery(formType, tableName));
	    queue.add("truncate table " + tableName);
	    queue.add(generateUpdateTableQuery(formType, tableName));
	} catch (Exception e) {
	    LOG.error("Unable to proecss FormType {}. Stack trace: {}", formType.toString(), e.getMessage());
	}
	return queue;
    }

}
