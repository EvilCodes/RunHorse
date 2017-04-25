package com.yujie.hero.tasks.pwdsetting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.tasks.login.LoginActivity;
import com.yujie.hero.utils.StartTargetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PwdFragment extends Fragment implements PwdContract.View ,View.OnClickListener{

    @Bind(R.id.resetPwd_one)
    EditText resetPwdOne;
    @Bind(R.id.resetPwd_two)
    EditText resetPwdTwo;
    @Bind(R.id.resetPwdBtn)
    Button resetPwdBtn;
    private Context context;
    private PwdContract.Presenter mPresenter;
    private String pwd,confirmPwd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_reset_pass_word, container, false);
        ButterKnife.bind(this, view);
        mPresenter.confirmPassword();
        return view;

    }



    private void initData() {
        pwd = resetPwdOne.getText().toString().trim();
        Log.e("initData", "pwd=" + pwd);
        confirmPwd = resetPwdTwo.getText().toString().trim();

    }

    @Override
    public void checkPassword() {

        if (pwd.isEmpty()) {
            resetPwdOne.setError("密码不能为空");
            resetPwdOne.requestFocus();

        } else if (confirmPwd.isEmpty()) {
            resetPwdTwo.setError("请输入密码");
            resetPwdTwo.requestFocus();
        }


    }

    @Override
    public void confirmPassword() {
        resetPwdBtn.setOnClickListener(this);
    }

    @Override
    public void goResetPassword(@NonNull Result result) {
        HeroApplication.getInstance().setCurrentUser(null);
        HeroApplication.getInstance().setCurrentTestCourse(null);
        HeroApplication.getInstance().setCURRENT_EXAM_ID(0);
        StartTargetActivity.jumpToTargetActivity(context, LoginActivity.class);
        getActivity().finish();



    }

    @Override
    public void showRequestResult(String des) {
        Toast.makeText(context, des, Toast.LENGTH_LONG).show();

    }

    @Override
    public void setPresenter(PwdContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {

        initData();
        mPresenter.checkPassword();

        if (v.getId() == R.id.resetPwdBtn && pwd.equals(confirmPwd)) {

            Log.e("PwdFragment", "pwd=" + pwd);

            mPresenter.goResetPassword(pwd);

        } else if (!pwd.isEmpty()&&!confirmPwd.isEmpty()){
            showRequestResult("两次输入密码不一致,请重新输入");
        }

    }
}
