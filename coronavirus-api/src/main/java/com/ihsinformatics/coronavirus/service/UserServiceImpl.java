/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.annotation.MeasureProcessingTime;
import com.ihsinformatics.coronavirus.model.Privilege;
import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.model.UserAttributeType;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.SearchCriteria;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class UserServiceImpl extends BaseService implements UserService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#deletePrivilege(com.
     * ihsinformatics.coronavirus.model.Privilege)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Metadata")
    public void deletePrivilege(Privilege obj) throws HibernateException {
	// Check dependencies first
	List<Role> roles = roleRepository.findAll();
	for (Role role : roles) {
	    if (role.getRolePrivileges().contains(obj)) {
		throw new HibernateException(
			"One or more Role objects depend on this Privilege. Please remove this Privilege from all Role objects first.");
	    }
	}
	privilegeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#deleteRole(com.
     * ihsinformatics.coronavirus.model.Role)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Role")
    public void deleteRole(Role obj, boolean force) throws HibernateException {
	// Check dependencies first
	List<User> users = userRepository.findAll();
	for (User user : users) {
	    if (user.getUserRoles().contains(obj)) {
		if (force) {
		    user.getUserRoles().remove(obj);
		    updateUser(user);
		} else {
		    throw new HibernateException(
			    "One or more User objects depend on this Role. Please remove this Role object from User objects (by setting the force parameter true) first.");
		}
	    }
	}
	roleRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#deleteUser(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @CheckPrivilege(privilege = "Delete User")
    public void deleteUser(User obj) throws HibernateException {
	userRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#deleteUserAttribute(com.
     * ihsinformatics.coronavirus.model.UserAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Delete User")
    public void deleteUserAttribute(UserAttribute obj) throws HibernateException {
	userAttributeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#deleteUserAttributeType(
     * com.ihsinformatics.coronavirus.model.UserAttributeType, boolean)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Metadata")
    public void deleteUserAttributeType(UserAttributeType obj, boolean force) throws HibernateException {
	List<UserAttribute> attributesByType = getUserAttributesByType(obj);
	if (!attributesByType.isEmpty()) {
	    if (force) {
		for (UserAttribute userAttribute : attributesByType) {
		    deleteUserAttribute(userAttribute);
		}
	    } else {
		throw new HibernateException(
			"One or more UserAttribute objects depend on this UserAttributeType. Please delete the dependent objects first.");
	    }
	}
	userAttributeTypeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#getPrivileges()
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public List<Privilege> getAllPrivileges() throws HibernateException {
	return privilegeRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#getRoles()
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Role")
    public List<Role> getAllRoles() throws HibernateException {
	List<Role> roles = roleRepository.findAll();
	for (Role role : roles) {
	    if (role.getIsRetired()) {
		roles.remove(role);
	    }
	}
	return roles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributeTypes()
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Metadata")
    public List<UserAttributeType> getAllUserAttributeTypes() throws HibernateException {
	return userAttributeTypeRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#getUsers()
     */
    @Override
    public List<User> getAllUsers() {
	return userRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getPrivilegeByName(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public Privilege getPrivilegeByName(String name) throws HibernateException {
	return privilegeRepository.findByPrivilegeName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getPrivilegeByUuid(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public Privilege getPrivilegeByUuid(String uuid) throws HibernateException {
	return privilegeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getRoleById(java.lang.
     * Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Role")
    public Role getRoleById(Integer id) throws HibernateException {
	Optional<Role> found = roleRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getRoleByName(java.lang.
     * String)
     */
    @Override
    @CheckPrivilege(privilege = "View Role")
    public Role getRoleByName(String name) throws HibernateException {
	return roleRepository.findByRoleName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getRoleByUuid(java.lang.
     * String)
     */
    @Override
    @CheckPrivilege(privilege = "View Role")
    public Role getRoleByUuid(String uuid) throws HibernateException {
	return roleRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getRolesByExample(com.
     * ihsinformatics.coronavirus.model.Role)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View Role")
    public List<Role> getRolesByExample(Role role) throws HibernateException {
	Example<Role> example = Example.of(role);
	return roleRepository.findAll(example);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttribute(com.
     * ihsinformatics.coronavirus.model.User,
     * com.ihsinformatics.coronavirus.model.UserAttributeType)
     */
    @Override
    public List<UserAttribute> getUserAttribute(User user, UserAttributeType attributeType) throws HibernateException {
	return userAttributeRepository.findByUserAndAttributeType(user, attributeType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributeById(
     * java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public UserAttribute getUserAttributeById(Integer id) throws HibernateException {
	Optional<UserAttribute> found = userAttributeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributeByUuid(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public UserAttribute getUserAttributeByUuid(String uuid) throws HibernateException {
	return userAttributeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributesByType(
     * com.ihsinformatics.coronavirus.model.UserAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<UserAttribute> getUserAttributesByType(UserAttributeType attributeType) throws HibernateException {
	return userAttributeRepository.findByAttributeType(attributeType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributesByUser(
     * com.ihsinformatics.coronavirus.model.User)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<UserAttribute> getUserAttributesByUser(User user) throws HibernateException {
	return userAttributeRepository.findByUser(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributesByValue
     * (java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<UserAttribute> getUserAttributesByValue(String value) throws HibernateException {
	return userAttributeRepository.findByValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributesByValue
     * (com.ihsinformatics.coronavirus.model.UserAttributeType, java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<UserAttribute> getUserAttributesByValue(UserAttributeType attributeType, String value)
	    throws HibernateException {
	return userAttributeRepository.findByAttributeTypeAndValue(attributeType, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserAttributeTypeById
     * (java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public UserAttributeType getUserAttributeTypeById(Integer id) throws HibernateException {
	Optional<UserAttributeType> found = userAttributeTypeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#
     * getUserAttributeTypeByName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public UserAttributeType getUserAttributeTypeByName(String name) throws HibernateException {
	return userAttributeTypeRepository.findByAttributeName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#
     * getUserAttributeTypeByShortName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public UserAttributeType getUserAttributeTypeByShortName(String shortName) throws HibernateException {
	return userAttributeTypeRepository.findByShortName(shortName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#
     * getUserAttributeTypeByUuid(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public UserAttributeType getUserAttributeTypeByUuid(String uuid) throws HibernateException {
	return userAttributeTypeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserById(java.lang.
     * Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public User getUserById(Integer id) throws HibernateException {
	Optional<User> found = userRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserByUsername(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public User getUserByUsername(String username) throws HibernateException {
	return userRepository.findByUsername(username);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUserByUuid(java.lang.
     * String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public User getUserByUuid(String uuid) throws HibernateException {
	return userRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUsersByExample(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View User")
    public List<User> getUsersByExample(User user) throws HibernateException {
	Example<User> example = Example.of(user);
	return userRepository.findAll(example);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#getUsersByFullName(java.
     * lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<User> getUsersByFullName(String name) throws HibernateException {
	return userRepository.findByFullName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#getUsersByRole(com.
     * ihsinformatics.coronavirus.model.Role)
     */
    @Override
    @CheckPrivilege(privilege = "View User")
    public List<User> getUsersByRole(Role role) throws HibernateException {
	return userRepository.findUsersByUserRolesRoleId(role.getRoleId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#savePrivilege(com.
     * ihsinformatics.coronavirus.model.Privilege)
     */
    @Override
    @CheckPrivilege(privilege = "Add Metadata")
    public Privilege savePrivilege(Privilege obj) throws HibernateException {
	if (getPrivilegeByName(obj.getPrivilegeName()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate Privilege!");
	}
	return privilegeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#saveRole(com.
     * ihsinformatics.coronavirus.model.Role)
     */
    @Override
    @CheckPrivilege(privilege = "Add Role")
    public Role saveRole(Role obj) throws HibernateException {
	if (getRoleByName(obj.getRoleName()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate Role!");
	}
	return roleRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#saveUser(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add User")
    public User saveUser(User obj) throws HibernateException {
	if (getUserByUsername(obj.getUsername()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate User!");
	}
	obj = (User) setCreateAuditAttributes(obj);
	return userRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#saveUserAttribute(com.
     * ihsinformatics.coronavirus.model.UserAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Add User")
    public UserAttribute saveUserAttribute(UserAttribute obj) throws HibernateException {
	obj = (UserAttribute) setCreateAuditAttributes(obj);
	return userAttributeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#saveUserAttributes(java.
     * util.List)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Add User")
    public List<UserAttribute> saveUserAttributes(List<UserAttribute> attributes) {
	for (UserAttribute attribute : attributes) {
	    attribute = (UserAttribute) setCreateAuditAttributes(attribute);
	}
	return userAttributeRepository.saveAll(attributes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#saveUserAttributeType(
     * com.ihsinformatics.coronavirus.model.UserAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "Add Metadata")
    public UserAttributeType saveUserAttributeType(UserAttributeType obj) throws HibernateException {
	if (getUserAttributeTypeByName(obj.getAttributeName()) != null) {
	    throw new HibernateException("Make sure you are not trying to save duplicate UserAttributeType!");
	}
	obj = (UserAttributeType) setCreateAuditAttributes(obj);
	return userAttributeTypeRepository.save(obj);
    }

    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "View User")
    public List<User> searchUsers(List<SearchCriteria> params) throws HibernateException {
	if (params == null) {
	    params = new ArrayList<>();
	}
	if (params.isEmpty()) {
	    return new ArrayList<>();
	}
	return userRepository.search(params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#updatePrivilege(com.
     * ihsinformatics.coronavirus.model.Privilege)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public Privilege updatePrivilege(Privilege obj) throws HibernateException {
	return privilegeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#updateRole(com.
     * ihsinformatics.coronavirus.model.Role)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Role")
    public Role updateRole(Role obj) throws HibernateException {
	obj = (Role) setUpdateAuditAttributes(obj);
	return roleRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.UserService#updateUser(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @MeasureProcessingTime
    @CheckPrivilege(privilege = "Edit User")
    public User updateUser(User obj) throws HibernateException {
	obj = (User) setUpdateAuditAttributes(obj);
	
	if(obj.getUserId() != null){
		if(obj.getUserId().equals(obj.getUpdatedBy().getUserId()))
			obj.setUpdatedBy(null);
		
		if(obj.getVoidedBy() != null){
			if(obj.getUserId().equals(obj.getVoidedBy().getUserId()))
				obj.setVoidedBy(null);
		}
	}
	
	return userRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#updateUserAttribute(com.
     * ihsinformatics.coronavirus.model.UserAttribute)
     */
    @Override
    @CheckPrivilege(privilege = "Edit User")
    public UserAttribute updateUserAttribute(UserAttribute obj) throws HibernateException {
	obj = (UserAttribute) setUpdateAuditAttributes(obj);
	return userAttributeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.UserService#updateUserAttributeType(
     * com.ihsinformatics.coronavirus.model.UserAttributeType)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Metadata")
    public UserAttributeType updateUserAttributeType(UserAttributeType obj) throws HibernateException {
	obj = (UserAttributeType) setUpdateAuditAttributes(obj);
	return userAttributeTypeRepository.save(obj);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#voidUser(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @CheckPrivilege(privilege = "Void User")
    @Transactional
    public void voidUser(User obj) throws HibernateException {
	obj = (User) setSoftDeleteAuditAttributes(obj);
	obj.setIsVoided(Boolean.TRUE);
	
	for (UserAttribute attribute : obj.getAttributes()) {
		if(Boolean.FALSE.equals(attribute.getIsVoided())){
			attribute.setIsVoided(Boolean.TRUE);
			attribute.setVoidedBy(obj.getVoidedBy());
			attribute.setDateVoided(obj.getDateVoided());
			attribute.setReasonVoided(obj.getReasonVoided() + " (User voided)");
			userAttributeRepository.softDelete(attribute);
		}
	}
	
	userRepository.softDelete(obj);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.FormService#unvoidUser(com.
     * ihsinformatics.coronavirus.model.User)
     */
    @Override
    @CheckPrivilege(privilege = "Void User")
    @Transactional
    public User unvoidUser(User obj) throws HibernateException, ValidationException, IOException {
	if (obj.getIsVoided()) {
	    obj.setIsVoided(Boolean.FALSE);
	    if (obj.getReasonVoided() == null) {
		obj.setReasonVoided("");
	    }
	    String voidedReason = obj.getReasonVoided();
	    obj.setReasonVoided(obj.getReasonVoided() + "(Unvoided on "
		    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
	    
	    for (UserAttribute attribute : obj.getAttributes()) {
			if(Boolean.TRUE.equals(attribute.getIsVoided()) && attribute.getReasonVoided().equals(voidedReason + " (User voided)")){
				attribute.setIsVoided(Boolean.FALSE);
				if (attribute.getReasonVoided() == null) {
					attribute.setReasonVoided("");
				 }
				attribute.setReasonVoided(attribute.getReasonVoided() + "(User unvoided on "
					    + DateTimeUtil.toSqlDateTimeString(new Date()) + ")");
				updateUserAttribute(attribute);
			}
		}
	    
	    return updateUser(obj);
	}
	return obj;
    }
}
