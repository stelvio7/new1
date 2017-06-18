package com.new1.setting;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.new1.model.BroadcastList;
import com.new1.model.Constant;
import com.new1.model.Server;
import com.new1.settop.R;
import com.noh.util.ImageDownloader;
import com.noh.util.PostHttp;
import com.noh.util.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.TimerTask;

public class SpeedActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    //private TextView txtNowDay = null;
    //private TextView txtNowTime = null;

    private ImageView imgArrow;
    private Button btnSpeedCheck;
    private Button btnSelectServer;
    private Button btnConfirmServer;

    private DownloadTask downloadTask = null;
    private ProgressDialog mProgress;
    private Button expireText;
    private ImageView ivTel = null;
    private TextView txtSpeed = null;

    private ImageView imgLock = null;
    private ImageView imgNet = null;
    private Context mContext = null;
    private Spinner txtServer = null;
    private Spinner txtConfirmServer = null;

    private TimerTask timerTask;

    public String videoUrl = "";
    public int idxServer = 0;
    public int idxConfirmServer = 0;
    private ArrayList<Server> serverList = new ArrayList<Server>();
    private ArrayList<String> strServerList = new ArrayList<String>();
    private ProgressBar progressBar1;
    private int backWidth;
    public ImageView speedback;
    private boolean isfirst = true;

    private ImageView backImage;

    public SpeedActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedcheck);

        mContext = this.getBaseContext();

        ImageDownloader imageDownloader = new ImageDownloader();
        ivTel = (ImageView) findViewById(R.id.tel);
        imageDownloader.download(Constant.mainUrl + "/image/info_tel.png", (ImageView) ivTel);

        imgLock = (ImageView) findViewById(R.id.imgLock);
        if (!Util.getChildset(mContext))
            imgLock.setBackgroundResource(R.drawable.image_main_lock_off);
        else
            imgLock.setBackgroundResource(R.drawable.image_main_lock_on);
        imgNet = (ImageView) findViewById(R.id.imgNet);
        if (!Util.checkNetwordState(mContext))
            imgNet.setBackgroundResource(R.drawable.image_main_net_off);

        expireText = (Button) findViewById(R.id.imgExpireDate);
        Button expireText2 = (Button) findViewById(R.id.imgExpireDate2);
        if (!getExpireDate().equals("")) {
            expireText.setText(getExpireDate().substring(0, 2));
            expireText2.setText(getExpireDate().substring(2, 4));
        }

        //txtNowDay = (TextView) findViewById(R.id.txtNowDay);
        //txtNowTime = (TextView) findViewById(R.id.txtNowTime);

        backImage = (ImageView) findViewById(R.id.backImage);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        txtServer = (Spinner) findViewById(R.id.txtServer);
        txtConfirmServer = (Spinner) findViewById(R.id.txtConfirmServer);

        speedback = (ImageView) findViewById(R.id.speedback);

        imgArrow = (ImageView) findViewById(R.id.imgArrow);
        btnSpeedCheck = (Button) findViewById(R.id.btnSpeedCheck);
        btnSelectServer = (Button) findViewById(R.id.btnSelectServer);
        btnConfirmServer = (Button) findViewById(R.id.btnConfirmServer);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);
        btnSpeedCheck.requestFocus();



        VideoTask videoTask = new VideoTask(getBaseContext());
        videoTask.execute();

        speedback.post(new Runnable() {
            @Override
            public void run() {
                backWidth = speedback.getWidth();
                Log.e("DEBUG", "h2: " + speedback.getHeight());
                Log.e("DEBUG", "w2: " + speedback.getWidth());
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (downloadTask != null) {
            downloadTask = null;
        }
        if (mProgress != null) {
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

    class DownloadTask extends AsyncTask<String, Integer, Long> {
        private Context mContext;
        private String result = "";
        float speed = 0;

        public DownloadTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mProgress = ProgressDialog.show(SpeedActivity.this, "", getResources().getString(R.string.wait), true, true);
            //mProgress.setCancelable(false);
        }

        @Override
        protected Long doInBackground(String... params) {
            // TODO Auto-generated method stub
            int count = 0;
            float result = 0;

            try {
                Thread.sleep(100);

                URL url = new URL("http://" + serverList.get(idxServer).getDomain() + "/movie/old/o_199107060.mp4");
                Log.d("dd", "http://" + serverList.get(idxServer).getDomain() + "/movie/old/o_199107060.mp4");
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                //OutputStream output = new FileOutputStream("/sdcard/Downloadvideo.mp4");

                byte data[] = new byte[1024];

                long total = 0;

                long startTime = System.currentTimeMillis();
                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // output.write(data, 0, count);
                    //Log.e(null, "total:" + total + "count:" +count);
                    if (total >= 500000)
                        break;
                }
                long endTime = System.currentTimeMillis();
                //output.flush();
                //output.close();
                input.close();

                long diffTime = endTime - startTime;
                result = (500000 / diffTime);
                speed = result / (float) 1000;
                Log.e(null, "time:" + (500000 / diffTime));
                if (result <= 0) {
                    return 54L;
                } else if (result <= 1000) {
                    result = result * ((float) 430 / (float) 1000);
                    //Log.e(null, "result:" + result);
                    return (long) result + 54L;
                } else if (result <= 5000) {
                    //result -= 1000;
                    result = result * ((float) 820 / (float) 4000);
                    //Log.e(null, "result:" + result);
                    return (long) result + 287L;
                } else if (result <= 10000) {
                    result = result * ((float) 210 / (float) 5000);
                    //Log.e(null, "result:" + result);
                    return (long) result + 1117L;
                } else if (result <= 20000) {
                    result = result * ((float) 210 / (float) 10000);
                    //Log.e(null, "result:" + result);
                    return (long) result + 1334L;
                } else {
                    return (long) 1770L;
                }

                // 작업이 진행되면서 호출하며 화면의 업그레이드를 담당하게 된다
                //publishProgress("progress", 1, "Task " + 1 + " number");

            } catch (InterruptedException e) {
                e.printStackTrace();
                return 50L;
            } catch (IOException e) {
                e.printStackTrace();
                return 50L;
            }

            // 수행이 끝나고 리턴하는 값은 다음에 수행될 onProgressUpdate 의 파라미터가 된다
            // return result;

        }

        @SuppressLint("NewApi")
        @Override
        protected void onPostExecute(Long lresult) {
            // TODO Auto-generated method stub
            imgArrow.setX((float) lresult * backWidth / 1872);
            if (mProgress != null)
                mProgress.dismiss();
            txtSpeed.setText(String.valueOf(speed));
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



    class GetNowTask extends AsyncTask<String, Integer, Boolean> {
        private Context mContext;
        boolean success = false;

        public GetNowTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean loginSuccess = false;
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

            strJson = postmake.httpConnect(
                        /*Constant.mainUrl + */Constant.mainUrl + "/module/tv/member.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                int idx = Integer.parseInt(json_data.getString("live_server"));

                for(int i = 0; i < serverList.size(); i++) {
                    if(serverList.get(i).getCode().equals(String.valueOf(idx))){
                        idxServer = i;
                        idxConfirmServer = i;
                    }
                }


                Log.d("", "idx" + idxServer);

                loginSuccess = true;
            } catch (Exception e) {
            }
            if (loginSuccess)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            if (result) {
                Log.e(null, "aaaaaaaaaaaaaaaa" + idxServer);
                if(txtServer != null) {
                    txtServer.setSelected(true);
                    txtServer.setSelection(idxServer);

                }
                if(txtConfirmServer != null){
                    txtConfirmServer.setSelected(true);
                    txtConfirmServer.setSelection(idxServer);
                }
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

    class VideoTask extends AsyncTask<String, Integer, Boolean> {
        private Context mContext;
        boolean success = false;

        public VideoTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean loginSuccess = false;
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            strJson = postmake.httpConnect(
                        /*Constant.mainUrl + */Constant.mainUrl + "/module/tv/nation.php", nameValuePairs);
            try {
                JSONArray jArray = new JSONArray(strJson);

                for (int i = 0; i < jArray.length(); i++) {
                    BroadcastList tempList = new BroadcastList();
                    JSONObject json_data = jArray.getJSONObject(i);
                    Server serverData = new Server();
                    serverData.setCode(json_data.getString("code"));
                    serverData.setDomain(json_data.getString("domain"));
                    serverData.setName(json_data.getString("name"));
                    strServerList.add(serverData.getName());
                    serverList.add(serverData);
                    loginSuccess = true;
                }
            } catch (JSONException e) {
            }
            if (loginSuccess)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub

            if (result) {
                btnSpeedCheck.setVisibility(View.VISIBLE);
                txtServer.setAdapter(new ArrayAdapter<String>(SpeedActivity.this, R.layout.spinner_item, strServerList));


                txtServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            if(!isfirst) {
                                                                idxServer = position;
                                                                Log.d("", "set" + idxServer);
                                                                //SelectTask downloadTask = new SelectTask(getBaseContext());
                                                                //downloadTask.execute();

                                                            }
                                                            //isfirst = false;
                                                        }
                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    }
                );

                txtConfirmServer.setAdapter(new ArrayAdapter<String>(SpeedActivity.this, R.layout.spinner_item, strServerList));


                txtConfirmServer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            if(!isfirst) {
                                                                idxConfirmServer = position;
                                                                Log.d("", "set" + idxConfirmServer);
                                                                SelectTask downloadTask = new SelectTask(getBaseContext());
                                                                downloadTask.execute();

                                                            }
                                                            isfirst = false;
                                                        }
                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    }
                );

                if(isfirst) {
                    GetNowTask getTask = new GetNowTask(getBaseContext());
                    getTask.execute();
                }
                btnConfirmServer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        txtConfirmServer.performClick();
                    }
                });

                btnSelectServer.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        txtServer.performClick();
                    }
                });
                btnSpeedCheck.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        DownloadTask downloadTask = new DownloadTask(getBaseContext());
                        downloadTask.execute();
                    }
                });
                //txtServer.setText(serverList.get(idxServer));
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

    class SelectTask extends AsyncTask<String, Integer, Boolean> {
        private Context mContext;
        boolean success = false;

        public SelectTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            String strJson = "";
            PostHttp postmake = new PostHttp();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("vod_server", serverList.get(idxConfirmServer).getCode()));
            nameValuePairs.add(new BasicNameValuePair("live_server", serverList.get(idxConfirmServer).getCode()));
            nameValuePairs.add(new BasicNameValuePair("id", getMacaddress()));

            strJson = postmake.httpConnect(
                        Constant.mainUrl + "/module/tv/member_server_proc.php", nameValuePairs);
            try {
                JSONObject json_data = new JSONObject(strJson);
                if(json_data.getString("result").equals("Y")){
                    success = true;
                }
            } catch (JSONException e) {
            }
            if (success)
                return true;
            else
                return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            if (result) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(SpeedActivity.this);
                alt_bld.setMessage(serverList.get(idxConfirmServer).getName() + getString(R.string.serverchanged));
                alt_bld.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();
            }else{
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(SpeedActivity.this);
                alt_bld.setMessage(R.string.servernotchange);
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
            //	mImageView.scrollTo(mScrollStep++, 0);
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();

        }
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

}

