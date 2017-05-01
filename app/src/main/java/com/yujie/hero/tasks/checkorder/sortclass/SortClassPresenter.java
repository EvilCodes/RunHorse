package com.yujie.hero.tasks.checkorder.sortclass;

import android.util.Log;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;

/**
 * Created by BlackFox on 2017/5/1.
 */


public class SortClassPresenter implements SortClassContract.Presenter {
    private SortClassFragment mFragment;
    private RemoteDataSource mRemoteDataSource;

    public SortClassPresenter(SortClassFragment fragment, RemoteDataSource remoteDataSource) {
        this.mFragment = fragment;
        this.mRemoteDataSource = remoteDataSource;
    }


    @Override
    public void initNearlyGradeData(String userName, RemoteDataSource.LoadExerciseGradeCallback callback) {
        mRemoteDataSource.getNearlyExerciseGrades(userName, callback);

    }


    @Override
    public void initData(String b_class) {

            mRemoteDataSource.getExerciseGradeSortInClass(b_class, new RemoteDataSource.LoadExerciseSortInClassCallback() {
                @Override
                public void onExerciseSortInClass(ExerciseBean[] result) {

                    Log.e("SortClassPresenter","result="+result);
                    if (result != null && result.length != 0) {
                        mFragment.getData(Utils.array2List(result));

                    } else {
                        mFragment.showToast("未能获取到数据");
                    }

                }

                @Override
                public void onExerciseSortInClassSaved(boolean isSaved) {

                }

                @Override
                public void onDataNotAvailable() {
                    mFragment.showToast("网络不通畅,请稍后再试");


                }
            });

    }

    @Override
    public void setListener() {
        Log.e("SortClassPresent", "setListner.mFragment=" + mFragment);

            mFragment.setListener();

    }

    @Override
    public void resetViewPort() {

            mFragment.resetViewPort();

    }

    @Override
    public void prepareDataAnimation(ArrayList<ExerciseBean> personData) {

            mFragment.prepareDataAnimation(personData);

    }

    @Override
    public void reset() {

            mFragment.reset();

    }

    @Override
    public void generateData() {

            mFragment.generateData();

    }

    @Override
    public void initAdapter() {

            mFragment.initAdapter();

    }
}
