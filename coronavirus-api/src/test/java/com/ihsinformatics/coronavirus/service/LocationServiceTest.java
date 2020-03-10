/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class LocationServiceTest extends BaseServiceTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#deleteLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldDeleteLocation() {
	doNothing().when(locationRepository).delete(any(Location.class));
	locationService.deleteLocation(hogwartz, true);
	verify(locationRepository, times(1)).delete(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#deleteLocationAttribute(com.ihsinformatics.coronavirus.model.LocationAttribute)}.
     */
    @Test
    public void shouldDeleteLocationAttribute() {
	doNothing().when(locationAttributeRepository).delete(any(LocationAttribute.class));
	locationService.deleteLocationAttribute(noOfHogwartzStudents);
	verify(locationAttributeRepository, times(1)).delete(any(LocationAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#deleteLocationAttributeType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test
    public void shouldDeleteLocationAttributeType() {
	when(locationAttributeRepository.findByAttributeType(any(LocationAttributeType.class)))
		.thenReturn(Collections.emptyList());
	doNothing().when(locationAttributeTypeRepository).delete(any(LocationAttributeType.class));
	locationService.deleteLocationAttributeType(noOfStudents, true);
	verify(locationAttributeRepository, times(1)).findByAttributeType(any(LocationAttributeType.class));
	verify(locationAttributeTypeRepository, times(1)).delete(any(LocationAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getAllLocationAttributeTypes()}.
     */
    @Test
    public void shouldGetAllLocationAttributeTypes() {
	when(locationAttributeTypeRepository.findAll()).thenReturn(Arrays.asList(noOfStudents, noOfTeachers));
	assertThat(locationService.getAllLocationAttributeTypes(), containsInAnyOrder(noOfStudents, noOfTeachers));
	verify(locationAttributeTypeRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getAllLocations()}.
     */
    @Test
    public void shouldGetAllLocations() {
	when(locationRepository.findAll()).thenReturn(Arrays.asList(hogwartz, diagonalley));
	assertThat(locationService.getAllLocations(), containsInAnyOrder(diagonalley, hogwartz));
	verify(locationRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetLocationAttributeById() {
	Optional<LocationAttribute> attributeObj = Optional.of(noOfHogwartzStudents);
	when(locationAttributeRepository.findById(any(Integer.class))).thenReturn(attributeObj);
	assertThat(locationService.getLocationAttributeById(1), is(noOfHogwartzStudents));
	verify(locationAttributeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributesByType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test
    public void shouldGetLocationAttributesByType() {
	when(locationAttributeRepository.findByAttributeType(any(LocationAttributeType.class)))
		.thenReturn(Arrays.asList(noOfHogwartzStudents));
	assertThat(locationService.getLocationAttributesByType(noOfStudents), contains(noOfHogwartzStudents));
	verify(locationAttributeRepository, times(1)).findByAttributeType(any(LocationAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributesByTypeAndValue(com.ihsinformatics.coronavirus.model.LocationAttributeType, java.lang.String)}.
     */
    @Test
    public void shouldGetLocationAttributesByValue() {
	when(locationAttributeRepository.findByValue(any(String.class)))
		.thenReturn(Arrays.asList(noOfHogwartzStudents));
	assertThat(locationService.getLocationAttributesByValue("1000"), contains(noOfHogwartzStudents));
	verify(locationAttributeRepository, times(1)).findByValue(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeTypeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetLocationAttributeTypeById() {
	Optional<LocationAttributeType> attributeObj = Optional.of(noOfStudents);
	when(locationAttributeTypeRepository.findById(any(Integer.class))).thenReturn(attributeObj);
	assertThat(locationService.getLocationAttributeTypeById(0), is(noOfStudents));
	verify(locationAttributeTypeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeTypeByName(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationAttributeTypeByName() {
	when(locationAttributeTypeRepository.findByAttributeName(any(String.class))).thenReturn(noOfStudents);
	assertThat(locationService.getLocationAttributeTypeByName(noOfStudents.getAttributeName()), is(noOfStudents));
	verify(locationAttributeTypeRepository, times(1)).findByAttributeName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeTypeByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationAttributeTypeByShortName() {
	when(locationAttributeTypeRepository.findByShortName(any(String.class))).thenReturn(noOfStudents);
	assertThat(locationService.getLocationAttributeTypeByShortName(noOfStudents.getAttributeName()),
		is(noOfStudents));
	verify(locationAttributeTypeRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetLocationById() {
	Optional<Location> hogwartzObj = Optional.of(hogwartz);
	when(locationRepository.findById(any(Integer.class))).thenReturn(hogwartzObj);
	assertThat(locationService.getLocationById(0), is(hogwartz));
	verify(locationRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationByShortName() {
	when(locationRepository.findByShortName(any(String.class))).thenReturn(hogwartz);
	assertThat(locationService.getLocationByShortName(hogwartz.getShortName()), is(hogwartz));
	verify(locationRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationByAddress(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetLocationsByAddress() {
	when(locationRepository.findByAddress(any(String.class), any(String.class), any(String.class),
		any(String.class), any(String.class))).thenReturn(Arrays.asList(hogwartz, diagonalley));
	assertEquals(2, locationService.getLocationsByAddress("", "", "", "England").size());
	verify(locationRepository, times(1)).findByAddress(any(String.class), any(String.class), any(String.class),
		any(String.class), any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationsByCategory(com.ihsinformatics.coronavirus.model.Definition)}.
     */
    @Test
    public void shouldGetLocationsByCategory() {
	when(locationRepository.findByCategory(any(Definition.class))).thenReturn(Arrays.asList(hogwartz, diagonalley));
	assertThat(locationService.getLocationsByCategory(school), containsInAnyOrder(diagonalley, hogwartz));
	verify(locationRepository, times(1)).findByCategory(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationByContact(java.lang.String, boolean)}.
     */
    @Test
    public void shouldGetLocationsByContact() {
	when(locationRepository.findByContact(any(String.class), any(Boolean.class)))
		.thenReturn(Arrays.asList(hogwartz, burrow));
	assertEquals(2, locationService.getLocationsByContact("+447911123456", Boolean.FALSE).size());
	verify(locationRepository, times(1)).findByContact(any(String.class), any(Boolean.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationsByName() {
	when(locationRepository.findByLocationName(any(String.class))).thenReturn(Arrays.asList(hogwartz, diagonalley));
	assertThat(locationService.getLocationsByName(hogwartz.getLocationName()),
		containsInAnyOrder(hogwartz, diagonalley));
	verify(locationRepository, times(1)).findByLocationName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationsByParent(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldGetLocationsByParent() {
	diagonalley.setParentLocation(hogwartz);
	when(locationRepository.findByParentLocation(any(Location.class))).thenReturn(Arrays.asList(diagonalley));
	assertThat(locationService.getLocationsByParent(hogwartz), containsInAnyOrder(diagonalley));
	verify(locationRepository, times(1)).findByParentLocation(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#deleteLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotDeleteLocation() {
	hogwartz.getAttributes().add(noOfHogwartzStudents);
	doNothing().when(locationRepository).delete(any(Location.class));
	locationService.deleteLocation(hogwartz, false);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#deleteLocationAttributeType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotDeleteLocationAttributeType() {
	when(locationAttributeRepository.findByAttributeType(any(LocationAttributeType.class)))
		.thenReturn(Arrays.asList(noOfHogwartzStudents));
	locationService.deleteLocationAttributeType(noOfStudents, false);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#saveLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotSaveLocation() {
	when(locationRepository.findByShortName(any(String.class))).thenReturn(diagonalley);
	locationService.saveLocation(diagonalley);
    }

    @Test
    public void shouldReturnAnObject() {
	Location location = mock(Location.class);
	assertNotNull(location);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#saveLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldSaveLocation() {
	when(locationRepository.findByShortName(any(String.class))).thenReturn(null);
	when(locationRepository.save(any(Location.class))).thenReturn(diagonalley);
	assertThat(locationService.saveLocation(diagonalley), is(diagonalley));
	verify(locationRepository, times(1)).save(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#saveLocationAttribute(com.ihsinformatics.coronavirus.model.LocationAttribute)}.
     */
    @Test
    public void shouldSaveLocationAttribute() {
	when(locationAttributeRepository.save(any(LocationAttribute.class))).thenReturn(noOfHogwartzStudents);
	assertThat(locationService.saveLocationAttribute(noOfHogwartzStudents), is(noOfHogwartzStudents));
	verify(locationAttributeRepository, times(1)).save(any(LocationAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#saveLocationAttributes(java.util.List)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldSaveLocationAttributes() {
	when(locationAttributeRepository.saveAll(any(List.class)))
		.thenReturn(Arrays.asList(noOfHogwartzStudents, noOfDiagonalleyTeachers, noOfHogwartzTeachers));
	List<LocationAttribute> attributes = locationService.saveLocationAttributes(locationAttributes);
	assertEquals(locationAttributes.size(), attributes.size());
	assertThat(attributes, containsInAnyOrder(noOfHogwartzStudents, noOfHogwartzTeachers, noOfDiagonalleyTeachers));
	verify(locationAttributeRepository, times(1)).saveAll(any(List.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#saveLocationAttributeType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test
    public void shouldSaveLocationAttributeType() {
	when(locationAttributeTypeRepository.save(any(LocationAttributeType.class))).thenReturn(noOfStudents);
	assertThat(locationService.saveLocationAttributeType(noOfStudents), is(noOfStudents));
	verify(locationAttributeTypeRepository, times(1)).save(any(LocationAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#searchLocations(java.util.List)}.
     */
    @Test
    public void shouldSearchLocationsByParams() {
	// Refer to LocationRepositoryTest class
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#updateLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldUpdateLocation() {
	when(locationRepository.save(any(Location.class))).thenReturn(hogwartz);
	hogwartz = locationService.updateLocation(hogwartz);
	assertNotNull(hogwartz.getDateUpdated());
	verify(locationRepository, times(1)).save(any(Location.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#updateLocationAttributeType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test
    public void shouldUpdateLocationAttribute() {
	when(locationAttributeRepository.save(any(LocationAttribute.class))).thenReturn(noOfHogwartzStudents);
	assertNotNull(locationService.updateLocationAttribute(noOfHogwartzStudents).getDateUpdated());
	verify(locationAttributeRepository, times(1)).save(any(LocationAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#updateLocationAttributeType(com.ihsinformatics.coronavirus.model.LocationAttributeType)}.
     */
    @Test
    public void shouldUpdateLocationAttributeType() {
	when(locationAttributeTypeRepository.save(any(LocationAttributeType.class))).thenReturn(noOfStudents);
	assertNotNull(locationService.updateLocationAttributeType(noOfStudents).getDateUpdated());
	verify(locationAttributeTypeRepository, times(1)).save(any(LocationAttributeType.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#unvoidLocation(com.ihsinformatics.coronavirus.model.Location)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnvoidLocation() throws HibernateException, ValidationException, IOException {
    hogwartz.setIsVoided(true);
    hogwartz.setReasonVoided("Testing");
    
    List<LocationAttribute> locationAttributes = Arrays.asList(noOfHogwartzStudents, noOfHogwartzTeachers);
    hogwartz.setAttributes(locationAttributes);
    
	when(locationRepository.save(any(Location.class))).thenReturn(hogwartz);
	locationService.unvoidLocation(hogwartz);
	verify(locationRepository, times(1)).save(any(Location.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#voidLocation(com.ihsinformatics.coronavirus.model.Location)}.
     */
    @Test
    public void shouldVoidLocation() {
	doNothing().when(locationRepository).softDelete(any(Location.class));
	try {
		List<LocationAttribute> locationAttributes = Arrays.asList(noOfHogwartzStudents, noOfHogwartzTeachers);
	    hogwartz.setAttributes(locationAttributes);		
		locationService.voidLocation(hogwartz);
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ValidationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	verify(locationRepository, times(1)).softDelete(any(Location.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationAttributeByUuid() {
	when(locationAttributeRepository.findByUuid(any(String.class))).thenReturn(noOfHogwartzStudents);
	assertEquals(locationService.getLocationAttributeByUuid(noOfHogwartzStudents.getUuid()).getUuid(), noOfHogwartzStudents.getUuid());
	verify(locationAttributeRepository, times(1)).findByUuid(any(String.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.LocationServiceImpl#getLocationAttributeTypeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetLocationAttributeTypeByUuid() {
	when(locationAttributeTypeRepository.findByUuid(any(String.class))).thenReturn(noOfStudents);
	assertEquals(locationService.getLocationAttributeTypeByUuid(noOfStudents.getUuid()).getUuid(), noOfStudents.getUuid());
	verify(locationAttributeTypeRepository, times(1)).findByUuid(any(String.class));
    }
}
