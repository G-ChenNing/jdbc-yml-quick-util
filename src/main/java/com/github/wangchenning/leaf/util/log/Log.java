package com.github.wangchenning.leaf.util.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Log {
	public static void log(String logFileName,Object obj) throws IOException
	{
		PrintWriter out=new PrintWriter(new FileWriter(logFileName,true));
		Calendar now=Calendar.getInstance();
		out.append(now.get(Calendar.YEAR)+"-"+now.get(Calendar.MONTH)
		+"-"+now.get(Calendar.DATE)+" "+
				now.get(Calendar.HOUR_OF_DAY)+":"
		+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)
		+"\t"+obj.toString()+"\n");
		out.close();
	}
}
