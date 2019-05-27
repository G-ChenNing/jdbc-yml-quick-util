package com.github.wangchenning.leaf.util.sql;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public  class MSQLGroup implements Iterable<MSQL> {
	private final List<MSQL> sqlGroup;
	private MSQLGroup() {
		this.sqlGroup = new LinkedList<>();
	}
	public static MSQLGroup EMPTY () {
		return new MSQLGroup();
	}
	public MSQLGroup add(MSQL sql) {
		sqlGroup.add(sql);
		return this;
	}
	@Override
	public Iterator<MSQL> iterator() {
		return sqlGroup.iterator();
	}
}
