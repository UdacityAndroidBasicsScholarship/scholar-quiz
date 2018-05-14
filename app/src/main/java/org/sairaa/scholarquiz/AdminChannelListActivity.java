package org.sairaa.scholarquiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminChannelListActivity extends AppCompatActivity {

    ListView adminAllChannelListView;

    //Databas Reference
    private DatabaseReference mChannelRef;
    private DatabaseReference mChannelListRef;

    DatabaseReference mDatabase;

    CustomAdminAllChannelAdapter customAdapter;
    List<ChannelList> adminAllChannelList;
    ChannelList channel = new ChannelList();

    SearchView searchChannel;
    List<ChannelList> searchAllChannelList;

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    ChannelList itemSelected;
    Dialog menuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_channel_listview);

        menuDialog = new Dialog(this);

        // ListView to show list of channels available
        adminAllChannelListView = findViewById(R.id.listView_AllChannel);
        adminAllChannelList = new ArrayList<>();

        searchChannel  = findViewById(R.id.searchview_Channel);
        searchAllChannelList = new ArrayList<>();

        // To get User id of Current user so we can travel to Subscription List of User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mChannelRef = mDatabase.child("SQ_ChannelList/");

        if (!isNetworkAvailable()) {
            Toast.makeText(AdminChannelListActivity.this,"To view available channels, Please Connect your Phone to Internet..",Toast.LENGTH_SHORT).show();
        }

        /**
         * Code to Read All Channel List from Firebase and show them in Channel List Activity
         */

        mChannelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot channelSnapshot) {
                adminAllChannelList.clear();
                if (channelSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(AdminChannelListActivity.this, "No Channel Available", Toast.LENGTH_LONG).show();

                } else {

                    for ( DataSnapshot channelListSnapshot : channelSnapshot.getChildren()) {

                        channelId = String.valueOf(channelListSnapshot.getKey());
                        channelName = String.valueOf(channelListSnapshot.child("Name").getValue());
                        moderatorName = String.valueOf(channelListSnapshot.child("Moderator").getValue());
                        moderatorId = String.valueOf(channelListSnapshot.child("ModeratorID").getValue());

                        // Show channels available to user
                        channel = channelListSnapshot.getValue(ChannelList.class);
                        adminAllChannelList.add(new ChannelList(moderatorName,moderatorId, channelName, channelId));
                        customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), adminAllChannelList);
                        adminAllChannelListView.setAdapter(customAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


        adminAllChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                ChannelList itemSelected = adminAllChannelList.get(i);

                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getModeratorName());
                moderatorId = String.valueOf(itemSelected.getModeratorID());

                // quizList Activity Intent
                Intent userListIntent = new Intent(AdminChannelListActivity.this, AdminUserListActivity.class);

                userListIntent.putExtra("channelId", channelId);
                userListIntent.putExtra("channelName", channelName);
                userListIntent.putExtra("moderatorName", moderatorName);
                userListIntent.putExtra("moderatorId", moderatorId);

                startActivity(userListIntent);

            }
        });

        searchChannel.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Boolean foundFlag = false;

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchAllChannelList.clear();

                if(!query.isEmpty()) {

                    for (int i = 0; i < adminAllChannelList.size(); i++) {

                        if (adminAllChannelList.get(i).getChannelName().equals(query)) {

                            searchAllChannelList.add(new ChannelList(adminAllChannelList.get(i).getModeratorName(), adminAllChannelList.get(i).getModeratorID(), adminAllChannelList.get(i).getChannelName(), adminAllChannelList.get(i).getChannelId()));
                            customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), searchAllChannelList);
                            adminAllChannelListView.setAdapter(customAdapter);

                            foundFlag = true;

                            break;

                        } else {
                            foundFlag = false;
                        }

                    }

                    if (!foundFlag) {

                        // Toast.makeText(AdminChannelListActivity.this, "No Match Found", Toast.LENGTH_LONG).show();
                        customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), adminAllChannelList);
                        adminAllChannelListView.setAdapter(customAdapter);
                    }else {
                        getSelectedChannel();
                    }
                }else {

                    customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), adminAllChannelList);
                    adminAllChannelListView.setAdapter(customAdapter);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchAllChannelList.clear();

                if(!newText.isEmpty()) {

                    for (int i = 0; i < adminAllChannelList.size(); i++) {

                        if (adminAllChannelList.get(i).getChannelName().equals(newText)) {

                            searchAllChannelList.add(new ChannelList(adminAllChannelList.get(i).getModeratorName(), adminAllChannelList.get(i).getModeratorID(), adminAllChannelList.get(i).getChannelName(), adminAllChannelList.get(i).getChannelId()));
                            customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), searchAllChannelList);
                            adminAllChannelListView.setAdapter(customAdapter);

                            foundFlag = true;

                            break;

                        } else {
                            foundFlag = false;
                        }

                    }

                    if (!foundFlag) {

                        // Toast.makeText(AdminChannelListActivity.this, "No Match Found", Toast.LENGTH_LONG).show();
                        customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), adminAllChannelList);
                        adminAllChannelListView.setAdapter(customAdapter);
                    }else {
                        getSelectedChannel();
                    }
                }else {

                    customAdapter = new CustomAdminAllChannelAdapter(getApplicationContext(), adminAllChannelList);
                    adminAllChannelListView.setAdapter(customAdapter);
                }
                return false;
            }
        });

    }


    public void getSelectedChannel(){
        searchAllChannelList.clear();

        adminAllChannelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Variable to store selected item
                itemSelected = searchAllChannelList.get(i);

                // Variables to store Channel Information to pass to quizList Activity
                channelId = String.valueOf(itemSelected.getChannelId());
                channelName = String.valueOf(itemSelected.getChannelName());
                moderatorName = String.valueOf(itemSelected.getModeratorName());
                moderatorId = String.valueOf(itemSelected.getModeratorID());

                // quizList Activity Intent
                Intent userListIntent = new Intent(AdminChannelListActivity.this, AdminUserListActivity.class);

                userListIntent.putExtra("channelId", channelId);
                userListIntent.putExtra("channelName", channelName);
                userListIntent.putExtra("moderatorName", moderatorName);
                userListIntent.putExtra("moderatorId", moderatorId);

                startActivity(userListIntent);

            }
        });
    }


    public void goBackButton(View view){
        startActivity(new Intent(AdminChannelListActivity.this, AdminHomeActivity.class));
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
        Intent homeIntent = new Intent(AdminChannelListActivity.this, AdminHomeActivity.class);
        startActivity(homeIntent);
    }
}
