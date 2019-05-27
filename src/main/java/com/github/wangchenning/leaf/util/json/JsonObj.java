package com.github.wangchenning.leaf.util.json;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 用于解析、生成json
 * 
 * 不要直接往JsonObj中put jsonObj,否则会和预期结果不一样
 * @author wangchenning
 *
 */
public class JsonObj {
	@JsonProperty
	private final Map<String, Object> jsonObj;
	public class JsonParseIncorrectException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public JsonParseIncorrectException(String msg) {
			super(msg);
		}
		
	}
	
	private JsonObj() {
		jsonObj = new HashMap<>();
	}
	
	/**
	 * 用于生成
	 * @return
	 */
	public static JsonObj instance() {
		return new JsonObj();
	}
	
	public JsonObj union(JsonObj other) {
		jsonObj.putAll(other.jsonObj);
		return this;
	}
	
	public int size() {
		return jsonObj.size();
	}
	/**
	 * 用于解析
	 * @param json
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public JsonObj(String json) throws JsonParseException, JsonMappingException, IOException {
		if (null == json || json.isEmpty()) {
			throw new JsonParseIncorrectException("传入json为null或为空");
		}
		ObjectMapper om = new ObjectMapper();
		jsonObj = om.readValue(json, Map.class);
	}
	private JsonObj(Map<String, Object> jsonObj) {
		this.jsonObj = jsonObj;
	}
	

	public JsonObj putProperty(String k, Object v) {
		this.jsonObj.put(k, v);
		return this;
	}
	
	public Object getProperty(String key) {
		return jsonObj.get(key);
	}
	
	public Object getPropertyOrDefault(String key, Object defaultValue) {
		return jsonObj.getOrDefault(key, defaultValue);
	}
	
	public JsonObj getJsonObj(String key) {
		Object obj = jsonObj.get(key);
		if (null == obj) {
			return null;
		}
		if (Map.class.isInstance(obj)) {
			return new JsonObj((Map)obj);
		} else {
			throw new JsonParseIncorrectException("不能转化为json对象,该对象类型为："  +  jsonObj.get(key).getClass());
		}
	}
	
	public List<JsonObj> getJsonArray(String key) {
		List<JsonObj> jsobObjs = new ArrayList<>();
		Object obj = jsonObj.get(key);
		if (null == obj) {
			return null;
		}
		if (List.class.isInstance(obj)) {
			List jsonObjArray = (List) obj;
			for (Object o : jsonObjArray) {
				if (null == o) {
					continue;
				}
				if (Map.class.isInstance(o)) {
					jsobObjs.add(new JsonObj((Map)o));
				} else {
					throw new JsonParseIncorrectException("转化为json数组对象不成功");
				}	
			}
			return jsobObjs;
		} else {
			throw new JsonParseIncorrectException("不能转化为json对象,该对象类型为："  +  jsonObj.get(key).getClass());
		}
	}
	
	public String toJson() {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 待测试，暂时别用
	 * @param c
	 * @return
	 */
	public <T> T toObj(Class<T> c) {
		T t = null;
		try {
			t = c.newInstance();
			for (Field f : c.getDeclaredFields()) {
				if (null == getProperty(f.getName())) {
					continue;
				}
				f.setAccessible(true);
				f.set(t, getProperty(f.getName()));
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (String key : jsonObj.keySet()) {
			sb.append(key)
				.append(":")
				.append(jsonObj.get(key))
				.append(",");
		}
		if (!jsonObj.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * getProperty简化版，类型转换正确性需调用者保证
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return (String)jsonObj.get(key);
	}
	/**
	 * getProperty简化版，类型转换正确性需调用者保证
	 * @param key
	 * @return
	 */
	public Integer getInteger(String key) {
		return (Integer) jsonObj.get(key);
	}
	/**
	 *getProperty简化版，类型转换正确性需调用者保证
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		return (Double) jsonObj.get(key);
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.putAll(jsonObj);
		return map;
	}
	public static void main(String[] args) throws IOException {
//		ObjectMapper om = new ObjectMapper();
//		Map<String, Object> personalMsg = new HashMap<>();
//		personalMsg.put("name", "杨逸斐");
//		personalMsg.put("age", 21);
//		Map<String, Object> location = new HashMap<>();
//		location.put("city", "shanghai");
//		location.put("country", "China");
//		personalMsg.put("location", location);
//		List<Map<String, Object>> friends = new ArrayList<>();
//		Map<String, Object> ying = new HashMap<>();
//		ying.put("姓名", "y");
//		ying.put("phone", "123");
//		Map<String, Object> ye = new HashMap<>();
//		ye.put("身高", 1);
//		friends.add(ye);
//		friends.add(ying);
//		personalMsg.put("friends", friends);
//		String json = om.writeValueAsString(personalMsg);
//		System.out.println(json);
//		//解析
//		JsonObj jsonobj = new JsonObj(json);
//		JsonObj msg = jsonobj;
//		System.out.println("name: " + msg.getProperty("name"));
//		JsonObj loc = msg.getJsonObj("location");
//		System.out.println("country: " + loc.getProperty("country"));
//		List<JsonObj> fs = msg.getJsonArray("friends");
//		System.out.println(fs.get(0));
		
		JsonObj jo = JsonObj.instance();
		jo.putProperty("name", "Yang");
		List<Integer> ages = new ArrayList<>();
		ages.add(1);
		ages.add(2);
		jo.putProperty("age", ages);
		System.out.println(jo.toJson());
		
		JsonObj jo1 = JsonObj.instance();
		jo1.putProperty("gender", "male");
		jo1.putProperty("jo", jo);
		System.out.println(jo1.toJson());
	}
}
