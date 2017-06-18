 package com.new1.settop.delivery;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.new1.settop.R;

import com.new1.model.CategoryList;
import com.new1.model.Constant;
import com.new1.settop.shopping.DetailShoppingActivity;
import com.new1.settop.shopping.ShoppingCoverFlow;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

 public class DeliveryCategoryActivity extends Activity {

	private ShoppingCoverFlow coverFlow;
	
	private TextView[] detailtextView = new TextView[56]; 
	private int detailtextRes[] = {R.id.detailtext0, R.id.detailtext1, R.id.detailtext2, R.id.detailtext3, R.id.detailtext4, 
			R.id.detailtext5, R.id.detailtext6, R.id.detailtext7, R.id.detailtext8, R.id.detailtext9,
			R.id.detailtext10, R.id.detailtext11, R.id.detailtext12, R.id.detailtext13, R.id.detailtext14,
			R.id.detailtext15, R.id.detailtext16, R.id.detailtext17, R.id.detailtext18, R.id.detailtext19,
			R.id.detailtext20, R.id.detailtext21, R.id.detailtext22, R.id.detailtext23, R.id.detailtext24,
			R.id.detailtext25, R.id.detailtext26, R.id.detailtext27, R.id.detailtext28, R.id.detailtext29,
			R.id.detailtext30, R.id.detailtext31, R.id.detailtext32, R.id.detailtext33, R.id.detailtext34,
			R.id.detailtext35, R.id.detailtext36, R.id.detailtext37, R.id.detailtext38, R.id.detailtext39,
			R.id.detailtext40, R.id.detailtext41, R.id.detailtext42, R.id.detailtext43, R.id.detailtext44,
			R.id.detailtext45, R.id.detailtext46, R.id.detailtext47, R.id.detailtext48, R.id.detailtext49,
			R.id.detailtext50, R.id.detailtext51, R.id.detailtext52, R.id.detailtext53, R.id.detailtext54, R.id.detailtext55};
	
	private ImageView[] detailshowbtn = new ImageView[56]; 
	private int detailshowbtnRes[] = {R.id.detailshowbtn0, R.id.detailshowbtn1, R.id.detailshowbtn2, R.id.detailshowbtn3, R.id.detailshowbtn4, 
			R.id.detailshowbtn5, R.id.detailshowbtn6, R.id.detailshowbtn7, R.id.detailshowbtn8, R.id.detailshowbtn9,
			R.id.detailshowbtn10, R.id.detailshowbtn11, R.id.detailshowbtn12, R.id.detailshowbtn13, R.id.detailshowbtn14, 
			R.id.detailshowbtn15, R.id.detailshowbtn16, R.id.detailshowbtn17, R.id.detailshowbtn18, R.id.detailshowbtn19,
			R.id.detailshowbtn20, R.id.detailshowbtn21, R.id.detailshowbtn22, R.id.detailshowbtn23, R.id.detailshowbtn24, 
			R.id.detailshowbtn25, R.id.detailshowbtn26, R.id.detailshowbtn27, R.id.detailshowbtn28, R.id.detailshowbtn29,
			R.id.detailshowbtn30, R.id.detailshowbtn31, R.id.detailshowbtn32, R.id.detailshowbtn33, R.id.detailshowbtn34, 
			R.id.detailshowbtn35, R.id.detailshowbtn36, R.id.detailshowbtn37, R.id.detailshowbtn38, R.id.detailshowbtn39,
			R.id.detailshowbtn40, R.id.detailshowbtn41, R.id.detailshowbtn42, R.id.detailshowbtn43, R.id.detailshowbtn44, 
			R.id.detailshowbtn45, R.id.detailshowbtn46, R.id.detailshowbtn47, R.id.detailshowbtn48, R.id.detailshowbtn49,
			R.id.detailshowbtn50, R.id.detailshowbtn51, R.id.detailshowbtn52, R.id.detailshowbtn53, R.id.detailshowbtn54, R.id.detailshowbtn55};
	
	private String mainid = null;
	protected String subid = null;
	private Context mContext = null;
	private boolean isDestroy = false;
	private ImageView ivTel = null;
	private ImageView ivPhone = null;
	
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private ArrayList<CategoryList> categoryList = null;
	
	public DeliveryCategoryActivity(){
	}
	
	private String code1 = "";
	private String code2 = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.infodelivery);

	    mContext = this.getBaseContext();
	    Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		mainid = myBundle.getString("mainid");
		subid =  myBundle.getString("subid");
		
		
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
        for(int i = 0; i < 56; i++){
        	detailtextView[i] = (TextView)findViewById(detailtextRes[i]);
        	detailshowbtn[i] = (ImageView)findViewById(detailshowbtnRes[i]);
        }
		
        for(int i = 0; i < detailshowbtn.length; i++){
        	detailshowbtn[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = 999;
					for(int i = 0; i < categoryList.size(); i++){
						if(v.getId() == detailshowbtn[i].getId()){
							position = i;
						}
					}
					if(position < categoryList.size())
						goListActivity(categoryList.get(position).getCode());
				}
			});
        }
        
		//modify
		//searchButton = (Button)findViewById(R.id.searchButton);
		//searchText = (EditText)findViewById(R.id.searchText);
		ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivTel);
		
		//ivPhone = (ImageView)findViewById(R.id.ivPhone);
		//imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivPhone);
		//coverTextView = (TextView) findViewById(R.id.coverflowtext);
		Button expireText = (Button) findViewById(R.id.imgExpireDate);
		Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        GetCategoryTask getCategoryTask = new GetCategoryTask(mContext);
        getCategoryTask.execute();
		//noSearchToast = Toast.makeText(this, getString(R.string.nosearch), Toast.LENGTH_SHORT);

	}
	
	private void goListActivity(String code){
		Intent intent = new Intent(DeliveryCategoryActivity.this, DeliveryListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle myData = new Bundle();	
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        myData.putString("code1", code);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		isDestroy = true;
		
		super.onDestroy();
	}

	
	
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	private void goDetailShoppingActivity(String idx, String sub1, String sub2, String sub3, String sub4, String sub5
			,String sub6, String sub7, String sub8, String sub9, String sub10
			,String sub11, String sub12, String sub13, String sub14, String sub15
			,String sub16, String sub17, String sub18, String sub19, String sub20, String sub21){
		Intent intent = new Intent(DeliveryCategoryActivity.this, DetailShoppingActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();	
		myData.putString("type", "shopping");
		myData.putString("idx", idx);
		myData.putString("sub1", sub1);
        myData.putString("sub2", sub2);
        myData.putString("sub3", sub3);
        myData.putString("sub4", sub4);
        myData.putString("sub5", sub5);
        myData.putString("sub6", sub6);
        myData.putString("sub7", sub7);
        myData.putString("sub8", sub8);
        myData.putString("sub9", sub9);
        myData.putString("sub10", sub10);
        myData.putString("sub11", sub11);
        myData.putString("sub12", sub12);
        myData.putString("sub13", sub13);
        myData.putString("sub14", sub14);
        myData.putString("sub15", sub15);
        myData.putString("sub16", sub16);
        myData.putString("sub17", sub17);
        myData.putString("sub18", sub18);
        myData.putString("sub19", sub19);
        myData.putString("sub20", sub20);
        myData.putString("sub21", sub21);
        intent.putExtras(myData);
		startActivity(intent);
	}
	

	 private void setCategory(){
		 for(int i = 0; i < categoryList.size(); i++){
	        detailtextView[i].setText(categoryList.get(i).getName());
		 }
	 }

	//맥어드레스 가져오기
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
	private String getAuth(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("auth", "");
	}
				
	private ProgressDialog mProgress;
	
	 class GetCategoryTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	private String result = "";
    	
    	public GetCategoryTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		mProgress = ProgressDialog.show(DeliveryCategoryActivity.this, "", getResources().getString(R.string.wait), true, true);
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
    		//nameValuePairs.add(new BasicNameValuePair("end", "5"));
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/delivery_category.php", nameValuePairs);
    		JSONArray jArray = null;
    		try{
    			categoryList = new ArrayList<CategoryList>();
    			jArray = new JSONArray(strJson);
    			for(int i = 0; i < jArray.length(); i++){
    				JSONObject json_data = jArray.getJSONObject(i); 
    				CategoryList tempList = new CategoryList(json_data.getString("code"), json_data.getString("name"));
    				//list.add(tempList);
    				categoryList.add(i, tempList);
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
    		setCategory();
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
}
