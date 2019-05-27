package com.github.wangchenning.leaf.util.time;

import java.util.Date;

public class TimeUtil {
	/**
	 * 获得秒级时间戳(10位)
	 * @return
	 */
	synchronized public static final String getSecondTimeStamp()
	{
		Date now=new Date();
		return Long.toString(now.getTime()/1000);
	}
	synchronized public static final String getTimeStamp() {
		return Long.toString(new Date().getTime());
	}
	
//	public static void main(String[] args) {
//		System.out.println(getTimeStamp());
//	}
}
