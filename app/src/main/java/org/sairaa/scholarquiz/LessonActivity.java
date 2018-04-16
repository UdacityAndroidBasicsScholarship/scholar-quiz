package org.sairaa.scholarquiz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import org.sairaa.scholarquiz.data.QuizDbHelper;
import org.sairaa.scholarquiz.data.QuizContract.*;

public class LessonActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbarr;
    private SharedPreferenceConfig sharedPreferenceConfig;

    private QuizDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);



        drawerLayout = findViewById(R.id.drawer_layout);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarr = (Toolbar)findViewById(R.id.toolbar_lesson);
        setSupportActionBar(toolbarr);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.logout_nav:
                                    sharedPreferenceConfig.writeLoginStatus(false);
                                    startActivity(new Intent(LessonActivity.this,LoginActivity.class));
                                    finish();
                                break;
                            case R.id.subscribe_nav:
                                startActivity(new Intent(LessonActivity.this,SubscribeActivity.class));
                                break;
                            case R.id.score_nav:
                                insertLesson();
                                displayDatabaseInfo();
                                break;
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        mDbHelper = new QuizDbHelper(this);
        displayDatabaseInfo();
    }

    private void insertLesson() {
        // insert dummy data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Inserting dummy data to subscription table
        ContentValues values = new ContentValues();

        values.put(subscriptionEntry.S_ID, 1001);
        values.put(subscriptionEntry.L_ID, 2001);
        values.put(subscriptionEntry.L_NAME, "Lesson 1");
        values.put(subscriptionEntry.TIME_STAMP, "10/04/2018");

        long newRowId =  db.insert(subscriptionEntry.TABLE_NAME,null,values);
        Log.i("Subscription inserted "," "+newRowId);


        // Inserting dummy data to quiz Table table
        ContentValues valuesQuiz = new ContentValues();

        //valuesQuiz.put(quizQuestionEntry._ID, 1);
        valuesQuiz.put(quizQuestionEntry.Q_ID, 3001);
        valuesQuiz.put(quizQuestionEntry.Q_NO, 2);
        valuesQuiz.put(quizQuestionEntry.QUESTION, "How");
        valuesQuiz.put(quizQuestionEntry.OPTION1, "now");
        valuesQuiz.put(quizQuestionEntry.OPTION2, "yes");
        valuesQuiz.put(quizQuestionEntry.OPTION3, "wow");
        valuesQuiz.put(quizQuestionEntry.OPTION4, "no");
        valuesQuiz.put(quizQuestionEntry.ANSWER, 2);

        long newquizId = db.insert(quizQuestionEntry.TABLE_NAME,null,valuesQuiz);

        Log.i("Quiz inserted "," "+newquizId);

        // Inserting dummy data to lessonQuiz Table table
        ContentValues valueslessonQuiz = new ContentValues();


        //valueslessonQuiz.put(lessonQuizEntry._ID, 1);
        valueslessonQuiz.put(lessonQuizEntry.L_ID, 4001);
        valueslessonQuiz.put(lessonQuizEntry.Q_ID, 3001);
        valueslessonQuiz.put(lessonQuizEntry.Q_NAME,"Lesson1");


        long newlessonquizId = db.insert(lessonQuizEntry.TABLE_NAME,null,valueslessonQuiz);

        Log.i("Lesson Quiz inserted "," "+newlessonquizId);

        // Inserting dummy data to scoreboard table
        ContentValues valuesScoreBoard = new ContentValues();

        valuesScoreBoard.put(scoreBoardEntry.S_ID, 1001);
        valuesScoreBoard.put(scoreBoardEntry.L_ID, 2001);
        valuesScoreBoard.put(scoreBoardEntry.Q_ID, 3001);
        valuesScoreBoard.put(scoreBoardEntry.SCORE, 7);
        valuesScoreBoard.put(scoreBoardEntry.TOTAL, 10);


        long newvalueScoreId =  db.insert(scoreBoardEntry.TABLE_NAME,null,valuesScoreBoard);
        Log.i("ScoreBoard inserted "," "+newRowId);

    }

    private void displayDatabaseInfo() {
        String dataCheck = "";
        QuizDbHelper mDbHelper = new QuizDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ subscriptionEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.test);
            dataCheck = "Number of rows in subscription database table: " + cursor.getCount()+"\n";
            //displayView.setText(dataCheck);
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
        // checking retribing data insertion to quiztable
        cursor = db.rawQuery("SELECT * FROM "+ quizQuestionEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.test);
            dataCheck = dataCheck +"Number of rows in quiz database table: " + cursor.getCount()+"\n";
            //displayView.setText(dataCheck);
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // checking retribing data insertion to lessonquiz table
        cursor = db.rawQuery("SELECT * FROM "+ lessonQuizEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.test);
            dataCheck = dataCheck +"Number of rows in lessonQuiz database table: " + cursor.getCount()+"\n";
            //displayView.setText(dataCheck);
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // checking retribing data insertion to scoreboard table
        cursor = db.rawQuery("SELECT * FROM "+ scoreBoardEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.test);
            dataCheck = dataCheck +"Number of rows in ScoreBoard database table: " + cursor.getCount()+"\n";
            displayView.setText(dataCheck);
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
