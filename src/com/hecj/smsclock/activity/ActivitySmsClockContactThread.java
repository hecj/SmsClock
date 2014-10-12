package com.hecj.smsclock.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.BaseActivity;
import com.hecj.smsclock.adapter.AdapterSmsGroupThread;
import com.hecj.smsclock.model.SMS;
import com.hecj.smsclock.observer.SMSContentObServer;
import com.hecj.smsclock.resolver.ResolverSMS;
import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.ConstantUtil;
import com.hecj.smsclock.util.HandleWhatCodeUtil;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.Pagination;
import com.hecj.smsclock.util.StringUtil;
import com.hecj.smsclock.view.SMSHeaderFreshListView;
import com.hecj.smsclock.view.SMSHeaderFreshListView.OnRefreshListViewListener;
/**
 * ÿһ��Thread�Ķ����б����Ͷ���ҳ��
 * @author HECJ
 *
 */
public class ActivitySmsClockContactThread extends BaseActivity implements OnRefreshListViewListener{
	/**
	 * ListView Thread
	 */
	private SMSHeaderFreshListView threadListView = null ;
	
	private EditText mSendContent = null ;
	private ImageView mSendButton = null ;
	private Button mAppTitleBack = null ;
	
	/**
	 * �ֻ���
	 */
	private String telPhone = null ;
	/**
	 * ListView data
	 */
	private List<SMS> mDataList = new ArrayList<SMS>();
	/**��������*/
	private List<SMS> mDataTempList = new ArrayList<SMS>();
	/**
	 * Adapter 
	 */
	private AdapterSmsGroupThread  mAdapterSmsGroupThread = new AdapterSmsGroupThread(this,mDataList);
	
	/**
	 * ListView Handler
	 */
	private Handler mListViewHandler = null ;
	/**
	 * Resolver SMS
	 */
	private ResolverSMS mResolverSMS = new ResolverSMS(this);
	
	/**
	 * Pagination
	 */
	private Pagination mPagination = new Pagination();
	
	/**
	 * �û�Thread Id
	 */
	private String threadId = "";
	
	/**
	 * ҵ����
	 */
	private ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(this);
	
	/**
	 * ��һ���̼߳�������
	 */
	private Thread mInitThread = null ;
	/**
	 * �ڲ��̸߳�������
	 */
	private Thread mContentThread = null ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_alarm_sms_group_thread);
		
		TextView tv_telPhone = (TextView)this.findViewById(R.id.sms_alarm_group_thread_telphone);
		TextView tv_name = (TextView)this.findViewById(R.id.sms_alarm_group_thread_name);
		threadListView = (SMSHeaderFreshListView)this.findViewById(R.id.sms_alarm_group_thread_listview);
		mSendContent = (EditText)this.findViewById(R.id.sms_alarm_group_thread_sendcontent);
		mSendButton = (ImageView)this.findViewById(R.id.sms_alarm_group_thread_sendbutton);
		mAppTitleBack = (Button)this.findViewById(R.id.tv_app_title_back);
		threadListView.setAdapter(mAdapterSmsGroupThread);
		threadListView.setOnRefreshListViewListener(this);
		mSendButton.setOnClickListener(new MyOnClickListener());
		mAppTitleBack.setOnClickListener(new MyOnClickListener());
		
		//��ȡIntent�е�ֵ
		Intent mIntent = getIntent();
		if(mIntent != null){
			Bundle mBunlde = mIntent.getExtras();
			threadId = mBunlde.getString("theadId");
			String name = mBunlde.getString("name");
			telPhone = mBunlde.getString("telphone");
			
			tv_telPhone.setText(telPhone);
			tv_name.setText("������"+name+"������");
			
		}
		
		/**
		 * Handler
		 */
		mListViewHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				switch (msg.what) {
				case HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI:
					//��������
					notifyListView();
					threadListView.setSelection(mDataList.size()>5?5:mDataList.size());
					break;
				case HandleWhatCodeUtil.HANDLER_LISTVIEW_CHANGE:
					//��ѯ�����������ݲ���
					mContentThread = new Thread(new ListViewUpdateThread());
					mContentThread.start();
					
					break;
				case HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI_BOTTOM:
					notifyListView();
					//ѡ�����һ��
					//threadListView.setSelection(mDataList.size() - 1);
					//�Զ���ʾ�ײ�
					threadListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					
					break;
				default:
					break;
				}
				
			}
		};
		//�����̼߳�������
		mInitThread = new Thread(new ListViewThread());
		mInitThread.start();
		
		
		//ע��������ݿ��SMS�仯
		registerSMSContentObserver();
		
	}
	
	/**
	 * ע��ContentObserver
	 */
	private void registerSMSContentObserver(){
		//ע��������ݿ��SMS�仯
		Uri smsUri = Uri.parse(ConstantUtil.CONTENT_SMS);
		//�ڶ���������ʾ����SMS��������
		getContentResolver().registerContentObserver(smsUri, true,new SMSContentObServer(mListViewHandler));
	}
	
	/**
	 * ��������
	 */
	private void notifyListView(){
		
		mDataList.clear();
		mDataList.addAll(mDataTempList);
		mAdapterSmsGroupThread.notifyDataSetChanged();
	}
	
	/**
	 * Thread ��������
	 * @author HECJ
	 *
	 */
	class ListViewThread implements Runnable{

		@Override
		public void run() {
			//����ҳ��
			mPagination.setCurrPage(mPagination.getCurrPage()+1);
			//��ѯ����
			List<Object> mList = mResolverSMS.getSmsThreadId(threadId, mPagination);
			//��������
			List<SMS> temp = new ArrayList<SMS>() ;
			for(int i=0;i<mDataList.size();i++){
				temp.add(mDataList.get(i));
			}
			LogUtil.v("mDataList:"+mDataList.size());
			//���һ��
			mDataTempList.clear();
			//�������������
			mDataTempList.addAll((List<SMS>) mList.get(0));
			LogUtil.v("mDataTempList:"+temp.size());
			mDataTempList.addAll(temp);
			LogUtil.v("mDataTempList:"+mDataTempList.size());
			//��������
			mPagination.setCountSize(((Pagination)mList.get(1)).getCountSize());
			
			Message msg = new Message();
			msg.what = HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI;
			mListViewHandler.sendMessage(msg);
			
		}
		
	}
	/**
	 * ��ѯ���ݿ��Ƿ��������ݲ���
	 */
	class ListViewUpdateThread implements Runnable{
		@Override
		public void run() {
			Date lastTime = mDataList.get(mDataList.size()-1).getDate();
			List<SMS> mData = mResolverSMS.searchSMSGroupByThreadIdNewData(threadId,lastTime.getTime());
			if(mData.size()>0){
				//�������������
				mDataTempList.addAll(mData);
				Message msg = new Message();
				msg.what = HandleWhatCodeUtil.HANDLER_LISTVIEW_NOTIFI_BOTTOM;
				mListViewHandler.sendMessage(msg);
			}
		}
	}
	
	/**
	 * ����¼�
	 */
	class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sms_alarm_group_thread_sendbutton:
				String mSendContentText = mSendContent.getText().toString();
				
				if(StringUtil.isEmpty(mSendContentText)){
					Toast.makeText(ActivitySmsClockContactThread.this, "�������������", Toast.LENGTH_SHORT).show();
					break;
				}
				
				mSendContent.setText(null);
				
				//�����Ų��뱾��
				mServicesSmsClock.insertSystemSMS(telPhone, 2, mSendContentText);
				
				mServicesSmsClock.sendSMS(new Intent(), telPhone, mSendContentText);
				
				SMS sms = new SMS();
				sms.setDate(new Date());
				sms.setBody(mSendContentText);
				sms.setType("2");
				mDataTempList.add(sms);
				
				notifyListView();
				//��������
//				mAdapterSmsGroupThread.notifyDataSetChanged();
				threadListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
				//��������
				mPagination.setCountSize(mPagination.getCountSize()+1);
				
				//��ѯ�����������ݲ���
				/*mContentThread = new Thread(new ListViewUpdateThread());
				mContentThread.start();*/
				
				break;
			case R.id.tv_app_title_back:
				finish();
				break;
			default:
				break;
			}
		}
		
	}

	@Override
	public void onRefresh() {
		new Thread(new ListViewThread()).start();
	}

	@Override
	public int getDadaSize() {
		return mPagination.getCountSize();
	}
	
}
