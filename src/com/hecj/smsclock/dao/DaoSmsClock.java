package com.hecj.smsclock.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.hecj.smsclock.model.SMSSalarm;
import com.hecj.smsclock.util.FormatUtil;
import com.hecj.smsclock.util.LogUtil;

public class DaoSmsClock extends DBLink{
	
	
	public DaoSmsClock(Context context) {
		super(context);
	}

	/**
	 * add SMSSalarm
	 * @param sMSSalarm
	 */
	public long addSMSSalarm(SMSSalarm sMSSalarm){
		long n = 0;
		try{
			LogUtil.i("addSMSSalarm start...");
			
			ContentValues values = new ContentValues();
			values.put("telphone", sMSSalarm.getTelphone());
			values.put("telname", sMSSalarm.getTelname());
			values.put("content", sMSSalarm.getContent());
			values.put("sendTime", FormatUtil.formatYYYYMMDDHMS(sMSSalarm.getSendTime()));
			values.put("createtime", FormatUtil.formatYYYYMMDDHMS(sMSSalarm.getCreatetime()));
			n = getWritableDatabase().insert("smsalarm", null, values);
			
			LogUtil.i("addSMSSalarm end...n:"+n);
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}finally{
			closeDB();
		}
		return n;
	}
	
	/**
	 * 分页查询
	 * @param params -->limit[0],limit[1]
	 * @return
	 */
	public Cursor searchSMSSalarm(int startCursor,int pageSize,String currDate){

		Cursor mCursor = null ;
		try{
			LogUtil.i("searchSMSSalarm start...");
			mCursor = getReadableDatabase().query("smsalarm", new String[]{"_id","telphone","telname","content","status","sendTime","createtime"}, "sendTime>? AND status=?", new String[]{currDate,"0"}, null, null, "sendTime",startCursor+","+pageSize);
			LogUtil.i("searchSMSSalarm end...");
			
		}catch(Exception ex){
			LogUtil.i(ex.getMessage());
			ex.printStackTrace();
		}
		return mCursor ;
	}
	/**
	 * 查询多少条
	 * @return
	 */
	public Cursor searchSMSSalarmCount(String currDate){
		Cursor mCursor = null ;
		try{
			
			LogUtil.i("searchSMSSalarmCount start...");
			mCursor = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM smsalarm t WHERE t.sendTime>? AND t.status=? ", new String[]{currDate,"0"});
			LogUtil.i("searchSMSSalarmCount end...");
			
		}catch(Exception ex){
			LogUtil.i(ex.getMessage());
			ex.printStackTrace();
		}
		return mCursor; 
	}
	
	/**
	 * 分页查询
	 * search searchYetSendSMSSalarm
	 * @return
	 */
	public Cursor searchYetSendSMSSalarm(Integer... params){
		Cursor _cursor = null ;
		try{
			LogUtil.i("searchYetSendSMSSalarm start...");
			_cursor = getReadableDatabase().query("smsalarm", new String[]{"_id","telphone","telname","content","status","sendTime","createtime"}, "status=?", new String[]{"1"}, null, null, "sendTime",params[0]+","+params[1]);
			
			LogUtil.i("searchYetSendSMSSalarm end...");
			
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}
		return _cursor ;
		
	}
	/**
	 * search searchYetSendSMSSalarmCount
	 * @return
	 */
	public Cursor searchYetSendSMSSalarmCount(){
		Cursor _cursor = null ;
		try{
			LogUtil.i("searchYetSendSMSSalarmCount start...");
			_cursor = getReadableDatabase().rawQuery("SELECT COUNT(*) FROM smsalarm t where t.status=?", new String[]{"1"});
			
			LogUtil.i("searchYetSendSMSSalarmCount end...");
			
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}
		return _cursor ;
		
	}
	
	/**
	 * search searchSMSSalarmLimit1
	 * @return
	 */
	public Cursor searchSMSSalarmLimit1(){
		Cursor _cursor = null ;
		try{
			LogUtil.i("searchSMSSalarmLimit1 start...");
			_cursor = getReadableDatabase().query("smsalarm", new String[]{"_id","telphone","telname","content","status","sendTime","createtime"}, "sendTime>?", new String[]{FormatUtil.currDateYMDHMSString()}, null, null, "sendTime","1");
			LogUtil.i("searchSMSSalarmLimit1 end...");
			
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}
		return _cursor ;
		
	}
	
	/**
	 * deleteSMSSalarm
	 * @return
	 */
	public int deleteSMSSalarm(String id){
		int count = 0;
		try{
			LogUtil.i("deleteSMSSalarm start...");
//			count = db.delete("smsalarm", "_id", new String[]{id});
			getWritableDatabase().execSQL("delete from smsalarm where _id="+id);
			LogUtil.i("deleteSMSSalarm end...");
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}finally{
			closeDB();
		}
		return count ;
		
	}
	
	/**
	 * findSMSSalarmById
	 * @return
	 */
	public Cursor findSMSSalarmById(String id){
		Cursor mCursor = null ;
		try{
			LogUtil.i("findSMSSalarmById start...");
			mCursor = getWritableDatabase().query("smsalarm", new String[]{"_id","telphone","telname","content","status","sendTime","createtime"},"_id=?", new String[]{id}, null, null, null);
			LogUtil.i("findSMSSalarmById end...");
			
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}
		return mCursor ;
		
	}
	
	/**
	 * updateSMSSalarm
	 * @return
	 */
	public int updateSMSSalarm(ContentValues pValues,String pId){
		int n = 0;
		try{
			LogUtil.i("updateSMSSalarm start...");
			n = getWritableDatabase().update("smsalarm", pValues, "_id=?", new String[]{pId});
			LogUtil.i("updateSMSSalarm end...");
			
		}catch(Exception ex){
			LogUtil.e( ex.getMessage());
			ex.printStackTrace();
		}finally{
			closeDB();
		}
		return n;
		
	}
}
