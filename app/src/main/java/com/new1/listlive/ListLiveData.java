package com.new1.listlive;

import java.util.ArrayList;

public class ListLiveData {
	private String titleImage;
	private ArrayList<ListLiveItemData> listItem;

	public ListLiveData(String titleImage, ArrayList<ListLiveItemData> listItem) {
		super();
		this.titleImage = titleImage;
		this.listItem = listItem;
	}

	public String getTitleImage() {
		return titleImage;
	}

	public void setTitleImage(String titleImage) {
		this.titleImage = titleImage;
	}

	public ArrayList<ListLiveItemData> getListItem() {
		return listItem;
	}

	public void setListItem(ArrayList<ListLiveItemData> listItem) {
		this.listItem = listItem;
	}
}
