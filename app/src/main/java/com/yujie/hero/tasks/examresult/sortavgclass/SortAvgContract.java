package com.yujie.hero.tasks.examresult.sortavgclass;

import com.yujie.hero.data.bean.ExamClassGradeBean;
import com.yujie.hero.data.bean.ExamGradeAvgBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/28.
 */

public interface SortAvgContract {
    interface View {
        void getClassAvgGrade(ArrayList<ExamGradeAvgBean> classGrades);
        void getExamClass(ArrayList<ExamClassGradeBean> stuGrades);
        void showToast(String msg);
        void setListener();
        void resetViewPort();
        void generateInitialLineData();
        void generateColumnData();



    }
    interface Presenter {
        void showToast(String msg);
        void initStuGrades(int classId,String examId);
        void initClassData(String examId);
        void setListener();
        void resetViewPort();
        void generateInitialLineData();
        void generateColumnData();





    }
}
