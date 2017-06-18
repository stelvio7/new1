package com.new1.settop.shopping;

import java.text.SimpleDateFormat; 
import java.util.ArrayList; 
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.new1.settop.R;

import com.new1.model.Constant;
import com.new1.model.ExchangeDateList;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExchangeActivity extends Activity {
    /** Called when the activity is first created. */
	private String strRechargeNumber;
	private TextView txtExchange1;
	private TextView txtExchange2;
	private TextView txtExchange3;
	private TextView txtExchange4;
	private TextView txtExchange5;
	private TextView txtExchange6;
	private TextView etExchange1;
	private TextView etExchange2;
	private TextView etExchange3;
	private TextView etExchange4;
	private TextView etExchange5;
	private TextView etExchange6;
	private GetExchangeTask getExchangeTask= null;
	private ProgressDialog mProgress;
	private Button expireText;
	private ImageView ivTel = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Context mContext = null;
	private Button expireText2;
	
	private ArrayList<ExchangeDateList> exchangeDateList;
	
	public ExchangeActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoexchange);
        
        mContext = this.getBaseContext();
        
        ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivTel);
        
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
		else
        	imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        

		txtExchange1 =  (TextView) findViewById(R.id.txtExchange1);
		txtExchange2 =  (TextView) findViewById(R.id.txtExchange2);
		txtExchange3 =  (TextView) findViewById(R.id.txtExchange3);
		txtExchange4 =  (TextView) findViewById(R.id.txtExchange4);
		txtExchange5 =  (TextView) findViewById(R.id.txtExchange5);
		txtExchange6 =  (TextView) findViewById(R.id.txtExchange6);
		etExchange1 =  (TextView) findViewById(R.id.etExchange1);
		etExchange2 =  (TextView) findViewById(R.id.etExchange2);
		etExchange3 =  (TextView) findViewById(R.id.etExchange3);
		etExchange4 =  (TextView) findViewById(R.id.etExchange4);
		etExchange5 =  (TextView) findViewById(R.id.etExchange5);
		etExchange6 =  (TextView) findViewById(R.id.etExchange6);
		
		
        expireText = (Button) findViewById(R.id.imgExpireDate);
        txtExchange1.requestFocus();
        expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        getExchangeTask = new GetExchangeTask(getBaseContext());
		getExchangeTask.execute();
    }
    
    
	@Override
	protected void onDestroy() {
		if(getExchangeTask != null){
			getExchangeTask = null;
		}
		if(mProgress != null){
			mProgress = null;
		}
		
		// TODO Auto-generated method stub
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
		}
  	  return super.onKeyDown(keyCode, event); 
	}
	
	
	//충전하기
	 class GetExchangeTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	private String result = "";
    	
    	public GetExchangeTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		mProgress = ProgressDialog.show(ExchangeActivity.this, "", getResources().getString(R.string.wait), true, true);
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
    		nameValuePairs.add(new BasicNameValuePair("end", "6"));
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/exchange.php", nameValuePairs);
    		JSONArray jArray = null;
    		try{
    			exchangeDateList = new ArrayList<ExchangeDateList>();
    			jArray = new JSONArray(strJson);
    			for(int i = 0; i < jArray.length(); i++){
    				JSONObject json_data = jArray.getJSONObject(i); 
    				ExchangeDateList tempList = new ExchangeDateList(json_data.getString("date"), json_data.getString("price"));
    				//list.add(tempList);
    				exchangeDateList.add(i, tempList);
    			}
    		}catch(JSONException e){
    			Log.e(null, e.toString()); 
    		}
    		//publishProgress(1);
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long lresult) {
    		// TODO Auto-generated method stub
    		mProgress.dismiss();
    		setExchangeInfo();
    	}
    
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    	}
    
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}
    }
	
	 private String getNetExpireDate(String macAdress){
		String expireDate = "";
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//macAdress = "123456789";
		Log.e(null, "mac:" + macAdress);
		nameValuePairs.add(new BasicNameValuePair("id", macAdress));
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/expiredate.php", nameValuePairs);
		try{
			JSONObject json_data = new JSONObject(strJson);
			expireDate = json_data.getString("date");
			
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
		String[] st = new String[3];
		if(expireDate != null)
			st = expireDate.split("-");
		if(st.length > 1)
			expireDate = st[1] + st[2];
		return expireDate;
	}
	 
	 private void setExchangeInfo(){
		 String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if(exchangeDateList.size() > 0){
			//if(exchangeDateList.get(0).getDate().equals(now))
				etExchange1.setText(exchangeDateList.get(0).getPrice());
		}
		if(exchangeDateList.size() > 1){
			txtExchange2.setText(exchangeDateList.get(1).getDate());
			etExchange2.setText(exchangeDateList.get(1).getPrice());
		}
		if(exchangeDateList.size() > 2){
			txtExchange3.setText(exchangeDateList.get(2).getDate());
			etExchange3.setText(exchangeDateList.get(2).getPrice());
		}
		if(exchangeDateList.size() > 3){
			txtExchange4.setText(exchangeDateList.get(3).getDate());
			etExchange4.setText(exchangeDateList.get(3).getPrice());
		}
		if(exchangeDateList.size() > 4){
			txtExchange5.setText(exchangeDateList.get(4).getDate());
			etExchange5.setText(exchangeDateList.get(4).getPrice());
		}
		if(exchangeDateList.size() > 5){
			txtExchange6.setText(exchangeDateList.get(5).getDate());
			etExchange6.setText(exchangeDateList.get(5).getPrice());
		}	
	 }
	 
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
		
	//맥어드레스 가져오기
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
	private void saveAuth(boolean isAuth){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if(isAuth)
			editor.putString("auth", "ok");
		else
			editor.putString("auth", "");
		editor.commit();
	}
}

