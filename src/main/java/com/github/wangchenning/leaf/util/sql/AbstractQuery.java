package com.github.wangchenning.leaf.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.sql.MSQL.SQL_Type;
import com.github.wangchenning.leaf.util.sql.SelectQuery.SELECT_SQL;
public abstract class AbstractQuery implements MQuery {
	private final static Logger logger = LoggerFactory.getLogger(AbstractQuery.class);
	protected final SQL_Type type;
	protected final MSQLGroup sqlGroup;
//	protected final  DBManager dbManager;
	protected AbstractQuery(SQL_Type type, MSQLGroup sqlGroup) {
		this.type = type;
		this.sqlGroup = sqlGroup;
//		this.dbManager = dbManager;
		check();
	}
	public static MQuery concreteSQLQuery(String id, SQL_Type sql_type, MSQLGroup sqlGroup) {
		switch  (sql_type) {
		case SELECT : return new SelectQuery(sqlGroup);
		case INSERT : return new CreateQuery(sqlGroup);
		case UPDATE : return new UpdateQuery(sqlGroup);
		case DELETE : return new DeleteQuery(sqlGroup);
		case BATCH : return new BatchQuery(sqlGroup);
		default :
			throw new IllegalArgumentException("不支持的类型: " + sql_type);
		}
	}
	
	public static int countCharInStr(String str, char ch) {
		int count = 0;
		for (int i = 0; i < str.length(); ++i) {
			if (str.charAt(i) == ch) {
				count++;
			}
		}
		return count;
	}

	private void check() {
		for (MSQL msql : sqlGroup) {
			int paramsCount = countCharInStr(msql.sql, '?');
			if (paramsCount != msql.params.length) {
				throw new IllegalArgumentException(msql.name + ",参数个数不匹配[" + msql.sql + "]"
						+ "，期望" + paramsCount + "个,实际：" + msql.params.length + "个");
			}
		}
	}
	@Override
	public void delete(EntityManager em, JsonObj args) {
		changeDB(em, args);
	}
	@Override
	public void update(EntityManager em, JsonObj args) {
		changeDB(em, args);
	}

	
	
	@Override
	public void delete(DataSource dataSource, JsonObj args) throws SQLException {
		changeDB(dataSource, args);
	}
	@Override
	public void update(DataSource dataSource, JsonObj args) throws SQLException {
		changeDB(dataSource, args);
	}
	protected void changeDB(DataSource dataSource, JsonObj args) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			conn.setReadOnly(false);
			Iterator<MSQL> it = this.sqlGroup.iterator();
			try {
				while (it.hasNext()) {
					MSQL msql = it.next();
					PreparedStatement ps = conn.prepareStatement(msql.sql);
					int i = 1; 
					for (String param : msql.params) {
						Object value = args.getProperty(param);
						if (null == value) {
							throw new IllegalArgumentException("参数中需包括: " + param);
						}
						ps.setObject(i, value);
						i++;
					}
					int raws = ps.executeUpdate();
					System.out.println(raws + "行数据被更新");
					logger.debug(raws + "行数据被更新");
				}
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
	} catch (SQLException e) {
		throw e;
	}
}
	@Deprecated
	protected void changeDB(EntityManager em, JsonObj args) {
		try {
			for (MSQL msql : sqlGroup) {
				Query q = em.createNativeQuery(msql.sql);
				int i = 1;
				for (String param : msql.params) {
					Object value = args.getProperty(param);
					if (null == value) {
						throw new IllegalArgumentException("参数中需包括: " + param);
					}
					q.setParameter(i, value);
					i++;
				}
				em.getTransaction().begin();
				int rowsAffect = q.executeUpdate();
				em.getTransaction().commit();
				logger.info("执行" + msql.name + rowsAffect + "行受影响");
				logger.debug(q.toString());
			}
		} finally {
			em.close();
		}

	}
	
}
