package com.new1.model;


import android.os.Parcel;
import android.os.Parcelable;

public class DramaContent implements Parcelable {
	private String title;
	private String p_code;
	private String vod_type;
	private String vod_code;
	private String watch;
	//private Button btns;
	private String subid;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getP_code() {
		return p_code;
	}
	public void setP_code(String p_code) {
		this.p_code = p_code;
	}
	public String getVod_type() {
		return vod_type;
	}
	public void setVod_type(String vod_type) {
		this.vod_type = vod_type;
	}
	public String getVod_code() {
		return vod_code;
	}
	public void setVod_code(String vod_code) {
		this.vod_code = vod_code;
	}
	public String getWatch() {
		return watch;
	}
	public void setWatch(String watch) {
		this.watch = watch;
	}

	public DramaContent(){

	}
//	public Button getBtns() {
//		return btns;
//	}
//
//	public void setBtns(Button btns) {
//		this.btns = btns;
//	}

	public String getSubid() {
		return subid;
	}

	public void setSubid(String subid) {
		this.subid = subid;
	}

	public DramaContent(Parcel in) {
		readFromParcel(in);
	}


	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(title);
		dest.writeString(p_code);
		dest.writeString(vod_type);
		dest.writeString(vod_code);
		dest.writeString(watch);
		//dest.writeValue(btns);
		dest.writeString(subid);
	}

	private void readFromParcel(Parcel in){
		title = in.readString();
		p_code = in.readString();
		vod_type = in.readString();
		vod_code = in.readString();
		watch = in.readString();
		//btns = (Button)in.readValue(null);
		subid = in.readString();
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public DramaContent createFromParcel(Parcel in) {
			return new DramaContent(in);
		}

		public DramaContent[] newArray(int size) {
			return new DramaContent[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
