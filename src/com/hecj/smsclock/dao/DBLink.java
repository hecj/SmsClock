package com.hecj.smsclock.dao;

import com.hecj.smsclock.database.SMSSqliteOpenHelper;
import com.hecj.smsclock.util.LogUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBLink {

	private SMSSqliteOpenHelper dbOpenHelper = null ;
	private SQLiteDatabase dbW = null ;
	private SQLiteDatabase dbR = null ;
	private Context _context = null ;
	public DBLink(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this._context = context ;
	}
	
	private synchronized SMSSqliteOpenHelper getDBOpenHelper(){
		
		if(dbOpenHelper == null){
				LogUtil.i("getDBOpenHelper");
				dbOpenHelper = new SMSSqliteOpenHelper(_context, "sms", null, 1);
		}
		return dbOpenHelper ;
	}
	/**
	 * getWritableDatabase
	 * @return
	 */
	protected SQLiteDatabase getWritableDatabase(){
		if(dbW == null|| !dbW.isOpen()){
			LogUtil.i("getWritableDatabase");
			dbW = getDBOpenHelper().getWritableDatabase();
		}
		return dbW;
	}
	/**
	 * getReadableDatabase
	 * @return
	 */
	protected SQLiteDatabase getReadableDatabase(){
		if(dbR == null || !dbR.isOpen()){
			LogUtil.i("getReadableDatabase");
			dbR = getDBOpenHelper().getReadableDatabase();
		}
		return dbR;
	}
	
	
	/**
	 * close Cursor
	 */
	public void closeCursor(Cursor _cursor){
		if(_cursor!= null && !_cursor.isClosed()){
			_cursor.close();
		}
	}
	/**
	 * close db
	 */
	public void closeDB(){
		if(dbW!=null && dbW.isOpen()){
			dbW.close();
		}
		if(dbR!=null && dbR.isOpen()){
			dbR.close();
		}
	}
	
	
}
