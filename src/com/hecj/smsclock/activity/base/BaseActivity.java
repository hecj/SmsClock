package com.hecj.smsclock.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
	
	
	
	}
	
	/**
	 * Æô¶¯ÐÂµÄActivity
	 * @param pClass
	 */
	protected void startActivity(Class<?> pClass){
		Intent mIntent = new Intent(this,pClass);
		startActivity(mIntent);
	}

}