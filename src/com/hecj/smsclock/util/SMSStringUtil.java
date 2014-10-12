package com.hecj.smsclock.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 工具类
 * @author HECJ
 *
 */
public class SMSStringUtil {
	
	/**
	 * 
	 * @param date
	 * @param currTime
	 * @return
	 */
	public static String showTimeString(Date date,Date currTime){
		long minuxTime = date.getTime() - currTime.getTime() ; 
		long day = minuxTime/(1000*60*60*24);
		minuxTime =minuxTime%(1000*60*60*24); 
		long hour = minuxTime/(1000*60*60);
		minuxTime = minuxTime%(1000*60*60);
		long minute = minuxTime/(1000*60);
		minuxTime = minuxTime%(1000*60);
		long second = minuxTime/1000;

		String str = "";
		if(day>0){
			str = day+"天";
		}
		if(hour>0 || str.length()>0){
			str += hour+"小时";
		}
		if(minute>0 || str.length()>0){
			str += minute+"分钟";
		}
		str += second+"秒";
		
		return str ;
	}
	
	/**
	 * 根据日期格式化字符串
	 * @param pDate
	 * @return
	 */
	public static String getShowSmsTime(Date pDate){
		
		if(FormatUtil.getYMD(pDate).equals(FormatUtil.getYMD(new Date()))){
			return FormatUtil.getHHmm(pDate);
		}
		
		Calendar leftCalendar = Calendar.getInstance();
		leftCalendar.add(Calendar.DAY_OF_MONTH, -1);
		if(FormatUtil.getYMD(pDate).equals(FormatUtil.getYMD(leftCalendar.getTime()))){
			return "昨天";
		}
		leftCalendar.add(Calendar.DAY_OF_MONTH, -1);
		if(FormatUtil.getYMD(pDate).equals(FormatUtil.getYMD(leftCalendar.getTime()))){
			return "前天";
		}
		//今年
		if(FormatUtil.getYYYY(pDate).equals(FormatUtil.getYYYY(leftCalendar.getTime()))){
			return FormatUtil.getMMdd(pDate);
		}else{
			//历史年份
			return FormatUtil.getyyyyMMdd(pDate);
		}
	}
	
}
