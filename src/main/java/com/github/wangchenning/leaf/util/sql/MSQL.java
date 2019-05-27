package com.github.wangchenning.leaf.util.sql;
public abstract class MSQL {
	static enum SQL_Type {
		SELECT,
		UPDATE,
		INSERT,
		DELETE,
		BATCH
	}
	private static final String[] EMPTY = new String[0];
	protected final String name;
	protected final String sql;
	protected final String[] params;
	MSQL(String name, String sql, String params[]) {
		this.name = name;
		this.sql = sql;
		this.params = params;
	}
	public static String[] EMPTY_PARAMS() {
		return EMPTY;
	}
}
