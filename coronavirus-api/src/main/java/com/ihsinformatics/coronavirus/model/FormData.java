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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "form_data")
public class FormData extends DataEntity {

    private static final long serialVersionUID = -2288674874134225415L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Integer formId;

    @ManyToOne
    @JoinColumn(name = "form_type_id", nullable = false)
    private FormType formType;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "form_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date formDate;

    @Column(name = "reference_id", nullable = false, unique = true, length = 255)
    private String referenceId;

    @Column(name = "data", columnDefinition = "text")
    private String data;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "form_participant", joinColumns = @JoinColumn(name = "form_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    @Builder.Default
    private List<Participant> formParticipants = new ArrayList<>();

    @Convert(converter = JsonToMapConverter.class)
    @Builder.Default
    @Transient
    private Map<String, Object> dataMap = new HashMap();

    /**
     * Converts schema Map into serialized JSON text
     * 
     * @throws JsonProcessingException
     */
    public void serializeSchema() throws JsonProcessingException {
	ObjectMapper objectMapper = new ObjectMapper();
	objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	this.data = objectMapper.writeValueAsString(dataMap);
    }

    /**
     * Converts schema in serialized JSON text into Map
     * 
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void deserializeSchema() throws IOException {
	ObjectMapper objectMapper = new ObjectMapper();
	objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
	objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	try {
	    this.dataMap = objectMapper.readValue(data, HashMap.class);
	} catch (Exception e) {
	    // In case of exception, handle the escape and quotes manually
	    String str = data.replace("\\", "");
	    if (str.startsWith("\"") && str.endsWith("\"")) {
		str = str.substring(1, str.length() - 1);
	    }
	    this.dataMap = objectMapper.readValue(str, HashMap.class);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append(formId);
	builder.append(", ");
	builder.append(formType);
	builder.append(", ");
	if (location != null) {
	    builder.append(location);
	    builder.append(", ");
	}
	if (formDate != null) {
	    builder.append(formDate);
	    builder.append(", ");
	}
	builder.append(referenceId);
	return builder.toString();
    }
}
