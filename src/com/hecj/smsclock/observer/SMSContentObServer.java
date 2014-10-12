package com.hecj.smsclock.observer;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;

import com.hecj.smsclock.util.HandleWhatCodeUtil;
import com.hecj.smsclock.util.LogUtil;

/**
 * ����SMS���ݿⷢ���仯
 * 
 * @author HECJ
 * 
 */
public class SMSContentObServer extends ContentObserver {

	private Handler mHandler = null;

	public SMSContentObServer(Handler handler) {
		super(handler);
		this.mHandler = handler;
		LogUtil.i("SMSContentObServer");
	}

	@Override
	public boolean deliverSelfNotifications() {

		LogUtil.i("deliverSelfNotifications");
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		LogUtil.v("onChange" + selfChange);
		// ֪ͨHandler���ݸı���
		Message msg = new Message();
		msg.what = HandleWhatCodeUtil.HANDLER_LISTVIEW_CHANGE;
		mHandler.sendMessage(msg);

	}

}
