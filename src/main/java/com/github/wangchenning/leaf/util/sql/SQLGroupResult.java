package com.github.wangchenning.leaf.util.sql;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wangchenning.leaf.util.json.JsonUtil;
public class SQLGroupResult {
	private final Map<String, Stream<Map<String, Object>>> groupResult;
	private SQLGroupResult() {
		groupResult = new HashMap<>();
	}
	public static  class SQLGroupResultBuilder {
		private final Map<String, Stream.Builder<Map<String, Object>>> builder;
		public SQLGroupResultBuilder() {
			builder = new HashMap<>();
		}
		/**
		 * must insure that inintResult  method called before
		 * @param sqlName
		 * @param res
		 * @return
		 */
		public SQLGroupResultBuilder addResult(String sqlName, Map<String, Object> res) {
			builder.get(sqlName).add(res);
			return this;
		}
		/**
		 * must be called, and only call once
		 * @param sqlName
		 * @return
		 */
		public SQLGroupResultBuilder initResult(String sqlName) {
			builder.put(sqlName, Stream.builder());
			return this;
		}
		public SQLGroupResult build() {
			SQLGroupResult sqlGroupResult = new SQLGroupResult();
			builder.entrySet().forEach(e -> {
				sqlGroupResult.groupResult.put(e.getKey(), e.getValue().build());
			});
			return sqlGroupResult;
		}
	}
	public List<Map<String, Object>> getSQLResult(String sqlName) {
		return groupResult.get(sqlName).collect(Collectors.toList());
	}
	public Stream<Map<String, Object>> getSQLResultStream(String sqlName) {
		return groupResult.get(sqlName);
	}
	public String getJSON() {
		Map<String, List<Map<String, Object>>> json = new HashMap<>();
		groupResult.entrySet().forEach(e -> {
			json.put(e.getKey(), e.getValue().collect(Collectors.toList()));
		});
		try {
			return JsonUtil.getJsonStr(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
