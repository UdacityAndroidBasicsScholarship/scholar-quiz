package org.sairaa.scholarquiz.ui.Subscription;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
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
    private ListView lessonListView;
    //databse reference
//    private DatabaseReference mChannelRef;
//    private DatabaseReference mChannelListRef;
//    private DatabaseReference mSubscriptionListRef;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReferance;
    private ChildEventListener mChildEventListener;

    String channelExist = "N";


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
        lessonListView = findViewById(R.id.lesson_list);
        List<LessonListModel> lessonListModels = new ArrayList<>();
        adapter = new LessonSubscriptionAdapter(this, new ArrayList<LessonListModel>());
        lessonListView.setAdapter(adapter);

        //attachDatabaseListner();
        attachToBeSubscribedChannelListner();






        lessonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Intent intent = new Intent(SubscribeActivity.this,LessonActivity.class);
                //int i = view.getId();
                LessonListModel lessonInfo = (LessonListModel) lessonListView.getItemAtPosition(position);

//                intent.putExtra("id", lessonInfo.getLid());
//                intent.putExtra("name", lessonInfo.getlName());
//                intent.putExtra("moderator",lessonInfo.getModId());

//                Toast.makeText(SubscribeActivity.this,"id : "+lessonInfo.getChannelId()+" ,"+lessonInfo.getChannelName(),Toast.LENGTH_SHORT).show();
                // Insert into Sqlite
                //insertSubscriptionList(user.getUid(),lessonInfo.getChannelId(),lessonInfo.getChannelName(),lessonInfo.getModeratorName());
                //startActivity(intent);
                //insert clicked item to firebase database
                insertSubscriptionList(lessonInfo);
                finish();
//                mMessageDatabaseReferance.removeEventListener(mChildEventListener);
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


        }

    }

    private void attachToBeSubscribedChannelListner() {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("ChannelList").orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot channelSnapshot) {
                        for ( final DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase.getInstance().getReference().child("Subscription").child(String.valueOf(user.getUid()))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot subscriptionSnapshot) {
                                            for (final DataSnapshot subscriptionListSnapshot : subscriptionSnapshot.getChildren()) {
                                                if(subscriptionListSnapshot.getKey().equals(channelListSnapshot.getKey())){
                                                    channelExist = "Y";
//                                                    Toast.makeText(SubscribeActivity.this," 2"+channelListSnapshot.getKey()+"3"+subscriptionListSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                                                    break;
                                                }else {
                                                    channelExist = "N";

                                                }
                                            }
                                            if (channelExist.equals("N")){
                                                String channelId = channelListSnapshot.getKey().toString();
                                                LessonListModel model = channelListSnapshot.getValue(LessonListModel.class);
                                                adapter.add(new LessonListModel(model.getModeratorName(),model.getChannelName(),channelId));
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//        FirebaseDatabase.getInstance().getReference().child("Subscription").child(String.valueOf(user.getUid()))
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(final DataSnapshot subscriptionSnapshot) {
//                        String channelId = String.valueOf(subscriptionSnapshot.getKey());
//
//                        if(subscriptionSnapshot.hasChildren()){
//                            for (final DataSnapshot subscriptionListSnapshot : subscriptionSnapshot.getChildren()) {
//
//                                FirebaseDatabase.getInstance().getReference().child("ChannelList").orderByKey()
//                                        .addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot channelSnapshot) {
////                                                String key = channelSnapshot.getRef().getKey().toString();
////                                                LessonListModel friendlyMessage = channelSnapshot.getValue(LessonListModel.class);
//
////                                                Toast.makeText(SubscribeActivity.this," 1"+key,Toast.LENGTH_SHORT).show();
//                                                for ( DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {
//                                                    if(subscriptionListSnapshot.getKey().equals(channelListSnapshot.getKey())){
//                                                        channelExist = "Y";
//                                                        Toast.makeText(SubscribeActivity.this," 2"+channelListSnapshot.getKey()+"3"+subscriptionListSnapshot.getKey(),Toast.LENGTH_SHORT).show();
//                                                        break;
//                                                    }else {
//                                                        channelExist = "N";
//                                                        String channelId = channelListSnapshot.getKey().toString();
//                                                        LessonListModel model = channelListSnapshot.getValue(LessonListModel.class);
//                                                        adapter.add(new LessonListModel(model.getModeratorName(),model.getChannelName(),channelId));
//                                                        break;
//                                                    }
//                                                }
////                                                if (channelExist.equals("N")){
////                                                    String channelId = String.valueOf(.getKey());
////                                                    LessonListModel friendlyMessage = channelSnapshot.getValue(LessonListModel.class);
////
////                                                    Toast.makeText(SubscribeActivity.this," 0"+channelId,Toast.LENGTH_SHORT).show();
////                                                    //adapter.add(new LessonListModel(friendlyMessage.getModeratorName(),friendlyMessage.getChannelName(),channelId));
////                                                }
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
////                                Toast.makeText(SubscribeActivity.this,""+subscriptionListSnapshot.getKey(),Toast.LENGTH_SHORT).show();
//                            }
//                        }else{
//                            attachDatabaseListner();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.hasChildren()){
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                SubscribedListModel subscribedList = snapshot.getValue(SubscribedListModel.class);
////                            System.out.println(user.email);
//                                Toast.makeText(SubscribeActivity.this,""+dataSnapshot.getKey()+" : "+subscribedList.getChannelId(),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }else{
//                            attachDatabaseListner();
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    private void insertSubscriptionList(final LessonListModel lessonInfo) {
        // To get User id of Current user so we can travel to Subscription List of User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AppInfo.databaseReference.child("Subscription").child(String.valueOf(user.getUid())).child(lessonInfo.getChannelId()).setValue("Y")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SubscribeActivity.this,"Subscribed : "+lessonInfo.getChannelId(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void attachDatabaseListner() {
        if(mChildEventListener == null){
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String channelId = String.valueOf(dataSnapshot.getKey());
                    LessonListModel friendlyMessage = dataSnapshot.getValue(LessonListModel.class);

//                    Toast.makeText(SubscribeActivity.this," 0"+channelId,Toast.LENGTH_SHORT).show();
                    adapter.add(new LessonListModel(friendlyMessage.getModeratorName(),friendlyMessage.getChannelName(),channelId));
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
