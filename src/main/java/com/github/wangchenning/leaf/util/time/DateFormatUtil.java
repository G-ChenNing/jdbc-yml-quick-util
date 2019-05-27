package com.github.wangchenning.leaf.util.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static String getyyyy_MM_dd_hh_mmDateStr(Date date) {
		synchronized (sdf) {
			return sdf.format(date);
		}
	}
	public static void main(String[] args) {
		System.out.println(getyyyy_MM_dd_hh_mmDateStr(new Date()));
	}
}
