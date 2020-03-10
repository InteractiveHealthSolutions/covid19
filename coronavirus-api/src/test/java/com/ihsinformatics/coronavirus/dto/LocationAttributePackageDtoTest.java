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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.dto.LocationAttributeDto;
import com.ihsinformatics.coronavirus.dto.LocationAttributePackageDto;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class LocationAttributePackageDtoTest extends BaseServiceTest {

    @Test
    public void shouldConvertToLocationAttributePackage() throws JSONException {
	super.reset();
	hogwartz.setLocationId(100);
	noOfTeachers.setAttributeTypeId(101);
	noOfStudents.setAttributeTypeId(102);
	initLocationAttributes();
	List<LocationAttributeDto> attributes = new ArrayList<>();
	attributes.add(new LocationAttributeDto(noOfHogwartzStudents));
	attributes.add(new LocationAttributeDto(noOfHogwartzTeachers));
	JSONObject json = new JSONObject();
	JSONArray attributesArray = new JSONArray();
	for (LocationAttributeDto attribute : attributes) {
	    JSONObject attributeObj = new JSONObject();
	    attributeObj.put("attributeValue", attribute.getAttributeValue());
	    JSONObject typeObj = new JSONObject();
	    typeObj.put("attributeTypeId", attribute.getAttributeTypeId());
	    attributeObj.put("attributeType", typeObj);
	    attributesArray.put(attributeObj);
	}
	json.put("locationId", hogwartz.getLocationId());
	json.put("attributes", attributesArray);
	LocationAttributePackageDto attributePackageDto = new LocationAttributePackageDto(json);
	assertNotNull(attributePackageDto);
    }
}
