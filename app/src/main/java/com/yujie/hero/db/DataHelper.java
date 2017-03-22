package com.yujie.hero.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yujie.hero.activity.MainActivity;
import com.yujie.hero.bean.ExerciseBean;
import com.yujie.hero.bean.UserBean;
import com.yujie.hero.bean.WordContentBean;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yujie on 2016/9/5.
 */
public class DataHelper extends SQLiteOpenHelper {
    public DataHelper(Context context) {
        super(context, "ucaiherodb.db", null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String user_sql = "create table if not exists t_user( " +
                    "uid char(15) primary key,"+
                    "pwd char(20) not null,"+
                    "user_name char(15) not null,"+
                    "sex Integer check(1 or 2),"+
                    "b_class Integer not null,"+
                    "avatar char(30),"+
                    "top_grade int(3),"+
                    "status Integer check(0 or 1)"+
                    ");";
        db.execSQL(user_sql);
        String word_content_sql = "create table if not exists t_words_content( " +
                    "id Integer primary key AUTOINCREMENT,"+
                    "word char(20) not null,"+
                    "course_id char(3) not null,"+
                    " FOREIGN KEY(course_id) REFERENCES t_course(simple_name)"+
                    ");";
        db.execSQL(word_content_sql);
        String daily_exercise_sql = "create table if not exists t_daily_exercise( " +
                    "id Integer PRIMARY KEY AUTOINCREMENT,"+
                    "grade int(3) NOT NULL,"+
                    "exe_time char(20) NOT NULL,"+
                    "user_name char(15) NOT NULL,"+
                    "course_id char(3) NOT NULL,"+
                    "b_class Integer NOT NULL,"+
                    "b_start_time char(20) NOT NULL"+
                    ");";
        db.execSQL(daily_exercise_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 添加数据
     * @param user 用户数据
     * @param status 登录状态
     * @return
     */
    public boolean addUser(UserBean user,int status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",user.getUid());
        values.put("pwd",user.getPwd());
        values.put("user_name",user.getUser_name());
        values.put("sex",user.getSex());
        values.put("b_class",user.getSex());
        values.put("avatar",user.getAvatar());
        values.put("top_grade",user.getTop_grade());
        values.put("status",status);
        long insert = db.insert("t_user", null, values);
        return insert>0;
    }

    /**
     * 修改用户数据
     * @param user
     * @param status
     * @return
     */
    public boolean updateUser(UserBean user,int status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",user.getUid());
        values.put("pwd",user.getPwd());
        values.put("user_name",user.getUser_name());
        values.put("sex",user.getSex());
        values.put("b_class",user.getSex());
        values.put("avatar",user.getAvatar());
        values.put("top_grade",user.getTop_grade());
        values.put("status",status);
        int update = db.update("t_user", values,"user_name=?",new String[]{user.getUser_name()});
        return update>0;
    }

    /**
     * 修改登录状态
     * @param status
     * @param user_name
     * @return
     */
    public boolean updateStatus(int status,String user_name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status",status);
        int update = db.update("t_user", values, "user_name=?", new String[]{user_name});
        return update>0;
    }

    public UserBean findLoginUser(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from t_user where status=1";
        Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                String uid = cursor.getString(cursor.getColumnIndex("uid"));
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                int sex = cursor.getInt(cursor.getColumnIndex("sex"));
                int b_class = cursor.getInt(cursor.getColumnIndex("b_class"));
                String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                int top_grade = cursor.getInt(cursor.getColumnIndex("top_grade"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                UserBean user = new UserBean(uid,pwd,user_name,sex,b_class,top_grade,avatar);
                return user;
            }
        return null;
    }

    public UserBean findUserByUid(String id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select * from t_user where uid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        while (cursor.moveToNext()){
            String uid = cursor.getString(cursor.getColumnIndex("uid"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            String user_name = cursor.getString(cursor.getColumnIndex("user_name"));
            int sex = cursor.getInt(cursor.getColumnIndex("sex"));
            int b_class = cursor.getInt(cursor.getColumnIndex("b_class"));
            String avatar = cursor.getString(cursor.getColumnIndex("avatar"));
            int top_grade = cursor.getInt(cursor.getColumnIndex("top_grade"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            UserBean user = new UserBean(uid,pwd,user_name,sex,b_class,top_grade,avatar);
            return user;
        }
        return null;
    }

    /**
     * 添加日常练习成绩
     * @param user
     * @param status
     * @return
     */
    public boolean addExerciseGrade(ExerciseBean user, int status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("grade",user.getGrade());
        values.put("exe_time",user.getExe_tiem());
        values.put("user_name",user.getUser_name());
        values.put("course_id",user.getCourse_id());
        values.put("b_class",user.getB_class());
        values.put("b_start_time",user.getStart_time());
        long insert = db.insert("t_daily_exercise", null, values);
        return insert>0;
    }

    public ArrayList<WordContentBean> getWords(String course_id){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WordContentBean> wordList = new ArrayList<>();
        WordContentBean word = null;
        int max = getWordCount(course_id);
        Random r = new Random();
        int start = r.nextInt(max-4);
        if (start<0)
            start = start+4;
        String sql = "select * from t_words_content where course_id=? limit "+start+","+4;
        Cursor cursor = db.rawQuery(sql, new String[]{course_id});
        while (cursor.moveToNext()){
            word = new WordContentBean();
            word.setId(cursor.getInt(cursor.getColumnIndex("id")));
            word.setWord(cursor.getString(cursor.getColumnIndex("word")));
            word.setCourse_id(cursor.getString(cursor.getColumnIndex("course_id")));
            wordList.add(word);
        }
        return wordList;
    }

    public int getWordCount(String course_id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT count(*) as count from t_words_content WHERE course_id=? ";
        Cursor cursor = db.rawQuery(sql,new String[]{course_id});
        while (cursor.moveToNext()){
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        return 0;
    }

    public boolean addWord(ArrayList<WordContentBean> wordList){
        SQLiteDatabase db = getWritableDatabase();
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
            return true;
        }
        return true;

    }

    private void cleanData(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "delete from "+tableName+";" +
                "update sqlite_sequence SET seq = 0 where name ='"+tableName+"';";
        db.execSQL(sql);
    }
}
