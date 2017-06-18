package com.new1.model;

import com.new1.listlive.ListLiveItemData;

import java.util.ArrayList;

public class LiveChannel {
	private String code;
	private String name;
	private ArrayList<ListLiveItemData> listLiveDataList[] = new ArrayList[4];

	public LiveChannel(String code, String name){
		this.code = code;
		this.name = name;
	}

	public ArrayList<ListLiveItemData>[] getListLiveDataList() {
		return listLiveDataList;
	}

	public void setListLiveDataList(ArrayList<ListLiveItemData>[] listLiveDataList) {
		this.listLiveDataList = listLiveDataList;
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
