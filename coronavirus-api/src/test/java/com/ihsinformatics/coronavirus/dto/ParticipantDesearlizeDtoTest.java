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
import com.ihsinformatics.coronavirus.dto.ParticipantDesearlizeDto;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class ParticipantDesearlizeDtoTest extends BaseServiceTest {

    private ParticipantDesearlizeDto participentDesearlizeDto;

    @Before
    public void reset() {
	super.reset();
	 participentDesearlizeDto = new ParticipantDesearlizeDto(seeker.getParticipantId(), seeker.getUuid(), seeker.getLocation(), seeker.getIdentifier(),
			seeker.getPerson().getDob(), seeker.getPerson().getFirstName(), seeker.getPerson().getGender(), null);	
    }

    @Test
    public void shouldDecipaherIntegerLocationAttribute() throws JSONException {
    	assertNotNull(participentDesearlizeDto.getDecipherObject(ronHeight, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherStringLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Point Person Desigination").dataType(DataType.STRING)
    			.shortName("POINT_PERSON_DESIGNATION").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("Professor").build();
    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherLocationLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Point Person Location").dataType(DataType.LOCATION)
    			.shortName("POINT_PERSON_LOCATION").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("1").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherJSONLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Related Projects").dataType(DataType.JSON)
    			.shortName("RELATED_PROJECTS").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("[{\"projectId\":7},{\"projectId\":8},{\"projectId\":9}]").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherDefinitionArrayLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Project Types").dataType(DataType.JSON)
    			.shortName("PROJECT_TYPES").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("[{\"definitionId\":7},{\"definitionId\":8},{\"definitionId\":9}]").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    
    @Test
    public void shouldDecipaherDonorLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Project Donor").dataType(DataType.JSON)
    			.shortName("PROJECT_DONOR").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("[{\"donorId\":7},{\"donorId\":8},{\"donorId\":9}]").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherUserArrayLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Project Monitors").dataType(DataType.JSON)
    			.shortName("PROJECT_MONITOR").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("[{\"userId\":7},{\"userId\":8},{\"userId\":9}]").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherDefinitionLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("School Level").dataType(DataType.DEFINITION)
    			.shortName("SCHOOL_LEVEL").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("2").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void shouldDecipaherUserLocationAttribute() throws JSONException {
    	
    	PersonAttributeType pointPerson = PersonAttributeType.builder().attributeName("Project Supervisor").dataType(DataType.USER)
    			.shortName("PROJECT_SUPERVISOR").build();
    	PersonAttribute pointPersonAttribute = PersonAttribute.builder().person(seeker.getPerson()).attributeType(pointPerson)
    			.attributeValue("1").build();
    	    	
    	assertNotNull(participentDesearlizeDto.getDecipherObject(pointPersonAttribute, locationService, metadataService, userService, donorService));
    }
    
    @Test
    public void testParticipentDesearlizeDto() throws JSONException {
    	
    	assertNotNull(new ParticipantDesearlizeDto (seeker,  locationService, metadataService, userService, donorService));
    }
    
}
