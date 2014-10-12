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
 * �Զ���ListView
 * ���ͷ��ˢ��
 * @author HECJ
 *
 */
public class SMSFooterFreshListView extends ListView implements OnScrollListener{
	
	/**
	 * ��ʾ�ĵ�һ���б���λ��
	 */
	private int lastVisibleItem = -1;
	/**
	 * ˢ�½ӿ�
	 */
	private OnRefreshListViewListener mOnRefreshListViewListener ;
	
	/**
	 *�Ƿ���ϼ����������� 
	 */
	private boolean isLoadData = false; 
	
	/**
	 * �ж��Ƿ�ͬһ�ε��
	 */
	private boolean isTouch = false ;
	/**
	 * û�����ݱ�־
	 */
	private boolean noData = false ;
	
	/**
	 * ��¼��һ�ε����λ��
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
	 * ��ӵײ�ˢ��
	 */
	private TextView mListViewFooterFresh = null ;
	private void init(){
		mListViewFooterFresh = new TextView(this.getContext());
		mListViewFooterFresh.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		mListViewFooterFresh.setVisibility(View.GONE);
		addFooterView(mListViewFooterFresh);
		//ע�Ử���¼�
		setOnScrollListener(this);
	}
	
	/**
	 * ˢ�½���ӿ�
	 */
	public interface OnRefreshListViewListener{
		/**'
		 * �����ص����෽��(ˢ��)
		 */
		public void onRefresh();
		/**'
		 * �����ص����෽�����õ����������������ж��Ƿ����ˢ�£�
		 */
		public int getDadaSize();
		
	}
	
	/**
	 * ע�����ˢ���¼�
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
	 * Touch�¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_DOWN :
			
			firstY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE :
			//�������ݣ���������
			if(!isTouch && lastVisibleItem == totalItemCount && totalItemCount <this.getDadaSize()){
				mListViewFooterFresh.setText("���ֺ����...");
				isLoadData = true ;
				isTouch = true ;
			}else if(!isTouch && lastVisibleItem == totalItemCount && totalItemCount == this.getDadaSize() && this.getDadaSize()>0){
				//û��������
				mListViewFooterFresh.setText("û��������");
				noData = true;
				isTouch = true ;
			}
			mListViewFooterFresh.setVisibility(View.VISIBLE);
			//����ʱ�ı�ˢ�µ�Padding
			if(isTouch){
				float y = firstY-ev.getY();
				mListViewFooterFresh.setPadding(0, 0, 0, (int)y);
			}
			break;
		case MotionEvent.ACTION_UP :
			//��������
			if( isLoadData && mListViewFooterFresh.getPaddingBottom() >0 ){
				mListViewFooterFresh.setText("���ڼ��ؼ���...");
				this.onRefresh();
				mListViewFooterFresh.setText("�������!");
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
	 * �����¼�
	 */
	private int totalItemCount;
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.lastVisibleItem = firstVisibleItem + visibleItemCount -1 ;
		this.totalItemCount = totalItemCount - 1 ;

	}

}
