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

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
