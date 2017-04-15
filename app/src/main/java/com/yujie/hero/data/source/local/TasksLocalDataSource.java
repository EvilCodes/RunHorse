/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yujie.hero.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;


import com.yujie.hero.data.source.TasksDataSource;
import com.yujie.hero.data.bean.UserBean;
import com.yujie.hero.data.bean.WordContentBean;
import com.yujie.hero.data.bean.ExerciseBean;

import java.util.ArrayList;
import java.util.Random;



/**
 * Concrete implementation of a data source as a db.
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDbHelper mDbHelper;

    // Prevent direct instantiation.
    private TasksLocalDataSource(@NonNull Context context) {
        mDbHelper = new TasksDbHelper(context, 1);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * @param callback
     */
    @Override
    public void getLoginTuserTask(@NonNull LoadTuserCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from t_user where status=1";
        UserBean user = null;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String uid = cursor.getString(cursor.getColumnIndex("uid"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            int sex = cursor.getInt(cursor.getColumnIndex("sex"));
            int b_class = cursor.getInt(cursor.getColumnIndex("b_class"));
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            int top_grade = cursor.getInt(cursor.getColumnIndex("top_grade"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            user = new UserBean(uid, pwd, user_name, sex, b_class, top_grade, avatar);
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        //以实现内部接口的方法的方式将所得的数据传给Presenter

        if (user == null) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onUserBeanLoginLoaded(user);
        }
    }

    @Override
    public void getTuserTaskByUid(@NonNull LoadTuserCallback callback, String id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "select * from t_user where uid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        UserBean user = null;
        while (cursor.moveToNext()) {
            String uid = cursor.getString(cursor.getColumnIndex("uid"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            int sex = cursor.getInt(cursor.getColumnIndex("sex"));
            int b_class = cursor.getInt(cursor.getColumnIndex("b_class"));
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            int top_grade = cursor.getInt(cursor.getColumnIndex("top_grade"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            user = new UserBean(uid, pwd, user_name, sex, b_class, top_grade, avatar);
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        //以实现内部接口的方法的方式将所得的数据传给Presenter
        if (user == null) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onUserBeanLoadedByUid(user);
        }
    }

    @Override
    public void getNetWorkTuserTask(@NonNull LoadTuserCallback callback, @NonNull String uid, @NonNull String pwd) {

    }

    @Override
    public void saveTuserTask(@NonNull UserBean userBean, @NonNull int status, @NonNull LoadTuserCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", userBean.getUid());
        values.put("pwd", userBean.getPwd());
        values.put("user_name", userBean.getUser_name());
        values.put("sex", userBean.getSex());
        values.put("b_class", userBean.getSex());
        values.put("avatar", userBean.getAvatar());
        values.put("top_grade", userBean.getTop_grade());
        values.put("status", status);
        long insert = db.insert("t_user", null, values);
//        callback.onUserBeanSaved(insert > 0);
    }

    @Override
    public void updateTuserTask(@NonNull UserBean user, @NonNull int status, @NonNull LoadTuserCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", user.getUid());
        values.put("pwd", user.getPwd());
        values.put("user_name", user.getUser_name());
        values.put("sex", user.getSex());
        values.put("b_class", user.getSex());
        values.put("avatar", user.getAvatar());
        values.put("top_grade", user.getTop_grade());
        values.put("status", status);
        int update = db.update("t_user", values, "user_name=?", new String[]{user.getUser_name()});
        callback.onUserBeanUpdated(update > 0);
    }

    @Override
    public void updateTuserStatusTask(@NonNull int status, @NonNull String user_name, @NonNull LoadTuserCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        int update = db.update("t_user", values, "user_name=?", new String[]{user_name});
        callback.onStatusUpdated(update > 0);
    }

    /**
     * 添加日常练习的成绩
     *
     * @param user
     * @param status
     * @param callback
     */
    @Override
    public void saveExerciseGradeTask(@NonNull ExerciseBean user, @NonNull int status, @NonNull LoadExerciseGradeCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("grade", user.getGrade());
        values.put("exe_time", user.getExe_tiem());
        values.put("user_name", user.getUser_name());
        values.put("course_id", user.getCourse_id());
        values.put("b_class", user.getB_class());
        values.put("b_start_time", user.getStart_time());
        long insert = db.insert("t_daily_exercise", null, values);
        callback.onExerciseGradeSaved(insert > 0);
    }




    @Override
    public void getWordContentsTask(@NonNull String course_id, @NonNull LoadWordContentBeansCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ArrayList<WordContentBean> wordList = new ArrayList<>();
        WordContentBean word = null;
        int max = getWordCount(course_id);
        Random r = new Random();
        int start = r.nextInt(max - 4);
        if (start < 0)
            start = start + 4;
        String sql = "select * from t_words_content where course_id=? limit " + start + "," + 4;
        Cursor cursor = db.rawQuery(sql, new String[]{course_id});
        while (cursor.moveToNext()) {
            word = new WordContentBean();
            word.setId(cursor.getInt(cursor.getColumnIndex("id")));
            word.setWord(cursor.getString(cursor.getColumnIndex("word")));
            word.setCourse_id(cursor.getString(cursor.getColumnIndex("course_id")));
            wordList.add(word);
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();

        //以实现内部接口的方法的方式将所得的数据传给Presenter

        if (wordList == null) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
        }
        callback.onWordContentBeansLoaded(wordList);


    }

    public int getWordCount(String course_id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT count(*) as count from t_words_content WHERE course_id=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{course_id});
        while (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return 0;
    }

    @Override
    public void saveWordWordContensTask(@NonNull ArrayList<WordContentBean> wordList, @NonNull TasksDataSource.LoadWordContentBeansCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int i = 0;
        db.beginTransaction();
        for (WordContentBean bean:wordList){
            ContentValues values = new ContentValues();
            values.put("word",bean.getWord());
            values.put("course_id",bean.getCourse_id());
            long l = db.insert("t_words_content", null, values);
            if (l>0)
                i++;
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        if (i == wordList.size()){
            db.close();
        }
        callback.onWordContentBeansSaved(true);
    }

    public void cleanData(String tableName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "delete from "+tableName+";" +
                "update sqlite_sequence SET seq = 0 where name ='"+tableName+"';";
        db.execSQL(sql);
    }


}
