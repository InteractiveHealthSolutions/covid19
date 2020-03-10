/* Copyright(C) 2018 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.util;

/**
 * @author owais.hussain@ihsinformatics.com
 *
 */
public enum Gender {
    MALE('M', new String[] { "male", "man", "masculine" }), FEMALE('F', new String[] { "female", "woman", "feminine" }),
    TRANSGENDER('T',
	    new String[] { "transgender", "transsexual", "male transgender", "female transgender", "transgender male",
		    "transgender female" }),
    BIGENDER('B', new String[] { "bigender" }), NONCONFORMING('N', new String[] { "nonconforming" }),
    MALE_TO_FEMALE('X', new String[] { "male to female" }), FEMALE_TO_MALE('Y', new String[] { "female to male" }),
    PANGENDER('P', new String[] { "pangender" }), OTHER('O', new String[] {});

    private Character symbol;
    private String[] aliases;

    private Gender() {
    }

    private Gender(Character symbol, String[] aliases) {
	this.symbol = symbol;
	this.aliases = aliases;
    }

    /**
     * Searches for all aliases of the Gender and returns true if any one matches
     * 
     * @param alias
     * @return
     */
    public boolean match(String alias) {
	for (String a : aliases) {
	    if (a.equalsIgnoreCase(alias)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Searches all Gender enums for the given alias and returns the matching
     * Gender. If no results are found, OTHER type is returned.
     * 
     * @param alias
     * @return
     */
    public static Gender getDataTypeByAlias(String alias) {
	for (Gender g : Gender.values()) {
	    for (String a : g.aliases) {
		if (a.equalsIgnoreCase(alias)) {
		    return g;
		}
	    }
	}
	return OTHER;
    }

    /**
     * @return the symbol
     */
    public Character getSymbol() {
	return symbol;
    }

    /**
     * @return aliases
     */
    public String[] getAliases() {
	return aliases;
    }

    @Override
    public String toString() {
	return String.valueOf(getSymbol());
    }
}
