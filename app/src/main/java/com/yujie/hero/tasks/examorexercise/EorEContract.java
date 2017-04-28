package com.yujie.hero.tasks.examorexercise;

import android.content.DialogInterface;
import android.view.View;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.tasks.pwdsetting.PwdContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface EorEContract {
    interface View extends BaseView<Presenter> {
        void showRunHorse();
        void initTitle();
        void getWordContent(String contentTxt);
        void setTextChangedListener();
        void initCountDownTimer(long millisInFuture, long countDownInterval);
        void showDialog(String title, String message, DialogInterface.OnClickListener clickListener, DialogInterface.OnKeyListener keyListener);
        void showToast(String msg);



    }

    interface Presenter extends BasePresenter {
        void showRunHorse();
        void initTitle();

        //通过调用一方的相关方法才能是预设的调用逻辑具体运行起来
        void showWordContent(String course_simple_name);

        void setTextChangedListener();

        void uploadGrade(UserBean currentUser,  String speed);

        void addExerciseGreade(UserBean currentUser, String speed,String course_simple_name,int challengePoint);

        void addExamGrades( UserBean currentUser,  String speed,String course_simple_name);

        void initCountDownTimer(long millisInFuture, long countDownInterval);

        void showDialog(String title, String message, DialogInterface.OnClickListener clickListener, DialogInterface.OnKeyListener keyListener);

        void showToast(String msg);






    }
}
