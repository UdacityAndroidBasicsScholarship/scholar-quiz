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
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
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

public class UserChannelActivity extends AppCompatActivity {

    ListView channelList;
    DatabaseReference mDatabase;
    CustomUserChannelAdapter customAdapter;
    List<ChannelList> userChannelList;
    ChannelList userChannel = new ChannelList();
    String channelExist = "N";

    SearchView searchUserChannel;
    List<ChannelList> searchAllUserChannelList;

    //Database Reference
    private DatabaseReference mChannelRef;
    private DatabaseReference mChannelListRef;
    private DatabaseReference mSubscriptionListRef;
    private DatabaseReference mUserRef;

    StorageReference downloadImageStorageReference;
    FirebaseStorage storage;
    Bitmap bitmap;
    ImageView imageView_UserPhoto;

    // Variables to store information of user
    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    FirebaseUser user;

    String userId;
    String userName;
    Bundle userBundle;

    Dialog menuDialog;

    TextView txtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_channel_listview);

        menuDialog = new Dialog(this);

        userBundle = getIntent().getExtras();

        userName = userBundle.getString("userName","User Name");

        // ListView to show list of channels available
        channelList = findViewById(R.id.listView_UserChannel);
        userChannelList = new ArrayList<>();

        searchUserChannel = findViewById(R.id.searchview_UserChannel);
        searchAllUserChannelList = new ArrayList<>();

        // To get User id of Current user so we can travel to Subscription List of User
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = String.valueOf(user.getUid());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSubscriptionListRef = mDatabase.child("SQ_Subscription/" + String.valueOf(user.getUid()));
        mChannelRef = mDatabase.child("SQ_ChannelList/");

        storage = FirebaseStorage.getInstance();
        downloadImageStorageReference = storage.getReferenceFromUrl("gs://scholar-quiz.appspot.com").child("images/").child(userId);

        mUserRef = mDatabase.child(user.getUid());

        if (!isNetworkAvailable()) {
            Toast.makeText(UserChannelActivity.this, "To view your subscribed channels, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();
        }


        blinkTextView();


        /**
         * Code to Read All Channel List from Firebase and show them in Channel List Activity
         */


        //    final ProgressDialog progressDialog = new ProgressDialog(this);
        //   progressDialog.setTitle("Loading...");
        //  progressDialog.show();


        // Read all the channels name from Firebase
        mChannelRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot channelSnapshot) {

                // Check the count of Channels to subscribe to. If no channel, show message else show list of channels
                if (channelSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(UserChannelActivity.this, "Not Subscribed to any Channel!!", Toast.LENGTH_LONG).show();

                } else {

                    // Loop through all channel lists
                    for (final DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {

                        // Read all the channels that user has subscribed to
                        mSubscriptionListRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot subscriptionSnapshot) {

                                for (DataSnapshot subscriptionListSnapshot : subscriptionSnapshot.getChildren()) {

                                    // If channel is already subscribed to by user set the channelExist to Y
                                    if (channelListSnapshot.getKey().equals(subscriptionListSnapshot.getKey())) {
                                        channelExist = "Y";
                                        break;
                                    } else {
                                        channelExist = "N";
                                    }

                                }

                                // If channelExist = Y i.e channel is  subscribed then add it to channel list adapter to show it to user else set it to N
                                if (channelExist.equals("Y")) {

                                    channelId = String.valueOf(channelListSnapshot.getKey());
                                    channelName = String.valueOf(channelListSnapshot.child("Name").getValue());
                                    moderatorName = String.valueOf(channelListSnapshot.child("Moderator").getValue());
                                    moderatorId = String.valueOf(channelListSnapshot.child("ModeratorID").getValue());

                                    // Show channels available to user
                                    userChannel = channelListSnapshot.getValue(ChannelList.class);
                                    userChannelList.add(new ChannelList(moderatorName, moderatorId, channelName, channelId));

                                    customAdapter = new CustomUserChannelAdapter(getApplicationContext(), userChannelList);
                                    channelList.setAdapter(customAdapter);

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getMessage());
                            }
                        });
                    }

                }

                // progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }


        });


        // When Channel List button is clicked, take the user to Channel List screen
        findViewById(R.id.button_ChannelList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent channelListIntent = new Intent(UserChannelActivity.this, AllChannelListActivity.class);
                channelListIntent.putExtra("userName", userName);
                startActivity(channelListIntent);

                finish();
            }
        });


        findViewById(R.id.button_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backIntent = new Intent(UserChannelActivity.this, HomeActivity.class);
                backIntent.putExtra("userName", userName);
                startActivity(backIntent);
            }
        });

        // When user selects a channel take user to the activity showing all the quizzes in that activity
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                ChannelList itemSelected = userChannelList.get(i);

                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getModeratorName());
                moderatorId = String.valueOf(itemSelected.getModeratorID());

                // quizList Activity Intent
                Intent quizListIntent = new Intent(UserChannelActivity.this, QuizListActivity.class);

                quizListIntent.putExtra("channelId", channelId);
                quizListIntent.putExtra("channelName", channelName);
                quizListIntent.putExtra("moderatorName", moderatorName);
                quizListIntent.putExtra("moderatorId", moderatorId);
                quizListIntent.putExtra("userName", userName);

                startActivity(quizListIntent);

            }
        });


        searchUserChannel.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Boolean foundFlag = false;

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchAllUserChannelList.clear();

                if (!query.isEmpty()) {

                    for (int i = 0; i < userChannelList.size(); i++) {

                        channelId = String.valueOf(userChannelList.get(i).getChannelId());
                        channelName = String.valueOf(userChannelList.get(i).getChannelName());
                        moderatorName = String.valueOf(userChannelList.get(i).getModeratorName());
                        moderatorId = String.valueOf(userChannelList.get(i).getModeratorID());

                        if(channelName.equals(query)) {

                            searchAllUserChannelList.add(new ChannelList(moderatorName,moderatorId,channelName,channelId));

                            customAdapter = new CustomUserChannelAdapter(getApplicationContext(), searchAllUserChannelList);
                            channelList.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomUserChannelAdapter(getApplicationContext(), userChannelList);
                        channelList.setAdapter(customAdapter);
                    }

                }else {

                    customAdapter = new CustomUserChannelAdapter(getApplicationContext(), userChannelList);
                    channelList.setAdapter(customAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAllUserChannelList.clear();

                if (!newText.isEmpty()) {

                    for (int i = 0; i < userChannelList.size(); i++) {

                        channelId = String.valueOf(userChannelList.get(i).getChannelId());
                        channelName = String.valueOf(userChannelList.get(i).getChannelName());
                        moderatorName = String.valueOf(userChannelList.get(i).getModeratorName());
                        moderatorId = String.valueOf(userChannelList.get(i).getModeratorID());

                        if(channelName.equals(newText)) {

                            searchAllUserChannelList.add(new ChannelList(moderatorName,moderatorId,channelName,channelId));

                            customAdapter = new CustomUserChannelAdapter(getApplicationContext(), searchAllUserChannelList);
                            channelList.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomUserChannelAdapter(getApplicationContext(), userChannelList);
                        channelList.setAdapter(customAdapter);
                    }

                }else {

                    customAdapter = new CustomUserChannelAdapter(getApplicationContext(), userChannelList);
                    channelList.setAdapter(customAdapter);
                }

                return false;
            }
        });

    }

    private void blinkTextView(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView txtBlink = findViewById(R.id.textView_BlinkText);

                        if(txtBlink .getVisibility() == View.VISIBLE){
                            txtBlink .setVisibility(View.INVISIBLE);
                        }else{
                            txtBlink .setVisibility(View.VISIBLE);
                        }
                        blinkTextView();
                    }
                });
            }
        }).start();
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
            startActivity(new Intent(UserChannelActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(UserChannelActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }


    public void editProfilePressed(View view) {

        Intent editProfileIntent = new Intent(UserChannelActivity.this, UserProfileActivity.class);
        startActivity(editProfileIntent);

        finish();

    }



    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(UserChannelActivity.this, UserChannelActivity.class);

        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(UserChannelActivity.this, AllChannelListActivity.class);

        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(UserChannelActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(UserChannelActivity.this, LeaderboardChannelActivity.class);

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
        Intent homeIntent = new Intent(UserChannelActivity.this, HomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }


}