package com.github.wangchenning.leaf.util.http;
//import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	
	
	public static String getURL(String url,Map<String,String> params)
	{
		String getURL=url;
		String paramsStr="";
		int i=0;
		Iterator<Entry<String, String>> iter=params.entrySet().iterator();
		while(iter.hasNext())
		{
			Entry<String, String> e=(Entry<String, String>) iter.next();
			if(i==0)
			{
				paramsStr+="?"+e.getKey()+"="+e.getValue();
			}
			else
			{
				paramsStr+="&"+e.getKey()+"="+e.getValue();
			}
			++i;
		}
		
		/*System.out.println(getURL+paramsStr);
		try {
			paramsStr=URLEncoder.encode(paramsStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return getURL+paramsStr;
	}
	/*
	 * @param url 原url,例如http://open.weixin.qq.com/connect/qrconnect?
	 * @param object 类属性为要请求的参数
	 */
	public static String getUrl(String url,Object object,String[] except)
	{
		Field[] field =object.getClass().getDeclaredFields();        //获取实体类的所有属性，返回Field数组  
	     for(int j=0 ; j<field.length ; j++){     //遍历所有属性
	    	 String name = field[j].getName();    //获取属性的名字     
	    	 //System.out.println(name);
	    	 
	    	 //在except集中的不加到url中
	    	 boolean exceptThisName=false;
	    	 for(String exceptName:except)
	    	 {
	    		 if(name.equals(exceptName))
	    		 {
	    			 exceptThisName=true;
	    			 break;
	    		 }
	    	 }
	    	 if(exceptThisName)
	    		 continue;
	         String Name = name.substring(0,1).toUpperCase()+name.substring(1); //将属性的首字符大写，方便构造get，set方法
	         //String type = field[j].getGenericType().toString();    //获取属性的类型
	         //if(type.equals("class java.lang.String")){   //如果type是类类型，则前面包含"class "，后面跟类名
	         Method m;
			try {
				m = object.getClass().getMethod("get"+Name);
				Object value =m.invoke(object);    //调用getter方法获取属性值
				if(value!=null)
					{
					if(j==0)
						url=url+name+"="+value.toString();
					else
					url=url+"&"+name+"="+value.toString();
					}
			}catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}		
	     }
//	     System.out.println(url);
	     return url;
	}

}