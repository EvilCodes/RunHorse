package com.yujie.hero.tasks.checkorder.sortcourse;

import android.text.TextUtils;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;

/**
 * Created by BlackFox on 2017/5/1.
 */

public class SortCoursePresenter implements SortCourseContract.Presenter {
    SortCourseFragment mFragment;
    RemoteDataSource mRemoteDataSource;

    public SortCoursePresenter(SortCourseFragment fragment, RemoteDataSource remoteDataSource) {
        this.mFragment = fragment;
        this.mRemoteDataSource = remoteDataSource;
    }

    @Override
    public void setListener() {
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

    @Override
    public void initData(String courseId) {
        mRemoteDataSource.getExerciseGradeSortInCourse(courseId, new RemoteDataSource.LoadExerciseSortInCourseCallback() {
            @Override
            public void onExerciseSortInCourseLoaded(ExerciseBean[] result) {
                if (result != null && result.length != 0) {
                    mFragment.getData(Utils.array2List(result));
                } else {
                    mFragment.showToast("数据加载失败");

                }
            }

            @Override
            public void onExerciseSortInCourseSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅，请稍后再试");

            }
        });

    }

    @Override
    public void initNearlyGrades(String username, RemoteDataSource.LoadExerciseGradeCallback callback) {
        mRemoteDataSource.getNearlyExerciseGrades(username,callback);

    }
}
