package com.hecj.smsclock.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.FrameActivity;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.services.ServicesSmsClock;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.SMSStringUtil;
import com.hecj.smsclock.util.StringUtil;

/**
 * set sms content
 * @author HECJ
 *
 */
public class ActivitySmsClockSetContent extends FrameActivity implements OnClickListener,OnLongClickListener{

	private TextView sms_alarm_id = null ;
	private Button sms_alarm_set_sms_clock = null ;
	private EditText sms_alarm_telphone = null ;
	private EditText sms_alarm_telname = null ;
	private EditText sms_alarm_content = null ;
	private DatePicker sms_alarm_datePicker1 = null ;
	private TimePicker sms_alarm_timePicker1 = null ;
	private Resources res = null;
	
	private ServicesSmsClock mServicesSmsClock = new ServicesSmsClock(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitleName(R.string.app_clock_setting);
		setBodyBackground(R.drawable.app_body_bg);
		setBodyLayout(R.layout.sms_alarm_set_content);
		res = getResources();
		sms_alarm_set_sms_clock = (Button)this.findViewById(R.id.sms_alarm_set_sms_clock);
		sms_alarm_id = (TextView)this.findViewById(R.id.sms_alarm_id);
		sms_alarm_telphone = (EditText)this.findViewById(R.id.sms_alarm_telphone);
		sms_alarm_telname = (EditText)this.findViewById(R.id.sms_alarm_telname);
		sms_alarm_content = (EditText)this.findViewById(R.id.sms_alarm_content);
		sms_alarm_datePicker1 = (DatePicker)this.findViewById(R.id.sms_alarm_datePicker1);
		sms_alarm_timePicker1 = (TimePicker)this.findViewById(R.id.sms_alarm_timePicker1);
		sms_alarm_set_sms_clock.setOnClickListener(this);
		sms_alarm_telphone.setOnLongClickListener(this);
		
		
		
		
		//初始化数据
		initViewData();
		

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sms_alarm_set_sms_clock:
			/**
			 * 添加步骤，查询当前数据库有无闹钟。
			 * 若有闹钟，查询当前插入的时间与数据库闹钟时间匹配。
			 * 若无数据，则把当前添加的数据设置为闹钟
			 */
			try{
				String telPhone = sms_alarm_telphone.getText().toString().trim(); 
				String telContent = sms_alarm_content.getText().toString().trim();
				String telName = sms_alarm_telname.getText().toString().trim();
				int year = sms_alarm_datePicker1.getYear();
				int month = sms_alarm_datePicker1.getMonth();
				int dayOfMonth = sms_alarm_datePicker1.getDayOfMonth();
				int hour = sms_alarm_timePicker1.getCurrentHour();
				int minute = sms_alarm_timePicker1.getCurrentMinute();
				
				Date mSendDate = FormatUtil.getDataByParams(year, month, dayOfMonth, hour, minute,0); //发送时间
				Long mClockTime = mSendDate.getTime();
				Date mCurrDate = FormatUtil.currDate();//当前时间
				
				if(StringUtil.isEmpty(telPhone.trim())){
					Toast.makeText(this, res.getString(R.string.sms_input_telphone_msg), Toast.LENGTH_SHORT).show();
					return;
				}
				if(StringUtil.isEmpty(telContent.trim())){
					Toast.makeText(this, res.getString(R.string.sms_input_content_msg), Toast.LENGTH_SHORT).show();
					return;
				}
				if(mSendDate.before(mCurrDate)){
					Toast.makeText(this, res.getString(R.string.sms_sent_time_exit_msg), Toast.LENGTH_SHORT).show();
					return;
				}
				
				SMSSalarm smsSalarm = new SMSSalarm();
				smsSalarm.setTelphone(telPhone);
				smsSalarm.setTelname(telName);
				smsSalarm.setContent(telContent);
				smsSalarm.setSendTime(mSendDate);
				smsSalarm.setCreatetime(mCurrDate);
				
				//判断是更新还是添加
				String smsId = sms_alarm_id.getText().toString();
				int addPid = -1;//记录插入数据库Id
				boolean isCloseActivity = false;
				if(StringUtil.isEmpty(smsId)){
					addPid = (int) mServicesSmsClock.addSmsClock(smsSalarm);
					Toast.makeText(this,SMSStringUtil.showTimeString(mSendDate, mCurrDate)+res.getString(R.string.sms_send_msg), Toast.LENGTH_SHORT).show();
				}else{
					addPid = Integer.parseInt(smsId);
					smsSalarm.setId(Integer.parseInt(smsId));
					mServicesSmsClock.updateSmsClock(smsSalarm);
					startActivity(ActivitySmsClockSearch.class);
					Toast.makeText(this, res.getString(R.string.sms_update_msg), Toast.LENGTH_SHORT).show();
					isCloseActivity = true;
				}
				
				SMSSalarm mSmsSalarm = mServicesSmsClock.getSmsSalarmLimit1();
				//说明添加是第一条数据
				if(addPid == mSmsSalarm.getId()){
					mServicesSmsClock.setSmsClock(addPid,telPhone,telName, telContent, mClockTime);
				}
				
				//关闭Activity
				if(isCloseActivity){
					finish();
				}
			
			}catch(Exception ex){
				ex.printStackTrace();
				Toast.makeText(this, res.getString(R.string.sms_setting_fail_msg), Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void initViewData(){
		//判断是否设置数据
		Intent mIntent = getIntent();
		Bundle savedInstanceState = mIntent.getExtras();
		if(savedInstanceState != null ){
			
			setTitleName(res.getString(R.string.app_clock_udpate));
			setBackActivity(ActivitySmsClockSearch.class);
			
			SMSSalarm mSMSSalarm = (SMSSalarm) savedInstanceState.getSerializable("SMSSalarm");
			sms_alarm_id.setText(String.valueOf(mSMSSalarm.getId()));
			sms_alarm_telphone.setText(mSMSSalarm.getTelphone());
			sms_alarm_telname.setText(mSMSSalarm.getTelname());
			sms_alarm_content.setText(mSMSSalarm.getContent());
			Date mSendTime = mSMSSalarm.getSendTime();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(mSendTime);
			int mYear = calendar.get(Calendar.YEAR);
			int mMonth = calendar.get(Calendar.MONDAY);
			int mDday = calendar.get(Calendar.DAY_OF_MONTH);
			int mHours = calendar.get(Calendar.HOUR_OF_DAY);
			int mMinute = calendar.get(Calendar.MINUTE);
			sms_alarm_datePicker1.init(mYear, mMonth, mDday, null);
			sms_alarm_timePicker1.setCurrentHour(mHours);
			sms_alarm_timePicker1.setCurrentMinute(mMinute);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onLongClick(View v) {
		
		switch (v.getId()) {
		case R.id.sms_alarm_telphone:
			
			Intent mIntent = new Intent(Intent.ACTION_PICK);
			mIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
			startActivityForResult(mIntent, 123);
			
			break;
		default:
			break;
		}
		
		
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 123:
			//读取选择的联系人姓名及电话
			if(resultCode == Activity.RESULT_OK){
				
				Uri mUri = data.getData();
				List<Object> mContactList = mServicesSmsClock.seachContactByUri(mUri);
				String telName = (String) mContactList.get(0);
				String telNumber = (String) mContactList.get(1);
				sms_alarm_telphone.setText(telNumber);
				sms_alarm_telname.setText(telName);
			}
			
			break;

		default:
			break;
		}
		
		
	}
}
