package com.hecj.smsclock.version;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.hecj.smsclock.util.Base64Util;
import com.hecj.smsclock.util.StringUtil;

public class AppVersionManager implements Runnable {
	private Handler mHandler = null;
	private int mType = -1;
	private Context mContext = null;
	private static String version = "";
	public AppVersionManager(Context context ,Handler handler,int type) {
		this.mHandler = handler;
		this.mType = type;
		this.mContext = context;
		
	}

	public AppVersionManager() {
	}

	/**
	 * ��ȡApp�汾��
	 */
	public String getAppVersion(Context pContext) {
		PackageManager mPackageManager = pContext.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = null;
		try {
			packInfo = mPackageManager.getPackageInfo(
					pContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packInfo.versionName;

	}

	@Override
	public void run() {
		
		
		
		try{
			
			SharedPreferences msSharedPreferences = mContext.getSharedPreferences("SmsClock_Server", Context.MODE_PRIVATE);
			String ip = msSharedPreferences.getString("IP", "");
			String port = msSharedPreferences.getString("PORT", "");
			
			switch (mType) {
			case 0:
				// ��ȡ�������汾�Žӿڵ�ַ
	//			String getV = "http://10.0.2.2:8080/version/UploadVersion?type=0";
				String getV = "http://"+ip+":"+port+"/version/UploadVersion?type=0";
				// ���ؽӿڵ�ַ
				// http://localhost:8080/version/UploadVersion?type=1
	
				version = httpClient(getV);
				if(StringUtil.isEmpty(version)){
					version = "fail" ;
				}else{
					version = getTagString(version, "content");
				}
				Message msg = new Message();
				msg.what = 1234;
				msg.obj = version;
				mHandler.sendMessage(msg);
				break;
			case 1:
				
				// ���ؽӿڵ�ַ
			   String downURL = "http://"+ip+":"+port+"/version/UploadVersion?type=1";
			   String downText = httpClient(downURL);
			   downText = getTagString(downText, "content");
			   
			   File savePath = new File("sdcard/updatedemo/");
			   if(!savePath.exists()){
				   savePath.mkdir();
			   }else{
				   for(File f:savePath.listFiles()){
					   f.delete();
				   }
			   }
			  //Toast.makeText(mContext, "��ʼ��װ:"+downText.length(), Toast.LENGTH_SHORT).show();
			   File file2 = new File("sdcard/updatedemo/"+version); 
			   if(file2.exists()){
				  file2.delete(); 
			   }
			   File file = new File("sdcard/updatedemo/"+version); 
			   FileOutputStream fos = new FileOutputStream(file);     
			   byte[] bytes = Base64Util.decode(downText);
			   fos.write(bytes,0,bytes.length);
			   fos.close();
				Intent intent = new Intent();    
				//ִ�ж���     
				intent.setAction(Intent.ACTION_VIEW);    
				//ִ�е���������     
				intent.setDataAndType(Uri.fromFile(new File("sdcard/updatedemo/"+version)), "application/vnd.android.package-archive");//���߰����˴�AndroidӦΪandroid��������ɰ�װ����      
				mContext.startActivity(intent);  
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			default:
				break;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
/*
	private String getVersion(String validateUrl) {
		String ddd = "";
		try {
			URL url = new URL(validateUrl); // ����URL����
			// ����һ��URLConnection��������ʾ��URL�����õ�Զ�̶��������
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000); // �������ӳ�ʱΪ5��
			conn.setRequestMethod("GET"); // �趨����ʽ
			conn.connect(); // ������Զ�̶����ʵ������
			// ���ش����Ӷ�ȡ��������
			DataInputStream dis = new DataInputStream(conn.getInputStream());
			// �ж��Ƿ�������Ӧ����
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.out.println("��������쳣��!!!");
			}
			int length = dis.available();
			byte[] bb = new byte[length];
			dis.read(bb);
			dis.close();
			LogUtil.v("==="+bb.length);
			LogUtil.v("==="+new String(bb));
			ddd = new String(bb);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.v(e.getMessage());
			System.out.println("�����쳣��");
		} finally {
		}
		return ddd;
	}
	*/
	private String httpClient(String url){
		/*		
 		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
		params.add(new BasicNameValuePair("param1", "�й�"));  
		params.add(new BasicNameValuePair("param2", "value2"));  
		*/
		//�Բ�������  
//		String param = URLEncodedUtils.format(params, "UTF-8");  
		//baseUrl             
		String baseUrl = url;  
		//��URL�����ƴ��  
		HttpGet getMethod = new HttpGet(baseUrl);  
		HttpClient httpClient = new DefaultHttpClient();  
		String str = null;
		try {  
		    HttpResponse response = httpClient.execute(getMethod); //����GET����  
//		    Log.i(TAG, "resCode = " + response.getStatusLine().getStatusCode()); //��ȡ��Ӧ��  
//		    Log.i(TAG, "result = " + EntityUtils.toString(response.getEntity(), "utf-8"));//��ȡ��������Ӧ����  
		    str = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ClientProtocolException e) {  
		    e.printStackTrace();  
		} catch (IOException e) {  
		    e.printStackTrace();  
		}  
		return str ;

	}

	/**
	 * ���ݱ�ǩȡ��ֵ��Ϣ
	 * 
	 * @param xml
	 * @param tag
	 * @return
	 */
	private String getTagString(String xml, String tag) {
		int beginIndex = xml.indexOf("<" + tag + ">");
		int endIndex;
		if (beginIndex != -1) {
			endIndex = xml.indexOf("</" + tag + ">");
			return xml.substring(beginIndex + tag.length() + 2, endIndex);
		}
		return null;
	}
}
