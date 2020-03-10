/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.ihsinformatics.coronavirus.Context;
import com.ihsinformatics.coronavirus.model.Location;
import com.ihsinformatics.coronavirus.util.SearchCriteria;
import com.ihsinformatics.coronavirus.util.SearchQueryCriteriaConsumer;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class CustomLocationRepositoryImpl implements CustomLocationRepository {

    @Autowired
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.repository.CustomPersonRepository#
     * findByAddress(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Location> findByAddress(String address, String landmark, String cityVillage, String stateProvince,
	    String country) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Location> criteriaQuery = criteriaBuilder.createQuery(Location.class);
	Root<Location> location = criteriaQuery.from(Location.class);
	Predicate finalPredicate = criteriaBuilder.isNotNull(location.get("uuid"));
	if (address != null) {
	    Predicate address1Predicate = criteriaBuilder.like(location.get("address1"), "%" + address + "%");
	    Predicate address2Predicate = criteriaBuilder.like(location.get("address2"), "%" + address + "%");
	    Predicate address3Predicate = criteriaBuilder.like(location.get("address3"), "%" + address + "%");
	    finalPredicate = criteriaBuilder.or(address1Predicate, address2Predicate, address3Predicate);
	}
	if (landmark != null) {
	    Predicate landmark1Predicate = criteriaBuilder.like(location.get("landmark1"), "%" + landmark + "%");
	    Predicate landmark2Predicate = criteriaBuilder.like(location.get("landmark2"), "%" + landmark + "%");
	    finalPredicate = criteriaBuilder.or(finalPredicate, landmark1Predicate, landmark2Predicate);
	}
	if (cityVillage != null) {
	    Predicate cityVillagePredicate = criteriaBuilder.like(location.get("cityVillage"), "%" + cityVillage + "%");
	    finalPredicate = criteriaBuilder.or(finalPredicate, cityVillagePredicate);
	}
	if (stateProvince != null) {
	    Predicate stateProvincePredicate = criteriaBuilder.like(location.get("stateProvince"),
		    "%" + stateProvince + "%");
	    finalPredicate = criteriaBuilder.or(finalPredicate, stateProvincePredicate);
	}
	if (country != null) {
	    Predicate countryPredicate = criteriaBuilder.equal(location.get("country"), country);
	    criteriaQuery.where(finalPredicate, countryPredicate);
	} else {
	    criteriaQuery.where(finalPredicate);
	}
	TypedQuery<Location> query = entityManager.createQuery(criteriaQuery).setMaxResults(Context.MAX_RESULT_SIZE);
	return query.getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ihsinformatics.coronavirus.repository.CustomPersonRepository#
     * findByContact(java.lang.String, java.lang.Boolean)
     */
    @Override
    public List<Location> findByContact(String contact, Boolean primaryContactOnly) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Location> criteriaQuery = criteriaBuilder.createQuery(Location.class);
	Root<Location> location = criteriaQuery.from(Location.class);
	Predicate contactPredicate = null;
	if (primaryContactOnly) {
	    contactPredicate = criteriaBuilder.like(location.get("primaryContact"), "%" + contact);
	} else {
	    Predicate primaryContactPredicate = criteriaBuilder.like(location.get("primaryContact"), "%" + contact);
	    Predicate secondaryContactPredicate = criteriaBuilder.like(location.get("secondaryContact"), "%" + contact);
	    Predicate tertiaryContactPredicate = criteriaBuilder.like(location.get("tertiaryContact"), "%" + contact);
	    contactPredicate = criteriaBuilder.or(primaryContactPredicate, secondaryContactPredicate,
		    tertiaryContactPredicate);
	}
	criteriaQuery.where(contactPredicate);
	TypedQuery<Location> query = entityManager.createQuery(criteriaQuery).setMaxResults(Context.MAX_RESULT_SIZE);
	return query.getResultList();
    }

    @Override
    public List<Location> search(List<SearchCriteria> params) {
	if (params == null) {
	    return new ArrayList<>();
	}
	if (params.isEmpty()) {
	    return new ArrayList<>();
	}
	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Location> query = builder.createQuery(Location.class);
	Root<Location> r = query.from(Location.class);
	Predicate predicate = builder.conjunction();
	SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, builder, r);
	params.stream().forEach(searchConsumer);
	predicate = searchConsumer.getPredicate();
	query.where(predicate);
	return entityManager.createQuery(query).getResultList();
    }
}
