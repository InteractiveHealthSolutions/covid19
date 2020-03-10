/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.util;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class SystemResourceUtil {

    public static final int BYTES_IN_ONE_KB = 1024;

    public static final int BYTES_IN_ONE_MB = 1048576;

    public static final int BYTES_IN_ONE_GB = 1073741824;

    public static final int HISTORY_SIZE = 600;

    private static SystemResourceUtil instance = new SystemResourceUtil();

    private Queue<Float> diskHistory;

    private Queue<Float> memoryHistory;

    private Queue<Float> cpuHistory;

    // This has to be a singleton
    private SystemResourceUtil() {
	diskHistory = new ArrayBlockingQueue<>(HISTORY_SIZE);
	memoryHistory = new ArrayBlockingQueue<>(HISTORY_SIZE);
	cpuHistory = new ArrayBlockingQueue<>(HISTORY_SIZE);
    }

    /**
     * @return the instance
     */
    public static SystemResourceUtil getInstance() {
	return instance;
    }

    /**
     * Resets all history
     */
    public void clearHistory() {
	diskHistory.clear();
	memoryHistory.clear();
	cpuHistory.clear();
    }

    /**
     * Returns percentage of available disk space
     * 
     * @return
     */
    public float getAverageDiskAvailabilityPercentage() {
	OptionalDouble average = diskHistory.stream().mapToDouble(i -> i).average();
	return (float) (average.isPresent() ? average.getAsDouble() : 0);
    }

    /**
     * Returns percentage of available memory
     * 
     * @return
     */
    public float getAverageMemoryAvailabilityPercentage() {
	OptionalDouble average = memoryHistory.stream().mapToDouble(i -> i).average();
	return (float) (average.isPresent() ? average.getAsDouble() : 0);
    }

    /**
     * Returns percentage of available CPU resources
     * 
     * @return
     */
    public float getAverageProcessorAvailabilityPercentage() {
	OptionalDouble average = cpuHistory.stream().mapToDouble(i -> i).average();
	return (float) (average.isPresent() ? average.getAsDouble() : 0);
    }

    public int getCurrentHistorySize() {
	return diskHistory.size();
    }

    /**
     * Returns percentage of available disk space
     * 
     * @return
     */
    public float getDiskAvailabilityPercentage() {
	float max = (float) new File("/").getTotalSpace() / BYTES_IN_ONE_GB;
	float used = max - (float) new File("/").getFreeSpace() / BYTES_IN_ONE_GB;
	return (max - used) * 100 / max;
    }

    /**
     * Returns percentage of available memory
     * 
     * @return
     */
    public float getMemoryAvailabilityPercentage() {
	MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
	float max = (float) memoryMXBean.getHeapMemoryUsage().getMax() / BYTES_IN_ONE_GB;
	float used = (float) memoryMXBean.getHeapMemoryUsage().getUsed() / BYTES_IN_ONE_GB;
	return (max - used) * 100 / max;
    }

    /**
     * Returns the number of CPU cores in the machine
     * 
     * @return
     */
    public int getNumberOfProcessors() {
	return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Returns the amount of RAM available in bytes
     * 
     * @param unit either 'k', 'm' or 'g' representing the unit of memory to return
     *             the value in
     * @return
     */
    public long getTotalMemory(char unit) {
	switch (unit) {
	case 'k':
	case 'K':
	    return Runtime.getRuntime().maxMemory() / BYTES_IN_ONE_KB;
	case 'm':
	case 'M':
	    return Runtime.getRuntime().maxMemory() / BYTES_IN_ONE_MB;
	case 'g':
	case 'G':
	    return Runtime.getRuntime().maxMemory() / BYTES_IN_ONE_GB;
	default:
	    return Runtime.getRuntime().maxMemory();
	}
    }

    /**
     * Returns percentage of available CPU resources
     * 
     * @return
     */
    public float getProcessorAvailabilityPercentage() {
	try {
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[] { "ProcessCpuLoad" });
	    if (list.isEmpty()) {
		return -1f;
	    }
	    Attribute att = (Attribute) list.get(0);
	    Double value = (Double) att.getValue();
	    if (value == -1.0) {
		return -1f;
	    }
	    return 100 - (float) ((int) (value * 1000) / 10.0);
	} catch (Exception e) {
	    return -1f;
	}
    }

    /**
     * Fetch resources and store in history. If the HISTORY_SIZE limit is reached,
     * the first item from each queue is removed before entering new value
     */
    @MeasureProcessingTime
    public void noteReadings() {
	if (diskHistory.size() >= HISTORY_SIZE) {
	    diskHistory.remove();
	}
	diskHistory.add(getDiskAvailabilityPercentage());
	if (memoryHistory.size() >= HISTORY_SIZE) {
	    memoryHistory.remove();
	}
	memoryHistory.add(getMemoryAvailabilityPercentage());
	if (cpuHistory.size() >= HISTORY_SIZE) {
	    cpuHistory.remove();
	}
	cpuHistory.add(getProcessorAvailabilityPercentage());
    }
}
