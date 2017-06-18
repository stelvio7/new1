package com.new1.setting;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.new1.settop.R;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.model.ShortChannelDateList;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener; 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShortChannelActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private String strRechargeNumber;
	private TextView shorNum1;
	private TextView shorNum2;
	private TextView shorNum3;
	private TextView shorNum4;
	private TextView shorNum5;
	private TextView shorNum6;
	private TextView shorNum7;
	private TextView shorNum8;
	private TextView shorNum9;
	private TextView shorNum10;
	
	private boolean isNumClicked = false;
	private boolean isChannelClicked = false;
	
	private ProgressDialog mProgress;
	private Button expireText;
	private ImageView ivTel = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Context mContext = null;
	private Button expireText2;
	private GetShortTask getShortTask= null;
	private ImageView backImage;

	private boolean viewMenu = false;
	
	LinearLayout llMenu;
	RelativeLayout slmenu;
	private ArrayList<BroadcastList> liveList = null;
	
	private ArrayList<ShortChannelDateList> shortChannelDateList;
	private int clickIdx;
	
	public ShortChannelActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortchannel);
        
        liveList = new ArrayList<BroadcastList>();
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
        
        shorNum1 =  (TextView) findViewById(R.id.shorNum1);
        shorNum2 =  (TextView) findViewById(R.id.shorNum2);
        shorNum3 =  (TextView) findViewById(R.id.shorNum3);
        shorNum4 =  (TextView) findViewById(R.id.shorNum4);
        shorNum5 =  (TextView) findViewById(R.id.shorNum5);
        shorNum6 =  (TextView) findViewById(R.id.shorNum6);
        shorNum7 =  (TextView) findViewById(R.id.shorNum7);
        shorNum8 =  (TextView) findViewById(R.id.shorNum8);
        shorNum9 =  (TextView) findViewById(R.id.shorNum9);
        shorNum10 =  (TextView) findViewById(R.id.shorNum10);
        expireText = (Button) findViewById(R.id.imgExpireDate);
        
        expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }

		backImage = (ImageView) findViewById(R.id.backImage);
		backImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		shorNum1.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {      
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton(1);
                	  return true;           
                  } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DEL
                		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
                	  return false;   
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }           
          });   
        shorNum1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(1);
			}
        });
        shorNum2.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton(2);
                	  return true;           
                  } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
                		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
                	  return false;   
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }                  
          });  
        shorNum2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(2);
			}
        });
        shorNum3.setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton(3);
                	  return true;           
                  } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
                		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
                	  return false;   
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }                 
          });  
        shorNum3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(3);
			}
        });
        shorNum4.setOnKeyListener(new OnKeyListener() {           
       	public boolean onKey(View v, int keyCode, KeyEvent event) {           
               if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                 if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
               	  clickOkButton(4);
               	  return true;           
                 } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
               		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
               	  return false;   
                 } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
               	  finish();
               	  return false;   
                 }
               }
               return false;   
             }               
         });  
        shorNum4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(4);
			}
        });
       shorNum5.setOnKeyListener(new OnKeyListener() {           
      	public boolean onKey(View v, int keyCode, KeyEvent event) {           
              if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
              	  clickOkButton(5);
              	  return true;           
                } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
              		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
              	  return false;   
                } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
              	  finish();
              	  return false;   
                }
              }
              return false;   
            }               
        });  
       shorNum5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(5);
			}
       });
	      shorNum6.setOnKeyListener(new OnKeyListener() {           
	     	public boolean onKey(View v, int keyCode, KeyEvent event) {           
	             if (event.getAction() == KeyEvent.ACTION_DOWN) {       
	               if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
	             	  clickOkButton(6);
	             	  return true;           
	               } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
	             		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
	             	  return false;   
	               } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
	             	  finish();
	             	  return false;   
	               }
	             }
	             return false;   
	           }               
	       });  
	      shorNum6.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					clickOkButton(6);
				}
	        });
	     shorNum7.setOnKeyListener(new OnKeyListener() {           
	    	public boolean onKey(View v, int keyCode, KeyEvent event) {           
	            if (event.getAction() == KeyEvent.ACTION_DOWN) {       
	              if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
	            	  clickOkButton(7);
	            	  return true;           
	              } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
	            		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
	            	  return false;   
	              } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
	            	  finish();
	            	  return false;   
	              }
	            }
	            return false;   
	          }               
	      });  
	     shorNum7.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					clickOkButton(7);
				}
	        });
	    shorNum8.setOnKeyListener(new OnKeyListener() {           
	   	public boolean onKey(View v, int keyCode, KeyEvent event) {           
	           if (event.getAction() == KeyEvent.ACTION_DOWN) {       
	             if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
	           	  clickOkButton(8);
	           	  return true;           
	             } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
	           		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
	           	  return false;   
	             } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
	           	  finish();
	           	  return false;   
	             }
	           }
	           return false;   
	         }               
	     });  
	    shorNum8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(8);
			}
        });
	   shorNum9.setOnKeyListener(new OnKeyListener() {           
	  	public boolean onKey(View v, int keyCode, KeyEvent event) {           
	          if (event.getAction() == KeyEvent.ACTION_DOWN) {       
	            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
	          	  clickOkButton(9);
	          	  return true;           
	            } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
	          		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
	          	  return false;   
	            } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
	          	  finish();
	          	  return false;   
	            }
	          }
	          return false;   
	        }               
	    });  
	   shorNum9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(9);
			}
       });
	  	shorNum10.setOnKeyListener(new OnKeyListener() {           
		 	public boolean onKey(View v, int keyCode, KeyEvent event) {           
		         if (event.getAction() == KeyEvent.ACTION_DOWN) {       
		           if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
		         	  clickOkButton(0);
		         	  return true;           
		           } else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT|| keyCode == KeyEvent.KEYCODE_DEL
		         		  || (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9)){
		         	  return false;   
		           } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
		         	  finish();
		         	  return false;   
		           }
		         }
		         return false;   
		       }               
		   });  
	  	shorNum10.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton(0);
			}
        });
        getShortTask = new GetShortTask(getBaseContext());
        getShortTask.execute();
        
        GetTask getTask = new GetTask(mContext);
		getTask.execute();
		
		shorNum1.setFocusable(true);
        shorNum1.requestFocus();
    }
    
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		for(int i = 0; i < liveList.size();i++){
			if(arg0 == liveList.get(i).getBtns()){
				
				if(!isChannelClicked){
					isChannelClicked = true;
					
					Log.e(null, "position=" + i);
					RegistTask viewDetailTask = new RegistTask(mContext, i);
					viewDetailTask.execute();
					
				}
			}
		}
	}
    
    void setMenu(){
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		slmenu = (RelativeLayout) findViewById(R.id.slmenu);

		for (int i = 0; i < liveList.size(); i++) {
            /*Button button = new Button(this);
            button.setBackgroundResource(R.drawable.live_select_button);
            button.setText(liveList.get(i).getTitle());
            button.setTextColor(Color.WHITE);
            button.setTextSize(23);
            button.setOnClickListener(this);*/
			//button.setFocusable(true);
			//button.requestFocus();
			//liveList.get(i).setBtns(button);
			//button01.setLayoutParams(params);
			//llMenu.addView(button);

			LayoutInflater inflater = LayoutInflater.from(mContext);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.livemenu_row, null, false);
			liveList.get(i).setBtns(layout);
			TextView txtNowBroad = (TextView)layout.findViewById(R.id.txtNowBroad);
			txtNowBroad.setText(liveList.get(i).getNowTitle());
			TextView txtNowChannel = (TextView)layout.findViewById(R.id.txtNowChannel);
			txtNowChannel.setText(liveList.get(i).getTitle());
			TextView txtNowChannel2 = (TextView)layout.findViewById(R.id.txtNowChannel2);
			txtNowChannel2.setText(liveList.get(i).getTitle());
			TextView txtNextTime = (TextView)layout.findViewById(R.id.txtNextTime);
			txtNextTime.setText(liveList.get(i).getNextTime() + "~");
			TextView txtNextBroad = (TextView)layout.findViewById(R.id.txtNextBroad);
			txtNextBroad.setText(liveList.get(i).getNextTitle());
			LinearLayout ll1 = (LinearLayout) layout.findViewById(R.id.ll1);
			LinearLayout ll2 = (LinearLayout) layout.findViewById(R.id.ll2);


			ll1.setVisibility(View.GONE);

			final int k = i;
			layout.setOnClickListener(this);
			layout.setOnFocusChangeListener(new View.OnFocusChangeListener(){
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(hasFocus){
						//for(int k =0; k < ((ViewGroup)v).getChildCount(); k++){
						((ViewGroup)v).getChildAt(0).setVisibility(View.VISIBLE);
						((ViewGroup)v).getChildAt(1).setVisibility(View.GONE);
						//}
					}else{
						//for(int k =0; k < ((ViewGroup)v).getChildCount(); k++){
						((ViewGroup)v).getChildAt(0).setVisibility(View.GONE);
						((ViewGroup)v).getChildAt(1).setVisibility(View.VISIBLE);
						//}
					}
				}
			});
			llMenu.addView(layout);
		}
	}
    
    void clickOkButton(int idx){
    	//if(!isNumClicked){
    	//	isNumClicked = true;
			clickIdx = idx;
			
			viewMenu = true;
			slmenu.setVisibility(View.VISIBLE);
			if(liveList.size() > 0) {
				liveList.get(0).getBtns().setFocusable(true);
				liveList.get(0).getBtns().requestFocus();
			}
    	//}
		
	}
    
	@Override
	protected void onDestroy() {
		if(getShortTask != null){
			getShortTask = null;
		}
		if(mProgress != null){
			mProgress = null;
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
			if (viewMenu)
			{
				viewMenu = false;
				slmenu.setVisibility(View.GONE);
			}
			else
			{
				finish();
			}
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			
		} else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {

		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			//clickOkButton();
		}
  	  return super.onKeyDown(keyCode, event); 
	}
	
	 private void setShorChannelInfo(){
		 for(int i = 0; i < shortChannelDateList.size(); i++){
			if(shortChannelDateList.get(i).getShortcut().equals("1")){
				shorNum1.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("2")){
				shorNum2.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("3")){
				shorNum3.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("4")){
				shorNum4.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("5")){
				shorNum5.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("6")){
				shorNum6.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("7")){
				shorNum7.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("8")){
				shorNum8.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("9")){
				shorNum9.setText(shortChannelDateList.get(i).getPtitle());
			}else if(shortChannelDateList.get(i).getShortcut().equals("0")){
				shorNum10.setText(shortChannelDateList.get(i).getPtitle());
			}
		 }
	 }
	 
	 
	 class GetShortTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	private String result = "";
    	
    	public GetShortTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		//mProgress = ProgressDialog.show(ShortChannelActivity.this, "", getResources().getString(R.string.wait), true, true);
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/live_shortcut.php", nameValuePairs);
    		JSONArray jArray = null;
    		try{
    			shortChannelDateList = new ArrayList<ShortChannelDateList>();
    			jArray = new JSONArray(strJson);
    			for(int i = 0; i < jArray.length(); i++){
    				JSONObject json_data = jArray.getJSONObject(i); 
    				ShortChannelDateList tempList = new ShortChannelDateList(json_data.getString("shortcut"), json_data.getString("p_code"), json_data.getString("p_title"));
    				//list.add(tempList);
    				shortChannelDateList.add(i, tempList);
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
    		//mProgress.dismiss();
    		setShorChannelInfo();
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
	
	private void getImages(){
		int startIdx = 0;
		int endIdx = 29999;
		

		//String[] subids = {"122", "123", "124", "125", "126", "131", "127"};;
		//for(int j = 0; j < 7; j++){
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
			nameValuePairs.add(new BasicNameValuePair("end", "30"));//String.valueOf(endIdx)));
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			
			//nameValuePairs.add(new BasicNameValuePair("code2", subids[j]));
			
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
					//tempList.setSubid(subids[j]);
					tempList.setTitle(json_data.getString("title"));
					tempList.setPu_no(json_data.getString("pu_no"));
					//list.add(tempList);

					tempList.setSubid("live");

					tempList.setNowTitle(json_data.getString("now_title"));
					tempList.setNowTime(json_data.getString("now_time"));
					tempList.setNextTitle(json_data.getString("next_title"));
					tempList.setNextTime(json_data.getString("next_time"));
					liveList.add(tempList);
				}
			}catch(JSONException e){
				Log.e(null, e.toString()); 
			}finally{
    			isNumClicked = false;
    		}
		//}
	}
	
	
	class GetTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetTask(Context context) {
    		mContext = context;
    	}
    
    	 @Override
    	 protected final void onPreExecute() {
    		 mProgress = ProgressDialog.show(ShortChannelActivity.this, "", getResources().getString(R.string.wait), true, true);
    	  }
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		getImages();
    		return 0L;
    	}
    	
   
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		mProgress.dismiss();
    		setMenu();
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
	
	class RegistTask extends AsyncTask<String, Integer, Long> {
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
    	
    	public RegistTask(Context context, int position) {
    		mContext = context;
    		this.position = position;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		mProgress = ProgressDialog.show(ShortChannelActivity.this, "", getResources().getString(R.string.wait), true, true);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
			String strJson = "";
			PostHttp postmake = new PostHttp();
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
			if(liveList.size() > 0){
				nameValuePairs.add(new BasicNameValuePair("p_code", liveList.get(position).getIdx()));
				nameValuePairs.add(new BasicNameValuePair("no", String.valueOf(clickIdx)));
			}
				
			strJson = postmake.httpConnect(
					Constant.mainUrl + "/module/tv/live_shortcut_proc.php", nameValuePairs);
			//Log.e(null, "json" + broadcastList.get(position).getIdx());
			JSONObject jsonObject = null;
			try{
				jsonObject = new JSONObject(strJson);
				//JSONObject json_data = jsonObject.getJSONObject(0); 
				//detailTitle = json_data.getString("title");
				result_code = jsonObject.getString("result");
			}catch(JSONException e){
				
			}finally{
				isChannelClicked = false;
    		}
			
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		mProgress.dismiss();
    		if(result_code.equals("Y")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(ShortChannelActivity.this);
    			alt_bld.setMessage("단축채널이 지정되었습니다.");
			    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	getShortTask = new GetShortTask(getBaseContext());
		                getShortTask.execute();
		                slmenu.setVisibility(View.GONE);
		                switch(clickIdx){
			                case 1:
			                	shorNum1.requestFocus();
			                break;
			                case 2:
			                	shorNum2.requestFocus();
			                break;
			                case 3:
			                	shorNum3.requestFocus();
			                break;
			                case 4:
			                	shorNum4.requestFocus();
			                break;
			                case 5:
			                	shorNum5.requestFocus();
			                break;
			                case 6:
			                	shorNum6.requestFocus();
			                break;
			                case 7:
			                	shorNum7.requestFocus();
			                break;
			                case 8:
			                	shorNum8.requestFocus();
			                break;
			                case 9:
			                	shorNum9.requestFocus();
			                break;
			                case 0:
			                	shorNum10.requestFocus();
			                break;
		                }
		                
					    
		                dialog.dismiss();
		            }
		        });
			    AlertDialog alert = alt_bld.create();
			    alert.show();
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

