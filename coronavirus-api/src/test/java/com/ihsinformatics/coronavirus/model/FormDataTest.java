/**
 * 
 */
package com.ihsinformatics.coronavirus.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.util.DataType;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class FormDataTest {

    private Element schoolElement = Element.builder().dataType(DataType.LOCATION).elementName("School")
	    .shortName("SCHOOL").build();
    private Element houseElement = Element.builder().dataType(DataType.STRING).elementName("House Name")
	    .shortName("HOUSE").build();
    private Element roleElement = Element.builder().dataType(DataType.DEFINITION).elementName("Team Role")
	    .shortName("ROLE").build();
    private Element numberElement = Element.builder().dataType(DataType.INTEGER).elementName("Dress Number")
	    .shortName("NO").build();
    private Element heightElement = Element.builder().dataType(DataType.FLOAT).elementName("Height").shortName("HEIGHT")
	    .build();
    private Element captainElement = Element.builder().dataType(DataType.BOOLEAN).elementName("Is Team Captain")
	    .shortName("CAPTAIN").build();
    private Element genderElement = Element.builder().dataType(DataType.CHARACTER).elementName("Gender")
	    .shortName("GENDER").build();
    private Element dateJoinedElement = Element.builder().dataType(DataType.DATE).elementName("Date Joined")
	    .shortName("JOIN_DATE").build();
    private Element refereeElement = Element.builder().dataType(DataType.USER).elementName("Referred By")
	    .shortName("REFEREE").build();
    private Element titlesElement = Element.builder().dataType(DataType.JSON).elementName("Titles").shortName("TITLES")
	    .build();
    private StringBuilder dataJsonStr;
    private Map<String, Object> sampleDataMap;

    @Before
    public void reset() throws JSONException {
	dataJsonStr = new StringBuilder();
	dataJsonStr.append("{");
	dataJsonStr.append("\"SCHOOL\": \"c89e09d8-bd26-4cec-853e-03dd4299b33c\",");
	dataJsonStr.append("\"ROLE\": \"c89e09d8-a791-4cec-853e-03dd4299b33c\",");
	dataJsonStr.append("\"NO\": 7,");
	dataJsonStr.append("\"JOIN_DATE\": \"2019-01-01\",");
	dataJsonStr.append("\"REFEREE\": \"664348c6-ecd4-486c-b0b0-edcf19a72d36\",");
	dataJsonStr.append("\"TITLES\": {");
	dataJsonStr.append("\"values\": [");
	dataJsonStr.append("\"Player of the Series 1992\",");
	dataJsonStr.append("\"Olympics Gold Medal 1993\",");
	dataJsonStr.append("\"Captain of the Year 1994\",");
	dataJsonStr.append("\"Hatrick Championship Title Winning Streak 1993-1995\"");
	dataJsonStr.append("]},");
	dataJsonStr.append("\"GENDER\": \"M\",");
	dataJsonStr.append("\"HOUSE\": \"Ravenclaw\",");
	dataJsonStr.append("\"HEIGHT\": 5.8,");
	dataJsonStr.append("\"CAPTAIN\": \"False\"");
	dataJsonStr.append("}");
	sampleDataMap = new HashMap<>();
	sampleDataMap.put(schoolElement.getShortName(), "c89e09d8-bd26-4cec-853e-03dd4299b33c");
	sampleDataMap.put(houseElement.getShortName(), "Ravenclaw");
	sampleDataMap.put(roleElement.getShortName(), "c89e09d8-a791-4cec-853e-03dd4299b33c");
	sampleDataMap.put(numberElement.getShortName(), 7);
	sampleDataMap.put(heightElement.getShortName(), 5.8);
	sampleDataMap.put(captainElement.getShortName(), "False");
	sampleDataMap.put(genderElement.getShortName(), "M");
	sampleDataMap.put(dateJoinedElement.getShortName(),
		DateTimeUtil.toSqlDateString(DateTimeUtil.create(1, 1, 2019)));
	sampleDataMap.put(refereeElement.getShortName(), "664348c6-ecd4-486c-b0b0-edcf19a72d36");
	JSONArray titlesJson = new JSONArray();
	titlesJson.put("Player of the Series 1992");
	titlesJson.put("Olympics Gold Medal 1993");
	titlesJson.put("Captain of the Year 1994");
	titlesJson.put("Hatrick Championship Title Winning Streak 1993-1995");
	sampleDataMap.put(titlesElement.getShortName(), titlesJson);
    }

    @Test
    public void shouldDeserialize() throws JSONException, IOException {
	FormData fd = FormData.builder().formDate(DateTimeUtil.create(1, 1, 2019)).referenceId("100").build();
	fd.setData(dataJsonStr.toString());
	fd.deserializeSchema();
	fd.setDataMap(sampleDataMap);
	Map<String, Object> actual = fd.getDataMap();
	// Check not null
	assertNotNull(actual);
	// Check size
	assertEquals(sampleDataMap.size(), actual.size());
	// Match items. The line below will match every item in any map
	assertThat(sampleDataMap.entrySet(), Matchers.everyItem(Matchers.isIn(actual.entrySet())));
	assertThat(actual.entrySet(), Matchers.everyItem(Matchers.isIn(sampleDataMap.entrySet())));
    }

    @Test
    public void shouldSerialize() throws IOException, JSONException {
	FormData fd = FormData.builder().formDate(DateTimeUtil.create(1, 1, 2019)).referenceId("100").build();
	fd.setDataMap(sampleDataMap);
	fd.serializeSchema();
	JSONObject expected = new JSONObject(dataJsonStr.toString());
	JSONObject actual = new JSONObject(fd.getData());
	JSONAssert.assertEquals(expected, actual, false);
    }
}
