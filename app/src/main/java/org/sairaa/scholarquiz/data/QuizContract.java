package org.sairaa.scholarquiz.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class QuizContract {

    public static final String CONTENT_AUTHORITY = "org.sairaa.scholarquiz";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SUBSCRIBE = "subscrib";
    public static final String PATH_LESSONQUIZ = "lessonQuiz";
    public static final String PATH_QUIZ = "quiz";
    public static final String PATH_SCOREBOARD = "scoreBoard";





    private QuizContract(){

    }

    public static final class subscriptionEntry implements BaseColumns{

        public static final Uri CONTENT_URI_SUBSCRIBE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBSCRIBE);

        public final static String TABLE_NAME = "subscrib";

        public final static String _ID = BaseColumns._ID;
        public final static String S_ID = "sid";
        public final static String L_ID = "lid";
        public final static String L_NAME = "l_name";
        public final static String TIME_STAMP = "time_stamp";


    }

    public static final class lessonQuizEntry implements BaseColumns{

        public static final Uri CONTENT_URI_LESSONQUIZ = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LESSONQUIZ);

        public final static String TABLE_NAME = "lessonQuiz";
        public final static String _ID = BaseColumns._ID;
        public final static String L_ID = "lid";
        public final static String Q_ID = "qid";
        public final static String Q_NAME = "qname";


    }

    public static final class quizQuestionEntry implements BaseColumns{

        public static final Uri CONTENT_URI_QUIZ = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_QUIZ);


        public final static String TABLE_NAME = "quiz";

        public final static String _ID = BaseColumns._ID;
        public final static String Q_ID = "qid";
        public final static String Q_NO = "qno";
        public final static String QUESTION = "question";
        public final static String OPTION1 = "opt1";
        public final static String OPTION2 = "opt2";
        public final static String OPTION3 = "opt3";
        public final static String OPTION4 = "opt4";
        public final static String ANSWER = "answer";
    }

    public static final class scoreBoardEntry implements BaseColumns{

        public static final Uri CONTENT_URI_SCOREBOARD = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCOREBOARD);

        public final static String TABLE_NAME = "scoreBoard";

        public final static String _ID = BaseColumns._ID;
        public final static String S_ID = "sid";
        public final static String L_ID = "lid";
        public final static String Q_ID = "qid";
        public final static String SCORE = "score";
        public final static String TOTAL = "total";
    }
}
