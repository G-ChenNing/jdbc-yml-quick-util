package com.github.wangchenning.leaf.util.sql;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.sql.MSQL.SQL_Type;

public class DeleteQuery extends AbstractQuery {

	protected DeleteQuery(MSQLGroup sqlGroup) {
		super(SQL_Type.DELETE, sqlGroup);
		check();
	}
	private void check() {
		for (MSQL sql : sqlGroup) {
			int indexOfFrom = sql.sql.indexOf("delete");
			int idx = indexOfFrom == -1 ? sql.sql.indexOf("DELETE") : indexOfFrom;
			if (idx == -1) {
				throw new IllegalArgumentException("sql 中必须包含delete或DELETE" + sql.sql);
			}
		}
	}

	public static class DELETE_SQL extends MSQL {
		DELETE_SQL(String name, String sql, String[] params) {
			super(name, sql, params);
		}
	}

	@Deprecated
	@Override
	public void delete(EntityManager em, JsonObj args) {
		super.delete(em, args);
	}
	@Override
	public void delete(DataSource dataSource, JsonObj args) throws SQLException {
		super.delete(dataSource, args);
	}
	
	
	
}
