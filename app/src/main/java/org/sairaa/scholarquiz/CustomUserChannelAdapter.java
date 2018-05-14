package org.sairaa.scholarquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomUserChannelAdapter extends ArrayAdapter<ChannelList> {

    LayoutInflater inflter;
    ChannelList userChannel;

    public CustomUserChannelAdapter(Context context, List<ChannelList> userChannelList){
        super(context,0, userChannelList);
    }

    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_user_channel,parent,false);
        }

        userChannel = getItem(position);

        TextView channelTextView = convertView.findViewById(R.id.textView_Channel);
        TextView moderatorTextView = convertView.findViewById(R.id.textView_Moderator);

        channelTextView.setText(String.valueOf(userChannel.getChannelName()));
        moderatorTextView.setText(String.valueOf("Moderator: " + userChannel.getModeratorName()));

        notifyDataSetChanged();

        return convertView;

    }
}
