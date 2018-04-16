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

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(subscriptionEntry.S_ID, 1001);
        values.put(subscriptionEntry.L_ID, 2001);
        values.put(subscriptionEntry.L_NAME, "Lesson 1");
        values.put(subscriptionEntry.TIME_STAMP, "10/04/2018");

        long newRowId =  db.insert(subscriptionEntry.TABLE_NAME,null,values);

    }

    private void displayDatabaseInfo() {
        QuizDbHelper mDbHelper = new QuizDbHelper(this);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ subscriptionEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.test);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
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
