package com.github.wangchenning.leaf.util.sql;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.random.RandomUtil;

public class CreateQuery extends AbstractQuery implements MQuery {
	private final static Logger logger = LoggerFactory.getLogger(CreateQuery.class);
	public CreateQuery(MSQLGroup sqlGroup) {
		super(MSQL.SQL_Type.INSERT, sqlGroup);
		check();
	}

	public static class CREATE_SQL extends MSQL {

		public CREATE_SQL(String name, String sql, String[] params) {
			super(name, sql, params);
		}
		
	}
	
	private void check() {
		for (MSQL sql : sqlGroup) {
			int indexOfFrom = sql.sql.indexOf("insert");
			int idx = indexOfFrom == -1 ? sql.sql.indexOf("INSERT") : indexOfFrom;
			if (idx == -1) {
				throw new IllegalArgumentException("sql 中必须包含insert或INSERT" + sql.sql);
			}
		}
	}
	@Deprecated
	@Override
	public String create(EntityManager em, JsonObj args) {
		if (null == args.getProperty("id")) {
			String id = RandomUtil.getId();
			args.putProperty("id", id);
		}
		changeDB(em, args);
		return args.getProperty("id").toString();
	}
	@Override
	public String create(DataSource dataSource, JsonObj args) throws SQLException {
		if (null == args.getProperty("id")) {
			String id = RandomUtil.getId();
			args.putProperty("id", id);
		}
		changeDB(dataSource, args);
		return args.getProperty("id").toString();
	}

	
}
