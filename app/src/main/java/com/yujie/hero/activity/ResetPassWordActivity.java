package com.yujie.hero.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.hero.HeroApplication;
import com.yujie.hero.I;
import com.yujie.hero.R;
import com.yujie.hero.bean.Result;
import com.yujie.hero.utils.OkHttpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPassWordActivity extends AppCompatActivity {
    public static final String TAG = ResetPassWordActivity.class.getSimpleName();
    private Context mContext ;
    @Bind(R.id.resetPwd_one)
    EditText resetPwdOne;
    @Bind(R.id.resetPwd_two)
    EditText resetPwdTwo;
    @Bind(R.id.resetPwdBtn)
    Button resetPwdBtn;
    String pwd_1;
    String pwd_2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_word);
        ButterKnife.bind(this);
        mContext = ResetPassWordActivity.this;
    }

    @OnClick(R.id.resetPwdBtn)
    public void onClick() {
        pwd_1 = resetPwdOne.getText().toString();
        pwd_2 = resetPwdTwo.getText().toString();
        if (pwd_1.isEmpty()|pwd_2.isEmpty()){
            resetPwdTwo.setText("");
            resetPwdOne.setError("不能为空");
            resetPwdOne.requestFocus();
            return;
        }else {
            if (pwd_1.equals(pwd_2)){
                resetPwd();
            }else {
                Toast.makeText(ResetPassWordActivity.this,"两次输入密码不一致,请重新输入",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void resetPwd() {
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_UPDATE_USER)
                .addParam(I.User.UID,HeroApplication.getInstance().getCurrentUser().getUid())
                .addParam(I.User.USER_NAME,HeroApplication.getInstance().getCurrentUser().getUser_name())
                .addParam(I.User.PWD,pwd_2)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isFlag()){
                            HeroApplication.getInstance().setCurrentUser(null);
                            HeroApplication.getInstance().setCurrentTestCourse(null);
                            HeroApplication.getInstance().setCURRENT_EXAM_ID(0);
                            Toast.makeText(ResetPassWordActivity.this,"修改成功，请重新登陆",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(ResetPassWordActivity.this,"修改失败,请确认输入无误(arguementException:1001)"
                                    ,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ResetPassWordActivity.this,"网络不通畅，修改失败",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
