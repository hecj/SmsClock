package com.hecj.smsclock.util;

public class StringUtil {
	
	/**
	 * null | "" -->true
	 * @param pStr
	 * @return
	 */
	public static boolean isEmpty(String pStr){
		return pStr == null || pStr.equals("null") || pStr.equals("") || pStr =="";
	}
}
