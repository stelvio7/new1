package com.new1.settop;

import com.noh.util.ImageDownloader;
import com.noh.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class Detail2DialogActivity extends Dialog {

    //private TextView mContentView;
    // private Button mLeftButton;
    // private Button mRightButton;
    // private Button mXButton;
    // private String mTitle;
    // private String mContent;

    //private View.OnClickListener mLeftClickListener;
    //private View.OnClickListener mRightClickListener;
    private View.OnKeyListener mXClickPlayListener;
    private View.OnClickListener mClickPlayListener;
    private View.OnKeyListener mXClickRestoreListener;
    private View.OnClickListener mClickRestoreListener;
    private Dialog.OnKeyListener mKeyAdListener;
    private View.OnClickListener mClickAdListener;
    private View.OnClickListener mClickAd2Listener;
    private Button movieadd;
    private Button movieplay;

    private ImageView adLeft;
    private ImageView adRight;
    private String type;
    private Context mContext;
    private String adImage;
    private String adImage2;

    private final ImageDownloader imageDownloader = new ImageDownloader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        if (type.equals("delete"))
            setContentView(R.layout.dialogpopupdelete);
        else
            setContentView(R.layout.dialogpopupmovie);


        setLayout();
        setClickListener(mXClickPlayListener, mClickPlayListener, mXClickRestoreListener, mClickRestoreListener, mKeyAdListener, mClickAdListener, mClickAd2Listener);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(adImage, adLeft, Util.getImageLoaderOption(), new SimpleImageLoadingListener() {
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

        ImageLoader imageLoader2 = ImageLoader.getInstance();
        imageLoader2.displayImage(adImage2, adRight, Util.getImageLoaderOption(), new SimpleImageLoadingListener() {
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


    public Detail2DialogActivity(Context context,
                                 View.OnClickListener mClickPlayListener, View.OnKeyListener mXClickPlayListener, View.OnClickListener mClickRestoreListener, View.OnKeyListener mXClickRestoreListener,
                                 View.OnClickListener mClickAdListener, Dialog.OnKeyListener mKeyAdListener, View.OnClickListener mClickAd2Listener, String type, String adImage, String adImage2) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mClickPlayListener = mClickPlayListener;
        this.mXClickPlayListener = mXClickPlayListener;
        this.mClickRestoreListener = mClickRestoreListener;
        this.mXClickRestoreListener = mXClickRestoreListener;
        this.mKeyAdListener = mKeyAdListener;
        this.mClickAdListener = mClickAdListener;
        this.mClickAd2Listener = mClickAd2Listener;
        this.type = type;
        this.mContext = context;
        this.adImage = adImage;
        this.adImage2 = adImage2;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {

            return false;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setClickListener(View.OnKeyListener x, View.OnClickListener x2, View.OnKeyListener x3, View.OnClickListener x4, Dialog.OnKeyListener x5, View.OnClickListener x6, View.OnClickListener x7) {
        movieplay.setOnKeyListener(x);
        movieplay.setOnClickListener(x2);
        movieadd.setOnKeyListener(x3);
        movieadd.setOnClickListener(x4);
        setOnKeyListener(x5);
        adLeft.setOnClickListener(x6);
        adRight.setOnClickListener(x7);
    }

    /*
     * Layout
     */
    private void setLayout() {

        movieplay = (Button) findViewById(R.id.movieplay);
        movieadd = (Button) findViewById(R.id.movieadd);
        adLeft = (ImageView) findViewById(R.id.adLeft);
        adRight = (ImageView) findViewById(R.id.adRight);

    }

}
