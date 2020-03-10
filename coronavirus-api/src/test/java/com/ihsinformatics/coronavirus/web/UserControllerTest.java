/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.dto.UserDto;
import com.ihsinformatics.coronavirus.model.BaseEntity;
import com.ihsinformatics.coronavirus.model.Privilege;
import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.model.UserAttribute;
import com.ihsinformatics.coronavirus.model.UserAttributeType;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.web.UserController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest extends BaseTestData {

    private static String API_PREFIX = "/api/";

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void reset() {
	super.initData();
	MockitoAnnotations.initMocks(this);
	mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(MockMvcResultHandlers.print()).build();
    }

    @Test
    public void shouldCreatePrivilege() throws Exception {
	when(userService.savePrivilege(any(Privilege.class))).thenReturn(curse);
	String content = BaseEntity.getGson().toJson(curse);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "privilege")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "privilege/" + curse.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(userService, times(1)).savePrivilege(any(Privilege.class));
    }

    @Test
    public void shouldCreateRole() throws Exception {
	when(userService.saveRole(any(Role.class))).thenReturn(headmaster);
	String content = BaseEntity.getGson().toJson(headmaster);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "role")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "role/" + headmaster.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(userService, times(1)).saveRole(any(Role.class));
    }

    @Test
    public void shouldCreateUser() throws Exception {
	when(userService.saveUser(any(User.class))).thenReturn(snape);
	String content = BaseEntity.getGson().toJson(snape);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "user")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "user/" + snape.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    public void shouldCreateUserAttribute() throws Exception {
	when(userService.saveUserAttribute(any(UserAttribute.class))).thenReturn(snapeBlood);
	String content = BaseEntity.getGson().toJson(snapeBlood);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "userattribute")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "userattribute/" + snapeBlood.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(userService, times(1)).saveUserAttribute(any(UserAttribute.class));
    }

    @Test
    public void shouldCreateUserAttributeType() throws Exception {
	when(userService.saveUserAttributeType(any(UserAttributeType.class))).thenReturn(blood);
	String content = BaseEntity.getGson().toJson(blood);
	RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API_PREFIX + "userattributetype")
		.accept(MediaType.APPLICATION_JSON_UTF8).contentType(MediaType.APPLICATION_JSON_UTF8).content(content);
	ResultActions actions = mockMvc.perform(requestBuilder);
	actions.andExpect(status().isCreated());
	String expectedUrl = API_PREFIX + "userattributetype/" + blood.getUuid();
	actions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedUrl));
	verify(userService, times(1)).saveUserAttributeType(any(UserAttributeType.class));
    }

    @Test
    public void shouldDeleteRole() throws Exception {
	when(userService.getRoleByUuid(any(String.class))).thenReturn(headmaster);
	doNothing().when(userService).deleteRole(headmaster, false);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "role/{uuid}", headmaster.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(userService, times(1)).getRoleByUuid(headmaster.getUuid());
	verify(userService, times(1)).deleteRole(headmaster, false);
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldDeleteUser() throws Exception {
	when(userService.getUserByUuid(any(String.class))).thenReturn(dumbledore);
	doNothing().when(userService).voidUser(dumbledore);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "user/{uuid}?reasonVoided=Test123", dumbledore.getUuid()));
	verify(userService, times(1)).getUserByUuid(dumbledore.getUuid());
	verify(userService, times(1)).voidUser(dumbledore);
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldDeleteUserAttribute() throws Exception {
	when(userService.getUserAttributeByUuid(any(String.class))).thenReturn(snapeBlood);
	doNothing().when(userService).deleteUserAttribute(snapeBlood);
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "userattribute/{uuid}", snapeBlood.getUuid()));
	actions.andExpect(status().isNoContent());
	verify(userService, times(1)).getUserAttributeByUuid(snapeBlood.getUuid());
	verify(userService, times(1)).deleteUserAttribute(snapeBlood);
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldDeleteUserAttributeType() throws Exception {
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "userattributetype/{uuid}", blood.getUuid()));
	actions.andExpect(status().isNotImplemented());
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetPrivilege() throws Exception {
	when(userService.getPrivilegeByUuid(any(String.class))).thenReturn(curse);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "privilege/{uuid}", curse.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.privilegeName", Matchers.is(curse.getPrivilegeName())));
	verify(userService, times(1)).getPrivilegeByUuid(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetPrivilegeByName() throws Exception {
	when(userService.getPrivilegeByName(any(String.class))).thenReturn(curse);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "privilege/name/{name}", curse.getPrivilegeName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.privilegeName", Matchers.is(curse.getPrivilegeName())));
	verify(userService, times(1)).getPrivilegeByName(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetPrivileges() throws Exception {
	when(userService.getAllPrivileges()).thenReturn(Arrays.asList(curse, charm, magic));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "privileges"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
	verify(userService, times(1)).getAllPrivileges();
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetRole() throws Exception {
	when(userService.getRoleByUuid(any(String.class))).thenReturn(headmaster);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "role/{uuid}", headmaster.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.uuid", Matchers.is(headmaster.getUuid())));
	verify(userService, times(1)).getRoleByUuid(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetRoleById() throws Exception {
	when(userService.getRoleById(any(Integer.class))).thenReturn(headmaster);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "role/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.uuid", Matchers.is(headmaster.getUuid())));
	verify(userService, times(1)).getRoleById(any(Integer.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetRoleByName() throws Exception {
	when(userService.getRoleByName(any(String.class))).thenReturn(headmaster);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "role/name/{uuid}", headmaster.getRoleName()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.uuid", Matchers.is(headmaster.getUuid())));
	verify(userService, times(1)).getRoleByName(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetRoles() throws Exception {
	when(userService.getAllRoles()).thenReturn(Arrays.asList(headmaster));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "roles"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(userService, times(1)).getAllRoles();
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUser() throws Exception {
	when(userService.getUserByUuid(any(String.class))).thenReturn(dumbledore);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "user/{uuid}", dumbledore.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.username", Matchers.is(dumbledore.getUsername())));
	actions.andExpect(jsonPath("$.fullName", Matchers.is(dumbledore.getFullName())));
	verify(userService, times(1)).getUserByUuid(any(String.class));
    }

    @Test
    public void shouldGetUserAttribute() throws Exception {
	when(userService.getUserAttributeByUuid(any(String.class))).thenReturn(snapeBlood);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattribute/{uuid}", snapeBlood.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.attributeValue", Matchers.is(snapeBlood.getAttributeValue())));
	verify(userService, times(1)).getUserAttributeByUuid(any(String.class));
    }

    @Test
    public void shouldGetUserAttributeById() throws Exception {
	when(userService.getUserAttributeById(any(Integer.class))).thenReturn(snapeBlood);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattribute/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.attributeValue", Matchers.is(snapeBlood.getAttributeValue())));
	verify(userService, times(1)).getUserAttributeById(any(Integer.class));
    }

    @Test
    public void shouldGetUserAttributesByUser() throws Exception {
	when(userService.getUserByUuid(any(String.class))).thenReturn(snape);
	when(userService.getUserAttributesByUser(any(User.class))).thenReturn(Arrays.asList(snapeBlood));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattributes/user/{uuid}", snape.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
	verify(userService, times(1)).getUserByUuid(any(String.class));
	verify(userService, times(1)).getUserAttributesByUser(any(User.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUserAttributeType() throws Exception {
	when(userService.getUserAttributeTypeByUuid(any(String.class))).thenReturn(blood);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattributetype/{uuid}", blood.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(blood.getShortName())));
	verify(userService, times(1)).getUserAttributeTypeByUuid(any(String.class));
    }

    @Test
    public void shouldGetUserAttributeTypeById() throws Exception {
	when(userService.getUserAttributeTypeById(any(Integer.class))).thenReturn(blood);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattributetype/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(blood.getShortName())));
	verify(userService, times(1)).getUserAttributeTypeById(any(Integer.class));
    }

    @Test
    public void shouldGetUserAttributeTypeByName() throws Exception {
	when(userService.getUserAttributeTypeByName(any(String.class))).thenReturn(blood);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "userattributetype/name/{name}", blood.getAttributeName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.uuid", Matchers.is(blood.getUuid())));
	verify(userService, times(1)).getUserAttributeTypeByName(any(String.class));
    }

    @Test
    public void shouldGetUserAttributeTypeByShortName() throws Exception {
	when(userService.getUserAttributeTypeByShortName(any(String.class))).thenReturn(blood);
	ResultActions actions = mockMvc
		.perform(get(API_PREFIX + "userattributetype/shortname/{shortName}", blood.getShortName()));
	actions.andExpect(status().isOk());
	actions.andExpect(jsonPath("$.shortName", Matchers.is(blood.getShortName())));
	verify(userService, times(1)).getUserAttributeTypeByShortName(any(String.class));
    }

    @Test
    public void shouldGetUserAttributeTypes() throws Exception {
	when(userService.getAllUserAttributeTypes()).thenReturn(Arrays.asList(occupation, patronus, blood));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "userattributetypes"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
	verify(userService, times(1)).getAllUserAttributeTypes();
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUserById() throws Exception {
	when(userService.getUserById(any(Integer.class))).thenReturn(dumbledore);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "user/id/{id}", 1));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.uuid", Matchers.is(dumbledore.getUuid())));
	verify(userService, times(1)).getUserById(any(Integer.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUserByUsername() throws Exception {
	when(userService.getUserByUsername(any(String.class))).thenReturn(dumbledore);
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "user/username/{username}", dumbledore.getUsername()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$.uuid", Matchers.is(dumbledore.getUuid())));
	verify(userService, times(1)).getUserByUsername(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUserList() throws Exception {
	when(userService.getAllUsers()).thenReturn(Arrays.asList(dumbledore, snape));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "user/list"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	UserDto dumbledoreDto = new UserDto(dumbledore);
	UserDto snapeDto = new UserDto(snape);
	actions.andExpect(jsonPath("$[0].username", Matchers.is(dumbledoreDto.getUsername())));
	actions.andExpect(jsonPath("$[1].username", Matchers.is(snapeDto.getUsername())));
	verify(userService, times(1)).getAllUsers();
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUsers() throws Exception {
	when(userService.getAllUsers()).thenReturn(Arrays.asList(dumbledore, snape, tonks));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "users"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
	verify(userService, times(1)).getAllUsers();
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUsersByName() throws Exception {
	when(userService.getUsersByFullName(any(String.class))).thenReturn(Arrays.asList(fred, george));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "users/name/{name}", "Weasley"));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	verify(userService, times(1)).getUsersByFullName(any(String.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldGetUsersByRole() throws Exception {
	dumbledore.getUserRoles().add(headmaster);
	umbridge.getUserRoles().add(headmaster);
	when(userService.getRoleByUuid(any(String.class))).thenReturn(headmaster);
	when(userService.getUsersByRole(any(Role.class))).thenReturn(Arrays.asList(dumbledore, umbridge));
	ResultActions actions = mockMvc.perform(get(API_PREFIX + "users/role/{uuid}", headmaster.getUuid()));
	actions.andExpect(status().isOk());
	actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
	actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
	actions.andExpect(jsonPath("$[0].uuid", Matchers.is(dumbledore.getUuid())));
	verify(userService, times(1)).getRoleByUuid(any(String.class));
	verify(userService, times(1)).getUsersByRole(any(Role.class));
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotDeletePrivilege() throws Exception {
	ResultActions actions = mockMvc.perform(delete(API_PREFIX + "privilege/{uuid}", magic.getUuid()));
	actions.andExpect(status().isNotImplemented());
	verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldNotUpdatePrivilege() throws Exception {
	String content = BaseEntity.getGson().toJson(charm);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "privilege/{uuid}", charm.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isNotImplemented());
    }

    @Test
    public void shouldUpdateRole() throws Exception {
	when(userService.getRoleByUuid(any(String.class))).thenReturn(headmaster);
	when(userService.updateRole(any(Role.class))).thenReturn(headmaster);
	String content = BaseEntity.getGson().toJson(occupation);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "role/{uuid}", headmaster.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(userService, times(1)).getRoleByUuid(any(String.class));
	verify(userService, times(1)).updateRole(any(Role.class));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
	when(userService.getUserByUuid(any(String.class))).thenReturn(dumbledore);
	when(userService.updateUser(any(User.class))).thenReturn(dumbledore);
	String content = BaseEntity.getGson().toJson(dumbledore);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "user/{uuid}", dumbledore.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(userService, times(1)).getUserByUuid(any(String.class));
	verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    public void shouldUpdateUserAttribute() throws Exception {
	when(userService.getUserAttributeByUuid(any(String.class))).thenReturn(snapeBlood);
	when(userService.updateUserAttribute(any(UserAttribute.class))).thenReturn(snapeBlood);
	String content = BaseEntity.getGson().toJson(hogwartz);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "userattribute/{uuid}", snapeBlood.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(userService, times(1)).getUserAttributeByUuid(any(String.class));
	verify(userService, times(1)).updateUserAttribute(any(UserAttribute.class));
    }

    @Test
    public void shouldUpdateUserAttributeType() throws Exception {
	when(userService.getUserAttributeTypeByUuid(any(String.class))).thenReturn(occupation);
	when(userService.updateUserAttributeType(any(UserAttributeType.class))).thenReturn(occupation);
	String content = BaseEntity.getGson().toJson(occupation);
	ResultActions actions = mockMvc.perform(put(API_PREFIX + "userattributetype/{uuid}", occupation.getUuid())
		.contentType(MediaType.APPLICATION_JSON_UTF8).content(content));
	actions.andExpect(status().isOk());
	verify(userService, times(1)).getUserAttributeTypeByUuid(any(String.class));
	verify(userService, times(1)).updateUserAttributeType(any(UserAttributeType.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.UserController#unvoidUser(java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void shouldUnvoidUser() throws Exception {
	when(userService.getUserByUuid(any(String.class))).thenReturn(dumbledore);
	when(userService.unvoidUser(any(User.class))).thenReturn(dumbledore);
	ResultActions actions = mockMvc.perform(patch(API_PREFIX + "user/{uuid}", dumbledore.getUuid()));
	verify(userService, times(1)).getUserByUuid(dumbledore.getUuid());
	verify(userService, times(1)).unvoidUser(dumbledore);
	verifyNoMoreInteractions(userService);
    }
}
