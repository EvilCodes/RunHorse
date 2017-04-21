package com.yujie.hero.tasks.register;

import com.yujie.hero.BasePresenter;
import com.yujie.hero.BaseView;
import com.yujie.hero.data.bean.AreasBean;
import com.yujie.hero.data.bean.ClassObj;
import com.yujie.hero.data.bean.CourseBean;
import com.yujie.hero.data.bean.Result;
import com.yujie.hero.data.bean.StartTimeBean;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

public interface RegisterContract {
    interface View extends BaseView<Presenter> {

        void showRequestResult(String msg);

        void showChoseDialog();

        boolean checkPersonMessage();

        void setAreaList(AreasBean[] result);

        void setCourseList(CourseBean[] result);

        void setStartTimeList(StartTimeBean[] result);

        void setClassList(ClassObj[] result);

        void checkOnClickButton();


        void goRegister(Result result);


        void showChosedDialog();
    }

    interface Presenter extends BasePresenter {

        void addLocalPreference();

        void checkOnClickButton();

        void goCourseTask();

        void goStartTimeTask();

        void goAeraTask();

        void goClassTask(String area, String course, String time);



        void goRegister(String uid,String pwd,String userName,String sex,
                        String classId,String topGrade);

        void showChoseClasses();

        boolean checkPersonMessage();




    }
}
