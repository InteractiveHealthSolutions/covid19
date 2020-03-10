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

import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Project;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Service
public interface DonorService {

    /* Delete methods */
    void deleteDonor(Donor obj) throws HibernateException;

    void deleteProject(Project obj) throws HibernateException;

    /* Fetch methods */
    /**
     * Returns a list of {@link Donor} objects
     * 
     * @return
     */
    List<Donor> getAllDonors();

    /**
     * Returns a list of {@link Project} objects
     * 
     * @return
     */
    List<Project> getAllProjects();

    /**
     * Returns {@link Donor} object by given Id
     * 
     * @param id
     * @return
     */
    Donor getDonorById(Integer id);

    /**
     * Returns {@link Donor} object by matching short name
     * 
     * @param shortName
     * @return
     */
    Donor getDonorByShortName(String shortName);

    /**
     * Returns {@link Donor} object by given UUID
     * 
     * @param uuid
     * @return
     */
    Donor getDonorByUuid(String uuid);

    /**
     * Returns a list of {@link Donor} by matching name
     * 
     * @param name
     * @return
     */
    List<Donor> getDonorsByName(String name);

    /**
     * Returns {@link Project} object by given Id
     * 
     * @param id
     * @return
     */
    Project getProjectById(Integer id);

    /**
     * Returns {@link Project} object by matching short name
     * 
     * @param shortName
     * @return
     */
    Project getProjectByShortName(String shortName);

    /**
     * Returns {@link Project} object by given UUID
     * 
     * @param uuid
     * @return
     */
    Project getProjectByUuid(String uuid);

    /**
     * Returns a list of {@link Project} by given {@link Donor} object
     * 
     * @param donor
     * @return
     */
    List<Project> getProjectsByDonor(Donor donor);

    /**
     * Returns a list of {@link Project} by matching name
     * 
     * @param name
     * @return
     */
    List<Project> getProjectsByName(String name);

    /* Save methods */
    Donor saveDonor(Donor obj);

    Project saveProject(Project obj);

    /* Update methods */
    Donor updateDonor(Donor obj);

    Project updateProject(Project obj);
    
    /**
     * Void {@link Project} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    void voidProject(Project obj) throws HibernateException, ValidationException, IOException;
    
    /**
     * Restore the voided {@link Project} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    Project unvoidProject(Project obj) throws HibernateException, ValidationException, IOException;
    
    /**
     * Void {@link Donor} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    void voidDonor(Donor obj) throws HibernateException, ValidationException, IOException;
    
    /**
     * Restore the voided {@link Project} object
     * 
     * @param obj
     * @throws HibernateException
     * @throws IOException
     * @throws ValidationException
     */
    Donor unvoidDonor(Donor obj) throws HibernateException, ValidationException, IOException;
}
