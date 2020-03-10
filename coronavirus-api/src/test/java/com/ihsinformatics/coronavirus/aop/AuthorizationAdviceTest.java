/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.aop;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.aop.AuthorizationAdvice;
import com.ihsinformatics.coronavirus.service.SecurityService;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationAdviceTest extends BaseTestData {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private AuthorizationAdvice authorizationAdvice = new AuthorizationAdvice();

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.aop.AuthorizationAdvice#checkAccess(org.aspectj.lang.ProceedingJoinPoint, org.ihsinformatics.coronavirus.annotation.CheckPrivilege)}.
     * 
     * @throws Throwable
     */
    @Test
    public void shouldAuthorize() throws Throwable {
	when(securityService.getLoggedInUser()).thenReturn(admin);
	when(securityService.hasPrivilege(any(String.class))).thenReturn(true);
	CheckPrivilege checkPrivilege = new CheckPrivilege() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return null;
	    }

	    @Override
	    public String privilege() {
		return "USE MAGIC";
	    }
	};
	authorizationAdvice.checkAccess(joinPoint, checkPrivilege);
	verify(joinPoint, times(1)).proceed();
	verify(securityService, times(1)).getLoggedInUser();
	verify(securityService, times(1)).hasPrivilege(any(String.class));
	verify(joinPoint, never()).proceed(null);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.aop.AuthorizationAdvice#checkAccess(org.aspectj.lang.ProceedingJoinPoint, org.ihsinformatics.coronavirus.annotation.CheckPrivilege)}.
     * 
     * @throws Throwable
     */
    @Test(expected = Throwable.class)
    public void shouldNotAuthorize() throws Throwable {
	when(securityService.getLoggedInUser()).thenReturn(admin);
	when(securityService.hasPrivilege(any(String.class))).thenReturn(false);
	CheckPrivilege checkPrivilege = new CheckPrivilege() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return null;
	    }

	    @Override
	    public String privilege() {
		return "USE MAGIC";
	    }
	};
	authorizationAdvice.checkAccess(joinPoint, checkPrivilege);
    }
}
