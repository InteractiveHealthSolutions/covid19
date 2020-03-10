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
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.service.FormService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.ParticipantService;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Setter
@Getter
public class FormDataDto {

    private Integer formId;

    private String uuid;

    private String formTypeUuid;

    private String locationUuid;

    private Date formDate;

    private String referenceId;

    private JSONObject data;

    private Set<String> formParticipantUuids = new HashSet<>();

    public FormDataDto(Integer formId, String uuid, String formTypeUuid, String locationUuid, Date formDate,
	    String referenceId, JSONObject data, Set<String> formParticipantUuids) {
	this.formId = formId;
	this.uuid = uuid;
	this.formTypeUuid = formTypeUuid;
	this.locationUuid = locationUuid;
	this.formDate = formDate;
	this.referenceId = referenceId;
	this.data = data;
	this.formParticipantUuids = formParticipantUuids;
    }

    public FormData toFormData(FormService formService, LocationService locationService,
	    ParticipantService participantService) {
	Location location = locationService.getLocationByUuid(locationUuid);
	String dataStr = JSONObject.quote(data.toString());
	FormData formData = FormData.builder().formType(formService.getFormTypeByUuid(formTypeUuid)).formDate(formDate)
		.location(location).referenceId(referenceId).build();
	if(uuid != null)
		formData.setUuid(uuid);
	formData.setData(dataStr);
	for (String participantUuid : formParticipantUuids) {
	    formData.getFormParticipants().add(participantService.getParticipantByUuid(participantUuid));
	}
	return formData;
    }

    public FormDataDto(JSONObject json, FormService formService, LocationService locationService,
	    ParticipantService participantService) throws JSONException {
	if (json.has("uuid")) {
	    this.uuid = json.getString("uuid");
	}	
	if (json.has("formDate")) {
	    Date date = DateTimeUtil.fromSqlDateString(json.get("formDate").toString());
	    this.formDate = date;
	}
	if (json.has("referenceId")) {
	    String reference = json.getString("referenceId");
	    this.referenceId = reference;
	}
	if (json.has("formType")) {
	    JSONObject formTypeJson = json.getJSONObject("formType");
	    Integer formTypeId = formTypeJson.getInt("formTypeId");
	    this.formTypeUuid = formService.getFormTypeById(formTypeId).getUuid();
	}
	if (json.has("location")) {
	    JSONObject locationJson = json.getJSONObject("location");
	    Integer locationId = locationJson.getInt("locationId");
	    this.locationUuid = locationService.getLocationById(locationId).getUuid();
	}
	if (json.has("formParticipants")) {
	    JSONArray participants = json.getJSONArray("formParticipants");
	    for(int i = 0; i<participants.length(); i++){
		JSONObject participantJson = participants.getJSONObject(i);
		Integer participantId = participantJson.getInt("participantId");
		formParticipantUuids.add(participantService.getParticipantById(participantId).getUuid());
	    }
	}
	JSONObject dataJson = new JSONObject(json.get("data").toString());
	this.data = dataJson;
    }
}
