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

public class CustomLeaderboardChannelAdapter extends ArrayAdapter<ChannelList>{

    LayoutInflater inflter;
    ChannelList leaderboardChannel;

    public CustomLeaderboardChannelAdapter(Context context, List<ChannelList> myScorecardChannelList){
        super(context,0, myScorecardChannelList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_leaderboard_channel,parent,false);
        }

        leaderboardChannel = getItem(position);

        TextView channelTextView = convertView.findViewById(R.id.textView_Channel);
        channelTextView.setText(String.valueOf(leaderboardChannel.getChannelName()));

        notifyDataSetChanged();

        return convertView;

    }
}
