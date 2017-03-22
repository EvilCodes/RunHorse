package com.yujie.hero.bean;

public class CourseBean {
	private String simple_name;
	private String course_name;
	public String getSimple_name() {
		return simple_name;
	}
	public void setSimple_name(String simple_name) {
		this.simple_name = simple_name;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	@Override
	public String toString() {
		return "CourseBean [simple_name=" + simple_name + ", course_name="
				+ course_name + "]";
	}
	public CourseBean(String simple_name, String course_name) {
		super();
		this.simple_name = simple_name;
		this.course_name = course_name;
	}
	public CourseBean() {
		super();
	}
	
}
