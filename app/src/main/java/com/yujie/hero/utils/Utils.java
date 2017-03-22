package com.yujie.hero.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yujie on 2016/4/8.
 */
public class Utils {
    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    public static <T> ArrayList<T> array2List(T[] array) {
        List<T> list = Arrays.asList(array);
        ArrayList newList = new ArrayList(list);
        return newList;
    }

    public static void showToast(Activity activity, String message, int lengthLong) {
        Toast.makeText(activity,message,lengthLong).show();
    }

    /**
     * 像素转dp
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context,int px){
        int density = (int) context.getResources().getDisplayMetrics().density;
        return px/density;
    }

    /**
     * dp转像素
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context,int dp){
        int density = (int) context.getResources().getDisplayMetrics().density;
        return dp*density;
    }

}
