package com.hecj.smsclock.activity.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.hecj.smsclock.R;
import com.hecj.smsclock.dialog.DialogSmsClock;
import com.hecj.smsclock.util.StringUtil;
import com.hecj.smsclock.version.AppVersionManager;
/**
 * 主窗体框架
 * @author Administrator
 *
 */
public class FrameActivity extends BaseActivity {
	
	private boolean isOpeanSystemMenu = false;
	private RelativeLayout mainView = null;
	private RelativeLayout appContent = null ;
	private RelativeLayout appSystem = null ;
	protected int widthPixels ;
	protected int heightPixels ;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//获取主页面
		mainView = (RelativeLayout) (LayoutInflater.from(this).inflate(R.layout.main, null).findViewById(R.id.app_main));
		//主页面内容
		appContent = (RelativeLayout) mainView.findViewById(R.id.app_content);
		//系统菜单
		appSystem = (RelativeLayout) mainView.findViewById(R.id.app_system);
		//获取屏幕大小
		initDisplayMetrics();
		//初始化Layout
		initLayout();
		//设置Content
		setContentView(mainView);
		//初始化系统点击事件
		initSystemOnClick();
		//初始化系统菜单ListView
		initSystemMenu();
	}
	
	/**
	 * 获取屏幕大小
	 */
	private void initDisplayMetrics(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//宽度
		widthPixels = metrics.widthPixels;
		//高度
		heightPixels = metrics.heightPixels;
		
	}
	
	/**
	 * 初始化屏幕的Layout
	 */
	private RelativeLayout.LayoutParams appSystemParams = null ;
	private FrameLayout.LayoutParams appContentParams = null ;
	private void initLayout(){
		
		//appSystem
		appSystemParams = new RelativeLayout.LayoutParams((int)(widthPixels*(3/5.0)),RelativeLayout.LayoutParams.FILL_PARENT);
		//appContent
		appContentParams = new FrameLayout.LayoutParams(widthPixels,FrameLayout.LayoutParams.FILL_PARENT);
		//隐藏左侧菜单
		appSystem.setLayoutParams(appSystemParams);
		//右侧Content
		appContent.setLayoutParams(appContentParams);
		
	}
	
	/**
	 * 设置返回的Activity
	 */
	protected void setBackActivity(final Class<?> pClass){
		//返回按钮
		this.findViewById(R.id.tv_app_title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(pClass);
				finish();
			}
		});
	}
	
	/**
	 * 设置body内容
	 * @param layoutId
	 */
	protected void setBodyLayout(int layoutId) {
		
		LinearLayout bodyLayout = (LinearLayout)this.findViewById(R.id.app_body);
		View view = LayoutInflater.from(this).inflate(layoutId, null);
		bodyLayout.addView(view);
		
	}
	/**
	 * 设置body bg
	 * @param layoutId
	 */
	protected void setBodyBackground(int bgId) {
		
		LinearLayout bodyLayout = (LinearLayout)this.findViewById(R.id.app_body);
		bodyLayout.setBackgroundResource(bgId);
		
	}
	
	/**
	 * 隐藏返回按钮
	 */
	protected void hiddenTitleBack(){
		this.findViewById(R.id.tv_app_title_back).setVisibility(Button.GONE);
	}
	
	/**
	 * 设置Title名字
	 * @param pTitleName
	 */
	protected void setTitleName(Object pTitleName){
		TextView mTitleName = (TextView)this.findViewById(R.id.app_title_name);
		if(pTitleName instanceof Integer){
			mTitleName.setText((Integer)pTitleName);
		}else if(pTitleName instanceof String){
			mTitleName.setText((String)pTitleName);
		}
	}
	
	/**
	 * 设置底点击事件
	 */
	private void initSystemOnClick(){
		
		//打开系统菜单事件
		TextView appSystemButton = (TextView)this.findViewById(R.id.app_system_button);
		appSystemButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpeanSystemMenu){
					closeSystemMenu();
				}else{
					opeanSystemMenu();
				}
			}
		});
		
		//关闭系统菜单事件
		View appButtonBack = this.findViewById(R.id.tv_app_button_back);
		appButtonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpeanSystemMenu){
					closeSystemMenu();
				}else{
					opeanSystemMenu();
				}
			}
		});
		
		//返回上个Activity事件
		this.findViewById(R.id.tv_app_title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//返回按钮
		this.findViewById(R.id.app_system_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOpeanSystemMenu){
					closeSystemMenu();
				}else{
					opeanSystemMenu();
				}
			}
		});
		
		//系统设置事件
		this.findViewById(R.id.app_menu_setting).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				LinearLayout mLinearLayout = (LinearLayout) LayoutInflater.from(FrameActivity.this).inflate(R.layout.app_menu_setting_dialog, null);
				final DialogSmsClock mDialogSmsClock = new DialogSmsClock(FrameActivity.this, mLinearLayout);
				WindowManager.LayoutParams lp = getWindow().getAttributes();   
				//dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
				//lp.x = 20; 
				// 新位置X坐标       
				//lp.y = 20; 
				// 新位置Y坐标       
				lp.width = (int)(widthPixels*0.8); 
				// 宽度        
				lp.height = (int)(heightPixels*0.4); 
				// 高度        
				lp.alpha = 0.9f; 
				// 透明度
				mDialogSmsClock.show();
				//设置dialog无标题
				mDialogSmsClock.getWindow().setAttributes(lp);
				
				final EditText etIp = (EditText)mLinearLayout.findViewById(R.id.app_menu_dialog_setting_ip);
				final EditText etPort = (EditText)mLinearLayout.findViewById(R.id.app_menu_dialog_setting_port);
				
				//设置对话框的数据
				final SharedPreferences mSharedPreferences = FrameActivity.this.getSharedPreferences("SmsClock_Server", Context.MODE_PRIVATE);
				String ip = mSharedPreferences.getString("IP", "");
				String port = mSharedPreferences.getString("PORT", "");
				if(!StringUtil.isEmpty(ip)){
					etIp.setText(ip);
				}
				if(!StringUtil.isEmpty(port)){
					etPort.setText(port);
				}
				//提交
				mLinearLayout.findViewById(R.id.app_menu_dialog_setting_sure).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences.Editor edit = mSharedPreferences.edit().putString("IP", etIp.getText().toString());
						edit.putString("PORT", etPort.getText().toString());
						edit.commit();
						edit.clear();
						
						mDialogSmsClock.cancel();
						Toast.makeText(FrameActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
					}
				});
				
				//关闭
				mLinearLayout.findViewById(R.id.app_menu_dialog_setting_exit).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mDialogSmsClock.cancel();
					}
				});
			}
			
		});
	}
	
	/**
	 * 打开系统菜单
	 */
	private void opeanSystemMenu(){
		AnimationSet animationSet = new AnimationSet(false);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//scaleAnimation.setRepeatMode(Animation.INFINITE);
//		scaleAnimation.setRepeatCount(1);
		//scaleAnimation.setFillAfter(true);
		//scaleAnimation.setDuration(1000);
		//scaleAnimation.setFillEnabled(true);
		
		TranslateAnimation translateAnimation = new TranslateAnimation(0, (int)(widthPixels*(2/4.0)), 0, 0);
		//translateAnimation.setDuration(1000);
		//translateAnimation.setRepeatMode(Animation.INFINITE);
		//translateAnimation.setFillAfter(true);
		//translateAnimation.setFillEnabled(true);
		
		//改变内容布局
		//appContentParams.setMargins((int)(widthPixels*(3/4.0)), 50, 0, 50);
		//appContent.setLayoutParams(appContentParams);
	
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(translateAnimation);
		
		animationSet.setFillAfter(true);
		animationSet.setFillEnabled(true);
		animationSet.setDuration(300);
		animationSet.setRepeatMode(Animation.INFINITE);
		
		//appContent.startAnimation(animationSet);
		
		appContentParams.setMargins((int)(widthPixels*(3/5.0)), (int)(widthPixels*(1/5.0)), 0,(int)(widthPixels*(1/5.0)));
		appContent.setLayoutParams(appContentParams);
		
		appSystem.setVisibility(View.VISIBLE);

		isOpeanSystemMenu = true;
	}
	/**
	 * 关闭系统菜单
	 */
	private void closeSystemMenu(){
		
		AnimationSet animationSet = new AnimationSet(false);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		//scaleAnimation.setRepeatMode(Animation.INFINITE);
//		scaleAnimation.setRepeatCount(1);
		//scaleAnimation.setFillAfter(true);
		//scaleAnimation.setDuration(1000);
		//scaleAnimation.setFillEnabled(true);
		
		TranslateAnimation translateAnimation = new TranslateAnimation((int)(widthPixels*(2/4.0)), 0, 0, 0);
		//translateAnimation.setDuration(1000);
		//translateAnimation.setRepeatMode(Animation.INFINITE);
		//translateAnimation.setFillAfter(true);
		//translateAnimation.setFillEnabled(true);
		
		//改变内容布局
		//appContentParams.setMargins((int)(widthPixels*(3/4.0)), 50, 0, 50);
		//appContent.setLayoutParams(appContentParams);
	
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(translateAnimation);
		
		animationSet.setFillAfter(true);
		animationSet.setFillEnabled(true);
		animationSet.setDuration(300);
		animationSet.setRepeatMode(Animation.INFINITE);
		
		//appContent.startAnimation(animationSet);
		
		//改变内容布局
		appContentParams.setMargins(0, 0, 0, 0);
		appContent.setLayoutParams(appContentParams);
		
		//appSystem
		appSystem.setVisibility(View.GONE);
		
		isOpeanSystemMenu = false;
	}
	
	/**
	 * 初始化系统菜单ListView
	 */
	private void initSystemMenu(){
		
		ListView mListView = (ListView) this.findViewById(R.id.app_menu_listview);
		List<Map<String,Object>> mData = new ArrayList<Map<String,Object>>();
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("name", "系统检测");
		mData.add(map1);
		map1 = new HashMap<String,Object>();
		map1.put("name", "系统初始化");
		mData.add(map1);
		map1 = new HashMap<String,Object>();
		map1.put("name", "版本更新");
		mData.add(map1);
		map1 = new HashMap<String,Object>();
		map1.put("name", "系统信息");
		mData.add(map1);
		SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, mData, R.layout.app_menu_listview_item, new String[]{"name"}, new int[]{R.id.app_menu_listview_name});
		mListView.setAdapter(mSimpleAdapter);
		mListView.setOnItemClickListener(new SystemMenuOnClick());
		
		//版本号
		String version = new AppVersionManager().getAppVersion(this);
		TextView tvAppVersion = (TextView)this.findViewById(R.id.app_menu_version);
		tvAppVersion.setText("当前版本:"+version);
		
		
	}
	
	
	class SystemMenuOnClick implements OnItemClickListener{
		private String appVersion = "";
		Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				//检测版本
				if(msg.what == 1234){
					
					String version = (String)msg.obj;
					appVersion = new AppVersionManager().getAppVersion(FrameActivity.this);
					if(!StringUtil.isEmpty(version)){
						if(version.trim().equals(appVersion+".apk")){
							
							Toast.makeText(FrameActivity.this, "已是最新版本!", Toast.LENGTH_SHORT).show();
						}else if(version.trim().equals("fail")){
							
							Toast.makeText(FrameActivity.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
						}else{
							
							Toast.makeText(FrameActivity.this, "服务器版本号："+version, Toast.LENGTH_SHORT).show();
							//显示对话框
							final Dialog mDialog = new DialogSmsClock(FrameActivity.this,R.layout.app_menu_version_dialog,R.style.MyDialog);
							mDialog.show();
							//删除
							mDialog.findViewById(R.id.app_menu_dialog_sure).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(FrameActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();
									//下载服务器版本 
									new Thread(new AppVersionManager(FrameActivity.this,handler,1)).start();	
									mDialog.cancel();
								}
							});
							//退出
							mDialog.findViewById(R.id.app_menu_dialog_exit).setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									mDialog.cancel();
								}
							});
						}
						
					}else{
						Toast.makeText(FrameActivity.this, "已是最新版本!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			switch (position) {
			case 2:
				//Toast.makeText(FrameActivity.this, appVersion, Toast.LENGTH_SHORT).show();
				//获取服务器版本号
				new Thread(new AppVersionManager(FrameActivity.this,handler,0)).start();		
				
				break;

			default:
				break;
			}
		}
		
	}
	
	
	
	
}	
