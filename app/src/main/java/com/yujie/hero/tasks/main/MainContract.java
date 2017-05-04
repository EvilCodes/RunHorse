package com.yujie.hero.tasks.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.ExamBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.WordContentBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void showToast(String msg);

        void setViewData();

        void initReceiver();

        void initNavigationView();

        void setListenner();

        void getData(ExerciseBean[] result);

        void getNearlyTenGrades(ExerciseBean[] result);

        void showAvatar(Bitmap result);

        void setClassName(String className);

        void getExamNow(ArrayList<ExamBean> nowExam);

        void getWordContent(ArrayList<WordContentBean> list);

        void generateValues();

        void generateData();

        void generateLineData();

        void generateColumnData();

        void initSpinner();

    }

    interface Presenter extends BasePresenter {
        void initSpinner();

        void generateValues();

        void generateData();

        void generateLineData();

        void generateColumnData();

        void initReceiver();

        void setViewData();

        void initNavigationView();

        void setListenner();

        void getTenGrades(String userName);

        void downLoadWordContent();

        void getExamNow();

        void showAvatar(String avatar);

        void initData(String userName);

        void getClassName(String examId);

        void addWordsToLocal(ArrayList<WordContentBean> list);

        void showExamDialog(Context context,String title,
                            DialogInterface.OnClickListener listener, String[] examArray);


        void showClassAndTime(Context context, String title, android.view.View view, DialogInterface.OnClickListener listener);

        void logout(Activity activity,Class<?> mClass);

        int getWordContent(String course_simpleName);
    }

}
