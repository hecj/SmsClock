package com.hecj.smsclock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
/**
 * ×Ô¶¨ÒåDialog
 * @author HECJ
 *
 */
public class DialogSmsClock extends Dialog {
	
	private int mLayoutId = 0;
	private View mView = null;

	public DialogSmsClock(Context context,int pLayoutId,int themeStyleId) {
		super(context,themeStyleId);
		this.mLayoutId = pLayoutId ;
	}
	
	public DialogSmsClock(Context context,int pLayoutId) {
		super(context);
		this.mLayoutId = pLayoutId ;
	}
	
	public DialogSmsClock(Context context,View pView) {
		super(context);
		this.mView = pView ;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(mLayoutId != 0){
			setContentView(mLayoutId);
		}else if(mView != null ){
			setContentView(mView);
		}
	}


}
