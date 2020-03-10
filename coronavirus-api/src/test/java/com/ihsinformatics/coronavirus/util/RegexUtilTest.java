/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Owais
 */
package com.ihsinformatics.coronavirus.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ihsinformatics.coronavirus.util.RegexUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class RegexUtilTest {

    @Test
    public final void testIsAlphaNumeric() {
    }

    @Test
    public final void testIsContactNumber() {
	String[] right = { "03452345345", "+923452345345", "0345-2345345", "0345 2345345", "+92-345-2345345" };
	String[] wrong = { "0092-00-356-9887", "00-3452345345", "+92055alpha55", "99#255689", "*55554444889" };
	for (String num : right) {
	    assertTrue(num + " is valid but rejected", RegexUtil.isContactNumber(num));
	}
	for (String num : wrong) {
	    assertFalse(num + " is invalid but accepted", RegexUtil.isContactNumber(num));
	}
    }

    @Test
    public final void testIsEmailAddress() {
	String[] right = { "owaishussain@outlook.com", "ali.habib@ghd.ihn.org.pk", "_______@gmail.com" };
	String[] wrong = { "plainaddress", "@domain.com", "#@%^%#$@#$@#.com", "email@111.222.333.44444",
		".email@domain.com" };
	for (String str : right) {
	    assertTrue(str + " is valid but rejected", RegexUtil.isEmailAddress(str));
	}
	for (String str : wrong) {
	    assertFalse(str + " is invalid but accepted", RegexUtil.isEmailAddress(str));
	}
    }

    @Test
    public final void testIsNumeric() {
	String[] validIntegers = { "0", "1", "9999", "-9999" };
	String[] invalidIntegers = { "1.5", "2e14" };
	String[] validFloat = { "0", "1", "9999", "-9999", "0.0", "1.1", "99.99", "-99.99" };
	String[] invalidFloat = { "1..5", ".55" };
	for (String s : validIntegers) {
	    assertTrue(s + " is valid but rejected", RegexUtil.isNumeric(s, false));
	}
	for (String s : invalidIntegers) {
	    assertFalse(s + " is invalid but accepted", RegexUtil.isNumeric(s, false));
	}
	for (String s : validFloat) {
	    assertTrue(s + " is valid but rejected", RegexUtil.isNumeric(s, true));
	}
	for (String s : invalidFloat) {
	    assertFalse(s + " is invalid but accepted", RegexUtil.isNumeric(s, true));
	}
    }

    @Test
    public final void testIsValidCheckDigit() {
	assertTrue(RegexUtil.isValidCheckDigit("0000-0"));
	assertTrue(RegexUtil.isValidCheckDigit("1234567890-3"));
	assertFalse(RegexUtil.isValidCheckDigit("987654-5"));
    }

    @Test
    public final void testIsValidDate() {
	assertTrue(RegexUtil.isValidDate("09-08-2019"));
	assertTrue(RegexUtil.isValidDate("09/08/2019"));
	assertFalse(RegexUtil.isValidDate("9819"));
    }

    @Test
    public final void testIsValidNIC() {
	assertTrue(RegexUtil.isValidNIC("42101-1234567-8"));
	assertTrue(RegexUtil.isValidNIC("4210112345678"));
	assertFalse(RegexUtil.isValidNIC("1234567"));
    }

    @Test
    public final void testIsValidTime() {
	assertTrue(RegexUtil.isValidTime("3:30 pm", true));
	assertTrue(RegexUtil.isValidTime("15:30", false));
	assertFalse(RegexUtil.isValidTime("59:61", false));
    }

    @Test
    public final void testIsValidURL() {
	assertTrue(RegexUtil.isValidURL("https://web.whatsapp.com/"));
	assertTrue(RegexUtil.isValidURL("https://pastebin.com/CDZiDVDR"));
	assertTrue(RegexUtil.isValidURL("www.outlook.live.com/mail/inbox/id/ADLTFjMGQtMDACLTAwCg"));
	assertTrue(RegexUtil.isValidURL("http://localhost:8080/swagger-ui.html"));
    }

    @Test
    public final void testIsWord() {
	String[] right = { "Owais", "A_Hussain", " " };
	String[] wrong = { "1234", "Owais+Ahmed", "Ow@is", "" };
	for (String str : right) {
	    assertTrue(str + " is valid but rejected", RegexUtil.isWord(str));
	}
	for (String str : wrong) {
	    assertFalse(str + " is invalid but accepted", RegexUtil.isWord(str));
	}
    }
}
