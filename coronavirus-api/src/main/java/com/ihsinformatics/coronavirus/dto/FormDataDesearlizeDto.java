/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.TypeMismatchException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.ihsinformatics.coronavirus.Context;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.Project;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.LocationServiceImpl;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.service.ParticipantService;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.util.DataType;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@Setter
@Getter
@JsonAutoDetect(fieldVisibility =  JsonAutoDetect.Visibility.ANY)
public class FormDataDesearlizeDto {

    private Integer formId;

    private String uuid;

    private FormType formType;

    private Location location;

    private Date formDate;

    private String referenceId;

    private Set<FormDataMapObject> data = new HashSet<>();

    private Set<String> formParticipantUuids = new HashSet<>();

    public FormDataDesearlizeDto(Integer formId, String uuid, FormType formType, Location location, Date formDate,
	    String referenceId, Set<FormDataMapObject> data, Set<String> formParticipantUuids) {
	this.formId = formId;
	this.uuid = uuid;
	this.formType = formType;
	this.location = location;
	this.formDate = formDate;
	this.referenceId = referenceId;
	this.data = data;
	this.formParticipantUuids = formParticipantUuids;
    }
    
    public FormDataDesearlizeDto(FormData formdata, LocationService locationService,
    	    ParticipantService participantService, MetadataService metadataService, UserService userService, DonorService donorService){
    	
    	
    	this.formId = formdata.getFormId();
    	this.uuid = formdata.getUuid();
	    this.formType = formdata.getFormType();
	    this.location = formdata.getLocation();
	    this.formDate = formdata.getFormDate();
	    this.referenceId = formdata.getReferenceId();
	    
	    String unescape = formdata.getData();
	    
	    JSONObject dataObject = null;
	    try{
	    	dataObject =  new JSONObject(unescape);
	    } catch(JSONException e){
	    	unescape =  unescape(unescape);
	    	if(unescape.startsWith("\""))
		    	unescape = unescape.substring(1, unescape.length()-1);
	    	try {
				dataObject =  new JSONObject(unescape);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
	    }
	    
	    if(dataObject != null){
		    Iterator<String> keys = dataObject.keys();
	
		    try {
			    while(keys.hasNext()) {
			        String key = keys.next();
			        Element element = metadataService.getElementByShortName(key);
			        Object value = dataObject.get(key);
					
			        FormDataMapObject dmapObj = new FormDataMapObject();
			        if(element != null){
			        	
			        	dmapObj = getDecipherObject(element,value.toString(), metadataService, userService, participantService, donorService);
			  
			        }	
			        else{
			        	dmapObj.setKey(key);
			        	dmapObj.setDataType(DataType.STRING.toString());
			        	dmapObj.setValue(value);
			        }
			        data.add(dmapObj);
			    }
		    
		    } catch (JSONException e) {
				e.printStackTrace();
			}
	    }
    	
   }
    
    
    public FormDataMapObject getDecipherObject(Element element, String value, MetadataService metadataService, UserService userService, 
    		ParticipantService participantService, DonorService donorService) throws TypeMismatchException {
    	DataType dataType = element.getDataType();
    	FormDataMapObject dmapObj = new FormDataMapObject();
    	dmapObj.setKey(element);
    	dmapObj.setDataType(element.getDataType().toString());

    	Object returnValue = null;

    	if (dataType == null)
    		returnValue = value; 
    	else if(dataType.equals(DataType.BOOLEAN))
    		returnValue = Boolean.parseBoolean(value);
    	else if(dataType.equals(DataType.CHARACTER))
    		returnValue = (value.charAt(0));
    	else if(dataType.equals(DataType.DATE))
    		returnValue = DateTimeUtil.fromString(value, Context.DEFAULT_DATE_FORMAT);
    	else if(dataType.equals(DataType.DATETIME) || dataType.equals(DataType.TIME))
    		returnValue = DateTimeUtil.fromString(value, Context.DEFAULT_DATETIME_FORMAT);
    	else if(dataType.equals(DataType.FLOAT))
    		returnValue = Double.parseDouble(value);
    	else if(dataType.equals(DataType.INTEGER))
    		returnValue = Integer.parseInt(value);
    	else if(dataType.equals(DataType.LOCATION)){
    	    LocationService locationService = new LocationServiceImpl();
    	    if (value.matches(RegexUtil.UUID)) {
    	    	returnValue =  locationService.getLocationByUuid(value);
    	    } else {
    	    	returnValue = locationService.getLocationById(Integer.parseInt(value));
    	    }
    	}
    	else if(dataType.equals(DataType.USER)){
    	    if (value.matches(RegexUtil.UUID)) {
    	    	returnValue =  userService.getUserByUuid(value);
    	    } else {
    	    	returnValue = userService.getUserById(Integer.parseInt(value));
    	    }
    	}
    	else if(dataType.equals(DataType.DEFINITION)){
    	   if (value.matches(RegexUtil.UUID)) {
    		   returnValue = metadataService.getDefinitionByUuid(value);
    	    } else if (value.matches(RegexUtil.INTEGER)) {
    	    	returnValue = metadataService.getDefinitionById(Integer.parseInt(value));
    	    } else {
    	    List<Definition> definitions =  metadataService.getDefinitionByShortName(value);
    	    if(definitions.size() == 1)
    	    	returnValue = definitions.get(0);
		      else{
		    	 DefinitionType definitionType = metadataService.getDefinitionTypeByShortName(element.getShortName());
		    	 List<Definition> dList = metadataService.getDefinitionsByDefinitionType(definitionType);
		    	 for(Definition df : dList){
		    		 if(df.getShortName().equals(value))
		    			 returnValue = df;
		    	 } 
		    	  
		      }
    	    
    	    }
    	}  
    	else if(dataType.equals(DataType.JSON)){
    	   try {
    	    	    	
    		 JSONObject jsonObject =  new JSONObject(value);
    		 JSONArray jsonAAray = jsonObject.getJSONArray("values");
    		 
    		 JSONArray returnJsonArray = new JSONArray();
    		 for (int i = 0; i < jsonAAray.length(); i++) {
    			 
    			  Object obj = jsonAAray.get(i);
    			  JSONObject jObj = null;
    			
    			  HashMap hashMap = new HashMap();
    			  try{
    				
    				  if(obj instanceof JSONObject)
    					  jObj = jsonAAray.getJSONObject(i);
    				  else{    				  
    					  String str = jsonAAray.getString(i);
    					  jObj =  new JSONObject(str);
    				  }
    				  Iterator<String> keys = jObj.keys();
    				  dmapObj.setDataType(element.getShortName());
    				  
    				  while(keys.hasNext()) {
    				      String key = keys.next();
    				      hashMap.put(key, jObj.get(key));
    				  }
    				  
    				returnJsonArray.put(hashMap.toString());
    				  
    			  } catch (JSONException e1) {
    	    			
    				  String str = jsonAAray.getString(i);
    				  dmapObj.setDataType("definition_array");
    				  List<Definition> definitions = metadataService.getDefinitionByShortName(str);
        		      if(definitions.size() == 1)
        		    	  returnJsonArray.put(definitions.get(0));
        		      else{
        		    	 DefinitionType definitionType = metadataService.getDefinitionTypeByShortName(element.getShortName());
        		    	 List<Definition> dList = metadataService.getDefinitionsByDefinitionType(definitionType);
        		    	 for(Definition df : dList){
        		    		 if(df.getShortName().equals(str))
        		    			 returnJsonArray.put(df); 
        		    	 } 
        		    	  
        		      }
    				  
    	    	  }
    			  	      
    		 }
    		 returnValue = returnJsonArray;
    	    } catch (JSONException e) {
    		try {
    			JSONArray jsonArray = new JSONArray(value);
    			JSONArray returnJsonArray = new JSONArray();
       		 	for (int i = 0; i < jsonArray.length(); i++) {
       		 		
       		 		JSONObject jObj = jsonArray.getJSONObject(i);
       		 		if(jObj.has("userId")){
				    	 User u =  userService.getUserById(jObj.getInt("userId"));
				    	 returnJsonArray.put(u); 
				    	 dmapObj.setDataType("user_array");
       		 		} else  if(jObj.has("donorId")){
				    	 Donor d =  donorService.getDonorById(jObj.getInt("donorId"));
				    	 returnJsonArray.put(d); 
				    	 dmapObj.setDataType("donor_array");
      		 		} else  if(jObj.has("projectId")){
				    	 Project p =  donorService.getProjectById(jObj.getInt("projectId"));
				    	 returnJsonArray.put(p); 
				    	 dmapObj.setDataType("project_array");
     		 		}
       		 		else {
       		 			
       		 		dmapObj.setDataType(element.getShortName());	
     
	  				returnJsonArray.put(jObj.toString());
       		 	   }
       		 		
       		 		
       		 	}
       		 returnValue =  returnJsonArray;
    		} catch (JSONException e1) {
    			returnValue = null;
    		}
    	    }
    		
    	}
    	else if(dataType.equals(DataType.STRING) || dataType.equals(DataType.UNKNOWN)){
    		if(element.getShortName().equals("participant_id")){
    			if (value.matches(RegexUtil.INTEGER)) {
    				returnValue = participantService.getParticipantById(Integer.valueOf(value));
        	    }
    			else {
    				returnValue = participantService.getParticipantByIdentifier(value);
    			}
    			 dmapObj.setDataType("participant");
    		}
    		else if(element.getShortName().equals("province") || element.getShortName().equals("district")){ 
    			returnValue = value;
    			dmapObj.setDataType(element.getShortName());
    		}
    		else returnValue = value;
    	}
    	
    	dmapObj.setValue(returnValue);
    	return dmapObj;
    } 

    
    public static String unescape(String input) {
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
          char delimiter = input.charAt(i); i++; // consume letter or backslash

          if(delimiter == '\\' && i < input.length()) {

            // consume first after backslash
            char ch = input.charAt(i); i++;

            if(ch == '\\' || ch == '/' || ch == '"' || ch == '\'') {
              builder.append(ch);
            }
            else if(ch == 'n') builder.append('\n');
            else if(ch == 'r') builder.append('\r');
            else if(ch == 't') builder.append('\t');
            else if(ch == 'b') builder.append('\b');
            else if(ch == 'f') builder.append('\f');
            else if(ch == 'u') {

              StringBuilder hex = new StringBuilder();

              // expect 4 digits
              if (i+4 > input.length()) {
                throw new RuntimeException("Not enough unicode digits! ");
              }
              for (char x : input.substring(i, i + 4).toCharArray()) {
                if(!Character.isLetterOrDigit(x)) {
                  throw new RuntimeException("Bad character in unicode escape.");
                }
                hex.append(Character.toLowerCase(x));
              }
              i+=4; // consume those four digits.

              int code = Integer.parseInt(hex.toString(), 16);
              builder.append((char) code);
            } else {
              throw new RuntimeException("Illegal escape sequence: \\"+ch);
            }
          } else { // it's not a backslash, or it's the last character.
            builder.append(delimiter);
          }
        }

        return builder.toString();
      }

}
