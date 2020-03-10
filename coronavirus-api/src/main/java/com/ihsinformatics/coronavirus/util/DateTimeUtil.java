/*
Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

package com.ihsinformatics.coronavirus.util;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for several Date and Time functions
 * 
 * @author owais.hussain@ihsinformatics.com
 */
public class DateTimeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtil.class);

    /**
     * @deprecated because dd and MM can be confused with each other
     */
    @Deprecated
    public static final String FE_FORMAT = "dd/MM/yyyy HH:mm:ss";

    /**
     * @deprecated because dd and MM can be confused with each other
     */
    @Deprecated
    public static final String FE_FORMAT_TRUNC = "dd/MM/yyyy";

    /**
     * @deprecated because dd and MM can be confused with each other
     */
    @Deprecated
    public static final String DOB_FORMAT = "dd/MM/yyyy";

    public static final String STANDARD_DATE = "dd/MM/yyyy";

    public static final String STANDARD_DATETIME = "dd/MM/yyyy HH:mm:ss";

    public static final String STANDARD_DATE_HYPHENATED = "dd-MM-yyyy";

    public static final String STANDARD_DATETIME_HYPHENATED = "dd-MM-yyyy HH:mm:ss";

    public static final String SQL_DATE = "yyyy-MM-dd";

    public static final String SQL_DATESTAMP = "yyyyMMdd";

    public static final String SQL_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String SQL_TIMESTAMP = "yyyyMMddHHmmss";

    public static final String US_DATE = "MM/dd/yyyy";

    public static final String US_SHORT_DATE = "MM/dd/yy";

    public static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss"; // 1970-01-01T00:00:00.000

    public static final String ISO8601_FULL = "yyyy-MM-dd'T'HH:mm:ss.'Z'"; // 1970-01-01T00:00:00.000+0000

    private static final Map<String, String> DATE_FORMATS;

    static {
	DATE_FORMATS = new HashMap<>();
	// Time only
	DATE_FORMATS.put("^[0-2]\\d[0-5]\\d$", "HHmm");
	DATE_FORMATS.put("^[0-2]\\d:[0-5]\\d$", "HH:mm");
	DATE_FORMATS.put("^[0-2]\\d([0-5]\\d){2}$", "HHmmss");
	DATE_FORMATS.put("^[0-2]\\d:[0-5]\\d:[0-5]\\d$", "HH:mm:ss");
	// Date only
	DATE_FORMATS.put("^\\d{2}[0-1]\\d[0-3]\\d$", "yyMMdd");
	DATE_FORMATS.put("^\\d{4}[0-1]\\d[0-3]\\d$", SQL_DATESTAMP);
	DATE_FORMATS.put("^\\d{2}-[0-1]\\d-[0-3]\\d$", "yy-MM-dd");
	DATE_FORMATS.put("^\\d{4}-[0-1]\\d-[0-3]\\d$", SQL_DATE);
	DATE_FORMATS.put("^[0-3]\\d-[0-1]\\d-\\d{2}$", "dd-MM-yy");
	DATE_FORMATS.put("^[0-3]\\d-[0-1]\\d-\\d{4}$", STANDARD_DATE_HYPHENATED);
	DATE_FORMATS.put("^[0-3]\\d [0-1]\\d \\d{2}$", "dd MM yy");
	DATE_FORMATS.put("^[0-3]\\d [0-1]\\d \\d{4}$", "dd MM yyyy");
	DATE_FORMATS.put("^[0-3]\\d/[0-1]\\d/\\d{2}$", "dd/MM/yy");
	DATE_FORMATS.put("^[0-3]\\d/[0-1]\\d/\\d{4}$", STANDARD_DATE);
	DATE_FORMATS.put("^[0-3]\\d [a-z]{3} \\d{2}$", "dd MMM yy");
	DATE_FORMATS.put("^[0-3]\\d [a-z]{3} \\d{4}$", "dd MMM yyyy");
	// Date and Time
	DATE_FORMATS.put("^\\d{12,14}$", "tt");
	DATE_FORMATS.put("^\\d{4}[0-1]\\d[0-3]\\d[0-2]\\d[0-5]\\d$", "yyyyMMddHHmm");
	DATE_FORMATS.put("^\\d{4}[0-1]\\d[0-3]\\d [0-2]\\d[0-5]\\d$", "yyyyMMdd HHmm");
	DATE_FORMATS.put("^\\d{4}[0-1]\\d[0-3]\\d[0-2]\\d([0-5]\\d){2}$", SQL_TIMESTAMP);
	DATE_FORMATS.put("^\\d{4}[0-1]\\d[0-3]\\d [0-2]\\d([0-5]\\d){2}$", "yyyyMMdd HHmmss");
	DATE_FORMATS.put("^\\d{4}-[0-1]\\d-[0-3]\\d [0-2]\\d:[0-5]\\d$", "yyyy-MM-dd HH:mm");
	DATE_FORMATS.put("^\\d{4}-[0-1]\\d-[0-3]\\d [0-2]\\d:[0-5]\\d:[0-5]\\d$", SQL_DATETIME);
	// ISO8601
	DATE_FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}\\.\\d{2,4}$", "yyyy-MM-dd HH:mm:ss.'Z'");
    }

    private DateTimeUtil() {
    }

    public static Date fromString(String string, String format) {
	if (string == null) {
	    return null;
	}
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	Date date;
	try {
	    date = simpleDateFormat.parse(string);
	} catch (ParseException e) {
	    try {
		simpleDateFormat = new SimpleDateFormat(detectDateFormat(string));
		date = simpleDateFormat.parse(string);
	    } catch (ParseException e2) {
		LOG.error(e2.getMessage());
		return null;
	    }
	}
	return date;
    }

    public static String toString(Date date, String format) {
    
	if (date == null) {
	    return null;
	}
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	return simpleDateFormat.format(date);
    }

    public static Date fromSqlDateString(String sqlDate) {
	return fromString(sqlDate, SQL_DATE);
    }

    public static Date fromSqlDateTimeString(String sqlDate) {
	return fromString(sqlDate, SQL_DATETIME);
    }

    public static String toSqlDateString(Date date) {
	return toString(date, SQL_DATE);
    }

    public static String toSqlDateTimeString(Date date) {
	return toString(date, SQL_DATETIME);
    }

    /**
     * Returns closest matching date format from given date in string
     * 
     * @param dateString
     * @return
     */
    public static String detectDateFormat(String dateString) {
	for (Entry<String, String> entry : DATE_FORMATS.entrySet()) {
	    if (dateString.toLowerCase().matches(entry.getKey())) {
		return entry.getValue();
	    }
	}
	throw new InvalidParameterException("Given date does not match any of the standard conventions.");
    }

    /**
     * Returns date object from given parameters
     * 
     * @param date  from 1 to 31
     * @param month from 1 to 12
     * @param year  natural year, like 2019
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final Date create(int date, int month, int year) {
	return new Date(year - 1900, month - 1, date);
    }

    /**
     * Returns date object from given parameters
     * 
     * @param date
     * @param month
     * @param year
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    @SuppressWarnings("deprecation")
    public static final Date create(int date, int month, int year, int hours, int minutes, int seconds) {
	return new Date(year - 1900, month - 1, date, hours, minutes, seconds);
    }
}
