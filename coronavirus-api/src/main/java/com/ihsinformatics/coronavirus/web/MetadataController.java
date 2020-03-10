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
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Element;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Metadata Controller")
public class MetadataController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataService service;

    @ApiOperation(value = "Create New Definition")
    @PostMapping("/definition")
    public ResponseEntity<?> createDefinition(@RequestBody Definition obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create definition: {}", obj);
	try {
	    Definition result = service.saveDefinition(obj);
	    return ResponseEntity.created(new URI("/api/definition/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create New DefinitionType")
    @PostMapping("/definitiontype")
    public ResponseEntity<?> createDefinitionType(@RequestBody DefinitionType obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create definition type: {}", obj);
	try {
	    DefinitionType result = service.saveDefinitionType(obj);
	    return ResponseEntity.created(new URI("/api/definitiontype/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create New Element")
    @PostMapping("/element")
    public ResponseEntity<?> createElement(@RequestBody Element obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create element: {}", obj);
	try {
	    Element result = service.saveElement(obj);
	    return ResponseEntity.created(new URI("/api/element/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Delete Definition")
    @DeleteMapping("/definition/{uuid}")
    public ResponseEntity<?> deleteDefinition(@PathVariable String uuid) {
	LOG.info("Request to delete definition: {}", uuid);
	try {
	    service.deleteDefinition(service.getDefinitionByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete DefinitionType")
    @DeleteMapping("/definitiontype/{uuid}")
    public ResponseEntity<?> deleteDefinitionType(@PathVariable String uuid) {
	return notImplementedResponse(DefinitionType.class.getName());
    }

    @ApiOperation(value = "Delete Element")
    @DeleteMapping("/element/{uuid}")
    public ResponseEntity<?> deleteElement(@PathVariable String uuid) {
	LOG.info("Request to delete element: {}", uuid);
	try {
	    service.deleteElement(service.getElementByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get Definition By UUID")
    @GetMapping("/definition/{uuid}")
    public ResponseEntity<?> getDefinition(@PathVariable String uuid) {
	Definition obj = service.getDefinitionByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Definition By ID")
    @GetMapping("/definition/id/{id}")
    public ResponseEntity<?> getDefinitionById(@PathVariable Integer id) {
	Definition obj = service.getDefinitionById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Definition by short name")
    @GetMapping("/definition/shortname/{shortName}")
    public ResponseEntity<?> getDefinitionByShortName(@PathVariable String shortName) {
    	List<Definition> list = service.getDefinitionByShortName(shortName);
    	if (!list.isEmpty()) {
    	    return ResponseEntity.ok().body(list);
    	}
    	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get Definitions by DefinitionType")
    @GetMapping("/definitions/definitiontype/{uuid}")
    public ResponseEntity<?> getDefinitionsByDefinitionType(@PathVariable String uuid) {
	DefinitionType definitionType = uuid.matches(RegexUtil.UUID) ? service.getDefinitionTypeByUuid(uuid)
		: service.getDefinitionTypeByShortName(uuid);
	List<Definition> list = service.getDefinitionsByDefinitionType(definitionType);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Definitions by name")
    @GetMapping("/definitions/name/{name}")
    public ResponseEntity<?> getDefinitionsByName(@PathVariable String name) {
	List<Definition> list = service.getDefinitionsByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get DefinitionType By UUID")
    @GetMapping("/definitiontype/{uuid}")
    public ResponseEntity<?> getDefinitionType(@PathVariable String uuid) {
	DefinitionType obj = service.getDefinitionTypeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get DefinitionType By ID")
    @GetMapping("/definitiontype/id/{id}")
    public ResponseEntity<?> getDefinitionTypeById(@PathVariable Integer id) {
	DefinitionType obj = service.getDefinitionTypeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get DefinitionType by short name")
    @GetMapping("/definitiontype/shortname/{shortName}")
    public ResponseEntity<?> getDefinitionTypeByShortName(@PathVariable String shortName) {
	DefinitionType obj = service.getDefinitionTypeByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all DefinitionTypes")
    @GetMapping("/definitiontypes")
    public Collection<?> getDefinitionTypes() {
	return service.getAllDefinitionTypes();
    }

    @ApiOperation(value = "Get DefinitionTypes by name")
    @GetMapping("/definitiontypes/name/{name}")
    public ResponseEntity<?> getDefinitionTypesByName(@PathVariable String name) {
	List<DefinitionType> list = service.getDefinitionTypesByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get Element By UUID")
    @GetMapping("/element/{uuid}")
    public ResponseEntity<?> getElement(@PathVariable String uuid) {
	Element obj = service.getElementByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Element By ID")
    @GetMapping("/element/id/{id}")
    public ResponseEntity<?> getElementById(@PathVariable Integer id) {
	Element obj = service.getElementById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Element by short name")
    @GetMapping("/element/shortname/{shortName}")
    public ResponseEntity<?> getElementByShortName(@PathVariable String shortName) {
	Element obj = service.getElementByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all Elements")
    @GetMapping("/elements")
    public Collection<?> getElements() {
	return service.getAllElements();
    }

    @ApiOperation(value = "Get Elements by name")
    @GetMapping("/elements/name/{name}")
    public ResponseEntity<?> getElementsByName(@PathVariable String name) {
	List<Element> list = service.getElementsByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Update existing Definition")
    @PutMapping("/definition/{uuid}")
    public ResponseEntity<?> updateDefinition(@PathVariable String uuid, @Valid @RequestBody Definition obj) {
	Definition found = service.getDefinitionByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setDefinitionId(found.getDefinitionId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update definition: {}", obj);
	return ResponseEntity.ok().body(service.updateDefinition(obj));
    }

    @ApiOperation(value = "Update existing DefinitionType")
    @PutMapping("/definitiontype/{uuid}")
    public ResponseEntity<?> updateDefinitionType(@PathVariable String uuid, @Valid @RequestBody DefinitionType obj) {
	DefinitionType found = service.getDefinitionTypeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setDefinitionTypeId(found.getDefinitionTypeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update definition type: {}", obj);
	return ResponseEntity.ok().body(service.updateDefinitionType(obj));
    }

    @ApiOperation(value = "Update existing Element")
    @PutMapping("/element/{uuid}")
    public ResponseEntity<?> updateElement(@PathVariable String uuid, @Valid @RequestBody Element obj) {
	Element found = service.getElementByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setElementId(found.getElementId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update element: {}", obj);
	return ResponseEntity.ok().body(service.updateElement(obj));
    }
}
