package com.yujie.hero.tasks.examresult;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface ExamResultContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter {
        void initViewPage(FragmentManager manager);
        void setViewPager();



    }
}
