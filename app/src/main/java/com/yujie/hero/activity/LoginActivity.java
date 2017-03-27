package com.yujie.hero.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.application.I;
import com.yujie.hero.R;
import com.yujie.hero.bean.UserBean;
import com.yujie.hero.db.DataHelper;
import com.yujie.hero.utils.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    @Bind(R.id.login_activity_EditText_inputPhone)
    EditText loginActivityEditTextInputPhone;
    @Bind(R.id.login_activity_EditText_inputPwd)
    EditText loginActivityEditTextInputPwd;
    @Bind(R.id.login_activity_Button_login)
    Button loginActivityButtonLogin;
    @Bind(R.id.login_activity_Button_register)
    Button loginActivityButtonRegister;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        pd = new ProgressDialog(mContext);

    }

    /**
     * init uid
     */
    private void initUid() {
        UserBean currentUser = HeroApplication.getInstance().getCurrentUser();
        if (currentUser!=null){
            loginActivityEditTextInputPhone.setText(currentUser.getUid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUid();
    }

    @OnClick({R.id.login_activity_Button_login, R.id.login_activity_Button_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_activity_Button_login:
                Log.e(TAG, "login");
                login();
                break;
            case R.id.login_activity_Button_register:
                startActivity(new Intent(mContext,RegisterActivity.class));
                break;
        }
    }

    /**
     * execute login
     */
    private void login() {
        final String uid = loginActivityEditTextInputPhone.getText().toString();
        String pwd = loginActivityEditTextInputPwd.getText().toString();
        if (uid.isEmpty()){
            loginActivityEditTextInputPhone.setError("未输入内容,请重试");
            loginActivityEditTextInputPhone.requestFocus();
            return;
        }
        if (pwd.isEmpty()){
            loginActivityEditTextInputPwd.setError("未输入内容,请重试");
            loginActivityEditTextInputPwd.requestFocus();
            return;
        }
        OkHttpUtils<UserBean> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_LOGIN)
                .addParam(I.User.UID,uid)
                .addParam(I.User.PWD,pwd)
                .post()
                .targetClass(UserBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<UserBean>() {
                    @Override
                    public void onSuccess(UserBean result) {
                        if (result.getUid()==null){
                            Toast.makeText(LoginActivity.this,"登陆失败，请确认您的账号或密码",Toast.LENGTH_LONG).show();
                        }else {
                            if(new DataHelper(mContext).findUserByUid(uid)!=null){
                                if(updateData(result)){
                                    HeroApplication.getInstance().setCurrentUser(result);
                                    startActivity(new Intent(mContext,MainActivity.class));
                                    finish();
                                }
                            }else {
                                if(addData(result)){
                                    HeroApplication.getInstance().setCurrentUser(result);
                                    startActivity(new Intent(mContext,MainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * when sign in success,and the user is existed in database,update user's status to "1"
     * @param result
     * @return
     */
    private boolean updateData(UserBean result) {
        DataHelper helper = new DataHelper(mContext);
        return helper.updateStatus(1,result.getUser_name());
    }

    /**
     * when sign in success,add the user data to local database,and update the login status to "1"
     * @param result
     */
    private boolean addData(UserBean result) {
        DataHelper helper = new DataHelper(mContext);
        return helper.addUser(result,1);
    }
}
