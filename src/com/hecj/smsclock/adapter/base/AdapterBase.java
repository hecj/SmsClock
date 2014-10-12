package com.hecj.smsclock.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {

	private List<?> mList = null; 
	private Context mContext = null;
	public AdapterBase(Context pContext,List<?> pList){
		this.mList = pList;
		this.mContext = pContext;
	}
	
	public List<?> getmList() {
		return mList;
	}

	public Context getmContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	protected View getLayout(int pResourceId) {
		return LayoutInflater.from(mContext).inflate(pResourceId, null);
	}

}
