package com.yujie.hero.bean;

/**
 * Created by yujie on 16-9-12.
 */
public class WordContentBean {
    private int id;
    private String word;
    private String course_id;

    @Override
    public String toString() {
        return "WordContentBean{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", course_id='" + course_id + '\'' +
                '}';
    }

    public WordContentBean() {
    }

    public WordContentBean(int id, String word, String course_id) {
        this.id = id;
        this.word = word;
        this.course_id = course_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }
}
