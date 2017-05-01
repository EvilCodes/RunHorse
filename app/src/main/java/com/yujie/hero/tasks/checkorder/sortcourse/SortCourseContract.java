package com.yujie.hero.tasks.checkorder.sortcourse;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;

import java.util.ArrayList;

/**
 * Created by BlackFox on 2017/5/1.
 */

public interface SortCourseContract {

    interface View {

        void setListener();

        void resetViewPort();

        void prepareDataAnimation(ArrayList<ExerciseBean> personData);

        void reset();

        void generateData();

        void initAdapter();

        void getData(ArrayList<ExerciseBean> grades);

        void showToast(String msg);

    }

    interface Presenter {
        void setListener();

        void resetViewPort();

        void prepareDataAnimation(ArrayList<ExerciseBean> personData);

        void reset();


        void generateData();

        void initAdapter();

        void initData(String courseId);

        void initNearlyGrades(String username, RemoteDataSource.LoadExerciseGradeCallback callback);



    }

}
