package com.github.wangchenning.leaf.util.sql;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.sql.MSQL.SQL_Type;

public class UpdateQuery extends AbstractQuery {

	private final static Logger logger  = LoggerFactory.getLogger(UpdateQuery.class);
	protected UpdateQuery(MSQLGroup sqlGroup) {
		super(SQL_Type.UPDATE, sqlGroup);
		check();
	}
	
	public static class UPDATE_SQL extends MSQL {
		UPDATE_SQL(String name, String sql, String[] params) {
			super(name, sql, params);
		}
	}
	private void check() {
		for (MSQL msql : sqlGroup) {
			UPDATE_SQL sql = (UPDATE_SQL) msql;
			int indexOfFrom = sql.sql.indexOf("update");
			int idx = indexOfFrom == -1 ? sql.sql.indexOf("UPDATE") : indexOfFrom;
			if (idx == -1) {
				throw new IllegalArgumentException("sql 中必须包含update或UPDATE" + sql.sql);
			}
		}
	}


	@Deprecated
	@Override
	public void update(EntityManager em, JsonObj args) {
		super.update(em, args);
	}


	@Override
	public void update(DataSource dataSource, JsonObj args) throws SQLException {
		super.update(dataSource, args);
	}
	
	

	
}
