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
				//发送短信
				if(mServicesSmsClock.sendSMS(intent, telPhone, telContent)){
					Toast.makeText(context, res.getString(R.string.notification_title), Toast.LENGTH_SHORT).show();
					//发送Notification
					mServicesSmsClock.sendNotification(R.drawable.app_icon, res.getString(R.string.notification_title), "给"+telName+"发送了一条短信!", System.currentTimeMillis());
					//更新状态
					mServicesSmsClock.updateYetSendSmsClock(id);
					//将短信插入本机
					mServicesSmsClock.insertSystemSMS(telPhone, 2, telContent);
					
				}
				//设置下一个闹钟
				SMSSalarm mSmsSalarm = mServicesSmsClock.getSmsSalarmLimit1();
				if(mSmsSalarm != null){
					mServicesSmsClock.setSmsClock(mSmsSalarm.getId(),mSmsSalarm.getTelphone(),mSmsSalarm.getTelname(), mSmsSalarm.getContent(), mSmsSalarm.getSendTime().getTime());
					Toast.makeText(context, "下一条短信于"+SMSStringUtil.showTimeString(mSmsSalarm.getSendTime(),FormatUtil.currDate())+"后发送!", Toast.LENGTH_SHORT).show();
				}
			}else if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){ //开机
				//设置下一个闹钟
				SMSSalarm mSmsSalarm = mServicesSmsClock.getSmsSalarmLimit1();
				if(mSmsSalarm != null){
					mServicesSmsClock.setSmsClock(mSmsSalarm.getId(),mSmsSalarm.getTelphone(),mSmsSalarm.getTelname(), mSmsSalarm.getContent(), mSmsSalarm.getSendTime().getTime());
					Toast.makeText(context, "已开机,短信于"+SMSStringUtil.showTimeString(mSmsSalarm.getSendTime(),FormatUtil.currDate())+"后发送!", Toast.LENGTH_SHORT).show();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{

		}
		
	}

}
