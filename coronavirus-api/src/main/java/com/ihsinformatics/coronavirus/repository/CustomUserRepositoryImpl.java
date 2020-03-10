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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;

import com.ihsinformatics.coronavirus.model.User;
import com.ihsinformatics.coronavirus.util.SearchCriteria;
import com.ihsinformatics.coronavirus.util.SearchQueryCriteriaConsumer;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<User> search(List<SearchCriteria> params) {
	if (params == null) {
	    return new ArrayList<>();
	}
	if (params.isEmpty()) {
	    return new ArrayList<>();
	}
	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	CriteriaQuery<User> query = builder.createQuery(User.class);
	Root<User> r = query.from(User.class);
	Predicate predicate = builder.conjunction();
	SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, builder, r);
	params.stream().forEach(searchConsumer);
	predicate = searchConsumer.getPredicate();
	query.where(predicate);
	return entityManager.createQuery(query).getResultList();
    }
}
