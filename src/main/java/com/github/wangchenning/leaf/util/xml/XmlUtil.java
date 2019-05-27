package com.github.wangchenning.leaf.util.xml;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.jackson.JsonProcessingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.xml.XmlMapper;
public class XmlUtil {
	
	
	public static Element parseXML(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlStr.getBytes()));
		Document docu = db.parse(inputSource);
		return docu.getDocumentElement();
	}
	
	public static String createXMLStr(Map<String, String> params) {
		DocumentBuilder db = null;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = db.newDocument();
			Element root = doc.createElement("xml");
			params.forEach((key, value) -> {
				Element element = doc.createElement(key);
				element.setTextContent(value);
				root.appendChild(element);
			});
			doc.appendChild(root);
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			tf.transform(new DOMSource(doc), new StreamResult(outputStream));
			return new String(outputStream.toByteArray());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return  null ;
	}
	@Deprecated
	/**
	 * 属性类型均为String，且有相应public getXX方法
	 * 不加入到xml的设为null
	 * @param obj with getXXX方法
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public static String object2Xml(Object obj) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		StringBuilder xml=new StringBuilder();
		xml.append("<xml>\n");
		Field[] fields=obj.getClass().getDeclaredFields();
		for(Field field:fields)
		{
			String tagName=field.getName();
			String methodName="get"+tagName.substring(0, 1).toUpperCase()+tagName.substring(1);
			String tagValue=(String)obj.getClass().getMethod(methodName).invoke(obj);
			if(null!=tagValue)
			{
				xml.append("<"+tagName+">"+tagValue+"</"+tagName+">\n");
			}
		}
		xml.append("</xml>\n");
		return xml.toString();
	}
	
	@Deprecated
	/**
	 * 返回xmlStr中属性为tagName的属性值
	 * @param tagName
	 * @param xmlStr
	 * @return 
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public static String getTagValue(String tagName,String xmlStr) throws JsonProcessingException, IOException
	{
		XmlMapper xmlMapper=new XmlMapper();
		org.codehaus.jackson.JsonNode jsonNode=xmlMapper.readTree(xmlStr);
		org.codehaus.jackson.JsonNode fieldNode = jsonNode.get(tagName);
		return fieldNode==null?null:fieldNode.asText();
	}
	
	@Deprecated
	/**
	 * 将xmlStr时
	 * @param result map:key[标签名称],value[标签值,调用时应为null，若xmlStr中含有该标签则为其值]
	 * @param xmlStr 待解析的xml字符串
	 */
	public static void parseXml(Map<String,String> result,String xmlStr)
	{
		String CDATA = "<![CDATA[";
		result.keySet().forEach(tagName->
		{
			String tag="<"+tagName+">";
			int pos=xmlStr.indexOf(tag);
			boolean exsit = -1!=pos;
			if(exsit)
			{
				int valuePos = pos+tag.length();
				int valueEndPos = xmlStr.indexOf("<", valuePos);
				if(xmlStr.substring(valuePos).contains(CDATA))
				{
					valuePos+=CDATA.length();
					valueEndPos=xmlStr.indexOf("]]>", valuePos);
				}
				String tagValue = xmlStr.substring(valuePos, valueEndPos);
				result.replace(tagName, tagValue);
			}
		}
				);
	}
}
