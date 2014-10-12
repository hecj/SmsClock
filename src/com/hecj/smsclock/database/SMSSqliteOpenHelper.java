package com.hecj.smsclock.database;

import com.hecj.smsclock.util.LogUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SMSSqliteOpenHelper extends SQLiteOpenHelper {

	public SMSSqliteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	/**

	CREATE TABLE smsalarm ( 
    _id         INTEGER         PRIMARY KEY AUTOINCREMENT,
    telphone   VARCHAR( 15 ),
    telname	   VARCHAR( 50 ),
    content    VARCHAR( 500 ),
    status	   VARCHAR( 1 ) DEFAULT (0),
    sendTime   DATETIME,
    createtime DATETIME
    
    status 0:Î´·¢ËÍ£¬1:ÒÑ·¢ËÍ
);

	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		StringBuffer createTable = new StringBuffer();
		createTable.append("CREATE TABLE smsalarm ( ");
		createTable.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
		createTable.append("telphone VARCHAR(15),");
		createTable.append("telname	VARCHAR(50),");
		createTable.append("content VARCHAR(500),");
		createTable.append("status VARCHAR(1) DEFAULT (0),");
		createTable.append("sendTime DATETIME,");
		createTable.append("createtime DATETIME");
		createTable.append(");");
		
		try{
			db.execSQL(createTable.toString());
			LogUtil.i("create table success!");
		}catch(Exception ex){
			ex.printStackTrace();
			LogUtil.e("create table fail!");
			LogUtil.e(ex.getMessage());
		}finally{
			//db.close();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/*if(newVersion>oldVersion){
			StringBuffer createTable = new StringBuffer();
			createTable.append("CREATE TABLE smsalarm ( ");
			createTable.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
			createTable.append("telphone VARCHAR(15),");
			createTable.append("telname	VARCHAR(50),");
			createTable.append("content VARCHAR(500),");
			createTable.append("status VARCHAR(1) DEFAULT (0),");
			createTable.append("sendTime DATETIME,");
			createTable.append("createtime DATETIME");
			createTable.append(");");
			
			try{
				db.execSQL(createTable.toString());
				Log.i("hecjlog", "onUpgrade table success!");
			}catch(Exception ex){
				ex.printStackTrace();
				Log.e("hecjlog", "onUpgrade table fail!");
				Log.e("hecjlog", ex.getMessage());
			}finally{
				//db.close();
			}
		}*/
	}

}
