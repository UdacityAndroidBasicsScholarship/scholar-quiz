package org.sairaa.scholarquiz;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomAdminAllUserAdapter extends ArrayAdapter<UserList> {

    LayoutInflater inflter;
    UserList user;

    // Declare Firebase Database Reference
    private DatabaseReference mDatabase;
    private DatabaseReference mChannelRef;

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

    public CustomAdminAllUserAdapter(Context context, List<UserList> userList){
        super(context,0, userList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_admin_user_list,parent,false);
        }

        user = getItem(position);

        TextView userNameTextView = convertView.findViewById(R.id.textView_UserName);
        TextView userEmailTextView = convertView.findViewById(R.id.textView_UserEmail);

        userNameTextView.setText(String.valueOf(user.getName()));
        userEmailTextView.setText(String.valueOf("Email ID: " + user.getEmailId()));

        return convertView;
    }
}
