package com.yujie.hero.tasks.checkorder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yujie.hero.fragment.SortClassFragment;
import com.yujie.hero.fragment.SortCourseFragment;
import com.yujie.hero.fragment.SortTimeFragment;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CheckOrderPresenter implements CheckOrderContract.Presenter{
    CheckOrderFragment mFragment;

    public CheckOrderPresenter(CheckOrderFragment mFragment) {
        this.mFragment = mFragment;
        mFragment.setPresenter(this);
    }

    @Override
    public void initViewPager(FragmentManager manager) {
        mFragment.sortViewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new SortClassFragment();
                    case 1:
                        return new SortCourseFragment();
                    case 2:
                        return new SortTimeFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "查看班级排行";
                    case 1:
                        return "查看学科排行";
                    case 2:
                        return "查看同期排行";
                }
                return null;
            }

        });

    }

    @Override
    public void setViewPager() {
        mFragment.sortTab.setViewPager(mFragment.sortViewPager);


    }

    @Override
    public void start() {

    }
}
