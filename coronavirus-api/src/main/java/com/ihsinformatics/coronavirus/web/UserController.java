/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.dto.UserDto;
import com.ihsinformatics.coronavirus.model.Privilege;
import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.model.UserAttributeType;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author owais.hussain@ihsinformatics.com
 */

@RestController
@RequestMapping("/api")
@Api(value = "User Controller")
public class UserController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService service;

    @ApiOperation(value = "Create a new Privilege")
    @PostMapping("/privilege")
    public ResponseEntity<?> createPrivilege(@Valid @RequestBody Privilege obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create donor: {}", obj);
	try {
	    Privilege result = service.savePrivilege(obj);
	    return ResponseEntity.created(new URI("/api/privilege/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create New Role")
    @PostMapping("/role")
    public ResponseEntity<?> createRole(@RequestBody Role obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create role: {}", obj);
	try {
	    Role result = service.saveRole(obj);
	    return ResponseEntity.created(new URI("/api/role/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create new User")
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create user: {}", obj);
	try {
	    User result = service.saveUser(obj);
	    return ResponseEntity.created(new URI("/api/user/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create New UserAttribute")
    @PostMapping("/userattribute")
    public ResponseEntity<?> createUserAttribute(@RequestBody UserAttribute obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create user attribute: {}", obj);
	try {
	    UserAttribute result = service.saveUserAttribute(obj);
	    return ResponseEntity.created(new URI("/api/userattribute/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create New UserAttributeType")
    @PostMapping("/userattributetype")
    public ResponseEntity<?> createUserAttributeType(@RequestBody UserAttributeType obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create user attribute type: {}", obj);
	try {
	    UserAttributeType result = service.saveUserAttributeType(obj);
	    return ResponseEntity.created(new URI("/api/userattributetype/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Delete a Role")
    @DeleteMapping("/privilege/{uuid}")
    public ResponseEntity<?> deletePrivilege(@PathVariable String uuid) {
	return notImplementedResponse(Privilege.class.getName());
    }

    @ApiOperation(value = "Delete a Role")
    @DeleteMapping("/role/{uuid}")
    public ResponseEntity<?> deleteRole(@PathVariable String uuid) {
	LOG.info("Request to delete role: {}", uuid);
	try {
	    service.deleteRole(service.getRoleByUuid(uuid), false);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Void User")
    @DeleteMapping("/user/{uuid}")
    public ResponseEntity<?> voidUser(@PathVariable String uuid, @RequestParam("reasonVoided")String reasonVoided) {
	LOG.info("Request to delete User: {}", uuid);
	try {
		User user = uuid.matches(RegexUtil.UUID) ? service.getUserByUuid(uuid)
				: service.getUserById(Integer.parseInt(uuid));
		if(user == null)
			return noEntityFoundResponse(uuid);
		user.setReasonVoided(reasonVoided);
	    service.voidUser(user);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.ok().body("SUCCESS");
    }
    
    @ApiOperation(value = "Restore User")
    @PatchMapping("/user/{uuid}")
    public ResponseEntity<?> unvoidUser(@PathVariable String uuid) {
	LOG.info("Request to restore user: {}", uuid);
	try {
		User user = uuid.matches(RegexUtil.UUID) ? service.getUserByUuid(uuid)
				: service.getUserById(Integer.parseInt(uuid));
		if(user == null)
			return noEntityFoundResponse(uuid);
	    User obj = service.unvoidUser(user);
	    return ResponseEntity.ok().body(obj);

	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
    }

    @ApiOperation(value = "Delete a UserAttribute")
    @DeleteMapping("/userattribute/{uuid}")
    public ResponseEntity<?> deleteUserAttribute(@PathVariable String uuid) {
	LOG.info("Request to delete user attribute: {}", uuid);
	try {
	    service.deleteUserAttribute(service.getUserAttributeByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete a UserAttribute")
    @DeleteMapping("/userattributetype/{uuid}")
    public ResponseEntity<?> deleteUserAttributeType(@PathVariable String uuid) {
	return notImplementedResponse(User.class.getName());
    }

    @ApiOperation(value = "Get list of all Users (lightweight objects)")
    @GetMapping("/user/list")
    public List<UserDto> getUserList() {
	List<User> list = service.getAllUsers();
	List<UserDto> users = new ArrayList<>();
	for (User user : list) {
	    users.add(new UserDto(user));
	}
	return users;
    }

    @ApiOperation(value = "Get Privilege by UUID")
    @GetMapping("/privilege/{uuid}")
    public ResponseEntity<?> getPrivilege(@PathVariable String uuid) {
	Privilege obj = service.getPrivilegeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Privilege by name")
    @GetMapping("/privilege/name/{name}")
    public ResponseEntity<?> getPrivilegeByName(@PathVariable String name) {
	Privilege obj = service.getPrivilegeByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get all Privileges")
    @GetMapping("/privileges")
    public Collection<Privilege> getPrivileges() {
	return service.getAllPrivileges();
    }

    @ApiOperation(value = "Get Role by UUID")
    @GetMapping("/role/{uuid}")
    public ResponseEntity<?> getRole(@PathVariable String uuid) {
	Role obj = service.getRoleByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Role by ID")
    @GetMapping("/role/id/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Integer id) {
	Role obj = service.getRoleById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Role by name")
    @GetMapping("/role/name/{name}")
    public ResponseEntity<?> getRoleByName(@PathVariable String name) {
	Role obj = service.getRoleByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get all Roles")
    @GetMapping("/roles")
    public Collection<?> getRoles() {
	return service.getAllRoles();
    }

    @ApiOperation(value = "Get User by UUID")
    @GetMapping("/user/{uuid}")
    public ResponseEntity<?> getUser(@PathVariable String uuid) {
	User obj = service.getUserByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get UserAttribute by UUID")
    @GetMapping("/userattribute/{uuid}")
    public ResponseEntity<?> getUserAttribute(@PathVariable String uuid) {
	UserAttribute obj = service.getUserAttributeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get UserAttribute by ID")
    @GetMapping("/userattribute/id/{id}")
    public ResponseEntity<?> getUserAttributeById(@PathVariable Integer id) {
	UserAttribute obj = service.getUserAttributeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get UserAttributes by Location")
    @GetMapping("/userattributes/user/{uuid}")
    public ResponseEntity<?> getUserAttributesByUser(@PathVariable String uuid) {
	User user = uuid.matches(RegexUtil.UUID) ? service.getUserByUuid(uuid) : service.getUserByUsername(uuid);
	List<UserAttribute> list = service.getUserAttributesByUser(user);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get UserAttributeType By UUID")
    @GetMapping("/userattributetype/{uuid}")
    public ResponseEntity<?> getUserAttributeType(@PathVariable String uuid) {
	UserAttributeType obj = service.getUserAttributeTypeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get UserAttributeType by ID")
    @GetMapping("/userattributetype/id/{id}")
    public ResponseEntity<?> getUserAttributeTypeById(@PathVariable Integer id) {
	UserAttributeType obj = service.getUserAttributeTypeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get UserAttributeType by name")
    @GetMapping("/userattributetype/name/{name}")
    public ResponseEntity<?> getUserAttributeTypeByName(@PathVariable String name) {
	UserAttributeType obj = service.getUserAttributeTypeByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get UserAttributeType by short name")
    @GetMapping("/userattributetype/shortname/{shortName}")
    public ResponseEntity<?> getUserAttributeTypeByShortName(@PathVariable String shortName) {
	UserAttributeType obj = service.getUserAttributeTypeByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all UserAttributeTypes")
    @GetMapping("/userattributetypes")
    public Collection<?> getUserAttributeTypes() {
	return service.getAllUserAttributeTypes();
    }

    @ApiOperation(value = "Get User by ID")
    @GetMapping("/user/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
	User obj = service.getUserById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get User by User name")
    @GetMapping("/user/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
	User obj = service.getUserByUsername(username);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(username);
    }

    @ApiOperation(value = "Get all Users")
    @GetMapping("/users")
    public Collection<?> getUsers() {
	return service.getAllUsers();
    }

    @ApiOperation(value = "Get Users by name")
    @GetMapping("/users/name/{name}")
    public ResponseEntity<?> getUsersByName(@PathVariable String name) {
	List<User> list = service.getUsersByFullName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get Users by Role")
    @GetMapping("/users/role/{uuid}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String uuid) {
	Role role = uuid.matches(RegexUtil.UUID) ? service.getRoleByUuid(uuid) : service.getRoleByName(uuid);
	List<User> list = service.getUsersByRole(role);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Update an existing privilege")
    @PutMapping("/privilege/{uuid}")
    public ResponseEntity<?> updatePrivilege(@PathVariable String uuid, @Valid @RequestBody Privilege obj) {
	return notImplementedResponse(Privilege.class.getName());
    }

    @ApiOperation(value = "Update existing Role")
    @PutMapping("/role/{uuid}")
    public ResponseEntity<?> updateRole(@PathVariable String uuid, @Valid @RequestBody Role obj) {
	Role found = service.getRoleByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setRoleId(found.getRoleId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update role: {}", obj);
	return ResponseEntity.ok().body(service.updateRole(obj));
    }

    @ApiOperation(value = "Update existing User")
    @PutMapping("/user/{uuid}")
    public ResponseEntity<?> updateUser(@PathVariable String uuid, @Valid @RequestBody User obj) {
	User found = service.getUserByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setUserId(found.getUserId());
	obj.setUuid(found.getUuid());
	
	if(obj.getPasswordHash() == null)
		obj.setPasswordHash(found.getPasswordHash());
		
	LOG.info("Request to update user: {}", obj);
	return ResponseEntity.ok().body(service.updateUser(obj));
    }

    @ApiOperation(value = "Update existing UserAttribute")
    @PutMapping("/userattribute/{uuid}")
    public ResponseEntity<?> updateUserAttribute(@PathVariable String uuid, @Valid @RequestBody UserAttribute obj) {
	UserAttribute found = service.getUserAttributeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeId(found.getAttributeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update user attribute: {}", obj);
	return ResponseEntity.ok().body(service.updateUserAttribute(obj));
    }

    @ApiOperation(value = "Update existing UserAttributeType")
    @PutMapping("/userattributetype/{uuid}")
    public ResponseEntity<?> updateUserAttributeType(@PathVariable String uuid,
	    @Valid @RequestBody UserAttributeType obj) {
	UserAttributeType found = service.getUserAttributeTypeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeTypeId(found.getAttributeTypeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update user attribute type: {}", obj);
	return ResponseEntity.ok().body(service.updateUserAttributeType(obj));
    }
}
