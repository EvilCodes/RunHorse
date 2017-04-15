package com.yujie.hero.data.bean;

public class AreasBean {
	private String simple_name;
	private String area_name;
	public String getSimple_name() {
		return simple_name;
	}
	public void setSimple_name(String simple_name) {
		this.simple_name = simple_name;
	}
	public String getArea_name() {
		return area_name;
	}
	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	@Override
	public String toString() {
		return "AreasBean [simple_name=" + simple_name + ", area_name="
				+ area_name + "]";
	}
	public AreasBean(String simple_name, String area_name) {
		super();
		this.simple_name = simple_name;
		this.area_name = area_name;
	}
	public AreasBean() {
		super();
	}
}
