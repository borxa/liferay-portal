/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.softwarecatalog.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.softwarecatalog.model.SCProductEntry;

/**
 * @author Brian Wing Shun Chan
 */
public class ProductEntryTypeComparator extends OrderByComparator {

	public static final String ORDER_BY_ASC = "SCProductEntry.type_ ASC";

	public static final String ORDER_BY_DESC = "SCProductEntry.type_ DESC";

	public static final String[] ORDER_BY_FIELDS = {"type"};

	public ProductEntryTypeComparator() {
		this(false);
	}

	public ProductEntryTypeComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		SCProductEntry productEntry1 = (SCProductEntry)obj1;
		SCProductEntry productEntry2 = (SCProductEntry)obj2;

		int value = productEntry1.getType().toLowerCase().compareTo(
			productEntry2.getType().toLowerCase());

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private boolean _ascending;

}