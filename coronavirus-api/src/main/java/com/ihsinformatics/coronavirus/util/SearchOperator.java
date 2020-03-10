/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

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
 */
public enum SearchOperator {
    EQUALS(new String[] { "=" }), GREATER_THAN(new String[] { ">", "gt" }),
    GREATER_THAN_EQUALS(new String[] { ">=", "gte" }), LESS_THAN(new String[] { "<", "lt" }),
    LESS_THAN_EQUALS(new String[] { "<=", "lte" }), LIKE(new String[] { ":", "%", "lk", "like" }),
    NOT_EQUALS(new String[] { "<>", "!=", "ne", "neq" }), NOT_LIKE(new String[] { "!%", "nlk", "unlike" }),
    UNKNOWN(new String[] { "" });

    private String[] aliases;

    private SearchOperator(String[] aliases) {
	this.aliases = aliases;
    }

    /**
     * Return all listed aliases against a SearchOperator. For example, aliases for
     * GREATER_THAN can be 'gt' and '>'
     * 
     * @return
     */
    public String[] getAliases() {
	return aliases;
    }

    /**
     * Searches for all aliases of the SearchOperator and returns true if any one
     * matches
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
     * Searches all SearchOperator enums for the given alias and returns the
     * matching SearchOperator. If no results are found, UNKNOWN type is returned.
     * 
     * @param alias
     * @return
     */
    public static SearchOperator getSearchOperatorByAlias(String alias) {
	for (SearchOperator operator : SearchOperator.values()) {
	    for (String a : operator.aliases) {
		if (a.equalsIgnoreCase(alias)) {
		    return operator;
		}
	    }
	}
	return UNKNOWN;
    }

    /**
     * Return default representation of data type
     */
    @Override
    public String toString() {
	return this.aliases[0];
    }
}
