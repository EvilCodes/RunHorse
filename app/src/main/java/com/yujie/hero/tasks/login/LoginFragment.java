package com.yujie.hero.tasks.login;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.activity.MainActivity;
import com.yujie.hero.activity.RegisterActivity;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.utils.StartTargetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class LoginFragment extends Fragment implements LoginContract.View, View.OnClickListener {

    private LoginContract.Presenter mPresenter;
    private Context context;
    private ProgressDialog pd;
    private String userName;
    private String pwd;
    private SharedPreferences sharedPreferences;

    @Bind(R.id.login_activity_EditText_inputPhone)
    EditText loginActivityEditTextInputPhone;
    @Bind(R.id.login_activity_EditText_inputPwd)
    EditText loginActivityEditTextInputPwd;
    @Bind(R.id.login_activity_Button_login)
    Button loginActivityButtonLogin;
    @Bind(R.id.login_activity_Button_register)
    Button loginActivityButtonRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        mPresenter.checkOnClickButton();
        initData();
        return view;
    }

    private void initData() {
        context = getContext();
        pd = new ProgressDialog(context);
        sharedPreferences = context.getSharedPreferences("user_name", Context.MODE_PRIVATE);
    }
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.initUid();
    }

    @Override
    public void initUid() {

        String userName = sharedPreferences.getString("userName", null);

        if (userName != null) {
            loginActivityEditTextInputPhone.setText(userName);
        }


    }

    @Override
    public void checkOnClickButton() {

        loginActivityButtonLogin.setOnClickListener(this);

        loginActivityButtonRegister.setOnClickListener(this);


    }

    @Override
    public void getUserFromRemote(UserBean user) {

        HeroApplication.getInstance().setCurrentUser(user);
        mPresenter.addLoginUserToLocalData(user,1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", user.getUid());
        StartTargetActivity.jumpToTargetActivity(context, MainActivity.class);

    }

    @Override
    public void showRequestResult(String requestResult) {

        Toast.makeText(context, requestResult, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void showProgress() {
        pd.setMessage("加载中。。。");
        pd.show();

    }

    @Override
    public void dismissProgress() {
        pd.dismiss();

    }

    @Override
    public void checkEditText() {

        userName = loginActivityEditTextInputPhone.getText().toString();
        pwd = loginActivityEditTextInputPwd.getText().toString();
        if (userName.isEmpty()) {
            loginActivityEditTextInputPhone.setError("请输入用户名");
            loginActivityEditTextInputPhone.requestFocus();
        }
        if (pwd.isEmpty()) {
            loginActivityEditTextInputPwd.setError("请输入用户密码");
            loginActivityEditTextInputPwd.requestFocus();
        }


    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

        this.mPresenter = presenter;


    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
        pd.cancel();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_activity_Button_login:
                Log.e("LoginFragment", "login_button is clicked");
                mPresenter.checkEditText();
                mPresenter.LoginTask(userName,pwd);
                break;

            case R.id.login_activity_Button_register:

                Log.e("LoginFragment", "register_button is clicked");

                StartTargetActivity.jumpToTargetActivity(getActivity(), RegisterActivity.class);

                break;
        }

    }

    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

}
