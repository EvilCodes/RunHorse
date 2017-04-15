package com.yujie.hero.data.bean;

public class ExamClassGradeBean {
	private String user_name;
	private int grade;
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		return "ExamClassGradeBean [user_name=" + user_name + ", grade="
				+ grade + "]";
	}
	public ExamClassGradeBean(String user_name, int grade) {
		super();
		this.user_name = user_name;
		this.grade = grade;
	}
	public ExamClassGradeBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
