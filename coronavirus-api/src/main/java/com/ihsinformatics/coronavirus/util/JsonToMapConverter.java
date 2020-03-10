/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Purpose of this class is to convert a JSON object into a Map data structure
 * 
 * @author Heril Muratovic
 *         (https://stackoverflow.com/users/4078505/heril-muratovic)
 */
@Converter
public class JsonToMapConverter implements AttributeConverter<String, Map<String, Serializable>> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Serializable> convertToDatabaseColumn(String attribute) {
	if (attribute == null) {
	    return new HashMap<>();
	}
	try {
	    ObjectMapper objectMapper = new ObjectMapper();
	    return objectMapper.readValue(attribute, HashMap.class);
	} catch (IOException e) {
	    LOG.error(e.getMessage());
	}
	return new HashMap<>();
    }

    @Override
    public String convertToEntityAttribute(Map<String, Serializable> dbData) {
	ObjectMapper objectMapper = new ObjectMapper();
	try {
	    return objectMapper.writeValueAsString(dbData);
	} catch (JsonProcessingException e) {
	    LOG.error(e.getMessage());
	    return null;
	}
    }
}
