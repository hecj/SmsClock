package com.hecj.smsclock.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.adapter.base.AdapterBase;
import com.hecj.smsclock.model.SMS;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.SMSStringUtil;
import com.hecj.smsclock.util.StringUtil;
/**
 * ���ŷ���Adapter
 * @author HECJ
 *
 */
public class AdapterSmsGroup extends AdapterBase {
	
	private Context mContent ;
	public AdapterSmsGroup(Context pContext, List<?> pList) {
		super(pContext, pList);
		this.mContent = pContext;
	}

	@Override
	public View getView(int position, View mView, ViewGroup parent) {
		
		try{
		
			if(mView == null ){
				mView = LayoutInflater.from(mContent).inflate(R.layout.sms_alarm_sms_group_listitem, null);
			}
			TextView tv_threadId = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_threadId);
			TextView tv_telphone = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_telphone);
			TextView tv_Name = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_name);
			TextView tv_telContent = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_content);
			TextView tv_sendTime = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_sendtime);
			TextView tv_count = (TextView) mView.findViewById(R.id.sms_alarm_sms_group_count);
			
			SMS sms = (SMS) getmList().get(position);
			//����
			tv_Name.setText(StringUtil.isEmpty(sms.getPerson())||sms.getPerson().trim().equals("0")?sms.getAddress():sms.getPerson());
			//�ֻ���
			tv_telphone.setText(sms.getAddress());
			//��������
			tv_telContent.setText(StringUtil.isEmpty(sms.getBody())?"":sms.getBody());
			//����ʱ��
			tv_sendTime.setText(SMSStringUtil.getShowSmsTime(sms.getDate()));
			//ͬһ�˶�������
			tv_count.setText("("+String.valueOf(sms.getCount())+")");
			tv_threadId.setText(String.valueOf(sms.getThreadId()));
			
		}catch(Exception ex){
			LogUtil.e(ex.getMessage());
		}
		return mView;
	}

}
