/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.aop;

import javax.security.auth.login.LoginException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AuthorizationServiceException;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.service.SecurityService;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Aspect
@Configuration
public class AuthorizationAdvice {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecurityService securityService;

    @Around(value = "@annotation(checkPrivilege)")
    public Object checkAccess(ProceedingJoinPoint joinPoint, CheckPrivilege checkPrivilege) throws Throwable {
	User user = securityService.getLoggedInUser();
	if (user == null) {
	    throw new LoginException(
		    String.format("No user is currently logged into the system. Execution of '%s' cannot be performed",
			    joinPoint.getSignature()));
	}
	if (securityService.hasPrivilege(checkPrivilege.privilege())) {
	    if (LOG.isDebugEnabled()) {
		LOG.info("Allowed execution to '{}' for '{}'", user.getUsername(), joinPoint.getSignature());
	    }
	    return joinPoint.proceed();
	}
	String message = String.format("User '%s' is not authorized to execute '%s'", user, joinPoint.getSignature());
	throw new AuthorizationServiceException(message, new Throwable(message));
    }
}
