package com.hecj.smsclock.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class AppSystemUtil {
	
	/**
	 * 获取屏幕大小
	 * @param pContext
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context pContext){
		
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)pContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics ;
	}
}
