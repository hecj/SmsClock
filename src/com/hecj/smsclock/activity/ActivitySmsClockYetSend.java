package com.hecj.smsclock.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.FrameActivity;
import com.hecj.smsclock.adapter.SmsClockYetSendAdapter;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.Pagination;
import com.hecj.smsclock.view.SMSFooterFreshListView;
import com.hecj.smsclock.view.SMSFooterFreshListView.OnRefreshListViewListener;

public class ActivitySmsClockYetSend extends FrameActivity implements OnRefreshListViewListener{
	
	
	private ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(this);
	
	private SMSFooterFreshListView mListView = null ;
	private List<SMSSalarm> mDataList = new ArrayList<SMSSalarm>();
	private List<SMSSalarm> mDataTempList = new ArrayList<SMSSalarm>();
	private SmsClockYetSendAdapter mSmsClockYetSendAdapter = new SmsClockYetSendAdapter(this,mDataList);
	private Handler mListViewHandler = null ;
	
	private TextView appTitleRight = null ;
	/**
	 * ListView handler code
	 */
	private final static int LISTVIEW_HANDLER_CODE = 1;
	
	/**
	 * ��ҳ��
	 */
	private Pagination mPagination = new Pagination();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleName(R.string.app_clock_send);
		setBodyBackground(R.drawable.app_body_bg);
		setBodyLayout(R.layout.sms_alarm_search);
	
		mListView = (SMSFooterFreshListView)this.findViewById(R.id.sms_alarm_search_listview);
		appTitleRight = (TextView)this.findViewById(R.id.app_title_right);
		//��������
		mListView.setAdapter(mSmsClockYetSendAdapter);
		//ˢ���¼�
		mListView.setOnRefreshListViewListener(this);
		//handler����UI����
		mListViewHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==LISTVIEW_HANDLER_CODE){
					initListView();
				}
			}
		};
		//�����̸߳�������
		new Thread(new ListViewThread()).start();
		
	}
	
	/**
	 * ��ʼ��ListView
	 */
	private void initListView() {
		mDataList.clear();
		mDataList.addAll(mDataTempList);
		//����UI
		mSmsClockYetSendAdapter.notifyDataSetChanged();
		
		appTitleRight.setText("��"+mPagination.getCountSize()+"�� ��"+(mPagination.getCountSize()==0?0:mPagination.getCurrPage())+"ҳ");
	}
	
	/**
	 * ListView�첽����ListView����
	 * @author HECJ
	 *
	 */
	class ListViewThread implements Runnable{
		@Override
		public void run() {
			//���߳��в�ѯ����
			//����ҳ��
			mPagination.setCurrPage(mPagination.getCurrPage()+1);
			//��ѯ����
			List<Object> mList = mServicesSmsClock.getSmsClockYetSendByLimit(mPagination);
			//�������
			mDataTempList.addAll((List<SMSSalarm>) mList.get(0));
			//������������
			mPagination.setCountSize(((Pagination)mList.get(1)).getCountSize());
			
			Message msg = new Message();
			msg.what = LISTVIEW_HANDLER_CODE;
			mListViewHandler.sendMessage(msg);
		}
	}

	@Override
	public void onRefresh() {
		//�����̸߳�������
		new Thread(new ListViewThread()).start();
	}

	@Override
	public int getDadaSize() {
		return mPagination.getCountSize();
	}
	
}
