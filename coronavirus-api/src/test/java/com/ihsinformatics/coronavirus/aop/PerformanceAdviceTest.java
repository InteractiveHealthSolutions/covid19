/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.aop;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.ihsinformatics.coronavirus.aop.PerformanceAdvice;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class PerformanceAdviceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ProceedingJoinPoint joinPoint;

    private PerformanceAdvice performanceAdvice = new PerformanceAdvice();

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.aop.PerformanceAdvice#executionTime(org.aspectj.lang.ProceedingJoinPoint)}.
     * 
     * @throws Throwable
     */
    @Test
    public void shouldExecutionTimeMethod() throws Throwable {
	performanceAdvice.executionTime(joinPoint);
	verify(joinPoint, times(1)).proceed();
	verify(joinPoint, never()).proceed(null);
    }
}
