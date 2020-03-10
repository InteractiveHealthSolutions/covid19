package com.ihsinformatics.coronavirus.dto;

import com.ihsinformatics.coronavirus.model.LocationAttributeType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */

@Setter
@Getter
public class LocationMapObject {
	
	 
	    private Integer attributeId;
	    private LocationAttributeType attributeType;
	    private String dataType;
	    private Object value;

}
