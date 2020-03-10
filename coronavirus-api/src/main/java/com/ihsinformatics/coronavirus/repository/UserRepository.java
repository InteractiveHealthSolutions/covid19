/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ihsinformatics.coronavirus.model.User;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface UserRepository extends CustomUserRepository, JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.fullName LIKE CONCAT('%', :fullName, '%')")
    List<User> findByFullName(@Param("fullName") String fullName);

    User findByUsername(String username);

    User findByUuid(String uuid);

    List<User> findUsersByUserRolesRoleId(Integer roleId);
    
    @Query("UPDATE User d set d.isVoided = true WHERE d = :user")
    @Modifying
    void softDelete(@Param("user") User user);
}
