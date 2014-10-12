package com.hecj.smsclock.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hecj.smsclock.adapter.base.AdapterBase;

public class AdapterGridView extends AdapterBase {


	public AdapterGridView(Context pContext, List<?> pList) {
		super(pContext, pList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView mTextView = new ImageView(getmContext());
		mTextView.setBackgroundResource((Integer) (getmList().get(position)));
		return mTextView;
	}


}
