package com.yujie.hero.tasks.examresult;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yujie.hero.tasks.examresult.examsort.ExamSortFragment;
import com.yujie.hero.tasks.examresult.sortavgclass.SortAvgFragment;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ExamResultPresenter implements ExamResultContract.Presenter{
    private ExamResultFragment mFragment;

    public ExamResultPresenter(ExamResultFragment mFragment) {
        this.mFragment = mFragment;
        mFragment.setPresenter(this);
    }

    @Override
    public void initViewPage(FragmentManager manager){
        mFragment.examsortViewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return new ExamSortFragment();
                    case 1:
                        return new SortAvgFragment();
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




    }

    @Override
    public void setViewPager() {
        mFragment.examSortTab.setViewPager(mFragment.examsortViewPager);

    }

    @Override
    public void start() {

    }
}
