/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Component;

import com.ihsinformatics.coronavirus.annotation.CheckPrivilege;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Element;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Component
public class MetadataServiceImpl extends BaseService implements MetadataService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#deleteDefinition(com
     * .ihsinformatics.coronavirus.model.Definition)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Definition")
    public void deleteDefinition(Definition obj) throws HibernateException {
	definitionRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#deleteDefinitionType
     * (com.ihsinformatics.coronavirus.model.DefinitionType)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Metadata")
    public void deleteDefinitionType(DefinitionType obj) throws HibernateException {
	definitionTypeRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#deleteElement(com.
     * ihsinformatics.coronavirus.model.Element)
     */
    @Override
    @CheckPrivilege(privilege = "Delete Element")
    public void deleteElement(Element obj) {
	elementRepository.delete(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getAllDefinitionTypes()
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public List<DefinitionType> getAllDefinitionTypes() {
	return definitionTypeRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#getElements()
     */
    @Override
    @CheckPrivilege(privilege = "View Element")
    public List<Element> getAllElements() {
	return elementRepository.findAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getDefinitionById(
     * java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Definition")
    public Definition getDefinitionById(Integer id) {
	Optional<Definition> found = definitionRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionByShortName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Definition")
    public List<Definition> getDefinitionByShortName(String shortName) {
	return definitionRepository.findByShortName(shortName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getDefinitionByUuid(
     * java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Definition")
    public Definition getDefinitionByUuid(String uuid) {
	return definitionRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionsByDefinitionType(com.ihsinformatics.coronavirus.model.
     * DefinitionType)
     */
    @Override
    @CheckPrivilege(privilege = "View Definition")
    public List<Definition> getDefinitionsByDefinitionType(DefinitionType definitionType) {
	return definitionRepository.findByDefinitionType(definitionType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getDefinitionsByName
     * (java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Definition")
    public List<Definition> getDefinitionsByName(String name) {
	return definitionRepository.findByName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionTypeById(java.lang.Integer)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public DefinitionType getDefinitionTypeById(Integer id) {
	Optional<DefinitionType> found = definitionTypeRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionTypeByShortName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public DefinitionType getDefinitionTypeByShortName(String shortName) {
	return definitionTypeRepository.findByShortName(shortName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionTypeByUuid(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public DefinitionType getDefinitionTypeByUuid(String uuid) {
	return definitionTypeRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getDefinitionTypesByName(java.lang.String)
     */
    @Override
    @CheckPrivilege(privilege = "View Metadata")
    public List<DefinitionType> getDefinitionTypesByName(String name) {
	return definitionTypeRepository.findByName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getElementById(java.
     * lang.Integer)
     */
    @Override
    public Element getElementById(Integer id) {
	Optional<Element> found = elementRepository.findById(id);
	if (found.isPresent()) {
	    return found.get();
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.service.MetadataService#
     * getElementByShortName(java.lang.String)
     */
    @Override
    public Element getElementByShortName(String name) {
	return elementRepository.findByShortName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getElementByUuid(
     * java.lang.String)
     */
    @Override
    public Element getElementByUuid(String uuid) {
	return elementRepository.findByUuid(uuid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getElementsByName(
     * java.lang.String)
     */
    @Override
    public List<Element> getElementsByName(String name) {
	return elementRepository.findByName(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getObjectById(java.
     * lang.Class, java.lang.Integer)
     */
    @Override
    public Serializable getObjectById(Class<?> clazz, Integer id) {
	return (Serializable) getEntityManager().find(clazz, id);
    }

    /**
     * Returns a {@link Serializable} object by class name and generated Id
     * 
     * @param className
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public Serializable getObjectById(String className, Integer id) throws ClassNotFoundException {
	return getObjectById(Class.forName(className), id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#getObjectByUuid(java
     * .lang.Class, java.lang.String)
     */
    public Serializable getObjectByUuid(Class<?> clazz, String uuid) {
	CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
	CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(clazz);
	Root<?> root = criteriaQuery.from(clazz);
	criteriaQuery.where(criteriaBuilder.equal(root.get("uuid"), uuid));
	TypedQuery<?> query = getEntityManager().createQuery(criteriaQuery);
	return (Serializable) query.getSingleResult();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#saveDefinition(com.
     * ihsinformatics.coronavirus.model.Definition)
     */
    @Override
    @CheckPrivilege(privilege = "Add Definition")
    public Definition saveDefinition(Definition obj) {
	obj = (Definition) setCreateAuditAttributes(obj);
	return definitionRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#saveDefinitionType(
     * com.ihsinformatics.coronavirus.model.DefinitionType)
     */
    @Override
    @CheckPrivilege(privilege = "Add Metadata")
    public DefinitionType saveDefinitionType(DefinitionType obj) {
	obj = (DefinitionType) setCreateAuditAttributes(obj);
	return definitionTypeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#saveElement(com.
     * ihsinformatics.coronavirus.model.Element)
     */
    @Override
    @CheckPrivilege(privilege = "Add Element")
    public Element saveElement(Element obj) {
	obj = (Element) setCreateAuditAttributes(obj);
	return elementRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#updateDefinition(com
     * .ihsinformatics.coronavirus.model.Definition)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Definition")
    public Definition updateDefinition(Definition obj) {
	obj = (Definition) setUpdateAuditAttributes(obj);
	return definitionRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#updateDefinitionType
     * (com.ihsinformatics.coronavirus.model.DefinitionType)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Metadata")
    public DefinitionType updateDefinitionType(DefinitionType obj) {
	obj = (DefinitionType) setUpdateAuditAttributes(obj);
	return definitionTypeRepository.save(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.service.MetadataService#updateElement(com.
     * ihsinformatics.coronavirus.model.Element)
     */
    @Override
    @CheckPrivilege(privilege = "Edit Element")
    public Element updateElement(Element obj) {
	obj = (Element) setUpdateAuditAttributes(obj);
	return elementRepository.save(obj);
    }
}
