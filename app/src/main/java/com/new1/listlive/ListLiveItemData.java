package com.new1.listlive;



public class ListLiveItemData {
	private String title;
	private String hasVod;
	private String time;
	private String vodLink;
	public ListLiveItemData(String title, String hasVod, String time, String vodLink) {
		super();
		this.title = title;
		this.hasVod = hasVod;
		this.time = time;
		this.vodLink = vodLink;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHasVod() {
		return hasVod;
	}

	public void setHasVod(String hasVod) {
		this.hasVod = hasVod;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getVodLink() {
		return vodLink;
	}

	public void setVodLink(String vodLink) {
		this.vodLink = vodLink;
	}
}
