package com.hecj.smsclock.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.adapter.base.AdapterBase;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.LogUtil;

public class SmsClockSearchAdapter extends AdapterBase {

	public SmsClockSearchAdapter(Context pContext, List<?> pList) {
		super(pContext, pList);
	}

	@Override
	public View getView(int position, View mView, ViewGroup parent) {
		try{
			
			if(mView == null){
				mView = LayoutInflater.from(getmContext()).inflate(R.layout.sms_alarm_search_listitem, null) ;
			}
			
			TextView mId = (TextView)mView.findViewById(R.id.sms_alarm_search_id);
			TextView mTelphone = (TextView)mView.findViewById(R.id.sms_alarm_search_telphone);
			TextView mTelName = (TextView)mView.findViewById(R.id.sms_alarm_search_telname);
			TextView mContent = (TextView)mView.findViewById(R.id.sms_alarm_search_content);
			TextView mSendtime = (TextView)mView.findViewById(R.id.sms_alarm_search_sendtime);
			TextView mShowtime = (TextView)mView.findViewById(R.id.sms_alarm_search_showtime);
			
			SMSSalarm mSms = (SMSSalarm) getmList().get(position);
			
			mId.setText(String.valueOf(mSms.getId()));
			mTelphone.setText(mSms.getTelphone());
			mTelName.setText(mSms.getTelname());
			mContent.setText(mSms.getContent());
			mSendtime.setText(FormatUtil.formatYMDHMS(mSms.getSendTime()));
			mShowtime.setText(mSms.getShowTime());
			
		}catch(Exception ex){
			ex.printStackTrace();
			LogUtil.e(ex.getMessage());
		}
		
		return mView;
	}

}
