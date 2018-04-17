package org.sairaa.scholarquiz;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.sairaa.scholarquiz.data.QuizDbHelper;

import java.util.ArrayList;
import java.util.List;
import org.sairaa.scholarquiz.data.QuizContract.subscriptionEntry;

public class SubscribeActivity extends AppCompatActivity implements LoaderCallbacks<List<LessonInfo>>{

    private static final int LESSON_LOADER_ID = 1;
    private static final String LOG_SUBSCRIBEACTIVITY = SubscribeActivity.class.getName();
    private static final String LESSON_URL = "";
    private LessonSubscriptionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        Log.i(LOG_SUBSCRIBEACTIVITY,"ScubscribeActivity");

        final ListView lessonListView = findViewById(R.id.lesson_list);

        adapter = new LessonSubscriptionAdapter(this, new ArrayList<LessonInfo>());

        lessonListView.setAdapter(adapter);

        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Intent intent = new Intent(SubscribeActivity.this,LessonActivity.class);
                //int i = view.getId();
                LessonInfo lessonInfo = (LessonInfo) lessonListView.getItemAtPosition(position);

//                intent.putExtra("id", lessonInfo.getLid());
//                intent.putExtra("name", lessonInfo.getlName());
//                intent.putExtra("moderator",lessonInfo.getModId());

                Toast.makeText(SubscribeActivity.this,"id : "+lessonInfo.getLid()+" ,"+lessonInfo.getlName(),Toast.LENGTH_SHORT).show();
                insertSubscriptionList(lessonInfo.getLid(),lessonInfo.getlName(),lessonInfo.getModId());
                //startActivity(intent);
                finish();
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            Log.i(LOG_SUBSCRIBEACTIVITY,"ScubscribeActivity Internet connected");
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LESSON_LOADER_ID, null, this);
        }

    }

    private void insertSubscriptionList(int lid, String s, int modId) {
        QuizDbHelper mDbHelper = new QuizDbHelper(this);
        // insert dummy data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Inserting dummy data to subscription table
        ContentValues values = new ContentValues();

        values.put(subscriptionEntry.S_ID, lid);

        values.put(subscriptionEntry.L_NAME, s);
        values.put(subscriptionEntry.L_ID, modId);
        values.put(subscriptionEntry.TIME_STAMP, "10/04/2018");

        //long newRowId =  db.insert(subscriptionEntry.TABLE_NAME,null,values);
        //Log.i("Subscription inserted "," "+newRowId);
        Uri newUri = getContentResolver().insert(subscriptionEntry.CONTENT_URI_SUBSCRIBE,values);
        if(newUri != null){
            Toast.makeText(SubscribeActivity.this,"uri : "+newUri,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader<List<LessonInfo>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_SUBSCRIBEACTIVITY,"onCreateLoader() called..");

        return new LessonLoder(this);
    }

    @Override
    public void onLoadFinished(Loader<List<LessonInfo>> loader, List<LessonInfo> lessonInfos) {

        Log.i(LOG_SUBSCRIBEACTIVITY,"onLoadFinished() called..");
        adapter.clear();
        adapter.addAll(lessonInfos);
        //adapter.addAll(lessonInfos);

    }

    @Override
    public void onLoaderReset(Loader<List<LessonInfo>> loader) {

    }
}
