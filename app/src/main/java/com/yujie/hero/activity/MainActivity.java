package com.yujie.hero.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yujie.hero.HeroApplication;
import com.yujie.hero.I;
import com.yujie.hero.R;
import com.yujie.hero.bean.ExamBean;
import com.yujie.hero.bean.ExerciseBean;
import com.yujie.hero.bean.UserBean;
import com.yujie.hero.bean.WordContentBean;
import com.yujie.hero.db.DataHelper;
import com.yujie.hero.listener.OnSetAvatarListener;
import com.yujie.hero.utils.FileUtils;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.Utils;
import com.yujie.hero.view.CircleTextImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyLineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private UserBean currentUser;
    private CircleTextImageView userAvatar;
    private TextView userName;
    private TextView userUid;
    private TextView userClass;
    private ProgressDialog pd;
    /** student's grades*/
    ArrayList<ExerciseBean> stuGrades;

    /** the chart's area*/
    private TextView noJoinTxt;
    private ImageView noJoinImg;
    private ComboLineColumnChartView chart;
    private ComboLineColumnChartData data;
    /** chart setting*/
    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 0;
    /** chart data array*/
    int[][] randomNumbersTab;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasPoints = true;
    private boolean hasLines = true;
    private boolean isCubic = true;
    private boolean hasLabels = true;

    /** select avatar*/
    private OnSetAvatarListener listener;

    /** Current exam which is testing array**/
    ArrayList<ExamBean> nowExam;
    String[] examArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initUserInfo();
        initActionBar();
        initNavigationView();
        initData();
        initChart();
        initAvatarListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initChart();
    }

    /**
     * pick avatar listener
     */
    private void initAvatarListener() {
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener = new OnSetAvatarListener(MainActivity.this,R.id.drawer_layout,
                        currentUser.getUid(),"user_avatar",currentUser.getUid());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){
            return;
        }
        listener.setAvatar(requestCode,data,userAvatar);
    }

    /**
     * init view
     */
    private void initView() {
        noJoinTxt = (TextView) findViewById(R.id.NoJoinTxt);
        noJoinImg = (ImageView) findViewById(R.id.NoJoinImg);
        pd = new ProgressDialog(this);
    }

    /**
     * init the chart view
     */
    private void initChart() {
        chart = (ComboLineColumnChartView) findViewById(R.id.main_activity_col_line_showGrades);
        chart.setOnValueTouchListener(new ValueTouchListener());

    }

    /**
     * init student's grades list
     */
    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = stuGrades.get(j).getGrade();
            }
        }
    }

    /**
     * init the chart,draw the AxisX and AxisY
     */
    private void generateData() {
        // Chart looks the best when line data and column data have similar maximum viewports.
        data = new ComboLineColumnChartData(generateColumnData(), generateLineData());

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
        chart.setComboLineColumnChartData(data);
    }

    /**
     * init the lineChart's data
     * @return
     */
    private LineChartData generateLineData() {
        LineChartData lineChartData;
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
        return lineChartData;

    }

    /**
     * init the columnChar's data.
     * @return
     */
    private ColumnChartData generateColumnData() {
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numberOfPoints; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numberOfLines; ++j) {
                values.add(new SubcolumnValue(stuGrades.get(i).getGrade(), ChartUtils.COLOR_GREEN));
            }

            columns.add(new Column(values));
        }

        ColumnChartData columnChartData = new ColumnChartData(columns);
        return columnChartData;
    }

    /**
     * get studtent's nearly exercise grades from server
     */
    private void initData() {
        stuGrades = new ArrayList<>();
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GETNEARLYGRADES)
                .addParam(I.User.USER_NAME,currentUser.getUser_name())
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        if (result!=null&result.length!=0){
                            chart.setVisibility(View.VISIBLE);
                            noJoinImg.setVisibility(View.GONE);
                            noJoinTxt.setVisibility(View.VISIBLE);
                            noJoinTxt.setText("The last ten exercises");
                            stuGrades = Utils.array2List(result);
                            numberOfPoints = stuGrades.size();
                            randomNumbersTab = new int[maxNumberOfLines][numberOfPoints];
                            generateValues();
                            generateData();
                        }else {
                            chart.setVisibility(View.GONE);
                            noJoinImg.setVisibility(View.VISIBLE);
                            noJoinTxt.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    private void initUserInfo() {
        currentUser = HeroApplication.getInstance().getCurrentUser();
    }

    /**
     * init navigationview and init userAvatar,userName,userUid...
     */
    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        /** user avatar*/
        userAvatar = (CircleTextImageView) headerView.findViewById(R.id.userAvatar);
        showAvatar(HeroApplication.AVATAR_ROOT+currentUser.getAvatar(),userAvatar);
        /** userName*/
        userName = (TextView) headerView.findViewById(R.id.userName);
        userName.setText(currentUser.getUser_name());
        /** userUid*/
        userUid = (TextView) headerView.findViewById(R.id.userUid);
        userUid.setText(currentUser.getUid());
        /** user class*/
        userClass = (TextView) headerView.findViewById(R.id.userclass);
        OkHttpUtils<String> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GETCLASSNAME)
                .addParam(I.Exam.ID,currentUser.getB_class()+"")
                .targetClass(String.class)
                .execute(new OkHttpUtils.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result!=null){
                            userClass.setText(result);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }

    /**
     * download avatar and show avatar
     * @param url
     * @param userAvatar
     */
    private void showAvatar(String url, final CircleTextImageView userAvatar) {
        OkHttpUtils<Bitmap> utils = new OkHttpUtils<>();
        utils.url(url)
                .downloadFile()
                .execute(new OkHttpUtils.OnCompleteListener<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap result) {
                        File file = FileUtils.getAvatarPath(MainActivity.this,"user_avatar", currentUser.getUid() + ".jpg");
                        if(result!=null){
                            userAvatar.setImageBitmap(result);
                            if(!file.getParentFile().exists()){
                                return ;
                            }
                            FileOutputStream out = null;
                            try {
                                out = new FileOutputStream(file);
                                result.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else {
                            if (file.exists()){
                                userAvatar.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                            }else {
                                userAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.app_icon));
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }


    /**
     * init view
     */
    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("UcaiHero");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * when the drawer_layout is opening,and you press the back keyboard,
     * the drawer will close,else,will super the onBackPressed();
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * NavigationItemSelected listener
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.getTenGrades) {
            getTenGrades();
        } else if (id == R.id.getClassSort) {
            startActivity(new Intent(mContext,ShowSortActivity.class));
        } else if (id == R.id.startExam) {
            goExam();
        } else if (id == R.id.startExercise) {
            goExercise();
        }else if (id == R.id.logout){
            logout();
        }else if (id == R.id.point){
            startActivity(new Intent(mContext,ResetPassWordActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * logout and clear the userData,update the user login status to 0
     */
    private void logout() {
        new DataHelper(mContext).updateStatus(0,currentUser.getUser_name());
        HeroApplication.getInstance().setCURRENT_EXAM_ID(0);
        HeroApplication.getInstance().setCurrentTestCourse(null);
        HeroApplication.getInstance().setCurrentUser(null);
        Intent intent = new Intent(mContext,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * go GameActivity with exam
     */
    private void goExam() {
        OkHttpUtils<ExamBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_EXAM_NOW)
                .targetClass(ExamBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamBean[]>() {
                    @Override
                    public void onSuccess(ExamBean[] result) {
                        if (result!=null & result.length>0){
                            nowExam = Utils.array2List(result);
                            examArray = new String[nowExam.size()];
                            for (int i=0;i<nowExam.size();i++){
                                examArray[i] = "名称: "+nowExam.get(i).getExam_name()+" 课程: "+
                                        nowExam.get(i).getCourse_id()+" 时间: "+nowExam.get(i).getExam_time();
                            }
                            showExamDialog();
                        }else {
                            Toast.makeText(MainActivity.this,"当前没有正在进行的考试",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * the dialog to chose exam
     */
    private void showExamDialog() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("当前已开考的考试")
                .setItems(examArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExamBean examBean = nowExam.get(which);
                        HeroApplication.getInstance().setCURRENT_EXAM_ID(examBean.getId());
                        if(new DataHelper(mContext).getWordCount(examBean.getCourse_id())==0){
                            Toast.makeText(MainActivity.this,"没有发现单词数据,开始下载",Toast.LENGTH_SHORT).show();
                            pd.show();
                            downloadContent(examBean.getCourse_id(),"1");
                        }else {
                            Intent intent = new Intent(mContext,GameActivity.class);
                            String action_code = examBean.getCourse_id()+","+"1"+","+HeroApplication.EXAM_CODE+","+"0";
                            intent.putExtra("action_code",action_code);
                            startActivity(intent);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * go GameActivity with exercise
     */
    private void goExercise() {
        String[] time = new String[]{"1分钟","3分钟","5分钟","10分钟"};
        String[] course = new String[]{"Android","IOS","PHP","H5"};
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_chose,null);
        final Spinner choseDialogSpinnerTime = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_time);
        final Spinner choseDialogSpinnerCourse = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_course);
        initSpinner(choseDialogSpinnerCourse, choseDialogSpinnerTime,time,course);
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择课程及时间")
                .setView(view)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String course = choseDialogSpinnerCourse.getSelectedItem().toString();
                        HeroApplication.getInstance().setCurrentTestCourse(course);
                        String course_simpleName = course.substring(0,1).toLowerCase();
                        String time = choseDialogSpinnerTime.getSelectedItem().toString().substring(0,2);
                        if (time.substring(1,2).equals("分")){
                            time = time.substring(0,1);
                        }
                        if(new DataHelper(mContext).getWordCount(course_simpleName)==0){
                            Toast.makeText(MainActivity.this,"没有发现单词数据,开始下载",Toast.LENGTH_SHORT).show();
                            pd.show();
                            downloadContent(course_simpleName,time);
                        }else {
                            Intent intent = new Intent(mContext,GameActivity.class);
                            String action_code = course_simpleName+","+time+","+HeroApplication.EXERCISE_CODE+","+"0";
                            intent.putExtra("action_code",action_code);
                            startActivity(intent);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * download the word data to local database
     * @param course_simpleName
     * @param time
     */
    private void downloadContent(final String course_simpleName, final String time) {
        OkHttpUtils<WordContentBean[]> utils = new OkHttpUtils<WordContentBean[]>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_DOWNLOAD_CONTENT)
                .targetClass(WordContentBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<WordContentBean[]>() {
                    @Override
                    public void onSuccess(WordContentBean[] result) {
                        if (result!=null&result.length!=0){
                            if (new DataHelper(mContext).addWord(Utils.array2List(result))){
                                pd.dismiss();
                                Toast.makeText(MainActivity.this,"单词数据库更新成功",Toast.LENGTH_SHORT).show();
                                if (HeroApplication.getInstance().getCURRENT_EXAM_ID()!=0){
                                    Intent intent = new Intent(mContext,GameActivity.class);
                                    String action_code = course_simpleName+","+"1"+","+HeroApplication.EXAM_CODE+","+"0";
                                    intent.putExtra("action_code",action_code);
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(mContext,GameActivity.class);
                                    String action_code = course_simpleName+","+time+","+HeroApplication.EXERCISE_CODE+","+"0";
                                    intent.putExtra("action_code",action_code);
                                    startActivity(intent);
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initSpinner(Spinner choseDialogSpinnerCourse, Spinner choseDialogSpinnerTime, String[] time, String[] course) {
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, Utils.array2List(time));
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerTime.setAdapter(timeAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, Utils.array2List(course));
        courseAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerCourse.setAdapter(courseAdapter);
    }

    /**
     * get the best ten grades from server and init the chart to set the data to chart
     */
    private void getTenGrades() {
        ArrayList<ExerciseBean> bestGrades = new ArrayList<>();
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_TEN_GRADES)
                .addParam(I.User.USER_NAME,currentUser.getUser_name())
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        if (result!=null&result.length!=0){
                            chart.setVisibility(View.VISIBLE);
                            noJoinImg.setVisibility(View.GONE);
                            noJoinTxt.setVisibility(View.VISIBLE);
                            noJoinTxt.setText("最好的十个成绩");
                            stuGrades = Utils.array2List(result);
                            numberOfPoints = stuGrades.size();
                            randomNumbersTab = new int[maxNumberOfLines][numberOfPoints];
                            generateValues();
                            generateData();
                        }else {
                            chart.setVisibility(View.GONE);
                            noJoinImg.setVisibility(View.VISIBLE);
                            noJoinTxt.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(MainActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * the ValueTouchListener,show the option time if clicked;
     */
    private class ValueTouchListener implements ComboLineColumnChartOnValueSelectListener {

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

        @Override
        public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
        }

        @Override
        public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(mContext, stuGrades.get(pointIndex).getExe_tiem(), Toast.LENGTH_SHORT).show();
        }

    }
}
