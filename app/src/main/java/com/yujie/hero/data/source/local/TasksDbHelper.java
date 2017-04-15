package com.yujie.hero.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class TasksDbHelper extends SQLiteOpenHelper{



    public static final String DATABASE_NAME = "ucaiherodb.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String BOOLEAN_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";


    //    String user_sql = "create table if not exists t_user( " +
    //            "uid char(15) primary key,"+
    //            "pwd char(20) not null,"+
    //            "user_name char(15) not null,"+
    //            "sex Integer check(1 or 2),"+
    //            "b_class Integer not null,"+
    //            "avatar char(30),"+
    //            "top_grade int(3),"+
    //            "status Integer check(0 or 1)"+
    //            ");";
    private static final String SQL_CREATE_T_USER = "CREATE TABLE" + TasksPersistenceContract.
            TaskEntry.Tuser.TABLE_NAME + "(" + TasksPersistenceContract.
            TaskEntry.Tuser.COLUMN_NAME_USER_ID + TEXT_TYPE + "PRIMARY KEY,"
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_SEX + BOOLEAN_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_B_CLASS + BOOLEAN_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_AVATAR + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_TOP_GRADE + BOOLEAN_TYPE
            + TasksPersistenceContract.TaskEntry.Tuser.COLUMN_NAME_STATUS+BOOLEAN_TYPE+ ");";


    private static final String SQL_CREATE_DAILY_EXERCISE = "CREATE TABLE" + TasksPersistenceContract.
            TaskEntry.DailyExercise.TABLE_NAME + "(" + TasksPersistenceContract.
            TaskEntry.DailyExercise.COLUMN_NAME_DAILY_EXERCISE_ID + BOOLEAN_TYPE + "PRIMARY KEY AUTOINCREMENT,"
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_DAILY_GRADE + BOOLEAN_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_EXE_TIME + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_USER + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_COURSE_ID + TEXT_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_B_CLASS + BOOLEAN_TYPE + COMMA_SEP
            + TasksPersistenceContract.TaskEntry.DailyExercise.COLUMN_NAME_B_START_TIME + TEXT_TYPE  + ");";


//    String word_content_sql = "create table if not exists t_words_content( " +
//            "id Integer primary key AUTOINCREMENT,"+
//            "word char(20) not null,"+
//            "course_id char(3) not null,"+
//            " FOREIGN KEY(course_id) REFERENCES t_course(simple_name)"+
//            ");";
private static final String SQL_CREATE_WORDS_CONTENT = "CREATE TABLE" + TasksPersistenceContract.
        TaskEntry.WordsContent.TABLE_NAME + "(" + TasksPersistenceContract.
        TaskEntry.WordsContent.COLUMN_NAME_WORDS_ID + BOOLEAN_TYPE + "PRIMARY KEY AUTOINCREMENT,"
        + TasksPersistenceContract.TaskEntry.WordsContent.COLUMN_NAME_WORD+TEXT_TYPE+ COMMA_SEP
        + TasksPersistenceContract.TaskEntry.WordsContent.COLUMN_NAME_COURSE_ID+TEXT_TYPE+COMMA_SEP
        +" FOREIGN KEY(course_id) REFERENCES t_course(simple_name)"+
        ");";


    public TasksDbHelper(Context context,int version) {
        super(context, DATABASE_NAME, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DAILY_EXERCISE);
        db.execSQL(SQL_CREATE_T_USER);
        db.execSQL(SQL_CREATE_WORDS_CONTENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists"+ TasksPersistenceContract.TaskEntry.DailyExercise.TABLE_NAME);
            db.execSQL("drop table if exists"+ TasksPersistenceContract.TaskEntry.Tuser.TABLE_NAME);
            db.execSQL("drop table if exists"+ TasksPersistenceContract.TaskEntry.WordsContent.TABLE_NAME);
        }
        onCreate(db);

    }
}
