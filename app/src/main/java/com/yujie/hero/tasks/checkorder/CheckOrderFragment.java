package com.yujie.hero.tasks.checkorder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.yujie.hero.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CheckOrderFragment extends Fragment implements CheckOrderContract.View {
    @Bind(R.id.sortTab)
    PagerSlidingTabStrip sortTab;
    @Bind(R.id.sortViewPager)
    ViewPager sortViewPager;
    private Context context ;
    CheckOrderContract.Presenter mPresenter;

    @Nullable
    @Override 
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.from(context).inflate(R.layout.fragment_show_sort, container, false);
        ButterKnife.bind(this, view);
        mPresenter.initViewPager(getActivity().getSupportFragmentManager());
        mPresenter.setViewPager();
        return view;
    }

    @Override
    public void setPresenter(CheckOrderContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
