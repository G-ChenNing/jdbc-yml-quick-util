package com.github.wangchenning.leaf.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import com.github.wangchenning.leaf.util.sql.SQLGroupResult.SQLGroupResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.json.JsonUtil;

public class SelectQuery extends AbstractQuery {
	private static final Logger logger = LoggerFactory.getLogger(SelectQuery.class);
	public static class SELECT_SQL extends MSQL {
		private final String[] fieldNames;
		public SELECT_SQL(String name, String sql, String[] params, String[] fieldNames) {
			super(name, sql, params);
			this.fieldNames = fieldNames;
		}
		@Override
		public String toString() {
			return "SELECT_SQL [fieldNames=" + Arrays.toString(fieldNames) + ", name=" + name + ", sql=" + sql
					+ ", params=" + Arrays.toString(params) + "]";
		}
		
	}
	public SelectQuery(MSQLGroup sqlGroup) {
		super(MSQL.SQL_Type.SELECT, sqlGroup);
//		check();
	}
	
	
	private void check() {
		for (MSQL msql : sqlGroup) {
			SELECT_SQL sql = (SELECT_SQL) msql;
			int indexOfFrom = sql.sql.indexOf("from");
			int idx = indexOfFrom == -1 ? sql.sql.indexOf("FROM") : indexOfFrom;
			if (idx == -1) {
				throw new IllegalArgumentException("sql 中必须包含from或FROM");
			}
			int fieldCount = sql.sql.substring(0, idx).split(",").length;
			if (fieldCount != sql.fieldNames.length) {
				throw new IllegalArgumentException("返回值个数不匹配");
			}
		}
	}
	@Override
	public String select(EntityManager em, JsonObj args) throws JsonProcessingException {
		try {
			Map<String, Object> res = new HashMap<>();
			em.getTransaction().begin();
			for (MSQL msql : sqlGroup) {
				SELECT_SQL sql = (SELECT_SQL) msql;
				Query q = em.createNativeQuery(sql.sql);
				int i = 1; 
				for (String param : sql.params) {
					Object value = args.getProperty(param);
					if (null == value) {
						throw new IllegalArgumentException("参数中需包括: " + param);
					}
					q.setParameter(i, value);
					i++;
				}
				List rawDatas = q.getResultList();
				List<Object[]> datas;
				if (sql.fieldNames.length == 1) {
					List<Object> temp = rawDatas;
					datas = temp.stream().map(o -> {
						Object[] os = {o};
						return os;
					}
							).collect(Collectors.toList());
				}
				else {
					datas = rawDatas;
				}
				if (datas.isEmpty()) {
					res.put(msql.name, "");
				} else {
					List<String> dataJson = datas.stream().map(os -> {
						JsonObj jo = JsonObj.instance();
						for (int idx = 0; idx < os.length; ++idx) {
							jo.putProperty(sql.fieldNames[idx], os[idx]);
						}
						return jo.toJson();
					}).collect(Collectors.toList());
					res.put(msql.name, dataJson);
				}
			}
			em.getTransaction().commit();
			return JsonUtil.getJsonStr(res);
		} finally {
			em.close();
		}
	}
	

	@Override
	public SQLGroupResult select(DataSource dataSource, JsonObj args) throws SQLException  {
		try (Connection conn = dataSource.getConnection()) {
				conn.setAutoCommit(false);
				conn.setReadOnly(true);
				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				Iterator<MSQL> it = this.sqlGroup.iterator();
				SQLGroupResultBuilder sqlGroupResultBuilder = new SQLGroupResultBuilder();
				try {
					while (it.hasNext()) {
						SELECT_SQL selectSQL = (SELECT_SQL) it.next();
						String realSql = selectSQL.sql;
						for (int i = 0; i < selectSQL.params.length; i++) {
							if (isInParam(selectSQL.params[i])) { //处理in子句
								realSql = replaceInParam(selectSQL.params.length - i, realSql, ((List)args.getProperty(selectSQL.params[i])).size());
							}
						}
						PreparedStatement ps = conn.prepareStatement(realSql);
						logger.debug("realSQL: " + realSql);
						System.out.println("realSQL: " + realSql);
						int i = 1; 
						for (String param : selectSQL.params) {
							Object value = args.getProperty(param);
							if (null == value) {
								throw new IllegalArgumentException("参数中需包括: " + param);
							}
							if (isInParam(param)) {
								List listValue = (List)value;
								for (Object o : listValue) {
									ps.setObject(i, o);
									i++;
								}
							} else {
								ps.setObject(i, value);
								i++;
							}
						}
						ResultSet rs = ps.executeQuery();
						sqlGroupResultBuilder.initResult(selectSQL.name);
						while (rs.next()) {
							Map<String, Object> rowData = new HashMap<>();
							for (int resIdx = 0; resIdx < selectSQL.fieldNames.length; resIdx++) {
								rowData.put(selectSQL.fieldNames[resIdx], rs.getObject(resIdx + 1));				
							}
							sqlGroupResultBuilder.addResult(selectSQL.name, rowData);
						}
					}
					conn.commit();
					conn.setReadOnly(false);
					return sqlGroupResultBuilder.build();
				} catch (SQLException e) {
					conn.rollback();
					throw e;
				}
		} catch (SQLException e) {
			throw e;
		}
		
	}

	private boolean isInParam(String param) {
		return param.startsWith("in_param");
	}
	/**
	 * 处理in中参数
	 * @param paramIdx 倒序
	 * @param sql
	 * @param realParamCount
	 * @return
	 */
	private String replaceInParam(int paramIdx, String sql, int realParamCount) {
		StringBuilder sb = new StringBuilder();
		int paramCount = 0;
		for (int i = sql.length() - 1; i >= 0; i--) {
			if (sql.charAt(i) == '?') {
				paramCount++;
				if (paramCount == paramIdx) {
					sb.append(sql.substring(0, i));
					while (realParamCount > 0) {
						sb.append("?,");
						realParamCount --;
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.append(sql.substring(i + 1));
				}
			}
		}
		return sb.length() == 0 ? sql : sb.toString();
	}
} 
