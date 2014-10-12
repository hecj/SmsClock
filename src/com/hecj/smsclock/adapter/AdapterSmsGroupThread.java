package com.hecj.smsclock.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.adapter.base.AdapterBase;
import com.hecj.smsclock.model.SMS;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.SMSStringUtil;

public class AdapterSmsGroupThread extends AdapterBase {
	
	private Context mContent ;
	
	public AdapterSmsGroupThread(Context pContext, List<?> pList) {
		super(pContext, pList);
		this.mContent = pContext;
	}

	@Override
	public View getView(int position, View mView, ViewGroup parent) {
		try{
			
			SMS sms = (SMS) getmList().get(position);
			if(mView == null ){
				mView = LayoutInflater.from(mContent).inflate(R.layout.sms_alarm_sms_group_thread_listitem, null);
			}
			TextView tv_reciver = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_thread_listitem_reciver);
			TextView tv_send = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_thread_listitem_send);
			TextView tv_recivertime = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_thread_listitem_recivertime);
			TextView tv_sendtime = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_thread_listitem_sendtime);
			SpannableStringBuilder sb = new SpannableStringBuilder(sms.getBody());
			//判断是接收的还是发送的
			if(sms.getType().equals("1")){
				tv_reciver.setText(sb);
				tv_recivertime.setText(SMSStringUtil.getShowSmsTime(sms.getDate()));
				
				tv_reciver.setVisibility(View.VISIBLE);
				tv_recivertime.setVisibility(View.VISIBLE);
				tv_send.setVisibility(View.GONE);
				tv_sendtime.setVisibility(View.GONE);
			}else{
				tv_send.setText(sb);
				tv_sendtime.setText(SMSStringUtil.getShowSmsTime(sms.getDate()));
				
				tv_reciver.setVisibility(View.GONE);
				tv_recivertime.setVisibility(View.GONE);
				tv_send.setVisibility(View.VISIBLE);
				tv_sendtime.setVisibility(View.VISIBLE);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			LogUtil.e(ex.getMessage());
		}
		return mView;
	}

}
