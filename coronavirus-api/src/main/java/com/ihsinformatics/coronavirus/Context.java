/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus;

import java.lang.management.ManagementFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.ihsinformatics.coronavirus.util.DateTimeUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 */

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Context extends SpringBootServletInitializer {

    public static final String DEFAULT_DATE_FORMAT;

    public static final String DEFAULT_DATETIME_FORMAT;

    public static final int MAX_RESULT_SIZE;

    public static final boolean DEBUG_MODE;

    static {
	DEFAULT_DATE_FORMAT = DateTimeUtil.SQL_DATE;
	DEFAULT_DATETIME_FORMAT = DateTimeUtil.SQL_DATETIME;
	MAX_RESULT_SIZE = 500;
	DEBUG_MODE = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
    }

    public static void main(String[] args) {
	SpringApplication.run(Context.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	return application.sources(Context.class);
    }
}
