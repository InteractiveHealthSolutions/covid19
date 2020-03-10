/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@Setter
@Getter
public class FormDataListDto {

   
	private Integer formId;

    private String uuid;
    
    private String formTypeName;

    private String formTypeUuid;
    
    private String formTypeGroup;

    private String locationUuid;
    
    private String locationName;
    
    private Integer locationId;

    private Date formDate;

    private Date dateCreated;
    
    private Date dateUpdated;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Boolean isVoided;
    
    private String voidedReason;
    
    public FormDataListDto(Integer formId, String uuid, String formTypeName, String formTypeUuid, String formTypeGroup,
			String locationUuid, String locationName, Integer locationId, Date formDate, Date dateCreated,
			Date dateUpdated, String createdBy, String updatedBy, Boolean isVoided, String voidedReason) {
		super();
		this.formId = formId;
		this.uuid = uuid;
		this.formTypeName = formTypeName;
		this.formTypeUuid = formTypeUuid;
		this.formTypeGroup = formTypeGroup;
		this.locationUuid = locationUuid;
		this.locationName = locationName;
		this.locationId = locationId;
		this.formDate = formDate;
		this.dateCreated = dateCreated;
		this.dateUpdated = dateUpdated;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.isVoided = isVoided;
		this.voidedReason = voidedReason;
	}
}
