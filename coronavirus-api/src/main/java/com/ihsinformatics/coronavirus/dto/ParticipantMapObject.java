package com.ihsinformatics.coronavirus.dto;

import com.ihsinformatics.coronavirus.model.PersonAttributeType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */

@Setter
@Getter
public class ParticipantMapObject {
	
	 
	    private Integer attributeId;
	    private PersonAttributeType attributeType;
	    private String dataType;
	    private Object value;

}
