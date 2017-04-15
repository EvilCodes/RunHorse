package com.yujie.hero.data.bean;

public class ExamGradeAvgBean {
	private float avgGrade;
	private String class_name;
	private int class_id;

	public float getAvgGrade() {
		return avgGrade;
	}

	public void setAvgGrade(float avgGrade) {
		this.avgGrade = avgGrade;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public int getClass_id() {
		return class_id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	@Override
	public String toString() {
		return "ExamGradeAvgBean{" +
				"avgGrade=" + avgGrade +
				", class_name='" + class_name + '\'' +
				", class_id=" + class_id +
				'}';
	}

	public ExamGradeAvgBean() {
	}

	public ExamGradeAvgBean(float avgGrade, String class_name, int class_id) {

		this.avgGrade = avgGrade;
		this.class_name = class_name;
		this.class_id = class_id;
	}
}
