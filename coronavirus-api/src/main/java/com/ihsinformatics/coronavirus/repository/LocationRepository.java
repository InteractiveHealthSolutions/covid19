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

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.Location;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface LocationRepository extends CustomLocationRepository, JpaRepository<Location, Integer> {

    List<Location> findByCategory(Definition category);

    @Query("SELECT l FROM Location l WHERE l.locationName LIKE CONCAT('%', :locationName, '%')")
    List<Location> findByLocationName(@Param("locationName") String locationName);

    List<Location> findByParentLocation(Location parentLocation);

    Location findByShortName(String shortName);

    Location findByUuid(String uuid);
    
    @Query("UPDATE Location d set d.isVoided = true WHERE d = :location")
    @Modifying
    void softDelete(@Param("location") Location locaion);
}
