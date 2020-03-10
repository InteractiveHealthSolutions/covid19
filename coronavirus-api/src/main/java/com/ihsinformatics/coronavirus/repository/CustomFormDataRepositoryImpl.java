/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.ihsinformatics.coronavirus.model.Definition;
import com.ihsinformatics.coronavirus.model.FormData;
import com.ihsinformatics.coronavirus.model.FormType;
import com.ihsinformatics.coronavirus.model.Location;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class CustomFormDataRepositoryImpl implements CustomFormDataRepository {

    @Autowired
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.repository.CustomFormDataRepository#
     * findByDateRange(java.util.Date, java.util.Date,
     * org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<FormData> findByDateRange(Date from, Date to, Pageable pageable) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<FormData> criteriaQuery = criteriaBuilder.createQuery(FormData.class);
	Root<FormData> formData = criteriaQuery.from(FormData.class);
	Predicate predicate = criteriaBuilder.between(formData.get("formDate"), from, to);
	criteriaQuery.where(predicate);
	TypedQuery<FormData> query = entityManager.createQuery(criteriaQuery);
	int totalRows = query.getResultList().size();
	query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
	query.setMaxResults(pageable.getPageSize());
	Page<FormData> result = new PageImpl<>(query.getResultList(), pageable, totalRows);
	return result;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.repository.CustomFormDataRepository#
     * findByDateRange(java.util.Date, java.util.Date)
     */
    @Override
    public List<FormData> findByDateRange(Date from, Date to) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<FormData> criteriaQuery = criteriaBuilder.createQuery(FormData.class);
	Root<FormData> formData = criteriaQuery.from(FormData.class);
	Predicate predicate = criteriaBuilder.between(formData.get("formDate"), from, to);
	criteriaQuery.where(predicate);
	TypedQuery<FormData> query = entityManager.createQuery(criteriaQuery);
	return query.getResultList();
	
    }
    

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.repository.CustomFormDataRepository#search(
     * com.ihsinformatics.coronavirus.model.FormType,
     * com.ihsinformatics.coronavirus.model.Location, java.util.Date,
     * java.util.Date, org.springframework.data.domain.Pageable)
     */
    @Override
    public Page<FormData> search(FormType formType, Location location, Date from, Date to, Pageable pageable) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<FormData> criteriaQuery = criteriaBuilder.createQuery(FormData.class);
	Root<FormData> formData = criteriaQuery.from(FormData.class);
	Predicate finalPredicate = criteriaBuilder.equal(formData.get("formType"), formType);
	if (location != null) {
	    Predicate locationPredicate = criteriaBuilder.equal(formData.get("location"), location);
	    finalPredicate = criteriaBuilder.and(finalPredicate, locationPredicate);
	}
	if (from != null && to != null) {
	    Predicate datePredicate = criteriaBuilder.between(formData.get("formDate"), from, to);
	    finalPredicate = criteriaBuilder.and(finalPredicate, datePredicate);
	}
	criteriaQuery.where(finalPredicate);
	TypedQuery<FormData> query = entityManager.createQuery(criteriaQuery);
	int totalRows = query.getResultList().size();
	query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
	query.setMaxResults(pageable.getPageSize());
	Page<FormData> result = new PageImpl<>(query.getResultList(), pageable, totalRows);
	return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ihsinformatics.coronavirus.repository.CustomFormDataRepository#search(
     * com.ihsinformatics.coronavirus.model.FormType,
     * com.ihsinformatics.coronavirus.model.Location, java.util.Date,
     * java.util.Date
     */
    @Override
    public List<FormData> search(FormType formType, Location location,Definition formGroup, Date from, Date to) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<FormData> criteriaQuery = criteriaBuilder.createQuery(FormData.class);
	Root<FormData> formData = criteriaQuery.from(FormData.class);
	Predicate finalPredicate = criteriaBuilder.between(formData.get("formDate"), from, to);
	if (location != null) {
	    Predicate locationPredicate = criteriaBuilder.equal(formData.get("location"), location);
	    finalPredicate = criteriaBuilder.and(finalPredicate, locationPredicate);
	}
	if (formType != null) {
	    Predicate formTypePredicate = criteriaBuilder.equal(formData.get("formType"), formType);
	    finalPredicate = criteriaBuilder.and(finalPredicate, formTypePredicate);
	}
	if (formGroup != null) {
	    Predicate componentPredicate = criteriaBuilder.equal(formData.get("formType").get("formGroup"), formGroup);
	    finalPredicate = criteriaBuilder.and(finalPredicate, componentPredicate);
	}
	criteriaQuery.where(finalPredicate);
	TypedQuery<FormData> query = entityManager.createQuery(criteriaQuery);
	return query.getResultList();
    }
}
