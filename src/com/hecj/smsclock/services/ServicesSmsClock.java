package com.hecj.smsclock.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.ActivityMain;
import com.hecj.smsclock.dao.DaoSmsClock;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.Pagination;
import com.hecj.smsclock.util.SMSStringUtil;

public class ServicesSmsClock {
	
	private static final Object LOCK = new Object();
	private DaoSmsClock  daoSmsClock = null ;
	private Context mContext = null ;
	private static final String SMSRECEIVE = "com.hecj.demo.sms.reciver.smsReciver";
	private Resources res = null;
	public ServicesSmsClock(Context pContext){
		this.mContext = pContext ;
	}

	private DaoSmsClock getDaoSmsClock() {
		if(daoSmsClock != null){
			return daoSmsClock;
		}else{
			synchronized(LOCK){
				return daoSmsClock = new DaoSmsClock(mContext);
			}
		}
	}
	/**
	 * get Resource
	 * @return
	 */
	private Resources getRes(){
		if(res != null){
			return res;
		}else{
			return res = mContext.getResources();
		}
	}
	
	/**
	 * 分页短信闹钟列表
	 * @return
	 */
	public List<Object> getSmsClockByLimit(Pagination pPagination){
		
		List<Object> mList = new ArrayList<Object>();
		List<SMSSalarm> mDataList = new ArrayList<SMSSalarm>();
		Cursor mCursorData = null ;
		Cursor mCursorCount = null ;
		try{
			
			mCursorData = getDaoSmsClock().searchSMSSalarm((pPagination.getCurrPage()-1)*pPagination.getPageSize(),pPagination.getPageSize(),FormatUtil.formatYYYYMMDDHMS(new Date()));
			mCursorCount = getDaoSmsClock().searchSMSSalarmCount(FormatUtil.formatYYYYMMDDHMS(new Date()));
			Date currDate = FormatUtil.currDate();
			while(mCursorData.moveToNext()){
				SMSSalarm mSms = new SMSSalarm();
				mSms.setId(mCursorData.getInt(0));
				mSms.setTelphone(mCursorData.getString(1));
				mSms.setTelname(mCursorData.getString(2));
				mSms.setContent(mCursorData.getString(3));
				mSms.setStatus(mCursorData.getString(4));
				mSms.setSendTime(FormatUtil.parseYMDHMS(mCursorData.getString(5)));
				mSms.setCreatetime(FormatUtil.parseYMDHMS(mCursorData.getString(6)));
				mSms.setShowTime(SMSStringUtil.showTimeString(mSms.getSendTime(), currDate)+getRes().getString(R.string.sms_send_msg));
				mDataList.add(mSms);
			}
			
			mList.add(mDataList);
			
			mCursorCount.moveToFirst();
			pPagination.setCountSize(mCursorCount.getInt(0));
			
			mList.add(pPagination);
			
		}catch(Exception ex){
			ex.printStackTrace();
		
		}finally{
			getDaoSmsClock().closeCursor(mCursorData);
			getDaoSmsClock().closeCursor(mCursorCount);
			getDaoSmsClock().closeDB();
		}
		
		return mList;
	}
	/**
	 * 查询已经发送短信闹钟列表
	 * @return
	 */
	public List<Object> getSmsClockYetSendByLimit(Pagination pPagination){
		List<Object> mList = new ArrayList<Object>();
		List<SMSSalarm> mDataList = new ArrayList<SMSSalarm>();
		Cursor mCursorData = null ;
		Cursor mCursorCount = null ;
		try{
			
			mCursorData = getDaoSmsClock().searchYetSendSMSSalarm(pPagination.startCursor(),pPagination.getPageSize());
			mCursorCount = getDaoSmsClock().searchYetSendSMSSalarmCount();
			
			while(mCursorData.moveToNext()){
				SMSSalarm mSms = new SMSSalarm();
				mSms.setId(mCursorData.getInt(0));
				mSms.setTelphone(mCursorData.getString(1));
				mSms.setTelname(mCursorData.getString(2));
				mSms.setContent(mCursorData.getString(3));
				mSms.setStatus(mCursorData.getString(4));
				mSms.setSendTime(FormatUtil.parseYMDHMS(mCursorData.getString(5)));
				mSms.setCreatetime(FormatUtil.parseYMDHMS(mCursorData.getString(6)));
				mSms.setShowTime(SMSStringUtil.showTimeString(mSms.getSendTime(), FormatUtil.currDate())+getRes().getString(R.string.sms_send_msg));
				mDataList.add(mSms);
			}
			
			mList.add(mDataList);
			
			mCursorCount.moveToFirst();
			pPagination.setCountSize(mCursorCount.getInt(0));
			
			mList.add(pPagination);
			
		}catch(Exception ex){
			ex.printStackTrace();
		
		}finally{
			getDaoSmsClock().closeCursor(mCursorData);
			getDaoSmsClock().closeCursor(mCursorCount);
			getDaoSmsClock().closeDB();
		}
		return mList;
	}
	
	/**
	 * 查询第一条数据
	 * @return
	 */
	public SMSSalarm getSmsSalarmLimit1(){
		SMSSalarm mSms = null ;
		Cursor mCursor = null ;
		try{
			
			mCursor= getDaoSmsClock().searchSMSSalarmLimit1();
			if(mCursor.getCount() >0 ){
				mCursor.moveToFirst();
				mSms = new SMSSalarm();
				mSms.setId(mCursor.getInt(0));
				mSms.setTelphone(mCursor.getString(1));
				mSms.setTelname(mCursor.getString(2));
				mSms.setContent(mCursor.getString(3));
				mSms.setStatus(mCursor.getString(4));
				mSms.setSendTime(FormatUtil.parseYMDHMS(mCursor.getString(5)));
				mSms.setCreatetime(FormatUtil.parseYMDHMS(mCursor.getString(6)));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			getDaoSmsClock().closeCursor(mCursor);
			getDaoSmsClock().closeDB();
		}
		return mSms ;
	}
	
	/**
	 * 更新List数据
	 * @param mList
	 * @return
	 */
	public List<Object> updateListData(List<SMSSalarm> pList){
		Date mCurrDate = FormatUtil.currDate() ;
		List<Object> mList = new ArrayList<Object>();
		try{
			int n = 0;
			for(int i=0;i<pList.size();i++){
				SMSSalarm mSmsSalarm = pList.get(i);
				mSmsSalarm.setShowTime(SMSStringUtil.showTimeString(mSmsSalarm.getSendTime(), mCurrDate)+getRes().getString(R.string.sms_send_msg));
				if(mSmsSalarm.getSendTime().before(mCurrDate)){
					pList.remove(i);
					n++;
				}
			}
			
			mList.add(pList);
			mList.add(n);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return mList ;
	}
	
	/**
	 * 设置一个短信闹钟
	 * @param telPhone
	 * @param telContent
	 * @param sendTime
	 */
	public void setSmsClock(int id,String telPhone,String telName,String telContent,Long sendTime){
		try{
			
			//设置当前添加的闹钟为短信闹钟
			AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
			//set clock
			Intent mIntent = new Intent();
			mIntent.putExtra("id", String.valueOf(id));
			mIntent.putExtra("telPhone", telPhone);
			mIntent.putExtra("telName", telName);
			mIntent.putExtra("telContent", telContent);
			mIntent.setAction(SMSRECEIVE);
			PendingIntent p1 = PendingIntent.getBroadcast(mContext, 0,mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			mAlarmManager.set(AlarmManager.RTC_WAKEUP, sendTime, p1);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * 发送一条短信
	 * @param pIntent
	 * @param pTelPhone
	 * @param pTelContent
	 * @return
	 */
	public boolean sendSMS(Intent pIntent,String pTelPhone,String pTelContent){
		try{
			
			//send a sms
			SmsManager sms = SmsManager.getDefault();
			PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, 0,pIntent, 0);
			sms.sendTextMessage(pTelPhone.trim(), null, pTelContent, mPendingIntent,  null) ;
			
			return true ;
		}catch(Exception ex){
			ex.printStackTrace() ;
		}
		return false;
	}
	
	/**
	 * 发送一个Notification，点击后打开应用主页
	 * @param pIcon
	 * @param pTitle
	 * @param pContent
	 * @param pTime
	 */
	public boolean sendNotification(int pIcon, String pTitle,String pContent,long pTime){
		try{
			//获取系统NotificationManager
			NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			//实例化Notification
			Notification mNotification = new Notification(pIcon, pTitle, pTime);
			mNotification.flags = Notification.FLAG_AUTO_CANCEL;//点击后，自动消失
			mNotification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
			//为Notification添加意图
			Intent mIntent = new Intent(mContext,ActivityMain.class);
			PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, 0);
			mNotification.setLatestEventInfo(mContext, pTitle, pContent, mPendingIntent);
			mNotificationManager.notify((int)(Math.random()*1000), mNotification);
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 往数据库添加一条闹钟数据
	 * @param pSmsSalarm
	 */
	public long addSmsClock(SMSSalarm pSmsSalarm){
		try{
			
			return getDaoSmsClock().addSMSSalarm(pSmsSalarm);
		
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			getDaoSmsClock().closeDB();
		}
		return -1;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public boolean deleteSmsClock(String id){
		
		return getDaoSmsClock().deleteSMSSalarm(id)>0?true:false;
	}
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	public SMSSalarm searchSmsClockById(String id){
		SMSSalarm mSms = null ;
		Cursor mCursor = null ;
		try{
			
			mCursor = getDaoSmsClock().findSMSSalarmById(id);
			if(mCursor.getCount() >0 ){
				mCursor.moveToFirst();
				mSms = new SMSSalarm();
				mSms.setId(mCursor.getInt(0));
				mSms.setTelphone(mCursor.getString(1));
				mSms.setTelname(mCursor.getString(2));
				mSms.setContent(mCursor.getString(3));
				mSms.setStatus(mCursor.getString(4));
				mSms.setSendTime(FormatUtil.parseYMDHMS(mCursor.getString(5)));
				mSms.setCreatetime(FormatUtil.parseYMDHMS(mCursor.getString(6)));
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			getDaoSmsClock().closeCursor(mCursor);
			getDaoSmsClock().closeDB();
		}
		return mSms;
	}
	
	/**
	 * 修改数据
	 * @param pSmsSalarm
	 */
	public int updateSmsClock(SMSSalarm pSmsSalarm){
		
		ContentValues mContentValues = new ContentValues();
		mContentValues.put("telphone", pSmsSalarm.getTelphone());
		mContentValues.put("telname", pSmsSalarm.getTelname());
		mContentValues.put("content", pSmsSalarm.getContent());
		mContentValues.put("sendTime", FormatUtil.formatYMDHMS(pSmsSalarm.getSendTime()));
		mContentValues.put("createtime", FormatUtil.formatYMDHMS(pSmsSalarm.getCreatetime()));
		return getDaoSmsClock().updateSMSSalarm(mContentValues, String.valueOf(pSmsSalarm.getId()));
	}
	
	/**
	 * 更新已发短信状态
	 * @param pId
	 */
	public int updateYetSendSmsClock(String pId){
		
		ContentValues mContentValues = new ContentValues();
		mContentValues.put("status", "1");
		return getDaoSmsClock().updateSMSSalarm(mContentValues, pId);
	}
	
	
	/**
	 * 根据选择的联系人Uri,获取联系人姓名及手机号
	 * @param pUri
	 * @return
	 */
	public List<Object> seachContactByUri(Uri pUri){
		List<Object> mList = new ArrayList<Object>(2);
		Cursor mCursor = null;
		Cursor telCursor = null;
		try{
			
			mCursor = mContext.getContentResolver().query(pUri, null, null, null, null);
			String mName ="";
			String phoneNumber ="";
			if(mCursor.moveToFirst()){
				mName = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String mTelNumber = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				long mId = mCursor.getLong(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
				if(mTelNumber.equalsIgnoreCase("1")){
					telCursor= mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+mId, null, null);
					while(telCursor.moveToNext()){
						phoneNumber = telCursor.getString(telCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					}
				}
			}
			
			mList.add(mName);
			mList.add(phoneNumber);
			
		}catch(Exception ex){
			mList.add("");
			mList.add("");
			ex.printStackTrace();
		}finally{
			if(mCursor !=null && !mCursor.isClosed()){
				mCursor.close();
			}
			if(telCursor !=null && !telCursor.isClosed()){
				telCursor.close();
			}
		}
		
		return mList;
	}
	/**
	 * 往系统信息表插入一条短信
	 * @param telNumber
	 * @param type
	 * @param body
	 */
	public boolean insertSystemSMS(String telNumber,int type,String body){
		try{
			ContentResolver mContentResolver = mContext.getContentResolver();
			Uri uri = Uri.parse("content://sms/");
			ContentValues mContentValues = new ContentValues();
			mContentValues.put("address", telNumber);
			mContentValues.put("type", type);
			mContentValues.put("date", System.currentTimeMillis());
			mContentValues.put("body", body);
			mContentResolver.insert(uri, mContentValues);
		}catch(Exception ex){
			LogUtil.e(ex.getMessage());
			return false;
		}
		return true;
	}

}
