package org.sairaa.scholarquiz.ui.Lesson;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.LessonCursorAdapter;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.SharedPreferenceConfig;
import org.sairaa.scholarquiz.data.QuizContract.subscriptionEntry;
import org.sairaa.scholarquiz.data.QuizDbHelper;
import org.sairaa.scholarquiz.model.LessonListModel;
import org.sairaa.scholarquiz.ui.Login.LoginActivity;
import org.sairaa.scholarquiz.ui.Moderator.QuizModeratorActivity;
import org.sairaa.scholarquiz.ui.Subscription.LessonSubscriptionAdapter;
import org.sairaa.scholarquiz.ui.Subscription.SubscribeActivity;
import org.sairaa.scholarquiz.ui.User.QuizActivity;

import java.util.ArrayList;
import java.util.List;

public class LessonActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, LessonActivityInterface{

    private DrawerLayout drawerLayout;
    private Toolbar toolbarr;
    private SharedPreferenceConfig sharedPreferenceConfig;

    private QuizDbHelper mDbHelper;

    private static final int LESSON_LOADER = 1;

    private LessonSubscriptionAdapter adapterList;
    private ListView lessonListView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReferance;
    private ChildEventListener mChildEventListener;

    LessonCursorAdapter adapter;
    ProgressDialog progressDialog;
    LessonActivityInterface lessonActivityInterface ;

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(LessonActivity.this,"On Resume",Toast.LENGTH_SHORT).show();
        adapterList.clear();
        lessonActivityInterface = this;
//        Attach all subscribed channel to adapter
        attachSubscribedLessonListListner();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(LessonActivity.this,"On Pause",Toast.LENGTH_SHORT).show();
        adapterList.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mMessageDatabaseReferance = mFirebaseDatabase.getReference().child("Subscription").child(user.getUid().toString());

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
//                                    sharedPreferenceConfig.writeLoginStatus(false);
//                                    startActivity(new Intent(LessonActivity.this,LoginActivity.class));
//                                    finish();
                                AppInfo.firebaseAuth.signOut();
                                startActivity(new Intent(LessonActivity.this,LoginActivity.class));
                                finish();
                                break;
                            case R.id.subscribe_nav:
                                startActivity(new Intent(LessonActivity.this,SubscribeActivity.class));
                                break;
                            case R.id.score_nav:
                                //insertLesson();
                                //displayDatabaseInfo();
                                break;
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        mDbHelper = new QuizDbHelper(this);
        //displayDatabaseInfo();
        final ListView lessonListView = findViewById(R.id.lesson_listview);
//        adapter = new LessonCursorAdapter(this,null);
//        lessonListView.setAdapter(adapter);

        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intentUser = new Intent(LessonActivity.this,QuizActivity.class);
                Intent intentModerator = new Intent(LessonActivity.this,QuizModeratorActivity.class);

                LessonListModel lessonInfo = (LessonListModel) lessonListView.getItemAtPosition(position);
                intentModerator.putExtra("channelId", lessonInfo.getChannelId());
                intentUser.putExtra("channelId", lessonInfo.getChannelId());
//                Toast.makeText(LessonActivity.this,"ll "+user.getUid()+" Mod :"+lessonInfo.getModeratorName(),Toast.LENGTH_SHORT).show();
                if(user.getUid().toString().equals(lessonInfo.getModeratorName())){
//                    Toast.makeText(LessonActivity.this,"ll "+user.getUid()+" Mod :"+lessonInfo.getModeratorName(),Toast.LENGTH_SHORT).show();
                    startActivity(intentModerator);
                }else{
//                    Toast.makeText(LessonActivity.this,"ll "+user.getUid()+" Mod :"+lessonInfo.getModeratorName(),Toast.LENGTH_SHORT).show();
                    startActivity(intentUser);
                }


//                startActivity(intent);
            }
        });


        List<LessonListModel> lessonListModels = new ArrayList<>();
        adapterList = new LessonSubscriptionAdapter(this, new ArrayList<LessonListModel>());
        lessonListView.setAdapter(adapterList);
        adapterList.clear();
        // kick off the loader
//        getLoaderManager().initLoader(LESSON_LOADER,null,this);
        Toast.makeText(LessonActivity.this,"On Create",Toast.LENGTH_SHORT).show();
        //attachSubscribedLessonListListner();
    }

    private void attachSubscribedLessonListListner() {

        lessonActivityInterface.showDialog();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Subscription").child(String.valueOf(user.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot subscriptionListSnapshot : dataSnapshot.getChildren()) {
//                            Toast.makeText(LessonActivity.this," 2"+subscriptionListSnapshot.getKey()+"3"+subscriptionListSnapshot.getKey(),Toast.LENGTH_SHORT).show();

                            FirebaseDatabase.getInstance().getReference().child("ChannelList").orderByKey()
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot channelSnapshot) {
                                            for ( final DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {

                                                if(subscriptionListSnapshot.getKey().equals(channelListSnapshot.getKey())){
//                                                    Toast.makeText(LessonActivity.this," 2"+channelListSnapshot.getKey()+"3"+subscriptionListSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                                                    String channelId = channelListSnapshot.getKey().toString();
                                                    LessonListModel model = channelListSnapshot.getValue(LessonListModel.class);
                                                    adapterList.add(new LessonListModel(model.getModeratorName(),model.getChannelName(),channelId));
                                                }
                                            }
                                            lessonActivityInterface.hideDialog();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            lessonActivityInterface.hideDialog();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection ={
                subscriptionEntry._ID,
                subscriptionEntry.S_ID,
                subscriptionEntry.L_ID,
                subscriptionEntry.L_NAME,
                subscriptionEntry.TIME_STAMP

        };
        return new CursorLoader(this,subscriptionEntry.CONTENT_URI_SUBSCRIBE,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        adapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        adapter.swapCursor(null);

    }

    @Override
    public void hideDialog() {

        if(progressDialog != null && progressDialog.isShowing()) {

            progressDialog.cancel();
        }

    }

    @Override
    public void showDialog() {

        hideDialog();

        progressDialog = ProgressDialog.show(this, null, "fetching subscribed channels", true, false);

    }

    @Override
    public void onError(Exception e) {

    }
}
