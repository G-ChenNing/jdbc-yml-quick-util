package com.github.wangchenning.leaf.util.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
	public static String MD5(String str) throws NoSuchAlgorithmException
	{
		 // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        StringBuilder md5Str=new StringBuilder();
        byte[] result = md.digest();
		for(int i=0;i<result.length;++i)
		{
//			result[i]+=256;
			int temp=((int)result[i]+256)%256;
			String hexString = Integer.toHexString(temp);
			md5Str.append(hexString.length()==2?hexString:"0"+hexString);
		}
		return md5Str.toString().toUpperCase();
	}
	
	
	public static String SHA1(String str) throws NoSuchAlgorithmException
	{
		 // 生成一个SHA1加密计算摘要
        MessageDigest md = MessageDigest.getInstance("SHA1");
        // 计算SHA1函数
        md.update(str.getBytes());
        // digest()最后确定返回sha1 hash值，返回值为8为字符串。因为sha1 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        StringBuilder sha1Str=new StringBuilder();
        byte[] result = md.digest();
		for(int i=0;i<result.length;++i)
		{
//			result[i]+=256;
			int temp=((int)result[i]+256)%256;
			String hexString = Integer.toHexString(temp);
			sha1Str.append(hexString.length()==2?hexString:"0"+hexString);
		}
		return sha1Str.toString();
	}
}
