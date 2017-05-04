package com.yujie.hero.data.source;

import android.support.annotation.NonNull;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public interface RemoteDataSource {


    interface UploadBestGradesCallback{

        void uploadedBestGrades(Result result);

        void onDataNotAvailable();

    }


    interface LoadTuserCallback {

        void onUserBeanLoginLoaded(UserBean userBeen);

        void onUserBeanLoadedByUid(UserBean userBeen);

        void onUserBeanSaved(boolean isSaved);

        void onTuserPasswordUpdated(Result result);

        void onDataNotAvailable();
    }


    interface LoadWordContentBeansCallback {

        void onWordContentBeansLoaded(List<WordContentBean> wordContentBeens);

        void onWordContentBeansSaved(boolean isSaved);

        void onDataNotAvailable();
    }


    interface LoadExerciseGradeCallback {

        void onExerciseGradeUpLoaded(Result result);

        void onExerciseGradeNearlyLoaded(ExerciseBean[] result);

        void onExerciseTenGradesLoaded(ExerciseBean[] result);

        void onDataNotAvailable();


    }

    interface LoadExamGradeCallback{

        void onExamGradeUpLoaded(Result result);

        void onExamGradeLoaded(ExamResultBean[] result);

        void onExamGradeSaved(boolean isSaved);


        void onDataNotAvailable();

    };


    interface LoadExerciseSortInTimeCallback{
        void onExerciseSortInTimeLoaded(ExerciseBean[] result);

        void onExerciseSortInTimeSaved(boolean isSaved);



        void onDataNotAvailable();
    }

    interface LoadExerciseSortInCourseCallback{

        void onExerciseSortInCourseLoaded(ExerciseBean[] result);

        void onExerciseSortInCourseSaved(boolean isSaved);


        void onDataNotAvailable();
    }

    interface LoadExerciseSortInClassCallback{

        void onExerciseSortInClass(ExerciseBean[] result);

        void onExerciseSortInClassSaved(boolean isSaved);



        void onDataNotAvailable();
    }

    interface LoadExamAvgGradeCallback{

        void onExamAvgGradeLoaded(ExamGradeAvgBean[] result);

        void onExamAvgGradeSaved(boolean isSaved);


        void onDataNotAvailable();
    }

    interface LoadClassGradeCallback{

        void onClassGradeLoaded(ExamClassGradeBean[] result);

        void onGlassGradeSaved(boolean isSaved);

        void onDataNotAvailable();
    }


    /**
     * 有关操作用户数据库的接口集
     *
     */
    void getExamAvgGrade(@NonNull String exam_id,@NonNull LoadExamAvgGradeCallback callback );
    void saveExamAvgGrade(@NonNull ExamGradeAvgBean[] result, @NonNull LoadExamAvgGradeCallback callback);

    void getClassGrade(@NonNull String exam_id, @NonNull String b_class, @NonNull LoadClassGradeCallback callback);
    void saveClassGrade(@NonNull ClassExamGradeBean[] result,@NonNull LoadClassGradeCallback callback);


    void getTuserTaskByUid(@NonNull LoadTuserCallback callback, @NonNull String uid);
    void getNetWorkTuserTask(@NonNull LoadTuserCallback callback, @NonNull String uid,
                             @NonNull String pwd);
    void updateTuserPasswordTask(@NonNull String pwd,  @NonNull LoadTuserCallback callback);
    void saveTuserTask(@NonNull UserBean user, @NonNull LoadTuserCallback callback);

    void uploadExerciseGradeTask(@NonNull UserBean currentUser,@NonNull String nowDate,@NonNull String course_simple_name, @NonNull String speed, @NonNull LoadExerciseGradeCallback callback);

    void uploadExamGradeTask(@NonNull UserBean currentUser, @NonNull String nowDate,@NonNull String course_simple_name,@NonNull String speed,@NonNull LoadExamGradeCallback callback);
    void getExamGradeTask(@NonNull String exam_id,@NonNull LoadExamGradeCallback callback);


    void uploadBestGradeTask(@NonNull String uid, @NonNull String speed,@NonNull UploadBestGradesCallback callback);

    void getWordContentsTask(@NonNull String course_id, @NonNull LoadWordContentBeansCallback callback);
    void saveWordContens(@NonNull ArrayList<WordContentBean> wordList, @NonNull LoadWordContentBeansCallback callback);

    void getNearlyExerciseGrades(@NonNull String username,@NonNull LoadExerciseGradeCallback callback );

    void getTenExerciseGrades(@NonNull String username,@NonNull LoadExerciseGradeCallback callback );

    void getExerciseGradeSortInTime(@NonNull String startTime, @NonNull LoadExerciseSortInTimeCallback callback);
    void saveExerciseGradeSortInTime(@NonNull ExerciseBean[] result,@NonNull LoadExerciseSortInTimeCallback callback);


    void getExerciseGradeSortInClass(@NonNull String b_class,@NonNull LoadExerciseSortInClassCallback callback);
    void saveExerciseGradeSortInClass(@NonNull ExerciseBean[] result,@NonNull LoadExerciseSortInClassCallback callback);


    void getExerciseGradeSortInCourse(@NonNull String courseId,@NonNull LoadExerciseSortInCourseCallback callback );
    void saveExerciseGradeSortInCourse(@NonNull ExerciseBean[] result,@NonNull LoadExerciseSortInCourseCallback callback);


interface LoadAreasTaskCallback{

    void onAreasTaskLoaded(AreasBean[] result);

    void onAreasTaskSaved(boolean isSaved);

    void onDataNotAvailable();

}

    void getAreasTask(@NonNull LoadAreasTaskCallback callback);
    void saveAreas(@NonNull AreasBean[] areas,@NonNull LoadAreasTaskCallback callback);

    interface LoadCourseTaskCallback{
        void onCourseLoaded(CourseBean[] result);
        void onCourseSaved(boolean isSaved);
        void onDataNotAvailable();
    }

    void getCourseTask(@NonNull LoadCourseTaskCallback callback);
    void saveCourse(@NonNull CourseBean[] result,@NonNull LoadCourseTaskCallback callback);

    interface LoadClassStartTimeCallback{
        void onClassStartTimeLoaded(StartTimeBean[] result);
        void onClassStartTimeSaved(boolean isSaved);
        void onDataNotAvailable();
    }

    void getStartTimeTask(@NonNull LoadClassStartTimeCallback callback);
    void saveStartTime(@NonNull StartTimeBean[] result,@NonNull LoadClassStartTimeCallback callback);

    interface LoadClassesCallback{
        void onClassesLoaded(ClassObj[] result);
        void onClassesSaved(boolean isSaved);
        void onDataNotAvailable();
    }

    void getClassListTask(@NonNull String area,@NonNull String course,@NonNull
                          String time,@NonNull LoadClassesCallback callback);
    void saveClassList(@NonNull ClassObj[] result, @NonNull LoadClassesCallback callback);


    interface LoadClassNameCallback{
        void onClassNameLoaded(Result result);
        void onDataNotAvailable();
    }
    void getClassNameByIdTask(@NonNull String exam_id,@NonNull LoadClassNameCallback callback);


    interface LoadExamNowCallback{
        void onExamNowLoaded(ExamBean[] result);
        void onExamNowSaved(boolean isSaved);
        void onDataNotAvailable();

    }
    void getExamNowTask(@NonNull LoadExamNowCallback callback);
    void saveExamNow(@NonNull ExamBean[] result,@NonNull LoadExamNowCallback callback);

    interface upLoadAvatarCallback{
        void onAvatarUploaded(Result result);
        void onDataNotAvailable();

    }
    void upLoadAvatarTask(@NonNull String uid, @NonNull File file,@NonNull upLoadAvatarCallback callback);

    interface registerTaskCallback{
        void onRegisterSuccess(Result result);

        void onDataNotAvailable();
    }

    void registerTask(@NonNull String uid, @NonNull String pwd,
                      @NonNull String username, @NonNull String sex,
                      @NonNull String classId, @NonNull String top_grade,
                      @NonNull registerTaskCallback callback);



}
