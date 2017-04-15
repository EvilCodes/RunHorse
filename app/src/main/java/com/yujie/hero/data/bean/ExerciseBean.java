package com.yujie.hero.data.bean;

public class ExerciseBean {
	private int id;
	private int grade;
	private String exe_tiem;
	private String user_name;
	private String course_id;
	private String b_class;
	private String start_time;


	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getExe_tiem() {
		return exe_tiem;
	}
	public void setExe_tiem(String exe_tiem) {
		this.exe_tiem = exe_tiem;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getB_class() {
		return b_class;
	}
	public void setB_class(String b_class) {
		this.b_class = b_class;
	}
	@Override
	public String toString() {
		return "ExerciseBean [id=" + id + ", grade=" + grade + ", exe_tiem="
				+ exe_tiem + ", user_name=" + user_name + ", course_id="
				+ course_id + ", b_class=" + b_class + ", start_time="
				+ start_time + "]";
	}
	public ExerciseBean(int id, int grade, String exe_tiem, String user_name,
			String course_id, String b_class, String start_time) {
		super();
		this.id = id;
		this.grade = grade;
		this.exe_tiem = exe_tiem;
		this.user_name = user_name;
		this.course_id = course_id;
		this.b_class = b_class;
		this.start_time = start_time;
	}
	public ExerciseBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
