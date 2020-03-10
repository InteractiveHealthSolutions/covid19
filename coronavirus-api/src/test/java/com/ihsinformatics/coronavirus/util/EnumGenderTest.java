/**
 * 
 */
package com.ihsinformatics.coronavirus.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.ihsinformatics.coronavirus.util.Gender;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public class EnumGenderTest {

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.Gender#getAliases()}.
     */
    @Test
    public void testGetAliases() {
	String[] aliases = Gender.FEMALE.getAliases();
	String[] expected = { "female", "woman", "feminine" };
	assertTrue(Arrays.equals(expected, aliases));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.Gender#getDataTypeByAlias(java.lang.String)}.
     */
    @Test
    public void testGetDataTypeByAlias() {
	assertEquals(Gender.TRANSGENDER, Gender.getDataTypeByAlias("transgender"));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.Gender#getSymbol()}.
     */
    @Test
    public void testGetSymbol() {
	assertEquals(new Character('F'), Gender.FEMALE.getSymbol());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.Gender#match(java.lang.String)}.
     */
    @Test
    public void testMatch() {
	assertTrue(Gender.MALE.match("man"));
	assertTrue(Gender.MALE.match("MASCULINE"));
	assertFalse(Gender.FEMALE.match("man"));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.util.Gender#toString()}.
     */
    @Test
    public void testToString() {
	assertEquals("M", Gender.MALE.toString());
    }
}
