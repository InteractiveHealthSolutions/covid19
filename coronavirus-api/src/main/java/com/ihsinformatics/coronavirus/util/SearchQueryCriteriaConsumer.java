/* Copyright(C) 2019 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/

package com.ihsinformatics.coronavirus.util;

/**
 * @author rabbia.hassan@ihsinformatics.com
 */

import java.util.function.Consumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchQueryCriteriaConsumer implements Consumer<SearchCriteria> {

    private Predicate predicate;

    private CriteriaBuilder builder;

    private Root<?> root;

    public SearchQueryCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, Root<?> root) {
	super();
	this.predicate = predicate;
	this.builder = builder;
	this.root = root;
    }

    @Override
    public void accept(SearchCriteria param) {
	switch (param.getOperator()) {
	case EQUALS:
	    predicate = builder.and(predicate, builder.equal(root.get(param.getKey()), param.getValue()));
	    break;
	case GREATER_THAN:
	    predicate = builder.and(predicate,
		    builder.greaterThan(root.get(param.getKey()), param.getValue().toString()));
	    break;
	case GREATER_THAN_EQUALS:
	    predicate = builder.and(predicate,
		    builder.greaterThanOrEqualTo(root.get(param.getKey()), param.getValue().toString()));
	    break;
	case LESS_THAN:
	    predicate = builder.and(predicate, builder.lessThan(root.get(param.getKey()), param.getValue().toString()));
	    break;
	case LESS_THAN_EQUALS:
	    predicate = builder.and(predicate,
		    builder.lessThanOrEqualTo(root.get(param.getKey()), param.getValue().toString()));
	    break;
	case LIKE:
	    predicate = builder.and(predicate, builder.like(root.get(param.getKey()), "%" + param.getValue() + "%"));
	    break;
	case NOT_EQUALS:
	    predicate = builder.and(predicate, builder.notEqual(root.get(param.getKey()), param.getValue()));
	    break;
	case NOT_LIKE:
	    predicate = builder.and(predicate, builder.notLike(root.get(param.getKey()), "%" + param.getValue() + "%"));
	    break;
	default:
	    break;
	}
    }

    public Predicate getPredicate() {
	return predicate;
    }
}
