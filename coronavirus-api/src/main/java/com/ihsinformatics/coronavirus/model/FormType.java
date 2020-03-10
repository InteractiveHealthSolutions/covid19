/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ihsinformatics.coronavirus.util.JsonToMapConverter;

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
@Table(name = "form_type")
@Builder
public class FormType extends MetadataEntity {

    private static final long serialVersionUID = -2288674874134225415L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_type_id")
    private Integer formTypeId;

    @Column(name = "form_name", nullable = false, unique = true, length = 255)
    private String formName;

    @Column(name = "short_name", nullable = false, unique = true, length = 50)
    private String shortName;

    @Column(name = "version")
    private Integer version;

    @Column(name = "form_schema", columnDefinition = "text")
    private String formSchema;

    @Convert(converter = JsonToMapConverter.class)
    @Builder.Default
    @Transient
    private Map<String, Object> formSchemaMap = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "form_group")
    private Definition formGroup;

    /**
     * Converts schema Map into serialized JSON text
     * 
     * @throws JsonProcessingException
     */
    public void serializeSchema() throws JsonProcessingException {
	ObjectMapper objectMapper = new ObjectMapper();
	this.formSchema = objectMapper.writeValueAsString(formSchemaMap);
    }

    /**
     * Converts schema in serialized JSON text into Map
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void deserializeSchema() throws IOException {
	ObjectMapper objectMapper = new ObjectMapper();
	this.formSchemaMap = objectMapper.readValue(formSchema, HashMap.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(formTypeId);
	builder.append(", ");
	builder.append(formName);
	builder.append(", ");
	builder.append(shortName);
	builder.append(", ");
	if (version != null) {
	    builder.append(version);
	    builder.append(", ");
	}
	builder.append(formGroup);
	return builder.toString();
    }
}
