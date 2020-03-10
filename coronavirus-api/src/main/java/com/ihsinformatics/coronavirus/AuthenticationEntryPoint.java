/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */

@Component
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    public static final String AAHUNG_CORONAVIRUS_AUTH_REALM = "AAHUNG_CORONAVIRUS_AUTH_REALM";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
	    throws IOException, ServletException {
	response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	PrintWriter writer = response.getWriter();
	writer.println("HTTP Status 401 - " + authEx.getMessage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
	setRealmName(AAHUNG_CORONAVIRUS_AUTH_REALM);
	super.afterPropertiesSet();
    }
}
