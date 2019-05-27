package com.github.wangchenning.leaf.util.json;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private final static ObjectMapper objectMapper = new ObjectMapper();
	public static String object2Json(Object obj) throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(obj);
	}
	public static String getJsonStr(Object obj) throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(obj);
	}
	
    public static String hashMapToJson(LinkedHashMap map) {  
        String string = "{";  
        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {  
            Entry e = (Entry) it.next();  
            string += "\"" + e.getKey() + "\":";  
            string += "\"" + e.getValue() + "\",";  
        }  
        string = string.substring(0, string.lastIndexOf(","));  
        string += "}";  
        return string;  
    } 
}
