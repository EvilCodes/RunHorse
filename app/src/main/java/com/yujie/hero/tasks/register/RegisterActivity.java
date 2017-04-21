package com.yujie.hero.tasks.register;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yujie.hero.R;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;
import com.yujie.hero.utils.AddFragmentToActivity;
import com.yujie.hero.utils.Utils;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.frame_register);
        if (registerFragment == null) {
            registerFragment = RegisterFragment.newInstance();
        }
        Log.e("RegisterActivity", "registerFragment = " + registerFragment);
        new ReigisterPresenter(new TasksRemoteDataSource(),registerFragment);
        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(),
                R.id.frame_register,registerFragment);





    }

}
