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
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.dto.LocationAttributeDto;
import com.ihsinformatics.coronavirus.dto.LocationAttributePackageDto;
import com.ihsinformatics.coronavirus.dto.LocationDesearlizeDto;
import com.ihsinformatics.coronavirus.dto.LocationDto;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.LocationAttribute;
import com.ihsinformatics.coronavirus.model.LocationAttributeType;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.util.RegexUtil;
import com.ihsinformatics.coronavirus.util.SearchCriteria;
import com.ihsinformatics.coronavirus.util.SearchOperator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Location Controller")
public class LocationController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LocationService service;

    @Autowired
    private MetadataService metadataService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DonorService donorService;

    @ApiOperation(value = "Create new Location")
    @PostMapping("/location")
    public ResponseEntity<?> createLocation(@RequestBody Location obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location: {}", obj);
	try {
	    Location result = service.saveLocation(obj);
	    return ResponseEntity.created(new URI("/api/location/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create new LocationAttribute")
    @PostMapping("/locationattribute")
    public ResponseEntity<?> createLocationAttribute(@RequestBody LocationAttribute obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location attribute: {}", obj);
	try {
	    LocationAttribute result = service.saveLocationAttribute(obj);
	    return ResponseEntity.created(new URI("/api/locationattribute/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create a set of new LocationAttributes. Caution! Should be called only to add new attributes to an existing location.")
    @PostMapping("/locationattributes")
    public ResponseEntity<?> createLocationAttributes(@RequestBody LocationAttributePackageDto obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location attributes: {}", obj);
	try {
	    List<LocationAttributeDto> attributes = obj.getAttributes();
	    List<LocationAttribute> locationAttributes = new ArrayList<>();
	    for (LocationAttributeDto attribute : attributes) {
		locationAttributes.add(attribute.toLocationAttribute(service));
	    }
	    service.saveLocationAttributes(locationAttributes);
	    Location location = locationAttributes.get(0).getLocation();
	    return ResponseEntity.created(new URI("/api/location/" + location.getUuid())).body(location);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Create new LocationAttributeType")
    @PostMapping("/locationattributetype")
    public ResponseEntity<?> createLocationAttributeType(@RequestBody LocationAttributeType obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location attribute type: {}", obj);
	try {
	    LocationAttributeType result = service.saveLocationAttributeType(obj);
	    return ResponseEntity.created(new URI("/api/locationattributetype/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Void Location")
    @DeleteMapping("/location/{uuid}")
    public ResponseEntity<?> voidLocation(@PathVariable String uuid, @RequestParam("reasonVoided")String reasonVoided) {
	LOG.info("Request to delete participant: {}", uuid);
	try {
		Location location = uuid.matches(RegexUtil.UUID) ? service.getLocationByUuid(uuid)
				: service.getLocationById(Integer.parseInt(uuid));
		if(location == null)
			return noEntityFoundResponse(uuid);
		location.setReasonVoided(reasonVoided);
	    service.voidLocation(location);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.ok().body("SUCCESS");
    }
    
    @ApiOperation(value = "Restore Location")
    @PatchMapping("/location/{uuid}")
    public ResponseEntity<?> unvoidLocation(@PathVariable String uuid) {
	LOG.info("Request to restore location: {}", uuid);
	try {
		Location location = uuid.matches(RegexUtil.UUID) ? service.getLocationByUuid(uuid)
				: service.getLocationById(Integer.parseInt(uuid));
		if(location == null)
			return noEntityFoundResponse(uuid);
	    Location obj = service.unvoidLocation(location);
	    return ResponseEntity.ok().body(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
    }

    @ApiOperation(value = "Delete a Location Attribute")
    @DeleteMapping("/locationattribute/{uuid}")
    public ResponseEntity<?> deleteLocationAttribute(@PathVariable String uuid) {
	LOG.info("Request to delete location attribute: {}", uuid);
	service.deleteLocationAttribute(service.getLocationAttributeByUuid(uuid));
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Delete a LocationAttributeType")
    @DeleteMapping("/locationattributetype/{uuid}")
    public ResponseEntity<?> deleteLocationAttributeType(@PathVariable String uuid) {
	return notImplementedResponse(LocationAttributeType.class.getName());
    }

    @ApiOperation(value = "Get Location By UUID")
    @GetMapping("/location/{uuid}")
    public ResponseEntity<?> getLocation(@PathVariable String uuid) {
	Location obj = service.getLocationByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get LocationAttribute by UUID")
    @GetMapping("/locationattribute/{uuid}")
    public ResponseEntity<?> getLocationAttribute(@PathVariable String uuid) {
	LocationAttribute obj = service.getLocationAttributeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get LocationAttribute By ID")
    @GetMapping("/locationattribute/id/{id}")
    public ResponseEntity<?> getLocationAttributeById(@PathVariable Integer id) {
	LocationAttribute obj = service.getLocationAttributeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get LocationAttributes by Location")
    @GetMapping("/locationattributes/location/{uuid}")
    public ResponseEntity<?> getLocationAttributesByLocation(@PathVariable String uuid) {
	Location location = uuid.matches(RegexUtil.UUID) ? service.getLocationByUuid(uuid)
		: service.getLocationByShortName(uuid);
	List<LocationAttribute> list = service.getLocationAttributesByLocation(location);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get LocationAttributeType By UUID")
    @GetMapping("/locationattributetype/{uuid}")
    public ResponseEntity<?> getLocationAttributeType(@PathVariable String uuid) {
	LocationAttributeType obj = service.getLocationAttributeTypeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get LocationAttributeType By ID")
    @GetMapping("/locationattributetype/id/{id}")
    public ResponseEntity<?> getLocationAttributeTypeById(@PathVariable Integer id) {
	LocationAttributeType obj = service.getLocationAttributeTypeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get LocationAttributeType by name")
    @GetMapping("/locationattributetype/name/{name}")
    public ResponseEntity<?> getLocationAttributeTypeByName(@PathVariable String name) {
	LocationAttributeType obj = service.getLocationAttributeTypeByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get LocationAttributeType by short name")
    @GetMapping("/locationattributetype/shortname/{shortName}")
    public ResponseEntity<?> getLocationAttributeTypeByShortName(@PathVariable String shortName) {
	LocationAttributeType obj = service.getLocationAttributeTypeByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all LocationAttributeTypes")
    @GetMapping("/locationattributetypes")
    public Collection<?> getLocationAttributeTypes() {
	return service.getAllLocationAttributeTypes();
    }

    @ApiOperation(value = "Get Location By ID")
    @GetMapping("/location/id/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Integer id) {
	Location obj = service.getLocationById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Location by short name")
    @GetMapping("/location/shortname/{shortName}")
    public ResponseEntity<?> getLocationByShortName(@PathVariable String shortName) {
	Location obj = service.getLocationByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get list of all Locations (lightweight objects)")
    @GetMapping("/location/list")
    public List<LocationDto> getLocationList() {
	List<Location> list = service.getAllLocations();
	List<LocationDto> locations = new ArrayList<>();
	for (Location location : list) {
	    locations.add(new LocationDto(location.getLocationId(), location.getLocationName(), location.getShortName(),
		    location.getUuid(), location.getCategory().getUuid(), location.getIsVoided(), location.getReasonVoided()));
	}
	return locations;
    }

    @ApiOperation(value = "Get all Locations")
    @GetMapping("/locations")
    public Collection<?> getLocations() {
	return service.getAllLocations();
    }

    @ApiOperation(value = "Get Locations by Address")
    @GetMapping(value = "/locations/address", params = { "address", "cityVillage", "stateProvince", "country" })
    public ResponseEntity<?> getLocationsByAddress(@RequestParam("address") String address,
	    @RequestParam("cityVillage") String cityVillage, @RequestParam("stateProvince") String stateProvince,
	    @RequestParam("country") String country) throws HibernateException {
	List<Location> list = service.getLocationsByAddress(address, cityVillage, stateProvince, country);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse("Search by Address");
    }

    @ApiOperation(value = "Get Locations by category")
    @GetMapping("/locations/category/{uuid}")
    public ResponseEntity<?> getLocationsByCategory(@PathVariable String uuid) {
    	
	List<Definition> category = new ArrayList<>();	
    if(uuid.matches(RegexUtil.UUID))	
    	category.add(metadataService.getDefinitionByUuid(uuid));
    else {
    	category = metadataService.getDefinitionByShortName(uuid);
    }
    		
    List<Location> list = new ArrayList<>();
    
    for(Definition c: category)
    	list.addAll(service.getLocationsByCategory(c));
   
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Locations by Contact")
    @GetMapping(value = "/locations/contact", params = { "contact", "primaryContactOnly" })
    public ResponseEntity<?> getLocationsByContact(@RequestParam("contact") String contact,
	    @RequestParam("primaryContactOnly") Boolean primaryContactOnly) throws HibernateException {
	if (primaryContactOnly == null) {
	    primaryContactOnly = Boolean.TRUE;
	}
	List<Location> list = service.getLocationsByContact(contact, primaryContactOnly);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse("Search by Contact");
    }

    @ApiOperation(value = "Get Locations by name")
    @GetMapping("/locations/name/{name}")
    public ResponseEntity<?> getLocationsByName(@PathVariable String name) {
	List<Location> list = service.getLocationsByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get Locations by parent Location")
    @GetMapping("/locations/parent/{uuid}")
    public ResponseEntity<?> getLocationsByParent(@PathVariable String uuid) {
	Location parent = uuid.matches(RegexUtil.UUID) ? service.getLocationByUuid(uuid)
		: service.getLocationByShortName(uuid);
	List<Location> list = service.getLocationsByParent(parent);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Locations by various parameters")
    @GetMapping(value = "/locations/search", params = { "category", "parent", "landmark1", "landmark2", "cityVillage",
	    "stateProvince", "country", "primaryContact", "primaryContactPerson", "secondaryContact",
	    "secondaryContactPerson", "email" })
    public ResponseEntity<?> searchLocations(@RequestParam("category") String categoryUuid,
	    @RequestParam("parent") String parentUuid, @RequestParam("landmark1") String landmark1,
	    @RequestParam("landmark2") String landmark2, @RequestParam("cityVillage") String cityVillage,
	    @RequestParam("stateProvince") String stateProvince, @RequestParam("country") String country,
	    @RequestParam("primaryContact") String primaryContact,
	    @RequestParam("primaryContactPerson") String primaryContactPerson,
	    @RequestParam("secondaryContact") String secondaryContact,
	    @RequestParam("secondaryContactPerson") String secondaryContactPerson, @RequestParam("email") String email)
	    throws HibernateException {
	List<SearchCriteria> params = new ArrayList<>();
	if (!"".equals(categoryUuid)) {
	    /*Definition category = categoryUuid.matches(RegexUtil.UUID)
		    ? metadataService.getDefinitionByUuid(categoryUuid)
		    : metadataService.getDefinitionByShortName(categoryUuid);*/
		Definition category = metadataService.getDefinitionByUuid(categoryUuid);
	    params.add(new SearchCriteria("category", SearchOperator.EQUALS, category));
	}
	if (!"".equals(parentUuid)) {
	    Location parent = parentUuid.matches(RegexUtil.UUID) ? service.getLocationByUuid(parentUuid)
		    : service.getLocationByShortName(parentUuid);
	    params.add(new SearchCriteria("parentLocation", SearchOperator.EQUALS, parent));
	}
	if (!"".equals(landmark1)) {
	    params.add(new SearchCriteria("landmark1", SearchOperator.LIKE, landmark1));
	}
	if (!"".equals(landmark2)) {
	    params.add(new SearchCriteria("landmark2", SearchOperator.LIKE, landmark2));
	}
	if (!"".equals(cityVillage)) {
	    params.add(new SearchCriteria("cityVillage", SearchOperator.LIKE, cityVillage));
	}
	if (!"".equals(stateProvince)) {
	    params.add(new SearchCriteria("stateProvince", SearchOperator.LIKE, stateProvince));
	}
	if (!"".equals(country)) {
	    params.add(new SearchCriteria("country", SearchOperator.EQUALS, country));
	}
	if (!"".equals(primaryContact)) {
	    params.add(new SearchCriteria("primaryContact", SearchOperator.EQUALS, primaryContact));
	}
	if (!"".equals(primaryContactPerson)) {
	    params.add(new SearchCriteria("primaryContactPerson", SearchOperator.LIKE, primaryContactPerson));
	}
	if (!"".equals(secondaryContact)) {
	    params.add(new SearchCriteria("secondaryContact", SearchOperator.EQUALS, secondaryContact));
	}
	if (!"".equals(secondaryContactPerson)) {
	    params.add(new SearchCriteria("secondaryContactPerson", SearchOperator.LIKE, secondaryContactPerson));
	}
	if (!"".equals(email)) {
	    params.add(new SearchCriteria("email", SearchOperator.EQUALS, email));
	}
	List<Location> list = service.searchLocations(params);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(Arrays.toString(params.toArray()));
    }

    @ApiOperation(value = "Update existing Location")
    @PutMapping("/location/{uuid}")
    public ResponseEntity<?> updateLocation(@PathVariable String uuid, @Valid @RequestBody Location obj) {
	Location found = service.getLocationByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setLocationId(found.getLocationId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update location: {}", obj);
	return ResponseEntity.ok().body(service.updateLocation(obj));
    }

    @ApiOperation(value = "Update existing LocationAttribute")
    @PutMapping("/locationattribute/{uuid}")
    public ResponseEntity<?> updateLocationAttribute(@PathVariable String uuid,
	    @Valid @RequestBody LocationAttribute obj) {
	LocationAttribute found = service.getLocationAttributeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeId(found.getAttributeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update location attribute: {}", obj);
	return ResponseEntity.ok().body(service.updateLocationAttribute(obj));
    }

    @ApiOperation(value = "Update existing LocationAttributeType")
    @PutMapping("/locationattributetype/{uuid}")
    public ResponseEntity<?> updateLocationAttributeType(@PathVariable String uuid,
	    @Valid @RequestBody LocationAttributeType obj) {
	LocationAttributeType found = service.getLocationAttributeTypeByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setAttributeTypeId(found.getAttributeTypeId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update location attribute type: {}", obj);
	return ResponseEntity.ok().body(service.updateLocationAttributeType(obj));
    }
    
    @ApiOperation(value = "Get Location With Dicipher Data By UUID")
    @GetMapping("/location/full/{uuid}")
    public ResponseEntity<?> getLocationDesearlizeDto(@PathVariable String uuid) {
    	LocationDesearlizeDto found = null;
		try {
			found = service.getLocationDesearlizeDtoUuid(uuid, service, metadataService, userService, donorService);
		} catch (HibernateException e) {
			return noEntityFoundResponse(uuid);
		} 
    	if (found == null) {
    		return noEntityFoundResponse(uuid);
    	}
    	return ResponseEntity.ok().body(found);
    }
}
