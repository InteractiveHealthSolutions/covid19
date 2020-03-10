/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ihsinformatics.coronavirus.BaseRepositoryData;
import com.ihsinformatics.coronavirus.model.Privilege;
import com.ihsinformatics.coronavirus.model.Role;
import com.ihsinformatics.coronavirus.repository.RoleRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest extends BaseRepositoryData {

    @Autowired
    private RoleRepository roleRepository;

    Role auror;

    @Before
    public void reset() {
	super.reset();
	auror = Role.builder().roleName("Auror").build();
    }

    @Test
    public void shouldDelete() {
	auror = entityManager.persist(auror);
	entityManager.flush();
	Integer id = auror.getRoleId();
	entityManager.detach(auror);
	roleRepository.delete(auror);
	Role found = entityManager.find(Role.class, id);
	assertNull(found);
    }

    @Test
    public void shouldFindById() throws Exception {
	Object id = entityManager.persistAndGetId(auror);
	entityManager.flush();
	entityManager.detach(auror);
	Optional<Role> found = roleRepository.findById(Integer.parseInt(id.toString()));
	assertTrue(found.isPresent());
    }

    @Test
    public void shouldFindByName() {
	auror = entityManager.persist(auror);
	entityManager.flush();
	Role found = roleRepository.findByRoleName(auror.getRoleName());
	assertNotNull(found);
	assertEquals(auror.getUuid(), found.getUuid());
	entityManager.remove(auror);
	entityManager.flush();
    }

    @Test
    public void shouldFindByUuid() throws Exception {
	auror = entityManager.persist(auror);
	entityManager.flush();
	entityManager.detach(auror);
	String uuid = auror.getUuid();
	Role found = roleRepository.findByUuid(uuid);
	assertNotNull(found);
    }

    @Test
    public void shouldSave() {
	auror = roleRepository.save(auror);
	roleRepository.flush();
	Role found = entityManager.find(Role.class, auror.getRoleId());
	assertNotNull(found);
    }

    @Test
    public void shouldSaveWithPrivileges() {
	Set<Privilege> rolePrivileges = new HashSet<>();
	rolePrivileges.add(magic);
	rolePrivileges.add(kill);
	auror.setRolePrivileges(rolePrivileges);
	auror = roleRepository.save(auror);
	roleRepository.flush();
	Role found = entityManager.find(Role.class, auror.getRoleId());
	assertNotNull(found);
	assertFalse(found.getRolePrivileges().isEmpty());
    }
}
