package com.new1.model;

import android.graphics.Bitmap;
import android.widget.Button;

public class ShortChannelDateList {
	private String shortcut;
	private String pcode;
	private String ptitle;
	
	public ShortChannelDateList(String shortcut, String pcode, String ptitle){
		this.shortcut = shortcut;
		this.pcode = pcode;
		this.ptitle = ptitle;
	}

	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getPtitle() {
		return ptitle;
	}

	public void setPtitle(String ptitle) {
		this.ptitle = ptitle;
	}
	

	
	
}
