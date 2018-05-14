package org.sairaa.scholarquiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyScorecardChannelActivity extends AppCompatActivity {

    ListView myScorecardChannelList;
    CustomMyScorecardChannelAdapter customAdapter;
    List<ChannelList> scorecardChannelList;
    ChannelList scorecardChannel = new ChannelList();

    //Database Reference
    private DatabaseReference mDatabase;
    private DatabaseReference mChannelRef;
    private DatabaseReference mScoreChannelRef;
    private DatabaseReference mQuizListRef;
    FirebaseUser user;

    // Variables to store information of user
    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    String totalNumberOfQuiz;
    String totalUserQuiz;
    String quizListId;
    Integer myQuizCount = 0;
    String userId;
    String userName;
    Dialog menuDialog;
    Bundle userBundle;

    StorageReference downloadImageStorageReference;
    FirebaseStorage storage;
    Bitmap bitmap;
    ImageView imageView_UserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scorecard_channel_listview);

        menuDialog = new Dialog(this);

        userBundle = getIntent().getExtras();
        userName = userBundle.getString("userName","User Name Default");

        // ListView to show list of channels available
        myScorecardChannelList = findViewById(R.id.listView_Score_Channel);
        scorecardChannelList = new ArrayList<>();

        // To get User id of Current user so we can travel to Subscription List of User
        user = FirebaseAuth.getInstance().getCurrentUser();

        userId = String.valueOf(user.getUid());

        storage = FirebaseStorage.getInstance();
        downloadImageStorageReference = storage.getReferenceFromUrl("gs://scholar-quiz.appspot.com").child("images/").child(userId);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mChannelRef = mDatabase.child("SQ_ChannelList/");
        mScoreChannelRef = mDatabase.child("SQ_Score/");
        mQuizListRef = mDatabase.child("SQ_QuizList/");

        if (!isNetworkAvailable()) {
            Toast.makeText(MyScorecardChannelActivity.this, "To view your channels, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();
        }

        /**
         * Get all the channels in whose quizzes user has participated
         * */

        mScoreChannelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot scoreChannelSnapshot) {

                for (DataSnapshot scoreChannelSnap : scoreChannelSnapshot.getChildren()) {

                    channelId = String.valueOf(scoreChannelSnap.getKey());

                    for (DataSnapshot scoreQuizSnap: scoreChannelSnap.getChildren()) {

                        quizListId = String.valueOf(scoreQuizSnap.getKey());

                        for (DataSnapshot userSnap : scoreQuizSnap.getChildren() ) {

                            if (userId.equals(String.valueOf(userSnap.getKey()))) {

                                myQuizCount++;

                            }

                        }

                    }

                    mQuizListRef.child(channelId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            totalNumberOfQuiz = String.valueOf(dataSnapshot.getChildrenCount());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mChannelRef.child(channelId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            channelName = String.valueOf(dataSnapshot.child("Name").getValue());
                            moderatorId = String.valueOf(dataSnapshot.child("ModeratorID").getValue());
                            moderatorName = String.valueOf(dataSnapshot.child("Moderator").getValue());

                            totalUserQuiz = String.valueOf(myQuizCount);
                            scorecardChannelList.add(new ChannelList(moderatorName,moderatorId,channelName,channelId,totalNumberOfQuiz, totalUserQuiz));

                            customAdapter = new CustomMyScorecardChannelAdapter(getApplicationContext(), scorecardChannelList);
                            myScorecardChannelList.setAdapter(customAdapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getMessage());
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        // When user selects a channel take user to the activity showing all the quizzes in that activity
        myScorecardChannelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                ChannelList itemSelected = scorecardChannelList.get(i);

                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getModeratorName());
                moderatorId = String.valueOf(itemSelected.getModeratorID());

                // quizList Activity Intent
                Intent quizListIntent = new Intent(MyScorecardChannelActivity.this, MyScoreQuizListActivity.class);

                quizListIntent.putExtra("channelId", channelId);
                quizListIntent.putExtra("channelName", channelName);
                quizListIntent.putExtra("moderatorName", moderatorName);
                quizListIntent.putExtra("moderatorId", moderatorId);
                quizListIntent.putExtra("userName", userName);

                startActivity(quizListIntent);

            }
        });



        findViewById(R.id.button_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(MyScorecardChannelActivity.this, HomeActivity.class);
                backIntent.putExtra("userName", userName);
                startActivity(backIntent);
            }
        });

    }

    /**
     * Menu Functions
     * */

    /**
     * 1. Function to Show Popup Menu
     * */
    public void showPopUp(View view) {

        menuDialog.setContentView(R.layout.menupopup);
        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtUserName = (TextView) menuDialog.getWindow().findViewById(R.id.textview_UserName);
        txtUserName.setText(userName);

        // Download User Image from Firebase and show it to User.
        final long ONE_MEGABYTE = 1024 * 1024;
        downloadImageStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView_UserPhoto.setImageBitmap(bitmap);

            }
        });

        imageView_UserPhoto = menuDialog.getWindow().findViewById(R.id.imageview_UserImage);

        if(bitmap != null) {
            imageView_UserPhoto.setImageBitmap(bitmap);
        }else {
            imageView_UserPhoto.setImageResource(R.drawable.userimage_default);
        }


        menuDialog.show();
    }

    /**
     * 2. Function to close Popup Menu
     * */
    public void closeMenu(View view) {
        menuDialog.dismiss();
    }

    /**
     * 3. Function to Show logout user
     * */

    public void logout(View view) {
        if (isNetworkAvailable()) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MyScorecardChannelActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(MyScorecardChannelActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }


    public void editProfilePressed(View view) {

        Intent editProfileIntent = new Intent(MyScorecardChannelActivity.this, UserProfileActivity.class);
        startActivity(editProfileIntent);

        finish();

    }

    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(MyScorecardChannelActivity.this, UserChannelActivity.class);

        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(MyScorecardChannelActivity.this, AllChannelListActivity.class);

        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(MyScorecardChannelActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(MyScorecardChannelActivity.this, LeaderboardChannelActivity.class);

        myLeaderboardIntent.putExtra("userName", userName);
        startActivity(myLeaderboardIntent);

        finish();
    }

    /**
     * Function to check if Device is connected to Internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void homeButton(View view){
        Intent homeIntent = new Intent(MyScorecardChannelActivity.this, HomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }

}
