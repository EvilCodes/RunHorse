package com.yujie.hero.tasks.login;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.UserBean;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void initUid();

        void checkOnClickButton();

        void getUserFromRemote(UserBean user);

        void showRequestResult(String requestResult);

        void showProgress();

        void dismissProgress();

        void checkEditText();

    }

    interface Presenter extends BasePresenter {

        void LoginTask(String username,String pwd);

        void checkEditText();

        void initUid();

        void checkOnClickButton();

        void addLoginUserToLocalData(UserBean user);







    }
}
