/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.IOException;
import java.util.List;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.dto.LocationDesearlizeDto;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.util.SearchCriteria;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */

@Service
public interface LocationService {

    /**
     * Setting force true will remove the {@link LocationAttribute} entities as well
     * 
     * @param obj
     * @param force
     * @throws HibernateException
     */
    void deleteLocation(Location obj, boolean force) throws HibernateException;

    /**
     * @param obj
     * @throws HibernateException
     */
    void deleteLocationAttribute(LocationAttribute obj) throws HibernateException;

    /**
     * Caution! Setting force true will completely remove each dependent entity
     * {@link LocationAttribute} as well
     * 
     * @param obj
     * @param force
     * @throws HibernateException
     */
    void deleteLocationAttributeType(LocationAttributeType obj, boolean force) throws HibernateException;

    /**
     * Returns list of {@link LocationAttributeType} objects
     * 
     * @return
     * @throws HibernateException
     */
    List<LocationAttributeType> getAllLocationAttributeTypes() throws HibernateException;

    /**
     * Returns list of {@link Location} objects
     * 
     * @return
     * @throws HibernateException
     */
    List<Location> getAllLocations() throws HibernateException;

    /**
     * Returns {@link LocationAttribute} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    LocationAttribute getLocationAttributeById(Integer id) throws HibernateException;

    /**
     * Returns {@link LocationAttribute} object by matching UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    LocationAttribute getLocationAttributeByUuid(String uuid) throws HibernateException;

    /**
     * Returns list of {@link LocationAttribute} objects by given {@link Location}
     * and {@link LocationAttributeType}
     * 
     * @param location
     * @param attributeType
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> getLocationAttributes(Location location, LocationAttributeType attributeType)
	    throws HibernateException;

    /**
     * Returns list of {@link LocationAttribute} objects by given {@link Location}
     * 
     * @param location
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> getLocationAttributesByLocation(Location location) throws HibernateException;

    /**
     * Returns list of {@link LocationAttribute} objects by given
     * {@link LocationAttributeType}
     * 
     * @param attributeType
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> getLocationAttributesByType(LocationAttributeType attributeType) throws HibernateException;

    /**
     * Returns list of {@link LocationAttribute} objects by given
     * {@link LocationAttributeType} and its value
     * 
     * @param attributeType
     * @param value
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> getLocationAttributesByTypeAndValue(LocationAttributeType attributeType, String value)
	    throws HibernateException;

    /**
     * Returns list of {@link LocationAttribute} objects by matching value
     * 
     * @param value
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> getLocationAttributesByValue(String value) throws HibernateException;

    /**
     * Returns {@link LocationAttributeType} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    LocationAttributeType getLocationAttributeTypeById(Integer id) throws HibernateException;

    /**
     * Returns {@link LocationAttributeType} object by matching name
     * 
     * @param name
     * @return
     * @throws HibernateException
     */
    LocationAttributeType getLocationAttributeTypeByName(String name) throws HibernateException;

    /**
     * Returns {@link LocationAttributeType} object by matching short name
     * 
     * @param shortName
     * @return
     * @throws HibernateException
     */
    LocationAttributeType getLocationAttributeTypeByShortName(String shortName) throws HibernateException;

    /**
     * Returns {@link LocationAttributeType} object by matching UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    LocationAttributeType getLocationAttributeTypeByUuid(String uuid) throws HibernateException;

    /**
     * Returns {@link Location} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    Location getLocationById(Integer id) throws HibernateException;

    /**
     * Returns {@link Location} object by matching short name
     * 
     * @param shortName
     * @return
     * @throws HibernateException
     */
    Location getLocationByShortName(String shortName) throws HibernateException;

    /**
     * Returns {@link Location} object by matching UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    Location getLocationByUuid(String uuid) throws HibernateException;

    /**
     * Returns list of {@link Location} objects by matching given address parameters
     * 
     * @param address
     * @param cityVillage
     * @param stateProvince
     * @param country
     * @return
     * @throws HibernateException
     */
    List<Location> getLocationsByAddress(String address, String cityVillage, String stateProvince, String country)
	    throws HibernateException;

    /**
     * Returns list of {@link Location} objects by category given as
     * {@link Definition} object
     * 
     * @param definition
     * @return
     * @throws HibernateException
     */
    List<Location> getLocationsByCategory(Definition definition) throws HibernateException;

    /**
     * Returns list of {@link Location} objects by matching given contact number
     * 
     * @param contact
     * @param primaryContactOnly when true, only primary contact number is matched
     * @return
     * @throws HibernateException
     */
    List<Location> getLocationsByContact(String contact, Boolean primaryContactOnly) throws HibernateException;

    /**
     * Returns list of {@link Location} objects by matching name
     * 
     * @param name
     * @return
     * @throws HibernateException
     */
    List<Location> getLocationsByName(String name) throws HibernateException;

    /**
     * Returns list of {@link Location} objects by parent {@link Location} object
     * 
     * @param parentLocation
     * @return
     * @throws HibernateException
     */
    List<Location> getLocationsByParent(Location parentLocation) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    Location saveLocation(Location obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    LocationAttribute saveLocationAttribute(LocationAttribute obj) throws HibernateException;

    /**
     * @param attributes
     * @return
     * @throws HibernateException
     */
    List<LocationAttribute> saveLocationAttributes(List<LocationAttribute> attributes) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    LocationAttributeType saveLocationAttributeType(LocationAttributeType obj) throws HibernateException;

    /**
     * Returns a list of {@link Location} objects by matching given parameters
     * 
     * @param params
     * @return
     * @throws HibernateException
     */
    @MeasureProcessingTime
    List<Location> searchLocations(List<SearchCriteria> params) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    Location updateLocation(Location obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    LocationAttribute updateLocationAttribute(LocationAttribute obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    LocationAttributeType updateLocationAttributeType(LocationAttributeType obj) throws HibernateException;
    
    
    /**
     * @param uuid
     * @return
     * @throws HibernateException
     */
    LocationDesearlizeDto getLocationDesearlizeDtoUuid(String uuid, LocationService locationService, MetadataService metadataService, UserService userService, DonorService donorService) throws HibernateException;

    /**
     * Restore the voided {@link Location} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    void voidLocation(Location obj) throws HibernateException, ValidationException, IOException;
    
    /**
     * Restore the voided {@link Location} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    Location unvoidLocation(Location obj) throws HibernateException, ValidationException, IOException;

    
}
