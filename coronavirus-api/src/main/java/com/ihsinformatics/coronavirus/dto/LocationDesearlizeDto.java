/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihsinformatics.coronavirus.Context;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.Project;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.MetadataService;
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
public class LocationDesearlizeDto {


	private Integer locationId;

    private String locationName;

    private String shortName;

    private Definition category;

    private String description;

    private String address1;

    private String address2;

    private String address3;

    private Integer postalCode;

    private String landmark1;

    private String landmark2;

    private String cityVillage;

    private String stateProvince;

    private String country;

    private Double latitude;

    private Double longitude;

    private String primaryContact;

    private String primaryContactPerson;

    private String secondaryContact;

    private String secondaryContactPerson;

    private String tertiaryContact;

    private String tertiaryContactPerson;
    
    private String extension;

    private String email;

    private List<LocationMapObject> attributes = new ArrayList<>();

    private Location parentLocation;
    
    
    public LocationDesearlizeDto(Integer locationId, String locationName, String shortName, Definition category,
			String description, String address1, String address2, String address3, Integer postalCode, String landmark1,
			String landmark2, String cityVillage, String stateProvince, String country, Double latitude,
			Double longitude, String primaryContact, String primaryContactPerson, String secondaryContact,
			String secondaryContactPerson, String tertiaryContact, String tertiaryContactPerson, String extension,
			String email, List<LocationMapObject> attributes, Location parentLocation) {
		super();
		this.locationId = locationId;
		this.locationName = locationName;
		this.shortName = shortName;
		this.category = category;
		this.description = description;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.postalCode = postalCode;
		this.landmark1 = landmark1;
		this.landmark2 = landmark2;
		this.cityVillage = cityVillage;
		this.stateProvince = stateProvince;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
		this.primaryContact = primaryContact;
		this.primaryContactPerson = primaryContactPerson;
		this.secondaryContact = secondaryContact;
		this.secondaryContactPerson = secondaryContactPerson;
		this.tertiaryContact = tertiaryContact;
		this.tertiaryContactPerson = tertiaryContactPerson;
		this.extension = extension;
		this.email = email;
		this.attributes = attributes;
		this.parentLocation = parentLocation;
	}
    
  public LocationDesearlizeDto(Location location, LocationService locationService, MetadataService metadataService, UserService userService, DonorService donorService) {
		
		this.locationId = location.getLocationId();
		this.locationName = location.getLocationName();
		this.shortName = location.getShortName();
		this.category = location.getCategory();
		this.description = location.getDescription();
		this.address1 = location.getAddress1();
		this.address2 = location.getAddress2();
		this.address3 = location.getAddress3();
		this.postalCode = location.getPostalCode();
		this.landmark1 = location.getLandmark1();
		this.landmark2 = location.getLandmark2();
		this.cityVillage = location.getCityVillage();
		this.stateProvince = location.getStateProvince();
		this.country = location.getCountry();
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		this.primaryContact = location.getPrimaryContact();
		this.primaryContactPerson = location.getPrimaryContactPerson();
		this.secondaryContact = location.getSecondaryContact();
		this.secondaryContactPerson = location.getSecondaryContactPerson();
		this.tertiaryContact = location.getTertiaryContact();
		this.tertiaryContactPerson = location.getTertiaryContactPerson();
		this.extension = location.getExtension();
		this.email = location.getEmail();
		this.parentLocation = location.getParentLocation();
		
		List<LocationAttribute> attributesList = location.getAttributes();
			
		for (int i = 0; i < attributesList.size(); i++) {
			LocationMapObject locMapObject = new LocationMapObject();
		 
			LocationAttribute attribute = attributesList.get(i);
			try {
				locMapObject = getDecipherObject(attribute, metadataService, locationService, userService, donorService);
			} catch (JSONException e) {
				continue;
			}
		
			this.attributes.add(locMapObject);  
		  
		}
			
		
	}

	
	
	 public LocationMapObject getDecipherObject(LocationAttribute attribute, MetadataService metadataService, LocationService locationService, UserService userService, DonorService donorService) throws JSONException {
		 	
		 	LocationMapObject locMapObject = new LocationMapObject();
		 	locMapObject.setAttributeId(attribute.getAttributeId()); 
			locMapObject.setAttributeType(attribute.getAttributeType());
			String value = attribute.getAttributeValue();
			DataType dataType = attribute.getAttributeType().getDataType();
		 	locMapObject.setDataType(dataType.toString());

	    	Object returnValue = null;

	    	if(dataType.equals(DataType.BOOLEAN))
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
	    	    } 
	    	}
	    	else if(dataType.equals(DataType.JSON)){

    			JSONArray jsonArray = new JSONArray(value);
    			JSONArray returnJsonArray = new JSONArray();
    			for (int i = 0; i < jsonArray.length(); i++) {
    				JSONObject jObj = jsonArray.getJSONObject(i);
    				if(jObj.has("definitionId")){
				    	 Definition d =  metadataService.getDefinitionById(jObj.getInt("definitionId"));
				    	 returnJsonArray.put(d); 
				    	 locMapObject.setDataType("definition_array");
  		 			} else if(jObj.has("projectId")){
				    	 Project p =  donorService.getProjectById(jObj.getInt("projectId"));
				    	 returnJsonArray.put(p); 
				    	 locMapObject.setDataType("project_array");
   		 			} else if(jObj.has("userId")){
				    	 User u =  userService.getUserById(jObj.getInt("userId"));
				    	 returnJsonArray.put(u); 
				    	 locMapObject.setDataType("user_array");
      		 		} else  if(jObj.has("donorId")){
				    	 Donor d =  donorService.getDonorById(jObj.getInt("donorId"));
				    	 returnJsonArray.put(d); 
				    	 locMapObject.setDataType("donor_array");
     		 		} 
    				
          		 returnValue =  returnJsonArray;
    			}
	    	}
	    	else if(dataType.equals(DataType.STRING) || dataType.equals(DataType.UNKNOWN)){
	    		returnValue = value;
	    	}
	    	
	    	locMapObject.setValue(returnValue);
	    	return locMapObject;
	    } 
	

	
}
		
		
		
		
    
    
    
    
    
    
    

    

