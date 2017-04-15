package com.yujie.hero.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.yujie.hero.application.HeroApplication;
import com.yujie.hero.application.I;
import com.yujie.hero.data.bean.AreasBean;
import com.yujie.hero.data.bean.ClassExamGradeBean;
import com.yujie.hero.data.bean.ClassObj;
import com.yujie.hero.data.bean.CourseBean;
import com.yujie.hero.data.bean.ExamBean;
import com.yujie.hero.data.bean.ExamGradeAvgBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.StartTimeBean;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.source.RemoteDataSource;
import com.yujie.hero.data.source.TasksDataSource;
import com.yujie.hero.utils.OkHttpUtils;
import com.yujie.hero.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class TasksRemoteDataSource implements RemoteDataSource {

    private static TasksRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, UserBean> USERBEAN_SERVICE_DATA;
    private final static Map<String, ExerciseBean> EXERCISEBEAN_SERVICE_DATA;
    private final static Map<Integer, WordContentBean> WORDCONTENTBEAN_SERVICE_DATA;
    static {

        USERBEAN_SERVICE_DATA = new LinkedHashMap<>(7);
        EXERCISEBEAN_SERVICE_DATA = new LinkedHashMap<>(7);
        WORDCONTENTBEAN_SERVICE_DATA = new LinkedHashMap<>(3);

    }
    public static TasksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }
    private TasksRemoteDataSource() {}

    @Override
    public void getNetWorkTuserTask(@NonNull final LoadTuserCallback callback, @NonNull String uid,
                                    @NonNull String pwd) {
        OkHttpUtils<UserBean> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_LOGIN)
                .addParam(I.User.UID,uid)
                .addParam(I.User.PWD,pwd)
                .post()
                .targetClass(UserBean.class)
                .execute(new OkHttpUtils.OnCompleteListener<UserBean>() {
                    @Override
                    public void onSuccess(UserBean result) {
                        if (result != null) {
                            USERBEAN_SERVICE_DATA.put(result.getUid(), result);
                            callback.onUserBeanLoginLoaded(result);
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("RemoteDataSource","getNetWorkTuserTask.error="+error);
                        callback.onDataNotAvailable();

                    }
                });
    }

    @Override
    public void updateTuserPasswordTask(@NonNull String pwd, @NonNull LoadTuserCallback callback) {

    }

    @Override
    public void getExamAvgGrade(@NonNull String exam_id, @NonNull LoadExamAvgGradeCallback callback) {

    }

    @Override
    public void saveExamAvgGrade(@NonNull ExamGradeAvgBean[] result, @NonNull LoadExamAvgGradeCallback callback) {

    }

    @Override
    public void getClassGrade(@NonNull String exam_id, @NonNull String b_class, @NonNull LoadClassGradeCallback callback) {

    }

    @Override
    public void saveClassGrade(@NonNull ClassExamGradeBean[] result, @NonNull LoadClassGradeCallback callback) {

    }

    @Override
    public void getTuserTaskByUid(@NonNull LoadTuserCallback callback, @NonNull String uid) {
        if (USERBEAN_SERVICE_DATA != null) {
            UserBean userBean = USERBEAN_SERVICE_DATA.get(uid);
            if (userBean != null) {
                callback.onUserBeanLoadedByUid(userBean);
            }
        } else {
            callback.onUserBeanLoadedByUid(null);
        }
    }



    @Override
    public void saveTuserTask(@NonNull UserBean user, @NonNull int status, @NonNull LoadTuserCallback callback) {

    }

    @Override
    public void uploadExerciseGradeTask(@NonNull UserBean currentUser, @NonNull String speed, @NonNull LoadExerciseGradeCallback callback) {

    }

    @Override
    public void uploadExamGradeTask(@NonNull UserBean currentUser, @NonNull String speed, @NonNull LoadExamGradeCallback callback) {

    }

    @Override
    public void getExamGradeTask(@NonNull String exam_id, @NonNull LoadExamGradeCallback callback) {

    }

    @Override
    public void uploadBestGradeTask(@NonNull String uid, @NonNull String speed) {

    }

    @Override
    public void getWordContentsTask(@NonNull String course_id, @NonNull final LoadWordContentBeansCallback callback) {
        OkHttpUtils<WordContentBean[]> utils = new OkHttpUtils<WordContentBean[]>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_DOWNLOAD_CONTENT)
                .targetClass(WordContentBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<WordContentBean[]>() {
                    @Override
                    public void onSuccess(WordContentBean[] result) {
                        if (result != null && result.length != 0) {
                            ArrayList<WordContentBean> wordContentList = Utils.array2List(result);
                            if (wordContentList != null && wordContentList.size() != 0) {
                                saveWordContens(wordContentList,callback);
                                callback.onWordContentBeansLoaded(wordContentList);
                            } else {
                                callback.onWordContentBeansLoaded(null);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();

                    }
                });

    }



    @Override
    public void getNearlyExerciseGrades(@NonNull String username, @NonNull LoadExerciseGradeCallback callback) {

    }

    @Override
    public void getTenExerciseGrades(@NonNull String username, @NonNull LoadExerciseGradeCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInTime(@NonNull String startTime, @NonNull LoadExerciseSortInTimeCallback callback) {

    }

    @Override
    public void saveExerciseGradeSortInTime(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInTimeCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInClass(@NonNull int b_class, @NonNull LoadExerciseSortInClassCallback callback) {

    }

    @Override
    public void saveExerciseGradeSortInClass(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInClassCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInCourse(@NonNull String courseId, @NonNull LoadExerciseSortInCourseCallback callback) {

    }

    @Override
    public void saveExerciseGradeSortInCourse(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInCourseCallback callback) {

    }

    @Override
    public void getAreasTask(@NonNull LoadAreasTaskCallback callback) {

    }

    @Override
    public void saveAreas(@NonNull AreasBean[] areas, @NonNull LoadAreasTaskCallback callback) {

    }

    @Override
    public void getCourseTask(@NonNull LoadCourseTaskCallback callback) {

    }

    @Override
    public void saveCourse(@NonNull CourseBean[] result, @NonNull LoadCourseTaskCallback callback) {

    }

    @Override
    public void getStartTimeTask(@NonNull LoadClassStartTimeCallback callback) {

    }

    @Override
    public void saveStartTime(@NonNull StartTimeBean[] result, @NonNull LoadClassStartTimeCallback callback) {

    }

    @Override
    public void getClassListTask(@NonNull String area, @NonNull String course, @NonNull String time, @NonNull LoadClassesCallback callback) {

    }

    @Override
    public void saveClassList(@NonNull ClassObj[] result, @NonNull LoadClassesCallback callback) {

    }

    @Override
    public void getClassNameByIdTask(@NonNull String exam_id, @NonNull LoadClassNameCallback callback) {

    }

    @Override
    public void getExamNowTask(@NonNull LoadExamNowCallback callback) {

    }

    @Override
    public void saveExamNow(@NonNull ExamBean[] result, @NonNull LoadExamNowCallback callback) {

    }

    @Override
    public void upLoadAvatarTask(@NonNull String uid, @NonNull File file, @NonNull upLoadAvatarCallback callback) {

    }

    @Override
    public void registerTask(@NonNull String uid, @NonNull String pwd, @NonNull String username, @NonNull String sex, @NonNull String classId, @NonNull String top_grade, @NonNull registerTaskCallback callback) {

    }

    @Override
    public void saveWordContens(@NonNull ArrayList<WordContentBean> wordList, @NonNull LoadWordContentBeansCallback callback) {
        for (WordContentBean wordContentBean : wordList) {
            WORDCONTENTBEAN_SERVICE_DATA.put(wordContentBean.getId(), wordContentBean);
        }
        callback.onWordContentBeansSaved(true);


    }




}
