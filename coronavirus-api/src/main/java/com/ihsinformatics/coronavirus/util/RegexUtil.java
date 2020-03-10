/*
Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */

package com.ihsinformatics.coronavirus.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Regular Expression provider class to verify a valid expression (e.g. valid email address, name, etc.)
 Characters:
 x	The character x
 \\	The backslash character
 \0n	The character with octal value 0n (0 <= n <= 7)
 \0nn	The character with octal value 0nn (0 <= n <= 7)
 \0mnn	The character with octal value 0mnn (0 <= m <= 3, 0 <= n <= 7)
 \xhh	The character with hexadecimal value 0xhh
 \t	The tab character ('\u0009')
 \n	The newline (line feed) character ('
 ')
 \r	The carriage-return character ('
 ')
 \f	The form-feed character ('\u000C')
 \a	The alert (bell) character ('\u0007')
 \e	The escape character ('\u001B')
 \cx	The control character corresponding to x

 Character classes:
 [abc]	a, b, or c (simple class)
 [^abc]	Any character except a, b, or c (negation)
 [a-zA-Z]	a through z or A through Z, inclusive (range)
 [a-d[m-p]]	a through d, or m through p: [a-dm-p] (union)
 [a-z&&[def]]	d, e, or f (intersection)
 [a-z&&[^bc]]	a through z, except for b and c: [ad-z] (subtraction)
 [a-z&&[^m-p]]	a through z, and not m through p: [a-lq-z](subtraction)

 Predefined character classes:
 .	Any character (may or may not match line terminators)
 \d	A digit: [0-9]
 \D	A non-digit: [^0-9]
 \s	A whitespace character: [ \t\n\x0B\f\r]
 \S	A non-whitespace character: [^\s]
 \w	A word character: [a-zA-Z_0-9]
 \W	A non-word character: [^\w]

 Classes for Unicode blocks and categories
 \p{InGreek}	A character in the Greek block (simple block)
 \p{Lu}	An uppercase letter (simple category)
 \p{Sc}	A currency symbol
 \P{InGreek}	Any character except one in the Greek block (negation)
 [\p{L}&&[^\p{Lu}]] 	Any letter except an uppercase letter (subtraction)

 Boundary matchers
 ^	The beginning of a line
 $	The end of a line
 \b	A word boundary
 \B	A non-word boundary
 \A	The beginning of the input
 \G	The end of the previous match
 \Z	The end of the input but for the final terminator, if any
 \z	The end of the input

 Greedy quantifiers
 X?	X, once or not at all
 X*	X, zero or more times
 X+	X, one or more times
 X{n}	X, exactly n times
 X{n,}	X, at least n times
 X{n,m}	X, at least n but not more than m times

 Reluctant quantifiers
 X??	X, once or not at all
 X*?	X, zero or more times
 X+?	X, one or more times
 X{n}?	X, exactly n times
 X{n,}?	X, at least n times
 X{n,m}?	X, at least n but not more than m times

 Possessive quantifiers
 X?+	X, once or not at all
 X*+	X, zero or more times
 X++	X, one or more times
 X{n}+	X, exactly n times
 X{n,}+	X, at least n times
 X{n,m}+	X, at least n but not more than m times

 Logical operators
 XY	X followed by Y
 X|Y	Either X or Y
 (X)	X, as a capturing group
 */

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class RegexUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RegexUtil.class);

    public static final String INTEGER = "^[-]?[0-9]+";

    public static final String DECIMAL = "^[-]?[0-9]+.{0,1}[0-9]*";

    public static final String ALPHABETS = "^[A-Za-z_ ]+";

    public static final String ALPHA_NUMERIC = "^[A-Za-z0-9]+";

    public static final String EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9\\-]+)*(\\.[A-Za-z]{2,})$";

    public static final String CONTACT_NO = "^[\\+|0][0-9\\s-]+";

    public static final String DATE = "(0[1-9]|[1-9]|[12][0-9]|3[01])[-/](0[1-9]|1[012]|[1-9])[-/](19|20)\\d{2}";

    public static final String TIME_AM_PM = "(1[012]|[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";

    public static final String TIME_24HR = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public static final String SQL_DATE = "^([0-9]{2,4})-([0-1][0-9])-([0-3][0-9])";

    public static final String SQL_TIME = "^([0-2][0-9]):([0-5][0-9]):([0-5][0-9])?$";

    public static final String SQL_DATETIME = "^([0-9]{2,4})-([0-1][0-9])-([0-3][0-9])(?:( [0-2][0-9]):([0-5][0-9]):([0-5][0-9]))?$";

    public static final String CNIC = "^[0-9]{5,5}[-.]{0,1}[0-9]{7,7}[-.]{0,1}[0-9]";

    public static final String URL = "^(((ht|f)tp(s?))\\:\\/\\/)?(localhost|([0-9]{1,2}|1[0-9]{2}|2[0-4][0-9]|25[0-5]|.){3}([0-9]{1,2}|1[0-9]{2}|2[0-4][0-9]|25[0-5])|(www.|[a-zA-Z].)[a-zA-Z0-9\\-\\.]+\\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk|pk|co|))(\\:[0-9]+)*(\\/($|[a-zA-Z0-9\\.\\,\\;\\?\\\\'\\\\\\\\\\\\+&amp;%\\\\$#\\\\=~_\\\\-]+))*$";

    public static final String TEXT = "^(a-zA-Z0-9@\\-#\\.\\(\\)\\/%&\\s)";

    public static final String UUID = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})";

    private RegexUtil() {
    }

    /**
     * Checks if given input is a valid number
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isNumeric(String string, boolean floating) {
	try {
	    if (floating)
		return string.matches(DECIMAL);
	    return string.matches(INTEGER);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid word
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isWord(String string) {
	try {
	    return string.matches(ALPHABETS);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid alphanumeric string
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isAlphaNumeric(String string) {
	try {
	    return string.matches(ALPHA_NUMERIC);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid contact number (e.g. Mobile no, fax)
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isContactNumber(String string) {
	try {
	    boolean valid = string.matches(CONTACT_NO);
	    if (string.contains("-")) {
		String[] array = string.split("-");
		for (String temp : array) {
		    try {
			if (Integer.parseInt(temp) == 0) {
			    valid = false;
			    break;
			}
		    } catch (NumberFormatException e) {
			// Do nothing
		    }
		}
	    }
	    return valid;
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid email address
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isEmailAddress(String string) {
	try {
	    return string.matches(EMAIL);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid date
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isValidDate(String string) {
	try {
	    return string.matches(DATE);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid time
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isValidTime(String string, boolean useAmPm) {
	try {
	    if (useAmPm)
		return string.matches(TIME_AM_PM);
	    return string.matches(TIME_24HR);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid national ID number
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isValidNIC(String string) {
	try {
	    return string.matches(CNIC);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is a valid URL
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isValidURL(String string) {
	try {
	    return string.matches(URL);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given input is valid as open-text. This includes all alphanumeric,
     * digits and common special characters
     * 
     * @param string input String
     * @return true/false
     */
    public static boolean isValidOpenText(String string) {
	try {
	    return string.matches(TEXT);
	} catch (Exception e) {
	    LOG.error(e.getMessage());
	    return false;
	}
    }

    /**
     * Checks if given string (string + hyphen + check digit) has a valid Luhn check
     * digit
     * 
     * @param str
     * @return true/false
     */
    public static boolean isValidCheckDigit(String str) {
	boolean isValid = true;
	str = str.replace("-", "");
	String idWithoutCheckdigit = str.substring(0, str.length() - 1);
	char idCheckdigit = str.charAt(str.length() - 1);
	String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVYWXZ_";
	idWithoutCheckdigit = idWithoutCheckdigit.trim();
	int sum = 0;
	for (int i = 0; i < idWithoutCheckdigit.length(); i++) {
	    char ch = idWithoutCheckdigit.charAt(idWithoutCheckdigit.length() - i - 1);
	    if (validChars.indexOf(ch) == -1)
		isValid = false;
	    int digit = ch - 48;
	    int weight;
	    if (i % 2 == 0) {
		weight = (2 * digit) - digit / 5 * 9;
	    } else {
		weight = digit;
	    }
	    sum += weight;
	}
	sum = Math.abs(sum) + 10;
	int checkDigit = (10 - (sum % 10)) % 10;
	isValid = checkDigit == Integer.parseInt(String.valueOf(idCheckdigit));
	return isValid;
    }
}
