package com.new1.settop;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.new1.listlive.ListMenuLiveAdapter;
import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.model.DramaContent;
import com.new1.model.ShortChannelDateList;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;

public class VideoViewActivity extends Activity implements OnClickListener {
    private ProgressDialog dlg = null;
    private FullVideoView vv = null;
    private String url = null;
    private AlertDialog aDlg = null;
    //이어보기 추가
    private AlertDialog aContinueDlg = null;
    private ViewBannerTask viewBannerTask = null;
    private ImageView bannerView = null;
    private String mainid = null;
    protected String subid = null;        //현재 카테고리
    private Context mContext = null;
    private boolean isPlaying = false;
    private String idx = null;
    private boolean isClicked = false;
    LinearLayout llMenu;
    RelativeLayout slmenu;
    boolean menuVisible = false;
    private boolean first = true;
    private ArrayList<BroadcastList> liveList = null;
    private ArrayList<DramaContent> dramaList = null;

    private boolean menuShowable = false;

    public static final int MENU_ADD = Menu.FIRST;
    public static final int MENU_DELETE = Menu.FIRST + 1;

    private ArrayList<ShortChannelDateList> shortChannelDateList;
    //이어 보기
    private String saveIdx;
    private ArrayList<Button> broadcastBtnList = new ArrayList<Button>();

    private ListView listView;
    private ListMenuLiveAdapter liveListAdapter;

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        for (int i = 0; i < liveList.size(); i++) {
            if (arg0 == liveList.get(i).getBtns()) {
                if (!isClicked) {
                    //saveFastIdx(String.valueOf(i));
                    if (vv != null) {
                        Log.e(null, "ididididid" + String.valueOf(vv.getCurrentPosition()));
                        saveDate(url, String.valueOf(vv.getCurrentPosition()));
                    }
                    if (vv != null) {
                        if (vv.isPlaying()) {
                            vv.stopPlayback();
                            vv = null;
                        }
                        vv = null;
                    }
                    isClicked = true;
                    //Log.e(null, "position=" + i);
                    ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, i);
                    viewDetailTask.execute();
                }
            }
        }
        for (int i = 0; i < dramaList.size(); i++) {
            if (arg0 == broadcastBtnList.get(i)) {
                if (!isClicked) {
                    //saveFastIdx(String.valueOf(i));
                    if (vv != null) {
                        Log.e(null, "ididididid" + String.valueOf(vv.getCurrentPosition()));
                        saveDate(url, String.valueOf(vv.getCurrentPosition()));
                    }
                    if (vv != null) {
                        if (vv.isPlaying()) {
                            vv.stopPlayback();
                            vv = null;
                        }
                        vv = null;
                    }
                    isClicked = true;
                    //Log.e(null, "position=" + i);
                    ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, i);
                    viewDetailTask.execute();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.videopage);

        liveList = new ArrayList<BroadcastList>();

        mContext = this.getBaseContext();
        isPlaying = false;
        bannerView = (ImageView) findViewById(R.id.imageview);

        try {
            Intent intent = getIntent();
            Bundle myBundle = intent.getExtras();
            idx = myBundle.getString("idx");
            mainid = myBundle.getString("mainid");
            subid = myBundle.getString("subid");
            url = myBundle.getString("videourl");

            saveIdx = getSavedDate(url);

            dramaList = getIntent().getParcelableArrayListExtra("dramadata");

            if (dlg == null) {
                dlg = ProgressDialog.show(VideoViewActivity.this, "", getString(R.string.wait), true);
                dlg.setCancelable(true);
                dlg.show();
            }
            //이어 보기
            if (mainid.equals("broadcast") && !subid.equals("update")) {
                setBroadcastMenu();
            } else {
                GetTask getTask = new GetTask(mContext);
                getTask.execute();
            }
            GetShortTask getshortTask = new GetShortTask(mContext);
            getshortTask.execute();

            if (!Constant.isTest) {
                // ViewBannerTask viewBannerTask = new ViewBannerTask(mContext);
                //viewBannerTask.execute();
            }
        } catch (Exception e) {
            //finish();
        }

        try {
            vv = (FullVideoView) findViewById(R.id.videoview);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(vv);
            vv.setVideoURI(Uri.parse(url));
            vv.setMediaController(mc);

            vv.requestFocus();

        } catch (Exception e) {
            //finish();
        }

        //이어보기 추가
        aContinueDlg = new AlertDialog.Builder(this)
                .setMessage(R.string.continuevideo)
                .setCancelable(true)
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        vv.start();
                    }
                })
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (saveIdx != null)
                            vv.seekTo(Integer.valueOf(saveIdx));
                        vv.start();
                    }
                })
                .create(); // 마지막 create()에서 다 만든 AlertDialog가 returne된다
        aContinueDlg.setCancelable(false);

        aDlg = new AlertDialog.Builder(this)
                .setMessage(R.string.novideo)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .create(); // 마지막 create()에서 다 만든 AlertDialog가 returne된다

        dlg.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (vv != null) {
                    vv.stopPlayback();
                    vv = null;
                }
                //finish();
            }
        });


        vv.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                aDlg.show();
                //finish();
                return true;
            }
        });

        vv.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                isPlaying = true;
                if (dlg != null && dlg.isShowing()) {
                    dlg.dismiss();
                    dlg = null;
                }
                mp.setOnVideoSizeChangedListener(onVideoSizeChangedListener);  
               /*
                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
  			ViewGroup.LayoutParams.FILL_PARENT);  
  			  vv.setLayoutParams(lp);  
               */
            }
        });


        vv.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                saveDate(url, String.valueOf(0));
                finish();
            }
        });

        //이어보기 추가
        if (vv != null) {
            if (!mainid.equals("live")) {
                if (Integer.valueOf(saveIdx) != 0) {
                    aContinueDlg.show();
                } else {
                    vv.start();
                }
            } else {
                vv.start();
            }
        }

    }


    private OnVideoSizeChangedListener onVideoSizeChangedListener =
            new OnVideoSizeChangedListener() {
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    Log.e(null, "width" + width);
                    Log.e(null, "height" + height);
                    if (mainid.equals("movie"))
                        vv.changeSize(width, height);
                }
            };

    private OnPreparedListener onPrepared = new OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {

        }
    };

    //이어보기추가
    public String getSavedDate(String idx) {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString(idx, "0");
    }

    //이어보기추가
    public void saveDate(String idx, String seekTime) {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(idx, seekTime);
        editor.commit();
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


    void setMenu() {
//        listView = (ListView) findViewById(R.id.menuListView);
//        liveListAdapter = new ListMenuLiveAdapter(VideoViewActivity.this,
//                liveList);
//
//        liveListAdapter.notifyDataSetChanged();
//        listView.setVisibility(View.VISIBLE);
//        listView.setAdapter(liveListAdapter);
//        //listView.setFocusable(false);
//        //listView.setOnItemClickListener(this);
//
//        //listView.setOnScrollListener(this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, position);
//                viewDetailTask.execute();
//            }
//        });
//
//        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//
//        listView.setItemsCanFocus(true);
//        listView.setSelected(true);
//        listView.setSelection(0);
//        listView.setItemChecked(0, true);
//
//        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                LinearLayout ll1 = (LinearLayout) view.findViewById(R.id.ll1);
//                LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.ll2);
//                ll1.setVisibility(View.VISIBLE);
//                ll2.setVisibility(View.VISIBLE);
//                liveList.get(position).setFocused(true);
//                Log.d("dddd", "Ddddddd");
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                //ll1.setVisibility(View.GONE);
//               /// ll2.setVisibility(View.GONE);
//            }
//        });

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
            TextView txtNowBroad = (TextView) layout.findViewById(R.id.txtNowBroad);
            txtNowBroad.setText(liveList.get(i).getNowTitle().trim());
            TextView txtNowChannel = (TextView) layout.findViewById(R.id.txtNowChannel);
            txtNowChannel.setText(liveList.get(i).getTitle().trim());
            TextView txtNowChannel2 = (TextView) layout.findViewById(R.id.txtNowChannel2);
            txtNowChannel2.setText(liveList.get(i).getTitle().trim());
            TextView txtNextTime = (TextView) layout.findViewById(R.id.txtNextTime);
            //String preNextTime = "";
            //if(liveList.get(i).getNextTime().length() > 2)
            //preNextTime = liveList.get(i).getNextTime().substring(2).trim();
            //if(preNextTime.length() > 1)
            txtNextTime.setText(liveList.get(i).getNextTime() + "~");
            TextView txtNextBroad = (TextView) layout.findViewById(R.id.txtNextBroad);
            txtNextBroad.setText(liveList.get(i).getNextTitle().trim());
            LinearLayout ll1 = (LinearLayout) layout.findViewById(R.id.ll1);
            LinearLayout ll2 = (LinearLayout) layout.findViewById(R.id.ll2);


            ll1.setVisibility(View.GONE);

            final int k = i;
            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vv != null) {
                        if (vv.isPlaying()) {
                            vv.stopPlayback();
                            vv = null;
                        }
                        vv = null;
                    }
                    ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, k);
                    viewDetailTask.execute();
                }
            });
            layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        //for(int k =0; k < ((ViewGroup)v).getChildCount(); k++){
                        ((ViewGroup) v).getChildAt(0).setVisibility(View.VISIBLE);
                        ((ViewGroup) v).getChildAt(1).setVisibility(View.GONE);
                        //}
                    } else {
                        //for(int k =0; k < ((ViewGroup)v).getChildCount(); k++){
                        ((ViewGroup) v).getChildAt(0).setVisibility(View.GONE);
                        ((ViewGroup) v).getChildAt(1).setVisibility(View.VISIBLE);
                        //}
                    }
                }
            });
            llMenu.addView(layout);
        }
    }


    void setBroadcastMenu() {
        llMenu = (LinearLayout) findViewById(R.id.llMenu);
        slmenu = (RelativeLayout) findViewById(R.id.slmenu);
        for (int i = 0; i < dramaList.size(); i++) {
            Button button = new Button(this);
            button.setBackgroundResource(R.drawable.live_select_button);
            button.setText(dramaList.get(i).getTitle());
            Log.d("", "5555555555" + dramaList.get(i).getTitle());
            button.setTextColor(Color.WHITE);
            button.setTextSize(23);
            button.setOnClickListener(this);
            //button.setFocusable(true);
            //button.requestFocus();

            broadcastBtnList.add(button);
            //dramaList.get(i).setBtns(button);
            //button01.setLayoutParams(params);
            llMenu.addView(button);
        }
        menuShowable = true;
    }


    @Override
    protected void onDestroy() {

        if (dlg != null) {
            dlg.dismiss();
            dlg = null;
        }
        if (aDlg != null) {
            aDlg.dismiss();
            aDlg = null;
        }
        if (vv != null) {
            if (vv.isPlaying()) {
                vv.stopPlayback();
                vv = null;
            }
            vv = null;
        }
        if (viewBannerTask != null) {
            viewBannerTask.cancel(true);
            viewBannerTask = null;
        }

        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void getImages() {
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
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            //Log.e(null, strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("idx"));
                tempList.setVod_code(json_data.getString("vod_code"));
                tempList.setImage(json_data.getString("image"));
                tempList.setSubid("live");
                tempList.setTitle(json_data.getString("title"));
                tempList.setPu_no(json_data.getString("pu_no"));

                tempList.setNowTitle(json_data.getString("now_title"));
                tempList.setNowTime(json_data.getString("now_time"));
                tempList.setNextTitle(json_data.getString("next_title"));
                tempList.setNextTime(json_data.getString("next_time"));
                //list.add(tempList);
                liveList.add(tempList);
            }
        } catch (JSONException e) {
            Log.e(null, e.toString());
        }
        //}
    }

    private String getMacaddress() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("macaddress", "");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        isPlaying = true;
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            //finish();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            //finish();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {

        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*if(vv != null){
				if(vv != null){
					vv.stopPlayback();
					vv = null;
				}
				vv = null;
			}*/
            //이어보기 추가
            if (vv != null) {
                Log.e(null, "ididididid" + String.valueOf(vv.getCurrentPosition()));
                saveDate(url, String.valueOf(vv.getCurrentPosition()));
            }
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (mainid.equals("live")) {
                if (vv != null) {
                    if (vv.isPlaying()) {
                        vv.stopPlayback();
                        vv = null;
                    }
                    vv = null;
                }
                int nextIdx = Integer.valueOf(getSavedFastIdx());
                if (nextIdx < liveList.size() - 1)
                    nextIdx++;
                else
                    nextIdx = 0;
                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, nextIdx);
                viewDetailTask.execute();
            }
            //return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            //return true;
            if (mainid.equals("live")) {
                if (vv != null) {
                    if (vv.isPlaying()) {
                        vv.stopPlayback();
                        vv = null;
                    }
                    vv = null;
                }
                int nextIdx = Integer.valueOf(getSavedFastIdx());
                if (nextIdx > 0)
                    nextIdx--;
                else
                    nextIdx = liveList.size() - 1;
                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, nextIdx);
                viewDetailTask.execute();
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            //이어보기 추가
            //if(mainid.equals("live")){
            if (menuShowable) {
                if (menuVisible) {
                    slmenu.setVisibility(View.GONE);
                    menuVisible = false;
                } else {
                    /*if (liveList.size() > 0) {
                        first = false;
                        //liveList.get(0).getBtns().setFocusable(true);
                        //getSavedFastIdx();
                        if (Integer.valueOf(getSavedFastIdx()) < liveList.size()) {
                            liveList.get(Integer.valueOf(getSavedFastIdx())).getBtns().requestFocus();
                        } else {
                            liveList.get(Integer.valueOf(0)).getBtns().requestFocus();
                        }
                    }*/
                    if (mainid.equals("broadcast") && !subid.equals("update")) {
                        if (dramaList.size() > 0) {
                            first = false;
                            for (int i = 0; i < broadcastBtnList.size(); i++) {
                                broadcastBtnList.get(i).setFocusable(true);
                            }
                            //getSavedFastIdx();
                            if (Integer.valueOf(getSavedFastIdx()) < dramaList.size()) {
                                broadcastBtnList.get(Integer.valueOf(getSavedFastIdx())).requestFocus();
                            } else {
                                broadcastBtnList.get(Integer.valueOf(0)).requestFocus();
                            }
                        }
                        slmenu.setVisibility(View.VISIBLE);
                    } else if (!mainid.equals("broadcast") && !mainid.equals("movie")) {
                        if (Integer.valueOf(getSavedFastIdx()) < liveList.size()) {
                            liveList.get(Integer.valueOf(getSavedFastIdx())).getBtns().requestFocus();
                        } else {
                            if (liveList.size() > 0)
                                liveList.get(0).getBtns().requestFocus();
                        }

//                        listView.setItemsCanFocus(true);
//                        listView.setSelected(true);
//                        listView.setSelection(0);
//                        listView.setItemChecked(0, true);
//                        liveListAdapter.notifyDataSetChanged();
                        slmenu.setVisibility(View.VISIBLE);
                    }

                    Log.d("vi", "visible");
                    menuVisible = true;
                }
            }

            return true;
            //}
        } else if (keyCode == KeyEvent.KEYCODE_0) {
            shortChannel(0);
        } else if (keyCode == KeyEvent.KEYCODE_1) {
            shortChannel(1);
        } else if (keyCode == KeyEvent.KEYCODE_2) {
            shortChannel(2);
        } else if (keyCode == KeyEvent.KEYCODE_3) {
            shortChannel(3);
        } else if (keyCode == KeyEvent.KEYCODE_4) {
            shortChannel(4);
        } else if (keyCode == KeyEvent.KEYCODE_5) {
            shortChannel(5);
        } else if (keyCode == KeyEvent.KEYCODE_6) {
            shortChannel(6);
        } else if (keyCode == KeyEvent.KEYCODE_7) {
            shortChannel(7);
        } else if (keyCode == KeyEvent.KEYCODE_8) {
            shortChannel(8);
        } else if (keyCode == KeyEvent.KEYCODE_9) {
            shortChannel(9);
        }
        return super.onKeyDown(keyCode, event);
    }

    class ViewBannerTask extends AsyncTask<String, Integer, Long> {
        private String imageUrl;
        private Context mContext;
        private Bitmap image;

        public ViewBannerTask(Context context) {
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
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            if (mainid.equals("live"))
                //nameValuePairs.add(new BasicNameValuePair("code1", "114"));
                nameValuePairs.add(new BasicNameValuePair("menu1", "114"));
            else
                nameValuePairs.add(new BasicNameValuePair("menu1", mainid));
            if (!mainid.equals("")) {
                if (subid.equals("update"))
                    nameValuePairs.add(new BasicNameValuePair("menu2", subid));
                else
                    //nameValuePairs.add(new BasicNameValuePair("code2", subid));
                    nameValuePairs.add(new BasicNameValuePair("menu2", subid));
                if (mainid.equals("live"))
                    nameValuePairs.add(new BasicNameValuePair("menu3", idx));
                strJson = postmake.httpConnect(
                        Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(strJson);
                    JSONObject json_data = jArray.getJSONObject(0);
                    //detailTitle = json_data.getString("title");
                    imageUrl = json_data.getString("image");
                    Log.e(null, "imageUrl" + imageUrl);
                } catch (JSONException e) {
                }
                UrlImageLoader urlImageload = new UrlImageLoader();
                image = urlImageload.getUrlImage(imageUrl);
            }
            publishProgress();
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }


            while (!isPlaying) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            //이어보기 추가
            if (vv != null) {
                if (!mainid.equals("live")) {
                    if (Integer.valueOf(saveIdx) != 0) {
                        aContinueDlg.show();
                    } else {
                        vv.start();
                    }
                } else {
                    vv.start();
                }
            }
            if (!isPlaying)
                dlg.show();
            bannerView.setVisibility(View.GONE);
            if (image != null) {
                image.recycle();
                image = null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            //	mImageView.scrollTo(mScrollStep++, 0);
            if (image != null) {
                if (dlg != null)
                    dlg.dismiss();
                bannerView.setImageBitmap(image);
                bannerView.setVisibility(View.VISIBLE);
                bannerView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
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
            if (!mainid.equals("broadcast")) {
                nameValuePairs.add(new BasicNameValuePair("menu", subid));

                nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
                if (liveList.size() > 0)
                    nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
                strJson = postmake.httpConnect(
                        Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
                //Log.e(null, "json" + broadcastList.get(position).getIdx());
                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(strJson);
                    JSONObject json_data = jArray.getJSONObject(0);
                    //detailTitle = json_data.getString("title");
                    detailP_code = json_data.getString("p_code");
                    detailVod_type = json_data.getString("vod_type");
                    detailVod_code = json_data.getString("vod_code");
                    Log.e(null, json_data.getString("p_code"));
                } catch (JSONException e) {

                }
            }

            strJson = "";
            nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
            if (mainid.equals("broadcast")) {
                nameValuePairs.add(new BasicNameValuePair("p_code", dramaList.get(position).getP_code()));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "D"));
                nameValuePairs.add(new BasicNameValuePair("vod_code", dramaList.get(position).getVod_code()));
            } else {
                nameValuePairs.add(new BasicNameValuePair("p_code", detailP_code));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
                nameValuePairs.add(new BasicNameValuePair("vod_code", detailVod_code));
            }
            if (!Util.getChildset(mContext))
                nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));

            if (liveList.size() > 0)
                nameValuePairs.add(new BasicNameValuePair("idx", liveList.get(position).getIdx()));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                //detailTitle = json_data.getString("title");
                result_code = json_data.getString("result");
                vod_url = json_data.getString("vod_url");
                //Log.e(null, vod_url);
            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            if (result_code != null) {
                if (mainid.equals("broadcast")) {
                    saveFastIdx(String.valueOf(position));
                    checkPlay(result_code, vod_url, idx, dramaList.get(position).getSubid(), true);
                } else {
                    saveFastIdx(String.valueOf(position));
                    checkPlay(result_code, vod_url, liveList.get(position).getIdx(), liveList.get(position).getSubid(), false);
                }
            }
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

    class ViewDetailShortTask extends AsyncTask<String, Integer, Long> {
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

        public ViewDetailShortTask(Context context, int position) {
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
            if (!mainid.equals("broadcast")) {
                nameValuePairs.add(new BasicNameValuePair("menu", subid));

                nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));
                if (shortChannelDateList.size() > 0)
                    nameValuePairs.add(new BasicNameValuePair("idx", shortChannelDateList.get(position).getPcode()));
                strJson = postmake.httpConnect(
                        Constant.mainUrl + "/module/tv/detail.php", nameValuePairs);
                //Log.e(null, "json" + broadcastList.get(position).getIdx());
                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(strJson);
                    JSONObject json_data = jArray.getJSONObject(0);
                    //detailTitle = json_data.getString("title");
                    detailP_code = json_data.getString("p_code");
                    detailVod_type = json_data.getString("vod_type");
                    detailVod_code = json_data.getString("vod_code");
                    Log.e(null, json_data.getString("p_code"));
                } catch (JSONException e) {

                }
            }

            strJson = "";
            nameValuePairs = null;
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
            if (mainid.equals("broadcast")) {
                nameValuePairs.add(new BasicNameValuePair("p_code", shortChannelDateList.get(position).getPcode()));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
                //nameValuePairs.add(new BasicNameValuePair("vod_code", liveList.get(position).getVod_code()));
            } else {
                nameValuePairs.add(new BasicNameValuePair("p_code", detailP_code));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "L"));
                nameValuePairs.add(new BasicNameValuePair("vod_code", detailVod_code));
            }
            if (!Util.getChildset(mContext))
                nameValuePairs.add(new BasicNameValuePair("adult_pwd", getChildNum()));

            if (shortChannelDateList.size() > 0)
                nameValuePairs.add(new BasicNameValuePair("idx", shortChannelDateList.get(position).getPcode()));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/play.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                //detailTitle = json_data.getString("title");
                result_code = json_data.getString("result");
                vod_url = json_data.getString("vod_url");
                //Log.e(null, vod_url);
            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            //Log.e(null, "url" + vod_url + " :idx:"+ liveList.get(position).getIdx());
            if (result_code != null)
                checkPlay(result_code, vod_url, shortChannelDateList.get(position).getPcode(), shortChannelDateList.get(position).getPcode(), false);
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

    private String getChildNum() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), Context.MODE_PRIVATE);
        return sp.getString("childnum", "0000");
    }

    private void checkPlay(String result, String vod_url, String idx, String gsubid, boolean broadcast) {
        if (Constant.isTest) {
            goLivePlay(vod_url, idx, gsubid, broadcast);
            return;
        }
        if (getAuth().equals("ok")) {
            if (result.equals("OK")) {
                goLivePlay(vod_url, idx, gsubid, broadcast);
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
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(VideoViewActivity.this);
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

    private void goLivePlay(String strUrl, String idx, String gsubid, boolean broadcastVod) {
        Intent intent = new Intent(VideoViewActivity.this, VideoViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        //Log.e(null, strUrl);
        myData.putString("idx", idx);
        myData.putString("videourl", strUrl);

        Log.e(null, "gsubid" + gsubid);
        myData.putString("subid", gsubid);
        if (broadcastVod) {
            myData.putString("mainid", "broadcast");
            intent.putParcelableArrayListExtra("dramadata", dramaList);
        } else
            myData.putString("mainid", "live");
        intent.putExtras(myData);
        startActivity(intent);

        if (vv != null) {
            Log.e(null, "ididididid" + String.valueOf(vv.getCurrentPosition()));
            saveDate(url, String.valueOf(vv.getCurrentPosition()));
        }
        finish();
    }

    private String getAuth() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("auth", "");
    }


    class GetTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;


        public GetTask(Context context) {
            mContext = context;
        }

        @Override
        protected final void onPreExecute() {
        }

        @Override
        protected Long doInBackground(String... params) {
            // TODO Auto-generated method stub
            getImages();
            Log.d("dd", "111111111111111");
            return 0L;
        }


        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            setMenu();
            menuShowable = true;
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
                    Constant.mainUrl + "/module/tv/live_shortcut.php", nameValuePairs);
            JSONArray jArray = null;
            try {
                shortChannelDateList = new ArrayList<ShortChannelDateList>();
                jArray = new JSONArray(strJson);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    ShortChannelDateList tempList = new ShortChannelDateList(json_data.getString("shortcut"), json_data.getString("p_code"), json_data.getString("p_title"));
                    //list.add(tempList);
                    shortChannelDateList.add(i, tempList);
                }
            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            //publishProgress(1);
            return 0L;
        }

        @Override
        protected void onPostExecute(Long lresult) {
            // TODO Auto-generated method stub
            //mProgress.dismiss();

            //setShorChannelInfo();
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

    private void shortChannel(int num) {
        if (shortChannelDateList != null) {
            for (int i = 0; i < shortChannelDateList.size(); i++) {
                if (shortChannelDateList.get(i).getShortcut().equals(String.valueOf(num))) {
                    if (!isClicked) {
                        if (vv != null) {
                            Log.e(null, "ididididid" + String.valueOf(vv.getCurrentPosition()));
                            saveDate(url, String.valueOf(vv.getCurrentPosition()));
                        }
                        if (vv != null) {
                            if (vv.isPlaying()) {
                                vv.stopPlayback();
                                vv = null;
                            }
                            vv = null;
                        }
                        isClicked = true;
                        //Log.e(null, "position=" + i);
                        ViewDetailShortTask viewDetailTask = new ViewDetailShortTask(mContext, i);
                        viewDetailTask.execute();
                    }
                }
            }
        }
    }
}
