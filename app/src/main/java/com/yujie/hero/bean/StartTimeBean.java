package com.yujie.hero.bean;

public class StartTimeBean {
	private String start_time;

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	@Override
	public String toString() {
		return "StartTimeBean [start_time=" + start_time + "]";
	}

	public StartTimeBean(String start_time) {
		super();
		this.start_time = start_time;
	}

	public StartTimeBean() {
		super();
	}
	
}
