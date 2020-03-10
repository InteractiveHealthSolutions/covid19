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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.repository.UserRepository;
import com.ihsinformatics.coronavirus.service.SecurityServiceImpl;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class SecurityServiceTest extends BaseRepositoryData {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityServiceImpl securityService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
	Authentication authentication = mock(Authentication.class);
	SecurityContext securityContext = mock(SecurityContext.class);
	when(securityContext.getAuthentication()).thenReturn(authentication);
	SecurityContextHolder.setContext(securityContext);
	when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(admin.getUsername());

	initPrivileges();
	initRoles();
	initUsers();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#hasPrivilege(java.lang.String)}.
     */
    @Test
    public void shouldHavePrivilege() {
	when(userRepository.findByUsername(any(String.class))).thenReturn(dumbledore);
	when(userRepository.findUsersByUserRolesRoleId(any(Integer.class)))
		.thenReturn(Arrays.asList(admin, dumbledore));
	assertTrue(securityService.hasPrivilege(charm.getPrivilegeName()));
	verify(userRepository, times(1)).findByUsername(any(String.class));
	verify(userRepository, times(1)).findUsersByUserRolesRoleId(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.BaseService#hasPrivilege(java.lang.String)}.
     */
    @Test
    public void shouldHavePrivilegeInRoles() {
	initRoles();
	dumbledore.getUserRoles().add(headmaster);
	when(userRepository.findByUsername(any(String.class))).thenReturn(dumbledore);
	when(userRepository.findUsersByUserRolesRoleId(any(Integer.class))).thenReturn(Collections.emptyList());
	assertTrue(securityService.hasPrivilege(charm.getPrivilegeName()));
	verify(userRepository, times(1)).findByUsername(any(String.class));
	verify(userRepository, times(1)).findUsersByUserRolesRoleId(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.SecurityServiceImpl#login(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldLogin() throws Exception {
	when(userRepository.findByUsername(any(String.class))).thenReturn(dumbledore);
	boolean isLoggedIn = securityService.login(dumbledore.getUsername(), "Expelliarmus");
	assertTrue(isLoggedIn);
	assertThat(SecurityServiceImpl.getCurrentUser(), is(dumbledore.getUsername()));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.SecurityServiceImpl#logout()}.
     */
    @Test
    public void shouldLogout() {
	securityService.logout();
	assertNull(SecurityServiceImpl.getCurrentUser());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.SecurityServiceImpl#login(java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldNotLogin() {
	when(userRepository.findByUsername(any(String.class))).thenReturn(dumbledore);
	boolean isLoggedIn = securityService.login(dumbledore.getUsername(), "InvalidPassword");
	assertFalse(isLoggedIn);
	assertNull(SecurityServiceImpl.getCurrentUser());
    }
}
