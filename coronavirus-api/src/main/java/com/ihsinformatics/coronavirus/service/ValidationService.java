/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.validation.ValidationException;

import org.hibernate.HibernateException;
import org.json.JSONException;

import com.ihsinformatics.coronavirus.model.DataEntity;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.util.DataType;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface ValidationService {

    /**
     * Validates whether given string represents a valid JSON object or not
     * 
     * @param jsonStr
     * @return
     */
    public boolean isValidJson(String jsonStr);

    /**
     * Father of validation methods. This method first checks if the input value is
     * of give dataType (String, Double, etc.), then matches regex. The regex must
     * be in format: LHS=RHS. If LHS is "regex", then RHS is expected to be a valid
     * regular expression to match value with; If LHS is "list", then RHS should be
     * a comma-separated list of strings to lookup value in; If LHS is "range", then
     * RHS should be a set of range parts, like 1-10,2.2,3.2,5.5,17.1-18.9, etc. in
     * which, the value will be checked; If LHS is "relation", then RHS is expected
     * to be a Entity.fieldName (case sensitive) string to lookup the value in
     * database
     * 
     * @param regex
     * @param dataType
     * @param value
     * @return
     * @throws ValidationException
     * @throws PatternSyntaxException
     */
    public boolean validateData(String regex, DataType dataType, String value)
	    throws ValidationException, PatternSyntaxException, HibernateException, ClassNotFoundException;

    /**
     * Validates the JSON schema in given {@link FormData} object
     * 
     * @param formData
     * @param dataEntity
     * @throws HibernateException
     * @throws ValidationException
     * @throws IOException
     */
    public void validateFormData(FormData formData, DataEntity dataEntity)
	    throws HibernateException, ValidationException, IOException;

    /**
     * Validates the JSON schema in given {@link FormType} object
     * 
     * @param formType
     * @return
     * @throws HibernateException
     * @throws ValidationException
     * @throws JSONException
     */
    public boolean validateFormType(FormType formType) throws HibernateException, ValidationException, JSONException;

    /**
     * Validates whether value is present in given comma-separated list
     * 
     * @param list
     * @param value
     * @return
     * @throws ValidationException
     */
    public boolean validateList(String list, String value) throws ValidationException;

    /**
     * Validates whether value is in given range. Range can be specified hyphened
     * and/or comma separated values. E.g. "1-10", "2.2-3.0", "1,3,5", "1-5,7,9",
     * etc.
     * 
     * @param range
     * @param value
     * @return
     * @throws ValidationException
     */
    public boolean validateRange(String range, Double value) throws ValidationException;

    /**
     * Validates value according to given regular expression
     * 
     * @param regex
     * @param value
     * @return
     * @throws PatternSyntaxException
     */
    public boolean validateRegex(String regex, String value) throws PatternSyntaxException;

    /**
     * Validates if value exists in given entity-field data. E.g. entity=Location,
     * field=locationName will check whether value exists in locationName of
     * location entity
     * 
     * @param entity
     * @param field
     * @param value
     * @return
     */
    public boolean validateRelation(String entity, String field, String value)
	    throws HibernateException, ClassNotFoundException;
}
