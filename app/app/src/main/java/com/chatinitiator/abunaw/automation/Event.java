package com.chatinitiator.abunaw.automation;

public class Event {
	private String title;
	private String time;
	private String frequency;
	private int flag;
	private String evntId;
	private String isActive;

	public Event(String title, String time,
				 String frequency,String id,String isActive) {
		this.title = title;
		this.time = time;
		this.frequency = frequency;
		//this.flag = flag;
		this.evntId = id;
		this.isActive = isActive;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getId() {
		return evntId;
	}

	public void setFlag(String id) {
		this.evntId = id;
	}

	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {this.isActive =  isActive; }


}