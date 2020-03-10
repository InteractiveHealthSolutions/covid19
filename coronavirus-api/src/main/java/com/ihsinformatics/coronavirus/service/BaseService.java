/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ihsinformatics.coronavirus.model.BaseEntity;
import com.ihsinformatics.coronavirus.model.DataEntity;
import com.ihsinformatics.coronavirus.model.MetadataEntity;
import com.ihsinformatics.coronavirus.repository.DefinitionRepository;
import com.ihsinformatics.coronavirus.repository.DefinitionTypeRepository;
import com.ihsinformatics.coronavirus.repository.DonorRepository;
import com.ihsinformatics.coronavirus.repository.ElementRepository;
import com.ihsinformatics.coronavirus.repository.FormDataRepository;
import com.ihsinformatics.coronavirus.repository.FormTypeRepository;
import com.ihsinformatics.coronavirus.repository.LocationAttributeRepository;
import com.ihsinformatics.coronavirus.repository.LocationAttributeTypeRepository;
import com.ihsinformatics.coronavirus.repository.LocationRepository;
import com.ihsinformatics.coronavirus.repository.ParticipantRepository;
import com.ihsinformatics.coronavirus.repository.PersonAttributeRepository;
import com.ihsinformatics.coronavirus.repository.PersonAttributeTypeRepository;
import com.ihsinformatics.coronavirus.repository.PersonRepository;
import com.ihsinformatics.coronavirus.repository.PrivilegeRepository;
import com.ihsinformatics.coronavirus.repository.ProjectRepository;
import com.ihsinformatics.coronavirus.repository.RoleRepository;
import com.ihsinformatics.coronavirus.repository.UserAttributeRepository;
import com.ihsinformatics.coronavirus.repository.UserAttributeTypeRepository;
import com.ihsinformatics.coronavirus.repository.UserRepository;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Service
public class BaseService {

    @Autowired
    protected DefinitionRepository definitionRepository;

    @Autowired
    protected DefinitionTypeRepository definitionTypeRepository;

    @Autowired
    protected DonorRepository donorRepository;

    @Autowired
    protected ElementRepository elementRepository;

    @Autowired
    protected FormDataRepository formDataRepository;

    @Autowired
    protected FormTypeRepository formTypeRepository;

    @Autowired
    protected LocationAttributeRepository locationAttributeRepository;

    @Autowired
    protected LocationAttributeTypeRepository locationAttributeTypeRepository;

    @Autowired
    protected LocationRepository locationRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected PersonRepository personRepository;

    @Autowired
    protected PersonAttributeRepository personAttributeRepository;

    @Autowired
    protected PersonAttributeTypeRepository personAttributeTypeRepository;

    @Autowired
    protected PrivilegeRepository privilegeRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserAttributeRepository userAttributeRepository;

    @Autowired
    protected UserAttributeTypeRepository userAttributeTypeRepository;

    @Autowired
    protected UserRepository userRepository;
    
    @Autowired
    protected ParticipantService participantService;
    
    @Autowired
    protected PersonService personService;
    
    @Autowired
    protected FormService formService;
    
    @Autowired
    private SecurityService securityService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
	return entityManager;
    }

    /**
     * Returns a hibernate session from {@link EntityManager}
     * 
     * @return
     */
    public Session getSession() {
	return entityManager.unwrap(Session.class);
    }

    /**
     * Sets the audit fields while creating a new object
     * 
     * @param obj
     * @return
     */
    public BaseEntity setCreateAuditAttributes(BaseEntity obj) {
	if (obj instanceof DataEntity) {
	    ((DataEntity) obj).setCreatedBy(securityService.getAuditUser());
	}
	return obj;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
	this.entityManager = entityManager;
    }

    /**
     * Sets the audit fields while voiding/retiring an object
     * 
     * @param obj
     * @return
     */
    public BaseEntity setSoftDeleteAuditAttributes(BaseEntity obj) {
	if (obj instanceof DataEntity) {
	    ((DataEntity) obj).setVoidedBy(securityService.getAuditUser());
	    ((DataEntity) obj).setDateVoided(new Date());
	} else if (obj instanceof MetadataEntity) {
	    ((MetadataEntity) obj).setDateRetired(new Date());
	}
	return obj;
    }

    /**
     * Sets the audit fields while updating an existing object
     * 
     * @param obj
     * @return
     */
    public BaseEntity setUpdateAuditAttributes(BaseEntity obj) {
	if (obj instanceof DataEntity) {
	    ((DataEntity) obj).setUpdatedBy(securityService.getAuditUser());
	    ((DataEntity) obj).setDateUpdated(new Date());
	} else if (obj instanceof MetadataEntity) {
	    ((MetadataEntity) obj).setDateUpdated(new Date());
	}
	return obj;
    }
}
