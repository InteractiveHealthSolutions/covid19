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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table(name = "location")
@Builder
public class Location extends DataEntity {

    private static final long serialVersionUID = 438143645994205849L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "location_name", nullable = false, length = 255)
    private String locationName;

    @Column(name = "short_name", nullable = false, unique = true, length = 50)
    private String shortName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", nullable = false)
    @NotAudited
    private Definition category;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "address1", length = 255)
    private String address1;

    @Column(name = "address2", length = 255)
    private String address2;

    @Column(name = "address3", length = 255)
    private String address3;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "landmark1", length = 255)
    private String landmark1;

    @Column(name = "landmark2", length = 255)
    private String landmark2;

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

    @Column(name = "primary_contact_person", length = 255)
    private String primaryContactPerson;

    @Column(name = "secondary_contact", length = 255)
    private String secondaryContact;

    @Column(name = "secondary_contact_person", length = 255)
    private String secondaryContactPerson;

    @Column(name = "tertiary_contact", length = 255)
    private String tertiaryContact;

    @Column(name = "tertiary_contact_person", length = 255)
    private String tertiaryContactPerson;
    
    @Column(name = "extension", length = 10)
    private String extension;

    @Column(name = "email", length = 255)
    private String email;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "location")
    @Builder.Default
    private List<LocationAttribute> attributes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_location")
    private Location parentLocation;

    @JsonManagedReference
    public List<LocationAttribute> getAttributes() {
	return attributes;
    }
}
