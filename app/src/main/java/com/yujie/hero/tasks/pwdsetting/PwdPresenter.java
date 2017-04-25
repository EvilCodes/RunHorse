package com.yujie.hero.tasks.pwdsetting;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.source.RemoteDataSource;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PwdPresenter implements PwdContract.Presenter{

    RemoteDataSource mRemoteDataSource;
    PwdFragment mFragment;

    public PwdPresenter(RemoteDataSource mRemoteDataSource, PwdFragment mFragment) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mFragment = mFragment;
        mFragment.setPresenter(this);
    }

    @Override
    public void checkPassword() {
        mFragment.checkPassword();

    }

    @Override
    public void confirmPassword() {
        mFragment.confirmPassword();

    }

    @Override
    public void goResetPassword(@NonNull String pwd) {
        if (!pwd.isEmpty()) {
            Log.e("PwdPresenter", "goResetPassword.begin");
            mRemoteDataSource.updateTuserPasswordTask(pwd, new RemoteDataSource.LoadTuserCallback() {
                @Override
                public void onUserBeanLoginLoaded(UserBean userBeen) {

                }

                @Override
                public void onUserBeanLoadedByUid(UserBean userBeen) {

                }

                @Override
                public void onUserBeanSaved(boolean isSaved) {

                }

                @Override
                public void onTuserPasswordUpdated(Result result) {
                    if (result != null && result.isFlag()) {

                        mFragment.goResetPassword(result);
                        mFragment.showRequestResult("修改成功，请重新登陆");
                    } else {
                        mFragment.showRequestResult("修改失败,请确认输入无误(arguementException:1001)");
                    }

                }

                @Override
                public void onDataNotAvailable() {
                    mFragment.showRequestResult("网络不通畅，修改失败");

                }
            })  ;
        }


    }

    @Override
    public void start() {

    }
}
