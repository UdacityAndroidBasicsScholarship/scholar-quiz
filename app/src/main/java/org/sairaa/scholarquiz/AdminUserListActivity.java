package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminUserListActivity extends AppCompatActivity {

    ListView userListView;

    CustomAdminAllUserAdapter customAdapter;
    List<UserList> allUserList;
    UserList user = new UserList();

    SearchView searchUser;
    List<UserList> searchAllUserList;

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersRef;
    private DatabaseReference mChannelListRef;

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;

    String adminFlag;
    String emailId;
    String moderatorFlag;
    String userName;
    String slackId;
    String userId;

    String mAdminFlag;
    String mEmailId;
    String mModeratorFlag;
    String mUserName;
    String mSlackId;
    String mUserId;
    String mChannelName;
    String mChannelId;

    UserList itemSelected;
    Bundle channelBundle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_listview);

        channelBundle = getIntent().getExtras();

        channelId = channelBundle.getString("channelId", "Channel Id");
        channelName = channelBundle.getString("channelName", "Channel Name");
        moderatorName = channelBundle.getString("moderatorName", "Moderator Name");
        moderatorId = channelBundle.getString("moderatorId", "Moderator Id");

        userListView = findViewById(R.id.listView_AllUsers);
        allUserList = new ArrayList<>();

        searchUser = findViewById(R.id.searchview_User);
        searchAllUserList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUsersRef = mDatabase.child("SQ_Users/");
        mChannelListRef = mDatabase.child("SQ_ChannelList/");

        if (!isNetworkAvailable()) {
            Toast.makeText(AdminUserListActivity.this, "Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();
        }

        mUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot usersListSnapshot) {
                allUserList.clear();

                if (usersListSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(AdminUserListActivity.this, "No Users Available", Toast.LENGTH_LONG).show();

                } else {

                    for (DataSnapshot usersListSnap : usersListSnapshot.getChildren()) {

                        adminFlag = String.valueOf(usersListSnap.child("AdminFlag").getValue());
                        emailId = String.valueOf(usersListSnap.child("EmailId").getValue());
                        moderatorFlag = String.valueOf(usersListSnap.child("ModeratorFlag").getValue());
                        userName = String.valueOf(usersListSnap.child("Name").getValue());
                        slackId = String.valueOf(usersListSnap.child("SlackId").getValue());
                        userId = String.valueOf(usersListSnap.getKey());

                        user = usersListSnap.getValue(UserList.class);
                        allUserList.add(new UserList(adminFlag, emailId, moderatorFlag, userName, slackId, userId, channelId, channelName));
                        customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                        userListView.setAdapter(customAdapter);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                itemSelected = allUserList.get(i);


                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getName());
                moderatorId = String.valueOf(itemSelected.getUserId());

                userId = String.valueOf(itemSelected.getUserId());
                adminFlag = String.valueOf(itemSelected.getAdminFlag());
                moderatorFlag = String.valueOf(itemSelected.getModeratorFlag());

                if (moderatorFlag.equals("No")) {

                    //Toast.makeText(AdminUserListActivity.this,"You selected " + moderatorName + " as Moderator for Channel " + channelName,Toast.LENGTH_SHORT).show();

                    showAlertDialog("Moderator Selected", "Assign " + moderatorName + " as the Moderator of Channel " + channelName + "?");


                } else {
                    Toast.makeText(AdminUserListActivity.this, moderatorName + " is already a Moderator. Please select other User", Toast.LENGTH_SHORT).show();
                }

            }
        });


        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Boolean foundFlag = false;

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAllUserList.clear();

                if (!query.isEmpty()) {

                    for (int i = 0; i < allUserList.size(); i++) {

                        mAdminFlag = String.valueOf(allUserList.get(i).getAdminFlag());
                        mEmailId = String.valueOf(allUserList.get(i).getEmailId());
                        mModeratorFlag = String.valueOf(allUserList.get(i).getModeratorFlag());
                        mUserName = String.valueOf(allUserList.get(i).getName());
                        mSlackId = String.valueOf(allUserList.get(i).getSlackId());
                        mUserId = String.valueOf(allUserList.get(i).getUserId());

                        if(mUserName.equals(query) || mEmailId.equals(query)) {

                            searchAllUserList.add(new UserList(mAdminFlag, mEmailId,mModeratorFlag,mUserName,mSlackId,mUserId,channelId,channelName));

                            customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), searchAllUserList);
                            userListView.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                        userListView.setAdapter(customAdapter);
                    }else {
                        getSelectedUser();
                    }

                }else {

                    customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                    userListView.setAdapter(customAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAllUserList.clear();

                if (!newText.isEmpty()) {

                    for (int i = 0; i < allUserList.size(); i++) {

                        mAdminFlag = String.valueOf(allUserList.get(i).getAdminFlag());
                        mEmailId = String.valueOf(allUserList.get(i).getEmailId());
                        mModeratorFlag = String.valueOf(allUserList.get(i).getModeratorFlag());
                        mUserName = String.valueOf(allUserList.get(i).getName());
                        mSlackId = String.valueOf(allUserList.get(i).getSlackId());
                        mUserId = String.valueOf(allUserList.get(i).getUserId());

                        if(mUserName.equals(newText) || mEmailId.equals(newText)) {

                            searchAllUserList.add(new UserList(mAdminFlag, mEmailId,mModeratorFlag,mUserName,mSlackId,mUserId,channelId,channelName));

                            customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), searchAllUserList);
                            userListView.setAdapter(customAdapter);

                            foundFlag = true;
                            break;

                        }else {
                            foundFlag = false;

                        }

                    }

                    if (!foundFlag) {

                        customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                        userListView.setAdapter(customAdapter);
                    }else {
                        getSelectedUser();
                    }

                }else {

                    customAdapter = new CustomAdminAllUserAdapter(getApplicationContext(), allUserList);
                    userListView.setAdapter(customAdapter);
                }
                return false;
            }
        });

    }

    public void getSelectedUser(){

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                itemSelected = searchAllUserList.get(i);


                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getName());
                moderatorId = String.valueOf(itemSelected.getUserId());

                userId = String.valueOf(itemSelected.getUserId());
                adminFlag = String.valueOf(itemSelected.getAdminFlag());
                moderatorFlag = String.valueOf(itemSelected.getModeratorFlag());

                if (moderatorFlag.equals("No")) {

                    //Toast.makeText(AdminUserListActivity.this,"You selected " + moderatorName + " as Moderator for Channel " + channelName,Toast.LENGTH_SHORT).show();

                    showAlertDialog("Moderator Selected", "Assign " + moderatorName + " as the Moderator of Channel " + channelName + "?");


                } else {
                    Toast.makeText(AdminUserListActivity.this, moderatorName + " is already a Moderator. Please select other User", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void updateData() {

        mUsersRef.child(moderatorId).child("ModeratorFlag").setValue("Yes");
        mChannelListRef.child(channelId).child("Moderator").setValue(moderatorName);
        mChannelListRef.child(channelId).child("ModeratorID").setValue(moderatorId);

    }

    public void goBackButton(View view) {

        Intent backIntent = new Intent(AdminUserListActivity.this, AdminChannelListActivity.class);
        backIntent.putExtra("userName", userName);
        startActivity(backIntent);

    }

    public void homeButton(View view) {
        Intent homeIntent = new Intent(AdminUserListActivity.this, AdminHomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }

    /**
     * Function to check if Device is connected to Internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showAlertDialog(String title, String message) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(AdminUserListActivity.this, moderatorName + " is now Moderator of channel " + channelName, Toast.LENGTH_SHORT).show();
                updateData();
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}