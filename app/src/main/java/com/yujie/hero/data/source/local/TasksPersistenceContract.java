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

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class TasksPersistenceContract {

    // To prevent someone from acciden  tally instantiating the contract class,
    // give it an empty constructor.
    private TasksPersistenceContract() {

    }

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {

        /**
         *有关DailyExercise数据库操作的相关字符串
         */
        interface DailyExercise{
            public static final String TABLE_NAME = " t_daily_exercise";
            public static final String COLUMN_NAME_DAILY_EXERCISE_ID = "id";
            public static final String COLUMN_NAME_DAILY_GRADE = "grade";
            public static final String COLUMN_NAME_EXE_TIME = "exe_time";
            public static final String COLUMN_NAME_USER = "user_name";
            public static final String COLUMN_NAME_COURSE_ID = "course_id";
            public static final String COLUMN_NAME_B_CLASS = "b_class";
            public static final String COLUMN_NAME_B_START_TIME = "b_start_time";

        }

        /**
         * Iuser相关的数据库用到的字符串
         */
        interface Tuser{
            public static final String TABLE_NAME = " t_user";
            public static final String COLUMN_NAME_USER_ID = "uid";
            public static final String COLUMN_NAME_PASSWORD = "pwd";
            public static final String COLUMN_NAME_USER_NAME = "user_name";
            public static final String COLUMN_NAME_AVATAR = "avatar";
            public static final String COLUMN_NAME_TOP_GRADE = "top_grade";
            public static final String COLUMN_NAME_SEX = "sex";
            public static final String COLUMN_NAME_STATUS = "status";
            public static final String COLUMN_NAME_B_CLASS = "b_class";
        }

        /**
         *有关WordsContent数据库操作相关的字符串
         */
        interface WordsContent{

            public static final String TABLE_NAME = " t_words_content";
            public static final String COLUMN_NAME_WORDS_ID = "id";
            public static final String COLUMN_NAME_WORD = "word";
            public static final String COLUMN_NAME_COURSE_ID = "course_id";
        }

    }
}
