/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.util;

import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.util.SystemResourceUtil;

import junit.framework.TestCase;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class SystemResourceUtilTest extends TestCase {

    SystemResourceUtil instance;

    @Before
    public void setUp() {
	instance = SystemResourceUtil.getInstance();
    }

    private void sleep() throws InterruptedException {
	Thread.sleep(50);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#clearHistory()}.
     */
    @Test
    public void testClearHistory() {
	instance.clearHistory();
	assertEquals(0, instance.getCurrentHistorySize());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#getAverageDiskAvailabilityPercentage()}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testGetAverageDiskAvailabilityPercentage() throws InterruptedException {
	for (int i = 0; i < 5; i++) {
	    instance.noteReadings();
	    sleep();
	}
	float disk = instance.getAverageDiskAvailabilityPercentage();
	assertTrue(disk > 0f);
	assertTrue(disk < 100f);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#getAverageMemoryAvailabilityPercentage()}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testGetAverageMemoryAvailabilityPercentage() throws InterruptedException {
	for (int i = 0; i < 5; i++) {
	    instance.noteReadings();
	    sleep();
	}
	float memory = instance.getAverageMemoryAvailabilityPercentage();
	assertTrue(memory > 0f);
	assertTrue(memory < 100f);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#getAverageProcessorAvailabilityPercentage()}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testGetAverageProcessorAvailabilityPercentage() throws InterruptedException {
	for (int i = 0; i < 5; i++) {
	    instance.noteReadings();
	    sleep();
	}
	float cpu = instance.getAverageProcessorAvailabilityPercentage();
	assertTrue(cpu > 0f);
	assertTrue(cpu <= 100f);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#getCurrentHistorySize()}.
     * 
     * @throws InterruptedException
     */
    @Test
    public void testGetCurrentHistorySize() throws InterruptedException {
	instance.clearHistory();
	for (int i = 0; i < 5; i++) {
	    instance.noteReadings();
	    sleep();
	}
	assertEquals(5, instance.getCurrentHistorySize());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.SystemResourceUtil#noteReadings()}.
     */
    @Test
    public void testNoteReadings() {
	instance.noteReadings();
	float disk = instance.getAverageDiskAvailabilityPercentage();
	float memory = instance.getAverageMemoryAvailabilityPercentage();
	float cpu = instance.getAverageProcessorAvailabilityPercentage();
	assertTrue(disk > 0f);
	assertTrue(disk < 100f);
	assertTrue(memory > 0f);
	assertTrue(memory < 100f);
	assertTrue(cpu >= 0f);
	assertTrue(cpu <= 100f);
    }
}
