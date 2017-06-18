package com.new1.model;

import android.graphics.Bitmap;
import android.widget.LinearLayout;

public class BroadcastList {
	private String idx;
	private String vod_code;
	private String image;
	private String title;
	private String pu_no;
	private Bitmap bitmap;
	private String type;
	private LinearLayout btns;
	private String subid;
	
    private String genre;
    private String point;
    private String age;
    private String date;
    private String director;
    private String cast;
    private String story;
    private String code2;
    
    private String wIdx;

	private String nowTitle;
	private String nowTime;
	private String nextTitle;
	private String nextTime;

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}

	private boolean focused;

	public String getwIdx() {
		return wIdx;
	}
	public void setwIdx(String wIdx) {
		this.wIdx = wIdx;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getCast() {
		return cast;
	}
	public void setCast(String cast) {
		this.cast = cast;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public LinearLayout getBtns() {
		return btns;
	}
	public void setBtns(LinearLayout btns) {
		this.btns = btns;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getVod_code() {
		return vod_code;
	}
	public void setVod_code(String vod_code) {
		this.vod_code = vod_code;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPu_no() {
		return pu_no;
	}
	public void setPu_no(String pu_no) {
		this.pu_no = pu_no;
	}

	public String getNowTitle() {
		return nowTitle;
	}

	public void setNowTitle(String nowTitle) {
		this.nowTitle = nowTitle;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	public String getNextTitle() {
		return nextTitle;
	}

	public void setNextTitle(String nextTitle) {
		this.nextTitle = nextTitle;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public void recyleBitmap(){
		if(bitmap != null){
			this.bitmap.recycle();
			this.bitmap = null;
		}
	}
	
	
}
