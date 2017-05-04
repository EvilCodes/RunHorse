package com.yujie.hero.tasks.splash;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yujie.hero.R;
import com.yujie.hero.data.application.HeroApplication;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.tasks.login.LoginActivity;
import com.yujie.hero.tasks.main.MainActivity;
import com.yujie.hero.utils.StartTargetActivity;

/**
 * Created by BlackFox on 2017/5/1.
 */

public class SplashFragment extends Fragment implements SplashContract.View{

    private Context mContext;
    private SplashPresenter mPresenter;
    private Handler mHandler;
    private static final int GO_LOGIN = 1001;
    private static final int GO_MAIN = 1002;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getContext();
        View view = inflater.from(mContext).inflate(R.layout.fragment_splash, container, false);
        mPresenter.initHandler();
        mPresenter.findLoginUser();
        return view;
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {

        mPresenter = (SplashPresenter) presenter;

    }

    @Override
    public void initHandler() {
         mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case GO_LOGIN:
                        mPresenter.goLogin();
                        break;
                    case GO_MAIN:
                        mPresenter.goMain();
                        break;
                }
            }
        };

    }

    @Override
    public void goMain() {
        StartTargetActivity.jumpToTargetActivity(mContext, MainActivity.class);
        getActivity().finish();


    }

    @Override
    public void goLogin() {
        StartTargetActivity.jumpToTargetActivity(mContext, LoginActivity.class);
        getActivity().finish();

    }



    @Override
    public void sendMessage(UserBean loginUser) {

        Log.e("SplashFragment", "sendMessage.loginUser=" + loginUser);

        if (loginUser!=null){
            mHandler.sendEmptyMessageDelayed(GO_MAIN,3000);
            HeroApplication.getInstance().setCurrentUser(loginUser);
        }else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN,3000);
        }



    }
}
