package com.yujie.hero.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AddFragmentToActivity {
    public static void addFragmentToActivity(FragmentManager manager, int layoutId, Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.commit();


    }
}
