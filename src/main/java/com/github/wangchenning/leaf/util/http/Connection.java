package com.github.wangchenning.leaf.util.http;
public interface Connection {
	/**
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式 GET或POST
	 * @param outputStr 发送数据
	 * @return 
	 */
	String	httpRequestAndReturnString(String requestUrl, String requestMethod, String outputStr);
	String GET_METHOD = "GET";
	String POST_METHOD = "POST";
}
