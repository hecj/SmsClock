package com.hecj.smsclock.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.hecj.smsclock.R;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.SMSStringUtil;
import com.hecj.smsclock.util.StringUtil;

public class ReciverSms extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(context);
		Resources res = context.getResources();
		try{
			if( intent.getAction().equals("com.hecj.demo.sms.reciver.smsReciver")){
				Bundle mBundle = intent.getExtras();
				String id = mBundle.getString("id");
				String telPhone = mBundle.getString("telPhone");
				String telName = mBundle.getString("telName");
				String telContent = mBundle.getString("telContent");
				if(StringUtil.isEmpty(telName)){
					telName = res.getString(R.string.sms_recevier_name);
				}
				//���Ͷ���
				if(mServicesSmsClock.sendSMS(intent, telPhone, telContent)){
					Toast.makeText(context, res.getString(R.string.notification_title), Toast.LENGTH_SHORT).show();
					//����Notification
					mServicesSmsClock.sendNotification(R.drawable.app_icon, res.getString(R.string.notification_title), "��"+telName+"������һ������!", System.currentTimeMillis());
					//����״̬
					mServicesSmsClock.updateYetSendSmsClock(id);
					//�����Ų��뱾��
					mServicesSmsClock.insertSystemSMS(telPhone, 2, telContent);
					
				}
				//������һ������
				SMSSalarm mSmsSalarm = mServicesSmsClock.getSmsSalarmLimit1();
				if(mSmsSalarm != null){
					mServicesSmsClock.setSmsClock(mSmsSalarm.getId(),mSmsSalarm.getTelphone(),mSmsSalarm.getTelname(), mSmsSalarm.getContent(), mSmsSalarm.getSendTime().getTime());
					Toast.makeText(context, "��һ��������"+SMSStringUtil.showTimeString(mSmsSalarm.getSendTime(),FormatUtil.currDate())+"����!", Toast.LENGTH_SHORT).show();
				}
			}else if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){ //����
				//������һ������
				SMSSalarm mSmsSalarm = mServicesSmsClock.getSmsSalarmLimit1();
				if(mSmsSalarm != null){
					mServicesSmsClock.setSmsClock(mSmsSalarm.getId(),mSmsSalarm.getTelphone(),mSmsSalarm.getTelname(), mSmsSalarm.getContent(), mSmsSalarm.getSendTime().getTime());
					Toast.makeText(context, "�ѿ���,������"+SMSStringUtil.showTimeString(mSmsSalarm.getSendTime(),FormatUtil.currDate())+"����!", Toast.LENGTH_SHORT).show();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{

		}
		
	}

}
