/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.web;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import com.ihsinformatics.coronavirus.dto.FormDataDesearlizeDto;
import com.ihsinformatics.coronavirus.dto.FormDataDto;
import com.ihsinformatics.coronavirus.dto.FormDataListDto;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.service.DonorService;
import com.ihsinformatics.coronavirus.service.FormService;
import com.ihsinformatics.coronavirus.service.LocationService;
import com.ihsinformatics.coronavirus.service.MetadataService;
import com.ihsinformatics.coronavirus.service.ParticipantService;
import com.ihsinformatics.coronavirus.service.SecurityService;
import com.ihsinformatics.coronavirus.service.UserService;
import com.ihsinformatics.coronavirus.util.DateTimeUtil;
import com.ihsinformatics.coronavirus.util.RegexUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */
@RestController
@RequestMapping("/api")
@Api(value = "Form Controller")
public class FormController extends BaseController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FormService service;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private MetadataService metadataService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DonorService donorService;
    

    @ApiOperation(value = "Create new FormData")
    @PostMapping("/formdata")
    public ResponseEntity<?> createFormData(@RequestBody FormData obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create form data: {}", obj);
	try {
	    if ("".equals(obj.getReferenceId())) {
		obj.setReferenceId(
			createReferenceId(securityService.getAuditUser(), obj.getLocation(), obj.getFormDate()));
	    }
	    FormData result = service.saveFormData(obj);
	    return ResponseEntity.created(new URI("/api/formdata/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    /**
     * This resource was provided only on strong demand from Tahira
     * 
     * @param input
     * @return
     * @throws URISyntaxException
     * @throws AlreadyBoundException
     * @deprecated because the resources expect an Entity object
     */
    @ApiOperation(value = "Create new FormData")
    @PostMapping("/formdatastream")
    @Deprecated
    public ResponseEntity<?> createFormDataAsJson(InputStream input) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location attributes via direct input stream.");
	try {
	    FormDataDto obj = new FormDataDto(inputStreamToJson(input), service, locationService, participantService);
	    if ("".equals(obj.getReferenceId())) {
		obj.setReferenceId(createReferenceId(securityService.getAuditUser(),
			locationService.getLocationByUuid(obj.getLocationUuid()), new Date()));
	    }
	    FormData result = service.saveFormData(obj.toFormData(service, locationService, participantService));
	    return ResponseEntity.created(new URI("/api/formdata/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object is input stream", e);
	}
    }
    
    
    /**
     * This resource was provided only on strong demand from Tahira
     * 
     * @param input
     * @return
     * @throws URISyntaxException
     * @throws AlreadyBoundException
     * @deprecated because the resources expect an Entity object
     */
    @ApiOperation(value = "Create new FormData")
    @PutMapping("/formdatastream")
    @Deprecated
    public ResponseEntity<?> updateFormDataAsJson(InputStream input) throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create location attributes via direct input stream.");
	try {
	    FormDataDto obj = new FormDataDto(inputStreamToJson(input), service, locationService, participantService);
	    FormData formdata = obj.toFormData(service, locationService, participantService);
	    
	    FormData found = service.getFormDataByUuid(formdata.getUuid());
		if (found == null) {
		    return noEntityFoundResponse(formdata.getUuid());
		}
	    formdata.setFormId(found.getFormId());
		LOG.info("Request to update form data: {}", formdata);		
		return ResponseEntity.ok().body(service.updateFormData(formdata));
	    
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object is input stream", e);
	}
    }
    
    @ApiOperation(value = "Get FormData With Dicipher Data By UUID")
    @GetMapping("/formdata/full/{uuid}")
    public ResponseEntity<?> getFormDataDesearlizeDto(@PathVariable String uuid) {
    	FormDataDesearlizeDto found = null;
		try {
			found = service.getFormDataDesearlizeDtoUuid(uuid, locationService, participantService, metadataService, userService, donorService);
		} catch (HibernateException e) {
			return noEntityFoundResponse(uuid);
		} 
    	if (found == null) {
    		return noEntityFoundResponse(uuid);
    	}
    	return ResponseEntity.ok().body(found);
    }

    @ApiOperation(value = "Create new FormType")
    @PostMapping("/formtype")
    public ResponseEntity<?> createFormType(@RequestBody FormType obj)
	    throws URISyntaxException, AlreadyBoundException {
	LOG.info("Request to create form type: {}", obj);
	try {
	    FormType result = service.saveFormType(obj);
	    return ResponseEntity.created(new URI("/api/formtype/" + result.getUuid())).body(result);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    /**
     * Returns a reference ID from given {@link FormData} object for auto assignment
     * 
     * @param obj
     * @return
     */
    public String createReferenceId(User user, Location location, Date date) {
	StringBuilder referenceId = new StringBuilder();
	if (user != null) {
	    referenceId.append(user.getUserId());
	    referenceId.append("-");
	}
	if (location != null) {
	    referenceId.append(location.getLocationId());
	    referenceId.append("-");
	}
	referenceId.append(DateTimeUtil.toString(date, DateTimeUtil.SQL_TIMESTAMP));
	return referenceId.toString();
    }

    @ApiOperation(value = "Get FormData By UUID")
    @GetMapping("/formdata/{uuid}")
    public ResponseEntity<?> getFormData(@PathVariable String uuid) {
	FormData obj = service.getFormDataByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get FormData by Date range - Paging")
    @GetMapping(value = "/formdata/date", params = { "from", "to", "page", "size" })
    public ResponseEntity<?> getFormDataByDatePaging(@RequestParam("from") String from, @RequestParam("to") String to,
	    @RequestParam("page") Integer page, @RequestParam("size") Integer size) {
	List<FormData> list = service.getFormDataByDate(DateTimeUtil.fromSqlDateString(from),
		DateTimeUtil.fromSqlDateString(to), page, size, "formDate", Boolean.TRUE);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(from + ", " + to);
    }
    
    @ApiOperation(value = "Get FormData by Date range")
    @GetMapping(value = "/formdata/list/date", params = { "from", "to" })
    public ResponseEntity<?> getFormDataByDate(@RequestParam("from") String from, @RequestParam("to") String to) {
	List<FormData> list = service.getFormDataByDate(DateTimeUtil.fromSqlDateString(from), DateTimeUtil.fromSqlDateString(to));
	if (!list.isEmpty()) {
		
		List<FormDataListDto> formDataDto = new ArrayList<>();
		for (FormData formData : list) {
			formDataDto.add(new FormDataListDto(formData.getFormId(), formData.getUuid(), formData.getFormType().getFormName(), formData.getFormType().getUuid(), ((formData.getFormType().getFormGroup() == null ) ? null : formData.getFormType().getFormGroup().getDefinitionName()),
					((formData.getLocation() == null) ? null : formData.getLocation().getUuid()), ((formData.getLocation() == null) ? null : formData.getLocation().getLocationName()), ((formData.getLocation() == null) ? null : formData.getLocation().getLocationId()), formData.getFormDate(), formData.getDateCreated(),
					formData.getDateUpdated(), ((formData.getCreatedBy() == null) ? null : formData.getCreatedBy().getFullName()), ((formData.getUpdatedBy() == null) ? null : formData.getUpdatedBy().getFullName()), formData.getIsVoided(), formData.getReasonVoided()));
		}
		
	    return ResponseEntity.ok().body(formDataDto);
	}
	return noEntityFoundResponse(from + ", " + to);
    }

    @ApiOperation(value = "Get FormData By ID")
    @GetMapping("/formdata/id/{id}")
    public ResponseEntity<?> getFormDataById(@PathVariable Integer id) {
	FormData obj = service.getFormDataById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get FormData By UUID")
    @GetMapping("/formdata/location/{uuid}")
    public ResponseEntity<?> getFormDataByLocation(@PathVariable String uuid) {
	Location location = uuid.matches(RegexUtil.UUID) ? locationService.getLocationByUuid(uuid)
		: locationService.getLocationByShortName(uuid);
	List<FormData> list = service.getFormDataByLocation(location);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get FormData By Reference ID")
    @GetMapping("/formdata/referenceid/{referenceId}")
    public ResponseEntity<?> getFormDataByReferenceId(@PathVariable String referenceId) {
	FormData obj = service.getFormDataByReferenceId(referenceId);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(referenceId);
    }

    @ApiOperation(value = "Get FormType By UUID")
    @GetMapping("/formtype/{uuid}")
    public ResponseEntity<?> getFormType(@PathVariable String uuid) {
	FormType obj = service.getFormTypeByUuid(uuid);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(uuid);
    }

    @ApiOperation(value = "Get FormType By ID")
    @GetMapping("/formtype/id/{id}")
    public ResponseEntity<?> getFormTypeById(@PathVariable Integer id) {
	FormType obj = service.getFormTypeById(id);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(id.toString());
    }

    @ApiOperation(value = "Get FormType by name/short name")
    @GetMapping("/formtype/name/{name}")
    public ResponseEntity<?> getFormTypeByShortName(@PathVariable String name) {
	FormType obj = service.getFormTypeByName(name);
	if (obj != null) {
	    return ResponseEntity.ok().body(obj);
	}
	return noEntityFoundResponse(name);
    }

    @ApiOperation(value = "Get all FormTypes")
    @GetMapping("/formtypes")
    public Collection<?> getFormTypes() {
	return service.getAllFormTypes(true);
    }

    @ApiOperation(value = "Retire FormType")
    @DeleteMapping("/formtype/{uuid}")
    public ResponseEntity<?> retireFormType(@PathVariable String uuid) {
	LOG.info("Request to retire form type: {}", uuid);
	service.retireFormType(service.getFormTypeByUuid(uuid));
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get FormData by Date range")
    @GetMapping(value = "/formdata/search")
    public ResponseEntity<?> searchFormDataPage(@RequestParam("formType") String formTypeUuid,
	    @RequestParam("location") String locationUuid, @RequestParam("from") String from,
	    @RequestParam("to") String to, @RequestParam("page") Integer page, @RequestParam("size") Integer size)
	    throws HibernateException {
	FormType formType = service.getFormTypeByUuid(formTypeUuid);
	Location location = locationService.getLocationByUuid(locationUuid);
	Date fromDate = DateTimeUtil.fromSqlDateString(from);
	Date toDate = DateTimeUtil.fromSqlDateString(to);
	List<FormData> list = service.searchFormData(formType, location, fromDate, toDate, page, size, "formDate",
		true);
	if (!list.isEmpty()) {
	    return ResponseEntity.ok().body(list);
	}
	return noEntityFoundResponse(from + ", " + to);
    }

    @ApiOperation(value = "Get FormData by Date range")
    @GetMapping(value = "/formdata/list/search")
    public ResponseEntity<?> searchFormData(@RequestParam(required = false, name="formType") String formTypeUuid,
	    @RequestParam(required = false, name="location") String locationUuid, @RequestParam(required = false, name="formGroup") String formGroupUuid, @RequestParam("from") String from,
	    @RequestParam("to") String to)
	    throws HibernateException {
	FormType formType = service.getFormTypeByUuid(formTypeUuid);
	Location location = locationService.getLocationByUuid(locationUuid);
	Definition component = metadataService.getDefinitionByUuid(formGroupUuid);
	Date fromDate = DateTimeUtil.fromSqlDateString(from);
	Date toDate = DateTimeUtil.fromSqlDateString(to);
	List<FormData> list = service.searchFormData(formType, location, component, fromDate, toDate, "formDate",
		true);
	if (!list.isEmpty()) {
		List<FormDataListDto> formDataDto = new ArrayList<>();
		for (FormData formData : list) {
			formDataDto.add(new FormDataListDto(formData.getFormId(), formData.getUuid(), formData.getFormType().getFormName(), formData.getFormType().getUuid(), ((formData.getFormType().getFormGroup() == null ) ? null : formData.getFormType().getFormGroup().getDefinitionName()),
					((formData.getLocation() == null) ? null : formData.getLocation().getUuid()), ((formData.getLocation() == null) ? null : formData.getLocation().getLocationName()), ((formData.getLocation() == null) ? null : formData.getLocation().getLocationId()), formData.getFormDate(), formData.getDateCreated(),
					formData.getDateUpdated(), ((formData.getCreatedBy() == null) ? null : formData.getCreatedBy().getFullName()), ((formData.getUpdatedBy() == null) ? null : formData.getUpdatedBy().getFullName()), formData.getIsVoided(), formData.getReasonVoided()));
		}
		
	    return ResponseEntity.ok().body(formDataDto);
	}
	return noEntityFoundResponse(from + ", " + to);
    }

    
    @ApiOperation(value = "Restore FormType")
    @PatchMapping("/formtype/{uuid}")
    public ResponseEntity<?> unretireFormType(@PathVariable String uuid) {
	LOG.info("Request to restore form type: {}", uuid);
	try {
	    service.unretireFormType(service.getFormTypeByUuid(uuid));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
	return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Restore FormData")
    @PatchMapping("/formdata/{uuid}")
    public ResponseEntity<?> unvoidFormData(@PathVariable String uuid) {
	LOG.info("Request to restore form data: {}", uuid);
	try {
		FormData formData = uuid.matches(RegexUtil.UUID) ? service.getFormDataByUuid(uuid)
				: service.getFormDataById(Integer.parseInt(uuid));
		if(formData == null)
			return noEntityFoundResponse(uuid);
	    FormData obj = service.unvoidFormData(formData);
	    return ResponseEntity.ok().body(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + uuid, e);
	}
    }

    @ApiOperation(value = "Update existing FormData")
    @PutMapping("/formdata/{uuid}")
    public ResponseEntity<?> updateFormData(@PathVariable String uuid, @Valid @RequestBody FormData obj) {
	FormData found = service.getFormDataByUuid(uuid);
	if (found == null) {
	    return noEntityFoundResponse(uuid);
	}
	obj.setFormId(found.getFormId());
	obj.setUuid(found.getUuid());
	LOG.info("Request to update form data: {}", obj);
	try {
	    service.updateFormData(obj);
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
	return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Update existing FormType")
    @PutMapping("/formtype/{uuid}")
    public ResponseEntity<?> updateFormType(@PathVariable String uuid, @Valid @RequestBody FormType obj) {
	obj.setUuid(uuid);
	LOG.info("Request to update form type: {}", obj);
	try {
	    return ResponseEntity.ok().body(service.updateFormType(obj));
	} catch (Exception e) {
	    return exceptionFoundResponse("Reference object: " + obj, e);
	}
    }

    @ApiOperation(value = "Void FormData by uuid/id")
    @DeleteMapping("/formdata/{uuid}")
    public ResponseEntity<?> voidFormData(@PathVariable String uuid, @RequestParam("reasonVoided")String reasonVoided) {
	LOG.info("Request to void form data: {}", uuid);
	FormData formData = uuid.matches(RegexUtil.UUID) ? service.getFormDataByUuid(uuid)
			: service.getFormDataById(Integer.parseInt(uuid));
	if(formData == null)
		return noEntityFoundResponse(uuid);
	formData.setReasonVoided(reasonVoided);
	service.voidFormData(formData);
	return ResponseEntity.ok().body("SUCCESS");
    }
}
