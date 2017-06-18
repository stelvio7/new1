package com.new1.settop;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

public class FullVideoView extends VideoView
{

	protected int overrideWidth = 480;
	protected int overrideHeight = 360;
	private boolean isChange = false;
	
	public FullVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
   		// TODO Auto-generated constructor stub
  	} 
  
  
  	@Override
  	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
  	{ 
        
        if(isChange){
        	Log.e(null, "전체보기");
        	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        else{
        	Log.e(null, "작게보기");
//        	//setMeasuredDimension(overrideWidth, overrideWidth );   
        	Display dis =((WindowManager)getContext().
                    getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//
			DisplayMetrics realMetrics = new DisplayMetrics();
			dis.getRealMetrics(realMetrics);
        	setMeasuredDimension(realMetrics.widthPixels, realMetrics.heightPixels );  
//        	
//        	
        }
  	} 
  	
  	
  	public void changeSize(int width, int height){
  		Log.e(null, "바꾸자");
  		isChange = true;
  		overrideWidth = width;
  		overrideWidth = height;
  		this.getHolder().setFixedSize(width, height);
  		requestLayout();
  	    invalidate();
  	  Log.e(null, "바꼈다");
  	}
}
