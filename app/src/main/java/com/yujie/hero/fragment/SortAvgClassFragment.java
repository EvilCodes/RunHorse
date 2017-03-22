package com.yujie.hero.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yujie.hero.HeroApplication;
import com.yujie.hero.I;
import com.yujie.hero.R;
import com.yujie.hero.bean.ExamClassGradeBean;
import com.yujie.hero.bean.ExamGradeAvgBean;
import com.yujie.hero.bean.ExamGradeBean;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.Utils;

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
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class SortAvgClassFragment extends Fragment {
    public static final String TAG = SortAvgClassFragment.class.getSimpleName();
    @Bind(R.id.chart_top)
    LineChartView chartTop;
    @Bind(R.id.chart_bottom)
    ColumnChartView chartBottom;

    /** student's name array*/
    ArrayList<String> nameArray;
    ArrayList<ExamClassGradeBean> stuGrades;
    /** class's name array*/
    String[] classArray;
    ArrayList<ExamGradeAvgBean> classGrades;
    /** LineChart data*/
    private LineChartData lineData;

    /** ColumnChart data*/
    private ColumnChartData columnData;

    public SortAvgClassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sort_avg_class, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }


    private void resetViewport() {
        chartTop.setValueSelectionEnabled(false);
        final Viewport v = new Viewport(chartTop.getMaximumViewport());
        v.bottom = 0;
        v.top = 500;
        v.left = 0;
        v.right = stuGrades.size();
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);
    }

    private void generateInitialLineData() {
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

    private void generateColumnData() {

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

    /**
     * init Data
     */
    private void initData() {
        initClassData();
        chartBottom.setOnValueTouchListener(new ValueTouchListener());
    }

    private void initClassData() {
        classGrades = new ArrayList<>();
        OkHttpUtils<ExamGradeAvgBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_CLASS_AVG_GRADE)
                .addParam(I.ExamGrade.EXAM_ID,HeroApplication.getInstance().getCURRENT_EXAM_ID()+"")
                .targetClass(ExamGradeAvgBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamGradeAvgBean[]>() {
                    @Override
                    public void onSuccess(ExamGradeAvgBean[] result) {
                        if (result!=null&result.length!=0){
                            classGrades = Utils.array2List(result);
                            classArray = new String[classGrades.size()];
                            for(int i=0;i<classGrades.size();i++){
                                classArray[i] = classGrades.get(i).getClass_name();
                            }
                            generateColumnData();
                            nameArray = new ArrayList<String>();
                            generateInitialLineData();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            ExamGradeAvgBean gradeAvgBean = classGrades.get(columnIndex);
            initStuGrades(gradeAvgBean.getClass_id());
        }

        @Override
        public void onValueDeselected() {

        }
    }

    private void initStuGrades(int classId) {
        stuGrades = new ArrayList<>();
        OkHttpUtils<ExamClassGradeBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_CLASS_GRADE_LIST)
                .addParam(I.ExamGrade.EXAM_ID,HeroApplication.getInstance().getCURRENT_EXAM_ID()+"")
                .addParam(I.ExamGrade.B_CLASS,classId+"")
                .targetClass(ExamClassGradeBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamClassGradeBean[]>() {
                    @Override
                    public void onSuccess(ExamClassGradeBean[] result) {
                        if (result!=null & result.length!=0){
                            nameArray.clear();
                            stuGrades = Utils.array2List(result);
                            for (int i=0;i<stuGrades.size();i++){
                                nameArray.add(stuGrades.get(i).getUser_name());
                            }
                            resetViewport();
                            generateInitialLineData();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(getActivity(),"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }
}
