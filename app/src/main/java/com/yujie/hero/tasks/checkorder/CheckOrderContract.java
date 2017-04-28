package com.yujie.hero.tasks.checkorder;

import android.support.v4.app.FragmentManager;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface CheckOrderContract {

    interface View extends BaseView<Presenter> {




    }
    interface Presenter extends BasePresenter {
        void initViewPager(FragmentManager manager);

        void setViewPager();

    }


}
