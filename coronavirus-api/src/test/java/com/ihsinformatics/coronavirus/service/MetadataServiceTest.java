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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.ihsinformatics.coronavirus.BaseServiceTest;
import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.DefinitionType;
import com.ihsinformatics.coronavirus.model.Element;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class MetadataServiceTest extends BaseServiceTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	super.reset();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#deleteDefinition(com.ihsinformatics.coronavirus.model.Definition)}.
     */
    @Test
    public void shouldDeleteDefinition() {
	doNothing().when(definitionRepository).delete(any(Definition.class));
	metadataService.deleteDefinition(firebolt);
	verify(definitionRepository, times(1)).delete(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#deleteDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     */
    @Test
    public void shouldDeleteDefinitionType() {
	when(definitionRepository.findByDefinitionType(any(DefinitionType.class))).thenReturn(null);
	doNothing().when(definitionTypeRepository).delete(any(DefinitionType.class));
	metadataService.deleteDefinitionType(broomStick);
	verify(definitionTypeRepository, times(1)).delete(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#deleteElement(com.ihsinformatics.coronavirus.model.Element)}.
     */
    @Test
    public void shouldDeleteElement() {
	doNothing().when(elementRepository).delete(any(Element.class));
	metadataService.deleteElement(schoolElement);
	verify(elementRepository, times(1)).delete(any(Element.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getAllDefinitionTypes()}.
     */
    @Test
    public void shouldGetAllDefinitionTypes() {
	when(definitionTypeRepository.findAll()).thenReturn(Arrays.asList(locationType, country));
	assertEquals(2, metadataService.getAllDefinitionTypes().size());
	verify(definitionTypeRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getAllElements()}.
     */
    @Test
    public void shouldGetAllElements() {
	when(elementRepository.findAll()).thenReturn(Arrays.asList(schoolElement, houseElement, broomstickElement));
	assertEquals(3, metadataService.getAllElements().size());
	verify(elementRepository, times(1)).findAll();
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetDefinitionById() {
	Optional<Definition> optional = Optional.of(france);
	when(definitionRepository.findById(any(Integer.class))).thenReturn(optional);
	assertEquals(metadataService.getDefinitionById(1), france);
	verify(definitionRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionByShortName() {
	when(definitionRepository.findByShortName(any(String.class))).thenReturn(Arrays.asList(england));
	assertEquals(1, definitionRepository.findByShortName("england").size());
	verify(definitionRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionByUuid() {
	when(definitionRepository.findByUuid(any(String.class))).thenReturn(england);
	assertEquals(metadataService.getDefinitionByUuid(england.getUuid()), england);
	verify(definitionRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionsByDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     */
    @Test
    public void shouldGetDefinitionsByDefinitionType() {
	when(definitionRepository.findByDefinitionType(any(DefinitionType.class)))
		.thenReturn(Arrays.asList(england, scotland, france));
	assertThat(metadataService.getDefinitionsByDefinitionType(country),
		Matchers.contains(england, scotland, france));
	verify(definitionRepository, times(1)).findByDefinitionType(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionsByName() {
	when(definitionRepository.findByName(any(String.class))).thenReturn(Arrays.asList(england, scotland, france));
	assertThat(metadataService.getDefinitionsByName(england.getDefinitionName()),
		Matchers.contains(england, scotland, france));
	verify(definitionRepository, times(1)).findByName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionTypeById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetDefinitionTypeById() {
	Optional<DefinitionType> optional = Optional.of(country);
	when(definitionTypeRepository.findById(any(Integer.class))).thenReturn(optional);
	assertEquals(metadataService.getDefinitionTypeById(1), country);
	verify(definitionTypeRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionTypeByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionTypeByShortName() {
	when(definitionTypeRepository.findByShortName(any(String.class))).thenReturn(country);
	assertEquals(metadataService.getDefinitionTypeByShortName(england.getShortName()), country);
	verify(definitionTypeRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionTypeByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionTypeByUuid() {
	when(definitionTypeRepository.findByUuid(any(String.class))).thenReturn(country);
	assertEquals(metadataService.getDefinitionTypeByUuid(england.getUuid()), country);
	verify(definitionTypeRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getDefinitionTypesByName(java.lang.String)}.
     */
    @Test
    public void shouldGetDefinitionTypesByName() {
	when(definitionRepository.findByName(any(String.class))).thenReturn(Arrays.asList(england, scotland));
	assertThat(metadataService.getDefinitionsByName("land"), Matchers.contains(england, scotland));
	verify(definitionRepository, times(1)).findByName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getElementById(java.lang.Integer)}.
     */
    @Test
    public void shouldGetElementById() {
	Optional<Element> optional = Optional.of(schoolElement);
	when(elementRepository.findById(any(Integer.class))).thenReturn(optional);
	assertEquals(metadataService.getElementById(1), schoolElement);
	verify(elementRepository, times(1)).findById(any(Integer.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getElementByShortName(java.lang.String)}.
     */
    @Test
    public void shouldGetElementByShortName() {
	when(elementRepository.findByShortName(any(String.class))).thenReturn(schoolElement);
	assertEquals(metadataService.getElementByShortName(schoolElement.getShortName()), schoolElement);
	verify(elementRepository, times(1)).findByShortName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getElementByUuid(java.lang.String)}.
     */
    @Test
    public void shouldGetElementByUuid() {
	when(elementRepository.findByUuid(any(String.class))).thenReturn(schoolElement);
	assertEquals(metadataService.getElementByUuid(schoolElement.getShortName()), schoolElement);
	verify(elementRepository, times(1)).findByUuid(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#getElementsByName(java.lang.String)}.
     */
    @Test
    public void shouldGetElementsByName() {
	when(elementRepository.findByName(any(String.class)))
		.thenReturn(Arrays.asList(schoolElement, broomstickElement, captainElement));
	assertThat(metadataService.getElementsByName("Name"),
		Matchers.contains(schoolElement, broomstickElement, captainElement));
	verify(elementRepository, times(1)).findByName(any(String.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#deleteDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     */
    @Test
    public void shouldNotDeleteDefinitionType() {
	when(definitionRepository.findByDefinitionType(any(DefinitionType.class)))
		.thenReturn(Arrays.asList(scotland, france, england));
	doNothing().when(definitionTypeRepository).delete(any(DefinitionType.class));
	metadataService.deleteDefinitionType(locationType);
	verify(definitionTypeRepository, times(1)).delete(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#saveDefinition(com.ihsinformatics.coronavirus.model.Definition)}.
     */
    @Test
    public void shouldSaveDefinition() {
	when(definitionRepository.save(any(Definition.class))).thenReturn(scotland);
	assertThat(metadataService.saveDefinition(scotland), is(scotland));
	verify(definitionRepository, times(1)).save(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#saveDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     */
    @Test
    public void shouldSaveDefinitionType() {
	when(definitionTypeRepository.save(any(DefinitionType.class))).thenReturn(locationType);
	assertThat(metadataService.saveDefinitionType(locationType), is(locationType));
	verify(definitionTypeRepository, times(1)).save(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#saveElement(com.ihsinformatics.coronavirus.model.Element)}.
     */
    @Test
    public void shouldSaveElement() {
	when(elementRepository.save(any(Element.class))).thenReturn(schoolElement);
	assertThat(metadataService.saveElement(schoolElement), is(schoolElement));
	verify(elementRepository, times(1)).save(any(Element.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#updateDefinition(com.ihsinformatics.coronavirus.model.Definition)}.
     */
    @Test
    public void shouldUpdateDefinition() {
	when(definitionRepository.save(any(Definition.class))).thenReturn(england);
	england = metadataService.updateDefinition(england);
	assertNotNull(england.getDateUpdated());
	verify(definitionRepository, times(1)).save(any(Definition.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#updateDefinitionType(com.ihsinformatics.coronavirus.model.DefinitionType)}.
     */
    @Test
    public void shouldUpdateDefinitionType() {
	when(definitionTypeRepository.save(any(DefinitionType.class))).thenReturn(country);
	country = metadataService.updateDefinitionType(country);
	assertNotNull(country.getDateUpdated());
	verify(definitionTypeRepository, times(1)).save(any(DefinitionType.class));
    }

    /**
     * Test method for
     * {@link com.ihsinformatics.coronavirus.service.MetadataServiceImpl#updateElement(com.ihsinformatics.coronavirus.model.Element)}.
     */
    @Test
    public void shouldUpdateElement() {
	when(elementRepository.save(any(Element.class))).thenReturn(schoolElement);
	schoolElement = metadataService.updateElement(schoolElement);
	assertNotNull(schoolElement.getDateUpdated());
	verify(elementRepository, times(1)).save(any(Element.class));
    }
}
