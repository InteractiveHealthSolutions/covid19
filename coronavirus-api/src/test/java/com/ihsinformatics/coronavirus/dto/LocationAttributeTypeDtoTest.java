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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.dto.LocationAttributeDto;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class LocationAttributeTypeDtoTest extends BaseServiceTest {

    private LocationAttributeDto locationAttributeDto;

    @Before
    public void reset() {
	super.reset();
	hogwartz.setLocationId(100);
	noOfTeachers.setAttributeTypeId(101);
	initLocationAttributes();
	locationAttributeDto = new LocationAttributeDto(noOfHogwartzTeachers);
    }

    @Test
    public void shouldConvertToLocationAttribute() {
	Optional<Location> hogwartzObj = Optional.of(hogwartz);
	when(locationRepository.findById(any(Integer.class))).thenReturn(hogwartzObj);
	Optional<LocationAttributeType> noOfTeachersObj = Optional.of(noOfTeachers);
	when(locationAttributeTypeRepository.findById(any(Integer.class))).thenReturn(noOfTeachersObj);
	assertNotNull(locationAttributeDto.toLocationAttribute(locationService));
    }
}
