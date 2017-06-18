package com.new1.settop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Connect extends Activity{
    /** Called when the activity is first created. */
	private UrlImageLoader urlImageload;
	private String mExpiredate = null;
	
	private ImageView imgConnect1;
	private ImageView imgConnect2;
	
	private ImageView contop1;
	private ImageView contop2;
	
	private AnimationDrawable frameAnimation1;
	private AnimationDrawable frameAnimation2;
	
	private ViewExpireDateTask viewExpireDateTask = null;
	private int tryCount = 0;
	
	private boolean disconnected = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	urlImageload = new UrlImageLoader();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect);

        imgConnect1 = (ImageView) findViewById(R.id.imgConnect1);
        imgConnect2 = (ImageView) findViewById(R.id.imgConnect2);
        
        contop1 = (ImageView) findViewById(R.id.contop1);
        contop2 = (ImageView) findViewById(R.id.contop2);
        contop1.setVisibility(View.VISIBLE);
        
        imgConnect1.setBackgroundResource(R.drawable.connect);
    	imgConnect2.setBackgroundResource(R.drawable.connect);
        
    	
        
        if(!Util.checkNetwordState(this.getBaseContext())){
        	contop1.setVisibility(View.GONE);
        	imgConnect1.setBackgroundResource(R.drawable.connect_x1);
        	frameAnimation1 = (AnimationDrawable) imgConnect1.getBackground();
        	frameAnimation1.start();
        	disconnected = true;
        	/*AlertDialog.Builder alt_bld = new AlertDialog.Builder(Connect.this);
		    alt_bld.setMessage(R.string.nonet);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                Intent intent = new Intent(Connect.this, HantvActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();
			return;*/
        }else{
        	contop1.setVisibility(View.GONE);
        	contop2.setVisibility(View.VISIBLE);
        	imgConnect1.setBackgroundResource(R.drawable.connect_check);
        	frameAnimation1 = (AnimationDrawable) imgConnect1.getBackground();
        	frameAnimation1.start();
        	
        	viewExpireDateTask = new ViewExpireDateTask(getBaseContext());
        	viewExpireDateTask.execute();
        	
        	
        }
    }
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(urlImageload != null)
			urlImageload = null;
		if(viewExpireDateTask != null){
			viewExpireDateTask.cancel(true);
			viewExpireDateTask = null;
		}
		
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	boolean go = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
		/*	Intent intent = new Intent(Connect.this, HantvActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(!go){
				go = true;
				startActivity(intent);
				this.finish();
			}*/
		
		if(disconnected){ 	
			finish();
  	  	}
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
  	  	if(disconnected){
			finish();
  	  	}
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			//if(isLoaded){
			/*	Intent intent = new Intent(Connect.this, HantvActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				this.finish();*/
			//}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class ViewExpireDateTask extends AsyncTask<String, Integer, Boolean> {
    	private  ArrayList<BroadcastList> list = null;
    	private String text;
    	private Context mContext;
    	private Bitmap image;
    	private boolean success;
    	
    	public ViewExpireDateTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Boolean doInBackground(String... params) {
    		// TODO Auto-generated method stub
//    		InetAddress ia = null;
//    		try {
//    			ia = InetAddress.getByName(Constant.url);
//    		} catch(IOException ie){
//    		}
//    		Log.e(null, ia.getHostAddress());
//    		Runtime runTime = Runtime.getRuntime();
//    		String host = Constant.host;
//    		String cmd = "ping -c 1 -W 10 "+ "192.168.0.1";//ia.getHostAddress(); //-c 1은 반복 횟수를 1번만 날린다는 뜻
//    		//202.179.177.22
//    		Process proc = null;
//
//    		try {
//    			proc = runTime.exec(cmd);
//    		} catch(IOException ie){
//    			Log.d("runtime.exec()", ie.getMessage());
//    		}
//    		try {
//    			proc.waitFor();
//    		} catch(InterruptedException ie){
//    			Log.d("proc.waitFor", ie.getMessage());
//    		}
//    		//여기서 반환되는 ping 테스트의 결과 값은 0, 1, 2 중 하나이다.
//    		// 0 : 성공, 1 : fail, 2 : error이다.
//    		int result = proc.exitValue();
//
//    		if (result == 0) {
//    			success = true;
//    		} else {
//    			success = false;
//    		}
    		
    		try{
	    		String strJson = "";
				PostHttp postmake = new PostHttp();
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("update", "U"));
				Log.e(null, "111");
				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/broadcastlist.php", nameValuePairs);
				JSONArray jArray = null;
				Log.e(null, "2" +strJson);
				jArray = new JSONArray(strJson);	
				JSONObject json_data = jArray.getJSONObject(0); 
				Log.e(null, "aaa");
				success = true;
			}catch(JSONException e){
				e.printStackTrace();
				Log.e(null, "exit");
				success = false;
			}
    		return success;
    	}
    
    	@Override
    	protected void onPostExecute(Boolean result) {
    		// TODO Auto-generated method stub
    		if(success){
    			contop2.setVisibility(View.GONE);
	        	imgConnect2.setBackgroundResource(R.drawable.connect_check);
	        	frameAnimation2 = (AnimationDrawable) imgConnect2.getBackground();
	        	frameAnimation2.start();
	        	
				Intent intent = new Intent(Connect.this, HantvActivity2.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
    		}else{
    			if(tryCount == 0){
	    			Constant.mainUrl = Constant.subUrl;
	    			Log.e(null, "xxxx22x");
	    			tryCount++;
	    			viewExpireDateTask = new ViewExpireDateTask(getBaseContext());
	            	viewExpireDateTask.execute();
    			}else{
    				contop2.setVisibility(View.GONE);
    				disconnected = true;
    				Log.e(null, "xxxx33x");
    				imgConnect2.setBackgroundResource(R.drawable.connect_x2);
		        	frameAnimation2 = (AnimationDrawable) imgConnect2.getBackground();
		        	frameAnimation2.start();
    			}
    		}
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		//	mImageView.scrollTo(mScrollStep++, 0);
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
   }
}
