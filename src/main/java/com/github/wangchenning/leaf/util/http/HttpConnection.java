package com.github.wangchenning.leaf.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;
@Service
public class HttpConnection implements Connection {

	/*@Override
	public ObjectMapper httpRequest(String requestUrl, String requestMethod, String outputStr) {
		String str=this.httpRequestAndReturnString(requestUrl, requestMethod, outputStr);
		if(str==null)
			return null;
		//return JSONObject.fromObject(str);
		ObjectMapper objectMapper=new ObjectMapper();
		//objectMapper.
	}*/
	/**
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式 GET或POST
	 * @param outputStr 发送数据
	 * @return 
	 */
	@Override
	public String httpRequestAndReturnString(String requestUrl, String requestMethod, String outputStr) {
		try {
			URL url;
			url = new URL(requestUrl);
			HttpURLConnection conn=(HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			//设置请求方式
			conn.setRequestMethod(requestMethod);
			//当outputStr不为null时，向输出流写数据
			if(null!=outputStr)
			{
				OutputStream outputStream=conn.getOutputStream();
				//编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			//从输入流读取返回内容
			InputStream inputStream=conn.getInputStream();
			Scanner scanner=new Scanner(inputStream,"UTF-8");
			String str="";
			while(scanner.hasNextLine())
			{
				str+=scanner.nextLine();
			}
			scanner.close();
			return str;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	
		
	}
	
	
	

}
