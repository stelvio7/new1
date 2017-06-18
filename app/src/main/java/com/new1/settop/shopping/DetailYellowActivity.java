package com.new1.settop.shopping;

import java.util.ArrayList; 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.new1.settop.R;
import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.model.ShoppingList;
import com.new1.settop.CoverFlow;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

public class DetailYellowActivity extends Activity{
    /** Called when the activity is first created. */
	
	private String[] subImgs = new String[21];
	private String idx;
	private String code;
	
	private ImageAdapter imageAdapter = null;
	private Context mContext;
	private ImageView ivTel = null;
	private int nowCoverFlowIdx = 0;
	private ShoppingDetailCoverFlow coverFlow;

	private int size = 0;
	//private ArrayList<ShoppingList> broadcastList = null;
	
	private ImageView btnLeft = null;
	private ImageView btnRight = null;
	
	private ViewNextTask viewNextTask = null;
	
	public DetailYellowActivity(){
		nowCoverFlowIdx = 0;
		bitmap = new Bitmap[21];
	}
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Bitmap[] bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yellowdetailcoverflow);
        
	    Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		code = myBundle.getString("code");
		try{
			code = myBundle.getString("code");
			idx = myBundle.getString("idx");
		}catch (Exception e){}

		btnLeft = (ImageView) findViewById(R.id.arrowleft);
		btnRight = (ImageView) findViewById(R.id.arrowright);
		btnRight.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
					if(nowCoverFlowIdx >= size)
						nowCoverFlowIdx = size - 1;
					Log.e(null,"now" + coverFlow.getSelectedItemPosition());
					if(coverFlow != null)
						coverFlow.setSelection(nowCoverFlowIdx);
					Log.e(null,"now" + coverFlow.getSelectedItemPosition());
				}
				return true;
			}
		});
		
		btnLeft.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					nowCoverFlowIdx = coverFlow.getSelectedItemPosition() - 1;
					if(nowCoverFlowIdx < 0)
						nowCoverFlowIdx = 0;
					Log.e(null,"now:" + nowCoverFlowIdx);
					if(coverFlow != null)
						coverFlow.setSelection(nowCoverFlowIdx);
				}
				return true;
			}
		});
		
        mContext = this.getBaseContext();
        ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivTel);
        
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
        Button expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        GetTask getTask = new GetTask(mContext);
		getTask.execute();
        
		
        //setCoverFlow();
    }
    
  
    
    
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
    
    protected String getMacaddress(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("macaddress", "");
	}
    
    private void setCoverFlow(){
		coverFlow = (ShoppingDetailCoverFlow) findViewById(R.id.coverflow);
	    imageAdapter = new ImageAdapter(this);
		
	    coverFlow.setAdapter(imageAdapter);
		coverFlow.setSpacing(10);
		coverFlow.setAnimationDuration(1000);
	}
    
	@Override
	protected void onDestroy() {
		recycleAllBitmap();
		if(viewNextTask != null){
			viewNextTask.cancel(true);
			viewNextTask = null;
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
			//finish();
			nowCoverFlowIdx = coverFlow.getSelectedItemPosition() - 1;
			if(nowCoverFlowIdx < 0)
				nowCoverFlowIdx = 0;
			Log.e(null,"now:" + nowCoverFlowIdx);
			if(coverFlow != null)
				coverFlow.setSelection(nowCoverFlowIdx);
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			//finish();
			nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
			if(nowCoverFlowIdx >= size)
				nowCoverFlowIdx = size - 1;
			Log.e(null,"now" + coverFlow.getSelectedItemPosition());
			if(coverFlow != null)
				coverFlow.setSelection(nowCoverFlowIdx);
			Log.e(null,"now" + coverFlow.getSelectedItemPosition());
		} 
		return super.onKeyDown(keyCode, event);  	  
	}
	
	public class ImageAdapter extends BaseAdapter {
		int itemBackground;
		private Context mContext;
		
		public ImageAdapter(Activity c) {
			mContext = c;
		}
		
		public int getCount() {
			if(subImgs[0].equals("")){
				size = 0;
				return 0;
			}
			if(subImgs[1].equals("")){
				size = 1;
				return 1;
			}
			if(subImgs[2].equals("")){
				size = 2;
				return 2;
			}
			if(subImgs[3].equals("")){
				size = 3;
				return 3;
			}
			if(subImgs[4].equals("")){
				size = 4;
				return 4;
			}
			if(subImgs[5].equals("")){
				size = 5;
				return 5;
			}
			if(subImgs[6].equals("")){
				size = 6;
				return 6;
			}
			if(subImgs[7].equals("")){
				size = 7;
				return 7;
			}
			if(subImgs[8].equals("")){
				size = 8;
				return 8;
			}
			if(subImgs[9].equals("")){
				size = 9;
				return 9;
			}
			if(subImgs[10].equals("")){
				size = 10;
				return 10;
			}
			if(subImgs[11].equals("")){
				size = 11;
				return 11;
			}
			if(subImgs[12].equals("")){
				size = 12;
				return 12;
			}
			if(subImgs[13].equals("")){
				size = 13;
				return 13;
			}
			if(subImgs[14].equals("")){
				size = 14;
				return 14;
			}
			if(subImgs[15].equals("")){
				size = 15;
				return 15;
			}
			if(subImgs[16].equals("")){
				size = 16;
				return 16;
			}
			if(subImgs[17].equals("")){
				size = 17;
				return 17;
			}
			if(subImgs[18].equals("")){
				size = 18;
				return 18;
			}
			if(subImgs[19].equals("")){
				size = 19;
				return 19;
			}
			return 21;
		}

		public Object getItem(int position) {
			return bitmap[position];
		}

		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			i.setLayoutParams(new CoverFlow.LayoutParams(Util.getWidth(mContext)-30, Util.getHeight(mContext))); 
			i.setPadding(0, 0, 0, 0);
			//i.setBackgroundResource(R.drawable.listborder);
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			ImageDownloader imageDownloader = new ImageDownloader();
			if(bitmap[position] != null){
				//imageDownloader.download(subImgs[position], (ImageView)i);
				i.setImageBitmap(bitmap[position]);
			}
			
			BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
			if(drawable != null)
			drawable.setAntiAlias(true);
			
			return i;
		}

		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}
 
	
	class ViewNextTask extends AsyncTask<String, Integer, Long> {
	 	
	 	public ViewNextTask(Context context) {
	 		
	 	}
	 
	 	@Override
	 	protected void onPreExecute() {
	 		// TODO Auto-generated method stub
	 		//showProgress();
	 		super.onPreExecute();
	 	}
	 
	 	@Override
	 	protected Long doInBackground(String... params) {
	 		// TODO Auto-generated method stub
	 		UrlImageLoader urlImageload = new UrlImageLoader();
	 		for(int i = 0; i < 20; i++){
	 			if(!subImgs[i].equals(""))
			 		bitmap[i] = urlImageload.getUrlImage(subImgs[i]);
    			//if(i % 10 == 0)
    			publishProgress(i);
    		}
	 			
	 		String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(code == null){
				nameValuePairs.add(new BasicNameValuePair("idx", idx));
			}else {
				nameValuePairs.add(new BasicNameValuePair("idx", code));
			}
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/yellow_page_hit.php", nameValuePairs);
	 		return 0L;
	 	}
	 	
	 	@Override
	 	protected void onPostExecute(Long result) {
	 		// TODO Auto-generated method stub
	 		dismissProgress();
	 		//imageAdapter.notifyDataSetChanged();
	 		
	 		
	 	}
	 
	 	@Override
	 	protected void onProgressUpdate(Integer... values) {
	 		// TODO Auto-generated method stub
	 		//if(values[0] == 0)
	 		//	dismissProgress();
	 		imageAdapter.notifyDataSetChanged();
	 	}
	 
	 	protected void showProgress() {
    		mProgress = new ProgressDialog(DetailYellowActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	ViewNextTask.this.cancel(true);
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
	 	protected void onCancelled() {
	 		// TODO Auto-generated method stub
	 		showCancelMessage();
	 		super.onCancelled();
	 	}	
	}
	
 	private void recycleAllBitmap(){
 		for(int i = 0; i < 20; i++){
 			if(bitmap[i] != null){
 				bitmap[i].recycle();
 				bitmap[i] = null;
 			}
 		}
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
    		//broadcastList = new ArrayList<ShoppingList>();
    		getImages();
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		return 0L;
    	}
    	
    	 
    	protected void showProgress() {
    		mProgress = new ProgressDialog(DetailYellowActivity.this);
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

    		setCoverFlow();
    		viewNextTask = new ViewNextTask(mContext);
            viewNextTask.execute();

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
	
	//커버플로우에 넣어줄 이미지 데이터를 데이터구조에 넣어 둠
		private void getImages(){
			int startIdx = 0;
			int endIdx = 1999;
			//broadcastList.clear();
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if(code == null){
				nameValuePairs.add(new BasicNameValuePair("idx", idx));
			}else {
				nameValuePairs.add(new BasicNameValuePair("code1", code));
			}
			nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
			nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/yellow_page.php", nameValuePairs);
			JSONArray jArray = null;
			try{
				//JSONObject json = new JSONObject(strJson);
				jArray = new JSONArray(strJson);
				for(int i = 0; i < jArray.length(); i++){
					ShoppingList tempList = new ShoppingList();
					JSONObject json_data = jArray.getJSONObject(i); 
					tempList.setIdx(json_data.getString("idx"));
					//tempList.setCode1(json_data.getString("code1"));
					//tempList.setCode2(json_data.getString("code2"));
					/*tempList.setImage(json_data.getString("list_image"));
					tempList.setTitle(json_data.getString("title"));
					tempList.setDetail_image(Constant.mainUrl + "/post/" + json_data.getString("detail_image"));
					tempList.setSub_image1(json_data.getString("sub_image1"));
					tempList.setSub_image2(json_data.getString("sub_image2"));
					tempList.setSub_image3(json_data.getString("sub_image3"));
					tempList.setSub_image4(json_data.getString("sub_image4"));
					tempList.setSub_image5(json_data.getString("sub_image5"));
					tempList.setSub_image6(json_data.getString("sub_image6"));
					tempList.setSub_image7(json_data.getString("sub_image7"));
					tempList.setSub_image8(json_data.getString("sub_image8"));
					tempList.setSub_image9(json_data.getString("sub_image9"));
					tempList.setSub_image10(json_data.getString("sub_image10"));
					tempList.setSub_image11(json_data.getString("sub_image11"));
					tempList.setSub_image12(json_data.getString("sub_image12"));
					tempList.setSub_image13(json_data.getString("sub_image13"));
					tempList.setSub_image14(json_data.getString("sub_image14"));
					tempList.setSub_image15(json_data.getString("sub_image15"));
					tempList.setSub_image16(json_data.getString("sub_image16"));
					tempList.setSub_image17(json_data.getString("sub_image17"));
					tempList.setSub_image18(json_data.getString("sub_image18"));
					tempList.setSub_image19(json_data.getString("sub_image19"));
					tempList.setSub_image20(json_data.getString("sub_image20"));
					broadcastList.add(i, tempList);
					*/
					subImgs[0] = json_data.getString("list_image");
					subImgs[1] = json_data.getString("detail_image");
					subImgs[2] = json_data.getString("sub_image1");
					subImgs[3] = json_data.getString("sub_image2");
					subImgs[4] = json_data.getString("sub_image3");
					subImgs[5] = json_data.getString("sub_image4");
					subImgs[6] = json_data.getString("sub_image5");
					subImgs[7] = json_data.getString("sub_image6");
					subImgs[8] = json_data.getString("sub_image7");
					subImgs[9] = json_data.getString("sub_image8");
					subImgs[10] = json_data.getString("sub_image9");
					subImgs[11] = json_data.getString("sub_image10");
					subImgs[12] = json_data.getString("sub_image11");
					subImgs[13] = json_data.getString("sub_image12");
					subImgs[14] = json_data.getString("sub_image13");
					subImgs[15] = json_data.getString("sub_image14");
					subImgs[16] = json_data.getString("sub_image15");
					subImgs[17] = json_data.getString("sub_image16");
					subImgs[18] = json_data.getString("sub_image17");
					subImgs[19] = json_data.getString("sub_image18");
				}
			}catch(JSONException e){
				Log.e(null, e.toString()); 
			}
			/*
			for(int i = 0; i < 5000; i++){
				BroadcastList tempList = new BroadcastList();
				broadcastList.add(tempList);
			}*/
		}
}