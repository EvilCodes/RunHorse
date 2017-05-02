package com.yujie.hero.tasks.examorexercise;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.yujie.hero.data.bean.ExamResultBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.TasksDataSource;
import com.yujie.hero.data.source.TasksRepository;
import com.yujie.hero.data.source.local.TasksLocalDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class EorEPresenter implements EorEContract.Presenter {

    EorEFragment mFragment;
    RemoteDataSource mRemoteDataSource;
    TasksRepository mTasksRepository;

    public EorEPresenter(TasksRepository mTasksRepository, RemoteDataSource mRemoteDataSource, EorEFragment mFragment) {
        Log.e("EorEPresenter", "mTasksRepository=" + mTasksRepository);
        this.mFragment = mFragment;
        this.mRemoteDataSource = mRemoteDataSource;
        this.mTasksRepository = mTasksRepository;
        mFragment.setPresenter(this);
    }

    //显示的逻辑通过定义相同的方法实现在View层逻辑操作的调用
    @Override
    public void showRunHorse() {
        mFragment.showRunHorse();

    }

    @Override
    public void initTitle() {
        mFragment.initTitle();

    }

    @Override
    public void showWordContent(String course_simple_name) {
        Log.e("EorEPresenter", "showWordContent="+course_simple_name);
        Log.e("EorEPresenter", "showWordcontent.mTasksRepository=" + mTasksRepository);
        mTasksRepository.getWordContentsTask(course_simple_name, new TasksDataSource.LoadWordContentBeansCallback() {
            @Override
            public void onWordContentBeansLoaded(List<WordContentBean> wordContentBeens) {
                Log.e("EorEPresenter", "wordContentBeens=" + wordContentBeens);
                if (wordContentBeens != null && wordContentBeens.size() != 0) {
                    StringBuilder sb = new StringBuilder();
                    for (WordContentBean word : wordContentBeens) {
                        sb.append(word.getWord() + " ");
                    }
                    String contentTxt = sb.substring(0, sb.length() - 1);
                    mFragment.getWordContent(contentTxt);

                }


            }

            @Override
            public void onWordContentBeansSaved(boolean isSaved) {

            }

            @Override
            public void onDataNotAvailable() {


            }
        });


    }

    @Override
    public void setTextChangedListener() {
        mFragment.setTextChangedListener();

    }

    @Override
    public void uploadGrade(UserBean currentUser, String speed) {
        mRemoteDataSource.uploadBestGradeTask(currentUser.getUid(), speed, new RemoteDataSource.UploadBestGradesCallback() {
            @Override
            public void uploadedBestGrades(Result result) {

                if (result != null && result.isFlag()) {
                    mFragment.showToast("个人最高成绩已更新");
                }
            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅,请稍后再试");

            }
        });

    }

    @Override
    public void addExerciseGreade(UserBean currentUser, final String speed,
                                  String course_simple_name, final int challengePoint) {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdFormatter.format(nowTime);
        mRemoteDataSource.uploadExerciseGradeTask(currentUser, nowDate, course_simple_name, speed, new RemoteDataSource.LoadExerciseGradeCallback() {
            @Override
            public void onExerciseGradeUpLoaded(Result result) {
                if (result != null && result.isFlag()) {
                    mFragment.showToast("练习成绩已经上传");
                    if (challengePoint != 0 && Integer.parseInt(speed) > challengePoint) {
                        mFragment.showToast("恭喜你,你成功的超越了你的对手");
                    }
                }

            }

            @Override
            public void onExerciseGradeNearlyLoaded(ExerciseBean[] result) {

            }

            @Override
            public void onExerciseTenGradesLoaded(ExerciseBean[] result) {

            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅,请稍后再试");

            }
        });

    }

    @Override
    public void addExamGrades(final UserBean currentUser, final String speed, final String course_simple_name) {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdFormatter.format(nowTime);
        mRemoteDataSource.uploadExamGradeTask(currentUser, nowDate, course_simple_name, speed, new RemoteDataSource.LoadExamGradeCallback() {
            @Override
            public void onExamGradeUpLoaded(Result result) {
                if (result != null && result.isFlag()) {
                    mFragment.showToast("成绩已经上传");
                    addExerciseGreade(currentUser, speed, course_simple_name, 0);
                } else {
                    mFragment.showToast("考试时间已过，无法上传考试成绩");
                }
            }

            @Override
            public void onExamGradeLoaded(ExamResultBean[] result) {
            }

            @Override
            public void onExamGradeSaved(boolean isSaved) {
            }

            @Override
            public void onDataNotAvailable() {
                mFragment.showToast("网络不通畅,请稍后再试");

            }
        });

    }

    @Override
    public void initCountDownTimer(long millisInFuture, long countDownInterval) {
        mFragment.initCountDownTimer(millisInFuture, countDownInterval);

    }

    @Override
    public void showDialog(String title, String message, DialogInterface.OnClickListener clickListener, DialogInterface.OnKeyListener keyListener) {
        mFragment.showDialog(title, message, clickListener, keyListener);

    }

    @Override
    public void showToast(String msg) {
        mFragment.showToast(msg);

    }

    @Override
    public void initAniData() {
        mFragment.initAniData();
    }

    @Override
    public void start() {

    }
}
