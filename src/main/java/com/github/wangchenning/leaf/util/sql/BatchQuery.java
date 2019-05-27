package com.github.wangchenning.leaf.util.sql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wangchenning.leaf.util.json.JsonObj;
import com.github.wangchenning.leaf.util.random.RandomUtil;
import com.github.wangchenning.leaf.util.sql.MSQL.SQL_Type;

public class BatchQuery extends AbstractQuery {

	Logger logger = LoggerFactory.getLogger(BatchQuery.class);
	public BatchQuery(MSQLGroup sqlGroup) {
		super(SQL_Type.BATCH, sqlGroup); 
	}

	public static class BATCH_SQL extends MSQL {

		BATCH_SQL(String name, String sql, String[] params) {
			super(name, sql, params);
		}
		
	}
	
	
	/**
	 * 对于插入，如不传入id参数则自动生成（String类型）,不过仍需在yaml配置文件params中声明id
	 * @throws SQLException 
	 */
	@Override
	public void batchInsert(DataSource dataSource, JsonObj args) throws SQLException {
		doBatch(dataSource, args, (map) ->{
			if (map.containsKey("id")) {
				return;
			} 
			map.put("id", RandomUtil.getId());
		});
	}
	
	
	
	@Override
	public void batchUpdate(DataSource dataSource, JsonObj args) throws SQLException {
		doBatch(dataSource, args, (map) -> {
			//不做特殊处理
		});
	}
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> handlerArgs(Object argsValue) {
		return (List<Map<String, Object>>) argsValue;
	}
	
	private void doBatch(DataSource dataSource, JsonObj args, Consumer<Map<String, Object>> idHandler) throws SQLException {
		try (Connection conn = dataSource.getConnection()) {
			conn.setAutoCommit(false);
			conn.setReadOnly(false);
			Iterator<MSQL> it = this.sqlGroup.iterator();
			try {
				while (it.hasNext()) {
					BATCH_SQL batchSQL = (BATCH_SQL) it.next();
					PreparedStatement ps = conn.prepareStatement(batchSQL.sql);
					List<Map<String, Object>> paramsList = handlerArgs(args.getProperty(batchSQL.name));
					for (Map<String, Object> params : paramsList) {
						idHandler.accept(params);
						int i = 1; 
						for (String param : batchSQL.params) {
							Object value = params.get(param);
							if (null == value) {
								throw new IllegalArgumentException("参数中需包括: " + param);
							}
							ps.setObject(i, value);
							i++;
						}
						ps.addBatch();
					}
					int[] res = ps.executeBatch();
					for (int i : res) {
						logger.info("update: " + i);
					}
				}
				conn.commit();
			} catch (Exception e) {
				logger.error("批量更新失败," + e.toString());
				conn.rollback();
				throw e;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("获取数据库连接失败" + e.toString());
			throw e;
		}
	}
}
