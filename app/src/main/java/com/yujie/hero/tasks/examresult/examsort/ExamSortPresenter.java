package com.yujie.hero.tasks.examresult.examsort;

import com.yujie.hero.data.bean.ExamResultBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ExamSortPresenter implements ExamSortContract.Presenter{

    private RemoteDataSource mRemoteDataSource;
    private ExamSortFragment mFragment;

    public ExamSortPresenter(RemoteDataSource mRemoteDataSource, ExamSortFragment mFragment) {

        this.mRemoteDataSource = mRemoteDataSource;
        this.mFragment = mFragment;
    }

    @Override
    public void initPersonOrChartData(String username, RemoteDataSource.LoadExerciseGradeCallback callback) {

        mRemoteDataSource.getNearlyExerciseGrades(username, callback);

    }

    @Override
    public void initData(String examId) {
        mRemoteDataSource.getExamGradeTask(examId, new RemoteDataSource.LoadExamGradeCallback() {
            @Override
            public void onExamGradeUpLoaded(Result result) {

            }

            @Override
            public void onExamGradeLoaded(ExamResultBean[] result) {
                if (result != null && result.length != 0) {
                    ArrayList<ExamResultBean> list = Utils.array2List(result);
                    mFragment.getExamGrade(list);
                } else {
                    mFragment.showToast("数据加载失败");

                }

            }

            @Override
            public void onExamGradeSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("数据加载失败");



            }
        });

    }

    @Override
    public void setListener() {
        mFragment.setListener();

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
    public void showToast(String msg) {
        mFragment.showToast(msg);

    }

    @Override
    public void resetViewPort() {
        mFragment.resetViewPort();
    }

    @Override
    public void prepareDateAnimation(ArrayList<ExerciseBean> personData) {
        mFragment.prepareDateAnimation(personData);
    }

}
