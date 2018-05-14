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


public class AllChannelListActivity extends AppCompatActivity {

    ListView channelList;

    //Databas Reference

    private DatabaseReference mChannelRef;
    private DatabaseReference mChannelListRef;
    private DatabaseReference mSubscriptionListRef;

    DatabaseReference mDatabase;

    StorageReference downloadImageStorageReference;
    FirebaseStorage storage;
    Bitmap bitmap;
    ImageView imageView_UserPhoto;

    CustomAllChannelAdapter customAdapter;
    List<ChannelList> allChannelList;
    ChannelList channel = new ChannelList();

    SearchView searchAllChannel;
    List<ChannelList> searchAllChannelList;

    String channelExist = "N";

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;

    Bundle userBundle;
    String userId;
    String userName;

    Dialog menuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_channel_listview);

        menuDialog = new Dialog(this);

        userBundle = getIntent().getExtras();

        userName = userBundle.getString("userName","User Name");

        // ListView to show list of channels available
        channelList = findViewById(R.id.listView_Channel);
        allChannelList = new ArrayList<>();

        searchAllChannel = findViewById(R.id.searchview_AllChannel);
        searchAllChannelList = new ArrayList<>();

        // To get User id of Current user so we can travel to Subscription List of User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = String.valueOf(user.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        blinkTextView();

        storage = FirebaseStorage.getInstance();
        downloadImageStorageReference = storage.getReferenceFromUrl("gs://scholar-quiz.appspot.com").child("images/").child(userId);

        mSubscriptionListRef = mDatabase.child("SQ_Subscription/" + String.valueOf(user.getUid()));

        mChannelRef = mDatabase.child("SQ_ChannelList/");

        if (!isNetworkAvailable()) {
            Toast.makeText(AllChannelListActivity.this,"To view available channels, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
        }

        /**
         * Code to Read All Channel List from Firebase and show them in Channel List Activity
         */

        // Read all the channels name from Firebase
        mChannelRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot channelSnapshot) {

                // Clear the Channel List View
                allChannelList.clear();

                // Check the count of Channels to subscribe to. If no channel, show message else show list of channels
                if (channelSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(AllChannelListActivity.this, "No Channel to Subscribe", Toast.LENGTH_LONG).show();

                } else {

                    // Loop through all channel lists
                    for (final DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {

                        // Read all the channels that user has subscribed to
                        mSubscriptionListRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot subscriptionSnapshot) {

                                for (DataSnapshot subscriptionListSnapshot : subscriptionSnapshot.getChildren()) {
                                    // If channel is already subscribed to by user ser the channelExist to Y
                                    if (channelListSnapshot.getKey().equals(subscriptionListSnapshot.getKey())) {
                                        channelExist = "Y";
                                        break;
                                    }

                                }

                                // If channelExist = N i.e channel is not subscribed then add it to channel list adapter to show it to user else set it to N
                                if (channelExist.equals("N")) {

                                    channelId = String.valueOf(channelListSnapshot.getKey());
                                    channelName = String.valueOf(channelListSnapshot.child("Name").getValue());
                                    moderatorName = String.valueOf(channelListSnapshot.child("Moderator").getValue());
                                    moderatorId = String.valueOf(channelListSnapshot.child("ModeratorID").getValue());

                                    // Show channels available to user
                                    channel = channelListSnapshot.getValue(ChannelList.class);
                                    allChannelList.add(new ChannelList(moderatorName,moderatorId, channelName, channelId));
                                    customAdapter = new CustomAllChannelAdapter(getApplicationContext(), allChannelList);
                                    channelList.setAdapter(customAdapter);

                                } else {
                                    channelExist = "N";
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getMessage());
                            }
                        });
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });

        searchAllChannel.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Boolean foundFlag = false;

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchAllChannelList.clear();

                if (!query.isEmpty()) {

                    for (int i = 0; i < allChannelList.size(); i++) {

                        channelId = String.valueOf(allChannelList.get(i).getChannelId());
                        channelName = String.valueOf(allChannelList.get(i).getChannelName());
                        moderatorName = String.valueOf(allChannelList.get(i).getModeratorName());
                        moderatorId = String.valueOf(allChannelList.get(i).getModeratorID());

                        if(channelName.equals(query)) {

                            searchAllChannelList.add(new ChannelList(moderatorName,moderatorId,channelName,channelId));

                            customAdapter = new CustomAllChannelAdapter(getApplicationContext(), searchAllChannelList);
                            channelList.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomAllChannelAdapter(getApplicationContext(), allChannelList);
                        channelList.setAdapter(customAdapter);
                    }

                }else {

                    customAdapter = new CustomAllChannelAdapter(getApplicationContext(), allChannelList);
                    channelList.setAdapter(customAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAllChannelList.clear();

                if (!newText.isEmpty()) {

                    for (int i = 0; i < allChannelList.size(); i++) {

                        channelId = String.valueOf(allChannelList.get(i).getChannelId());
                        channelName = String.valueOf(allChannelList.get(i).getChannelName());
                        moderatorName = String.valueOf(allChannelList.get(i).getModeratorName());
                        moderatorId = String.valueOf(allChannelList.get(i).getModeratorID());

                        if(channelName.equals(newText)) {

                            searchAllChannelList.add(new ChannelList(moderatorName,moderatorId,channelName,channelId));

                            customAdapter = new CustomAllChannelAdapter(getApplicationContext(), searchAllChannelList);
                            channelList.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomAllChannelAdapter(getApplicationContext(), allChannelList);
                        channelList.setAdapter(customAdapter);
                    }

                }else {

                    customAdapter = new CustomAllChannelAdapter(getApplicationContext(), allChannelList);
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
                        TextView txtBlink = findViewById(R.id.textView_BlinkMsg);

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

    public void goBackButton(View view) {

        Intent backIntent = new Intent(AllChannelListActivity.this, UserChannelActivity.class);
        backIntent.putExtra("userName", userName);
        startActivity(backIntent);

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
            startActivity(new Intent(AllChannelListActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(AllChannelListActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }


    public void editProfilePressed(View view) {

        Intent editProfileIntent = new Intent(AllChannelListActivity.this, UserProfileActivity.class);
        startActivity(editProfileIntent);

        finish();

    }

    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(AllChannelListActivity.this, UserChannelActivity.class);

        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(AllChannelListActivity.this, AllChannelListActivity.class);

        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(AllChannelListActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(AllChannelListActivity.this, LeaderboardChannelActivity.class);

        myLeaderboardIntent.putExtra("userName", userName);
        startActivity(myLeaderboardIntent);
        finish();
    }


    /**
     * Function to check if Device is connected to Internet
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void homeButton(View view){
        Intent homeIntent = new Intent(AllChannelListActivity.this, HomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }


}
