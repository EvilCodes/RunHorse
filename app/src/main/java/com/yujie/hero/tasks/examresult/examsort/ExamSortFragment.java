package com.yujie.hero.tasks.examresult.examsort;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.ExamResultBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ExamSortFragment extends Fragment implements ExamSortContract.View {
    @Bind(R.id.dataRecyclerView)
    RecyclerView dataRecyclerView;
    @Bind(R.id.gradeOfUser)
    TextView gradeOfUser;
    @Bind(R.id.dataLineChartView)
    LineChartView dataLineChartView;
    private Context mContext;
    private ExamSortContract.Presenter mPresenter;
    LineChartData data;
    ArrayList<ExamResultBean> grades;
    private LinearLayoutManager manager;
    private ExamGradeAdapter adapter;

    private int numberOfLines = 1;
    private int numberOfPoints = 0;


    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    ArrayList<ExerciseBean> personData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.from(mContext).inflate(R.layout.fragment_exam_sort, container, false);
        mPresenter = new ExamSortPresenter(new TasksRemoteDataSource(), this);
        ButterKnife.bind(this, view);
        mPresenter.initData(HeroApplication.getInstance().getCURRENT_EXAM_ID() + "");
        mPresenter.setListener();
        mPresenter.resetViewPort();
        return view;
    }


    @Override
    public void getExamGrade(ArrayList<ExamResultBean> grades) {
        adapter = new ExamGradeAdapter(mContext, grades);
        manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        dataRecyclerView.setLayoutManager(manager);
        dataRecyclerView.setAdapter(adapter);
        mPresenter.initPersonOrChartData(grades.get(0).getUser_name(), new RemoteDataSource.LoadExerciseGradeCallback() {
            @Override
            public void onExerciseGradeUpLoaded(Result result) {

            }

            @Override
            public void onExerciseGradeNearlyLoaded(ExerciseBean[] result) {
                if (result != null && result.length != 0) {
                    numberOfPoints = result.length;
                    personData = Utils.array2List(result);
                    mPresenter.reset();
                    mPresenter.generateData();

                } else {
                    mPresenter.showToast("为获取到正确的数据");
                }
            }

            @Override
            public void onExerciseTenGradesLoaded(ExerciseBean[] result) {

            }

            @Override
            public void onDataNotAvailable() {
                mPresenter.showToast("网络加载错误");

            }
        });
        mPresenter.initAdapter();
    }

    @Override
    public void setListener() {
        dataLineChartView.setViewportCalculationEnabled(false);
        dataLineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                mPresenter.showToast(personData.get(i1).getExe_tiem());
            }

            @Override
            public void onValueDeselected() {

            }
        });


    }

    @Override
    public void reset() {
        numberOfLines = 1;
        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        shape = ValueShape.CIRCLE;
        isFilled = false;
        hasLabels = false;
        isCubic = false;
        hasLabelForSelected = false;
        pointsHaveDifferentColor = false;

        dataLineChartView.setValueSelectionEnabled(hasLabelForSelected);
        mPresenter.resetViewPort();

    }

    @Override
    public void generateData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, personData.get(j).getGrade()));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            if (pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("time");
                axisY.setName("grade");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        dataLineChartView.setLineChartData(data);

    }

    @Override
    public void initAdapter() {
        adapter.setListener(new ExamGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position, ExamResultBean item) {
                gradeOfUser.setText(item.getUser_name() + "最近的练习成绩");
                mPresenter.initPersonOrChartData(item.getUser_name(), new RemoteDataSource.LoadExerciseGradeCallback() {
                    @Override
                    public void onExerciseGradeUpLoaded(Result result) {

                    }

                    @Override
                    public void onExerciseGradeNearlyLoaded(ExerciseBean[] result) {
                        if (result != null & result.length != 0) {
                            if (numberOfPoints == result.length) {
                                personData.clear();
                                personData.addAll(Utils.array2List(result));
                                mPresenter.prepareDateAnimation(personData);
                                dataLineChartView.startDataAnimation();
                            } else {
                                numberOfPoints = result.length;
                                personData.clear();
                                personData.addAll(Utils.array2List(result));
                                reset();
                                generateData();
                            }
                        } else {
                            mPresenter.showToast("该学生还未进行练习");
                        }

                    }

                    @Override
                    public void onExerciseTenGradesLoaded(ExerciseBean[] result) {

                    }

                    @Override
                    public void onDataNotAvailable() {
                        mPresenter.showToast("网络加载错误");

                    }
                });
            }
        });

    }

    @Override
    public void resetViewPort() {
        final Viewport v = new Viewport(dataLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 500;
        v.left = 0;
        v.right = numberOfPoints;
        dataLineChartView.setMaximumViewport(v);
        dataLineChartView.setCurrentViewport(v);

    }

    @Override
    public void prepareDateAnimation(ArrayList<ExerciseBean> personData) {
        for (Line line : data.getLines()) {
            for (int i = 0; i < numberOfPoints; i++) {
                PointValue value = line.getValues().get(i);
                value.setTarget(i, personData.get(i).getGrade());
            }
        }

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
