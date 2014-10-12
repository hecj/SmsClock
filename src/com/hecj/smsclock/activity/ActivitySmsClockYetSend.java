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
	 * 分页类
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
		//设置数据
		mListView.setAdapter(mSmsClockYetSendAdapter);
		//刷新事件
		mListView.setOnRefreshListViewListener(this);
		//handler更新UI数据
		mListViewHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==LISTVIEW_HANDLER_CODE){
					initListView();
				}
			}
		};
		//启动线程更新数据
		new Thread(new ListViewThread()).start();
		
	}
	
	/**
	 * 初始化ListView
	 */
	private void initListView() {
		mDataList.clear();
		mDataList.addAll(mDataTempList);
		//更新UI
		mSmsClockYetSendAdapter.notifyDataSetChanged();
		
		appTitleRight.setText("共"+mPagination.getCountSize()+"条 第"+(mPagination.getCountSize()==0?0:mPagination.getCurrPage())+"页");
	}
	
	/**
	 * ListView异步加载ListView数据
	 * @author HECJ
	 *
	 */
	class ListViewThread implements Runnable{
		@Override
		public void run() {
			//在线程中查询数据
			//更新页码
			mPagination.setCurrPage(mPagination.getCurrPage()+1);
			//查询数据
			List<Object> mList = mServicesSmsClock.getSmsClockYetSendByLimit(mPagination);
			//添加数据
			mDataTempList.addAll((List<SMSSalarm>) mList.get(0));
			//更新数据总数
			mPagination.setCountSize(((Pagination)mList.get(1)).getCountSize());
			
			Message msg = new Message();
			msg.what = LISTVIEW_HANDLER_CODE;
			mListViewHandler.sendMessage(msg);
		}
	}

	@Override
	public void onRefresh() {
		//启动线程更新数据
		new Thread(new ListViewThread()).start();
	}

	@Override
	public int getDadaSize() {
		return mPagination.getCountSize();
	}
	
}
