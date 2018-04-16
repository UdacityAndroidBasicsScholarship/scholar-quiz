package org.sairaa.scholarquiz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.sairaa.scholarquiz.data.QuizContract.*;

public class QuizDbHelper extends SQLiteOpenHelper {
    // DB Name
    private static final String DATABESE_NAME = "scholar.db";

    private static final int DATABASE_VERSION = 1;

    public QuizDbHelper(Context context){
        super(context, DATABESE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_SUBSCRIPTION_TABLE = "CREATE TABLE "+ subscriptionEntry.TABLE_NAME + " ("
                + subscriptionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + subscriptionEntry.S_ID + " INTEGER NOT NULL, "
                + subscriptionEntry.L_ID + " INTEGER NOT NULL, "
                + subscriptionEntry.L_NAME + " TEXT NOT NULL, "
                + subscriptionEntry.TIME_STAMP + " TEXT);";

        db.execSQL(SQL_CREATE_SUBSCRIPTION_TABLE);

        String SQL_CREATE_LESSON_QUIZ_TABLE = "CREATE TABLE "+lessonQuizEntry.TABLE_NAME + " ("
                +lessonQuizEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +lessonQuizEntry.L_ID + " INTEGER NOT NULL, "
                +lessonQuizEntry.Q_ID + " INTEGER NOT NULL, "
                +lessonQuizEntry.Q_NAME + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_LESSON_QUIZ_TABLE);

        String SQL_CREATE_QUIZ_TABLE = "CREATE TABLE "+quizQuestionEntry.TABLE_NAME + " ("
                +quizQuestionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +quizQuestionEntry.Q_ID + " INTEGER NOT NULL, "
                +quizQuestionEntry.Q_NO + " INTEGER NOT NULL, "
                +quizQuestionEntry.QUESTION + " TEXT NOT NULL, "
                +quizQuestionEntry.OPTION1 + " TEXT NOT NULL, "
                +quizQuestionEntry.OPTION2 + " TEXT NOT NULL, "
                +quizQuestionEntry.OPTION3 + " TEXT NOT NULL, "
                +quizQuestionEntry.OPTION4 + " TEXT NOT NULL, "
                +quizQuestionEntry.ANSWER + " INTEGER NOT NULL DEFAULT 0); ";

        db.execSQL(SQL_CREATE_QUIZ_TABLE);

        String SQL_CREATE_SCORE_BOARD_TABLE = "CREATE TABLE "+ scoreBoardEntry.TABLE_NAME + " ("
                +scoreBoardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  "
                +scoreBoardEntry.S_ID + " INTEGER NOT NULL, "
                +scoreBoardEntry.L_ID + " INTEGER NOT NULL, "
                +scoreBoardEntry.Q_ID + " INTEGER NOT NULL, "
                +scoreBoardEntry.SCORE + " INTEGER NOT NULL DEFAULT 0, "
                +scoreBoardEntry.TOTAL + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_SCORE_BOARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
