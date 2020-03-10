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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hamcrest.Matchers;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.model.Project;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class DonorServiceTest extends BaseServiceTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#deleteDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test
    public void shouldDeleteDonor() {
	doNothing().when(donorRepository).delete(any(Donor.class));
	donorService.deleteDonor(ministry);
	verify(donorRepository, times(1)).delete(any(Donor.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#deleteProject(com.ihsinformatics.coronavirus.model.Project)}.
     */
    @Test
    public void shouldDeleteProject() {
	doNothing().when(projectRepository).delete(any(Project.class));
	donorService.deleteProject(triwizardTournament);
	verify(projectRepository, times(1)).delete(any(Project.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getAllDonors()}.
     */
    @Test
    public void shouldGetAllDonors() {
	when(donorRepository.findAll()).thenReturn(Arrays.asList(ministry));
	assertEquals(1, donorService.getAllDonors().size());
	verify(donorRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getAllProjects()}.
     */
    @Test
    public void shouldGetAllProjects() {
	when(projectRepository.findAll()).thenReturn(Arrays.asList(triwizardTournament));
	assertEquals(1, donorService.getAllProjects().size());
	verify(projectRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getDonorById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetDonorById() {
	Optional<Donor> optional = Optional.of(ministry);
	when(donorRepository.findById(any(Integer.class))).thenReturn(optional);
	assertEquals(donorService.getDonorById(1), ministry);
	verify(donorRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getDonorByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetDonorByShortName() {
	when(donorRepository.findByShortName(any(String.class))).thenReturn(ministry);
	assertEquals(donorService.getDonorByShortName(ministry.getShortName()), ministry);
	verify(donorRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getDonorByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetDonorByUuid() {
	when(donorRepository.findByUuid(any(String.class))).thenReturn(ministry);
	assertEquals(donorService.getDonorByUuid(ministry.getUuid()), ministry);
	verify(donorRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getDonorsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetDonorsByName() {
	when(donorRepository.findByDonorName(any(String.class))).thenReturn(Arrays.asList(ministry));
	assertThat(donorService.getDonorsByName(ministry.getDonorName()), Matchers.containsInAnyOrder(ministry));
	verify(donorRepository, times(1)).findByDonorName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getProjectById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetProjectById() {
	Optional<Project> optional = Optional.of(triwizardTournament);
	when(projectRepository.findById(any(Integer.class))).thenReturn(optional);
	assertEquals(donorService.getProjectById(1), triwizardTournament);
	verify(projectRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getProjectByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetProjectByShortName() {
	when(projectRepository.findByShortName(any(String.class))).thenReturn(triwizardTournament);
	assertEquals(donorService.getProjectByShortName(triwizardTournament.getShortName()), triwizardTournament);
	verify(projectRepository, times(1)).findByShortName(any(String.class));
	;
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getProjectByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetProjectByUuid() {
	when(projectRepository.findByUuid(any(String.class))).thenReturn(triwizardTournament);
	assertEquals(donorService.getProjectByUuid(triwizardTournament.getUuid()), triwizardTournament);
	verify(projectRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getProjectsByDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test
    public void shouldGetProjectsByDonor() {
	when(projectRepository.findByDonor(any(Donor.class))).thenReturn(Arrays.asList(triwizardTournament));
	assertThat(donorService.getProjectsByDonor(ministry), Matchers.contains(triwizardTournament));
	verify(projectRepository, times(1)).findByDonor(any(Donor.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#getProjectsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetProjectsByName() {
	when(projectRepository.findByProjectName(any(String.class))).thenReturn(Arrays.asList(triwizardTournament));
	assertThat(donorService.getProjectsByName(triwizardTournament.getProjectName()),
		Matchers.containsInAnyOrder(triwizardTournament));
	verify(projectRepository, times(1)).findByProjectName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#deleteDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotDeleteDonor() {
	when(projectRepository.findByDonor(any(Donor.class))).thenReturn(Arrays.asList(triwizardTournament));
	donorService.deleteDonor(ministry);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#saveDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test
    public void shouldSaveDonor() {
	when(donorRepository.save(any(Donor.class))).thenReturn(ministry);
	assertThat(donorService.saveDonor(ministry), is(ministry));
	verify(donorRepository, times(1)).save(any(Donor.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#saveProject(com.ihsinformatics.coronavirus.model.Project)}.
     */
    @Test
    public void shouldSaveProject() {
	when(projectRepository.save(any(Project.class))).thenReturn(triwizardTournament);
	assertThat(donorService.saveProject(triwizardTournament), is(triwizardTournament));
	verify(projectRepository, times(1)).save(any(Project.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#updateDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test
    public void shouldUpdateDonor() {
	when(donorRepository.save(any(Donor.class))).thenReturn(ministry);
	ministry = donorService.updateDonor(ministry);
	assertNotNull(ministry.getDateUpdated());
	verify(donorRepository, times(1)).save(any(Donor.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#updateProject(com.ihsinformatics.coronavirus.model.Project)}.
     */
    @Test
    public void shouldUpdateProject() {
	when(projectRepository.save(any(Project.class))).thenReturn(triwizardTournament);
	triwizardTournament = donorService.updateProject(triwizardTournament);
	assertNotNull(triwizardTournament.getDateUpdated());
	verify(projectRepository, times(1)).save(any(Project.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#unvoidProject(com.ihsinformatics.coronavirus.model.Project)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnvoidProject() throws HibernateException, ValidationException, IOException {
    triwizardTournament.setIsVoided(true);
    triwizardTournament.setReasonVoided("Testing");
	when(projectRepository.save(any(Project.class))).thenReturn(triwizardTournament);
	donorService.unvoidProject(triwizardTournament);
	verify(projectRepository, times(1)).save(any(Project.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#voidProject(com.ihsinformatics.coronavirus.model.Project)}.
     */
    @Test
    public void shouldVoidProject() {
	doNothing().when(projectRepository).softDelete(any(Project.class));
	donorService.voidProject(triwizardTournament);
	verify(projectRepository, times(1)).softDelete(any(Project.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#unvoidDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     * 
     * @throws IOException
     * @throws ValidationException
     * @throws HibernateException
     */
    @Test
    public void shouldUnvoidDonor() throws HibernateException, ValidationException, IOException {
    ministry.setIsVoided(true);
    ministry.setReasonVoided("Testing");
	when(donorRepository.save(any(Donor.class))).thenReturn(ministry);
	donorService.unvoidDonor(ministry);
	verify(donorRepository, times(1)).save(any(Donor.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.DonorServiceImpl#voidDonor(com.ihsinformatics.coronavirus.model.Donor)}.
     */
    @Test
    public void shouldVoidDonor() {
	doNothing().when(donorRepository).softDelete(any(Donor.class));
	donorService.voidDonor(ministry);
	verify(donorRepository, times(1)).softDelete(any(Donor.class));
    }
}
