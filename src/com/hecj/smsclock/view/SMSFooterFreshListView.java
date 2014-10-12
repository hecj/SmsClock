package com.hecj.smsclock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
/**
 * 自定义ListView
 * 添加头部刷新
 * @author HECJ
 *
 */
public class SMSFooterFreshListView extends ListView implements OnScrollListener{
	
	/**
	 * 显示的第一个列表项位置
	 */
	private int lastVisibleItem = -1;
	/**
	 * 刷新接口
	 */
	private OnRefreshListViewListener mOnRefreshListViewListener ;
	
	/**
	 *是否符合加载数据条件 
	 */
	private boolean isLoadData = false; 
	
	/**
	 * 判断是否同一次点击
	 */
	private boolean isTouch = false ;
	/**
	 * 没有数据标志
	 */
	private boolean noData = false ;
	
	/**
	 * 记录第一次点击的位置
	 */
	private float firstY = 0 ;
	
	
	public SMSFooterFreshListView(Context context) {
		super(context);
		init();
	}

	public SMSFooterFreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SMSFooterFreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/**
	 * 添加底部刷新
	 */
	private TextView mListViewFooterFresh = null ;
	private void init(){
		mListViewFooterFresh = new TextView(this.getContext());
		mListViewFooterFresh.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		mListViewFooterFresh.setVisibility(View.GONE);
		addFooterView(mListViewFooterFresh);
		//注册滑动事件
		setOnScrollListener(this);
	}
	
	/**
	 * 刷新界面接口
	 */
	public interface OnRefreshListViewListener{
		/**'
		 * 函数回调子类方法(刷新)
		 */
		public void onRefresh();
		/**'
		 * 函数回调子类方法（得到数据总数，用于判断是否继续刷新）
		 */
		public int getDadaSize();
		
	}
	
	/**
	 * 注册监听刷新事件
	 * @param mOnRefreshListViewListener
	 */
	public void setOnRefreshListViewListener(OnRefreshListViewListener mOnRefreshListViewListener){
		this.mOnRefreshListViewListener = mOnRefreshListViewListener ;
	}
	
	public void onRefresh(){
		if(this.mOnRefreshListViewListener != null){
			this.mOnRefreshListViewListener.onRefresh();
		}
	}
	
	public int getDadaSize(){
		if(this.mOnRefreshListViewListener != null){
			return this.mOnRefreshListViewListener.getDadaSize();
		}
		return -1;
	}
	
	/**
	 * Touch事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN :
			
			firstY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE :
			//还有数据，继续加载
			if(!isTouch && lastVisibleItem == totalItemCount && totalItemCount <this.getDadaSize()){
				mListViewFooterFresh.setText("松手后加载...");
				isLoadData = true ;
				isTouch = true ;
			}else if(!isTouch && lastVisibleItem == totalItemCount && totalItemCount == this.getDadaSize() && this.getDadaSize()>0){
				//没有数据了
				mListViewFooterFresh.setText("没有数据了");
				noData = true;
				isTouch = true ;
			}
			mListViewFooterFresh.setVisibility(View.VISIBLE);
			//滑动时改变刷新的Padding
			if(isTouch){
				float y = firstY-ev.getY();
				mListViewFooterFresh.setPadding(0, 0, 0, (int)y);
			}
			break;
		case MotionEvent.ACTION_UP :
			//加载数据
			if( isLoadData && mListViewFooterFresh.getPaddingBottom() >0 ){
				mListViewFooterFresh.setText("正在加载加载...");
				this.onRefresh();
				mListViewFooterFresh.setText("加载完毕!");
				isLoadData = false ;
				
			}else if(noData){
				noData = false;
			}
			if(isTouch){
				mListViewFooterFresh.setPadding(0, 0, 0, 0);
			}
			mListViewFooterFresh.setVisibility(View.GONE);
			
			isTouch = false ;
			firstY = 0;
			break;

		default:
			break;
		}
		
		return super.onTouchEvent(ev);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
	/**
	 * 滑动事件
	 */
	private int totalItemCount;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem = firstVisibleItem + visibleItemCount -1 ;
		this.totalItemCount = totalItemCount - 1 ;

	}

}
