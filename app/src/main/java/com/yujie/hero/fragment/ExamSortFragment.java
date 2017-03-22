package com.yujie.hero.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.hero.HeroApplication;
import com.yujie.hero.I;
import com.yujie.hero.R;
import com.yujie.hero.adapter.ExamGradeAdapter;
import com.yujie.hero.bean.ExamResultBean;
import com.yujie.hero.bean.ExerciseBean;
import com.yujie.hero.utils.OkHttpUtils;
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

public class ExamSortFragment extends Fragment {
    public static final String TAG = ExamSortFragment.class.getSimpleName();
    @Bind(R.id.gradeOfUser)
    TextView gradeOfUser;
    private Context mContext;
    @Bind(R.id.dataRecyclerView)
    RecyclerView dataRecyclerView;
    @Bind(R.id.dataLineChartView)
    LineChartView dataLineChartView;
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

    public ExamSortFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_sort, container, false);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        initData();
        initListener();
        resetViewport();
        return view;
    }

    private void initListener() {
        dataLineChartView.setViewportCalculationEnabled(false);
        dataLineChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                Toast.makeText(mContext, personData.get(i1).getExe_tiem(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    private void resetViewport() {
        final Viewport v = new Viewport(dataLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 500;
        v.left = 0;
        v.right = numberOfPoints;
        dataLineChartView.setMaximumViewport(v);
        dataLineChartView.setCurrentViewport(v);
    }

    private void prepareDataAnimation(ArrayList<ExerciseBean> personData) {
        for (Line line : data.getLines()) {
            for (int i = 0; i < numberOfPoints; i++) {
                PointValue value = line.getValues().get(i);
                value.setTarget(i, personData.get(i).getGrade());
            }
        }
    }

    private void reset() {
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
        resetViewport();
    }

    private void generateData() {

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

    private void initAdapter() {
        adapter.setListener(new ExamGradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position, ExamResultBean item) {
                gradeOfUser.setText(item.getUser_name()+"最近的练习成绩");
                initPersonData(item);
            }
        });
    }

    private void initPersonData(ExamResultBean item) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GETNEARLYGRADES)
                .addParam(I.User.USER_NAME, item.getUser_name())
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        if (result != null & result.length != 0) {
                            if (numberOfPoints == result.length) {
                                personData.clear();
                                personData.addAll(Utils.array2List(result));
                                prepareDataAnimation(personData);
                                dataLineChartView.startDataAnimation();
                            } else {
                                numberOfPoints = result.length;
                                personData.clear();
                                personData.addAll(Utils.array2List(result));
                                reset();
                                generateData();
                            }
                        } else {
                            Toast.makeText(mContext, "该学生还未进行练习", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initData() {
        grades = new ArrayList<>();
        OkHttpUtils<ExamResultBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_EXAM_GRADE)
                .addParam(I.ExamGrade.EXAM_ID, HeroApplication.getInstance().getCURRENT_EXAM_ID() + "")
                .targetClass(ExamResultBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamResultBean[]>() {
                    @Override
                    public void onSuccess(ExamResultBean[] result) {
                        if (result != null & result.length!=0) {
                            grades = Utils.array2List(result);
                            adapter = new ExamGradeAdapter(mContext, grades);
                            manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            dataRecyclerView.setLayoutManager(manager);
                            dataRecyclerView.setAdapter(adapter);
                            if (grades.size() == 0 | grades == null) {
                                return;
                            }
                            initChart(grades);
                            initAdapter();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initChart(ArrayList<ExamResultBean> grades) {
        personData = new ArrayList<>();
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GETNEARLYGRADES)
                .addParam(I.User.USER_NAME, grades.get(0).getUser_name())
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        if (result != null & result.length != 0) {
                            numberOfPoints = result.length;
                            personData = Utils.array2List(result);
                            reset();
                            generateData();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
