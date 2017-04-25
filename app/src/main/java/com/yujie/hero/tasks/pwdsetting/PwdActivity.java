package com.yujie.hero.tasks.pwdsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.R;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;
import com.yujie.hero.utils.AddFragmentToActivity;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PwdActivity extends AppCompatActivity {
    private PwdFragment mFragment;
    private RemoteDataSource mRemoteDataSource;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass_word);
        initData();

    }

    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        mFragment = (PwdFragment) mFragmentManager.findFragmentById(R.id.fMResetPwd);
        if (mFragment == null) {
            mFragment = new PwdFragment();

        }
        mRemoteDataSource = TasksRemoteDataSource.getInstance();
        new PwdPresenter(mRemoteDataSource, mFragment);
        AddFragmentToActivity.addFragmentToActivity(mFragmentManager,
                R.id.fMResetPwd,mFragment);




    }
}
