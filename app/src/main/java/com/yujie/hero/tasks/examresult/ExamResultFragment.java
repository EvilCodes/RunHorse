package com.yujie.hero.tasks.examresult;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.yujie.hero.R;
import com.yujie.hero.fragment.ExamSortFragment;
import com.yujie.hero.fragment.SortAvgClassFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ExamResultFragment extends Fragment implements ExamResultContract.View {
    @Bind(R.id.examSortTab)
    PagerSlidingTabStrip examSortTab;
    @Bind(R.id.ExamsortViewPager)
    ViewPager examsortViewPager;
    private Context mContext;
    private ExamResultContract.Presenter mPresenter;
    private FragmentManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        manager = getActivity().getSupportFragmentManager();
        View view = inflater.from(mContext).inflate(R.layout.fragment_exam_result, container, false);
        ButterKnife.bind(this, view);
        mPresenter.initViewPage(manager);
        mPresenter.setViewPager();
        return view;
    }

    @Override
    public void setPresenter(ExamResultContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
