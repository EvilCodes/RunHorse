package com.yujie.hero.tasks.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.data.application.HeroApplication;
import com.yujie.hero.data.bean.ExamBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.view.CircleTextImageView;
import com.yujie.hero.tasks.checkorder.CheckOrderActivity;
import com.yujie.hero.tasks.examorexercise.EorEActivity;
import com.yujie.hero.tasks.login.LoginActivity;
import com.yujie.hero.tasks.pwdsetting.PwdActivity;
import com.yujie.hero.utils.FileUtils;
import com.yujie.hero.utils.OnSetAvatarListener;
import com.yujie.hero.utils.StartTargetActivity;
import com.yujie.hero.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;

/**
 * Created by Administrator on 2017/4/24.
 */

public class MainFragment extends Fragment implements MainContract.View, ComboLineColumnChartOnValueSelectListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.NoJoinTxt)
    TextView noJoinTxt;
    @Bind(R.id.NoJoinImg)
    ImageView noJoinImg;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.main_activity_col_line_showGrades)
    ComboLineColumnChartView mainActivityColLineShowGrades;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    private Context mContext;
    private MainContract.Presenter mPresenter;
    private ProgressDialog pd;
    private UserBean currentUser;
    private CircleTextImageView userAvatar;
    private TextView userName;
    private TextView userUid;
    private TextView userClass;
    private OnSetAvatarListener listener;
    private ComboLineColumnChartData data;
    private LineChartData lineChartData;
    private ColumnChartData columnChartData;
    private View dialogView;


    /**
     * student's grades
     */
    ArrayList<ExerciseBean> stuGrades;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 0;
    /**
     * chart data array
     */
    int[][] randomNumbersTab;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = true;
    private boolean hasLabels = true;

    private ResetPwdReceiver mReceiver;
    private ArrayList<ExamBean> nowExam;
    private String[] examArray;

    private String course_simpleName;
    private String time;
    private Spinner choseDialogSpinnerTime;
    private Spinner choseDialogSpinnerCourse;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.from(mContext).inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mPresenter.setViewData();
        mPresenter.initNavigationView();
        mPresenter.initData(currentUser.getUser_name());
        mPresenter.setListenner();
        mPresenter.initReceiver();
        return view;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.getTenGrades:
                mPresenter.getTenGrades(currentUser.getUser_name());
                break;
            case R.id.startExam:
                mPresenter.getExamNow();
                break;
            case R.id.point:
                StartTargetActivity.jumpToTargetActivity(mContext, PwdActivity.class);
                break;
            case R.id.startExercise:
                mPresenter.initSpinner();
                mPresenter.showClassAndTime(mContext, "选择课程及时间", dialogView, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String course = choseDialogSpinnerCourse.getSelectedItem().toString();
                        HeroApplication.getInstance().setCurrentTestCourse(course);
                        course_simpleName = course.substring(0, 1).toLowerCase();
                        time = choseDialogSpinnerTime.getSelectedItem().toString().substring(0, 2);
                        if (time.substring(1, 2).equals("分")) {
                            time = time.substring(0, 1);
                        }
                        int wordNum = mPresenter.getWordContent(course_simpleName);

                        if (wordNum == 0) {
                            showToast("没有发现单词数据，开始下载");
                            pd.show();
                            mPresenter.downLoadWordContent();
                        } else {
                            Intent intent = new Intent(mContext, EorEActivity.class);
                            String action_code = course_simpleName + "," + time + "," + HeroApplication.EXERCISE_CODE + "," + "0";
                            intent.putExtra("action_code", action_code);
                            getActivity().startActivity(intent);
                        }

                    }
                });


                break;
            case R.id.logout:
                mPresenter.logout(getActivity(), LoginActivity.class);

                break;
            case R.id.getClassSort:
                StartTargetActivity.jumpToTargetActivity(mContext, CheckOrderActivity.class);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.userAvatar) {
            listener = new OnSetAvatarListener(getActivity(), R.id.drawer_layout,
                    currentUser.getUid(), "user_avatar", currentUser.getUid());

        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void setViewData() {
        currentUser = HeroApplication.getInstance().getCurrentUser();
        pd = new ProgressDialog(mContext);
        toolbar.setTitle("UcaiHero");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void initReceiver() {
        mReceiver = new ResetPwdReceiver();
        IntentFilter fliter = new IntentFilter("ResetPwdFinished");
        mContext.registerReceiver(mReceiver, fliter);


    }

    @Override
    public void initNavigationView() {
        navView.setNavigationItemSelectedListener(this);
        View headerView = navView.getHeaderView(0);
        /** user avatar*/
        userAvatar = (CircleTextImageView) headerView.findViewById(R.id.userAvatar);
        mPresenter.showAvatar(HeroApplication.AVATAR_ROOT + currentUser.getAvatar());
        /** userName*/
        userName = (TextView) headerView.findViewById(R.id.userName);
        userName.setText(currentUser.getUser_name());
        /** userUid*/
        userUid = (TextView) headerView.findViewById(R.id.userUid);
        userUid.setText(currentUser.getUid());
        /** user class*/
        userClass = (TextView) headerView.findViewById(R.id.userclass);


    }

    @Override
    public void setListenner() {
        mainActivityColLineShowGrades.setOnValueTouchListener(this);
        userAvatar.setOnClickListener(this);


    }

    @Override
    public void getData(ExerciseBean[] result) {
        if (result != null & result.length != 0) {
            mainActivityColLineShowGrades.setVisibility(View.VISIBLE);
            noJoinImg.setVisibility(View.GONE);
            noJoinTxt.setVisibility(View.VISIBLE);
            noJoinTxt.setText("最近十次的练习成绩");
            stuGrades = Utils.array2List(result);
            Log.e("MainFragment", "MainFragment.result=" + stuGrades.get(0).toString());
            numberOfPoints = stuGrades.size();
            randomNumbersTab = new int[maxNumberOfLines][numberOfPoints];
            mPresenter.generateValues();
            mPresenter.generateColumnData();
            mPresenter.generateLineData();
            mPresenter.generateData();
        } else {
            mainActivityColLineShowGrades.setVisibility(View.GONE);
            noJoinImg.setVisibility(View.VISIBLE);
            noJoinTxt.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void getNearlyTenGrades(ExerciseBean[] result) {
        Log.e("MainFragment", "getNearlyTenGrades.result=" + result);
        if (result != null & result.length != 0) {
            mainActivityColLineShowGrades.setVisibility(View.VISIBLE);
            noJoinImg.setVisibility(View.GONE);
            noJoinTxt.setVisibility(View.VISIBLE);
            noJoinTxt.setText("最好的十个成绩");
            stuGrades = Utils.array2List(result);
            numberOfPoints = stuGrades.size();
            randomNumbersTab = new int[maxNumberOfLines][numberOfPoints];
            mPresenter.generateValues();
            mPresenter.generateColumnData();
            mPresenter.generateLineData();
            mPresenter.generateData();
        } else {
            mainActivityColLineShowGrades.setVisibility(View.GONE);
            noJoinImg.setVisibility(View.VISIBLE);
            noJoinTxt.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void showAvatar(Bitmap result) {
        File file = FileUtils.getAvatarPath(getActivity(), "user_avatar", currentUser.getUid() + ".jpg");

        if (result != null) {

            userAvatar.setImageBitmap(result);
            if (!file.getParentFile().exists()) {
                return;
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                result.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            if (file.exists()) {
                userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } else {
                userAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.app_icon));
            }
        }


    }

    @Override
    public void setClassName(String className) {
        userClass.setText(className);

    }


    @Override
    public void getExamNow(final ArrayList<ExamBean> nowExam) {
        examArray = new String[nowExam.size()];
        for (int i = 0; i < nowExam.size(); i++) {
            examArray[i] = "名称: " + nowExam.get(i).getExam_name() + " 课程: " +
                    nowExam.get(i).getCourse_id() + " 时间: " + nowExam.get(i).getExam_time();
        }
        mPresenter.showExamDialog(mContext, "当前已开考的考试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExamBean examBean = nowExam.get(which);
                HeroApplication.getInstance().setCURRENT_EXAM_ID(examBean.getId());
                int wordCount = mPresenter.getWordContent(examBean.getCourse_id());
                if (wordCount== 0) {
                    showToast("没有发现单词数据,开始下载");
                    pd.show();
                    course_simpleName = examBean.getCourse_id();
                    time = "1";
                    mPresenter.downLoadWordContent();
                } else {
                    Intent intent = new Intent(mContext, EorEActivity.class);
                    String action_code = examBean.getCourse_id() + "," + "1" + "," + HeroApplication.EXAM_CODE + "," + "0";
                    intent.putExtra("action_code", action_code);
                    startActivity(intent);
                }

            }
        }, examArray);


    }

    @Override
    public void getWordContent(ArrayList<WordContentBean> list) {
        mPresenter.addWordsToLocal(list);
        showToast("单词数据更新成功");
        if (HeroApplication.getInstance().getCURRENT_EXAM_ID() != 0) {
            Intent intent = new Intent(mContext, EorEActivity.class);
            String action_code = course_simpleName + "," + "1" + "," + HeroApplication.EXAM_CODE + "," + "0";
            intent.putExtra("action_code", action_code);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, EorEActivity.class);
            String action_code = course_simpleName + "," + time + "," + HeroApplication.EXERCISE_CODE + "," + "0";
            intent.putExtra("action_code", action_code);
            startActivity(intent);
        }


    }

    @Override
    public void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = stuGrades.get(j).getGrade();
            }
        }

    }

    @Override
    public void generateData() {




        data = new ComboLineColumnChartData();
        data.setLineChartData(lineChartData);
        data.setColumnChartData(columnChartData);


        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("time");
                axisY.setName("grade");
                axisX.setTextColor(Color.BLACK);
                axisY.setTextColor(Color.BLACK);
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        mainActivityColLineShowGrades.setComboLineColumnChartData(data);

    }

    @Override
    public void generateLineData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setCubic(isCubic);
            line.setHasLabels(hasLabels);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }
        lineChartData = new LineChartData(lines);

    }

    @Override
    public void generateColumnData() {
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numberOfPoints; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numberOfLines; ++j) {
                values.add(new SubcolumnValue(stuGrades.get(i).getGrade(), ChartUtils.COLOR_GREEN));
            }

            columns.add(new Column(values));
        }

        columnChartData = new ColumnChartData(columns);

    }

    @Override
    public void initSpinner() {
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_chose, null);
        String[] time = new String[]{"1分钟", "3分钟", "5分钟", "10分钟"};
        String[] course = new String[]{"Android", "IOS", "PHP", "H5"};

        choseDialogSpinnerTime = (Spinner) dialogView.findViewById(R.id.chose_dialog_Spinner_time);
        choseDialogSpinnerCourse = (Spinner) dialogView.findViewById(R.id.chose_dialog_Spinner_course);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, Utils.array2List(time));
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerTime.setAdapter(timeAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, Utils.array2List(course));
        courseAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerCourse.setAdapter(courseAdapter);


    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;

    }

    @Override
    public void onColumnValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {

    }

    @Override
    public void onPointValueSelected(int i, int i1, PointValue pointValue) {
        showToast(stuGrades.get(i1).getExe_tiem());
    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mReceiver);
        ButterKnife.unbind(this);
    }
}
