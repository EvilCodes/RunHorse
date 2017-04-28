package com.yujie.hero.tasks.examresult.sortavgclass;

import com.squareup.okhttp.internal.Util;
import com.yujie.hero.data.bean.ExamClassGradeBean;
import com.yujie.hero.data.bean.ExamGradeAvgBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/28.
 */

public class SortAvgPresenter implements SortAvgContract.Presenter{
    private RemoteDataSource mRemoteDatSource;
    private SortAvgFragment mFragment;

    public SortAvgPresenter(RemoteDataSource mRemoteDatSource, SortAvgFragment mFragment) {
        this.mRemoteDatSource = mRemoteDatSource;
        this.mFragment = mFragment;
    }

    @Override
    public void showToast(String msg) {
        mFragment.showToast(msg);

    }

    @Override
    public void initStuGrades(int classId, String examId) {
        mRemoteDatSource.getClassGrade(examId, classId+"", new RemoteDataSource.LoadClassGradeCallback() {
            @Override
            public void onClassGradeLoaded(ExamClassGradeBean[] result) {
                if (result != null && result.length != 0) {
                    mFragment.getExamClass(Utils.array2List(result));

                }

            }
            @Override
            public void onGlassGradeSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅，请稍后再试");

            }
        });

    }

    @Override
    public void initClassData(String examId) {
        mRemoteDatSource.getExamAvgGrade(examId, new RemoteDataSource.LoadExamAvgGradeCallback() {
            @Override
            public void onExamAvgGradeLoaded(ExamGradeAvgBean[] result) {
                if (result != null && result.length != 0) {
                    mFragment.getClassAvgGrade(Utils.array2List(result));
                }

            }

            @Override
            public void onExamAvgGradeSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅，请稍候再试");

            }
        });

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
    public void generateInitialLineData() {
        mFragment.generateInitialLineData();

    }

    @Override
    public void generateColumnData() {
        mFragment.generateColumnData();

    }
}
