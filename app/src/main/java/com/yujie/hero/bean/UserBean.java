package com.yujie.hero.bean;

public class UserBean {
	private String uid;
	private String pwd;
	private String user_name;
	private int sex;
	private int b_class;
	private int top_grade;
	private String avatar;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getB_class() {
		return b_class;
	}
	public void setB_class(int b_class) {
		this.b_class = b_class;
	}
	public int getTop_grade() {
		return top_grade;
	}
	public void setTop_grade(int top_grade) {
		this.top_grade = top_grade;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	@Override
	public String toString() {
		return "UserBean [uid=" + uid + ", pwd=" + pwd + ", user_name="
				+ user_name + ", sex=" + sex + ", b_class=" + b_class
				+ ", top_grade=" + top_grade + ", avatar=" + avatar + "]";
	}
	public UserBean(String uid, String pwd, String user_name, int sex,
			int b_class, int top_grade, String avatar) {
		super();
		this.uid = uid;
		this.pwd = pwd;
		this.user_name = user_name;
		this.sex = sex;
		this.b_class = b_class;
		this.top_grade = top_grade;
		this.avatar = avatar;
	}
	public UserBean() {
		super();
	}
}
