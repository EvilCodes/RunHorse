package com.yujie.hero.bean;

public class ExamGradeBean {
	private int id;
	private int exam_id;
	private int grade;
	private String submit_time;
	private String user_name;
	private int b_class;
	private String course_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getB_class() {
		return b_class;
	}
	public void setB_class(int b_class) {
		this.b_class = b_class;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	@Override
	public String toString() {
		return "ExamGradeBean [id=" + id + ", exam_id=" + exam_id + ", grade="
				+ grade + ", submit_time=" + submit_time + ", user_name="
				+ user_name + ", b_class=" + b_class + ", course_id="
				+ course_id + "]";
	}
	public ExamGradeBean(int id, int exam_id, int grade, String submit_time,
			String user_name, int b_class, String course_id) {
		super();
		this.id = id;
		this.exam_id = exam_id;
		this.grade = grade;
		this.submit_time = submit_time;
		this.user_name = user_name;
		this.b_class = b_class;
		this.course_id = course_id;
	}
	public ExamGradeBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
