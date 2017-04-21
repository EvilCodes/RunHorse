package com.yujie.hero.tasks.register;

import android.widget.Toast;

import com.yujie.hero.activity.*;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.application.I;
import com.yujie.hero.data.bean.AreasBean;
import com.yujie.hero.data.bean.ClassObj;
import com.yujie.hero.data.bean.CourseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.StartTimeBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class ReigisterPresenter implements RegisterContract.Presenter{

    private RemoteDataSource mRemoteDataSource;
    private RegisterFragment mFragment;

    public ReigisterPresenter(RemoteDataSource mRemoteDataSource, RegisterFragment mFragment) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mFragment = mFragment;
        mFragment.setPresenter(this);
    }

    @Override
    public void addLocalPreference() {

    }

    @Override
    public void checkOnClickButton() {
        mFragment.checkOnClickButton();

    }

    @Override
    public void goCourseTask() {
        mRemoteDataSource.getCourseTask(new RemoteDataSource.LoadCourseTaskCallback() {
            @Override
            public void onCourseLoaded(CourseBean[] result) {
                if (result != null && result.length != 0) {

                    mFragment.setCourseList(result);
                } else {
                    mFragment.showRequestResult("数据获取失败，请稍后再试");
                }

            }

            @Override
            public void onCourseSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showRequestResult("网络不通畅,请稍后再试");

            }
        });

    }

    @Override
    public void goStartTimeTask() {
        mRemoteDataSource.getStartTimeTask(new RemoteDataSource.LoadClassStartTimeCallback() {
            @Override
            public void onClassStartTimeLoaded(StartTimeBean[] result) {
                if (result != null && result.length != 0) {
                    mFragment.setStartTimeList(result);
                } else {
                    mFragment.showRequestResult("数据获取失败，请稍后再试");
                }
            }
            @Override
            public void onClassStartTimeSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showRequestResult("网络不通畅,请稍后再试");
            }
        });

    }

    @Override
    public void goAeraTask() {
        mRemoteDataSource.getAreasTask(new RemoteDataSource.LoadAreasTaskCallback() {
            @Override
            public void onAreasTaskLoaded(AreasBean[] result) {
                if (result != null && result.length != 0) {
                    mFragment.setAreaList(result);

                } else {
                    mFragment.showRequestResult("数据获取失败，请稍后再试");
                }

            }

            @Override
            public void onAreasTaskSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showRequestResult("网络不通畅,请稍后再试");


            }
        });

    }

    @Override
    public void goClassTask(String area, String course, String time) {
        mRemoteDataSource.getClassListTask(area, course, time, new RemoteDataSource.LoadClassesCallback() {
            @Override
            public void onClassesLoaded(ClassObj[] result) {
                if (result != null && result.length != 0) {
                    mFragment.setClassList(result);
                } else {
                    mFragment.showRequestResult("数据获取失败，请稍后再试");
                }
            }

            @Override
            public void onClassesSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showRequestResult("网络不通畅,请稍后再试");


            }
        });

    }

    @Override
    public void goRegister(final String uid, String pwd, String userName, String sex, String classId, String topGrade) {


        mRemoteDataSource.registerTask(uid, pwd, userName, sex, classId, topGrade, new RemoteDataSource.registerTaskCallback() {
            @Override
            public void onRegisterSuccess(Result result) {
                if (result != null&&result.isFlag()) {

                    mFragment.goRegister(result);
                    mFragment.showRequestResult("您的UID是: "+uid+" 已经为您自动填充");
                } else {
                    mFragment.showRequestResult("注册失败,该学号已被使用，请重新输入学号");
                }

            }
            @Override
            public void onDataNotAvailable() {
                mFragment.showRequestResult("网络不通畅,请稍后再试");

            }
        });


    }

    @Override
    public void showChoseClasses() {
        mFragment.showChoseDialog();
    }

    @Override
    public boolean checkPersonMessage() {
       return mFragment.checkPersonMessage();

    }


    @Override
    public void start() {

    }
}
