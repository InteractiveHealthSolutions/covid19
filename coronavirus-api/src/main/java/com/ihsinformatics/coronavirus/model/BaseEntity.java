/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsinformatics.coronavirus.util.DateDeserializer;
import com.ihsinformatics.coronavirus.util.DateSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@AllArgsConstructor // Lombok creates a constructor with all arguments
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private static GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer())
	    .registerTypeAdapter(Date.class, new DateSerializer()).setPrettyPrinting();

    @Getter
    protected static Gson gson;

    @Column(name = "uuid", updatable = false, unique = true, nullable = false, length = 38)
    protected String uuid;

    protected static void initGson() {
	BaseEntity.gson = builder.create();
    }

    protected BaseEntity() {
	this.uuid = UUID.randomUUID().toString();
    }
}
