/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ihsinformatics.coronavirus.BaseTestData;
import com.ihsinformatics.coronavirus.web.BaseController;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@RunWith(MockitoJUnitRunner.class)
public class BaseControllerTest extends BaseTestData {

    @InjectMocks
    private BaseController baseController;

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#dependencyFailure(java.lang.String)}.
     */
    @Test
    public void testDependencyFailure() {
	ResponseEntity<?> response = baseController.dependencyFailure("");
	assertEquals(HttpStatus.FAILED_DEPENDENCY, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#exceptionFoundResponse(java.lang.String)}.
     */
    @Test
    public void testExceptionFoundResponse() {
	ResponseEntity<?> response = baseController.exceptionFoundResponse("");
	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#inputStreamToJson(java.io.InputStream)}.
     * 
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void testInputStreamToJson() throws IOException, JSONException {
	String str = "{\"data\": \"{\\\"high_score\\\":\\\"170\\\",\\\"broomsticks\\\":\\\"Nimbus 2000\\\",\\\"winner\\\":\\\"Gryffindor\\\",\\\"audience_count\\\":\\\"600\\\"}\",\"matchDate\": \"2019-09-02\",\"matchType\": {\"matchTypeId\": 1},\"referenceId\": \"\"}";
	InputStream input = new ByteArrayInputStream(str.getBytes());
	JSONObject json = baseController.inputStreamToJson(input);
	assertTrue(json.has("data"));
	assertTrue(json.has("matchDate"));
	assertTrue(json.has("matchType"));
	assertTrue(json.has("referenceId"));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#invalidArgumentResponse(java.lang.String)}.
     */
    @Test
    public void testInvalidArgumentResponse() {
	ResponseEntity<?> response = baseController.invalidArgumentResponse("");
	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#invalidDataResponse(java.lang.String)}.
     */
    @Test
    public void testInvalidDataResponse() {
	ResponseEntity<?> response = baseController.invalidDataResponse("");
	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#noContent(java.lang.String)}.
     */
    @Test
    public void testNoContent() {
	ResponseEntity<?> response = baseController.noContent("");
	assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#noEntityFoundResponse(java.lang.String)}.
     */
    @Test
    public void testNoEntityFoundResponse() {
	ResponseEntity<?> response = baseController.noEntityFoundResponse("");
	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#notImplementedResponse(java.lang.String)}.
     */
    @Test
    public void testNotImplementedResponse() {
	ResponseEntity<?> response = baseController.notImplementedResponse("");
	assertEquals(HttpStatus.NOT_IMPLEMENTED, response.getStatusCode());
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.web.BaseController#resourceAlreadyExists(java.lang.String)}.
     */
    @Test
    public void testResourceAlreadyExists() {
	ResponseEntity<?> response = baseController.resourceAlreadyExists("");
	assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
}
