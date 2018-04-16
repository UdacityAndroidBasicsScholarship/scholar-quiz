package org.sairaa.scholarquiz;

import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        ListView lessonListView = findViewById(R.id.lesson_list);

        adapter = new LessonSubscriptionAdapter(this, new ArrayList<LessonInfo>());

        lessonListView.setAdapter(adapter);

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
