package com.yujie.hero.bean;

/**
 * Created by yujie on 16-9-16.
 */
public class ExamResultBean {
    private int exam_id;
    private String user_name;
    private int grade;
    private String submit_time;
    private String class_name;

    public ExamResultBean(int exam_id, String user_name, int grade, String submit_time, String class_name) {
        this.exam_id = exam_id;
        this.user_name = user_name;
        this.grade = grade;
        this.submit_time = submit_time;
        this.class_name = class_name;
    }

    public ExamResultBean() {

    }

    @Override
    public String toString() {
        return "ExamResultBean{" +
                "exam_id=" + exam_id +
                ", user_name='" + user_name + '\'' +
                ", grade=" + grade +
                ", submit_time='" + submit_time + '\'' +
                ", class_name='" + class_name + '\'' +
                '}';
    }

    public int getExam_id() {
        return exam_id;
    }

    public void setExam_id(int exam_id) {
        this.exam_id = exam_id;
    }

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

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
