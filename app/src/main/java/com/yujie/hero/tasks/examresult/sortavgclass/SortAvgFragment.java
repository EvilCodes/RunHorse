package com.yujie.hero.tasks.examresult.sortavgclass;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.ExamClassGradeBean;
import com.yujie.hero.data.bean.ExamGradeAvgBean;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/4/28.
 */

public class SortAvgFragment extends Fragment implements SortAvgContract.View, ColumnChartOnValueSelectListener {
    @Bind(R.id.chart_top)
    LineChartView chartTop;
    @Bind(R.id.chart_bottom)
    ColumnChartView chartBottom;
    private Context context;
    private SortAvgContract.Presenter mPresenter;
    /**
     * student's name array
     */
    private ArrayList<String> nameArray;
    private ArrayList<ExamClassGradeBean> stuGrades;
    /**
     * class's name array
     */
    private String[] classArray;
    private ArrayList<ExamGradeAvgBean> classGrades;
    /**
     * LineChart data
     */
    private LineChartData lineData;

    /**
     * ColumnChart data
     */
    private ColumnChartData columnData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.from(context).inflate(R.layout.fragment_sort_avg_class, container, false);
        mPresenter = new SortAvgPresenter(new TasksRemoteDataSource(), this);
        ButterKnife.bind(this, view);
        mPresenter.initClassData(HeroApplication.getInstance().getCURRENT_EXAM_ID()+"");
        mPresenter.setListener();
        return view;
    }

    @Override
    public void getClassAvgGrade(ArrayList<ExamGradeAvgBean> classGrades) {
        this.classGrades=classGrades;
        classArray = new String[classGrades.size()];
        for(int i=0;i<classGrades.size();i++){
            classArray[i] = classGrades.get(i).getClass_name();
        }
        generateColumnData();
        nameArray = new ArrayList<String>();
        generateInitialLineData();

    }

    @Override
    public void getExamClass(ArrayList<ExamClassGradeBean> stuGrades) {
        nameArray.clear();
        for (int i=0;i<stuGrades.size();i++){
            nameArray.add(stuGrades.get(i).getUser_name());
        }
        mPresenter.resetViewPort();
        generateInitialLineData();


    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setListener() {
        chartBottom.setOnValueTouchListener(this);


    }

    @Override
    public void resetViewPort() {
        chartTop.setValueSelectionEnabled(false);
        final Viewport v = new Viewport(chartTop.getMaximumViewport());
        v.bottom = 0;
        v.top = 500;
        v.left = 0;
        v.right = stuGrades.size();
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

    }

    @Override
    public void generateInitialLineData() {
        int numValues = nameArray.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues;i++) {
            values.add(new PointValue(i, stuGrades.get(i).getGrade()).setLabel(stuGrades.get(i).getGrade()+""));
            axisValues.add(new AxisValue(i).setLabel(nameArray.get(i)));
        }

        Line line = new Line(values);
        line.setHasLabels(true);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3).setTextColor(Color.BLACK));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 500,nameArray.size(), 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);

    }

    @Override
    public void generateColumnData() {
        int numSubcolumns = 1;
        int numColumns = classArray.length;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; i++) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; j++) {
                values.add(new SubcolumnValue(classGrades.get(i).getAvgGrade(), ChartUtils.pickColor()));
            }

            axisValues.add(new AxisValue(i).setLabel(classArray[i]));
            columns.add(new Column(values).setHasLabels(true));


        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setTextColor(Color.BLACK));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

        chartBottom.setColumnChartData(columnData);

        // Set selection mode to keep selected month column highlighted.
        chartBottom.setValueSelectionEnabled(true);
        chartBottom.setZoomType(ZoomType.HORIZONTAL);
    }

    @Override
    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue subcolumnValue) {
        ExamGradeAvgBean gradeAvgBean = classGrades.get(columnIndex);
        mPresenter.initStuGrades(gradeAvgBean.getClass_id(), HeroApplication.getInstance().getCURRENT_EXAM_ID() + "");
    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
