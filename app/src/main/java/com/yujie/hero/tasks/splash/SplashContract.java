package com.yujie.hero.tasks.splash;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.tasks.checkorder.sorttime.SortTimeContract;

/**
 * Created by BlackFox on 2017/5/1.
 */

public interface SplashContract {

    interface View extends BaseView<Presenter> {

        void initHandler();
        void goMain();
        void goLogin();
        void sendMessage(UserBean loginUser);
    }

    interface Presenter extends BasePresenter {

        void findLoginUser();
        void initHandler();
        void goMain();
        void goLogin();


    }

}
