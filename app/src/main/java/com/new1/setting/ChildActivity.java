package com.new1.setting;

import java.util.ArrayList;    

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.new1.settop.R;
import com.new1.model.Constant;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class ChildActivity extends Activity {
    /** Called when the activity is first created. */
	private String strPasswd;
    private String strNowPasswd;
	private String strPasswdConfirm;
	private EditText editPasswd;
    private EditText editPasswdNow;
    private EditText editPasswdConfirm;

    private Button toggleBtn1;
    private Button toggleBtn2;
    private Button toggleBtn3;
	
	private boolean safeMode12;
	private boolean safeMode15;
	private boolean safeMode19;
	
	private Button btnModify;
	private Button btnSend;
	
	private SendRechargeTask sendRechargeTask= null;
	private ProgressDialog mProgress;
	private Button expireText;
	private ImageView ivTel = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	private Context mContext = null;

	private boolean isCheckedPasswd = false;
	
	public ChildActivity(){
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child);
        
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
        
        expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
        
        
        editPasswd =  (EditText) findViewById(R.id.editPasswd);
        editPasswdNow =  (EditText) findViewById(R.id.editPasswdNow);
        editPasswdConfirm =  (EditText) findViewById(R.id.editPasswdConfirm);

		toggleBtn1 =  (Button) findViewById(R.id.toggleBtn1);
		toggleBtn2 =  (Button) findViewById(R.id.toggleBtn2);
		toggleBtn3 =  (Button) findViewById(R.id.toggleBtn3);
		toggleBtn1.requestFocus();
        
        //editPasswd.setInputType(InputType.TYPE_CLASS_NUMBER
       // 		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPasswd.setOnKeyListener(new OnKeyListener() {           
            public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {      
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  //clickOkButton();
                	  return false;           
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
       // editPasswdModify.setInputType(InputType.TYPE_CLASS_NUMBER
        //		 |InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editPasswdNow.setOnKeyListener(new OnKeyListener() {
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	 // clickOkButton();
                	  return false;           
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
       
        
        
        btnModify = (Button) findViewById(R.id.btnModify);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(toggleBtn1.getWindowToken(), 0);
		btnModify.setOnClickListener(new OnClickListener() {
		   @Override
		   public void onClick(View arg0) {
			   // TODO Auto-generated method stub
			   clickModifyButton();
		   }
	   });


		safeMode12 =  getChildset12();
        if(safeMode12){
			toggleBtn1.setBackgroundResource(R.drawable.button_safe_on_);

        }else{
			toggleBtn1.setBackgroundResource(R.drawable.button_safe_off_);
        }

		toggleBtn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(safeMode12){
					safeMode12 = false;
					toggleBtn1.setBackgroundResource(R.drawable.button_safe_off_);
					safeMode15 = false;
					toggleBtn2.setBackgroundResource(R.drawable.button_safe_off_);
					safeMode19 = false;
					toggleBtn3.setBackgroundResource(R.drawable.button_safe_off_);
				}else{
					safeMode12 = true;
					toggleBtn1.setBackgroundResource(R.drawable.button_safe_on_);
				}
			}
		});

		safeMode15 =  getChildset15();
		if(safeMode15){
			toggleBtn2.setBackgroundResource(R.drawable.button_safe_on_);
		}else{
			toggleBtn2.setBackgroundResource(R.drawable.button_safe_off_);
		}

		toggleBtn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(safeMode15){
					safeMode15 = false;
					toggleBtn2.setBackgroundResource(R.drawable.button_safe_off_);
					safeMode19 = false;
					toggleBtn3.setBackgroundResource(R.drawable.button_safe_off_);
				}else{
					safeMode15 = true;
					toggleBtn2.setBackgroundResource(R.drawable.button_safe_on_);
                    safeMode12 = true;
                    toggleBtn1.setBackgroundResource(R.drawable.button_safe_on_);
				}
			}
		});

		safeMode19 =  getChildset19();
		if(safeMode19){
			toggleBtn3.setBackgroundResource(R.drawable.button_safe_on_);
		}else{
			toggleBtn3.setBackgroundResource(R.drawable.button_safe_off_);
		}

		toggleBtn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(safeMode19){
					safeMode19 = false;
					toggleBtn3.setBackgroundResource(R.drawable.button_safe_off_);
				}else{
					safeMode19 = true;
					toggleBtn3.setBackgroundResource(R.drawable.button_safe_on_);
                    safeMode15 = true;
                    toggleBtn2.setBackgroundResource(R.drawable.button_safe_on_);
                    safeMode12 = true;
                    toggleBtn1.setBackgroundResource(R.drawable.button_safe_on_);
				}
			}
		});

        btnSend = (Button) findViewById(R.id.btnSave);
        InputMethodManager imm2 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm2.hideSoftInputFromWindow(btnSend.getWindowToken(), 0);
        btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickSaveButton();
			}

          });

//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				toggle.toggle();
//				if(editString.equals("1")){
//
//					toggle.setTextOff("TOGGLE ON");
//
//				}
//				else if(editString.equals("0")){
//
//					toggle.setTextOn("TOGGLE OFF");
//
//				}
//			}
//		});
//
	}
    
    
	@Override
	protected void onDestroy() {
		if(sendRechargeTask != null){
			sendRechargeTask = null;
		}
		if(mProgress != null){
			mProgress = null;
		}
		
		// TODO Auto-generated method stub
		if(editPasswd != null)
			editPasswd = null;
		if(editPasswdNow != null)
            editPasswdNow = null;
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
			//clickOkButton();
		}
  	  return super.onKeyDown(keyCode, event); 
	}

	private void clickModifyButton(){
		if(editPasswdNow.getText().toString().length() < 4 || editPasswdConfirm.getText().toString().length() < 4) {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage(R.string.check_child_4);
			alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = alt_bld.create();
			alert.show();
		}else{
			strNowPasswd = editPasswdNow.getText().toString();
			strPasswdConfirm = editPasswdConfirm.getText().toString();

			if(getChildNum().equals(strNowPasswd)){
				sendRechargeTask = new SendRechargeTask(getBaseContext());
				sendRechargeTask.execute();
			}else{
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
				alt_bld.setMessage(R.string.check_child_no);
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
	
	private void clickSaveButton(){
		//텍스트 필드를 모두 안채웠을때 
		if(editPasswd.getText().toString().length() < 4){
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage(R.string.check_child_4);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();
		 }else{
			 strPasswd = editPasswd.getText().toString();

			 if(getChildNum().equals(strPasswd)){
				 isCheckedPasswd = true;

				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
				 alt_bld.setMessage(R.string.check_child_ok);
				 alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					 public void onClick(DialogInterface dialog, int which) {
						 dialog.dismiss();
					 }
				 });
				 AlertDialog alert = alt_bld.create();
				 alert.show();

				 //sendRechargeTask = new SendRechargeTask(getBaseContext());
			     //sendRechargeTask.execute();
			 }else{
				 AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_no);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();
			 }
			 //getMacaddress();
		 }
	}
	
	//충전하기
	 class SendRechargeTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	private String result = "";
    	
    	public SendRechargeTask(Context context) {
    		mContext = context;
    	}
    
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		mProgress = ProgressDialog.show(ChildActivity.this, "", getResources().getString(R.string.wait), true, true);

            if(strPasswdConfirm.equals("")){
                strPasswdConfirm = editPasswdConfirm.getText().toString().trim();
            }
    		//mProgress.setCancelable(false);
    	}
    
    	@Override
    	protected Long doInBackground(String... params) {
    		// TODO Auto-generated method stub
    		String strJson = "";
    		PostHttp postmake = new PostHttp();
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

			nameValuePairs.add(new BasicNameValuePair("adult_pwd", strPasswdConfirm));
			String srtMode = "Y";
			if(safeMode12)
				srtMode = "Y";
			else 
				srtMode = "N";
			Log.e(null, "adult:"  + srtMode);
			nameValuePairs.add(new BasicNameValuePair("adult_mode", srtMode));
    		
    		strJson = postmake.httpConnect(
    				Constant.mainUrl +  "/module/tv/member_proc.php", nameValuePairs);
    		try{
    			JSONObject json = new JSONObject(strJson);
    			//jArray = new JSONArray(strJson);
    			result = json.getString("result");
    			
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
    		if(result.equals("Y")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_complete);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
    	                dialog.dismiss();
    	            }
    	        });
    		    AlertDialog alert = alt_bld.create();
    		    alert.show();

				//saveChildSet12();
				//saveChildSet15();
				//saveChildSet19();
    		    saveChildNum(strPasswdConfirm);
    		    editPasswdConfirm.setText("");
    			editPasswdNow.setText("");
    		}else if(result.equals("N")){
    			AlertDialog.Builder alt_bld = new AlertDialog.Builder(ChildActivity.this);
    		    alt_bld.setMessage(R.string.check_child_modify);
    		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
    	            public void onClick(DialogInterface dialog, int which) {
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
    	}
    
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}
    }
	 
	 private void saveChildSet12(boolean passwd){
		 SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		 SharedPreferences.Editor editor = sp.edit();
		 editor.putBoolean("chilset12", passwd);
		 editor.commit();
	 }
	 
	 private boolean getChildset12(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getBoolean("chilset12", false);
	}

	private void saveChildSet15(boolean passwd){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("chilset15", passwd);
		editor.commit();
	}

	private boolean getChildset15(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getBoolean("chilset15", false);
	}

	private void saveChildSet19(boolean passwd){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("chilset19", passwd);
		editor.commit();
	}

	private boolean getChildset19(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getBoolean("chilset19", false);
	}
	 
	 private void saveChildNum(String passwd){
		 SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		 SharedPreferences.Editor editor = sp.edit();
		 editor.putString("childnum", passwd);
		 editor.commit();
	 }
	
	 private String getChildNum(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("childnum", "0000");
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
	
}

