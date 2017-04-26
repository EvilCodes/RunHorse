package com.yujie.hero.tasks.examorexercise;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yujie.hero.R;
import com.yujie.hero.activity.ExamResultActivity;
import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.db.DataHelper;
import com.yujie.hero.utils.MCountDownTimer;
import com.yujie.hero.utils.StartTargetActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class EorEFragment extends Fragment implements TextWatcher, EorEContract.View {
    @Bind(R.id.timer)
    TextView timer;
    @Bind(R.id.point)
    TextView point;
    @Bind(R.id.currentSpeed)
    TextView currentSpeed;
    @Bind(R.id.sporter)
    TextView sporter;
    @Bind(R.id.course)
    TextView course;
    @Bind(R.id.word_content)
    TextView wordContent;
    @Bind(R.id.edit_content)
    EditText editContent;
    @Bind(R.id.iv_runhorse)
    ImageView ivRunhorse;
    @Bind(R.id.iv_slowhorse)
    ImageView ivSlowhorse;
    @Bind(R.id.run_place)
    RelativeLayout runPlace;
    private Context context;
    private EorEContract.Presenter mPresenter;
    private boolean isBegan = false;
    private Dialog dialog;
    private String contentTxt;
    private String course_simple_name ;
    private String time;
    private String code;
    private int keyCount = 0;
    private int challengePoint = 0;
    private MCountDownTimer mc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);
        mPresenter.initTitle();
        mPresenter.setTextChangedListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.showRunHorse();
        mPresenter.showWordContent(course_simple_name);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(!isBegan){
            mc.start();
            isBegan = true;
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            int pos = s.length() - 1;
            char c = s.charAt(pos);
            if (c!=contentTxt.charAt(pos)) {
                s.delete(pos,pos+1);
                mPresenter.showToast("input error");
            }
            keyCount++;
            if (s.length()==contentTxt.length()){
                mPresenter.showWordContent(course_simple_name);
                editContent.setText("");
            }
        }

    }

    @Override
    public void showRunHorse() {
        ivRunhorse.setImageResource(R.drawable.horses_running);
        TranslateAnimation animation = new TranslateAnimation(0,1000,0,0);
        animation.setDuration(5000);
        ivRunhorse.setAnimation(animation);
        ivRunhorse.setBackgroundColor(Color.TRANSPARENT);
        AnimationDrawable anima= (AnimationDrawable) ivRunhorse.getDrawable();
        anima.start();

        ivSlowhorse.setImageResource(R.drawable.horses_running);
        ivSlowhorse.setBackgroundColor(Color.TRANSPARENT);
        AnimationDrawable animSlow = (AnimationDrawable) ivSlowhorse.getDrawable();
        animSlow.start();

    }

    @Override
    public void initTitle() {
        String action_code =getActivity().getIntent().getStringExtra("action_code");
        Log.e("EorEFragment", "action_code=" + action_code);
        if (action_code!=null){
            String[] action = action_code.split(",");
            course_simple_name = action[0];
            time = action[1];
            code = action[2];
            challengePoint = Integer.parseInt(action[3]);
        }
        mPresenter.initCountDownTimer((Long.parseLong(time)) * 60 * 1000,1000);
        timer.setText((Integer.parseInt(time)*60)+"");
        point.setText(HeroApplication.getInstance().getCurrentUser().getTop_grade()+"");
        sporter.setText(HeroApplication.getInstance().getCurrentUser().getUser_name());
        course.setText(HeroApplication.getInstance().getCurrentTestCourse());

    }

    @Override
    public void getWordContent(String contentTxt) {
        //解决contentText.chatAt()为空指针的错误
        this.contentTxt = contentTxt;
        wordContent.setText(contentTxt);


    }

    @Override
    public void setTextChangedListener() {
        editContent.addTextChangedListener(this);

    }

    @Override
    public void initCountDownTimer(long millisInFuture, long countDownInterval) {
        mc=new MCountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (getActivity().isFinishing()){
                    mc.cancel();
                }else {
                    long l = millisUntilFinished / 1000;
                    timer.setText(""+l);
                    currentSpeed.setText((int)(((float)keyCount/((Integer.parseInt(time)*60)-l))*60)+"");
                }

            }

            @Override
            public void onFinish() {
                isBegan = false;
                editContent.setEnabled(false);
                final String speed = currentSpeed.getText().toString();
                final UserBean currentUser = HeroApplication.getInstance().getCurrentUser();
                if (code.equals(HeroApplication.EXERCISE_CODE)) {
                    mPresenter.addExerciseGreade(currentUser,speed,course_simple_name,challengePoint);
                } else if (code.equals(HeroApplication.EXAM_CODE)) {
                    mPresenter.addExamGrades(currentUser,speed,course_simple_name);
                }
                if (Integer.parseInt(speed) > currentUser.getTop_grade()) {
                    mPresenter.showDialog("成绩单", "你本次的打字速度是:" + speed + ",你成功超越了自己", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editContent.setEnabled(true);
                            editContent.setText("");
                            keyCount = 0;
                            timer.setText("" + Integer.parseInt(time) * 60);
                            currentUser.setTop_grade(Integer.parseInt(speed));

                            if (code.equals(HeroApplication.EXAM_CODE)) {
                                StartTargetActivity.jumpToTargetActivity(context, ExamResultActivity.class);
                            }
                            getActivity().finish();


                        }
                    }, new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                } else {
                    mPresenter.showDialog("成绩单", "你本次的打字速度是:" + speed + ",你需要更加努力哟", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editContent.setEnabled(true);
                            keyCount = 0;
                            timer.setText("" + Integer.parseInt(time) * 60);
                            if (code.equals(HeroApplication.EXAM_CODE)) {
                                startActivity(new Intent(context, ExamResultActivity.class));
                            }
                            getActivity().finish();

                        }
                    }, new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }

            }
        };



    }

//实现Dialog接口方法的自定义即接口传参
    @Override
    public void showDialog(String title, String message, DialogInterface.OnClickListener clickListener, DialogInterface.OnKeyListener keyListener) {
        dialog=new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok",clickListener)
                .setOnKeyListener(keyListener)
                .setCancelable(false)
                .create();
        dialog.show();

    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(EorEContract.Presenter presenter) {
        mPresenter=presenter;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
