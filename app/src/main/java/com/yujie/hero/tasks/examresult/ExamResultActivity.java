package com.yujie.hero.tasks.examresult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.R;
import com.yujie.hero.utils.AddFragmentToActivity;

public class ExamResultActivity extends AppCompatActivity {
    public static final String TAG = ExamResultActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        Fragment examResultFragment = getSupportFragmentManager().findFragmentById(R.id.fmExamResult);
        if (examResultFragment == null) {
            examResultFragment  = new ExamResultFragment();
        }
        new ExamResultPresenter((ExamResultFragment) examResultFragment);

        AddFragmentToActivity.addFragmentToActivity(getSupportFragmentManager(),R.id.fmExamResult,examResultFragment);
    }


}
