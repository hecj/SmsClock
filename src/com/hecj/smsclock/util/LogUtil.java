package com.hecj.smsclock.util;

import android.util.Log;
/**
 * ��־��
 * @author HECJ
 *
 */
public final class LogUtil {
	
	/**
	 * ����-1����ʾ����ӡ��־
	 */
	private static int RETURNVALUE = -1;
	
	/**
	 * VERBOSE������־
	 * @param pMsg
	 * @return
	 */
	public static int v(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.v(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * INFO������־
	 * @param pMsg
	 * @return
	 */
	public static int i(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.i(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * DEBUG������־
	 * @param pMsg
	 * @return
	 */
	public static int d(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.d(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * ERROR������־
	 * @param pMsg
	 * @return
	 */
	public static int e(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.e(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
	/**
	 * WARN������־
	 * @param pMsg
	 * @return
	 */
	public static int w(String pMsg){
		return ConstantUtil.ISPRINTLOG?Log.w(ConstantUtil.LOGNAME, pMsg):RETURNVALUE;
	}
}
