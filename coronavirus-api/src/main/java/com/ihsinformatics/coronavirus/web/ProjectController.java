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
import com.ihsinformatics.coronavirus.model.Project;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Project Controller")
public class ProjectController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DonorService service;

    @ApiOperation(value = "Create new Project")
    @PostMapping("/project")
    public ResponseEntity<?> createProject(@RequestBody Project obj) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create project: {}", obj);
	try {
	    Project result = service.saveProject(obj);
	    return ResponseEntity.created(new URI("/api/project/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Void Project")
    @DeleteMapping("/project/{uuid}")
    public ResponseEntity<?> voidProject(@PathVariable String uuid, @RequestParam("reasonVoided")String reasonVoided) {
	LOG.info("Request to delete project: {}", uuid);
	try {
		Project project = uuid.matches(RegexUtil.UUID) ? service.getProjectByUuid(uuid)
				: service.getProjectById(Integer.parseInt(uuid));
		if(project == null)
			return noEntityFoundResponse(uuid);
		project.setReasonVoided(reasonVoided);
	    service.voidProject(project);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.ok().body("SUCCESS");
    }
    
    @ApiOperation(value = "Restore Project")
    @PatchMapping("/project/{uuid}")
    public ResponseEntity<?> unvoidProject(@PathVariable String uuid) {
	LOG.info("Request to restore project: {}", uuid);
	try {
		Project project = uuid.matches(RegexUtil.UUID) ? service.getProjectByUuid(uuid)
				: service.getProjectById(Integer.parseInt(uuid));
		if(project == null)
			return noEntityFoundResponse(uuid);
	    Project obj = service.unvoidProject(project);
		return ResponseEntity.ok().body(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
    }

    @ApiOperation(value = "Get Project By UUID")
    @GetMapping("/project/{uuid}")
    public ResponseEntity<?> getProject(@PathVariable String uuid) {
	Project obj = service.getProjectByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Project By ID")
    @GetMapping("/project/id/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Integer id) {
	Project obj = service.getProjectById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get Project by short name")
    @GetMapping("/project/shortname/{shortName}")
    public ResponseEntity<?> getProjectByShortName(@PathVariable String shortName) {
	Project obj = service.getProjectByShortName(shortName);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(shortName);
    }

    @ApiOperation(value = "Get all Projects")
    @GetMapping("/projects")
    public Collection<?> getProjects() {
	return service.getAllProjects();
    }

    @ApiOperation(value = "Get Projects by Donor")
    @GetMapping("/projects/donor/{uuid}")
    public ResponseEntity<?> getProjectsByDonor(@PathVariable String uuid) {
	Donor donor = uuid.matches(RegexUtil.UUID) ? service.getDonorByUuid(uuid) : service.getDonorByShortName(uuid);
	List<Project> list = service.getProjectsByDonor(donor);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get Projects by name")
    @GetMapping("/projects/name/{name}")
    public ResponseEntity<?> getProjectsByName(@PathVariable String name) {
	List<Project> list = service.getProjectsByName(name);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Update existing Project")
    @PutMapping("/project/{uuid}")
    public ResponseEntity<?> updateProject(@PathVariable String uuid, @Valid @RequestBody Project obj) {
	Project found = service.getProjectByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setProjectId(found.getProjectId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update project: {}", obj);
	return ResponseEntity.ok().body(service.updateProject(obj));
    }
}
