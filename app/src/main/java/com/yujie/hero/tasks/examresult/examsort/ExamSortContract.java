package com.yujie.hero.tasks.examresult.examsort;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.ExamResultBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.OkHttpUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface ExamSortContract {

    interface View  {

        void getExamGrade(ArrayList<ExamResultBean> grades);

        void setListener();

        void reset();

        void generateData();

        void initAdapter();

        void resetViewPort();

        void prepareDateAnimation(ArrayList<ExerciseBean> personData);

        void showToast(String msg);


    }

    interface Presenter {
        void initPersonOrChartData(String username,RemoteDataSource.LoadExerciseGradeCallback listener);

        void initData(String examId);

        void setListener();

        void reset();

        void generateData();

        void initAdapter();

        void showToast(String msg);

        void resetViewPort();

        void prepareDateAnimation(ArrayList<ExerciseBean> personData);



    }

}
