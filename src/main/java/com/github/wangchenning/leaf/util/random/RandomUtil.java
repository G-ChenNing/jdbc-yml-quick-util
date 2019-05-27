package com.github.wangchenning.leaf.util.random;

import java.security.MessageDigest;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

public class RandomUtil {
	public static int NONCE_STR_LENGTH=10;
	/**
	 * 生成长度为NONCE_STR_LENGTH的随机字符串
	 * @return
	 */
	synchronized public static final String getNonceStr()
	{
		return UUID.randomUUID().toString().substring(0, NONCE_STR_LENGTH);
	}
	synchronized public static String getId() {
		return UUID.randomUUID().toString();
	}
	synchronized public static String getCode() {
		return UUID.randomUUID().toString();
	}
	/**
	 * 密码加密处理（MD5）
	 * @param src 原密码
	 * @return 加密后的内容
	 */
	public static String md5(String src){
		try{//采用MD5处理
			MessageDigest md = 
				MessageDigest.getInstance("MD5");
			byte[] output = md.digest(
				src.getBytes());//加密处理
			//将加密结果output利用Base64转成字符串输出
			String ret = 
			 Base64.encodeBase64String(output);
			return ret;
		}catch(Exception e){
			return "";
		}
	}
	public static void main(String[] args) {
//		System.out.println(getId());
//		System.out.println(getCode());
//		System.out.println(md5("123456"));
//		System.out.println(md5("123456"));
	}
}
