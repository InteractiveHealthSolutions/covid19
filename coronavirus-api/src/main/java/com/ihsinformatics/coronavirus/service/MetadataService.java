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

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Element;

/**
 * @author owais.hussain@ihsinformatics.com
 */
@Service
public interface MetadataService {

    /**
     * @param obj
     * @throws HibernateException
     */
    void deleteDefinition(Definition obj) throws HibernateException;

    /**
     * @param obj
     * @throws HibernateException
     */
    void deleteDefinitionType(DefinitionType obj) throws HibernateException;

    /**
     * @param obj
     * @throws HibernateException
     */
    void deleteElement(Element obj) throws HibernateException;

    /**
     * Returns list of {@link DefinitionType} all objects
     * 
     * @param name
     * @return
     */
    List<DefinitionType> getAllDefinitionTypes();

    /**
     * Returns list of all {@link Element} objects
     * 
     * @return
     */
    List<Element> getAllElements();

    /**
     * Returns a {@link Definition} object by generated Id
     * 
     * @param id
     * @return
     */
    Definition getDefinitionById(Integer id);

    /**
     * Returns a {@link Definition} object by matching short name
     * 
     * @param shortName
     * @return
     */
    List<Definition> getDefinitionByShortName(String shortName);

    /**
     * Returns a {@link Definition} object by matching UUID
     * 
     * @param uuid
     * @return
     */
    Definition getDefinitionByUuid(String uuid);

    /**
     * Returns list of {@link Definition} objects by {@link DefinitionType}
     * 
     * @param definitionType
     * @return
     */
    List<Definition> getDefinitionsByDefinitionType(DefinitionType definitionType);

    /**
     * Returns list of {@link Definition} objects by matching name
     * 
     * @param name
     * @return
     */
    List<Definition> getDefinitionsByName(String name);

    /**
     * Returns a {@link DefinitionType} object by generated Id
     * 
     * @param id
     * @return
     */
    DefinitionType getDefinitionTypeById(Integer id);

    /**
     * Returns {@link DefinitionType} object by matching short name
     * 
     * @param shortName
     * @return
     */
    DefinitionType getDefinitionTypeByShortName(String shortName);

    /**
     * Returns {@link DefinitionType} object by matching UUID
     * 
     * @param uuid
     * @return
     */
    DefinitionType getDefinitionTypeByUuid(String uuid);

    /**
     * Returns list of {@link DefinitionType} objects by matching name
     * 
     * @param name
     * @return
     */
    List<DefinitionType> getDefinitionTypesByName(String name);

    /**
     * Returns {@link Element} object by generated Id
     * 
     * @param uuid
     * @return
     */
    Element getElementById(Integer id);

    /**
     * Returns {@link Element} object by matching short name
     * 
     * @param name
     * @return
     */
    Element getElementByShortName(String name);

    /**
     * Returns {@link Element} object by matching UUID
     * 
     * @param uuid
     * @return
     */
    Element getElementByUuid(String uuid);

    /**
     * Returns list of all {@link Element} objects by matching name
     * 
     * @param name
     * @return
     */
    List<Element> getElementsByName(String name);

    /**
     * Returns an object against given class and generated Id
     * 
     * @param clazz
     * @param id
     * @return
     */
    Serializable getObjectById(Class<?> clazz, Integer id);

    /**
     * Returns an object against given class and UUID
     * 
     * @param clazz
     * @param uuid
     * @return
     */
    Serializable getObjectByUuid(Class<?> clazz, String uuid);

    /**
     * @param obj
     * @return
     */
    Definition saveDefinition(Definition obj);

    /**
     * @param obj
     * @return
     */
    DefinitionType saveDefinitionType(DefinitionType obj);

    /**
     * @param obj
     * @return
     */
    Element saveElement(Element obj);

    /**
     * @param obj
     * @return
     */
    Definition updateDefinition(Definition obj);

    /**
     * @param obj
     * @return
     */
    DefinitionType updateDefinitionType(DefinitionType obj);

    /**
     * @param obj
     * @return
     */
    Element updateElement(Element obj);
}
