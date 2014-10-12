package com.hecj.smsclock.util;

import android.util.Log;
/**
 * 日志类
 * @author HECJ
 *
 */
public final class LogUtil {
	
	/**
	 * 返回-1：表示不打印日志
	 */
	private static int RETURNVALUE = -1;
	
	/**
	 * VERBOSE级别日志
	 * @param pMsg
	 * @return
	 */
	public static int v(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.v(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * INFO级别日志
	 * @param pMsg
	 * @return
	 */
	public static int i(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.i(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * DEBUG级别日志
	 * @param pMsg
	 * @return
	 */
	public static int d(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.d(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * ERROR级别日志
	 * @param pMsg
	 * @return
	 */
	public static int e(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.e(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * WARN级别日志
	 * @param pMsg
	 * @return
	 */
	public static int w(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.w(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
}
