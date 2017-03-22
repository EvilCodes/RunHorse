package com.yujie.hero.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.yujie.hero.HeroApplication;
import com.yujie.hero.I;
import com.yujie.hero.R;
import com.yujie.hero.bean.AreasBean;
import com.yujie.hero.bean.ClassObj;
import com.yujie.hero.bean.CourseBean;
import com.yujie.hero.bean.Result;
import com.yujie.hero.bean.StartTimeBean;
import com.yujie.hero.bean.UserBean;
import com.yujie.hero.db.DataHelper;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.register_activity_EditText_stuNum)
    EditText registerActivityEditTextStuNum;
    private Context mContext;
    @Bind(R.id.register_activity_EditText_inputUserName)
    EditText registerActivityEditTextInputUserName;
    @Bind(R.id.register_activity_EditText_inputPwd)
    EditText registerActivityEditTextInputPwd;
    @Bind(R.id.register_activity_RadioButton_boy)
    RadioButton registerActivityRadioButtonBoy;
    @Bind(R.id.register_activity_Button_chooseClass)
    Button registerActivityButtonChooseClass;
    @Bind(R.id.register_activity_Button_register)
    Button registerActivityButtonRegister;
    LayoutInflater inflater;
    /**
     * area list
     **/
    ArrayList<AreasBean> arealist;
    HashMap<String, String> areaMap;
    ArrayList<String> areaStr;
    /**
     * course list
     **/
    ArrayList<CourseBean> courselist;
    HashMap<String, String> courseMap;
    ArrayList<String> courseStr;
    /**
     * time list
     */
    ArrayList<StartTimeBean> timelist;
    ArrayList<String> timeStr;
    /**
     * class list
     **/
    ArrayList<ClassObj> classlist;
    String[] classStr;
    Dialog dialog;

    /**class id**/
    int classId = 0;
    /** userName*/
    String user_name;
    /** password*/
    String passWord;
    /** sex 1:boy  2:girl */
    int sex = 0;
    /** uid*/
    String uid;
    /** top_grade*/
    int top_grade = 0;
    /** classSimpleName*/
    String classSimpleName;
    static final int CHOSECLASS = 100;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHOSECLASS:
                    ClassObj obj = (ClassObj) msg.obj;
                    registerActivityButtonChooseClass.setText(obj.getClass_name());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        initArea();
        initCourse();
        initStartTime();
    }


    private void initStartTime() {
        timelist = new ArrayList<>();
        OkHttpUtils<StartTimeBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_TIME)
                .targetClass(StartTimeBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<StartTimeBean[]>() {
                    @Override
                    public void onSuccess(StartTimeBean[] result) {
                       if (result!=null & result.length!=0){
                           timelist = Utils.array2List(result);
                           timeStr = new ArrayList<String>();
                           for (StartTimeBean time : timelist) {
                               timeStr.add(time.getStart_time());
                           }
                       }else {
                           Toast.makeText(RegisterActivity.this,"数据获取失败，请稍后再试",Toast.LENGTH_LONG).show();
                       }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * get course list from server
     */
    private void initCourse() {
        courselist = new ArrayList<>();
        OkHttpUtils<CourseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_COURSE)
                .targetClass(CourseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CourseBean[]>() {
                    @Override
                    public void onSuccess(CourseBean[] result) {
                        if (result!=null & result.length!=0){
                            courselist = Utils.array2List(result);
                            courseMap = new HashMap<String, String>();
                            courseStr = new ArrayList<String>();
                            for (CourseBean courseBean : courselist) {
                                courseMap.put(courseBean.getCourse_name(), courseBean.getSimple_name());
                                courseStr.add(courseBean.getCourse_name());
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this,"数据获取失败，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * get area list from server
     */
    private void initArea() {
        arealist = new ArrayList<>();
        OkHttpUtils<AreasBean[]> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_AREA)
                .targetClass(AreasBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<AreasBean[]>() {
                    @Override
                    public void onSuccess(AreasBean[] result) {
                        if (result!=null & result.length!=0){
                            arealist = Utils.array2List(result);
                            areaMap = new HashMap<String, String>();
                            areaStr = new ArrayList<String>();
                            for (AreasBean area : arealist) {
                                areaMap.put(area.getArea_name(), area.getSimple_name());
                                areaStr.add(area.getArea_name());
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this,"数据获取失败，请稍后再试",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }


    @OnClick({R.id.register_activity_Button_chooseClass, R.id.register_activity_Button_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_activity_Button_chooseClass:
                showChoseDialog();
                break;
            case R.id.register_activity_Button_register:
                goRegister();
                break;
        }
    }

    /**
     * do Register
     */
    private void goRegister() {
        user_name = registerActivityEditTextInputUserName.getText().toString();
        passWord = registerActivityEditTextInputPwd.getText().toString();
        sex = registerActivityRadioButtonBoy.isChecked()?1:2;
        String stuNum = registerActivityEditTextStuNum.getText().toString();
        if(user_name.isEmpty()){
            registerActivityEditTextInputUserName.setError("没有输入,请重试");
            registerActivityEditTextInputUserName.requestFocus();
            return;
        }
        if(passWord.isEmpty()){
            registerActivityEditTextInputPwd.setError("没有输入,请重试");
            registerActivityEditTextInputPwd.requestFocus();
            return;
        }
        if (sex==0){
            Toast.makeText(RegisterActivity.this,"请选择性别",Toast.LENGTH_LONG).show();
            return;
        }
        if (stuNum.isEmpty()){
            registerActivityEditTextStuNum.setError("没有输入,请重试");
            registerActivityEditTextStuNum.requestFocus();
            return;
        }
        if(classId==0 | classSimpleName.isEmpty()){
            return;
        }
        uid = classSimpleName+stuNum.toString();
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_REGISTER)
                .addParam(I.User.UID,uid)
                .addParam(I.User.PWD,passWord)
                .addParam(I.User.USER_NAME,user_name)
                .addParam(I.User.SEX,sex+"")
                .addParam(I.User.B_CLASS,classId+"")
                .addParam(I.User.TOP_GRADE,top_grade+"")
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        if (result!=null & result.isFlag()){
                            UserBean user = new UserBean(uid,passWord,user_name,sex,classId,top_grade,null);
                            HeroApplication.getInstance().setCurrentUser(user);
                            DataHelper help = new DataHelper(mContext);
                            help.addUser(user,0);
                            Toast.makeText(RegisterActivity.this,"您的UID是: "+uid+" 已经为您自动填充",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this,"注册失败,该学号已被使用，请重新输入学号",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * show dialog to chose class
     */
    private void showChoseDialog() {
        inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.chose_class_layout, null);
        final Spinner choseDialogSpinnerArea = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_area);
        final Spinner choseDialogSpinnerCourse = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_course);
        final Spinner choseDialogSpinnerStartTime = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_startTime);
        initSpinner(choseDialogSpinnerArea, choseDialogSpinnerCourse, choseDialogSpinnerStartTime);
        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String area = areaMap.get(choseDialogSpinnerArea.getSelectedItem().toString());
                        String course = courseMap.get(choseDialogSpinnerCourse.getSelectedItem().toString());
                        String time = choseDialogSpinnerStartTime.getSelectedItem().toString();
                        getClassList(area, course, time);
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * get Classlist from server
     *
     * @param area
     * @param course
     * @param time
     */
    private void getClassList(String area, String course, String time) {
        Log.e(TAG, "getClassList: "+area+course+time );
        OkHttpUtils<ClassObj[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_CLASS)
                .addParam(I.IClass.B_AREA, area)
                .addParam(I.IClass.B_COURSE, course)
                .addParam(I.IClass.START_TIME, time)
                .targetClass(ClassObj[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ClassObj[]>() {
                    @Override
                    public void onSuccess(ClassObj[] result) {
                        classlist = new ArrayList<ClassObj>();
                        classlist = Utils.array2List(result);
                        classStr = new String[classlist.size()];
                        for (int i = 0; i < classlist.size(); i++) {
                            classStr[i] = classlist.get(i).getClass_name();
                        }
                        showclassDialog();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(RegisterActivity.this,"网络不通畅,请稍后再试",Toast.LENGTH_LONG).show();
                    }
                });

    }

    /**
     * show chose class dialog
     */
    private void showclassDialog() {
        Dialog dialog = new AlertDialog.Builder(mContext)
                .setItems(classStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClassObj classObj = classlist.get(which);
                        Message msg = Message.obtain();
                        msg.what = CHOSECLASS;
                        msg.obj = classObj;
                        mHandler.sendMessage(msg);
                        classId = classObj.getId();
                        classSimpleName = classObj.getSimple_name();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * init dialog's spinner
     * @param choseDialogSpinnerArea
     * @param choseDialogSpinnerCourse
     * @param choseDialogSpinnerStartTime
     */
    private void initSpinner(Spinner choseDialogSpinnerArea, Spinner choseDialogSpinnerCourse, Spinner choseDialogSpinnerStartTime) {
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, areaStr);
        areaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerArea.setAdapter(areaAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, courseStr);
        courseAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerCourse.setAdapter(courseAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, timeStr);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerStartTime.setAdapter(timeAdapter);
    }

}
