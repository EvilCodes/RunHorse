package com.yujie.hero.tasks.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yujie.hero.Injection;
import com.yujie.hero.R;
import com.yujie.hero.utils.AddFragmentToActivity;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
        if (loginFragment == null) {

            loginFragment=LoginFragment.newInstance();

        }
        //MVP三个主要部分的创建
        new LoginPresenter(Injection.provideTasksRepository(this), loginFragment);

        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(),R.id.login_fragment,loginFragment);


    }



}
