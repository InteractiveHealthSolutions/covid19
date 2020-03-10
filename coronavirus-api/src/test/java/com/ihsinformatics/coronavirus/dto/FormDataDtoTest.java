/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.dto;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.dto.FormDataDto;
import com.ihsinformatics.coronavirus.model.FormData;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class FormDataDtoTest extends BaseServiceTest {

    private FormDataDto formDataDto;

    @Before
    public void reset() {
	super.reset();
	Set<String> participantUuids = new HashSet<>();
	participantUuids.add(seeker.getUuid());
	participantUuids.add(keeper.getUuid());
	JSONObject json;
	try {
	    json = new JSONObject(
		    "{\"data\": {\"date_start\": \"2019-09-12\",\"post_component\": \"{\\\"values\\\":[\\\"comms\\\"]}\",\"post_date\": \"2019-09-12\",\"post_type\": \"other\",\"post_type_other\": \"fjffhfjffj\",\"topic_covered\": \"{\\\"values\\\":[\\\"other\\\"]}\",\"topic_covered_other\": \"fjdjfchchcj\",\"post_platform\": {\"values\": [{\"post_platform\": \"facebook\",\"post_boosted\": false,\"post_likes_count\": 5544,\"post_comments_count\": 5,\"post_shares_count\": 2,\"post_boosted_count\": \"7\",\"post_url\": \"https://hfhgfhdgd\"}]}},\"formDate\": \"2019-09-02\",\"formType\": {\"formTypeId\": 22},\"referenceId\": \"\"}");
	    formDataDto = new FormDataDto(100, ronData.getUuid(), ronData.getFormType().getUuid(), hogwartz.getUuid(),
		    new Date(), ronData.getReferenceId(), json, participantUuids);
	} catch (JSONException e) {
	}
    }

    @Test
    public void shouldConvertToFormData() {
	FormData formData = formDataDto.toFormData(formService, locationService, participantService);
	assertNotNull(formData);
    }

    @Test
    public void shouldConvertToFormDataWithoutFormDate() {
	formDataDto.setFormDate(null);
	FormData formData = formDataDto.toFormData(formService, locationService, participantService);
	assertNotNull(formData);
    }
}
