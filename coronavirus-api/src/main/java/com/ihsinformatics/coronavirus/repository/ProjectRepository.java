/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Project;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT p FROM Project p WHERE p.dateGrantBegin >= :from AND p.dateGrantBegin <= :to")
    List<Project> findByDateRange(@Param("from") Date from, @Param("to") Date to);

    List<Project> findByDonor(Donor donor);

    @Query("SELECT p FROM Project p WHERE p.projectName LIKE CONCAT('%', :projectName, '%')")
    List<Project> findByProjectName(@Param("projectName") String projectName);

    Project findByShortName(String name);

    Project findByUuid(String uuid);
    
    @Query("UPDATE Project d set d.isVoided = true WHERE d = :project")
    @Modifying
    void softDelete(@Param("project") Project project);
}
