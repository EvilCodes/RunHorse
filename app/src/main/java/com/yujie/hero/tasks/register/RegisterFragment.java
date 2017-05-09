package com.yujie.hero.tasks.register;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.AreasBean;
import com.yujie.hero.data.bean.ClassObj;
import com.yujie.hero.data.bean.CourseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.StartTimeBean;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.tasks.login.LoginActivity;
import com.yujie.hero.utils.StartTargetActivity;
import com.yujie.hero.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.yujie.hero.R.id.register_activity_Button_chooseClass;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public class RegisterFragment extends Fragment implements RegisterContract.View, View.OnClickListener {

    @Bind(R.id.register_activity_EditText_inputUserName)
    EditText registerActivityEditTextInputUserName;
    @Bind(R.id.register_activity_EditText_inputPwd)
    EditText registerActivityEditTextInputPwd;
    @Bind(R.id.register_activity_RadioButton_boy)
    RadioButton registerActivityRadioButtonBoy;
    @Bind(R.id.register_activity_RadioButton_girl)
    RadioButton registerActivityRadioButtonGirl;
    @Bind(R.id.register_activity_EditText_stuNum)
    EditText registerActivityEditTextStuNum;
    @Bind(register_activity_Button_chooseClass)
    Button registerActivityButtonChooseClass;
    @Bind(R.id.register_activity_Button_register)
    Button registerActivityButtonRegister;
    private Context context;
    private RegisterContract.Presenter mPresenter;
    private String userName;
    private String pwd;
    private int sex;
    private String uid;
    private int classId;
    private int topGrade;
    private String classSimpleName;
    private static String TAG=RegisterFragment.class.getSimpleName();
    private Handler mHandler;
    private String stuNum;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        initHandler();
        initData();
        mPresenter.checkOnClickButton();
        return view;

    }

    private void initData() {

        mPresenter.goStartTimeTask();
        mPresenter.goCourseTask();
        mPresenter.goAeraTask();
    }

    private void initHandler() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                        ClassObj obj = (ClassObj) msg.obj;
                        registerActivityButtonChooseClass.setText(obj.getClass_name());
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_activity_Button_register:
                //post请求和get请求的区别post请求不能出现为空的状态另外明确方法内部新线程的问题
                boolean isNotNull = mPresenter.checkPersonMessage();

                if (isNotNull) {

                    mPresenter.goRegister(uid,pwd,userName,sex+"",classId+"",topGrade+"");
                }

                break;
            case R.id.register_activity_Button_chooseClass:

                mPresenter.showChoseClasses();

                break;
        }

    }

    @Override
    public void showRequestResult(String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showChoseDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.chose_class_layout, null);
        final Spinner choseDialogSpinnerArea = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_area);
        Log.e("RegisterFragment", "choseDialogSpinnerArea=" + choseDialogSpinnerArea);
        final Spinner choseDialogSpinnerCourse = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_course);
        final Spinner choseDialogSpinnerStartTime = (Spinner) view.findViewById(R.id.chose_dialog_Spinner_startTime);
        initSpinner(choseDialogSpinnerArea, choseDialogSpinnerCourse, choseDialogSpinnerStartTime);
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String area = areaMap.get(choseDialogSpinnerArea.getSelectedItem().toString());
                        String course = courseMap.get(choseDialogSpinnerCourse.getSelectedItem().toString());
                        String time = choseDialogSpinnerStartTime.getSelectedItem().toString();
                        mPresenter.goClassTask(area,course,time);
                    }
                })
                .create();
        dialog.show();
    }

    private void initSpinner(Spinner choseDialogSpinnerArea, Spinner choseDialogSpinnerCourse, Spinner choseDialogSpinnerStartTime) {
        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, areaStr);
        areaAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerArea.setAdapter(areaAdapter);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, courseStr);
        courseAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerCourse.setAdapter(courseAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, timeStr);
        timeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        choseDialogSpinnerStartTime.setAdapter(timeAdapter);


    }

    @Override
    public boolean checkPersonMessage() {
        userName = registerActivityEditTextInputUserName.getText().toString();
        pwd = registerActivityEditTextInputPwd.getText().toString();
        sex=registerActivityRadioButtonBoy.isChecked()?1:2;
        stuNum = registerActivityEditTextStuNum.getText().toString();

        if (userName.isEmpty()) {
            registerActivityEditTextInputUserName.setError("请输入用户名");
            registerActivityEditTextInputUserName.requestFocus();
            return false;
        }
        if (pwd.isEmpty()) {
            registerActivityEditTextInputPwd.setError("请输入密码");
            registerActivityEditTextInputPwd.requestFocus();
            return false;

        }
        if (sex==0){

         showRequestResult("性别为空，请重新设置");
            return false;

        }
        if (stuNum.isEmpty()){
        registerActivityEditTextStuNum.setError("没有输入,请重试");
        registerActivityEditTextStuNum.requestFocus();
            return false;

        }
    if(classId==0 | classSimpleName.isEmpty()){
        return false;

    }
    uid = classSimpleName+stuNum.toString();
        return true;


    }

    @Override
    public void setAreaList(AreasBean[] result) {
        arealist = Utils.array2List(result);
        areaMap = new HashMap<String, String>();
        areaStr = new ArrayList<String>();
        for (AreasBean area : arealist) {
            areaMap.put(area.getArea_name(), area.getSimple_name());
            areaStr.add(area.getArea_name());
        }


    }

    @Override
    public void setCourseList(CourseBean[] result) {
        courselist=Utils.array2List(result);
        courseMap = new HashMap<String, String>();
        courseStr = new ArrayList<String>();
        for (CourseBean courseBean : courselist) {
            courseMap.put(courseBean.getCourse_name(), courseBean.getSimple_name());
            courseStr.add(courseBean.getCourse_name());

        }


    }

    @Override
    public void setStartTimeList(StartTimeBean[] result) {
        timelist=Utils.array2List(result);
        timeStr = new ArrayList<String>();
        for (StartTimeBean timeBean : timelist) {
            timeStr.add(timeBean.getStart_time());
        }

    }

    @Override
    public void setClassList(ClassObj[] result) {
        classlist=Utils.array2List(result);
        classStr = new String[classlist.size()];
        for (int i=0;i<classlist.size();i++) {
            classStr[i] = classlist.get(i).getClass_name();

        }
        showChosedDialog();



    }



    @Override
    public void checkOnClickButton() {

        registerActivityButtonChooseClass.setOnClickListener(this);
        registerActivityButtonRegister.setOnClickListener(this);

    }



    @Override
    public void goRegister(Result result) {
        HeroApplication.getInstance().setCurrentUser(new UserBean(uid, pwd, userName, sex, classId,topGrade,null));
        StartTargetActivity.jumpToTargetActivity(context, LoginActivity.class);
        getActivity().finish();
    }


//内部接口方法的调用
    @Override
    public void showChosedDialog() {

        Dialog dialog = new AlertDialog.Builder(context)
                .setItems(classStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClassObj classObj = classlist.get(which);
                        Message msg = Message.obtain();
                        msg.obj = classObj;
                        mHandler.sendMessage(msg);
                        classId = classObj.getId();
                        classSimpleName = classObj.getSimple_name();
                    }
                })
                .create();
        dialog.show();

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        mPresenter = presenter;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();

    }
}
