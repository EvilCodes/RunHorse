package com.yujie.hero;

import android.app.Application;

import com.yujie.hero.bean.UserBean;

/**
 * Created by yujie on 16-9-7.
 */
public class HeroApplication extends Application {
    public static final String SERVER_ROOT = "http://192.168.10.9:8080/Hero/Server?";
    public static final String AVATAR_ROOT = "http://192.168.10.9:8080/Hero/avatar/";
    //    public static final String SERVER_ROOT = "http://10.0.2.2:8080/Hero/Server?";
//    public static final String AVATAR_ROOT = "http://10.0.2.2:8080/Hero/avatar/";
    public static final String EXERCISE_CODE = "10010";
    public static final String EXAM_CODE = "10011";

    public int getCURRENT_EXAM_ID() {
        return CURRENT_EXAM_ID;
    }

    public void setCURRENT_EXAM_ID(int CURRENT_EXAM_ID) {
        this.CURRENT_EXAM_ID = CURRENT_EXAM_ID;
    }

    private int CURRENT_EXAM_ID = 0;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * current user who signed in
     */
    private UserBean currentUser;

    public String getCurrentTestCourse() {
        return currentTestCourse;
    }

    public void setCurrentTestCourse(String currentTestCourse) {
        this.currentTestCourse = currentTestCourse;
    }

    private String currentTestCourse;
    private static HeroApplication instance = null;
     private HeroApplication(){}
     public static HeroApplication getInstance() {
        synchronized (HeroApplication.class) {
            if (instance == null) {
                instance = new HeroApplication();
            }
        }
     
        return instance;}

}
