package com.hecj.smsclock.util;

import java.util.Calendar;
import java.util.Date;

/**
 * ������
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
			str = day+"��";
		}
		if(hour>0 || str.length()>0){
			str += hour+"Сʱ";
		}
		if(minute>0 || str.length()>0){
			str += minute+"����";
		}
		str += second+"��";
		
		return str ;
	}
	
	/**
	 * �������ڸ�ʽ���ַ���
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
			return "����";
		}
		leftCalendar.add(Calendar.DAY_OF_MONTH, -1);
		if(FormatUtil.getYMD(pDate).equals(FormatUtil.getYMD(leftCalendar.getTime()))){
			return "ǰ��";
		}
		//����
		if(FormatUtil.getYYYY(pDate).equals(FormatUtil.getYYYY(leftCalendar.getTime()))){
			return FormatUtil.getMMdd(pDate);
		}else{
			//��ʷ���
			return FormatUtil.getyyyyMMdd(pDate);
		}
	}
	
}
