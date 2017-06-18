 package com.new1.settop;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.SoundSearcher;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

 public class BroadcastListActivity extends Activity {

	private String mainid = "";
	protected String subid = "";
	protected String subcate = "";
	private ArrayList<BroadcastList> broadcastList = null;
	private ArrayList<BroadcastList> allList = null;
	private ImageAdapter imageAdapter = null;
	private Context mContext = null;
	private ViewNextTask viewNextTask = null;
	private TextView coverTextView = null;
	private ViewTextTask viewTextTask = null;
	private int nowCoverFlowIdx = 0;
	private boolean isDestroy = false;
	private ImageView ivTel = null;
	
	private TextView txtNowDay = null;
	private TextView txtNowTime = null;
	//modify
	//private EditText searchText = null;
	//private Button searchButton = null;
	
	private RelativeLayout linearLayout1;
	private RelativeLayout buttonLayout;
	private EditText editSearch;
	private ImageView btnSearch;
	private ImageView nosearch;
	private TextView txtCategory;
	private TextView pagetext;
	
	private Toast noSearchToast = null;
	
	private ImageView btnLeft = null;
	private ImageView btnRight = null;
	
	private RelativeLayout rlMenu = null;
	
	private ImageView imgLock = null;
	private ImageView imgNet = null;
	
	private boolean isAllListLoad;
	
	private boolean menuVisible = false;
	private Button btnMenu0;
	public BroadcastListActivity(){
		broadcastList = new ArrayList<BroadcastList>();
		allList =  new ArrayList<BroadcastList>();
		nowCoverFlowIdx = 0;
		
		numberBtn = new ArrayList<ImageView>();
		pageIdx = 0;
		remainder = 0;
		maxPageIdx = 0;
	}
	
	ImageView btnMenu;
	private int maxPageIdx;		//페이지
	private int remainder;	
	private int pageIdx;		//현재 페이지
	
	private ArrayList<String> numberList = null;
	private ArrayList<ImageView> numberBtn = null;
	
	private int[] numberBtnName = {
			R.id.detailshowbtn0, R.id.detailshowbtn1, R.id.detailshowbtn2, R.id.detailshowbtn3, R.id.detailshowbtn4,
			R.id.detailshowbtn5, R.id.detailshowbtn6, R.id.detailshowbtn7, R.id.detailshowbtn8, R.id.detailshowbtn9,
			R.id.detailshowbtn10, R.id.detailshowbtn11
		};
	
	private int[] numberBorderName = {
			R.id.detailshowborder0, R.id.detailshowborder1, R.id.detailshowborder2, R.id.detailshowborder3, R.id.detailshowborder4,
			R.id.detailshowborder5, R.id.detailshowborder6, R.id.detailshowborder7, R.id.detailshowborder8, R.id.detailshowborder9,
			R.id.detailshowborder10, R.id.detailshowborder11
		};
	private ImageView borders[] = new ImageView[12];
	
	
	private ArrayList<ImageView> searchBtnList = null;
	private ImageView ivSearchBtns[] = new ImageView[19];
	private int[] searchBtns = {
			R.id.btnKeyG, R.id.btnKeyGG, R.id.btnKeyN, R.id.btnKeyD, R.id.btnKeyDD,
			R.id.btnKeyL, R.id.btnKeyM, R.id.btnKeyB, R.id.btnKeyBB, R.id.btnKeyS,
			R.id.btnKeySS, R.id.btnKeyO, R.id.btnKeyJ, R.id.btnKeyJJ, R.id.btnKeyCH,
			R.id.btnKeyK, R.id.btnKeyT, R.id.btnKeyP, R.id.btnKeyH
		};
	private String[] searchNames = {
			"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ"
			, "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
	};
	private ImageView searchBack = null;
	
	private Button expireText;
	private String category;
	private TimerTask timerTask;
	
	private void setNumberBtn(){
    	for(int i = 0; i < 12; i++){
    		ImageView tempBtn = (ImageView) findViewById(numberBtnName[i]);
        	numberBtn.add(tempBtn);
        	
        	borders[i] = (ImageView) findViewById(numberBorderName[i]);
        }
    }
	
	private void setSearchBtn(){
    	for(int i = 0; i < 19; i++){
    		final int my = i;
        	ivSearchBtns[i] = (ImageView) findViewById(searchBtns[i]);
        	
        	ivSearchBtns[i].setOnClickListener(new OnClickListener(){
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				editSearch.getText().append(searchNames[my]); 
    			}
            });
        }
    	
    	searchBack = (ImageView) findViewById(R.id.btnKeyBack);
		searchBack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(editSearch.getText().length() > 0)
					editSearch.getText().delete(editSearch.getText().length()-1, editSearch.getText().length());
				//searchString += searchNames[my];
				//editSearch.setText(searchString);
			}
        });
		
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.coverflow);

	    mContext = this.getBaseContext();
	    Intent intent = getIntent();
		Bundle myBundle = intent.getExtras();
		mainid = myBundle.getString("mainid");
		subid =  myBundle.getString("subid");
		ImageLoader.getInstance().init(Util.getConfig(mContext));
		
		setNumberBtn();
		setSearchBtn();
		
        
		btnMenu = (ImageView)findViewById(R.id.menu);	
		btnMenu.setOnTouchListener(new View.OnTouchListener() {	
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(menuVisible){
					addFocus();
					menuVisible = false;
					//coverFlow.requestFocus();
					rlMenu.setVisibility(View.GONE);
					btnMenu.setVisibility(View.VISIBLE);
				}else{
					deleteFocus();
					rlMenu.setVisibility(View.VISIBLE);
				    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listmenu_sliding_open);
					animation.setAnimationListener(animationListener2);
					rlMenu.startAnimation(animation);	
					btnMenu.setVisibility(View.GONE);
					menuVisible = true;
				}
				return false;
			}
		});
		
		linearLayout1 =  (RelativeLayout) findViewById(R.id.Layout1);
		buttonLayout =  (RelativeLayout) findViewById(R.id.buttonLayout);
		editSearch =  (EditText) findViewById(R.id.editSearch);
        btnSearch =  (ImageView) findViewById(R.id.btnSearch);
        nosearch = (ImageView)findViewById(R.id.nosearch);
        editSearch.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        txtCategory =  (TextView) findViewById(R.id.categorytext);
        pagetext =  (TextView) findViewById(R.id.pagetext);
        
        btnSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clickOkButton();
			}
        	
        });
        editSearch.setOnKeyListener(new OnKeyListener() {           
            public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {      
                  if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {   
                	  clickOkButton();
                	  return true;           
                  } else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4){
                	  finish();
                	  return false;   
                  }
                }
                return false;   
              }           
          });   
        
		rlMenu = (RelativeLayout)findViewById(R.id.rlMenu);
		
		imgLock = (ImageView)findViewById(R.id.imgLock);
		if(!Util.getChildset(mContext))
			imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        imgNet = (ImageView)findViewById(R.id.imgNet);
        if(!Util.checkNetwordState(mContext))
        	imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        
        btnMenu0 = (Button)findViewById(R.id.btnMenu0);
        final Button btnMenu1 = (Button)findViewById(R.id.btnMenu1);
		final Button btnMenu2 = (Button)findViewById(R.id.btnMenu2);
		final Button btnMenu3 = (Button)findViewById(R.id.btnMenu3);
		final Button btnMenu4 = (Button)findViewById(R.id.btnMenu4);
		final Button btnMenu5 = (Button)findViewById(R.id.btnMenu5);
		final Button btnMenu6 = (Button)findViewById(R.id.btnMenu6);
		final Button btnMenu7 = (Button)findViewById(R.id.btnMenu7);
		final Button btnMenu8 = (Button)findViewById(R.id.btnMenu8);
		final Button btnMenu9 = (Button)findViewById(R.id.btnMenu9);
		final Button btnMenu10 = (Button)findViewById(R.id.btnMenu10);
		final Button btnMenu11 = (Button)findViewById(R.id.btnMenu11);
		
		if(subid.equals("002")){
			category = "다시보기" + " ＞ " + "드라마" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			
			btnMenu2.setText("KBS");
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "008";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setText("MBC");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "009";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("SBS");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "010";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("케이블");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "011";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("유아교육");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "128";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});

			btnMenu7.setText("특집방송");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "012";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("006")){
			category = "다시보기" + " ＞ " + "오락방송" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			
			btnMenu2.setText("KBS");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "014";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("MBC");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "015";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("SBS");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "016";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("케이블");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "018";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("유아교육");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "107";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("특집방송");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "017";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("116")){
			category = "다시보기" + " ＞ " + "시사교양" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});

			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			
			btnMenu2.setText("KBS");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "121";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("MBC");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "132";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("SBS");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "133";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("케이블");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "134";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("다큐");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "135";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("특집방송");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "136";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("013")){
			category = "다시보기" + " ＞ " + "유아교육" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			
			btnMenu2.setText("유아자료");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "113";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("아동자료");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "019";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("영어교육");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "020";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("율동동요");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "021";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("교육자료");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "022";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("기타");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "112";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("007")){
			category = "다시보기" + " ＞ " + "케이블" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			
			btnMenu2.setText("미국드라마");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "099";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("성인드라마");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "102";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("CSI드라마");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "103";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("중국드라마");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "100";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("일본드라마");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "101";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("기타");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "115";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("026")){
			category = "다시보기" + " ＞ " + "방송창고" + " ＞ ";
			txtCategory.setText(category);
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
			
			btnMenu2.setText("KBS");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "039";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("KBS2");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "040";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("MBC");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "041";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("SBS");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "042";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("케이블");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "043";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("시사오락");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "129";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu8.setText("다큐");
			btnMenu8.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "130";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu8.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu8.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu8.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu9.setText("특집방송");
			btnMenu9.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "106";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu9.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu9.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu9.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu10.setText("기타");
			btnMenu10.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "106";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu10.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu10.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu10.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu11.setVisibility(View.GONE);
		}else if(subid.equals("116")){
			btnMenu0.setText("검색");
			btnMenu0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					hideMenu();
					linearLayout1.setVisibility(View.VISIBLE);
					buttonLayout.setVisibility(View.GONE);
					ivSearchBtns[0].requestFocus();
					
				}
			});
			btnMenu0.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu0.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu1.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
					}
				}
			});
			btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
			btnMenu1.setText("관람순");
			btnMenu1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					GetFavoriteTask getFavoriteTask = new GetFavoriteTask(mContext);
					getFavoriteTask.execute();
					hideMenu();
					
				}
			});
		
			btnMenu2.setText("KBS");
			btnMenu2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "121";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu2.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu2.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu2.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu3.setText("MBC");
			btnMenu3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "132";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu3.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu3.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu3.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu4.setText("SBS");
			btnMenu4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "133";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu4.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu4.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu4.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu5.setText("케이블");
			btnMenu5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "134";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu5.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu5.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu5.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu6.setText("다큐");
			btnMenu6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "135";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu6.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu6.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu6.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			btnMenu7.setText("특집방송");
			btnMenu7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					subcate = "136";
					GetTask getTask = new GetTask(mContext);
					getTask.execute();
					hideMenu();
				}
			});
			btnMenu7.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						btnMenu7.setTextColor(Color.parseColor("#0060A5"));
					} else {
						btnMenu7.setTextColor(Color.parseColor("#FFFFFF"));
					}
				}
			});
			
			btnMenu8.setVisibility(View.GONE);
			btnMenu9.setVisibility(View.GONE);
			btnMenu10.setVisibility(View.GONE);
			btnMenu11.setVisibility(View.GONE);
		}
		

		btnLeft = (ImageView) findViewById(R.id.pageleft);
		btnRight = (ImageView) findViewById(R.id.pageright);
    	
		btnRight.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
				//if(nowCoverFlowIdx >= broadcastList.size())
				//	nowCoverFlowIdx = broadcastList.size() - 1;
				//coverFlow.setSelection(nowCoverFlowIdx);
				
				if(pageIdx < maxPageIdx-1)
            	  	pageIdx++;
            	  refreshPage();
				return;
			}
		});
		
		btnLeft.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//nowCoverFlowIdx = coverFlow.getSelectedItemPosition() - 1;
				//if(nowCoverFlowIdx < 0)
				//	nowCoverFlowIdx = 0;
				//coverFlow.setSelection(nowCoverFlowIdx);
				if(pageIdx > 0)
          		  pageIdx--;
          	  refreshPage();
				return;
			}
		});
		
		
		
		//modify
		//searchButton = (Button)findViewById(R.id.searchButton);
		//searchText = (EditText)findViewById(R.id.searchText);
		ImageDownloader imageDownloader = new ImageDownloader();
		ivTel = (ImageView)findViewById(R.id.tel);
		//ivTel.setImageResource(R.drawable.image_main_logo);
		imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView)ivTel);
		coverTextView = (TextView) findViewById(R.id.coverflowtext);
		expireText = (Button) findViewById(R.id.imgExpireDate);
		Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if(!getExpireDate().equals("")){
        	expireText.setText(getExpireDate().substring(0, 2));
        	expireText2.setText(getExpireDate().substring(2, 4));
        }
        
		
		noSearchToast = Toast.makeText(this, getString(R.string.nosearch), Toast.LENGTH_SHORT);
		GetTask getTask = new GetTask(mContext);
		getTask.execute();
		
		LoadAllDataTask loadAllTask = new LoadAllDataTask(mContext);
		loadAllTask.execute();
		
		for(int i = 0; i < numberBtn.size(); i++){
			numberBtn.get(i).setOnFocusChangeListener(new OnFocusChangeListener() {

	            public void onFocusChange(View v, boolean hasFocus) {
	            	if(hasFocus){
	            		for(int j = 0; j < numberBtn.size(); j++)
	            			if(v == numberBtn.get(j))	{
	            				borders[j].setVisibility(View.VISIBLE);
	            				coverTextView.setText(broadcastList.get(pageIdx*12 + j).getTitle());
	            			}
	    			}else{
	    				for(int j = 0; j < numberBtn.size(); j++)
	    					if(v == numberBtn.get(j))	
	    						borders[j].setVisibility(View.GONE);
	    				
	    			}
	            }
	        });
	        numberBtn.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int position = -1;
					for(int i = 0; i < numberBtn.size(); i++){
						if(v.getId() == numberBtn.get(i).getId()){
							position = i;
						}
					}
					
					position = pageIdx*12 + position;
					
					if(subid.equals("update")) {
						if(broadcastList.get(position).getType().equals("show"))
							goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
						else
							goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("002")){
						goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("006")){
						goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("013")){
						goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("007")){
						goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("026")){
						goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}else if(subid.equals("116")){
						goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), category, subid, broadcastList.get(position).getImage());
					}
					
					
					
				}
			});
        }
		//modify
		/*searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				boolean searched = false;
				// TODO Auto-generated method stub
				if(!searchText.getText().toString().equals("")){
					for(int i =0; i < broadcastList.size(); i++){
						if(broadcastList.get(i).getTitle() != null && searchText.getText().toString() != null){
							if(broadcastList.get(i).getTitle().indexOf(searchText.getText().toString()) != -1){
								searched = true;
								coverFlow.setSelection(i);
								break;
							}
							if(!searched)
								noSearchToast.show();
						}
					}
				}
			}
		});*/
		setMenuopenAnimation();
		//if(numberBtn.get(0) != null)
		btnRight.requestFocus();
			
			
		numberBtn.get(0).setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT ){
                	  if(pageIdx > 0)
                		  pageIdx--;
                	  refreshPage();
                	  return true;   
                  }
                }
                return false;   
              }                  
          });  
        
        numberBtn.get(11).setOnKeyListener(new OnKeyListener() {           
        	public boolean onKey(View v, int keyCode, KeyEvent event) {           
                if (event.getAction() == KeyEvent.ACTION_DOWN) {       
                  if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ){
                	  if(pageIdx < maxPageIdx-1)
                	  	pageIdx++;
                	  refreshPage();
                	  return true;   
                  }
                }
                return false;   
              }                  
          });  
	}
	
	
	
	private void clickOkButton(){
		if(!editSearch.getText().toString().trim().equals("")){
			
			GetTask2 getTask = new GetTask2(mContext);
			getTask.execute();
			
			
		}
	}
	
	private AnimationListener animationListener2 = null;
	private void setMenuopenAnimation(){
    	animationListener2 = new AnimationListener()
	    {
			public void onAnimationEnd(Animation animation) {
				btnMenu0.requestFocus();
				coverTextView.setText("");
			}
			public void onAnimationRepeat(Animation animation) {btnMenu0.requestFocus();}
			public void onAnimationStart(Animation animation) {}
	    };
    }
	
	@Override
	protected void onDestroy() {
		if(timerTask != null){
			timerTask.cancel();
		}
		isDestroy = true;
		// TODO Auto-generated method stub
		Log.e(null, "destroy");
		if(viewNextTask != null){
			viewNextTask.cancel(true);
			viewNextTask = null;
		}
		if(viewTextTask != null){
			viewTextTask.cancel(true);
			viewTextTask = null;
		}
		recycleAllBitmap();
		super.onDestroy();
	}
	
	private boolean getAllDatas(){
		int startIdx = 0;
		int endIdx = 29999;
		
		allList = new ArrayList<BroadcastList>();
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if(subid.equals("update")){
			nameValuePairs.add(new BasicNameValuePair("update", "U"));
			if(!subid.equals(""))
				nameValuePairs.add(new BasicNameValuePair("code2", subid));
		}else{
			nameValuePairs.add(new BasicNameValuePair("code2", subid));
			nameValuePairs.add(new BasicNameValuePair("code3", subcate));
		}
		nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
		nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/broadcastlist.php", nameValuePairs);
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			for(int i = 0; i < jArray.length(); i++){
				BroadcastList tempList = new BroadcastList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("idx"));
				tempList.setVod_code(json_data.getString("vod_code"));
				tempList.setImage(json_data.getString("image"));
				tempList.setTitle(json_data.getString("title"));
				tempList.setPu_no(json_data.getString("pu_no"));
				tempList.setType(json_data.getString("type"));
				allList.add(i, tempList);
			}
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
		/*
		for(int i = 0; i < 5000; i++){
			BroadcastList tempList = new BroadcastList();
			broadcastList.add(tempList);
		}*/
		if(jArray.length() <= 0){
			return false;
		}else{
			return true;
		}
	}

	//커버플로우에 넣어줄 이미지 데이터를 데이터구조에 넣어 둠
	private boolean getImages(){
		int startIdx = 0;
		int endIdx = 1999;
		
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if(subid.equals("update")){
			nameValuePairs.add(new BasicNameValuePair("update", "U"));
			if(!subid.equals(""))
				nameValuePairs.add(new BasicNameValuePair("code2", subid));
		}else{
			nameValuePairs.add(new BasicNameValuePair("code2", subid));
			nameValuePairs.add(new BasicNameValuePair("code3", subcate));
		}
		nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
		nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/broadcastlist.php", nameValuePairs);
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			for(int i = 0; i < jArray.length(); i++){
				BroadcastList tempList = new BroadcastList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("idx"));
				tempList.setVod_code(json_data.getString("vod_code"));
				tempList.setImage(json_data.getString("image"));
				tempList.setTitle(json_data.getString("title"));
				tempList.setPu_no(json_data.getString("pu_no"));
				tempList.setType(json_data.getString("type"));
				broadcastList.add(i, tempList);
			}
		}catch(JSONException e){
			Log.e(null, e.toString()); 
		}
		/*
		for(int i = 0; i < 5000; i++){
			BroadcastList tempList = new BroadcastList();
			broadcastList.add(tempList);
		}*/
		if(jArray.length() <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean getImages2(){
		int startIdx = 0;
		int endIdx = 1999;
		
		broadcastList = new ArrayList<BroadcastList>();
		
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("keyword", editSearch.getText().toString().trim()));
		nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
		nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		
		nameValuePairs.add(new BasicNameValuePair("code1", "104"));
		
		if(subid.equals("update")){
			nameValuePairs.add(new BasicNameValuePair("update", "U"));
			if(!subcate.equals(""))
				nameValuePairs.add(new BasicNameValuePair("code2", subcate));
		}else{
			nameValuePairs.add(new BasicNameValuePair("code2", subid));
			nameValuePairs.add(new BasicNameValuePair("code3", subcate));
		}
		
		
		
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/search.php", nameValuePairs);
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			for(int i = 0; i < jArray.length(); i++){
				BroadcastList tempList = new BroadcastList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("idx"));
				tempList.setType(json_data.getString("vod_type"));
				tempList.setImage(json_data.getString("image"));
				tempList.setTitle(json_data.getString("title"));
				tempList.setCode2(json_data.getString("code2"));
				
				//tempList.setPu_no(json_data.getString("pu_no"));
				//list.add(tempList);
				broadcastList.add(i, tempList);
			}
			
		}catch(JSONException e){
			Log.e(null, e.toString()); 
			return false;
		}
		if(jArray.length() <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	private boolean getFavorite(){
		int startIdx = 0;
		int endIdx = 1999;
		
		broadcastList = new ArrayList<BroadcastList>();
		
		//ranking.php?type={유형}&top={순위}&code2={2차분류}&code3={3차분류}
		
		String strJson = "";
		PostHttp postmake = new PostHttp();
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("top", String.valueOf(endIdx)));
		nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
		
		nameValuePairs.add(new BasicNameValuePair("type", "broadcast"));
		
		if(subid.equals("update")){
			nameValuePairs.add(new BasicNameValuePair("update", "U"));
			if(!subcate.equals(""))
				nameValuePairs.add(new BasicNameValuePair("code2", subcate));
		}else{
			nameValuePairs.add(new BasicNameValuePair("code2", subid));
			//nameValuePairs.add(new BasicNameValuePair("code3", subcate));
		}
		
		
		
		strJson = postmake.httpConnect(
				Constant.mainUrl + "/module/tv/ranking.php", nameValuePairs);
		JSONArray jArray = null;
		try{
			//JSONObject json = new JSONObject(strJson);
			jArray = new JSONArray(strJson);
			for(int i = 0; i < jArray.length(); i++){
				BroadcastList tempList = new BroadcastList();
				JSONObject json_data = jArray.getJSONObject(i); 
				tempList.setIdx(json_data.getString("code"));
				//tempList.setType(json_data.getString("vod_type"));
				tempList.setImage(json_data.getString("image"));
				tempList.setTitle(json_data.getString("title"));
				if(json_data.getString("title").equals("")){
					break;
				}
				//tempList.setCode2(json_data.getString("code2"));
				//tempList.setPu_no(json_data.getString("pu_no"));
				//list.add(tempList);
				broadcastList.add(i, tempList);
			}
			
		}catch(JSONException e){
			Log.e(null, e.toString()); 
			return false;
		}
		if(jArray.length() <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	private void hideMenu(){
		linearLayout1.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.VISIBLE);
		menuVisible = false;
		//if(numberBtn.get(0) != null)
		//	numberBtn.get(0).requestFocus();
		btnRight.requestFocus();
		//coverFlow.requestFocus();
		rlMenu.setVisibility(View.GONE);
		btnMenu.setVisibility(View.VISIBLE);
	}
	
	private String getExpireDate(){
		SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
		return sp.getString("expiredate", "");
	}
	
	private Bitmap[] bitmap = new Bitmap[12];
	  //다음 페이지로 이동한 후 ui를 갱신시켜 줌
    private void refreshPage(){
    	if(broadcastList != null){
        	maxPageIdx = broadcastList.size()/12;
        	remainder = broadcastList.size()%12;
        }
        if(remainder > 0)
        	maxPageIdx++;
        pagetext.setText("" +  (pageIdx+1) + "/" + maxPageIdx);
    	for(int i = 0; i < 12;
    			i++){
    		if(bitmap[i] != null){
    			bitmap[i].recycle();
    			bitmap[i] = null;
    		}
    	}
    	
    	UrlImageLoader urlImageload = new UrlImageLoader();
    	int numberidx = pageIdx*12;
        for(int i = 0; i < 12; i++){
        	if(numberBtn != null){
        		if(numberidx < broadcastList.size()){
        			//numberBtn.get(i).setText(broadcastList.get(numberidx).getTitle());
        			//bitmap[i] = urlImageload.getUrlImage(broadcastList.get(numberidx).getImage());
        			//numberBtn.get(i).setImageBitmap(bitmap[i]);
        			
        			ImageLoader imageLoader = ImageLoader.getInstance();
        			imageLoader.displayImage(broadcastList.get(numberidx).getImage(), numberBtn.get(i), Util.getImageLoaderOption(), new SimpleImageLoadingListener() {
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
        		numberidx++;
        		numberBtn.get(i).setVisibility(View.VISIBLE);
        		if(numberidx > broadcastList.size())
        			numberBtn.get(i).setVisibility(View.INVISIBLE);
        	}
        }
    }
    
	
	//커버플로우 설정
	/*private void setCoverFlow(){
		coverFlow = (CoverFlow) findViewById(R.id.coverflow);
	    imageAdapter = new ImageAdapter(this);
		coverFlow.setAdapter(imageAdapter);
		
		coverFlow.setSpacing(-5);
		coverFlow.setAnimationDuration(500);
		
		viewNextTask = new ViewNextTask(mContext, broadcastList, coverFlow.getSelectedItemPosition()-11, 
				coverFlow.getSelectedItemPosition()+11);
		viewNextTask.execute();

		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		     @Override
		     public void onItemSelected(AdapterView<?> arg0, View arg1,
		       int arg2, long arg3) {
		    	 //coverFlow.requestFocus();
		    	 viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-11, 
		    			 (int)arg3+11);
		 		 viewNextTask.execute();
		    	 Log.e(null, broadcastList.get((int)arg3).getTitle());
		    	 viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int)arg3).getTitle());
		    	 viewTextTask.execute();
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
				if(subid.equals("update")) {
					if(broadcastList.get(position).getType().equals("show"))
						goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
					else
						goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("002")){
					goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("006")){
					goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("013")){
					goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("007")){
					goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("026")){
					goDetailDramaActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}else if(subid.equals("116")){
					goDetailShowActivity(broadcastList.get(position).getIdx(), broadcastList.get(position).getTitle(), subid, broadcastList.get(position).getImage());
				}
			}
		});
	}*/
    
    
    private void deleteFocus(){
    	for(int i = 0; i < numberBtn.size(); i++){
			numberBtn.get(i).setFocusable(false);
    	}
    	btnLeft.setFocusable(false);
    	btnRight.setFocusable(false);
    }
    
    private void addFocus(){
    	for(int i = 0; i < numberBtn.size(); i++){
			numberBtn.get(i).setFocusable(true);
    	}
    	btnLeft.setFocusable(true);
    	btnRight.setFocusable(true);
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if(!menuVisible){
				nowCoverFlowIdx += 9;
				if(nowCoverFlowIdx >= broadcastList.size())
					nowCoverFlowIdx = broadcastList.size() - 1;
				//coverFlow.setSelection(nowCoverFlowIdx);
			}
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if(!menuVisible){
				nowCoverFlowIdx -= 9;
				if(nowCoverFlowIdx < 0)
					nowCoverFlowIdx = 0;
				//coverFlow.setSelection(nowCoverFlowIdx);
			}
		}else if(keyCode == 41 || keyCode == 88) {
      	  if(pageIdx > 0)
      		  pageIdx--;
      	  refreshPage();
		}else if(keyCode == 30 || keyCode == 87) {
			if(pageIdx < maxPageIdx-1)
      	  	pageIdx++;
      	  refreshPage();
		}else if(keyCode == KeyEvent.KEYCODE_MENU || keyCode == 29) {
			if(menuVisible){
				addFocus();
				menuVisible = false;
				//coverFlow.requestFocus();
				rlMenu.setVisibility(View.GONE);
				btnMenu.setVisibility(View.VISIBLE);
			}else{
				deleteFocus();
				rlMenu.setVisibility(View.VISIBLE);
			    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listmenu_sliding_open);
				animation.setAnimationListener(animationListener2);
				rlMenu.startAnimation(animation);	
				btnMenu.setVisibility(View.GONE);
				menuVisible = true;
			}
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
			clickOkButton();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void closeKeyboad(){
		  InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		  if(editSearch != null)
			  inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
		  
	} 
	
	private void goDetailDramaActivity(String idx, String title, String category, String menu, String image){
		Intent intent = new Intent(BroadcastListActivity.this, DetailDramaActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();
		myData.putString("mainid", mainid);
		myData.putString("image", image);
        myData.putString("idx", idx);
        myData.putString("menu", menu);
        myData.putString("title", title);
        myData.putString("category", category);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	private void goDetailShowActivity(String idx, String title, String category, String menu, String image){
		Intent intent = new Intent(BroadcastListActivity.this, DetailShowActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle myData = new Bundle();	
		myData.putString("mainid", mainid);
		myData.putString("image", image);
        myData.putString("idx", idx);
        myData.putString("menu", menu);
        myData.putString("title", title);
        myData.putString("category", category);
        intent.putExtras(myData);
		startActivity(intent);
	}
	
	class LoadAllDataTask extends AsyncTask<String, Integer, Long> {
    	private Context mContext;
    	
    	public LoadAllDataTask(Context context) {
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
    		getAllDatas();
    		return 0L;
    	}
    
    	@Override
    	protected void onPostExecute(Long result) {
    		// TODO Auto-generated method stub
    		isAllListLoad = true;
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
	
	//서버에서 이미지를 받아 옴
	 class ViewNextTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;
    	private int startIdx;
    	private int endIdx;
    	//private Context mContext;
    	
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
    		UrlImageLoader urlImageload = new UrlImageLoader();
    		if(startIdx < 0)
    			startIdx = 0;
    		if(endIdx > broadcastList.size())
    			endIdx = broadcastList.size();
    		for(int i = startIdx;i < endIdx; i++){
    			if(broadcastList.get(i).getBitmap() == null)
    				broadcastList.get(i).setBitmap(urlImageload.getUrlImage(list.get(i).getImage()));
    			if(i % 10 == 0)
    				publishProgress(1);
    		}
	    	//image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		if( startIdx/100 > 1)
    			for(int i = 0; i < startIdx - (startIdx%100) - 10; i++){
    				if(broadcastList.get(i).getBitmap() != null){
    					broadcastList.get(i).recyleBitmap();
    			}
    		}
    		if( endIdx/100 < broadcastList.size()/100-1){
    			for(int i = endIdx + 10; i < broadcastList.size(); i++){
	    			if(broadcastList.get(i).getBitmap() != null){
	    				broadcastList.get(i).recyleBitmap();
	    			}
    			}
    		}
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
    		imageAdapter.notifyDataSetChanged();
    	}
    
    	@Override
    	protected void onCancelled() {
    		// TODO Auto-generated method stub
    		super.onCancelled();
    	}	
    }
	
	 class ViewTextTask extends AsyncTask<String, Integer, Long> {
	    	private  ArrayList<BroadcastList> list = null;
	    	private String text;
	    	private Context mContext;
	    	
	    	public ViewTextTask(Context context, ArrayList<BroadcastList> list, String text) {
	    		this.text = text;
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
	    		try{
	    		Thread.sleep(700);
	    		}catch(Exception e){}
	    		return 0L;
	    	}
	    
	    	@Override
	    	protected void onPostExecute(Long result) {
	    		// TODO Auto-generated method stub
	    		coverTextView.setText(text);
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
	 
	 public class ImageAdapter extends BaseAdapter {
			int itemBackground;
			private Context mContext;

			public ImageAdapter(Activity c) {
				mContext = c;
			}

			public int getCount() {
				return broadcastList.size();
			}

			public Object getItem(int position) {
				return broadcastList.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView i = new ImageView(mContext);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setLayoutParams(new CoverFlow.LayoutParams(160, 230));
				i.setPadding(1, 1, 1, 1);
				
				i.setBackgroundResource(R.drawable.listborder);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setImageBitmap(broadcastList.get(position).getBitmap());

				BitmapDrawable drawable = (BitmapDrawable) i.getDrawable();
				drawable.setAntiAlias(true);
				
				return i;
			}

			public float getScale(boolean focused, int offset) {
				return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
			}
		}
	 
	 private void recycleAllBitmap(){
		for(int i = 0; i <broadcastList.size(); i++)
			broadcastList.get(i).recyleBitmap();
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
    		broadcastList = new ArrayList<BroadcastList>();
    		getImages();
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		return 0L;
    	}
    	
    	 
    	protected void showProgress() {
    		mProgress = new ProgressDialog(BroadcastListActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetTask.this.cancel(true);
                }
            });

            String message = mContext.getString(R.string.str_wait);
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
    		//setCoverFlow();
    		pageIdx = 0;
    		remainder = 0;
    		maxPageIdx = 0;
    		addFocus(); 
            refreshPage();
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
	
	class GetTask2 extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetTask2(Context context) {
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
    		boolean result = false;
    		
    		broadcastList = new ArrayList<BroadcastList>();
    		for(int i = 0; i < allList.size(); i++){
    			if(SoundSearcher.matchString(allList.get(i).getTitle().toLowerCase().replaceAll(" ",""), editSearch.getText().toString().trim().toLowerCase())){
    				broadcastList.add(allList.get(i));
    				result = true;
        		}
			}
    		if(result)
    			return 0L;
    		else
    			return 1L;
    	}
    	
    	 
    	protected void showProgress() {
    		mProgress = new ProgressDialog(BroadcastListActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetTask2.this.cancel(true);
                }
            });

            String message = mContext.getString(R.string.str_wait);
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
    		if(result == 0L){
    			nosearch.setVisibility(View.GONE);
    			dismissProgress();
    			//setCoverFlow();
    			pageIdx = 0;
    			remainder = 0;
    			maxPageIdx = 0;
    	        refreshPage();
    			//editSearch.setVisibility(View.INVISIBLE);
    	        //btnSearch.setVisibility(View.INVISIBLE);
    	        linearLayout1.setVisibility(View.GONE);
    	        buttonLayout.setVisibility(View.VISIBLE);
    	        //coverFlow.setVisibility(View.VISIBLE);
    	        addFocus();
    		}else{
    			dismissProgress();
    			//nosearch.setVisibility(View.VISIBLE);
    			//setCoverFlow();
    			Toast.makeText(mContext, mContext.getString(R.string.nosearch), Toast.LENGTH_SHORT).show();
    	        refreshPage();
    			//coverFlow.setVisibility(View.GONE);
    	        addFocus();
    		}
    		closeKeyboad();
    		editSearch.setText("");
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
	
	class GetFavoriteTask extends AsyncTask<String, Integer, Long> {
    	private  ArrayList<BroadcastList> list = null;

    	
    	public GetFavoriteTask(Context context) {
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
    		boolean result = false;
    		result = getFavorite();
	    		//				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
    		//imageAdapter
    		//publishProgress(1);
    		if(result)
    			return 0L;
    		else
    			return 1L;
    	}
    	
    	 
    	protected void showProgress() {
    		mProgress = new ProgressDialog(BroadcastListActivity.this);
    		mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                	GetFavoriteTask.this.cancel(true);
                }
            });

            String message = mContext.getString(R.string.str_wait);
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
    		if(result == 0L){
    			nosearch.setVisibility(View.GONE);
    			dismissProgress();
    			//setCoverFlow();
    			
    			pageIdx = 0;
        		remainder = 0;
        		maxPageIdx = 0;
    	        refreshPage();
    			//editSearch.setVisibility(View.INVISIBLE);
    	        //btnSearch.setVisibility(View.INVISIBLE);
    	        linearLayout1.setVisibility(View.GONE);
    	        buttonLayout.setVisibility(View.VISIBLE);
    	        //coverFlow.setVisibility(View.VISIBLE);
    	        addFocus();
    		}else{
    			dismissProgress();
    			nosearch.setVisibility(View.VISIBLE);
    			//setCoverFlow();
    	        refreshPage();
    			//coverFlow.setVisibility(View.GONE);
    	        addFocus();
    		}
    		closeKeyboad();
    		editSearch.setText("");
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

