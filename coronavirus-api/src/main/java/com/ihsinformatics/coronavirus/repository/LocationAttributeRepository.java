/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface LocationAttributeRepository extends JpaRepository<LocationAttribute, Integer> {

    List<LocationAttribute> findByAttributeType(LocationAttributeType attributeType);

    @Query("SELECT a FROM LocationAttribute a WHERE a.attributeValue LIKE CONCAT(:attributeValue, '%') and a.attributeType = :attributeType")
    List<LocationAttribute> findByAttributeTypeAndValue(@Param("attributeType") LocationAttributeType attributeType,
	    @Param("attributeValue") String attributeValue);

    List<LocationAttribute> findByLocation(Location location);

    @Query("SELECT a FROM LocationAttribute a WHERE a.location = :location and a.attributeType = :attributeType")
    List<LocationAttribute> findByLocationAndAttributeType(@Param("location") Location location,
	    @Param("attributeType") LocationAttributeType attributeType);

    LocationAttribute findByUuid(String uuid);

    @Query("SELECT a FROM LocationAttribute a WHERE a.attributeValue LIKE CONCAT(:attributeValue, '%')")
    List<LocationAttribute> findByValue(@Param("attributeValue") String attributeValue);
    
    @Query("UPDATE LocationAttribute d set d.isVoided = true WHERE d = :locationAttribute")
    @Modifying
    void softDelete(@Param("locationAttribute") LocationAttribute locationAttribute);

}
