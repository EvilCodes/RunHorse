package com.yujie.hero.data.source;

import android.support.annotation.NonNull;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public interface TasksDataSource {


    interface LoadTuserCallback {

        void onUserBeanLoginLoaded(UserBean userBeen);

        void onUserBeanLoadedByUid(UserBean userBeen);

        void onUserBeanSaved(boolean isSaved);

        void onUserBeanUpdated(boolean isUpdated);

        void onStatusUpdated(boolean isStatusUpdated);

        void onDataNotAvailable();
    }


    interface LoadWordContentBeansCallback {

        void onWordContentBeansLoaded(List<WordContentBean> wordContentBeens);

        void onWordContentBeansSaved(boolean isSaved);

        void onDataNotAvailable();
    }


    interface LoadExerciseGradeCallback {

        void onExerciseGradeSaved(boolean isSaved);

        void onDataNotAvailable();

    }

    /**
     * 有关操作用户数据库的接口集
     *
     * @param callback
     */
    void getLoginTuserTask(@NonNull LoadTuserCallback callback);
    void getTuserTaskByUid(@NonNull LoadTuserCallback callback, @NonNull String uid);
    void getNetWorkTuserTask(@NonNull LoadTuserCallback callback, @NonNull String uid,
                             @NonNull String pwd);
    void updateTuserTask(@NonNull UserBean userBean, @NonNull int status, @NonNull LoadTuserCallback callback);
    void updateTuserStatusTask(@NonNull int status, @NonNull String username, @NonNull LoadTuserCallback callback);
    void saveTuserTask(@NonNull UserBean user, @NonNull int status, @NonNull LoadTuserCallback callback);
    void saveExerciseGradeTask(@NonNull ExerciseBean user, @NonNull int status, @NonNull LoadExerciseGradeCallback callback);


    void getWordContentsTask(@NonNull String course_id, @NonNull LoadWordContentBeansCallback callback);
    void saveWordWordContensTask(@NonNull ArrayList<WordContentBean> wordList, @NonNull LoadWordContentBeansCallback callback);


}
