package com.hecj.smsclock.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AdapterBodyPager extends PagerAdapter {

	private List<View> mList = null ;
	
	public AdapterBodyPager(List<View> pList) {
		this.mList = pList;
	}

	@Override
	public int getCount() {
		// »ñµÃsize
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View view, int position, Object object) //

	{
		((ViewPager) view).removeView(mList.get(position));
	}

	@Override
	public Object instantiateItem(View view, int position) //

	{
		((ViewPager) view).addView(mList.get(position), 0);
		return mList.get(position);
	}
	

}
