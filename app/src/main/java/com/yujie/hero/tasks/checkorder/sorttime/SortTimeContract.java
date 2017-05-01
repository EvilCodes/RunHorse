package com.yujie.hero.tasks.checkorder.sorttime;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;

import java.util.ArrayList;

/**
 * Created by BlackFox on 2017/5/1.
 */

public interface SortTimeContract {

    interface View {
        void showToast(String msg);
        void getData(ArrayList<ExerciseBean> grades);
        void setListener();
        void resetViewPort();
        void prepareDataAnimation(ArrayList<ExerciseBean> personData);
        void reset();
        void generateData();
        void initAdapter();

    }

    interface Presenter {

        void initData(String startTime);
        void initNearlyGrades(String username, RemoteDataSource.LoadExerciseGradeCallback callback);
        void setListener();
        void resetViewPort();
        void prepareDataAnimation(ArrayList<ExerciseBean> personData);
        void reset();
        void generateData();
        void initAdapter();







    }



}
