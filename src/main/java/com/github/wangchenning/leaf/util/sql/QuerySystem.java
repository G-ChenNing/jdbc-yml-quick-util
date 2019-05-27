package com.github.wangchenning.leaf.util.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.github.wangchenning.leaf.util.sql.SelectQuery.SELECT_SQL;
public class QuerySystem {
	private final static Logger logger = LoggerFactory.getLogger(QuerySystem.class);
	private final static ObjectMapper om = new ObjectMapper(new YAMLFactory());
	private final Map<String, MQuery> queryMap;
	public static class QuerySystemBuilder {
		private QuerySystem querySystem;
		public QuerySystemBuilder() {
			querySystem = new QuerySystem();
		}
		public QuerySystem build() {
			if (querySystem.queryMap.isEmpty()) {
				throw new IllegalStateException("还未添加参数");
			}
			return querySystem;
		}
		public QuerySystemBuilder add(InputStream yaml) throws JsonProcessingException, IOException {
			querySystem.init(yaml);
			return this;
		}
		/**
		 * 采用默认配置
		 * @return
		 * @throws IOException 
		 * @throws FileNotFoundException 
		 */
		public static QuerySystem createQuerySystem() throws FileNotFoundException, IOException {
			QuerySystem qs = new QuerySystem();
			String path = URLDecoder.decode(QuerySystemBuilder.class.getResource("/").getPath(),"utf-8");
			String modulesFileName = URLDecoder.decode(path + File.separator + "module.properties","utf-8");

//			System.out.println(modulesFileName);
			try (InputStream moduleProperties = new FileInputStream(new File(modulesFileName))) {
				Properties p = new Properties();
				p.load(moduleProperties);
				String modules = p.getProperty("name");
				assert null != modules && !modules.isEmpty();
			    for (String moduleName : modules.split(",")) {
			    	String moduleFile = path + moduleName;
			    	logger.info("加载模块: " + moduleName);
			    	try (InputStream moduleYaml = new FileInputStream(new File(moduleFile))) {
			    		qs.init(moduleYaml);
			    	}
			    }
			}
			return qs;
		}
	}
	
	private QuerySystem() {
		queryMap = new HashMap<>();
	}
	
	
	private QuerySystem init(InputStream yaml) throws JsonProcessingException, IOException {
		try {
			JsonNode root = om.readTree(yaml);
			for (JsonNode sql : root) {
				String id = sql.get("id").asText();
				String type = sql.get("type").asText().toUpperCase();
				JsonNode sqlsNode = sql.get("sqls");
				MSQLGroup sqlGroup = MSQLGroup.EMPTY();
			    MSQL.SQL_Type msql_type = MSQL.SQL_Type.valueOf(type);
			    Iterator<Entry<String, JsonNode>> fields = sqlsNode.fields();
			    while (fields.hasNext()) {
			    	Entry<String, JsonNode> e = fields.next();
			    	JsonNode selectSql = e.getValue();
			    	String sqlStr = selectSql.get("sql").asText();
			    	JsonNode paramsNode = selectSql.get("params");
			    	String[] params;
			    	if (null == paramsNode) {
			    		params = MSQL.EMPTY_PARAMS();
			    	} else {
			    		params = paramsNode.asText().split(",");
			    	}
			    	MSQL msql;
			    	String sqlName = e.getKey();
					switch (msql_type) {
			    	case SELECT : {
			    		String fieldNamesAsText = selectSql.get("field_names").asText();
			    		String[] fieldNames = fieldNamesAsText.split(",");
			    		msql = new SELECT_SQL(sqlName, sqlStr, params, fieldNames);
			    		break;
			    	}
			    	case INSERT : {
			    			msql = new CreateQuery.CREATE_SQL(sqlName, sqlStr, params);
			    			break;
			    		}
			    	case UPDATE : {
			    		msql = new UpdateQuery.UPDATE_SQL(sqlName, sqlStr, params);
			    		break;
			    	}
			    	case DELETE : {
			    		msql = new DeleteQuery.DELETE_SQL(sqlName, sqlStr, params);
			    		break;
			    	}
			    	case BATCH : {
			    		msql = new BatchQuery.BATCH_SQL(sqlName, sqlStr, params);
			    		break;
			    	}
					default:
						throw new IllegalArgumentException("不支持的类型: " + msql_type);
			    	}
			    	sqlGroup.add(msql);
			    }
			    queryMap.put(id, AbstractQuery.concreteSQLQuery(id, msql_type, sqlGroup));
			}
			return this;
		} finally {
			yaml.close();
		}
	}
	
	public static QuerySystem instance(InputStream yaml) throws JsonProcessingException, IOException {
		return new QuerySystem().init(yaml);
	}
	
	public MQuery getQuery(String id) {
		if (!queryMap.containsKey(id)) {
			throw new IllegalArgumentException("未定义id为:" + id + "的sql");
		}
		return queryMap.get(id);
	}
	
	public Set<String> ids() {
		return queryMap.keySet();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		QuerySystemBuilder.createQuerySystem();
	}
}
