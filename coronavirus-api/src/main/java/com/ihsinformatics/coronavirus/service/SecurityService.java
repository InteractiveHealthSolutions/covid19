/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import com.ihsinformatics.coronavirus.model.User;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface SecurityService {

    /**
     * Returns the {@link User} object for audit. If a user is not logged in, then
     * admin {@link User} is returned
     * 
     * @return
     */
    User getAuditUser();

    /**
     * Finds and returns the {@link User} currently logged in
     * 
     * @return
     */
    User getLoggedInUser();

    /**
     * Returns true if given {@link User} is an Administrator
     * 
     * @param baseService
     * @param user
     * @return
     */
    boolean hasAdminRole(User user);

    /**
     * @see hasPrivilege(java.lang.String)
     * @param privilege
     * @return
     */
    boolean hasPrivilege(String privilege);

    /**
     * Returns true if given {@link User} has the privilege
     * 
     * @param user
     * @param privilege
     * @return
     */
    boolean hasPrivilege(User user, String privilege);

    /**
     * Authenticate user
     * 
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    boolean login(String username, String password) throws SecurityException;

    /**
     * Logout
     */
    void logout();
}
