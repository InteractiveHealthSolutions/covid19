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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ihsinformatics.coronavirus.model.Donor;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Donor Controller")
public class DonorController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DonorService service;

    @ApiOperation(value = "Create New Donor")
    @PostMapping("/donor")
    public ResponseEntity<?> createDonor(@RequestBody Donor obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create donor: {}", obj);
	try {
	    Donor result = service.saveDonor(obj);
	    return ResponseEntity.created(new URI("/api/donor/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Void Donor")
    @DeleteMapping("/donor/{uuid}")
    public ResponseEntity<?> voidProject(@PathVariable String uuid, @RequestParam("reasonVoided")String reasonVoided) {
	LOG.info("Request to delete donor: {}", uuid);
	try {
		Donor donor = uuid.matches(RegexUtil.UUID) ? service.getDonorByUuid(uuid)
				: service.getDonorById(Integer.parseInt(uuid));
		if(donor == null)
			return noEntityFoundResponse(uuid);
		donor.setReasonVoided(reasonVoided);
	    service.voidDonor(donor);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.ok().body("SUCCESS");
    }
    
    @ApiOperation(value = "Restore Donor")
    @PatchMapping("/donor/{uuid}")
    public ResponseEntity<?> unvoidDonor(@PathVariable String uuid) {
	LOG.info("Request to restore donor: {}", uuid);
	try {
		Donor donor = uuid.matches(RegexUtil.UUID) ? service.getDonorByUuid(uuid)
				: service.getDonorById(Integer.parseInt(uuid));
		if(donor == null)
			return noEntityFoundResponse(uuid);
	    Donor obj = service.unvoidDonor(donor);
	    return ResponseEntity.ok().body(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
    }

    @ApiOperation(value = "Get Donor By UUID")
    @GetMapping("/donor/{uuid}")
    public ResponseEntity<?> getDonor(@PathVariable String uuid) {
	Donor obj = service.getDonorByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Donor By ID")
    @GetMapping("/donor/id/{id}")
    public ResponseEntity<?> getDonorById(@PathVariable Integer id) {
	Donor obj = service.getDonorById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Donor by short name")
    @GetMapping("/donor/shortname/{shortName}")
    public ResponseEntity<?> getDonorByShortName(@PathVariable String shortName) {
	Donor obj = service.getDonorByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all Donors")
    @GetMapping("/donors")
    public Collection<?> getDonors() {
	return service.getAllDonors();
    }

    @ApiOperation(value = "Get Donors by name")
    @GetMapping("/donors/name/{name}")
    public ResponseEntity<?> getDonorsByName(@PathVariable String name) {
	List<Donor> list = service.getDonorsByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Update existing Donor")
    @PutMapping("/donor/{uuid}")
    public ResponseEntity<?> updateDonor(@PathVariable String uuid, @Valid @RequestBody Donor obj) {
	Donor found = service.getDonorByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setDonorId(found.getDonorId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update donor: {}", obj);
	return ResponseEntity.ok().body(service.updateDonor(obj));
    }
}
