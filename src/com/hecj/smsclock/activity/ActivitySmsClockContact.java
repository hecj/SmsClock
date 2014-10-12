package com.hecj.smsclock.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.BaseActivity;
import com.hecj.smsclock.activity.base.FrameActivity;
import com.hecj.smsclock.adapter.AdapterSmsGroup;
import com.hecj.smsclock.model.SMS;
import com.hecj.smsclock.observer.SMSContentObServer;
import com.hecj.smsclock.resolver.ResolverSMS;
import com.hecj.smsclock.util.ConstantUtil;
import com.hecj.smsclock.util.HandleWhatCodeUtil;
import com.hecj.smsclock.util.LogUtil;

public class ActivitySmsClockContact extends FrameActivity{
	
	private ListView mListView = null ;
	private List<SMS> mList = new ArrayList<SMS>();
	//Adapter
	private AdapterSmsGroup mAdapterSmsGroup = new AdapterSmsGroup(this, mList);
	private Handler mListViewHandler = null ;
	
	private final static int LISTVIEW_HANDLER_CODE = 1;
	
	/**
	 * Resolver 
	 */
	private ResolverSMS mResolverSMS = new ResolverSMS(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleName("短信列表");
		setBodyBackground(R.drawable.app_body_bg);
		setBodyLayout(R.layout.sms_alarm_sms_group);
		
		mListView = (ListView) this.findViewById(R.id.sms_alarm_sms_group_listview);
		//绑定Adapter
		mListView.setAdapter(mAdapterSmsGroup);
		//ListView点击事件
		mListView.setOnItemClickListener(new ListViewOnItemClickListener());
		/**
		 * ListView Handler
		 */
		mListViewHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				case HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI:
					notifyListView();
					break;
					
				case HandleWhatCodeUtil.HANDLER_LISTVIEW_CHANGE:
					new Thread(new ListViewThread()).start();
					break;
					
				default:
					break;
				}
				
			}
		};
		
		/**
		 * 开启线程加载数据
		 */
		new Thread(new ListViewThread()).start();
		
		//注册监听数据库表SMS变化
		registerSMSContentObserver();
	}
	
	/**
	 * 初始化ListView
	 */
	private void notifyListView(){
		//通知更新数据
		mAdapterSmsGroup.notifyDataSetChanged();
	}
	
	/**
	 * 注册ContentObserver
	 */
	private void registerSMSContentObserver(){
		//注册监听数据库表SMS变化
		Uri smsUri = Uri.parse(ConstantUtil.CONTENT_SMS);
		//第二个参数表示监听SMS表及下属表
		getContentResolver().registerContentObserver(smsUri, true,new SMSContentObServer(mListViewHandler));
	}
	
	/**
	 * 线程更新UI
	 * @author HECJ
	 *
	 */
	class ListViewThread implements Runnable{

		@Override
		public void run() {
			//查询短信分组数据
			mList.clear();
			mList.addAll(mResolverSMS.searchSMSGroupByThreadId());
			
			Message msg = new Message();
			msg.what = HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI ;
			mListViewHandler.sendMessage(msg);
		}
		
	}
	
	/**
	 * ListView item 点击事件
	 * @author HECJ
	 */
	class ListViewOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			TextView tv_threadId = (TextView) view.findViewById(R.id.sms_alarm_sms_group_threadId);
			TextView tv_name = (TextView) view.findViewById(R.id.sms_alarm_sms_group_name);
			TextView tv_telphone = (TextView) view.findViewById(R.id.sms_alarm_sms_group_telphone);
			
			String theadId = tv_threadId.getText().toString();
			String name = tv_name.getText().toString();
			String telphone = tv_telphone.getText().toString();
			
			//启动Thread Activity
			Intent mIntent = new Intent(ActivitySmsClockContact.this,ActivitySmsClockContactThread.class);
			mIntent.putExtra("theadId", theadId);
			mIntent.putExtra("name", name);
			mIntent.putExtra("telphone", telphone);
			ActivitySmsClockContact.this.startActivity(mIntent);
			
		}
		
	}
	
}
