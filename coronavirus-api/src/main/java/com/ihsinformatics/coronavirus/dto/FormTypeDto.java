/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.service.MetadataService;

import lombok.Getter;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
public class FormTypeDto {

    private Integer formTypeId;

    private String formName;

    private String shortName;

    private Integer version;

    private String formSchema;

    private String uuid;

    private String formGroupUuid;

    public FormTypeDto(Integer formTypeId, String formName, String shortName, Integer version, String formSchema,
	    String formGroupUuid, String uuid) {
	super();
	this.formTypeId = formTypeId;
	this.formName = formName;
	this.shortName = shortName;
	this.version = version;
	this.formSchema = formSchema;
	this.formGroupUuid = formGroupUuid;
	this.uuid = uuid;
    }

    public FormTypeDto(FormType formType) {
	this.formTypeId = formType.getFormTypeId();
	this.formName = formType.getFormName();
	this.shortName = formType.getShortName();
	this.version = formType.getVersion();
	this.formSchema = formType.getFormSchema();
	this.formGroupUuid = formType.getFormGroup() == null ? null : formType.getFormGroup().getUuid();
	this.uuid = formType.getUuid();
    }

    public FormType toFormType(MetadataService metadataService) {
	FormType formType = FormType.builder().formTypeId(formTypeId).formName(formName).shortName(shortName)
		.version(version).formSchema(formSchema).build();
	Definition formGroup = metadataService.getDefinitionByUuid(formGroupUuid);
	formType.setFormGroup(formGroup);
	return formType;
    }
}
