package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class UserChannelActivity extends AppCompatActivity {

    ListView channelList;

    //Databas Reference
    private DatabaseReference mChannelRef;
    private DatabaseReference mChannelListRef;
    private DatabaseReference mSubscriptionListRef;

    DatabaseReference mDatabase;

    CustomUserChannelAdapter customAdapter;
    List<ChannelList> userChannelList;
    ChannelList userChannel = new ChannelList();

    String channelExist = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_channel_listview);


        // ListView to show list of channels available
        channelList = findViewById(R.id.listView_UserChannel);
        userChannelList = new ArrayList<>();


        // To get User id of Current user so we can travel to Subscription List of User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSubscriptionListRef = mDatabase.child("Subscription/" + String.valueOf(user.getUid()));

        mChannelRef = mDatabase.child("ChannelList/");


        /**
         * Code to Read All Channel List from Firebase and show them in Channel List Activity
         */

        // Read all the channels name from Firebase
        mChannelRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot channelSnapshot) {

                // Clear the Channel List View
                //AllChannelList.clear();

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
                                    // If channel is already subscribed to by user ser the channelExist to Y
                                    if (channelListSnapshot.getKey().equals(subscriptionListSnapshot.getKey())) {
                                        //Toast.makeText(ChannelListActivity.this, "Channel Key Exists:" + String.valueOf(subscriptionListSnapshot.getValue()), Toast.LENGTH_LONG).show();
                                        channelExist = "Y";
                                        break;
                                    } else {
                                        channelExist = "N";
                                    }

                                }

                                // If channelExist = Y i.e channel is  subscribed then add it to channel list adapter to show it to user else set it to N
                                if (channelExist.equals("Y")) {

                                    String channelId = String.valueOf(channelListSnapshot.getKey());
                                    // Show channels available to user
                                    userChannel = channelListSnapshot.getValue(ChannelList.class);
                                    userChannelList.add(new ChannelList(userChannel.getModeratorName(), userChannel.getChannelName(), channelId));
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
                startActivity(new Intent(UserChannelActivity.this, AllChannelListActivity.class));
                finish();
            }
        });


        // Sign Out User when Sign out button is clicked
        findViewById(R.id.button_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserChannelActivity.this, LoginActivity.class));
                finish();
            }
        });


        // When user selects a channel take user to the activity showing all the quizzes in that activity
        channelList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){

                // Variable to store selected item
                ChannelList itemSelected = userChannelList.get(i);

                // Variables to store Channel Information to pass to quizList Activity
                String channelId = itemSelected.getChannelId();
                String channelName= itemSelected.getChannelName();
                String moderaterName = "\nModerator: " + itemSelected.getModeratorName();

                // quizList Activity Intent
                Intent quizListIntent = new Intent(UserChannelActivity.this, QuizListActivity.class);
                quizListIntent.putExtra("channelId",channelId);
                quizListIntent.putExtra("channelName",channelName);
                quizListIntent.putExtra("ModeratorName", moderaterName);
                startActivity(quizListIntent);
            }
        });


    }

}




