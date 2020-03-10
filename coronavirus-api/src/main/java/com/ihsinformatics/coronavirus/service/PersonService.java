/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.util.List;

import org.hibernate.HibernateException;

import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.util.SearchCriteria;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public interface PersonService {

    /**
     * @param obj
     * @throws HibernateException
     */
    void deletePerson(Person obj) throws HibernateException;

    /**
     * @param obj
     * @throws HibernateException
     */
    void deletePersonAttribute(PersonAttribute obj) throws HibernateException;

    /**
     * Caution! Setting force true will completely remove each dependent entity
     * {@link PersonAttribute} as well
     * 
     * @param obj
     * @param force
     * @throws HibernateException
     */
    void deletePersonAttributeType(PersonAttributeType obj, boolean force) throws HibernateException;

    /**
     * Returns list of {@link PersonAttributeType} objects
     * 
     * @return
     * @throws HibernateException
     */
    List<PersonAttributeType> getAllPersonAttributeTypes() throws HibernateException;

    /**
     * Returns list of {@link Person} objects by matching given address parameters
     * 
     * @param address
     * @param cityVillage
     * @param stateProvince
     * @param country
     * @return
     * @throws HibernateException
     */
    List<Person> getPeopleByAddress(String address, String cityVillage, String stateProvince, String country)
	    throws HibernateException;

    /**
     * Returns list of {@link Person} objects by matching given contact number
     * 
     * @param contact
     * @param primaryContactOnly when true, only primary contact number is matched
     * @return
     * @throws HibernateException
     */
    List<Person> getPeopleByContact(String contact, Boolean primaryContactOnly) throws HibernateException;

    /**
     * Returns list of {@link Person} objects by matching all names with given
     * parameter
     * 
     * @param name
     * @return
     * @throws HibernateException
     */
    List<Person> getPeopleByName(String name) throws HibernateException;

    /**
     * Returns {@link PersonAttribute} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    PersonAttribute getPersonAttributeById(Integer id) throws HibernateException;

    /**
     * Returns {@link PersonAttribute} object by matching UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    PersonAttribute getPersonAttributeByUuid(String uuid) throws HibernateException;

    /**
     * Returns list of {@link PersonAttribute} objects by given {@link Person} and
     * {@link PersonAttributeType}
     * 
     * @param location
     * @param attributeType
     * @return
     * @throws HibernateException
     */
    List<PersonAttribute> getPersonAttributes(Person person, PersonAttributeType attributeType)
	    throws HibernateException;

    /**
     * Returns list of {@link PersonAttribute} objects by given {@link Person}
     * 
     * @param person
     * @return
     * @throws HibernateException
     */
    List<PersonAttribute> getPersonAttributesByPerson(Person person) throws HibernateException;

    /**
     * Returns list of {@link PersonAttribute} objects by given
     * {@link PersonAttributeType}
     * 
     * @param attributeType
     * @return
     * @throws HibernateException
     */
    List<PersonAttribute> getPersonAttributesByType(PersonAttributeType attributeType) throws HibernateException;

    /**
     * Returns list of {@link PersonAttribute} objects by given
     * {@link PersonAttributeType} and its value
     * 
     * @param attributeType
     * @param attributeValue
     * @return
     * @throws HibernateException
     */
    List<PersonAttribute> getPersonAttributesByTypeAndValue(PersonAttributeType attributeType, String attributeValue)
	    throws HibernateException;

    /**
     * Returns {@link PersonAttributeType} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    PersonAttributeType getPersonAttributeTypeById(Integer id) throws HibernateException;

    /**
     * Returns {@link PersonAttributeType} object by matching name
     * 
     * @param name
     * @return
     * @throws HibernateException
     */
    PersonAttributeType getPersonAttributeTypeByName(String name) throws HibernateException;

    /**
     * Returns {@link PersonAttributeType} object by matching short name
     * 
     * @param shortName
     * @return
     * @throws HibernateException
     */
    PersonAttributeType getPersonAttributeTypeByShortName(String shortName) throws HibernateException;

    /**
     * Returns {@link PersonAttributeType} object by matching UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    PersonAttributeType getPersonAttributeTypeByUuid(String uuid) throws HibernateException;

    /**
     * Returns {@link Person} object by generated Id
     * 
     * @param id
     * @return
     * @throws HibernateException
     */
    Person getPersonById(Integer id) throws HibernateException;

    /**
     * Returns {@link Person} object by given UUID
     * 
     * @param uuid
     * @return
     * @throws HibernateException
     */
    Person getPersonByUuid(String uuid) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    Person savePerson(Person obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    PersonAttribute savePersonAttribute(PersonAttribute obj) throws HibernateException;

    /**
     * @param attributes
     * @return
     * @throws HibernateException
     */
    List<PersonAttribute> savePersonAttributes(List<PersonAttribute> attributes) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    PersonAttributeType savePersonAttributeType(PersonAttributeType obj) throws HibernateException;

    /**
     * Returns list of {@link Person} objects by matching list of givem parameters
     * 
     * @param params
     * @return
     * @throws HibernateException
     */
    List<Person> searchPeople(List<SearchCriteria> params) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    Person updatePerson(Person obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    PersonAttribute updatePersonAttribute(PersonAttribute obj) throws HibernateException;

    /**
     * @param obj
     * @return
     * @throws HibernateException
     */
    PersonAttributeType updatePersonAttributeType(PersonAttributeType obj) throws HibernateException;

}
