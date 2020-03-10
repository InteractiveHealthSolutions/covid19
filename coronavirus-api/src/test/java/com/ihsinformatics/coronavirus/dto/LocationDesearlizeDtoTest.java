/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.dto.LocationDesearlizeDto;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class LocationDesearlizeDtoTest extends BaseServiceTest {

    private LocationDesearlizeDto locationDesearlizeDto;

    @Before
    public void reset() {
	super.reset();
	hogwartz.setLocationId(100);
	noOfTeachers.setAttributeTypeId(101);
	locationDesearlizeDto = new LocationDesearlizeDto(hogwartz.getLocationId(), hogwartz.getLocationName(), hogwartz.getShortName(), hogwartz.getCategory(),
    		hogwartz.getDescription(), hogwartz.getAddress1(), hogwartz.getAddress2(), hogwartz.getAddress3(), hogwartz.getPostalCode(), hogwartz.getLandmark1(),
    		hogwartz.getLandmark2(), hogwartz.getCityVillage(), hogwartz.getStateProvince(), hogwartz.getCountry(), hogwartz.getLatitude(),
    		hogwartz.getLongitude(), hogwartz.getPrimaryContact(), hogwartz.getPrimaryContactPerson(), hogwartz.getSecondaryContact(),
    		hogwartz.getSecondaryContactPerson(), hogwartz.getTertiaryContact(), hogwartz.getTertiaryContactPerson(), hogwartz.getExtension(),
    		hogwartz.getEmail(), null, hogwartz.getParentLocation());	
	initLocationAttributes();
    }

    @Test
    public void shouldDecipaherIntegerLocationAttribute() throws JSONException {
    	assertNotNull(locationDesearlizeDto.getDecipherObject(noOfHogwartzStudents, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherStringLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Point Person Desigination").dataType(DataType.STRING)
    			.shortName("POINT_PERSON_DESIGNATION").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("Professor").build();
    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherLocationLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Point Person Location").dataType(DataType.LOCATION)
    			.shortName("POINT_PERSON_LOCATION").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("1").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherJSONLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Related Projects").dataType(DataType.JSON)
    			.shortName("RELATED_PROJECTS").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("[{\"projectId\":7},{\"projectId\":8},{\"projectId\":9}]").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherDefinitionArrayLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Project Types").dataType(DataType.JSON)
    			.shortName("PROJECT_TYPES").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("[{\"definitionId\":7},{\"definitionId\":8},{\"definitionId\":9}]").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    
    @Test
    public void shouldDecipaherDonorLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Project Donor").dataType(DataType.JSON)
    			.shortName("PROJECT_DONOR").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("[{\"donorId\":7},{\"donorId\":8},{\"donorId\":9}]").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherUserArrayLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Project Monitors").dataType(DataType.JSON)
    			.shortName("PROJECT_MONITOR").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("[{\"userId\":7},{\"userId\":8},{\"userId\":9}]").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherDefinitionLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("School Level").dataType(DataType.DEFINITION)
    			.shortName("SCHOOL_LEVEL").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("2").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherUserLocationAttribute() throws JSONException {
    	
    	LocationAttributeType pointPerson = LocationAttributeType.builder().attributeName("Project Supervisor").dataType(DataType.USER)
    			.shortName("PROJECT_SUPERVISOR").isRequired(Boolean.FALSE).build();
    	LocationAttribute pointPersonAttribute = LocationAttribute.builder().location(hogwartz).attributeType(pointPerson)
    			.attributeValue("1").build();
    	    	
    	assertNotNull(locationDesearlizeDto.getDecipherObject(pointPersonAttribute, metadataService, locationService, userService, donorService));
    }
    
    @Test
    public void testLocationDesearlizeDto() throws JSONException {
    	
    	    	
    	assertNotNull(new LocationDesearlizeDto (hogwartz,  locationService, metadataService, userService, donorService));
    }
    
}
