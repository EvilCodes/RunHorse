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
import com.yujie.hero.data.bean.ExamClassGradeBean;
import com.yujie.hero.data.bean.ExamGradeAvgBean;
import com.yujie.hero.data.bean.ExamResultBean;
import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.StartTimeBean;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.source.RemoteDataSource;
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
    public TasksRemoteDataSource() {

    }

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

    /**
     * 参数待修改
     * @param pwd
     * @param callback
     */
    @Override
    public void updateTuserPasswordTask(@NonNull String pwd, @NonNull final LoadTuserCallback callback) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_UPDATE_USER)
                .addParam(I.User.UID,HeroApplication.getInstance().getCurrentUser().getUid())
                .addParam(I.User.USER_NAME,HeroApplication.getInstance().getCurrentUser().getUser_name())
                .addParam(I.User.PWD,pwd)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        callback.onTuserPasswordUpdated(result);
                    }
                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void getExamAvgGrade(@NonNull String exam_id, @NonNull final LoadExamAvgGradeCallback callback) {
        OkHttpUtils<ExamGradeAvgBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_CLASS_AVG_GRADE)
                .addParam(I.ExamGrade.EXAM_ID,exam_id)
                .targetClass(ExamGradeAvgBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamGradeAvgBean[]>() {
                    @Override
                    public void onSuccess(ExamGradeAvgBean[] result) {
                        callback.onExamAvgGradeLoaded(result);
                    }
                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();

                    }
                });

    }

    @Override
    public void saveExamAvgGrade(@NonNull ExamGradeAvgBean[] result, @NonNull LoadExamAvgGradeCallback callback) {

    }

    @Override
    public void getClassGrade(@NonNull String exam_id, @NonNull String b_class, @NonNull final LoadClassGradeCallback callback) {
        OkHttpUtils<ExamClassGradeBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_GET_CLASS_GRADE_LIST)
                .addParam(I.ExamGrade.EXAM_ID,exam_id)
                .addParam(I.ExamGrade.B_CLASS,b_class)
                .targetClass(ExamClassGradeBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamClassGradeBean[]>() {
                    @Override
                    public void onSuccess(ExamClassGradeBean[] result) {
                       callback.onClassGradeLoaded(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

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
    public void saveTuserTask(@NonNull UserBean user,  @NonNull LoadTuserCallback callback) {


    }

    @Override
    public void uploadExerciseGradeTask(@NonNull UserBean currentUser, @NonNull String nowDate, @NonNull String course_simple_name, @NonNull String speed, @NonNull final LoadExerciseGradeCallback callback) {

        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_ADD_EXERCISE_GRADE)
                .addParam(I.Exercise.GRADE,speed)
                .addParam(I.Exercise.EXE_TIME,nowDate)
                .addParam(I.Exercise.COURSE_ID,course_simple_name)
                .addParam(I.Exercise.USER_NAME,currentUser.getUser_name())
                .addParam(I.Exercise.B_CLASS,currentUser.getB_class()+"")
                .addParam(I.Exercise.START_TIME,currentUser.getUid().substring(1,7))
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        callback.onExerciseGradeUpLoaded(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void uploadExamGradeTask(@NonNull UserBean currentUser, @NonNull String nowDate, @NonNull String course_simple_name, @NonNull String speed, @NonNull final LoadExamGradeCallback callback) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_ADD_EXAM_GRADE)
                .addParam(I.ExamGrade.EXAM_ID,HeroApplication.getInstance().getCURRENT_EXAM_ID()+"")
                .addParam(I.ExamGrade.GRADE,speed)
                .addParam(I.ExamGrade.SUBMIT_TIME,nowDate)
                .addParam(I.ExamGrade.USER_NAME,currentUser.getUser_name())
                .addParam(I.ExamGrade.B_CLASS,currentUser.getB_class()+"")
                .addParam(I.ExamGrade.COURSE_ID,course_simple_name)
                .addParam(I.ExamGrade.B_START_TIME,currentUser.getUid().substring(1,7))
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {

                        callback.onExamGradeUpLoaded(result);

                    }
                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }



    @Override
    public void getExamGradeTask(@NonNull String exam_id, @NonNull final LoadExamGradeCallback callback) {
        OkHttpUtils<ExamResultBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_EXAM_GRADE)
                .addParam(I.ExamGrade.EXAM_ID, exam_id)
                .targetClass(ExamResultBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamResultBean[]>() {
                    @Override
                    public void onSuccess(ExamResultBean[] result) {
                        callback.onExamGradeLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();

                    }
                });

    }

    @Override
    public void uploadBestGradeTask(@NonNull String uid, @NonNull String speed, @NonNull final UploadBestGradesCallback callback) {
        OkHttpUtils<Result> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_UPDATE_BEST_GRADE)
                .addParam(I.User.UID, uid)
                .addParam(I.User.TOP_GRADE, speed + "")
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                        callback.uploadedBestGrades(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();

                    }
                });
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
    public void getNearlyExerciseGrades(@NonNull String username, @NonNull final LoadExerciseGradeCallback callback) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GETNEARLYGRADES)
                .addParam(I.User.USER_NAME, username)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        callback.onExerciseGradeNearlyLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();

                    }
                });

    }

    @Override
    public void getTenExerciseGrades(@NonNull String username, @NonNull LoadExerciseGradeCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInTime(@NonNull String startTime, @NonNull final LoadExerciseSortInTimeCallback callback) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_SORT_IN_TIME)
                .addParam(I.Exercise.START_TIME, startTime)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        callback.onExerciseSortInTimeLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveExerciseGradeSortInTime(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInTimeCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInClass(@NonNull String b_class, @NonNull final LoadExerciseSortInClassCallback callback) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_SORT_IN_CLASS)
                .addParam(I.User.B_CLASS, b_class)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        callback.onExerciseSortInClass(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveExerciseGradeSortInClass(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInClassCallback callback) {

    }

    @Override
    public void getExerciseGradeSortInCourse(@NonNull String courseId, @NonNull final LoadExerciseSortInCourseCallback callback) {
        OkHttpUtils<ExerciseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_SORT_IN_COURSE)
                .addParam(I.Exercise.COURSE_ID, courseId)
                .targetClass(ExerciseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<ExerciseBean[]>() {
                    @Override
                    public void onSuccess(ExerciseBean[] result) {
                        callback.onExerciseSortInCourseLoaded(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveExerciseGradeSortInCourse(@NonNull ExerciseBean[] result, @NonNull LoadExerciseSortInCourseCallback callback) {

    }

    @Override
    public void getAreasTask(@NonNull final LoadAreasTaskCallback callback) {
        OkHttpUtils<AreasBean[]> utils = new OkHttpUtils();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_AREA)
                .targetClass(AreasBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<AreasBean[]>() {
                    @Override
                    public void onSuccess(AreasBean[] result) {
                            callback.onAreasTaskLoaded(result);
                    }
                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });

    }

    @Override
    public void saveAreas(@NonNull AreasBean[] areas, @NonNull LoadAreasTaskCallback callback) {

    }

    @Override
    public void getCourseTask(@NonNull final LoadCourseTaskCallback callback) {
        OkHttpUtils<CourseBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_COURSE)
                .targetClass(CourseBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<CourseBean[]>() {
                    @Override
                    public void onSuccess(CourseBean[] result) {
                            callback.onCourseLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });


    }

    @Override
    public void saveCourse(@NonNull CourseBean[] result, @NonNull LoadCourseTaskCallback callback) {




    }

    @Override
    public void getStartTimeTask(@NonNull final LoadClassStartTimeCallback callback) {
        OkHttpUtils<StartTimeBean[]> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST, I.Request.REQUEST_GET_TIME)
                .targetClass(StartTimeBean[].class)
                .execute(new OkHttpUtils.OnCompleteListener<StartTimeBean[]>() {
                    @Override
                    public void onSuccess(StartTimeBean[] result) {
                        callback.onClassStartTimeLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });




    }

    @Override
    public void saveStartTime(@NonNull StartTimeBean[] result, @NonNull LoadClassStartTimeCallback callback) {

    }

    @Override
    public void getClassListTask(@NonNull String area, @NonNull String course, @NonNull String time, @NonNull final LoadClassesCallback callback) {
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
                        callback.onClassesLoaded(result);

                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });


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
    public void registerTask(@NonNull String uid, @NonNull String pwd, @NonNull String username, @NonNull String sex, @NonNull String classId, @NonNull String top_grade, @NonNull final registerTaskCallback callback) {
        OkHttpUtils<Result> utils = new OkHttpUtils<>();
        utils.url(HeroApplication.SERVER_ROOT)
                .addParam(I.REQUEST,I.Request.REQUEST_REGISTER)
                .addParam(I.User.UID,uid)
                .addParam(I.User.PWD,pwd)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.SEX,sex)
                .addParam(I.User.B_CLASS,classId)
                .addParam(I.User.TOP_GRADE,top_grade)
                .post()
                .targetClass(Result.class)
                .execute(new OkHttpUtils.OnCompleteListener<Result>() {
                    @Override
                    public void onSuccess(Result result) {
                            callback.onRegisterSuccess(result);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onDataNotAvailable();
                    }
                });


    }

    @Override
    public void saveWordContens(@NonNull ArrayList<WordContentBean> wordList, @NonNull LoadWordContentBeansCallback callback) {
        for (WordContentBean wordContentBean : wordList) {
            WORDCONTENTBEAN_SERVICE_DATA.put(wordContentBean.getId(), wordContentBean);
        }
        callback.onWordContentBeansSaved(true);


    }




}
