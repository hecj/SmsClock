package com.hecj.smsclock.reciver;

import java.util.Date;

import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.FormatUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
/**
 * 接收短信
 * @author HECJ
 *
 */
public class ReceiverSystemSMS extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(context);
		
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Bundle mBundle = intent.getExtras();
			//获取短信
			Object[] objs = (Object[]) mBundle.get("pdus");
			//创建SmsMessage数组
			SmsMessage[] mSmsMessage = new SmsMessage[objs.length];
			for(int n=0;n<mSmsMessage.length;n++){
				mSmsMessage[n] = SmsMessage.createFromPdu((byte[])objs[n]);
				//发件人手机号
				String sender = mSmsMessage[n].getOriginatingAddress();
				//短信内容
				String content = mSmsMessage[n].getMessageBody();
				//接收时间
				Date date = new Date(mSmsMessage[n].getTimestampMillis());
				String sendTime = FormatUtil.formatYMDHMS(date);
				
				//插入数据到系统短信表
				//mServicesSmsClock.insertSystemSMS(sender, 1, content);
				//中断广播
				//abortBroadcast();
				
			}
		}
	}

}
