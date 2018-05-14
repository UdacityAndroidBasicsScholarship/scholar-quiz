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

public class CustomAdminAllChannelAdapter extends ArrayAdapter<ChannelList> {
    LayoutInflater inflter;
    ChannelList channel;

    public CustomAdminAllChannelAdapter(Context context, List<ChannelList> channelList){
        super(context,0, channelList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_admin_channel_list,parent,false);
        }

        channel = getItem(position);

        TextView channelTextView = convertView.findViewById(R.id.textView_Channel);
        TextView moderatorTextView = convertView.findViewById(R.id.textView_Moderator);

        channelTextView.setText(String.valueOf(channel.getChannelName()));
        moderatorTextView.setText(String.valueOf("Moderator: " + channel.getModeratorName()));

        return convertView;
    }
}
