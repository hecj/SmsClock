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
 * ���ն���
 * @author HECJ
 *
 */
public class ReceiverSystemSMS extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(context);
		
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Bundle mBundle = intent.getExtras();
			//��ȡ����
			Object[] objs = (Object[]) mBundle.get("pdus");
			//����SmsMessage����
			SmsMessage[] mSmsMessage = new SmsMessage[objs.length];
			for(int n=0;n<mSmsMessage.length;n++){
				mSmsMessage[n] = SmsMessage.createFromPdu((byte[])objs[n]);
				//�������ֻ���
				String sender = mSmsMessage[n].getOriginatingAddress();
				//��������
				String content = mSmsMessage[n].getMessageBody();
				//����ʱ��
				Date date = new Date(mSmsMessage[n].getTimestampMillis());
				String sendTime = FormatUtil.formatYMDHMS(date);
				
				//�������ݵ�ϵͳ���ű�
				//mServicesSmsClock.insertSystemSMS(sender, 1, content);
				//�жϹ㲥
				//abortBroadcast();
				
			}
		}
	}

}
