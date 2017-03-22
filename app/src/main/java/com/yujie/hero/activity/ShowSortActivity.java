package com.yujie.hero.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.yujie.hero.R;
import com.yujie.hero.fragment.SortClassFragment;
import com.yujie.hero.fragment.SortCourseFragment;
import com.yujie.hero.fragment.SortTimeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowSortActivity extends AppCompatActivity {
    public static final String TAG = ShowSortActivity.class.getSimpleName();
    @Bind(R.id.sortTab)
    PagerSlidingTabStrip sortTab;
    @Bind(R.id.sortViewPager)
    ViewPager sortViewPager;
    private Context mContext = ShowSortActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sort);
        ButterKnife.bind(this);
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPager();
    }

    private void initViewPager() {
        sortViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        sortTab.setViewPager(sortViewPager);
    }
}
