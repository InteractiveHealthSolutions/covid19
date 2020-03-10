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
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@Table(name = "person")
@Builder
public class Person extends DataEntity {

    private static final long serialVersionUID = 438143645994205849L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "title", length = 10)
    private String title;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "family_name", length = 50)
    private String familyName;

    @Column(name = "gender", nullable = false, length = 50)
    private String gender;

    @Column(name = "dob", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(name = "dob_estimated")
    private Boolean dobEstimated;

    @Column(name = "address1", length = 255)
    private String address1;

    @Column(name = "address2", length = 255)
    private String address2;

    @Column(name = "landmark", length = 255)
    private String landmark;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "city_village", length = 255)
    private String cityVillage;

    @Column(name = "state_province", length = 255)
    private String stateProvince;

    @Column(name = "country", length = 255)
    private String country;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "primary_contact", length = 255)
    private String primaryContact;

    @Column(name = "secondary_contact", length = 255)
    private String secondaryContact;

    @Column(name = "email", length = 255)
    private String email;

    @OneToOne
    @JoinColumn(name = "person_id")
    @JsonIgnore
    @NotAudited
    private Participant participant;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    @Builder.Default
    private List<PersonAttribute> attributes = new ArrayList<>();

    @JsonManagedReference
    public List<PersonAttribute> getAttributes() {
	return attributes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	if (personId != null) {
	    builder.append(personId);
	    builder.append(", ");
	}
	if (firstName != null) {
	    builder.append(firstName);
	    builder.append(", ");
	}
	if (lastName != null) {
	    builder.append(lastName);
	    builder.append(", ");
	}
	if (familyName != null) {
	    builder.append(familyName);
	    builder.append(", ");
	}
	if (gender != null) {
	    builder.append(gender);
	    builder.append(", ");
	}
	if (dob != null) {
	    builder.append(DateTimeUtil.toSqlDateTimeString(dob));
	    builder.append(", ");
	}
	builder.append(dobEstimated);
	builder.append(", ");
	if (country != null)
	    builder.append(country);
	return builder.toString();
    }

}
