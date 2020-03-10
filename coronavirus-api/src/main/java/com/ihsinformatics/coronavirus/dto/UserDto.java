/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import java.util.ArrayList;
import java.util.List;

import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.service.UserService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
public class UserDto {

    private Integer userId;

    private String username;

    private String fullName;

    private String uuid;

    private List<String> attributeUuids = new ArrayList<>();

    private List<String> userRoleUuids = new ArrayList<>();

    public UserDto(Integer userId, String username, String fullName, String uuid, List<String> attributeUuids,
	    List<String> userRoleUuids) {
	super();
	this.userId = userId;
	this.username = username;
	this.fullName = fullName;
	this.uuid = uuid;
	this.attributeUuids = attributeUuids;
	this.userRoleUuids = userRoleUuids;
    }

    public UserDto(User user) {
	this.userId = user.getUserId();
	this.username = user.getUsername();
	this.fullName = user.getFullName();
	this.uuid = user.getUuid();
	for (UserAttribute attribute : user.getAttributes()) {
	    this.attributeUuids.add(attribute.getUuid());
	}
	for (Role role : user.getUserRoles()) {
	    this.attributeUuids.add(role.getUuid());
	}
    }

    public User toUser(UserService userService) {
	List<UserAttribute> attributes = new ArrayList<>();
	for (String attributeUuid : attributeUuids) {
	    attributes.add(userService.getUserAttributeByUuid(attributeUuid));
	}
	List<Role> userRoles = new ArrayList<>();
	for (String roleUuid : userRoleUuids) {
	    userRoles.add(userService.getRoleByUuid(roleUuid));
	}
	User user = User.builder().userId(userId).username(username).fullName(fullName).attributes(attributes)
		.userRoles(userRoles).build();
	user.setUuid(uuid);
	return user;
    }
}
