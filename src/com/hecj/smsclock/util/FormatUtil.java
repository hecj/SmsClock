package com.hecj.smsclock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class FormatUtil {

	private static final SimpleDateFormat format_YMDHMS = new SimpleDateFormat("yyyy-M-d HH:mm:ss",Locale.CHINA);
	private static final SimpleDateFormat format_YYYYMMDDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
	private static final SimpleDateFormat format_YMD = new SimpleDateFormat("yyyy-M-d",Locale.CHINA);
	private static final SimpleDateFormat format_yyyy = new SimpleDateFormat("yyyy",Locale.CHINA);
	private static final SimpleDateFormat format_MM = new SimpleDateFormat("M",Locale.CHINA);
	private static final SimpleDateFormat format_dd = new SimpleDateFormat("d",Locale.CHINA);
	private static final SimpleDateFormat format_HH = new SimpleDateFormat("HH",Locale.CHINA);
	private static final SimpleDateFormat format_mm = new SimpleDateFormat("mm",Locale.CHINA);
	private static final SimpleDateFormat format_ss = new SimpleDateFormat("ss",Locale.CHINA);
	private static final SimpleDateFormat format_HHmm = new SimpleDateFormat("HH:mm",Locale.CHINA);
	private static final SimpleDateFormat format_MMdd = new SimpleDateFormat("M月d日",Locale.CHINA);
	private static final SimpleDateFormat format_yyyyMMdd = new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
	
	
	/**
	 * @param pDateStr
	 * @return
	 */
	public static Date parseYMDHMS(String pDateStr){
		try {
			return format_YMDHMS.parse(pDateStr);
		} catch (ParseException e) {
			Log.e("FormatUtil", e.getMessage());
			e.printStackTrace();
		}
		return null ;
	}
	
	/**
	 * @param pDate
	 * @return
	 */
	public static String formatYMDHMS(Date pDate){
		try{
			return format_YMDHMS.format(pDate);
		}catch(Exception ex){
			
		}
		return "";
	}
	/**
	 * @param pDate
	 * @return
	 */
	public static String formatYYYYMMDDHMS(Date pDate){
		try{
			return format_YYYYMMDDHMS.format(pDate);
		}catch(Exception ex){
			
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public static String currDateYMDHMSString(){
		return formatYMDHMS(new Date());
	}
	/**
	 * 
	 * @return
	 */
	public static Date currDate(){
		long time = System.currentTimeMillis();
		return new Date(time);
	}
	
	/**
	 * 
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Date getDataByParams(int year,int month,int dayOfMonth,int hour,int minute,int second){

		Calendar mCaCalendar = Calendar.getInstance();
		mCaCalendar.set(Calendar.YEAR, year);
		mCaCalendar.set(Calendar.MONTH, month);
		mCaCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mCaCalendar.set(Calendar.HOUR_OF_DAY, hour);
		mCaCalendar.set(Calendar.MINUTE, minute);
		mCaCalendar.set(Calendar.SECOND, second);

		return mCaCalendar.getTime();
	}
	
	/**
	 * 得到当前时间秒
	 * @return
	 */
	public static String getCurrDateSS(){
		return format_ss.format(currDate());
	}
	
	public static String getYYYY(Date pDate){
		return format_yyyy.format(pDate);
	}
	public static String getMM(Date pDate){
		return format_MM.format(pDate);
	}
	public static String getDD(Date pDate){
		return format_dd.format(pDate);
	}
	public static String getHH(Date pDate){
		return format_HH.format(pDate);
	}
	public static String getMI(Date pDate){
		return format_mm.format(pDate);
	}
	public static String getSS(Date pDate){
		return format_ss.format(pDate);
	}

	public static Date getDate(long time){
		return new Date(time);
	}
	
	public static String getHHmm(Date pDate){
		return format_HHmm.format(pDate);
	}
	public static String getMMdd(Date pDate){
		return format_MMdd.format(pDate);
	}
	public static String getyyyyMMdd(Date pDate){
		return format_yyyyMMdd.format(pDate);
	}
	public static String getYMD(Date pDate){
		return format_YMD.format(pDate);
	}
}
