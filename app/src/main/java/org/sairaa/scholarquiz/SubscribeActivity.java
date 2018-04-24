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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.data.QuizDbHelper;

import java.util.ArrayList;
import java.util.List;
import org.sairaa.scholarquiz.data.QuizContract.subscriptionEntry;
import org.sairaa.scholarquiz.model.LessonListModel;

public class SubscribeActivity extends AppCompatActivity {

    private static final int LESSON_LOADER_ID = 1;
    private static final String LOG_SUBSCRIBEACTIVITY = SubscribeActivity.class.getName();
    private static final String LESSON_URL = "";
    private LessonSubscriptionAdapter adapter;
    //databse reference
//    private DatabaseReference mChannelRef;
//    private DatabaseReference mChannelListRef;
//    private DatabaseReference mSubscriptionListRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReferance;
    private ChildEventListener mChildEventListener;
//    DatabaseReference mDatabase;

    String channelExist = "N";
    ArrayList<LessonListModel> userChannelList;
    LessonListModel userChannel = new LessonListModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mMessageDatabaseReferance = mFirebaseDatabase.getReference().child("ChannelList");

        Log.i(LOG_SUBSCRIBEACTIVITY,"ScubscribeActivity");
//        // Initialize message ListView and its adapter
//        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
//        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
//        mMessageListView.setAdapter(mMessageAdapter);
        final ListView lessonListView = findViewById(R.id.lesson_list);
        List<LessonListModel> lessonListModels = new ArrayList<>();
        adapter = new LessonSubscriptionAdapter(this, new ArrayList<LessonListModel>());
        lessonListView.setAdapter(adapter);

        attachDatabaseListner();







        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Intent intent = new Intent(SubscribeActivity.this,LessonActivity.class);
                //int i = view.getId();
                LessonListModel lessonInfo = (LessonListModel) lessonListView.getItemAtPosition(position);

//                intent.putExtra("id", lessonInfo.getLid());
//                intent.putExtra("name", lessonInfo.getlName());
//                intent.putExtra("moderator",lessonInfo.getModId());

                Toast.makeText(SubscribeActivity.this,"id : "+lessonInfo.channelId+" ,"+lessonInfo.Name,Toast.LENGTH_SHORT).show();
                insertSubscriptionList(user.getUid(),lessonInfo.getChannelId(),lessonInfo.getChannelName(),lessonInfo.getModeratorName());
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
//            loaderManager.initLoader(LESSON_LOADER_ID, null, this);
            // To get User id of Current user so we can travel to Subscription List of User
//            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//            mDatabase = FirebaseDatabase.getInstance().getReference();
//
//            mSubscriptionListRef = mDatabase.child("Subscription/" + String.valueOf(user.getUid()));
//
//            mChannelRef = mDatabase.child("ChannelList/");



//            // Read all the channels name from Firebase
//            mChannelRef.addValueEventListener(new ValueEventListener() {
//
//                @Override
//                public void onDataChange(DataSnapshot channelSnapshot) {
//
//                    // Clear the Channel List View
//                    //AllChannelList.clear();
//
//                    // Check the count of Channels to subscribe to. If no channel, show message else show list of channels
//                    if (channelSnapshot.getChildrenCount() < 1) {
//                        Toast.makeText(SubscribeActivity.this, "Not Subscribed to any Channel!!", Toast.LENGTH_LONG).show();
//
//                    } else {
//
//                        // Loop through all channel lists
//                        for (final DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {
//
//                            // Read all the channels that user has subscribed to
//                            mSubscriptionListRef.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot subscriptionSnapshot) {
//
//                                    for (DataSnapshot subscriptionListSnapshot : subscriptionSnapshot.getChildren()) {
//                                        // If channel is already subscribed to by user ser the channelExist to Y
//                                        if (channelListSnapshot.getKey().equals(subscriptionListSnapshot.getKey())) {
//                                            //Toast.makeText(ChannelListActivity.this, "Channel Key Exists:" + String.valueOf(subscriptionListSnapshot.getValue()), Toast.LENGTH_LONG).show();
//                                            channelExist = "Y";
//                                            break;
//                                        } else {
//                                            channelExist = "N";
//                                        }
//
//                                    }
//
//                                    // If channelExist = Y i.e channel is  subscribed then add it to channel list adapter to show it to user else set it to N
//                                    if (channelExist.equals("Y")) {
//
//                                        String channelId = String.valueOf(channelListSnapshot.getKey());
//                                        // Show channels available to user
//                                        userChannel = channelListSnapshot.getValue(LessonListModel.class);
//                                        userChannelList.add(new LessonListModel(userChannel.getModeratorName(), userChannel.getChannelName(), channelId));
//                                        adapter = new LessonSubscriptionAdapter(getApplicationContext(), userChannelList);
//                                        lessonListView.setAdapter(adapter);
//
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    System.out.println("The read failed: " + databaseError.getMessage());
//                                }
//                            });
//                        }
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    System.out.println("The read failed: " + databaseError.getMessage());
//                }
//
//            });

        }

    }

    private void attachDatabaseListner() {
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    LessonListModel friendlyMessage = dataSnapshot.getValue(LessonListModel.class);
                    adapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mMessageDatabaseReferance.addChildEventListener(mChildEventListener);
        }

    }

    private void insertSubscriptionList(String uid, String lid, String s, String modId) {
        QuizDbHelper mDbHelper = new QuizDbHelper(this);
        // insert dummy data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Inserting dummy data to subscription table
        Log.i("Subscription inserted "," "+uid+" "+lid+ " : "+s);
        ContentValues values = new ContentValues();

        values.put(subscriptionEntry.S_ID, uid);
        values.put(subscriptionEntry.L_ID, s);
        values.put(subscriptionEntry.L_NAME, s);

        values.put(subscriptionEntry.TIME_STAMP, "10/04/2018");

        //long newRowId =  db.insert(subscriptionEntry.TABLE_NAME,null,values);
        //Log.i("Subscription inserted "," "+newRowId);
        Uri newUri = getContentResolver().insert(subscriptionEntry.CONTENT_URI_SUBSCRIBE,values);
        if(newUri != null){
            Toast.makeText(SubscribeActivity.this,"uri : "+newUri,Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public Loader<List<LessonInfo>> onCreateLoader(int i, Bundle bundle) {
//
//        Log.i(LOG_SUBSCRIBEACTIVITY,"onCreateLoader() called..");
//
//        return new LessonLoder(this);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<LessonInfo>> loader, List<LessonInfo> lessonInfos) {
//
//        Log.i(LOG_SUBSCRIBEACTIVITY,"onLoadFinished() called..");
//        adapter.clear();
//        adapter.addAll(lessonInfos);
//        //adapter.addAll(lessonInfos);
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<LessonInfo>> loader) {
//
//    }
}
