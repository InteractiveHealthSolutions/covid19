/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
@NoArgsConstructor
public class LocationAttributePackageDto implements Serializable {

    private static final long serialVersionUID = 8806252899739647327L;

    private List<LocationAttributeDto> attributes;

    public LocationAttributePackageDto(List<LocationAttributeDto> attributes) {
	this.attributes = attributes;
    }

    public LocationAttributePackageDto(JSONObject json) throws JSONException {
	attributes = new ArrayList<>();
	Integer locationId = json.getInt("locationId");
	JSONArray attributesJson = json.getJSONArray("attributes");
	for (int i = 0; i < attributesJson.length(); i++) {
	    JSONObject attributeJson = new JSONObject(attributesJson.get(i).toString());
	    Integer typeId = attributeJson.getJSONObject("attributeType").getInt("attributeTypeId");
	    String value = attributeJson.get("attributeValue").toString();
	    LocationAttributeDto attribute = new LocationAttributeDto();
	    attribute.setLocationId(locationId);
	    attribute.setAttributeTypeId(typeId);
	    attribute.setAttributeValue(value);
	    this.attributes.add(attribute);
	}
    }
}
