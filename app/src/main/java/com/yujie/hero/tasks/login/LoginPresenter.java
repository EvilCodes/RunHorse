package com.yujie.hero.tasks.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.LogPrinter;

import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.TasksDataSource;
import com.yujie.hero.data.source.TasksRepository;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private static String TAG = LoginPresenter.class.getSimpleName();

    LoginContract.View mLoginFragment;
    TasksRepository mTasksRepository;


    public LoginPresenter(
            @NonNull TasksRepository tasksRepository,
            @NonNull LoginContract.View loginFragment) {
        this.mLoginFragment = loginFragment;
        this.mTasksRepository = tasksRepository;
        mLoginFragment.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void LoginTask(final String username, String pwd) {
        mTasksRepository.getNetWorkTuserTask(new TasksDataSource.LoadTuserCallback() {
            @Override
            public void onUserBeanLoginLoaded(UserBean userBeen) {
                if (userBeen != null && userBeen.getUid() != null) {
                    Log.e(TAG, "userBean=" + userBeen);
                    mLoginFragment.getUserFromRemote(userBeen);

                } else {
                    mLoginFragment.showRequestResult("登陆失败，请确认您的账号或密码");
                }
            }

            @Override
            public void onUserBeanLoadedByUid(UserBean userBeen) {
            }

            @Override
            public void onUserBeanSaved(boolean isSaved) {

            }

            @Override
            public void onUserBeanUpdated(boolean isUpdated) {

            }

            @Override
            public void onStatusUpdated(boolean isStatusUpdated) {

            }

            @Override
            public void onDataNotAvailable() {
                mLoginFragment.showRequestResult("网络不通畅,请稍后再试");
            }
        }, username, pwd);

    }

    @Override
    public void checkEditText() {
        mLoginFragment.checkEditText();

    }

    @Override
    public void initUid() {
        mLoginFragment.initUid();

    }

    @Override
    public void checkOnClickButton() {
        mLoginFragment.checkOnClickButton();

    }

    @Override
    public void addLoginUserToLocalData(UserBean user, int status) {

        mTasksRepository.saveTuserTask(user, status, null);

    }


}
