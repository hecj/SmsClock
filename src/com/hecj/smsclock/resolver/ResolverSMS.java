package com.hecj.smsclock.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.hecj.smsclock.model.SMS;
import com.hecj.smsclock.util.ConstantUtil;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.LogUtil;
import com.hecj.smsclock.util.Pagination;
import com.hecj.smsclock.util.StringUtil;

/**
 * 简单封装自己的ContentResolver
 * @author HECJ
 *
 */
public class ResolverSMS{

	private static String CONTENT_SMS = "content://sms/";
	private Context mContext = null ;
	public ResolverSMS(Context context) {
		this.mContext = context;
	}

	
	/**
	 * 查询短信分组列表<br>
	 * SQL原型：<br>
SELECT _id,                                                                             <br>
 &nbsp;      thread_id,                                                                       <br>
 &nbsp;      (SELECT s.address FROM canonical_addresses s WHERE s._id = thread_id )           <br>
 &nbsp;      address,                                                                         <br>
 &nbsp;      person,                                                                          <br>
 &nbsp;      protocol,                                                                        <br>
 &nbsp;      read,                                                                            <br>
 &nbsp;      status,                                                                          <br>
 &nbsp;      type,                                                                            <br>
 &nbsp;      service_center,                                                                  <br>
 &nbsp;      body,                                                                            <br>
 &nbsp;      date,                                                                            <br>
 &nbsp;      (SELECT count( * ) FROM sms m WHERE m.thread_id = sms.thread_id ) count          <br>
           FROM sms                                                                              <br>
 WHERE                                                                                  <br>
&nbsp; _id IN (SELECT s._id FROM ( SELECT _id,max( date ) FROM sms GROUP BY thread_id  ) s )  <br> 
&nbsp;  ORDER BY date DESC;<br>
	 * @return
	 */
	public List<SMS> searchSMSGroupByThreadId(){
		
		List<SMS> mList = new ArrayList<SMS>();
		List<SMS> mDataList = new ArrayList<SMS>();
		Map<String,SMS> mDataMap = new HashMap<String,SMS>();
		Cursor mCursor = null ;
		Cursor mPhoneCursor = null ;
	
		try{
			//通过特殊手段实现多表级联查询
			String[] projection = {
						"_id",
				       "thread_id",
				       "(select a.address from sms s,threads t,canonical_addresses a where s.thread_id=t._id and t.recipient_ids= a._id and s.thread_id = sms.thread_id) address",
				       //"address",
				       "person",
				       "protocol",
				       "read",
				       "status",
				       "type",
				       "service_center",
				       "body",
				       "date",
				       "(select count(*) from sms m where m.thread_id=sms.thread_id) count"
			};
			mCursor = mContext.getContentResolver().query(Uri.parse(CONTENT_SMS), projection,
							null, null,"thread_id,date DESC");
/*			mCursor = mContext.getContentResolver().query(Uri.parse(CONTENT_SMS), projection,
					"_id in " +
							"(SELECT _id FROM (  SELECT _id, max( date ) FROM sms " +
							"where thread_id in (SELECT thread_id FROM threads) " +
							"GROUP BY thread_id ))", null,"date DESC");
*/			while(mCursor.moveToNext()){
				
				SMS sms = new SMS();
				sms.setId(mCursor.getString(mCursor.getColumnIndex("_id")));
				sms.setThreadId(mCursor.getString(mCursor.getColumnIndex("thread_id")));
				sms.setAddress(mCursor.getString(mCursor.getColumnIndex("address")));
				sms.setPerson(mCursor.getString(mCursor.getColumnIndex("person")));
				sms.setProtocol(mCursor.getString(mCursor.getColumnIndex("protocol")));
				sms.setRead(mCursor.getString(mCursor.getColumnIndex("read")));
				sms.setStatus(mCursor.getString(mCursor.getColumnIndex("status")));
				sms.setType(mCursor.getString(mCursor.getColumnIndex("type")));
				sms.setServiceCenter(mCursor.getString(mCursor.getColumnIndex("service_center")));
				sms.setBody(mCursor.getString(mCursor.getColumnIndex("body")));
				sms.setDate(FormatUtil.getDate(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("date")))));
				sms.setCount(mCursor.getInt(mCursor.getColumnIndex("count")));
				mList.add(sms);
			}

			//去重 获取最新时间
			for(SMS sms : mList){
				if(!mDataMap.containsKey(sms.getThreadId())){
					mDataMap.put(sms.getThreadId(), sms);
				}
			}
			
			//拼接手机号
			String telPhoneStr = "";
			//添加数据
			Iterator<Entry<String,SMS>> iterator = mDataMap.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String,SMS> entry = (Entry<String,SMS>)iterator.next();
				SMS s = (SMS) entry.getValue();
				mDataList.add(s);
				if(!StringUtil.isEmpty(s.getAddress())){
					telPhoneStr = s.getAddress()+",";
				}
			}
			
			for(int i=0;i<mDataList.size();i++){
				for(int j=i;j<mDataList.size();j++){
					if(mDataList.get(i).getDate().before(mDataList.get(j).getDate())){
						SMS temp = mDataList.get(i);
						mDataList.set(i, mDataList.get(j));
						mDataList.set(j, temp);
					}
				}
			}
			
			
			//查询联系人手机号备注
			String[] selectionPhone = {Phone.NUMBER,Phone.DISPLAY_NAME};
			/*mPhoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, selectionPhone ,
					"data1 in (?)", new String[]{telPhoneStr.substring(0, telPhoneStr.length()-1)}, null);*/
			mPhoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, selectionPhone ,
					null,null, null);
			//将手机号和联系人姓名放入map
			Map<String,String> tempMap  = new HashMap<String,String>();
			while(mPhoneCursor.moveToNext()){
				String telPhone = mPhoneCursor.getString(0).replaceAll(" ", "").replace("+86", "");
				String telName = mPhoneCursor.getString(1);
				tempMap.put(telPhone, telName);
			}
			
			//替换联系人姓名
			for(int i=0;i<mDataList.size();i++){
				SMS s = mDataList.get(i);
				if(tempMap.containsKey(s.getAddress().replaceAll(" ", "").replace("+86", ""))){
					s.setPerson(tempMap.get(s.getAddress().replaceAll(" ", "").replace("+86", "")));
				}
			}
			
			
		}catch(Exception ex){
			LogUtil.e(ex.getMessage());
		}finally{
			if(mCursor != null && !mCursor.isClosed()){
				mCursor.close();
			}
			if(mPhoneCursor != null && !mPhoneCursor.isClosed()){
				mPhoneCursor.close();
			}
		}
		return mDataList;
	}
	
	/**
	 * 分页查询 SMS Thread
	 * @param pagination
	 * @return
	 */
	public List<Object> getSmsThreadId(String threadId,Pagination pagination){
		List<Object> mList = new ArrayList<Object>();
		List<SMS> mDataList = new ArrayList<SMS>();
		
		Cursor mDataCursor = null ;
		Cursor mCountCursor = null ;
		try{
			
			String[] projection = {
						"_id",
				       "thread_id",
				       "(select s.address from canonical_addresses s where s._id=thread_id) address",
				       "person",
				       "protocol",
				       "read",
				       "status",
				       "type",
				       "service_center",
				       "body",
				       "date"
			};
			mDataCursor = mContext.getContentResolver().query(Uri.parse(ConstantUtil.CONTENT_SMS), projection, "_id in (select _id from sms s where s.thread_id=? order by s.date DESC limit ?,?)", new String[]{threadId,String.valueOf(pagination.startCursor()),String.valueOf(pagination.getPageSize())}, "date DESC");
			mCountCursor = mContext.getContentResolver().query(Uri.parse(ConstantUtil.CONTENT_SMS), new String[]{"count(*)"}, "thread_id=?", new String[]{threadId}, null);
			LogUtil.v("mDataCursor:"+mDataCursor.getCount()+"");
			while(mDataCursor.moveToNext()){
				
				SMS sms = new SMS();
				sms.setId(mDataCursor.getString(mDataCursor.getColumnIndex("_id")));
				sms.setThreadId(mDataCursor.getString(mDataCursor.getColumnIndex("thread_id")));
				sms.setAddress(mDataCursor.getString(mDataCursor.getColumnIndex("address")));
				sms.setPerson(mDataCursor.getString(mDataCursor.getColumnIndex("person")));
				sms.setProtocol(mDataCursor.getString(mDataCursor.getColumnIndex("protocol")));
				sms.setRead(mDataCursor.getString(mDataCursor.getColumnIndex("read")));
				sms.setStatus(mDataCursor.getString(mDataCursor.getColumnIndex("status")));
				sms.setType(mDataCursor.getString(mDataCursor.getColumnIndex("type")));
				sms.setServiceCenter(mDataCursor.getString(mDataCursor.getColumnIndex("service_center")));
				sms.setBody(mDataCursor.getString(mDataCursor.getColumnIndex("body")));
				sms.setDate(FormatUtil.getDate(Long.parseLong(mDataCursor.getString(mDataCursor.getColumnIndex("date")))));
				mDataList.add(sms);
			}
			//排序
			Collections.sort(mDataList, new Comparator<SMS>() {
				public int compare(SMS s1, SMS s2) {
					return s1.getDate().before(s2.getDate()) ? -1 : 1;
				}
			});
			
			
			if(mCountCursor.moveToFirst()){
				int count = mCountCursor.getInt(0);
				pagination.setCountSize(count);
			}
			
			mList.add(mDataList);
			mList.add(pagination);
			LogUtil.v("count:"+pagination.getCountSize()+"");
		
		}catch(Exception ex){
			mList.add(mDataList);
			mList.add(pagination);
			LogUtil.e(ex.getMessage());
		}finally{
			
			if(mDataCursor != null && !mDataCursor.isClosed()){
				mDataCursor.close();
			}
			if(mCountCursor != null && !mCountCursor.isClosed()){
				mCountCursor.close();
			}
		}
		
		return mList ;
	}
	
	/**
	 * 根据时间查询最新数据
	 * @param threadId
	 * @param time
	 * @return
	 */
	public List<SMS> searchSMSGroupByThreadIdNewData(String threadId,long time){
		
		List<SMS> mList = new ArrayList<SMS>();
		List<SMS> mDataList = new ArrayList<SMS>();
		Map<String,SMS> mDataMap = new HashMap<String,SMS>();
		Cursor mCursor = null ;
		try{
			//通过特殊手段实现多表级联查询
			String[] projection = {
						"_id",
				       "thread_id",
				       "(select a.address from sms s,threads t,canonical_addresses a where s.thread_id=t._id and t.recipient_ids= a._id and s.thread_id = sms.thread_id) address",
				       //"address",
				       "person",
				       "protocol",
				       "read",
				       "status",
				       "type",
				       "service_center",
				       "body",
				       "date",
				       "(select count(*) from sms m where m.thread_id=sms.thread_id) count"
			};
			mCursor = mContext.getContentResolver().query(Uri.parse(CONTENT_SMS), projection,
							"thread_id=? and date>?", new String[]{threadId,String.valueOf(time)},"date DESC");
			while(mCursor.moveToNext()){
				
				SMS sms = new SMS();
				sms.setId(mCursor.getString(mCursor.getColumnIndex("_id")));
				sms.setThreadId(mCursor.getString(mCursor.getColumnIndex("thread_id")));
				sms.setAddress(mCursor.getString(mCursor.getColumnIndex("address")));
				sms.setPerson(mCursor.getString(mCursor.getColumnIndex("person")));
				sms.setProtocol(mCursor.getString(mCursor.getColumnIndex("protocol")));
				sms.setRead(mCursor.getString(mCursor.getColumnIndex("read")));
				sms.setStatus(mCursor.getString(mCursor.getColumnIndex("status")));
				sms.setType(mCursor.getString(mCursor.getColumnIndex("type")));
				sms.setServiceCenter(mCursor.getString(mCursor.getColumnIndex("service_center")));
				sms.setBody(mCursor.getString(mCursor.getColumnIndex("body")));
				sms.setDate(FormatUtil.getDate(Long.parseLong(mCursor.getString(mCursor.getColumnIndex("date")))));
				sms.setCount(mCursor.getInt(mCursor.getColumnIndex("count")));
				mList.add(sms);
			}

			//去重 获取最新时间
			for(SMS sms : mList){
				if(!mDataMap.containsKey(sms.getThreadId())){
					mDataMap.put(sms.getThreadId(), sms);
				}
			}
			
			//添加数据
			Iterator<Entry<String,SMS>> iterator = mDataMap.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<String,SMS> entry = (Entry<String,SMS>)iterator.next();
				mDataList.add((SMS) entry.getValue());
			}
			
			for(int i=0;i<mDataList.size();i++){
				for(int j=i;j<mDataList.size();j++){
					if(mDataList.get(i).getDate().before(mDataList.get(j).getDate())){
						SMS temp = mDataList.get(i);
						mDataList.set(i, mDataList.get(j));
						mDataList.set(j, temp);
					}
				}
			}
			
		}catch(Exception ex){
			LogUtil.e(ex.getMessage());
		}finally{
			if(mCursor != null && !mCursor.isClosed()){
				mCursor.close();
			}
		}
		return mDataList;
	}
	
}
