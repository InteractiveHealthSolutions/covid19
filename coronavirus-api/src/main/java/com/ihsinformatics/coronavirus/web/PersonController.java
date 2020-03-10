/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AlreadyBoundException;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.model.Person;
import com.ihsinformatics.coronavirus.model.PersonAttribute;
import com.ihsinformatics.coronavirus.model.PersonAttributeType;
import com.ihsinformatics.coronavirus.service.PersonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Person Controller")
public class PersonController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PersonService service;

    @ApiOperation(value = "Create new Person")
    @PostMapping("/person")
    public ResponseEntity<?> createPerson(@RequestBody Person obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create person: {}", obj);
	try {
	    Person result = service.savePerson(obj);
	    return ResponseEntity.created(new URI("/api/person/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create new PersonAttribute")
    @PostMapping("/personattribute")
    public ResponseEntity<?> createPersonAttribute(@RequestBody PersonAttribute obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create person attribute: {}", obj);
	try {
	    PersonAttribute result = service.savePersonAttribute(obj);
	    return ResponseEntity.created(new URI("/api/personattribute/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create new PersonAttributeType")
    @PostMapping("/personattributetype")
    public ResponseEntity<?> createPersonAttributeType(@RequestBody PersonAttributeType obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create person attribute type: {}", obj);
	try {
	    PersonAttributeType result = service.savePersonAttributeType(obj);
	    return ResponseEntity.created(new URI("/api/personattributetype/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Delete Person")
    @DeleteMapping("/person/{uuid}")
    public ResponseEntity<?> deletePerson(@PathVariable String uuid) {
	LOG.info("Request to delete person: {}", uuid);
	try {
	    service.deletePerson(service.getPersonByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete a Person Attribute")
    @DeleteMapping("/personattribute/{uuid}")
    public ResponseEntity<?> deletePersonAttribute(@PathVariable String uuid) {
	LOG.info("Request to delete person attribute: {}", uuid);
	try {
	    service.deletePersonAttribute(service.getPersonAttributeByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete a LocationAttributeType")
    @DeleteMapping("/personattributetype/{uuid}")
    public ResponseEntity<?> deletePersonAttributeType(@PathVariable String uuid) {
	return notImplementedResponse(PersonAttributeType.class.getName());
    }

    @ApiOperation(value = "Get People by Address")
    @GetMapping(value = "/people/address", params = { "address", "cityVillage", "stateProvince", "country" })
    public ResponseEntity<?> getPeopleByAddress(@RequestParam("address") String address,
	    @RequestParam("cityVillage") String cityVillage, @RequestParam("stateProvince") String stateProvince,
	    @RequestParam("country") String country) throws HibernateException {
	List<Person> list = service.getPeopleByAddress(address, cityVillage, stateProvince, country);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse("Search by Address");
    }

    @ApiOperation(value = "Get People by Contact")
    @GetMapping(value = "/people/contact", params = { "contact", "primaryContactOnly" })
    public ResponseEntity<?> getPeopleByContact(@RequestParam("contact") String contact,
	    @RequestParam("primaryContactOnly") Boolean primaryContactOnly) throws HibernateException {
	if (primaryContactOnly == null) {
	    primaryContactOnly = Boolean.TRUE;
	}
	List<Person> list = service.getPeopleByContact(contact, primaryContactOnly);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse("Search by Contact");
    }

    @ApiOperation(value = "Get People by name")
    @GetMapping("/people/name/{name}")
    public ResponseEntity<?> getPeopleByName(@PathVariable String name) {
	List<Person> list = service.getPeopleByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get Person By UUID")
    @GetMapping("/person/{uuid}")
    public ResponseEntity<?> getPerson(@PathVariable String uuid) {
	Person obj = service.getPersonByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get PersonAttribute by UUID")
    @GetMapping("/personattribute/{uuid}")
    public ResponseEntity<?> getPersonAttribute(@PathVariable String uuid) {
	PersonAttribute obj = service.getPersonAttributeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get PersonAttributes by Location")
    @GetMapping("/personattributes/person/{uuid}")
    public ResponseEntity<?> getPersonAttributesByLocation(@PathVariable String uuid) {
	Person person = service.getPersonByUuid(uuid);
	List<PersonAttribute> list = service.getPersonAttributesByPerson(person);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get PersonAttributeType By UUID")
    @GetMapping("/personattributetype/{uuid}")
    public ResponseEntity<?> getPersonAttributeType(@PathVariable String uuid) {
	PersonAttributeType obj = service.getPersonAttributeTypeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get PersonAttributeType by name")
    @GetMapping("/personattributetype/name/{name}")
    public ResponseEntity<?> getPersonAttributeTypeByName(@PathVariable String name) {
	PersonAttributeType obj = service.getPersonAttributeTypeByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get PersonAttributeType by short name")
    @GetMapping("/personattributetype/shortname/{shortName}")
    public ResponseEntity<?> getPersonAttributeTypeByShortName(@PathVariable String shortName) {
	PersonAttributeType obj = service.getPersonAttributeTypeByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all PersonAttributeTypes")
    @GetMapping("/personattributetypes")
    public Collection<?> getPersonAttributeTypes() {
	return service.getAllPersonAttributeTypes();
    }

    @ApiOperation(value = "Get Person By ID")
    @GetMapping("/person/id/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Integer id) {
	Person obj = service.getPersonById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Update existing Person")
    @PutMapping("/person/{uuid}")
    public ResponseEntity<?> updatePerson(@PathVariable String uuid, @Valid @RequestBody Person obj) {
	Person found = service.getPersonByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setPersonId(found.getPersonId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update person: {}", obj);
	return ResponseEntity.ok().body(service.updatePerson(obj));
    }

    @ApiOperation(value = "Update existing PersonAttribute")
    @PutMapping("/personattribute/{uuid}")
    public ResponseEntity<?> updatePersonAttribute(@PathVariable String uuid, @Valid @RequestBody PersonAttribute obj) {
	PersonAttribute found = service.getPersonAttributeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeId(found.getAttributeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update person attribute: {}", obj);
	return ResponseEntity.ok().body(service.updatePersonAttribute(obj));
    }

    @ApiOperation(value = "Update existing PersonAttributeType")
    @PutMapping("/personattributetype/{uuid}")
    public ResponseEntity<?> updatePersonAttributeType(@PathVariable String uuid,
	    @Valid @RequestBody PersonAttributeType obj) {
	PersonAttributeType found = service.getPersonAttributeTypeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeTypeId(found.getAttributeTypeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update person attribute type: {}", obj);
	return ResponseEntity.ok().body(service.updatePersonAttributeType(obj));
    }
}
