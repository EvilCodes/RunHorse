package com.yujie.hero.tasks.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.Injection;
import com.yujie.hero.R;
import com.yujie.hero.utils.AddFragmentToActivity;

/**
 * Created by BlackFox on 2017/5/1.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SplashFragment fragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.fmSplash);
        if (fragment == null) {
            fragment=new SplashFragment();
        }
        new SplashPresenter(Injection.provideTasksRepository(this), fragment);

        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(),R.id.fmSplash,fragment);

    }
}
