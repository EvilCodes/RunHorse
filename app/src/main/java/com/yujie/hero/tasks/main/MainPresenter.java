package com.yujie.hero.tasks.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.yujie.hero.data.application.HeroApplication;
import com.yujie.hero.data.application.I;
import com.yujie.hero.data.bean.ExamBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.local.TasksLocalDataSource;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.StartTargetActivity;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/24.
 */

public class MainPresenter implements MainContract.Presenter {

    private RemoteDataSource mRemoteDataSource;
    private MainFragment mFragment;
    private TasksLocalDataSource mLocalDataSource;

    public MainPresenter(RemoteDataSource mRemoteDataSource, MainFragment mFragment, TasksLocalDataSource mLocalDataSource) {

        this.mRemoteDataSource = mRemoteDataSource;
        this.mFragment = mFragment;
        this.mLocalDataSource = mLocalDataSource;
        mFragment.setPresenter(this);
    }

    @Override
    public void initSpinner() {
        mFragment.initSpinner();
    }

    @Override
    public void generateValues() {
        mFragment.generateValues();

    }

    @Override
    public void generateData() {
        mFragment.generateData();

    }

    @Override
    public void generateLineData() {
        mFragment.generateLineData();

    }

    @Override
    public void generateColumnData() {
        mFragment.generateColumnData();

    }

    @Override
    public void initReceiver() {
        mFragment.initReceiver();
    }

    @Override
    public void setViewData() {
        mFragment.setViewData();

    }

    @Override
    public void initNavigationView() {
        mFragment.initNavigationView();

    }

    @Override
    public void setListenner() {
        mFragment.setListenner();

    }

    @Override
    public void getTenGrades(String userName) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_TEN_GRADES)
                .addParam(I.User.USER_NAME, userName)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        Log.e("MainPresenter", "getTenGrades.result=" + result.toString());
                        mFragment.getNearlyTenGrades(result);
                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络不通畅，请稍后再试");
                    }
                });

    }

    @Override
    public void downLoadWordContent() {
        OkHttpUtils<WordContentBean[]> utils = new OkHttpUtils<WordContentBean[]>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_DOWNLOAD_CONTENT)
                .targetClass(WordContentBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<WordContentBean[]>() {
                    @Override
                    public void onSuccess(WordContentBean[] result) {
                        if (result != null & result.length != 0) {
                            mFragment.getWordContent(Utils.array2List(result));
                        }

                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络不通畅，请稍后再试");

                    }
                });

    }

    @Override
    public void getExamNow() {
        OkHttpUtils<ExamBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_EXAM_NOW)
                .targetClass(ExamBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamBean[]>() {
                    @Override
                    public void onSuccess(ExamBean[] result) {
                        if (result != null & result.length > 0) {
                            mFragment.getExamNow(Utils.array2List(result));

                        } else {
                            mFragment.showToast("当前没有正在进行的考试");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络不通畅,请稍后再试");
                    }
                });


    }

    @Override
    public void showAvatar(String url) {
        OkHttpUtils<Bitmap> utils = new OkHttpUtils<>();
        utils.url(url)
                .downloadFile()
                .execute(new OkHttpUtils.OnCompleteListener<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result) {
                        mFragment.showAvatar(result);
                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络加载失败，请稍后再试");

                    }
                });


    }

    @Override
    public void initData(String userName) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GETNEARLYGRADES)
                .addParam(I.User.USER_NAME, userName)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        Log.e("MainPresenter", "MainPresenter.result=" + result.toString());
                        mFragment.getData(result);
                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络不通畅，请稍后再试");

                    }
                });

    }

    @Override
    public void getClassName(String examId) {
        OkHttpUtils<String> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GETCLASSNAME)
                .addParam(I.Exam.ID, examId)
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null) {

                            mFragment.setClassName(result);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        mFragment.showToast("网络不通畅，请稍后再试");

                    }
                });


    }

    @Override
    public void addWordsToLocal(ArrayList<WordContentBean> list) {
        mLocalDataSource.saveWordWordContensTask(list, null);

    }

    @Override
    public void showExamDialog(Context context, String title, DialogInterface.OnClickListener listener, String[] examArray) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(examArray, listener)
                .create();
        dialog.show();

    }

    @Override
    public void showClassAndTime(Context context, String title, View view, DialogInterface.OnClickListener listener) {
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setNegativeButton("OK", listener)
                .create();
        dialog.show();

    }

    @Override
    public void logout(Activity activity, Class<?> mClass) {
        mLocalDataSource.cleanData("t_user");
        HeroApplication.getInstance().setCURRENT_EXAM_ID(0);
        HeroApplication.getInstance().setCurrentTestCourse(null);
        HeroApplication.getInstance().setCurrentUser(null);
        StartTargetActivity.jumpToTargetActivity(activity, mClass);
        activity.finish();
    }

    @Override
    public int getWordContent(String course_simpleName) {

        return mLocalDataSource.getWordCount(course_simpleName);
    }

    @Override
    public void start() {

    }
}
