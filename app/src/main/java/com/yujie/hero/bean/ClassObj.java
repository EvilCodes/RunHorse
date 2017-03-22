package com.yujie.hero.bean;
public class ClassObj {
	private int id;
	private String class_name;
	private String b_area;
	private String b_course;
	private String start_time;
	private int class_number;
	private String simple_name;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getB_area() {
		return b_area;
	}
	public void setB_area(String b_area) {
		this.b_area = b_area;
	}
	public String getB_course() {
		return b_course;
	}
	public void setB_course(String b_course) {
		this.b_course = b_course;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public int getClass_number() {
		return class_number;
	}
	public void setClass_number(int class_number) {
		this.class_number = class_number;
	}
	public String getSimple_name() {
		return simple_name;
	}
	public void setSimple_name(String simple_name) {
		this.simple_name = simple_name;
	}
	@Override
	public String toString() {
		return "ClassObj [id=" + id + ", class_name=" + class_name
				+ ", b_area=" + b_area + ", b_course=" + b_course
				+ ", start_time=" + start_time + ", class_number="
				+ class_number + ", simple_name=" + simple_name + "]";
	}
	public ClassObj(int id, String class_name, String b_area, String b_course,
			String start_time, int class_number, String simple_name) {
		super();
		this.id = id;
		this.class_name = class_name;
		this.b_area = b_area;
		this.b_course = b_course;
		this.start_time = start_time;
		this.class_number = class_number;
		this.simple_name = simple_name;
	}
	public ClassObj() {
		super();
	}
}
