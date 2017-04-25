package com.yujie.hero.tasks.pwdsetting;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.source.RemoteDataSource;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface PwdContract {
    interface View extends BaseView<Presenter>{

        void checkPassword();
        void confirmPassword();
        void goResetPassword(@NonNull Result result);
        void showRequestResult(String des);





    }

    interface Presenter extends BasePresenter {

        void checkPassword();
        void confirmPassword();
        void goResetPassword(@NonNull String pwd);

    }
}
