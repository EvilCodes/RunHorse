package com.yujie.hero.tasks.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.R;
import com.yujie.hero.data.source.local.TasksLocalDataSource;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;
import com.yujie.hero.utils.AddFragmentToActivity;

/**
 * Created by Administrator on 2017/4/24.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment fragment= (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fmMain);
        if (fragment == null) {
            fragment = new MainFragment();
        }
        new MainPresenter(TasksRemoteDataSource.getInstance(), fragment, TasksLocalDataSource.getInstance(this));

        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(), R.id.fmMain, fragment);

    }
}
