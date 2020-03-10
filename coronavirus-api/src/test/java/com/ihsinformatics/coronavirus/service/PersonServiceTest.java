/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.Participant;
import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.util.SearchCriteria;
import com.ihsinformatics.coronavirus.util.SearchOperator;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class PersonServiceTest extends BaseServiceTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#deletePerson(com.ihsinformatics.coronavirus.model.Person)}.
     */
    @Test
    public void shouldDeletePerson() {
	when(participantRepository.findById(any(Integer.class))).thenReturn(null);
	doNothing().when(personRepository).delete(any(Person.class));
	personService.deletePerson(hermione);
	verify(personRepository, times(1)).delete(any(Person.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#deletePersonAttribute(com.ihsinformatics.coronavirus.model.PersonAttribute)}.
     */
    @Test
    public void shouldDeletePersonAttribute() {
	doNothing().when(personAttributeRepository).delete(any(PersonAttribute.class));
	personService.deletePersonAttribute(ronHeight);
	verify(personAttributeRepository, times(1)).delete(any(PersonAttribute.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#deletePersonAttributeType(com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     */
    @Test
    public void shouldDeletePersonAttributeType() {
	when(personAttributeRepository.findByAttributeType(any(PersonAttributeType.class)))
		.thenReturn(Collections.emptyList());
	doNothing().when(personAttributeTypeRepository).delete(any(PersonAttributeType.class));
	personService.deletePersonAttributeType(socialStatus, true);
	verify(personAttributeRepository, times(1)).findByAttributeType(any(PersonAttributeType.class));
	verify(personAttributeTypeRepository, times(1)).delete(any(PersonAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getAllPersonAttributeTypes()}.
     */
    @Test
    public void shouldGetAllPersonAttributeTypes() {
	when(personAttributeTypeRepository.findAll()).thenReturn(Arrays.asList(height, socialStatus));
	assertThat(personService.getAllPersonAttributeTypes(), containsInAnyOrder(height, socialStatus));
	verify(personAttributeTypeRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPeopleByAddress(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void shouldGetPeopleByAddress() {
	when(personRepository.findByAddress(any(String.class), any(String.class), any(String.class), any(String.class),
		any(String.class))).thenReturn(Arrays.asList(ron, harry));
	assertEquals(2, personService.getPeopleByAddress("", "", "", "England").size());
	verify(personRepository, times(1)).findByAddress(any(String.class), any(String.class), any(String.class),
		any(String.class), any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPeopleByContact(java.lang.String, boolean)}.
     */
    @Test
    public void shouldGetPeopleByContact() {
	when(personRepository.findByContact(any(String.class), any(Boolean.class)))
		.thenReturn(Arrays.asList(ron, hermione));
	assertEquals(2, personService.getPeopleByContact("03452345345", Boolean.TRUE).size());
	verify(personRepository, times(1)).findByContact(any(String.class), any(Boolean.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPeopleByName(java.lang.String)}.
     */
    @Test
    public void shouldGetPeopleByName() {
	when(personRepository.findByPersonName(any(String.class), any(String.class), any(String.class)))
		.thenReturn(Arrays.asList(ron, hermione));
	assertEquals(2, personService.getPeopleByName("Weasley").size());
	verify(personRepository, times(1)).findByPersonName(any(String.class), any(String.class), any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetPersonAttributeById() {
	Optional<PersonAttribute> ronSocialStatusObj = Optional.of(ronSocialStatus);
	when(personAttributeRepository.findById(any(Integer.class))).thenReturn(ronSocialStatusObj);
	assertThat(personService.getPersonAttributeById(1), is(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetPersonAttributeByUuid() {
	when(personAttributeRepository.findByUuid(any(String.class))).thenReturn(ronSocialStatus);
	assertThat(personService.getPersonAttributeByUuid(ronSocialStatus.getUuid()), is(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributes(com.ihsinformatics.coronavirus.model.Person, com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     */
    @Test
    public void shouldGetPersonAttributes() {
	when(personAttributeRepository.findByPersonAndAttributeType(any(Person.class), any(PersonAttributeType.class)))
		.thenReturn(Arrays.asList(ronSocialStatus));
	assertThat(personService.getPersonAttributes(ron, socialStatus), contains(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findByPersonAndAttributeType(any(Person.class),
		any(PersonAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributesByPerson(com.ihsinformatics.coronavirus.model.Person)}.
     */
    @Test
    public void shouldGetPersonAttributesByPerson() {
	when(personAttributeRepository.findByPerson(any(Person.class))).thenReturn(Arrays.asList(ronSocialStatus));
	assertThat(personService.getPersonAttributesByPerson(ron), contains(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findByPerson(any(Person.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributesByType(com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     */
    @Test
    public void shouldGetPersonAttributesByType() {
	when(personAttributeRepository.findByAttributeType(any(PersonAttributeType.class)))
		.thenReturn(Arrays.asList(ronSocialStatus));
	assertThat(personService.getPersonAttributesByType(socialStatus), contains(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findByAttributeType(any(PersonAttributeType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributesByTypeAndValue(com.ihsinformatics.coronavirus.model.PersonAttributeType, java.lang.String)}.
     */
    @Test
    public void shouldGetPersonAttributesByTypeAndValue() {
	when(personAttributeRepository.findByAttributeTypeAndValue(any(PersonAttributeType.class), any(String.class)))
		.thenReturn(Arrays.asList(ronSocialStatus));
	assertThat(personService.getPersonAttributesByTypeAndValue(socialStatus, ronSocialStatus.getAttributeValue()),
		contains(ronSocialStatus));
	verify(personAttributeRepository, times(1)).findByAttributeTypeAndValue(any(PersonAttributeType.class),
		any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeTypeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetPersonAttributeTypeById() {
	Optional<PersonAttributeType> socialStatusObj = Optional.of(socialStatus);
	when(personAttributeTypeRepository.findById(any(Integer.class))).thenReturn(socialStatusObj);
	assertThat(personService.getPersonAttributeTypeById(1), is(socialStatus));
	verify(personAttributeTypeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeTypeByName(java.lang.String)}.
     */
    @Test
    public void shouldGetPersonAttributeTypeByName() {
	when(personAttributeTypeRepository.findByAttributeName(any(String.class))).thenReturn(socialStatus);
	assertThat(personService.getPersonAttributeTypeByName(socialStatus.getUuid()), is(socialStatus));
	verify(personAttributeTypeRepository, times(1)).findByAttributeName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeTypeByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetPersonAttributeTypeByShortName() {
	when(personAttributeTypeRepository.findByShortName(any(String.class))).thenReturn(socialStatus);
	assertThat(personService.getPersonAttributeTypeByShortName(socialStatus.getUuid()), is(socialStatus));
	verify(personAttributeTypeRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonAttributeTypeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetPersonAttributeTypeByUuid() {
	when(personAttributeTypeRepository.findByUuid(any(String.class))).thenReturn(socialStatus);
	assertThat(personService.getPersonAttributeTypeByUuid(socialStatus.getUuid()), is(socialStatus));
	verify(personAttributeTypeRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetPersonById() {
	Optional<Person> hermioneObj = Optional.of(hermione);
	when(personRepository.findById(any(Integer.class))).thenReturn(hermioneObj);
	assertThat(personService.getPersonById(1), is(hermione));
	verify(personRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#getPersonByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetPersonByUuid() {
	when(personRepository.findByUuid(any(String.class))).thenReturn(hermione);
	assertThat(personService.getPersonByUuid(hermione.getUuid()), is(hermione));
	verify(personRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#deletePerson(com.ihsinformatics.coronavirus.model.Person)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotDeletePerson() {
	seeker.getPerson().setPersonId(99);
	Optional<Participant> seekerObj = Optional.of(seeker);
	when(participantRepository.findById(any(Integer.class))).thenReturn(seekerObj);
	personService.deletePerson(harry);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#deletePersonAttributeType(com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     */
    @Test(expected = HibernateException.class)
    public void shouldNotDeletePersonAttributeType() {
	when(personAttributeRepository.findByAttributeType(any(PersonAttributeType.class)))
		.thenReturn(Arrays.asList(ronSocialStatus));
	personService.deletePersonAttributeType(socialStatus, false);
    }

    @Test
    public void shouldReturnAnObject() {
	Person person = mock(Person.class);
	assertNotNull(person);
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#savePerson(com.ihsinformatics.coronavirus.model.Person)}.
     */
    @Test
    public void shouldSavePerson() {
	when(personRepository.save(any(Person.class))).thenReturn(ron);
	assertThat(personService.savePerson(ron), is(ron));
	verify(personRepository, times(1)).save(any(Person.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#savePersonAttributeType(com.ihsinformatics.coronavirus.model.PersonAttributeType)}.
     */
    @Test
    public void shouldSavePersonAttributeType() {
	when(personAttributeTypeRepository.save(any(PersonAttributeType.class))).thenReturn(height);
	assertThat(personService.savePersonAttributeType(height), is(height));
	verify(personAttributeTypeRepository, times(1)).save(any(PersonAttributeType.class));
    }
    
    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#savePersonAttribute(com.ihsinformatics.coronavirus.model.PersonAttribute)}.
     */
    @Test
    public void shouldSavePersonAttribute() {
	when(personAttributeRepository.save(any(PersonAttribute.class))).thenReturn(ronHeight);
	assertThat(personService.savePersonAttribute(ronHeight), is(ronHeight));
	verify(personAttributeRepository, times(1)).save(any(PersonAttribute.class));
    }


    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.PersonServiceImpl#searchPeople(java.util.List)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldSearchPeople() {
	when(personRepository.search(any(List.class))).thenReturn(Arrays.asList(ron, harry));
	List<SearchCriteria> params = new ArrayList<>();
	params.add(new SearchCriteria("gender", SearchOperator.EQUALS, "MALE"));
	List<Person> people = personService.searchPeople(params);
	assertEquals(2, people.size());
	verify(personRepository, times(1)).search(any(List.class));
    }

}
