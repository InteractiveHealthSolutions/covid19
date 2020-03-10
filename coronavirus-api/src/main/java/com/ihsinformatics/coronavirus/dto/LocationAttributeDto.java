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

import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.service.LocationService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
@NoArgsConstructor
public class LocationAttributeDto implements Serializable {

    private static final long serialVersionUID = -6440473759173074721L;

    private Integer attributeId;

    private Integer locationId;

    private Integer attributeTypeId;

    private String attributeValue;

    public LocationAttributeDto(LocationAttribute locationAttribute) {
	this.attributeId = locationAttribute.getAttributeId();
	this.locationId = locationAttribute.getLocation().getLocationId();
	this.attributeTypeId = locationAttribute.getAttributeType().getAttributeTypeId();
	this.attributeValue = locationAttribute.getAttributeValue();
    }

    public LocationAttribute toLocationAttribute(LocationService locationService) {
	Location location = locationService.getLocationById(locationId);
	LocationAttributeType attributeType = locationService.getLocationAttributeTypeById(attributeTypeId);
	LocationAttribute locationAttribute = LocationAttribute.builder().attributeId(attributeId)
		.attributeType(attributeType).location(location).attributeValue(attributeValue).build();
	return locationAttribute;
    }
}
