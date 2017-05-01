package com.yujie.hero.tasks.checkorder.sortclass;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;

import java.util.ArrayList;

/**
 * Created by BlackFox on 2017/5/1.
 */

public interface SortClassContract {
    interface View {
        void getData(ArrayList<ExerciseBean> list);

        void showToast(String msg);

        void setListener();

        void resetViewPort();

        void prepareDataAnimation(ArrayList<ExerciseBean> personData);

        void reset();

        void generateData();

        void initAdapter();


    }


    interface Presenter {
        void initNearlyGradeData(String userName, RemoteDataSource.LoadExerciseGradeCallback callback);

        void initData(String b_class);

        void setListener();

        void resetViewPort();

        void prepareDataAnimation(ArrayList<ExerciseBean> personData);

        void reset();

        void generateData();

        void initAdapter();

    }
}
