/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/
package com.ihsinformatics.coronavirus.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.repository.RoleRepository;
import com.ihsinformatics.coronavirus.repository.UserRepository;
import com.ihsinformatics.coronavirus.service.BaseService;
import com.ihsinformatics.coronavirus.service.SecurityService;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
public class _BaseServiceTest extends BaseTestData {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private BaseService baseService;

    @Before
    public void reset() {
	super.reset();
	initPrivileges();
	initRoles();
	dumbledore.setUserId(100);
	dumbledore.getUserRoles().add(headmaster);
	when(securityService.getAuditUser()).thenReturn(dumbledore);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setCreateAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetCreateAuditAttributesForEntity() {
	User entity = (User) baseService.setCreateAuditAttributes(tonks);
	assertNotNull(entity.getDateCreated());
	assertNotNull(entity.getCreatedBy());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setCreateAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetCreateAuditAttributesForMetadata() {
	DefinitionType entity = (DefinitionType) baseService.setCreateAuditAttributes(country);
	assertNotNull(entity.getDateCreated());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setSoftDeleteAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetSoftDeleteAuditAttributesForEntity() {
	User entity = (User) baseService.setSoftDeleteAuditAttributes(dumbledore);
	assertNotNull(entity.getDateVoided());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setSoftDeleteAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetSoftDeleteAuditAttributesForMetadata() {
	DefinitionType entity = (DefinitionType) baseService.setSoftDeleteAuditAttributes(country);
	assertNotNull(entity.getDateRetired());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setUpdateAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetUpdateAuditAttributesForEntity() {
	User entity = (User) baseService.setUpdateAuditAttributes(tonks);
	assertNotNull(entity.getDateUpdated());
	assertNotNull(entity.getUpdatedBy());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#setUpdateAuditAttributes(com.ihsinformatics.coronavirus.model.BaseEntity)}.
     */
    @Test
    public void shouldSetUpdateAuditAttributesForMetadata() {
	DefinitionType entity = (DefinitionType) baseService.setUpdateAuditAttributes(country);
	assertNotNull(entity.getDateUpdated());
    }
}
