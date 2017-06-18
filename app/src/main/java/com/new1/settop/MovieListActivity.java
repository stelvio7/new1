package com.new1.settop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;

import com.new1.settop.delivery.DeliveryListActivity;
import com.new1.settop.shopping.DetailYellowActivity;
import com.new1.settop.shopping.LifeListActivity;
import com.new1.settop.shopping.ShoppingListActivity;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.SoundSearcher;
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

public class MovieListActivity extends Activity {
    private int nowPositon;
    private String mainid = "";
    protected String subid = "";
    protected String subcate = "";
    private String genreid = "";
    private int genreidx = 0;
    private ArrayList<BroadcastList> broadcastList = null;
    private ArrayList<BroadcastList> allList = null;
    private ImageAdapter imageAdapter = null;
    private Context mContext = null;
    private ViewNextTask viewNextTask = null;
    private ViewTextTask viewTextTask = null;
    private TextView coverTextView = null;
    private int nowCoverFlowIdx = 0;
    private boolean isDestroy = false;
    private ImageView ivTel = null;
    private boolean isClicked = false;

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
    private DetailDialogActivity mCustomPopup;
    private Detail2DialogActivity mCustomPopup2;
    private String adImage;
    private String adBannerType;
    private String adBannerIdx;
    private String adImage2;
    private String adBannerType2;
    private String adBannerIdx2;

    private ImageView imgLock = null;
    private ImageView imgNet = null;

    private boolean menuVisible = false;
    private Button btnMenu0;

    private int maxPageIdx;        //페이지
    private int remainder;
    private int pageIdx;        //현재 페이지

    private ImageView backImage;

    private boolean isAllListLoad;

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

    private TimerTask timerTask;

    public MovieListActivity() {
        broadcastList = new ArrayList<BroadcastList>();
        allList = new ArrayList<BroadcastList>();
        nowCoverFlowIdx = 0;

        numberBtn = new ArrayList<ImageView>();
        pageIdx = 0;
        remainder = 0;
        maxPageIdx = 0;
    }

    ImageView btnMenu;
    Button expireText;

    private void setNumberBtn() {
        for (int i = 0; i < 12; i++) {
            ImageView tempBtn = (ImageView) findViewById(numberBtnName[i]);
            numberBtn.add(tempBtn);

            borders[i] = (ImageView) findViewById(numberBorderName[i]);
        }

    }

    private void setSearchBtn() {
        for (int i = 0; i < 19; i++) {
            final int my = i;
            ivSearchBtns[i] = (ImageView) findViewById(searchBtns[i]);

            ivSearchBtns[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    editSearch.getText().append(searchNames[my]);
                }
            });
        }

        searchBack = (ImageView) findViewById(R.id.btnKeyBack);
        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (editSearch.getText().length() > 0)
                    editSearch.getText().delete(editSearch.getText().length() - 1, editSearch.getText().length());
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
        subid = myBundle.getString("subid");
        ImageLoader.getInstance().init(Util.getConfig(mContext));

        setNumberBtn();
        setSearchBtn();


        btnMenu = (ImageView) findViewById(R.id.menu);
        btnMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (menuVisible) {
                    addFocus();
                    menuVisible = false;
                    //coverFlow.requestFocus();
                    rlMenu.setVisibility(View.GONE);
                    btnMenu.setVisibility(View.VISIBLE);
                } else {
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

        imgLock = (ImageView) findViewById(R.id.imgLock);
        if (!Util.getChildset(mContext))
            imgLock.setBackgroundResource(R.drawable.image_main_lock_off);

        imgNet = (ImageView) findViewById(R.id.imgNet);
        if (!Util.checkNetwordState(mContext))
            imgNet.setBackgroundResource(R.drawable.image_main_net_off);

        linearLayout1 = (RelativeLayout) findViewById(R.id.Layout1);
        buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
        editSearch = (EditText) findViewById(R.id.editSearch);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);
        nosearch = (ImageView) findViewById(R.id.nosearch);
        editSearch.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        txtCategory = (TextView) findViewById(R.id.categorytext);
        pagetext = (TextView) findViewById(R.id.pagetext);
        btnSearch.setOnClickListener(new OnClickListener() {

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
                    } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == 4) {
                        finish();
                        return false;
                    }
                }
                return false;
            }
        });

        AdTask adTask = new AdTask(mContext);
        adTask.execute();


        rlMenu = (RelativeLayout) findViewById(R.id.rlMenu);

        btnMenu0 = (Button) findViewById(R.id.btnMenu0);
        final Button btnMenu1 = (Button) findViewById(R.id.btnMenu1);
        final Button btnMenu2 = (Button) findViewById(R.id.btnMenu2);
        final Button btnMenu3 = (Button) findViewById(R.id.btnMenu3);
        final Button btnMenu4 = (Button) findViewById(R.id.btnMenu4);
        final Button btnMenu5 = (Button) findViewById(R.id.btnMenu5);
        final Button btnMenu6 = (Button) findViewById(R.id.btnMenu6);
        final Button btnMenu7 = (Button) findViewById(R.id.btnMenu7);
        final Button btnMenu8 = (Button) findViewById(R.id.btnMenu8);
        final Button btnMenu9 = (Button) findViewById(R.id.btnMenu9);
        final Button btnMenu10 = (Button) findViewById(R.id.btnMenu10);
        final Button btnMenu11 = (Button) findViewById(R.id.btnMenu11);

        if (subid.equals("update")) {
            if (mainid.equals("broadcast")) {
                txtCategory.setText("다시보기" + " ＞ " + "업데이트" + " ＞ ");
                btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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

                btnMenu1.setText("드라마");
                btnMenu1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subcate = "002";
                        GetTask getTask = new GetTask(mContext);
                        getTask.execute();
                        hideMenu();
                    }
                });
                btnMenu1.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            genreidx = 0;
                            btnMenu1.setTextColor(Color.parseColor("#0060A5"));
                        } else {
                            btnMenu1.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                });
                btnMenu2.setText("오락방송");
                btnMenu2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subcate = "006";
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
                btnMenu3.setText("시사교양");
                btnMenu3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subcate = "116";
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
                btnMenu4.setText("유아교육");
                btnMenu4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subcate = "013";
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
                        subcate = "007";
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
                btnMenu6.setText("방송창고");
                btnMenu6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subcate = "026";
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

                btnMenu7.setVisibility(View.GONE);
                btnMenu8.setVisibility(View.GONE);
                btnMenu9.setVisibility(View.GONE);
                btnMenu10.setVisibility(View.GONE);
                btnMenu11.setVisibility(View.GONE);
            } else {
                txtCategory.setText("영화" + " ＞ " + "업데이트" + " ＞ ");
                btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                            genreidx = 0;
                            btnMenu1.setTextColor(Color.parseColor("#0060A5"));
                        } else {
                            btnMenu1.setTextColor(Color.parseColor("#FFCF01"));
                        }
                    }
                });
//				btnMenu1.setText("인기순");
//				btnMenu1.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						subid = "update";
//						subcate = "favorite";
//						GetTask getTask = new GetTask(mContext);
//						getTask.execute();
//						hideMenu();
//					}
//				});
                btnMenu2.setText("한국영화");
                btnMenu2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "034";
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
                btnMenu3.setText("외국영화");
                btnMenu3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "035";
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
                btnMenu4.setText("성인영화");
                btnMenu4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "036";
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
                btnMenu5.setText("아동영화");
                btnMenu5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "037";
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
                btnMenu6.setText("명작영화");
                btnMenu6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "038";
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

                btnMenu7.setText("날짜순");
                btnMenu7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        subid = "update";
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

        } else if (subid.equals("034")) {
            txtCategory.setText("영화" + " ＞ " + "한국영화" + " ＞ ");
            btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                        genreidx = 0;
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

            btnMenu2.setText("드라마");
            btnMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "049";
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
            btnMenu3.setText("로맨스");
            btnMenu3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "050";
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
            btnMenu4.setText("코메디");
            btnMenu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "051";
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
            btnMenu5.setText("액션");
            btnMenu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "052";
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
            btnMenu6.setText("전쟁");
            btnMenu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "053";
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
            btnMenu7.setText("SF/판타지");
            btnMenu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "054";
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
            btnMenu8.setText("스릴러");
            btnMenu8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "055";
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
            btnMenu9.setText("공포");
            btnMenu9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "056";
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
            btnMenu10.setText("애니메이션");
            btnMenu10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "057";
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
            btnMenu11.setText("다큐");
            btnMenu11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "058";
                    GetTask getTask = new GetTask(mContext);
                    getTask.execute();
                    hideMenu();
                }
            });
            btnMenu11.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        btnMenu11.setTextColor(Color.parseColor("#0060A5"));
                    } else {
                        btnMenu11.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });
        } else if (subid.equals("035")) {
            txtCategory.setText("영화" + " ＞ " + "외국영화" + " ＞ ");
            btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                        genreidx = 0;
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

            btnMenu2.setText("드라마");
            btnMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "059";
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
            btnMenu3.setText("로맨스");
            btnMenu3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "060";
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
            btnMenu4.setText("코메디");
            btnMenu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "061";
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
            btnMenu5.setText("액션");
            btnMenu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "062";
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
            btnMenu6.setText("전쟁");
            btnMenu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "063";
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
            btnMenu7.setText("SF/판타지");
            btnMenu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "064";
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
            btnMenu8.setText("스릴러");
            btnMenu8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "065";
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
            btnMenu9.setText("공포");
            btnMenu9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "066";
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
            btnMenu10.setText("애니메이션");
            btnMenu10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "067";
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
            btnMenu11.setText("다큐");
            btnMenu11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "068";
                    GetTask getTask = new GetTask(mContext);
                    getTask.execute();
                    hideMenu();
                }
            });
            btnMenu11.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        btnMenu11.setTextColor(Color.parseColor("#0060A5"));
                    } else {
                        btnMenu11.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });

        } else if (subid.equals("036")) {
            txtCategory.setText("영화" + " ＞ " + "성인영화" + " ＞ ");

            btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                        genreidx = 0;
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

            btnMenu2.setText("드라마");
            btnMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "069";
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
            btnMenu3.setText("로맨스");
            btnMenu3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "070";
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
            btnMenu4.setText("코메디");
            btnMenu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "071";
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
            btnMenu5.setText("액션");
            btnMenu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "072";
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
            btnMenu6.setText("전쟁");
            btnMenu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "073";
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
            btnMenu7.setText("SF/판타지");
            btnMenu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "074";
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
            btnMenu8.setText("스릴러");
            btnMenu8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "075";
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
            btnMenu9.setText("공포");
            btnMenu9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "076";
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
            btnMenu10.setText("애니메이션");
            btnMenu10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "077";
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
            btnMenu11.setText("다큐");
            btnMenu11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "078";
                    GetTask getTask = new GetTask(mContext);
                    getTask.execute();
                    hideMenu();
                }
            });
            btnMenu11.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        btnMenu11.setTextColor(Color.parseColor("#0060A5"));
                    } else {
                        btnMenu11.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });

        } else if (subid.equals("037")) {
            txtCategory.setText("영화" + " ＞ " + "아동영화" + " ＞ ");
            btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                        genreidx = 0;
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

            btnMenu2.setText("애니/서양");
            btnMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "084";
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
            btnMenu3.setText("애니/동양");
            btnMenu3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "083";
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
            btnMenu4.setText("애니/한국");
            btnMenu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "082";
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
            btnMenu5.setText("외국영화");
            btnMenu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "081";
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
            btnMenu6.setText("동양영화");
            btnMenu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "080";
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
            btnMenu7.setText("한국영화");
            btnMenu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "079";
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
        } else if (subid.equals("038")) {
            txtCategory.setText("영화" + " ＞ " + "명작영화" + " ＞ ");
            btnMenu0.setTextColor(Color.parseColor("#FFCF01"));
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
                        genreidx = 0;
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

            btnMenu2.setText("드라마");
            btnMenu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "085";
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
            btnMenu3.setText("로맨스");
            btnMenu3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "086";
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
            btnMenu4.setText("코메디");
            btnMenu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "087";
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
            btnMenu5.setText("액션");
            btnMenu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "088";
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
            btnMenu6.setText("전쟁");
            btnMenu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "089";
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
            btnMenu7.setText("SF/판타지");
            btnMenu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "090";
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
            btnMenu8.setText("스릴러");
            btnMenu8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "091";
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
            btnMenu9.setText("공포");
            btnMenu9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "092";
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
            btnMenu10.setText("애니메이션");
            btnMenu10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "093";
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
            btnMenu11.setText("다큐");
            btnMenu11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    subcate = "094";
                    GetTask getTask = new GetTask(mContext);
                    getTask.execute();
                    hideMenu();
                }
            });
            btnMenu11.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        btnMenu11.setTextColor(Color.parseColor("#0060A5"));
                    } else {
                        btnMenu11.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });

        }
        //modify
        //searchButton = (Button)findViewById(R.id.searchButton);
        //searchText = (EditText)findViewById(R.id.searchText);
        ImageDownloader imageDownloader = new ImageDownloader();
        ivTel = (ImageView) findViewById(R.id.tel);
        //ivTel.setImageResource(R.drawable.image_main_logo);
        imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView) ivTel);

        btnLeft = (ImageView) findViewById(R.id.pageleft);
        btnRight = (ImageView) findViewById(R.id.pageright);

        btnMenu1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (subid.equals("update")) {
                    if (mainid.equals("broadcast")) {
                        genreid = "";
                    } else {
                        genreid = "";
                    }
                } else if (subid.equals("kor") || subid.equals("for") || subid.equals("ada") || subid.equals("old")) {
                    genreid = "";
                } else if (subid.equals("baby")) {
                    genreid = "";
                }
                //goMovie();
                return false;
            }
        });


        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //nowCoverFlowIdx = coverFlow.getSelectedItemPosition() + 1;
                //if(nowCoverFlowIdx >= broadcastList.size())
                //	nowCoverFlowIdx = broadcastList.size() - 1;
                //coverFlow.setSelection(nowCoverFlowIdx);
                if (pageIdx < maxPageIdx - 1)
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
                if (pageIdx > 0)
                    pageIdx--;
                refreshPage();
                return;
            }
        });
        for (int i = 0; i < numberBtn.size(); i++) {
            numberBtn.get(i).setOnFocusChangeListener(new OnFocusChangeListener() {

                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        for (int j = 0; j < numberBtn.size(); j++)
                            if (v == numberBtn.get(j)) {
                                borders[j].setVisibility(View.VISIBLE);
                                if (mainid.equals("broadcast"))
                                    coverTextView.setText(broadcastList.get(pageIdx * 12 + j).getTitle() + " " + broadcastList.get(pageIdx * 12 + j).getPu_no());
                                else
                                    coverTextView.setText(broadcastList.get(pageIdx * 12 + j).getTitle());

                            }
                    } else {
                        for (int j = 0; j < numberBtn.size(); j++)
                            if (v == numberBtn.get(j))
                                borders[j].setVisibility(View.GONE);

                    }
                }
            });
            numberBtn.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int position = -1;
                    for (int i = 0; i < numberBtn.size(); i++) {
                        if (v.getId() == numberBtn.get(i).getId()) {
                            position = i;
                        }
                    }
                    mPosition = pageIdx * 12 + position;
                    //mPosition = position;
                    if (mainid.equals("broadcast")) {
                        if (position != -1) {
                            /*
							if(!isClicked){
								isClicked = true;
								ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
								viewDetailTask.execute();
								if(mCustomPopup2 != null)
									mCustomPopup2.dismiss();
							}
							*/
                            if (!isClicked) {
                                //isClicked = true;
                                if (mCustomPopup2 != null)
                                    mCustomPopup2.dismiss();
                                mCustomPopup2 = new Detail2DialogActivity(MovieListActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener, mClickAdListener, mKeyAdListener, mClickAd2Listener,"movie", adImage, adImage2);
                                mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                mCustomPopup2.show();
                            }


                        }
						/*if(!isClicked){
							isClicked = true;
							ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
							viewDetailTask.execute();
						}*/
                    } else {

                        MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
                        viewDetailTask.execute();

                    }
                }
            });
        }

        coverTextView = (TextView) findViewById(R.id.coverflowtext);
        expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if (!getExpireDate().equals("")) {
            expireText.setText(getExpireDate().substring(0, 1) + " " + getExpireDate().substring(1, 2));
            expireText2.setText(getExpireDate().substring(2, 3) + " " + getExpireDate().substring(3, 4));
        }

        GetTask getTask = new GetTask(mContext);
        getTask.execute();

        LoadAllDataTask loadAllTask = new LoadAllDataTask(mContext);
        loadAllTask.execute();
        //noSearchToast = Toast.makeText(this, getString(R.string.nosearch), Toast.LENGTH_SHORT);
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
		});
		*/
		/*
		for(int i = 0; i < 5000; i++){
			BroadcastList tempList = new BroadcastList();
			broadcastList.add(tempList);
		}*/

        //if(numberBtn.get(0) != null)
        btnRight.requestFocus();

        setMenuopenAnimation();

        numberBtn.get(0).setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if (pageIdx > 0)
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
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (pageIdx < maxPageIdx - 1)
                            pageIdx++;
                        refreshPage();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    private void clickOkButton() {
        if (!editSearch.getText().toString().trim().equals("")) {

            GetTask2 getTask = new GetTask2(mContext);
            getTask.execute();
        }
    }

    private AnimationListener animationListener2 = null;

    private void setMenuopenAnimation() {
        animationListener2 = new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                btnMenu0.requestFocus();
                coverTextView.setText("");
            }

            public void onAnimationRepeat(Animation animation) {
                btnMenu0.requestFocus();
            }

            public void onAnimationStart(Animation animation) {
            }
        };
    }

    private Dialog.OnKeyListener mKeyAdListener = new Dialog.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (!isClicked) {
                    isClicked = true;
                    goAdActivity(adBannerIdx, adBannerType);
                }
            }else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (!isClicked) {
                    isClicked = true;
                    goAdActivity(adBannerIdx2, adBannerType2);
                }
            }
            return false;
        }
    };

    private View.OnClickListener mClickAdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                goAdActivity(adBannerIdx, adBannerType);

            }
        }
    };


    private View.OnClickListener mClickAd2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                goAdActivity(adBannerIdx2, adBannerType2);

            }
        }
    };

    private void goAdActivity(String idx, String bannerType){
        isClicked = false;
        if(bannerType == null)
            return;
        if(bannerType.equals("S")){
            Intent intent = new Intent(MovieListActivity.this, ShoppingListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("idx", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(bannerType.equals("D")){
            Intent intent = new Intent(MovieListActivity.this, DeliveryListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("idx", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(bannerType.equals("Y")){
            Intent intent = new Intent(MovieListActivity.this, DetailYellowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("idx", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(bannerType.equals("L")){
            Intent intent = new Intent(MovieListActivity.this, LifeListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("idx", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }
    }

    private View.OnKeyListener xPop2PlayKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                if (!isClicked) {
                    isClicked = true;
                    ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
                    viewDetailTask.execute();
                    if (mCustomPopup2 != null)
                        mCustomPopup2.dismiss();
                }
            }
            return false;
        }
    };

    private View.OnClickListener xPop2PlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
                viewDetailTask.execute();
                if (mCustomPopup2 != null)
                    mCustomPopup2.dismiss();
            }
        }
    };

    private View.OnKeyListener xPop2RestoreKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                //if(!isClicked){
                //	isClicked = true;
                if (mCustomPopup2 != null)
                    mCustomPopup2.dismiss();
                WishTask wishTask = new WishTask(mContext);
                wishTask.execute();

                //}
            }
            return false;
        }
    };

    private View.OnClickListener xPop2RestoreClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                if (mCustomPopup2 != null)
                    mCustomPopup2.dismiss();
                WishTask wishTask = new WishTask(mContext);
                wishTask.execute();

            }
        }
    };

    private Handler handler;
    private View.OnKeyListener xPopKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {

                //handler =  new Handler();
                //handler.postDelayed(irun, 200);
            }
            return false;
        }
    };

    Runnable irun = new Runnable() {
        public void run() {
            if (mCustomPopup != null) {
                //	 mCustomPopup.dismiss();
            }
            if (mCustomPopup2 != null) {
                mCustomPopup2.dismiss();
            }
		/*
		if(!isClicked){
			isClicked = true;
			ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
			viewDetailTask.execute();
			if(mCustomPopup2 != null)
				mCustomPopup2.dismiss();
		}
		*/

            mCustomPopup2 = new Detail2DialogActivity(MovieListActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener, mClickAdListener, mKeyAdListener, mClickAd2Listener,"movie", adImage, adImage2);
            mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomPopup2.show();

        }
    };

    private View.OnClickListener xPopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (mCustomPopup != null) {
                //mCustomPopup.dismiss();
            }
            if (mCustomPopup2 != null) {
                mCustomPopup2.dismiss();
            }
		 /*
		 if(!isClicked){
				isClicked = true;
				ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
				viewDetailTask.execute();
				if(mCustomPopup2 != null)
					mCustomPopup2.dismiss();
			}
		 */
            mCustomPopup2 = new Detail2DialogActivity(MovieListActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener,mClickAdListener, mKeyAdListener, mClickAd2Listener, "movie", adImage, adImage2);
            mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomPopup2.show();

        }
    };


    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        isDestroy = true;
        // TODO Auto-generated method stub
        if (viewNextTask != null) {
            viewNextTask.cancel(true);
            viewNextTask = null;
        }
        if (viewTextTask != null) {
            viewTextTask.cancel(true);
            viewTextTask = null;
        }
        if (mCustomPopup != null) {
            mCustomPopup.dismiss();
            mCustomPopup = null;
        }
        if (mCustomPopup2 != null) {
            mCustomPopup2.dismiss();
            mCustomPopup2 = null;
        }
        recycleAllBitmap();
        //if(broadcastList != null)
        //broadcastList = null;
        super.onDestroy();

    }

    private String getExpireDate() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("expiredate", "");
    }

    private boolean getAllDatas() {
        int startIdx = 0;
        int endIdx = 29999;

        allList = new ArrayList<BroadcastList>();
        String strJson = "";
        PostHttp postmake = new PostHttp();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (subid.equals("update")) {
            nameValuePairs.add(new BasicNameValuePair("update", "U"));
            if (!subcate.equals(""))
                nameValuePairs.add(new BasicNameValuePair("code2", subcate));
        } else {
            nameValuePairs.add(new BasicNameValuePair("code2", subid));
            nameValuePairs.add(new BasicNameValuePair("code3", subcate));
        }
        nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
        nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
        nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

        if (mainid.equals("broadcast"))
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/broadcastlist.php", nameValuePairs);
        else
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/movielist.php", nameValuePairs);
        JSONArray jArray = null;
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("idx"));
                tempList.setVod_code(json_data.getString("vod_code"));
                tempList.setImage(json_data.getString("image"));
                tempList.setTitle(json_data.getString("title"));
                tempList.setPu_no(json_data.getString("pu_no"));
                allList.add(i, tempList);
            }
        } catch (JSONException e) {
            Log.e(null, strJson);
        }
        if (jArray != null) {
            if (jArray.length() <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean getImages() {
        int startIdx = 0;
        int endIdx = 1999;

        String strJson = "";
        PostHttp postmake = new PostHttp();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (subid.equals("update")) {
            nameValuePairs.add(new BasicNameValuePair("update", "U"));
            if (!subcate.equals(""))
                nameValuePairs.add(new BasicNameValuePair("code2", subcate));
        } else {
            nameValuePairs.add(new BasicNameValuePair("code2", subid));
            nameValuePairs.add(new BasicNameValuePair("code3", subcate));
        }
        nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
        nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
        nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

        if (mainid.equals("broadcast"))
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/broadcastlist.php", nameValuePairs);
        else
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/movielist.php", nameValuePairs);
        JSONArray jArray = null;
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("idx"));
                tempList.setVod_code(json_data.getString("vod_code"));
                tempList.setImage(json_data.getString("image"));
                tempList.setTitle(json_data.getString("title"));
                tempList.setPu_no(json_data.getString("pu_no"));
                broadcastList.add(i, tempList);
            }
        } catch (JSONException e) {
            Log.e(null, strJson);
        }
        if (jArray != null) {
            if (jArray.length() <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean getImages2() {
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
        if (mainid.equals("broadcast"))
            nameValuePairs.add(new BasicNameValuePair("code1", "104"));
        else
            nameValuePairs.add(new BasicNameValuePair("code1", "105"));


        if (subid.equals("update")) {
            nameValuePairs.add(new BasicNameValuePair("update", "U"));
            if (!subcate.equals(""))
                nameValuePairs.add(new BasicNameValuePair("code2", subcate));
        } else {
            nameValuePairs.add(new BasicNameValuePair("code2", subid));
            nameValuePairs.add(new BasicNameValuePair("code3", subcate));
        }

        strJson = postmake.httpConnect(
                Constant.mainUrl + "/module/tv/search.php", nameValuePairs);
        JSONArray jArray = null;
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("idx"));
                tempList.setType(json_data.getString("vod_type"));
                tempList.setImage(json_data.getString("image"));
                tempList.setTitle(json_data.getString("title"));
                tempList.setCode2(json_data.getString("code2"));

                tempList.setPu_no("");
                //list.add(tempList);
                broadcastList.add(i, tempList);
            }

        } catch (JSONException e) {
            Log.e(null, e.toString());
            return false;
        }
        if (jArray.length() <= 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean getFavorite() {
        int startIdx = 0;
        int endIdx = 1999;

        broadcastList = new ArrayList<BroadcastList>();

        //ranking.php?type={유형}&top={순위}&code2={2차분류}&code3={3차분류}

        String strJson = "";
        PostHttp postmake = new PostHttp();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("top", String.valueOf(endIdx)));
        nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

        if (mainid.equals("broadcast"))
            nameValuePairs.add(new BasicNameValuePair("type", "broadcast"));
        else
            nameValuePairs.add(new BasicNameValuePair("type", "movie"));

        if (subid.equals("update")) {
            nameValuePairs.add(new BasicNameValuePair("update", "U"));
            if (!subcate.equals(""))
                nameValuePairs.add(new BasicNameValuePair("code2", subcate));
        } else {
            nameValuePairs.add(new BasicNameValuePair("code2", subid));
            //nameValuePairs.add(new BasicNameValuePair("code3", subcate));
        }


        strJson = postmake.httpConnect(
                Constant.mainUrl + "/module/tv/ranking.php", nameValuePairs);
        JSONArray jArray = null;
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("code"));
                //tempList.setType(json_data.getString("vod_type"));
                tempList.setImage(json_data.getString("image"));
                tempList.setTitle(json_data.getString("title"));
                if (json_data.getString("title").equals("")) {
                    break;
                }
                //tempList.setCode2(json_data.getString("code2"));
                //tempList.setPu_no(json_data.getString("pu_no"));
                //list.add(tempList);
                broadcastList.add(i, tempList);
            }

        } catch (JSONException e) {
            Log.e(null, e.toString());
            return false;
        }
        if (jArray.length() <= 0) {
            return false;
        } else {
            return true;
        }
    }


    private Bitmap[] bitmap = new Bitmap[12];

    private void refreshPage() {
        if (broadcastList != null) {
            maxPageIdx = broadcastList.size() / 12;
            remainder = broadcastList.size() % 12;
        }
        if (remainder > 0)
            maxPageIdx++;
        pagetext.setText("" + (pageIdx + 1) + "/" + maxPageIdx);
        for (int i = 0; i < 12; i++) {
            if (bitmap[i] != null) {
                bitmap[i].recycle();
                bitmap[i] = null;
            }
        }

        UrlImageLoader urlImageload = new UrlImageLoader();
        int numberidx = pageIdx * 12;
        for (int i = 0; i < 12; i++) {
            if (numberBtn != null) {

                if (numberidx < broadcastList.size()) {
                    //numberBtn.get(i).setText(broadcastList.get(numberidx).getTitle());
                    //bitmap[i] = urlImageload.getUrlImage(broadcastList.get(numberidx).getImage());
                    //numberBtn.get(i).setImageBitmap(bitmap[i]);

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(broadcastList.get(numberidx).getImage(), numberBtn.get(i), Util.getImageLoaderOption(), new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            //super.onLoadingComplete(imageUri, view, loadedImage);
                            if (Util.getWidth(mContext) != 0) {
                                try {
                                    float height = loadedImage.getHeight();
                                    float width = loadedImage.getWidth();
                                    loadedImage.setDensity(Bitmap.DENSITY_NONE);
                                    loadedImage = Bitmap.createScaledBitmap(loadedImage, loadedImage.getWidth(), loadedImage.getHeight(), true);
                                    ((ImageView) view).setImageBitmap(loadedImage);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                }
                numberidx++;
                numberBtn.get(i).setVisibility(View.VISIBLE);
                if (numberidx > broadcastList.size())
                    numberBtn.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }


	/*private void setCoverflowData(){
	coverFlow = (CoverFlow) findViewById(R.id.coverflow);
	imageAdapter = new ImageAdapter(this);
	coverFlow.setAdapter(imageAdapter);


	coverFlow.setSpacing(-5);

	coverFlow.setAnimationDuration(500);

	viewNextTask = new ViewNextTask(mContext, broadcastList, coverFlow.getSelectedItemPosition()-11,
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
	    	 viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-11,
	    			 (int)arg3+11);
	 			viewNextTask.execute();
				if(mainid.equals("broadcast"))
					viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int)arg3).getTitle() + " " + broadcastList.get((int)arg3).getPu_no());
				else
					viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int)arg3).getTitle());

	    	 viewTextTask.execute();
	    	 nowCoverFlowIdx = (int)arg3;
	    	 //coverTextView.setText(broadcastList.get((int)arg3).getTitle());
	    	 //if(arg3%10 == 0){
	    		// viewNextTask = new ViewNextTask(mContext, broadcastList, (int)arg3-10, (int)arg3+10);
	    		// viewNextTask.execute();
	    	 //}
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
			mPosition = position;
			if(mainid.equals("broadcast")){
				if(position != -1){
					 mCustomPopup2 = new Detail2DialogActivity(MovieListActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener, "movie");
					 mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
					 mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
					 mCustomPopup2.show();
				}
				/*if(!isClicked){
					isClicked = true;
					ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
					viewDetailTask.execute();
				}*/
			/*}else{

				MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
				viewDetailTask.execute();

			}

		}
	});
	}*/

    int mPosition;

    private void deleteFocus() {
        for (int i = 0; i < numberBtn.size(); i++) {
            numberBtn.get(i).setFocusable(false);
        }
        btnLeft.setFocusable(false);
        btnRight.setFocusable(false);
    }

    private void addFocus() {
        for (int i = 0; i < numberBtn.size(); i++) {
            numberBtn.get(i).setFocusable(true);
        }
        btnLeft.setFocusable(true);
        btnRight.setFocusable(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (!menuVisible) {
                nowCoverFlowIdx += 9;
                if (nowCoverFlowIdx >= broadcastList.size())
                    nowCoverFlowIdx = broadcastList.size() - 1;
                //coverFlow.setSelection(nowCoverFlowIdx);
            }
        } else if (keyCode == 41 || keyCode == 88) {
            if (pageIdx > 0)
                pageIdx--;
            refreshPage();
        } else if (keyCode == 30 || keyCode == 87) {
            if (pageIdx < maxPageIdx - 1)
                pageIdx++;
            refreshPage();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (!menuVisible) {
                nowCoverFlowIdx -= 9;
                if (nowCoverFlowIdx < 0)
                    nowCoverFlowIdx = 0;
                //coverFlow.setSelection(nowCoverFlowIdx);
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU || keyCode == 29) {
            if (menuVisible) {
                addFocus();
                menuVisible = false;
                //coverFlow.requestFocus();
                rlMenu.setVisibility(View.GONE);
                btnMenu.setVisibility(View.VISIBLE);
            } else {
                deleteFocus();
                rlMenu.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.listmenu_sliding_open);
                animation.setAnimationListener(animationListener2);
                rlMenu.startAnimation(animation);
                btnMenu.setVisibility(View.GONE);
                menuVisible = true;
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            clickOkButton();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeKeyboad() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (editSearch != null)
            inputMethodManager.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

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

    class ViewNextTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;
        private int startIdx;
        private int endIdx;
        private Context mContext;

        public ViewNextTask(Context context, ArrayList<BroadcastList> list, int start, int end) {
            if (start < 0)
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
            if (startIdx < 0)
                startIdx = 0;
            if (endIdx > broadcastList.size())
                endIdx = broadcastList.size();
            for (int i = startIdx; i < endIdx; i++) {
                if (broadcastList.get(i).getBitmap() == null)
                    broadcastList.get(i).setBitmap(urlImageload.getUrlImage(list.get(i).getImage()));
                if (i % 10 == 0)
                    publishProgress(1);
            }
            if (startIdx / 100 > 1)
                for (int i = 0; i < startIdx - (startIdx % 100) - 10; i++) {
                    if (broadcastList.get(i).getBitmap() != null) {
                        broadcastList.get(i).recyleBitmap();
                    }
                }
            if (endIdx / 100 < broadcastList.size() / 100 - 1) {
                for (int i = endIdx + 10; i < broadcastList.size(); i++) {
                    if (broadcastList.get(i).getBitmap() != null) {
                        broadcastList.get(i).recyleBitmap();
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

    class ViewTextTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;
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
            try {
                Thread.sleep(700);
            } catch (Exception e) {
            }
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

    private String detailP_code;
    private String detailVod_type;
    private String detailVod_code;

    class MovieDetailTask extends AsyncTask<String, Integer, Long> {
        private Context mContext;
        private int position;
        //private String detailTitle;

        private String result_code;
        private String vod_url;
        private String genre;
        private String point;
        private String age;
        private String date;
        private String director;
        private String cast;
        private String story;

        public MovieDetailTask(Context context, int position) {
            mContext = context;
            this.position = position;
            nowPositon = position;
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

            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
            if (broadcastList.size() > 0)
                nameValuePairs.add(new BasicNameValuePair("idx", broadcastList.get(position).getIdx()));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
            Log.e(null, strJson);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(strJson);
                JSONObject json_data = jArray.getJSONObject(0);
                //detailTitle = json_data.getString("title");
                detailP_code = json_data.getString("p_code");
                detailVod_type = json_data.getString("vod_type");
                detailVod_code = json_data.getString("vod_code");
                genre = json_data.getString("genre ");
                point = json_data.getString("p_star");
                age = json_data.getString("p_age");
                date = json_data.getString("p_svc_date");
                director = json_data.getString("p_director");
                cast = json_data.getString("p_acter");
                story = json_data.getString("p_stroy");
            } catch (JSONException e) {
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            //if(mCustomPopup != null)
            //mCustomPopup.dismiss();
            if (broadcastList.size() > 0) {
                mCustomPopup = new DetailDialogActivity(MovieListActivity.this, xPopClickListener, xPopKeyListener, broadcastList.get(position).getTitle(),
                        broadcastList.get(position).getImage(), genre, point,
                        age, date, director,
                        cast, story);


                mCustomPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mCustomPopup.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mCustomPopup.show();
                mCustomPopup.setCancelable(true);
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

    class ViewDetailTask extends AsyncTask<String, Integer, Long> {
        private String detailUrl;
        private String playUrl;
        private Context mContext;
        private int position;
        //private String detailTitle;

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

            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
            if (mainid.equals("broadcast")) {
                nameValuePairs.add(new BasicNameValuePair("p_code", broadcastList.get(position).getIdx()));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "D"));
                nameValuePairs.add(new BasicNameValuePair("vod_code", broadcastList.get(position).getVod_code()));
            } else {
                nameValuePairs.add(new BasicNameValuePair("p_code", detailP_code));
                nameValuePairs.add(new BasicNameValuePair("vod_type", detailVod_type));
                nameValuePairs.add(new BasicNameValuePair("vod_code", detailVod_code));
            }
            if (!Util.getChildset(mContext))
                nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));
            if (broadcastList.size() > 0)
                nameValuePairs.add(new BasicNameValuePair("idx", broadcastList.get(position).getIdx()));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                //detailTitle = json_data.getString("title");
                result_code = json_data.getString("result");
                vod_url = json_data.getString("vod_url");
                Log.e(null, result_code);
            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            Log.e(null, "url" + vod_url);
            checkPlay(result_code, vod_url, broadcastList.get(position).getIdx());
            isClicked = false;
            //if(mCustomPopup != null)
            //mCustomPopup.dismiss();
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

    private String getChildNum() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("childnum", "0000");
    }

    private void hideMenu() {
        linearLayout1.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.VISIBLE);
        menuVisible = false;
        btnRight.requestFocus();
        //if(numberBtn.get(0) != null)
        //numberBtn.get(0).requestFocus();
        //coverFlow.requestFocus();
        rlMenu.setVisibility(View.GONE);
        btnMenu.setVisibility(View.VISIBLE);
    }

    private void checkPlay(String result, String vod_url, String idx) {
        if (Constant.isTest) {
            goLivePlay(vod_url, idx);
            return;
        }
        Log.e(null, "go" + vod_url);
        if (result == null)
            return;
        if (getAuth().equals("ok")) {
            if (result.equals("OK")) {
                goLivePlay(vod_url, idx);
            } else if (result.equals("CANCEL")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage(R.string.videocancel);
                alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            } else if (result.equals("AGE_UNDER")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage(R.string.ageunder);
                alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            } else if (result.equals("CHILD_SAFE")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage(R.string.safechild);
                alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            } else if (result.equals("MEMBER_M")) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
                alt_bld.setMessage(R.string.memberm);
                alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            } else if (result.equals("POINT_UNDERSUPPLY")) {
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
        } else {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(MovieListActivity.this);
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

    private void goLivePlay(String strUrl, String idx) {
        //recycleAllBitmap();
        Intent intent = new Intent(MovieListActivity.this, VideoViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        myData.putString("idx", idx);
        myData.putString("videourl", strUrl);
        myData.putString("mainid", mainid);
        myData.putString("subid", subid);
        intent.putExtras(myData);
        startActivity(intent);
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


    //맥어드레스 가져오기
    private String getMacaddress() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("macaddress", "");
    }

    private String getAuth() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("auth", "");
    }

    private void recycleAllBitmap() {
        for (int i = 0; i < broadcastList.size(); i++)
            broadcastList.get(i).recyleBitmap();
    }

    private ProgressDialog mProgress;

    class GetTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;


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
            mProgress = new ProgressDialog(MovieListActivity.this);
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
            pageIdx = 0;
            remainder = 0;
            maxPageIdx = 0;
            //setCoverflowData();
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
        private ArrayList<BroadcastList> list = null;


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
            for (int i = 0; i < allList.size(); i++) {
                if (SoundSearcher.matchString(allList.get(i).getTitle().toLowerCase().replaceAll(" ", ""), editSearch.getText().toString().trim().toLowerCase())) {
                    broadcastList.add(allList.get(i));
                    result = true;
                }
            }
            if (result)
                return 0L;
            else
                return 1L;
        }


        protected void showProgress() {
            mProgress = new ProgressDialog(MovieListActivity.this);
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
            isClicked = false;
            if (result == 0L) {
                nosearch.setVisibility(View.GONE);
                dismissProgress();
                //setCoverflowData();
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
            } else {
                dismissProgress();
                //nosearch.setVisibility(View.VISIBLE);
                //setCoverflowData();
                Toast.makeText(mContext, mContext.getString(R.string.nosearch), Toast.LENGTH_SHORT).show();
                pageIdx = 0;
                remainder = 0;
                maxPageIdx = 0;
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

    class WishTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;

        public WishTask(Context context) {
            mContext = context;
        }

        @Override
        protected final void onPreExecute() {
            showProgress();
        }

        @Override
        protected Long doInBackground(String... params) {
            // TODO Auto-generated method stub
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
            nameValuePairs.add(new BasicNameValuePair("ptype", "write"));
            nameValuePairs.add(new BasicNameValuePair("p_code", broadcastList.get(nowPositon).getIdx()));
            nameValuePairs.add(new BasicNameValuePair("pu_idx", broadcastList.get(nowPositon).getVod_code()));

            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/wishlist_proc.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                //detailTitle = json_data.getString("title");
                //result_code = json_data.getString("result");
                //vod_url = json_data.getString("vod_url");
            } catch (JSONException e) {
                Log.e(null, e.toString());
                isClicked = false;
            }
            return 0L;

        }


        protected void showProgress() {
            mProgress = new ProgressDialog(MovieListActivity.this);
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    WishTask.this.cancel(true);
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
            isClicked = false;
            dismissProgress();
            // TODO Auto-generated method stub
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(MovieListActivity.this);
            alt_bld.setMessage(R.string.savebox);
            alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();

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
        private ArrayList<BroadcastList> list = null;


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
            if (result)
                return 0L;
            else
                return 1L;
        }


        protected void showProgress() {
            mProgress = new ProgressDialog(MovieListActivity.this);
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
            if (result == 0L) {
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
                //setCoverflowData();
                addFocus();

            } else {
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

    private int savedRandom;
    class AdTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;
        private int position;

        public AdTask(Context context) {
            mContext = context;
        }

        @Override
        protected final void onPreExecute() {
        }

        @Override
        protected Long doInBackground(String... params) {
            // TODO Auto-generated method stub
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
            nameValuePairs.add(new BasicNameValuePair("type", "V"));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(strJson);
                int random = (int)(Math.random() * jArray.length());
                savedRandom = random;
                JSONObject json_data = jArray.getJSONObject(random);
                adImage = json_data.getString("image");
                adBannerType = json_data.getString("banner_type");
                adBannerIdx = json_data.getString("banner_idx");
            } catch (JSONException e) {
                isClicked = false;
                Log.e(null, e.toString());
            }
            return 0L;
        }

        protected void showCancelMessage() {
            //Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
        }

        protected void showError(Context context, Throwable t) {
            //TODO exception
            String errorMessage = context.getString(R.string.str_network_error);
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            AdTask2 adTask2 = new AdTask2(mContext);
            adTask2.execute();
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

    class AdTask2 extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;
        private int position;

        public AdTask2(Context context) {
            mContext = context;
        }

        @Override
        protected final void onPreExecute() {
        }

        @Override
        protected Long doInBackground(String... params) {
            // TODO Auto-generated method stub
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
            nameValuePairs.add(new BasicNameValuePair("type", "V"));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(strJson);
                int random = (int)(Math.random() * jArray.length());
                while(random == savedRandom) {
                    random = (int) (Math.random() * jArray.length());
                }
                JSONObject json_data = jArray.getJSONObject(random);
                adImage2 = json_data.getString("image");
                adBannerType2 = json_data.getString("banner_type");
                adBannerIdx2 = json_data.getString("banner_idx");
            } catch (JSONException e) {
                isClicked = false;
                Log.e(null, e.toString());
            }
            return 0L;
        }

        protected void showCancelMessage() {
            //Toast.makeText(context, mTaskCancelledMessage, Toast.LENGTH_SHORT).show();
        }

        protected void showError(Context context, Throwable t) {
            //TODO exception
            String errorMessage = context.getString(R.string.str_network_error);
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
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

