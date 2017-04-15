package com.yujie.hero.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class StartTargetActivity {

    public static void jumpToTargetActivity(Context context, Class mClass) {

        context.startActivity(new Intent(context,mClass));

    }

}
