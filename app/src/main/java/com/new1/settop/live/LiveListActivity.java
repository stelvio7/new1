package com.new1.settop.live;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.settop.R;
import com.new1.settop.VideoViewActivity;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TimerTask;

public class LiveListActivity extends Activity {

	//private String strBaseUrla = "rtsp://livea.hanaro.tv:1935/";
	//private String strBaseUrlb = "rtsp://liveb.hanaro.tv:1935/";
	//private LiveCoverFlow coverFlow;
	
	private ImageView[] detailshowbtn = new ImageView[30]; 
	private int detailshowbtnRes[] = {R.id.detailshowbtn0, R.id.detailshowbtn1, R.id.detailshowbtn2, R.id.detailshowbtn3, R.id.detailshowbtn4, 
			R.id.detailshowbtn5, R.id.detailshowbtn6, R.id.detailshowbtn7, R.id.detailshowbtn8, R.id.detailshowbtn9,
			R.id.detailshowbtn10, R.id.detailshowbtn11, R.id.detailshowbtn12, R.id.detailshowbtn13, R.id.detailshowbtn14, 
			R.id.detailshowbtn15, R.id.detailshowbtn16, R.id.detailshowbtn17, R.id.detailshowbtn18, R.id.detailshowbtn19,
			R.id.detailshowbtn20, R.id.detailshowbtn21, R.id.detailshowbtn22, R.id.detailshowbtn23, R.id.detailshowbtn24,
			R.id.detailshowbtn25, R.id.detailshowbtn26, R.id.detailshowbtn27, R.id.detailshowbtn28, R.id.detailshowbtn29};
	
	private ImageView borders[] = new ImageView[30];
	private int[] detailshowBorderRes = {
		R.id.detailshowborder0, R.id.detailshowborder1, R.id.detailshowborder2, R.id.detailshowborder3, R.id.detailshowborder4,
		R.id.detailshowborder5, R.id.detailshowborder6, R.id.detailshowborder7, R.id.detailshowborder8, R.id.detailshowborder9,
		R.id.detailshowborder10, R.id.detailshowborder11, R.id.detailshowborder12, R.id.detailshowborder13, R.id.detailshowborder14,
		R.id.detailshowborder15, R.id.detailshowborder16, R.id.detailshowborder17, R.id.detailshowborder18, R.id.detailshowborder19, 
		R.id.detailshowborder20, R.id.detailshowborder21, R.id.detailshowborder22, R.id.detailshowborder23, R.id.detailshowborder24,
		R.id.detailshowborder25,  R.id.detailshowborder26,  R.id.detailshowborder27,  R.id.detailshowborder28,  R.id.detailshowborder29};
	
	
	private ArrayList<BroadcastList> liveList = null;
	private String mainid = null;
	protected String subid = null;
	//private TextView coverTextView = null;
	private ImageView ivTel = null;
	//private ViewNextTask viewNextTask = null;
	private Context mContext = null;
	private ImageAdapter imageAdapter = null;
	private boolean isClicked = false;

	private TextView nowTitle;
	private TextView nowTime;
	private TextView nextTitle;
	private TextView nextTime;
	
	private TimerTask timerTask;
	//private ImageView btnLeft = null;
	//private ImageView btnRight = null;
	//private int nowCoverFlowIdx = 0;
	
	public LiveListActivity(){
		liveList = new ArrayList<BroadcastList>();
		//nowCoverFlowIdx = 0;
	}
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.livecoverflow);
	    
	    mContext = this.getBaseContext();
	    
	    ImageLoader.getInstance().init(Util.getConfig(mContext));
	    
	    Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		mainid = myBundle.getString("mainid");
		subid =  myBundle.getString("subid");
		
		ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivTel);
		
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);

		nowTitle = (TextView)findViewById(R.id.nowTitle);
		nowTime = (TextView)findViewById(R.id.nowTime);
		nextTitle = (TextView)findViewById(R.id.nextTitle);
		nextTime = (TextView)findViewById(R.id.nextTime);
        
        for(int i = 0; i < detailshowbtn.length; i++){
        	detailshowbtn[i] = (ImageView)findViewById(detailshowbtnRes[i]);
        	borders[i] = (ImageView) findViewById(detailshowBorderRes[i]);
        }
        
       
       
        
        
        /*btnLeft = (ImageView) findViewById(R.id.arrowleft);
		btnRight = (ImageView) findViewById(R.id.arrowright);
		
		btnRight.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					nowCoverFlowIdx++;
					if(nowCoverFlowIdx >= liveList.size())
						nowCoverFlowIdx = liveList.size() - 1;
					coverFlow.setSelection(nowCoverFlowIdx);
				}
				return true;
			}
		});
		
		btnLeft.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					nowCoverFlowIdx --;
					if(nowCoverFlowIdx < 0)
						nowCoverFlowIdx = 0;
					coverFlow.setSelection(nowCoverFlowIdx);
				}
				return true;
			}
		});
		
		coverTextView = (TextView) findViewById(R.id.coverflowtext);
		*/
	    //coverFlow = (LiveCoverFlow) findViewById(R.id.coverflow);
		//coverFlow.setAdapter(new ImageAdapter(this));
		
		Button expireText = (Button) findViewById(R.id.imgExpireDate);
		Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
		//getImages();

		GetTask getTask = new GetTask(mContext);
		getTask.execute();
		/*coverFlow.setSpacing(15);

		coverFlow.setAnimationDuration(1000);
		coverFlow.setSelection(0, true);
		
		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		     @Override
		     public void onItemSelected(AdapterView<?> arg0, View arg1,
		       int arg2, long arg3) {
		    	 //Log.e(null, String.valueOf(arg3));
		       }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String strUrl = "";
				// TODO Auto-generated method stub
				if(subid != null){
					if(subid.equals("ground")){
						switch(position){
							case 0:
								strUrl = strBaseUrla + "live_01/live_01.sdp";
								break;
							case 1:
								strUrl = strBaseUrla + "live_02/live_02.sdp";
								break;
							case 2:
								strUrl = strBaseUrla + "live_03/live_03.sdp";
								break;
							case 3:
								strUrl = strBaseUrla + "live_04/live_04.sdp";
								break;
						}
					}else if(subid.equals("total")){
						switch(position){
						case 0:
							strUrl = strBaseUrla + "live_05/live_05.sdp";
							break;
						case 1:
							strUrl = strBaseUrla + "live_06/live_06.sdp";
							break;
						case 2:
							strUrl = strBaseUrla + "live_07/live_07.sdp";
							break;
						case 3:
							strUrl = strBaseUrla + "live_08/live_08.sdp";
							break;
						}
					}else if(subid.equals("sports")){
						switch(position){
						case 0:
							strUrl = strBaseUrlb + "live_15/live_15.sdp";
							break;
						case 1:
							strUrl = strBaseUrlb + "live_16/live_16.sdp";
							break;
						case 2:
							strUrl = strBaseUrlb +  "live_12/live_12.sdp";
							break;
						case 3:
							strUrl = strBaseUrlb + "live_13/live_13.sdp";
							break;
						case 4:
							strUrl = strBaseUrlb + "live_14/live_14.sdp";
							break;
						}
					}else if(subid.equals("cable")){
						switch(position){
						case 0:
							strUrl = strBaseUrla +  "live_09/live_09.sdp";
							break;
						case 1:
							strUrl = strBaseUrla + "live_10/live_10.sdp";
							break;
						case 2:
							strUrl = strBaseUrlb + "live_17/live_17.sdp";
							break;
						case 3:
							strUrl = strBaseUrlb + "live_18/live_18.sdp";
							break;
						
					
						}
					}else if(subid.equals("baby")){
						switch(position){
						case 0:
							strUrl = strBaseUrlb + "live_19/live_19.sdp";
							break;
						case 1:
							strUrl = strBaseUrlb + "live_20/live_20.sdp";
							break;
						}
					}else if(subid.equals("news")){
						switch(position){
						case 0:
							strUrl = strBaseUrlb + "live_11/live_11.sdp";
							break;
						}
					}
					if(getAuth().equals("ok"))
						goLivePlay(strUrl);
					else{
						AlertDialog.Builder alt_bld = new AlertDialog.Builder(LiveListActivity.this);
					    alt_bld.setMessage(R.string.memberm);
					    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				                dialog.dismiss();
				            }
				        });
					    AlertDialog alert = alt_bld.create();
					    alert.show();
					}
				}
			}
		});*/
	}
	
	class Customhandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

    		//Log.e(null, Util.getNowTime());
        }
    }
	
	private void getImages(){
		int startIdx = 0;
		int endIdx = 29999;
		
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		//nameValuePairs.add(new BasicNameValuePair("code2", subid));
		nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
		nameValuePairs.add(new BasicNameValuePair("end", "30"));//String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		Log.e(null, strJson);
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/live.php", nameValuePairs);
		
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			//Log.e(null, strJson);
			for(int i = 0; i < jArray.length(); i++){
				BroadcastList tempList = new BroadcastList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("idx"));
				tempList.setVod_code(json_data.getString("vod_code"));
				tempList.setImage(json_data.getString("image"));
				tempList.setTitle(json_data.getString("title"));
				tempList.setPu_no(json_data.getString("pu_no"));

				tempList.setNowTitle(json_data.getString("now_title"));
				tempList.setNowTime(json_data.getString("now_time"));
				tempList.setNextTitle(json_data.getString("next_title"));
				tempList.setNextTime(json_data.getString("next_time"));

				//list.add(tempList);
				liveList.add(i, tempList);
			}
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
	}
	
	private void setCoverflowData(){
		if(liveList.size() > 0) {
			nowTitle.setText(liveList.get(0).getNowTitle());
			nowTime.setText("(" + liveList.get(0).getNowTime() + ")");
			nextTitle.setText(liveList.get(0).getNextTitle());
			nextTime.setText("(" + liveList.get(0).getNextTime() + " ~)");
		}
		 for(int i = 0; i < liveList.size(); i++){
			 detailshowbtn[i].setFocusable(true);
			 
			 detailshowbtn[i].setOnFocusChangeListener(new OnFocusChangeListener() {

		            public void onFocusChange(View v, boolean hasFocus) {
		            	if(hasFocus){
		            		for(int j = 0; j < liveList.size(); j++)
		            			if(v == detailshowbtn[j])	{
		            				borders[j].setVisibility(View.VISIBLE);
		            				nowTitle.setText(liveList.get(j).getNowTitle());
									nowTime.setText("(" + liveList.get(j).getNowTime() + ")");
									nextTitle.setText(liveList.get(j).getNextTitle());
									nextTime.setText("(" + liveList.get(j).getNextTime() + ")");
		            			}
		    			}else{
		    				for(int j = 0; j < liveList.size(); j++)
		    					if(v == detailshowbtn[j])	
		    						borders[j].setVisibility(View.GONE);
		    				
		    			}
		            }
		        });
			 
        	detailshowbtn[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = 999;
					for(int i = 0; i < liveList.size(); i++){
						if(v.getId() == detailshowbtn[i].getId()){
							position = i;
						}
					}
					
					if(!isClicked){
						isClicked = true;
						if(position < liveList.size()){
							ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, position);
							viewDetailTask.execute();
						}
					}
				}
			});
        	//if(i == 0)
				
        }
		 
		 //detailshowbtn[0].requestFocus();
        UrlImageLoader urlImageload = new UrlImageLoader();
        for(int i = 0; i < liveList.size(); i++){
        	
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(liveList.get(i).getImage(), detailshowbtn[i], Util.getImageLoaderOption(), new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage){
					//super.onLoadingComplete(imageUri, view, loadedImage);
					if(Util.getWidth(mContext) != 0){
		            	try {
			                float height=loadedImage.getHeight();
			                float width=loadedImage.getWidth();
			                loadedImage.setDensity(Bitmap.DENSITY_NONE);
			                loadedImage = Bitmap.createScaledBitmap(loadedImage, loadedImage.getWidth(), loadedImage.getHeight(), true);
			                ((ImageView)view).setImageBitmap(loadedImage);
		                } catch (Exception e) {
		                    e.printStackTrace();
		                } 
		            }
				}
			});
		}
        
		/*coverFlow = (LiveCoverFlow) findViewById(R.id.coverflow);
		imageAdapter = new ImageAdapter(this);
		coverFlow.setAdapter(imageAdapter);
		
		coverFlow.setSpacing(15);

		coverFlow.setAnimationDuration(1000);
		
		viewNextTask = new ViewNextTask(mContext, liveList, coverFlow.getSelectedItemPosition()-11, 
				coverFlow.getSelectedItemPosition()+11);
		viewNextTask.execute();
		
		//viewNextTask = new ViewNextTask(mContext, broadcastList, 5, broadcastList.size());
		//viewNextTask.execute();
		
		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		     @Override
		     public void onItemSelected(AdapterView<?> arg0, View arg1,
		       int arg2, long arg3) {
		    	 //coverFlow.requestFocus();
		    	// Log.e(null, broadcastList.get((int)arg3).getTitle());
		    	 viewNextTask = new ViewNextTask(mContext, liveList, (int)arg3-11, 
		    			 (int)arg3+11);
		 			viewNextTask.execute();
		    	 
		    	 //coverTextView.setText(broadcastList.get((int)arg3).getTitle());
		    	 //if(arg3%10 == 0){
		    		// viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-10, (int)arg3+10);
		    		// viewNextTask.execute();
		    	 //}
		 			nowCoverFlowIdx = (int)arg3;
		       }

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
		
		coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(!isClicked){
					isClicked = true;
					ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, position);
					viewDetailTask.execute();
				}
			}
		});*/
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
	protected void onDestroy() {
		if(timerTask != null){
			timerTask.cancel();
		}
		// TODO Auto-generated method stub
		/*if(viewNextTask != null){
			viewNextTask.cancel(true);
			viewNextTask = null;
		}*/
		super.onDestroy();
	}

	
	private void goLivePlay(String strUrl, String idx){
		Intent intent = new Intent(LiveListActivity.this, VideoViewActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();	
		//Log.e(null, strUrl);
		myData.putString("idx", idx);
        myData.putString("videourl", strUrl);
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
	
	public class ImageAdapter extends BaseAdapter {
		int itemBackground;
		private Context mContext;

		//private ImageView[] mImages;
		public ImageAdapter(Activity c) {
			mContext = c;
			//for(Integer i:)
//				mImages = new ImageView[mImageIds.length];
		}
		
		public int getCount() {
			return liveList.size();
		}
		
		public Object getItem(int position) {
			return liveList.get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setLayoutParams(new LiveCoverFlow.LayoutParams(173, 245)); 
			i.setPadding(2, 13, 2, 4);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			
			i.setBackgroundResource(R.drawable.livelist_border);
			i.setImageBitmap(liveList.get(position).getBitmap());

			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			drawable.setAntiAlias(true);
			
			return i;
		}

		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}

	public String getSavedFastIdx() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("fastidx", "0");
	}

	public void saveFastIdx(String fastidx) {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("fastidx", fastidx);
		editor.commit();
	}

	private String getAuth(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("auth", "");
	}

	class ViewNextTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;
    	private int startIdx;
    	private int endIdx;
    	private Context mContext;
    	
    	public ViewNextTask(Context context, ArrayList<BroadcastList> list, int start, int end) {
    		if(start < 0)
    			start = 0;
    		this.list = list;
    		startIdx = start;
    		endIdx = end;
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		//while(true) {
    			//break;
    		//}
    		UrlImageLoader urlImageload = new UrlImageLoader();
    		if(startIdx < 0)
    			startIdx = 0;
    		if(endIdx > liveList.size())
    			endIdx = liveList.size();
    		for(int i = startIdx;i < endIdx; i++){
    			if(liveList.get(i).getBitmap() == null)
    				liveList.get(i).setBitmap(urlImageload.getUrlImage(list.get(i).getImage()));
    			if(i % 10 == 0)
    				publishProgress(1);
    		}
    		if( startIdx/100 > 1)
    			for(int i = 0; i < startIdx - (startIdx%100) - 10; i++){
    				if(liveList.get(i).getBitmap() != null){
    					liveList.get(i).recyleBitmap();
    			}
    		}
    		if( endIdx/100 < liveList.size()/100-1){
    			for(int i = endIdx + 10; i < liveList.size(); i++){
	    			if(liveList.get(i).getBitmap() != null){
	    				liveList.get(i).recyleBitmap();
	    			}
    			}
    		}
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		imageAdapter.notifyDataSetChanged();
    	}
    	
    	@Override
    	protected void onProgressUpdate(Integer... values) {
    		// TODO Auto-generated method stub
    		//	mImageView.scrollTo(mScrollStep++, 0);
    		imageAdapter.notifyDataSetChanged();
    	}
    	
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
    }
	
	class ViewDetailTask extends AsyncTask<String, Integer, Long> {
    	private String detailUrl;
    	private String playUrl;
    	private Context mContext;
    	private int position;
    	//private String detailTitle;
    	private String detailP_code;
    	private String detailVod_type;
    	private String detailVod_code;
    	private String result_code;
    	private String vod_url;
    	
    	public ViewDetailTask(Context context, int position) {
    		mContext = context;
    		this.position = position;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			if(!mainid.equals("broadcast")){
				nameValuePairs.add(new BasicNameValuePair("code2", subid));
				nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
				if(liveList.size() > 0)
					nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
				strJson = postmake.httpConnect(
						Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
				//Log.e(null, "json" + broadcastList.get(position).getIdx());
				JSONArray jArray = null;
				try{
					jArray = new JSONArray(strJson);
					JSONObject json_data = jArray.getJSONObject(0); 
					//detailTitle = json_data.getString("title");
					detailP_code = json_data.getString("p_code");
					detailVod_type = json_data.getString("vod_type");
					detailVod_code = json_data.getString("vod_code");
					Log.e(null, json_data.getString("p_code"));
				}catch(JSONException e){
					
				}
//			}
			
			strJson = "";
			nameValuePairs = null;
			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
			//if(mainid.equals("broadcast")){
				nameValuePairs.add(new BasicNameValuePair("p_code", liveList.get(position).getIdx()));
				nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
				nameValuePairs.add(new BasicNameValuePair("vod_code", liveList.get(position).getVod_code()));
			/*}else{
				nameValuePairs.add(new BasicNameValuePair("p_code", detailP_code));
				nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
				nameValuePairs.add(new BasicNameValuePair("vod_code", detailVod_code));
			}*/
			if(!Util.getChildset(mContext))
				nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));
			if(liveList.size() > 0)
				nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
			try{
				JSONObject json_data = new JSONObject(strJson); 
				//detailTitle = json_data.getString("title");
				result_code = json_data.getString("result");
				vod_url = json_data.getString("vod_url");
				//Log.e(null, vod_url);
			}catch(JSONException e){
				Log.e(null, e.toString()); 
			}
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub

    		Log.i(null, "url" + vod_url + " :idx:"+ liveList.get(position).getIdx());
			saveFastIdx(String.valueOf(position));
    		checkPlay(result_code, vod_url, liveList.get(position).getIdx());
    		isClicked = false;
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
	
	 private String getChildNum(){
			SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
			return sp.getString("childnum", "0000");
		}
	 
	private void checkPlay(String result, String vod_url, String idx){
		if(Constant.isTest){
			 goLivePlay(vod_url, idx);
			 return;
		}
		 if(getAuth().equals("ok")){
			 if(result.equals("OK")){
				 goLivePlay(vod_url, idx);
			 }
			 else if(result.equals("CANCEL")){
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			    alt_bld.setMessage(R.string.videocancel);
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
			 }
			 else if(result.equals("CHILD_SAFE")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.safechild);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("AGE_UNDER")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.ageunder);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("MEMBER_M")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.memberm);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
			 else if(result.equals("POINT_UNDERSUPPLY")){
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
				    alt_bld.setMessage(R.string.nopoint);
				    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			            public void onClick(DialogInterface dialog, int which) {
			                dialog.dismiss();
			            }
			        });
				    AlertDialog alert = alt_bld.create();
				    alert.show();
			 }
		 }else{
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(LiveListActivity.this);
			    alt_bld.setMessage(R.string.memberm);
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.dismiss();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
			}
	 }
	 
	
	private String getMacaddress() {
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
	
	private ProgressDialog mProgress;
	class GetTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetTask(Context context) {
    		mContext = context;
    	}
    
    	 @Override
    	 protected final void onPreExecute() {
    		 showProgress();
    	  }
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		//while(true) {
    			//break;
    		//}
    		
    		getImages();
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		return 0L;
    	}
    	
    	 
    	protected void showProgress() {
    		mProgress = new ProgressDialog(LiveListActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetTask.this.cancel(true);
                }
            });

            String message = "처리 중입니다.";
            mProgress.setMessage(message);
            mProgress.show();
        }
    	
    	protected void dismissProgress() {

            if (mProgress != null) {
            	mProgress.dismiss();
            }
        }

    	
    	protected void showCancelMessage() {
            dismissProgress();
            //Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
        }

    	
    	 protected void showError(Context context, Throwable t) {
    	        dismissProgress();

    	        //TODO exception ���� ��� ������ �����޽��� ����
    	        String errorMessage = context.getString(R.string.str_network_error);

    	        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
    	    }
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		dismissProgress();
    		setCoverflowData();
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
    		showCancelMessage();
    	}	
    }
}
