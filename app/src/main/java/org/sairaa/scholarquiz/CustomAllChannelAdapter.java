package org.sairaa.scholarquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomAllChannelAdapter extends ArrayAdapter<ChannelList> {

    LayoutInflater inflter;
    ChannelList channel;

    // Declare Firebase Database Reference
    private DatabaseReference mDatabase;
    private DatabaseReference mSubscriptionListRef;

    // Declare Firebase user Reference
    FirebaseUser user;

    public CustomAllChannelAdapter(Context context, List<ChannelList> channelList){
        super(context,0, channelList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_all_channel_list,parent,false);
        }

        channel = getItem(position);

        TextView channelTextView = convertView.findViewById(R.id.textView_Channel);
        TextView moderatorTextView = convertView.findViewById(R.id.textView_Moderator);

        channelTextView.setText(String.valueOf(channel.getChannelName()));
        moderatorTextView.setText(String.valueOf("Moderator: " + channel.getModeratorName()));

        /**
         * When User clicks on Channel in Channel List
         * - Add the UserId to Subscription Table in Firebase
         * - Add the channel id to Subscription Table in Firebase
         * - Set the Flag to Y for the Channel
         * */

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                user = FirebaseAuth.getInstance().getCurrentUser();

                String userId = String.valueOf(user.getUid());
                String channelId = String.valueOf(getItem(position).getChannelId());

                // Save the Users information in Users table in Firebase
                mSubscriptionListRef = mDatabase.child("SQ_Subscription/"+ userId);
                mSubscriptionListRef.child(channelId).setValue("Y");

                remove(channel);
                notifyDataSetChanged();
                clear();
            }

        });
        return convertView;
    }
}