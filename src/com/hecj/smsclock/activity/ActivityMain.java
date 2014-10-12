package com.hecj.smsclock.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.TableRow;
import android.widget.TextView;

import com.hecj.smsclock.R;
import com.hecj.smsclock.activity.base.FrameActivity;
import com.hecj.smsclock.adapter.AdapterBodyPager;

public class ActivityMain extends FrameActivity implements OnClickListener{

	private TextView sms_alarm_set1 = null ;
	private TextView sms_alarm_exist_set = null ;
	private TextView sms_alarm_contact = null ;
	private TextView sms_alarm_yet_send = null ;
	private List<View> mDataView = new ArrayList<View>();
	private ViewPager mViewPager = null ;
	private View mPage1 = null ;
	private View mPage2 = null ;
	private TextView leftIV = null ;
	private TextView rightIV = null ;
	private TextView app_point_select = null ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		hiddenTitleBack();
		setTitleName(R.string.app_title_name);
		setBodyBackground(R.drawable.app_body_bg);
		//设置body内容
		setBodyLayout(R.layout.app_main_body);
		mViewPager = (ViewPager)this.findViewById(R.id.app_body_viewPager);
		//绑定ViewPager
		bindViewPagerData();
		
		bindIV();
		//绑定GridView
		//bindGridView();
	}
	
	private void bindIV() {
		// TODO Auto-generated method stub
		//获取左右2个小图标
		leftIV = (TextView) this.findViewById(R.id.left_iv);
		rightIV = (TextView) this.findViewById(R.id.right_iv);
		app_point_select = (TextView) this.findViewById(R.id.app_point_select);
		sms_alarm_set1 = (TextView) mPage1.findViewById(R.id.sms_alarm_set1);
		sms_alarm_set1.setOnClickListener(this);
		sms_alarm_exist_set = (TextView) mPage1.findViewById(R.id.sms_alarm_exist_set);
		sms_alarm_exist_set.setOnClickListener(this);
		sms_alarm_contact = (TextView) mPage1.findViewById(R.id.sms_alarm_contact);
		sms_alarm_contact.setOnClickListener(this);
		sms_alarm_yet_send = (TextView) mPage1.findViewById(R.id.sms_alarm_yet_send);
		sms_alarm_yet_send.setOnClickListener(this);
	}

	/**
	 * 绑定ViewPager数据
	 */
	private void bindViewPagerData(){
		//添加viewPager页
		mPage1 = LayoutInflater.from(this).inflate(R.layout.app_viewpager_page1, null);
	    mPage2 = LayoutInflater.from(this).inflate(R.layout.app_viewpager_page2, null);
	    
	    //初始化grid item的宽和高
	    int width= (widthPixels-80-30)/2;
	    int height= (heightPixels-90-30)/2;
	    int width_height = width>height?height:width;
	    
	    TableRow.LayoutParams params = new TableRow.LayoutParams();
	    params.width = width_height;
	    params.height = width_height;
	    
	    mPage1.findViewById(R.id.sms_alarm_set1).setLayoutParams(params);
	    mPage1.findViewById(R.id.sms_alarm_exist_set).setLayoutParams(params);
	    mPage1.findViewById(R.id.sms_alarm_contact).setLayoutParams(params);
	    mPage1.findViewById(R.id.sms_alarm_yet_send).setLayoutParams(params);
	    
	    mPage2.findViewById(R.id.sms_alarm_wait).setLayoutParams(params);
	    
	    mDataView.add(mPage1);
	    mDataView.add(mPage2);
		//设置Adapter
		mViewPager.setAdapter(new AdapterBodyPager(mDataView));
		mViewPager.setCurrentItem(0);
		//viewPager事件
		mViewPager.setOnPageChangeListener(new ViewPagerOnPageChangeListener());
	}
	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	
	/**
	 * onclick view 
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.sms_alarm_set1:
			
			startActivity(ActivitySmsClockSetContent.class);
			break;
		case R.id.sms_alarm_exist_set:
			
			startActivity(ActivitySmsClockSearch.class);
			
			break;
		case R.id.sms_alarm_contact:
			//启动新的Activity
			startActivity(ActivitySmsClockContact.class);
			
			break;
		case R.id.sms_alarm_yet_send:
			//启动新的Activity
			startActivity(ActivitySmsClockYetSend.class);
			
			break;
		default:
			break;
		}
	}
	
	/**
	 *ViewPager滑动页事件
	 */
	private class ViewPagerOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			RotateAnimation rotateAnimation = null;
			if(arg0 == 0){
				leftIV.setVisibility(View.GONE);
				rightIV.setVisibility(View.VISIBLE);
				rotateAnimation = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,  RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			}else if(mViewPager.getChildCount()-1 == arg0 ){
				rightIV.setVisibility(View.GONE);
				leftIV.setVisibility(View.VISIBLE);
				rotateAnimation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,  RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			}else{
				leftIV.setVisibility(View.VISIBLE);
				rightIV.setVisibility(View.VISIBLE);
			}
			/**
			 * 改变小图标点
			 */
			rotateAnimation.setDuration(0);
			rotateAnimation.setFillAfter(true); //设置动画结束后停在结束位置
			
			app_point_select.startAnimation(rotateAnimation);
		}
		
	}

}
