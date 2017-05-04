package com.yujie.hero.tasks.splash;

import android.util.Log;

import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.source.TasksDataSource;
import com.yujie.hero.data.source.TasksRepository;

/**
 * Created by BlackFox on 2017/5/1.
 */

public class SplashPresenter implements SplashContract.Presenter{

    private TasksRepository mTasksRepository;
    private SplashFragment mFragment;

    public SplashPresenter(TasksRepository mTasksRepository, SplashFragment mFragment) {
        this.mTasksRepository = mTasksRepository;
        this.mFragment = mFragment;
        mFragment.setPresenter(this);

    }

    @Override
    public void start() {

    }

    @Override
    public void findLoginUser() {

        mTasksRepository.getLoginTuserTask(new TasksDataSource.LoadTuserCallback() {
            @Override
            public void onUserBeanLoginLoaded(UserBean userBean) {
                Log.e("SplashPresenter", "userBean=" + userBean);
                mFragment.sendMessage(userBean);


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
                mFragment.sendMessage(null);

            }
        });

    }

    @Override
    public void initHandler() {
        mFragment.initHandler();

    }

    @Override
    public void goMain() {
        mFragment.goMain();

    }

    @Override
    public void goLogin() {
        mFragment.goLogin();

    }



}
