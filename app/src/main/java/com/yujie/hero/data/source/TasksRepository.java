package com.yujie.hero.data.source;

import android.support.annotation.NonNull;

import com.yujie.hero.data.bean.ExerciseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.source.local.TasksLocalDataSource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class TasksRepository implements TasksDataSource {
    private static TasksRepository INSTANCE = null;

    private final RemoteDataSource mTasksRemoteDataSource;

    private final TasksDataSource mTasksLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, UserBean> mCachedUserBeanTasks;
    Map<String, WordContentBean> mCachedWordContentTasks;
    Map<String, ExerciseBean> mCachedExerciseBeanTasks;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private TasksRepository(@NonNull RemoteDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource) {

            mTasksRemoteDataSource = tasksRemoteDataSource;

            mTasksLocalDataSource = tasksLocalDataSource;
    }

    boolean mUserBeanCacheIsDirty = false;
    boolean mWordContentCacheIsDirty = false;
    boolean mExerciseBeanCacheIsDirty = false;


    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(RemoteDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     *
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getLoginTuserTask(@NonNull LoadTuserCallback callback) {
        mTasksLocalDataSource.getLoginTuserTask(callback);


    }

    @Override
    public void getTuserTaskByUid(@NonNull LoadTuserCallback callback, @NonNull String uid) {


    }


    private void refreshLocalDataSource(UserBean userBeen, LoadTuserCallback callback) {
        ((TasksLocalDataSource) mTasksLocalDataSource).cleanData("t_user");


    }

    private void refreshCache(UserBean userBeen) {
        if (mCachedUserBeanTasks == null) {
            mCachedUserBeanTasks = new LinkedHashMap<>();

        }
        mCachedUserBeanTasks.clear();
        mCachedUserBeanTasks.put(userBeen.getUid(), userBeen);
        mUserBeanCacheIsDirty = false;
    }

    @Override
    public void getNetWorkTuserTask(@NonNull final LoadTuserCallback callback, @NonNull final String uid, @NonNull final String pwd) {

        final UserBean cachedUserBean = getUserBeanTaskWithId(uid);

        // Respond immediately with cache if available
        if (cachedUserBean != null) {
            callback.onUserBeanLoginLoaded(cachedUserBean);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mTasksLocalDataSource.getTuserTaskByUid(new LoadTuserCallback() {
            @Override
            public void onUserBeanLoginLoaded(UserBean userBeen) {

            }

            @Override
            public void onUserBeanLoadedByUid(UserBean userBeen) {
                callback.onUserBeanLoginLoaded(userBeen);


            }

            @Override
            public void onUserBeanSaved(boolean isSaved) {

            }

            @Override
            public void onUserBeanUpdated(boolean isUpdated) {

            }

            @Override
            public void onStatusUpdated(boolean isStatusUpdated) {

            }

            @Override
            public void onDataNotAvailable() {

                mTasksRemoteDataSource.getNetWorkTuserTask(new RemoteDataSource.LoadTuserCallback() {
                    @Override
                    public void onUserBeanLoginLoaded(UserBean userBeen) {

                        callback.onUserBeanLoginLoaded(userBeen);

                    }

                    @Override
                    public void onUserBeanLoadedByUid(UserBean userBeen) {

                    }

                    @Override
                    public void onUserBeanSaved(boolean isSaved) {

                    }

                    @Override
                    public void onTuserPasswordUpdated(Result result) {

                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();

                    }
                }, uid, pwd);


            }
        }, uid);
    }


    private UserBean getUserBeanTaskWithId(String uid) {
        if (mCachedUserBeanTasks == null || mCachedUserBeanTasks.isEmpty()) {
            return null;
        }
        return mCachedUserBeanTasks.get(uid);
    }

    @Override
    public void updateTuserTask(@NonNull UserBean userBean, @NonNull int status, @NonNull LoadTuserCallback callback) {

    }

    @Override
    public void updateTuserStatusTask(@NonNull int status, @NonNull String username, @NonNull final LoadTuserCallback callback) {
        mTasksLocalDataSource.updateTuserStatusTask(status, username, new LoadTuserCallback() {
            @Override
            public void onUserBeanLoginLoaded(UserBean userBeen) {
            }
            @Override
            public void onUserBeanLoadedByUid(UserBean userBeen) {
            }

            @Override
            public void onUserBeanSaved(boolean isSaved) {
            }

            @Override
            public void onUserBeanUpdated(boolean isUpdated) {
            }
            @Override
            public void onStatusUpdated(boolean isStatusUpdated) {
                if (callback != null) {
                    callback.onStatusUpdated(isStatusUpdated);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    @Override
    public void saveTuserTask(@NonNull UserBean user, @NonNull int status, @NonNull LoadTuserCallback callback) {

        mTasksRemoteDataSource.saveTuserTask(user, status, (RemoteDataSource.LoadTuserCallback) callback);
        refreshLocalDataSource(user,callback);
        mTasksLocalDataSource.saveTuserTask(user, status, callback);
        if (mCachedUserBeanTasks == null) {
            mCachedUserBeanTasks = new LinkedHashMap<>();
        }
        mCachedUserBeanTasks.put(user.getUid(), user);
//        callback.onUserBeanSaved(true);

    }

    @Override
    public void saveExerciseGradeTask(@NonNull ExerciseBean user, @NonNull int status, @NonNull LoadExerciseGradeCallback callback) {

    }

    @Override
    public void getWordContentsTask(@NonNull String course_id, @NonNull LoadWordContentBeansCallback callback) {
        mTasksLocalDataSource.getWordContentsTask(course_id,callback);

    }

    @Override
    public void saveWordWordContensTask(@NonNull ArrayList<WordContentBean> wordList, @NonNull LoadWordContentBeansCallback callback) {

    }
}
