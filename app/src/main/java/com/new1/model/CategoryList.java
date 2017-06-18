package com.new1.model;

import android.graphics.Bitmap;
import android.widget.Button;

public class CategoryList {
	private String code;
	private String name;
	
	public CategoryList(String code, String name){
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
