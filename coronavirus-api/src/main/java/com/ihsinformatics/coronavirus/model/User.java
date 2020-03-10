/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "users")
@Builder
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "userPrivileges", "updatedBy", "voidedBy"}, allowSetters = true)
public class User extends DataEntity {

    private static final long serialVersionUID = 438143645994205849L;

    public static final byte HASH_ROUNDS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "password_hash", nullable = false, length = 255)
    @JsonIgnore
    @ToString.Exclude
    @NotAudited
    private String passwordHash;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    private List<UserAttribute> attributes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    @NotAudited
    private List<Role> userRoles = new ArrayList<>();

    /**
     * Returns a Set of {@link Privilege} objects associated with this user
     * 
     * @return
     */
    public Set<Privilege> getUserPrivileges() {
	Set<Privilege> privileges = new HashSet<>();
	for (Role role : userRoles) {
	    privileges.addAll(role.getRolePrivileges());
	}
	return privileges;
    }

    /**
     * In order to set password, first a salt is generated and password hash is
     * calculated using password + salt and set as password
     * 
     * @param password
     * @throws Exception
     */
    public void setPassword(String password) {
	String hash = BCrypt.hashpw(password, BCrypt.gensalt(HASH_ROUNDS));
	setPasswordHash(hash);
    }

    /**
     * Authenticates password
     * 
     * @param password
     * @return
     * @throws Exception
     */
    public boolean matchPassword(String password) {
	return BCrypt.checkpw(password, getPasswordHash());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return userId + ", " + username + ", " + fullName;
    }
}
