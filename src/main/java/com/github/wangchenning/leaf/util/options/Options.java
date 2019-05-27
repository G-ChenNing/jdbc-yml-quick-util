package com.github.wangchenning.leaf.util.options;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.github.wangchenning.leaf.util.json.JsonUtil;
import com.github.wangchenning.leaf.util.xml.XmlUtil;
/**
 *   该类目的为简化配置信息的设置
 * @author wangchenning
 *
 */
public class Options implements Iterable<Entry<String, String>> {
	private Map<String, String> options ;
	public Options() {
		options = new HashMap<>();
	}
	public Options put(String key, String value) {
		options.put(key, value);
		return this;
	}
	public String get(String key) {
		return options.get(key);
	}
	/**
	 * 替换原来的key,若不存在则返回false
	 * @param key
	 * @param newKey
	 * @return
	 */
	public boolean replaceKey(String key, String newKey) {
		if (options.containsKey(key)) {
			String v = options.get(key);
			options.remove(key);
			options.put(newKey, v);
			return true;
		}
		return false;
	}
	public String getOptionsJSON() {
		try {
			return JsonUtil.getJsonStr(options);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getOptionsXML() {
		return XmlUtil.createXMLStr(options);
	}
	public Set<String> keySet() {
		return options.keySet();
	}
	@Override
	public Iterator<Entry<String, String>> iterator() {
		return options.entrySet().iterator();
	}
	
	/**
	 * 使用示例如下
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options().put("name", "yangyifei")
				.put("age", "21");
		System.out.println(options.getOptionsJSON());
		System.out.println(options.getOptionsXML());
		
		for (Entry<String, String> option : options) {
			System.out.println(option.getKey() + ": " + option.getValue());
			
		}
	}
}

