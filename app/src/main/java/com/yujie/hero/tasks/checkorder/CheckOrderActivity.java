package com.yujie.hero.tasks.checkorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.R;
import com.yujie.hero.utils.AddFragmentToActivity;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CheckOrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);

        CheckOrderFragment fragment = (CheckOrderFragment) getSupportFragmentManager().findFragmentById(R.id.fmCheckOrder);
        if (fragment == null) {
            fragment = new CheckOrderFragment();
        }
        new CheckOrderPresenter(fragment);
        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(),R.id.fmCheckOrder,fragment);
    }
}
