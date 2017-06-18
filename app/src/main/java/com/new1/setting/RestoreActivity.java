package com.new1.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.settop.CoverFlow;
import com.new1.settop.Detail2DialogActivity;
import com.new1.settop.DetailDialogActivity;
import com.new1.settop.DetailDramaActivity;
import com.new1.settop.DetailShowActivity;
import com.new1.settop.R;
import com.new1.settop.VideoViewActivity;
import com.new1.settop.delivery.DeliveryListActivity;
import com.new1.settop.live.LiveCoverFlow;
import com.new1.settop.shopping.DetailYellowActivity;
import com.new1.settop.shopping.LifeListActivity;
import com.new1.settop.shopping.ShoppingListActivity;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.UrlImageLoader;
import com.noh.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestoreActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private String mainid = "";
    private DetailDialogActivity mCustomPopup;
    private CoverFlow coverFlow;
    private ProgressDialog mProgress;
    private Button expireText;
    private ImageView ivTel = null;
    private boolean isClicked = false;
    private ImageView imgLock = null;
    private ImageView imgNet = null;
    private Context mContext = null;
    private int nowPosition;

    private ArrayList<BroadcastList> broadcastList = null;
    private ImageAdapter imageAdapter = null;
    private ViewNextTask viewNextTask = null;
    private ViewTextTask viewTextTask = null;
    private TextView coverTextView = null;
    private int nowCoverFlowIdx = 0;
    private String type = "";
    private Detail2DialogActivity mCustomPopup2;

    private String adImage;
    private String adBannerType;
    private String adBannerIdx;
    private String adImage2;
    private String adBannerType2;
    private String adBannerIdx2;

    public RestoreActivity() {
        broadcastList = new ArrayList<BroadcastList>();
        nowCoverFlowIdx = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.restore);

        mContext = this.getBaseContext();

        ImageDownloader imageDownloader = new ImageDownloader();
        ivTel = (ImageView) findViewById(R.id.tel);
        //ivTel.setImageResource(R.drawable.image_main_logo);
        imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView) ivTel);

        ImageLoader.getInstance().init(Util.getConfig(mContext));

        imgLock = (ImageView) findViewById(R.id.imgLock);
        if (!Util.getChildset(mContext))
            imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        else
            imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
        imgNet = (ImageView) findViewById(R.id.imgNet);
        if (!Util.checkNetwordState(mContext))
            imgNet.setBackgroundResource(R.drawable.image_main_net_off);
        else
            imgNet.setBackgroundResource(R.drawable.image_main_net_on);

        coverTextView = (TextView) findViewById(R.id.coverflowtext);
        Button expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if (!getExpireDate().equals("")) {
            expireText.setText(getExpireDate().substring(0, 2));
            expireText2.setText(getExpireDate().substring(2, 4));
        }
        AdTask adTask = new AdTask(mContext);
        adTask.execute();

        AdTask2 adTask2 = new AdTask2(mContext);
        adTask2.execute();

        expireText = (Button) findViewById(R.id.imgExpireDate);
        clickOkButton();

    }

    private void setCoverflowData() {
        coverFlow = (CoverFlow) findViewById(R.id.coverflow);
        imageAdapter = new ImageAdapter(this);
        coverFlow.setAdapter(imageAdapter);


        coverFlow.setSpacing(-5);

        coverFlow.setAnimationDuration(500);

        viewNextTask = new ViewNextTask(mContext, broadcastList, coverFlow.getSelectedItemPosition() - 11,
                coverFlow.getSelectedItemPosition() + 11);
        //viewNextTask.execute();

        //viewNextTask = new ViewNextTask(mContext, broadcastList, 5, broadcastList.size());
        //viewNextTask.execute();

        coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //coverFlow.requestFocus();
                // Log.e(null, broadcastList.get((int)arg3).getTitle());
                viewNextTask = new ViewNextTask(mContext, broadcastList, (int) arg3 - 11,
                        (int) arg3 + 11);
                viewNextTask.execute();
                if (!broadcastList.get((int) arg3).getType().equals("")) {
                    type = "etc";
                    viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int) arg3).getTitle() + " " + broadcastList.get((int) arg3).getPu_no());

                } else if (broadcastList.get((int) arg3).getType().equals("")) {
                    type = "movie";
                    viewTextTask = new ViewTextTask(mContext, broadcastList, broadcastList.get((int) arg3).getTitle());
                }


                if (viewTextTask != null)
                    viewTextTask.execute();
                nowCoverFlowIdx = (int) arg3;
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


                if (position != -1) {
                    mCustomPopup2 = new Detail2DialogActivity(RestoreActivity.this, xPop2PlayClickListener, xPop2PlayKeyListener, xPop2RestoreClickListener, xPop2RestoreKeyListener, mClickAdListener, mKeyAdListener, mClickAd2Listener,"delete", adImage, adImage2);
                    mCustomPopup2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mCustomPopup2.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    mCustomPopup2.show();
                }

	 			/*if(broadcastList.get((int)position) == null)
	 				return;
				if(!broadcastList.get((int)position).getType().equals("")){
					type = "etc";
					Log.e(null, "click2");
					if(!isClicked){
						isClicked = true;
						MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
						viewDetailTask.execute();
					}
				}else if(broadcastList.get((int)position).getType().equals("")){
					type = "movie";
					Log.e(null, "click");
					if(!isClicked){
						isClicked = true;
						MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
						viewDetailTask.execute();
					}	
				}*/
            }
        });
    }

    private Dialog.OnKeyListener mKeyAdListener = new Dialog.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (!isClicked) {
                    isClicked = true;
                    goAdActivity(adBannerIdx);
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
                goAdActivity(adBannerIdx);

            }
        }
    };

    private Dialog.OnKeyListener mKeyAd2Listener = new Dialog.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (!isClicked) {
                    isClicked = true;
                    goAdActivity(adBannerIdx2);
                }
            }
            return false;

        }

    };

    private View.OnClickListener mClickAd2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                goAdActivity(adBannerIdx2);

            }
        }
    };

    private void goAdActivity(String idx){
        if(adBannerType.equals("S")){
            Intent intent = new Intent(RestoreActivity.this, ShoppingListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("code1", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(adBannerType.equals("D")){
            Intent intent = new Intent(RestoreActivity.this, DeliveryListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("code1", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(adBannerType.equals("Y")){
            Intent intent = new Intent(RestoreActivity.this, DetailYellowActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("code", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }else if(adBannerType.equals("L")){
            Intent intent = new Intent(RestoreActivity.this, LifeListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle myData = new Bundle();
            myData.putString("mainid", "");
            myData.putString("subid", "");
            myData.putString("code1", idx);
            intent.putExtras(myData);
            startActivity(intent);
        }
    }

    private View.OnKeyListener xPop2PlayKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e(null, "test" + broadcastList.get((int) mPosition).getType());
                if (broadcastList.get((int) mPosition) == null)
                    return true;
                if (!broadcastList.get((int) mPosition).getType().equals("") || broadcastList.get((int) mPosition).getCode2().equals("116")) {
                    Log.e(null, "test2");
                    type = "etc";
						/*if(!isClicked){
							isClicked = true;
							MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
							viewDetailTask.execute();
						}*/
                    if (broadcastList.get((int) mPosition).getCode2().equals("002")) {
                        goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("006")) {
                        goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("116")) {
                        goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("013")) {
                        goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("007")) {
                        goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("026")) {
                        goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    } else if (broadcastList.get((int) mPosition).getCode2().equals("116")) {
                        goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                    }
                } else if (broadcastList.get((int) mPosition).getType().equals("")) {
                    type = "movie";
                    Log.e(null, "test");
                    if (!isClicked) {
                        isClicked = true;
                        MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
                        viewDetailTask.execute();
                    }
                }
                if (mCustomPopup2 != null)
                    mCustomPopup2.dismiss();
            }
            return false;
        }
    };

    private View.OnClickListener xPop2PlayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (broadcastList.get((int) mPosition) == null)
                return;
            if (!broadcastList.get((int) mPosition).getType().equals("") || broadcastList.get((int) mPosition).getCode2().equals("116")) {
                type = "etc";
					/*if(!isClicked){
						isClicked = true;
						MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
						viewDetailTask.execute();
					}*/
                if (broadcastList.get((int) mPosition).getCode2().equals("002")) {
                    goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("006")) {
                    goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("116")) {
                    goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("013")) {
                    goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("007")) {
                    goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("026")) {
                    goDetailDramaActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                } else if (broadcastList.get((int) mPosition).getCode2().equals("116")) {
                    goDetailShowActivity(broadcastList.get(mPosition).getIdx(), broadcastList.get(mPosition).getTitle(), broadcastList.get((int) mPosition).getCode2(), broadcastList.get((int) mPosition).getImage());
                }
            } else if (broadcastList.get((int) mPosition).getType().equals("")) {
                type = "movie";
                Log.e(null, "click");
                if (!isClicked) {
                    isClicked = true;
                    MovieDetailTask viewDetailTask = new MovieDetailTask(mContext, mPosition);
                    viewDetailTask.execute();
                }
            }
            if (mCustomPopup2 != null)
                mCustomPopup2.dismiss();
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
            //if(!isClicked){
            //	isClicked = true;
            if (mCustomPopup2 != null)
                mCustomPopup2.dismiss();
            WishTask wishTask = new WishTask(mContext);
            wishTask.execute();
            //}
        }
    };

    private void goDetailDramaActivity(String idx, String title, String menu, String image) {
        mainid = "broadcast";
        Intent intent = new Intent(this, DetailDramaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        myData.putString("mainid", mainid);
        myData.putString("idx", idx);
        myData.putString("menu", menu);
        myData.putString("title", title);
        myData.putString("image", image);
        myData.putString("back", "back");
        intent.putExtras(myData);
        startActivity(intent);
    }

    private void goDetailShowActivity(String idx, String title, String menu, String image) {
        mainid = "broadcast";
        Intent intent = new Intent(this, DetailShowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        myData.putString("mainid", mainid);
        myData.putString("idx", idx);
        myData.putString("menu", menu);
        myData.putString("title", title);
        myData.putString("image", image);
        myData.putString("back", "back");
        intent.putExtras(myData);
        startActivity(intent);
    }

    int mPosition;

    @Override
    protected void onDestroy() {
        if (mCustomPopup2 != null)
            mCustomPopup2.dismiss();
        if (viewNextTask != null) {
            viewNextTask.cancel(true);
            viewNextTask = null;
        }
        if (viewTextTask != null) {
            viewTextTask.cancel(true);
            viewTextTask = null;
        }
        recycleAllBitmap();
        if (mProgress != null) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            //clickOkButton();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clickOkButton() {

        GetTask getTask = new GetTask(mContext);
        getTask.execute();
    }
    //텍스트 필드를 모두 안채웠을때
		/*if(regargeNum1.getText().toString().length() < 4 || regargeNum2.getText().toString().length() < 4 
				 ||regargeNum3.getText().toString().length() < 4 || regargeNum4.getText().toString().length() < 4 ){
			/*AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		    alt_bld.setMessage(R.string.check_recharge);
		    alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
		    AlertDialog alert = alt_bld.create();
		    alert.show();*/
		/* }else{
			 strRechargeNumber = regargeNum1.getText().toString() + "-" + regargeNum2.getText().toString() + "-" + 
					 regargeNum3.getText().toString() + "-" + regargeNum4.getText().toString();
			 
			 sendRechargeTask = new SendRechargeTask(getBaseContext());
		     sendRechargeTask.execute();
			 //getMacaddress();
		 }*/

    private boolean getImages() {
        int startIdx = 0;
        int endIdx = 29999;

        broadcastList = new ArrayList<BroadcastList>();

        String strJson = "";
        PostHttp postmake = new PostHttp();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("start", String.valueOf(startIdx)));
        nameValuePairs.add(new BasicNameValuePair("end", String.valueOf(endIdx)));
        nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

        strJson = postmake.httpConnect(
                Constant.mainUrl + "/module/tv/wishlist.php", nameValuePairs);
        JSONArray jArray = null;
        try {
            //JSONObject json = new JSONObject(strJson);
            jArray = new JSONArray(strJson);
            for (int i = 0; i < jArray.length(); i++) {
                BroadcastList tempList = new BroadcastList();
                JSONObject json_data = jArray.getJSONObject(i);
                tempList.setIdx(json_data.getString("idx"));
                tempList.setwIdx(json_data.getString("widx"));
                tempList.setType(json_data.getString("type"));
                tempList.setVod_code(json_data.getString("vod_code"));
                tempList.setImage(json_data.getString("image"));
                tempList.setTitle(json_data.getString("title"));
                tempList.setCode2(json_data.getString("code2"));
                tempList.setPu_no(json_data.getString("pu_no"));
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
            boolean result = false;
            result = getImages();
            //				image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageAdapter
            //publishProgress(1);
            if (result)
                return 0L;
            else
                return 1L;
        }


        protected void showProgress() {
            mProgress = new ProgressDialog(RestoreActivity.this);
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
            if (coverFlow != null)
                coverFlow.requestFocus();
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

            coverTextView.setText(this.text);
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
            isClicked = false;
            if (!isClicked) {
                isClicked = true;
                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
                viewDetailTask.execute();
            }
    		/*mCustomPopup = new DetailDialogActivity(RestoreActivity.this, xPopClickListener, xPopKeyListener, broadcastList.get(position).getTitle(),
					broadcastList.get(position).getImage(), genre, point,
					age, date, director,
					cast, story);
			
		    
			mCustomPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mCustomPopup.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT); 
			mCustomPopup.show();*/
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

    private View.OnKeyListener xPopKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                if (!isClicked) {
                    isClicked = true;
                    ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
                    viewDetailTask.execute();
                }
            }
            return false;
        }
    };

    private View.OnClickListener xPopClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (!isClicked) {
                isClicked = true;
                ViewDetailTask viewDetailTask = new ViewDetailTask(mContext, mPosition);
                viewDetailTask.execute();
            }
        }
    };

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
            if (type.equals("etc")) {
                nameValuePairs.add(new BasicNameValuePair("p_code", broadcastList.get(position).getIdx()));
                nameValuePairs.add(new BasicNameValuePair("vod_type", "D"));
                nameValuePairs.add(new BasicNameValuePair("vod_code", broadcastList.get(position).getType()));
            } else if (type.equals("movie")) {
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

            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            return 0L;
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            Log.e(null, "url" + vod_url);
            //checkPlay(result_code, vod_url, broadcastList.get(position).getIdx(), broadcastList.get(position).getCode2(), broadcastList.get(position).getType());
            checkPlay(result_code, vod_url, broadcastList.get(position).getIdx(), "", type);
            isClicked = false;
            if (mCustomPopup != null)
                mCustomPopup.dismiss();
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

    private void checkPlay(String result, String vod_url, String idx, String code2, String type) {
        if (Constant.isTest) {
            goLivePlay(vod_url, idx, code2, type);
            return;
        }
        Log.e(null, "go" + vod_url);
        if (getAuth().equals("ok")) {
            if (result.equals("OK")) {
                goLivePlay(vod_url, idx, code2, type);
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
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(RestoreActivity.this);
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

    private void goLivePlay(String strUrl, String idx, String code2, String type) {
        //recycleAllBitmap();
        Intent intent = new Intent(RestoreActivity.this, VideoViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle myData = new Bundle();
        myData.putString("idx", idx);
        myData.putString("videourl", strUrl);
        if (type.equals("etc"))
            mainid = "broadcast";
        else if (type.equals("movie"))
            mainid = "movie";
        myData.putString("mainid", mainid);
        myData.putString("subid", code2);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                i.setLayoutParams(new LiveCoverFlow.LayoutParams(259, 367));
                i.setPadding(2, 2, 2, 2);
            } else {
                i.setLayoutParams(new LiveCoverFlow.LayoutParams(173, 245));
                i.setPadding(1, 1, 1, 1);
            }

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


    private String getChildNum() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("childnum", "0000");
    }

    private String getNetExpireDate(String macAdress) {
        String expireDate = "";
        String strJson = "";
        PostHttp postmake = new PostHttp();
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        //macAdress = "123456789";
        Log.e(null, "mac:" + macAdress);
        nameValuePairs.add(new BasicNameValuePair("id", macAdress));
        strJson = postmake.httpConnect(
                Constant.mainUrl + "/module/tv/expiredate.php", nameValuePairs);
        try {
            JSONObject json_data = new JSONObject(strJson);
            expireDate = json_data.getString("date");

        } catch (JSONException e) {
            Log.e(null, e.toString());
        }
        String[] st = new String[3];
        if (expireDate != null)
            st = expireDate.split("-");
        if (st.length > 1)
            expireDate = st[1] + "  " + st[2];
        return expireDate;
    }

    private String getExpireDate() {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        return sp.getString("expiredate", "");
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

    private void saveAuth(boolean isAuth) {
        SharedPreferences sp = getSharedPreferences(Util.getApplicationName(getApplicationContext()), MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isAuth)
            editor.putString("auth", "ok");
        else
            editor.putString("auth", "");
        editor.commit();
    }

    private void recycleAllBitmap() {
        if (broadcastList != null) {
            for (int i = 0; i < broadcastList.size(); i++)
                broadcastList.get(i).recyleBitmap();
        }
    }


    class WishTask extends AsyncTask<String, Integer, Long> {
        private ArrayList<BroadcastList> list = null;
        private int position;

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

            //nowPosition = broadcastList.size() - pageIdx*25 - position-1;

            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));//getMacaddress()));
            nameValuePairs.add(new BasicNameValuePair("ptype", "del"));
            nameValuePairs.add(new BasicNameValuePair("idx", broadcastList.get(mPosition).getwIdx()));
            //nameValuePairs.add(new BasicNameValuePair("pu_idx", broadcastList.get(mPosition).getType()));

            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/wishlist_proc.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                //detailTitle = json_data.getString("title");
                //result_code = json_data.getString("result");
                //vod_url = json_data.getString("vod_url");
            } catch (JSONException e) {
                Log.e(null, e.toString());
            }
            return 0L;

        }


        protected void showProgress() {
            mProgress = new ProgressDialog(RestoreActivity.this);
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    WishTask.this.cancel(true);
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
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(RestoreActivity.this);
            alt_bld.setMessage("보관함에서 삭제되었습니다.");
            alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    clickOkButton();
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
            nameValuePairs.add(new BasicNameValuePair("type", "F"));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(strJson);
                JSONObject json_data = jArray.getJSONObject(0);
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
            nameValuePairs.add(new BasicNameValuePair("type", "F"));
            strJson = postmake.httpConnect(
                    Constant.mainUrl + "/module/tv/playerbanner.php", nameValuePairs);
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(strJson);
                JSONObject json_data = jArray.getJSONObject(0);
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


