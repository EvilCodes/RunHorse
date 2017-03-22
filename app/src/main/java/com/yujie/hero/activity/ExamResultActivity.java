package com.yujie.hero.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.astuetz.PagerSlidingTabStrip;
import com.yujie.hero.R;
import com.yujie.hero.fragment.ExamSortFragment;
import com.yujie.hero.fragment.SortAvgClassFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExamResultActivity extends AppCompatActivity {
    public static final String TAG = ExamResultActivity.class.getSimpleName();
    @Bind(R.id.examSortTab)
    PagerSlidingTabStrip examSortTab;
    @Bind(R.id.ExamsortViewPager)
    ViewPager ExamsortViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        ButterKnife.bind(this);
        initViewPager();
    }

    private void initViewPager() {
        ExamsortViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new ExamSortFragment();
                    case 1:
                        return new SortAvgClassFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0:
                        return "查看所有学生排行";
                    case 1:
                        return "查看参考班级排行";
                }
                return super.getPageTitle(position);
            }
        });
        examSortTab.setViewPager(ExamsortViewPager);
    }
}
