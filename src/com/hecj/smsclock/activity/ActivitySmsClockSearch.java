package com.hecj.smsclock.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.FrameActivity;
import com.hecj.smsclock.adapter.SmsClockSearchAdapter;
import com.hecj.smsclock.dialog.DialogSmsClock;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.Pagination;
import com.hecj.smsclock.view.SMSFooterFreshListView;
import com.hecj.smsclock.view.SMSFooterFreshListView.OnRefreshListViewListener;

public class ActivitySmsClockSearch extends FrameActivity implements Runnable,OnItemLongClickListener,OnRefreshListViewListener {

	private ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(this);
	private SMSFooterFreshListView mListView = null ;
	private Handler mListViewHandler = null ;
	
	private List<SMSSalarm> mDataList = new ArrayList<SMSSalarm>();
	private List<SMSSalarm> mDataTempList = new ArrayList<SMSSalarm>();
	private SmsClockSearchAdapter mSmsClockSearchAdapter = new SmsClockSearchAdapter(this,mDataList);
	
	private TextView appTitleRight = null ;
	/**
	 * 分页类
	 */
	private Pagination mPagination = new Pagination();
	/**
	 * ListView handler code
	 */
	private final static int LISTVIEW_HANDLER_CODE = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleName(R.string.app_clock_search);
		setBodyBackground(R.drawable.app_body_bg);
		setBodyLayout(R.layout.sms_alarm_search);
	
		mListView =  (SMSFooterFreshListView) this.findViewById(R.id.sms_alarm_search_listview);
		appTitleRight = (TextView)this.findViewById(R.id.app_title_right);
		
		mListView.setAdapter(mSmsClockSearchAdapter);
		//ListView item长按事件
		mListView.setOnItemLongClickListener(this);
		//注册刷新事件
		mListView.setOnRefreshListViewListener(this);
		
		//异步加载数据
		mListViewHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what == LISTVIEW_HANDLER_CODE){
					updateListView();
				}
			}
		};
		//启动线程加载数据
		new Thread(new ListViewThread()).start();
		
		new Thread(this).start();
	}
	
	/**
	 * 更新listview
	 */
	private void updateListView() {
		if(mDataTempList.size()>=0){
			
			appTitleRight.setText("共"+mPagination.getCountSize()+"条 第"+(mPagination.getCountSize()==0?0:mPagination.getCurrPage())+"页");
			mDataList.clear();
			mDataList.addAll(mDataTempList);
			this.mSmsClockSearchAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			//在线程中更新数据
			if(mDataTempList.size()>=0){
				List<Object> mList = mServicesSmsClock.updateListData(mDataTempList);
				mDataTempList = (List<SMSSalarm>) mList.get(0);
				//更新总数
				this.mPagination.setCountSize(this.mPagination.getCountSize()-(Integer) mList.get(1));
				//更新页码
				this.mPagination.setCurrPage(mDataTempList.size()%mPagination.getPageSize()==0?mDataTempList.size()/mPagination.getPageSize():(mDataTempList.size()/mPagination.getPageSize()+1));
			}
			
			mListViewHandler.sendMessage(mListViewHandler.obtainMessage(LISTVIEW_HANDLER_CODE));
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		
		TextView mId = (TextView) view.findViewById(R.id.sms_alarm_search_id);
		final String smsId = mId.getText().toString();
		
		//显示对话框
		final Dialog mDialog = new DialogSmsClock(this,R.layout.dialog_sms_clock_search,R.style.MyDialog);
		mDialog.show();
		//删除
		mDialog.findViewById(R.id.dialog_sms_clock_search_delete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mServicesSmsClock.deleteSmsClock(smsId);
				mDataTempList.remove(position);
				mDataList.remove(position);
				mSmsClockSearchAdapter.notifyDataSetChanged();
				//减去一条数据
				mPagination.setCountSize(mPagination.getCountSize() - 1); 
				//更新当前页码
				mPagination.setCurrPage((mPagination.getCurrPage()-1)*mPagination.getPageSize()==mPagination.getCountSize()?(mPagination.getCurrPage()-1):mPagination.getCurrPage());
				//mPagination.setCurrPage(mDataList.size()%mPagination.getPageSize()==0?mDataList.size()/mPagination.getPageSize():(mDataList.size()/mPagination.getPageSize()+1));

				appTitleRight.setText("共"+mPagination.getCountSize()+"条 第"+(mPagination.getCountSize()==0?0:mPagination.getCurrPage())+"页");
				mDialog.cancel();
				Toast.makeText(ActivitySmsClockSearch.this, getResources().getString(R.string.sms_delete_msg), Toast.LENGTH_SHORT).show();
			}
		});
		//编辑
		mDialog.findViewById(R.id.dialog_sms_clock_search_edit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				SMSSalarm mSMSSalarm = mServicesSmsClock.searchSmsClockById(smsId);
				Intent mIntent = new Intent(ActivitySmsClockSearch.this,ActivitySmsClockSetContent.class);
				mIntent.putExtra("SMSSalarm", mSMSSalarm);
				startActivity(mIntent);
				mDialog.cancel();
				finish();
			}
		});
		//退出
		mDialog.findViewById(R.id.dialog_sms_clock_search_exit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.cancel();
			}
		});
		
		return false;
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
			//增加页码
			mPagination.setCurrPage(mPagination.getCurrPage()+1);
			//查询数据
			List<Object> mList = mServicesSmsClock.getSmsClockByLimit(mPagination);
			//增加数据集
			mDataTempList.addAll((List<SMSSalarm>) mList.get(0));
			//更新总数
			mPagination.setCountSize(((Pagination)mList.get(1)).getCountSize());
			
			mListViewHandler.sendMessage(mListViewHandler.obtainMessage(LISTVIEW_HANDLER_CODE));
		}
	}


	@Override
	public void onRefresh() {
		//启动线程加载数据
		new Thread(new ListViewThread()).start();

	}

	@Override
	public int getDadaSize() {
		return mPagination.getCountSize();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
