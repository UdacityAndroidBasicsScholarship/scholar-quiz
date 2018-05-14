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

public class CustomMyScorecardChannelAdapter extends ArrayAdapter<ChannelList> {

    LayoutInflater inflter;
    ChannelList myScorecardChannel;

    public CustomMyScorecardChannelAdapter(Context context, List<ChannelList> myScorecardChannelList){
        super(context,0, myScorecardChannelList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_my_scorecard_channel,parent,false);
        }

        myScorecardChannel = getItem(position);

        TextView channelTextView = convertView.findViewById(R.id.textView_Channel);
        TextView totalQuizTextView = convertView.findViewById(R.id.textView_TotalQuiz);
        TextView totalUserQuizTextView = convertView.findViewById(R.id.textView_TotalUserQuiz);

        channelTextView.setText(String.valueOf(myScorecardChannel.getChannelName()));
        totalQuizTextView.setText(String.valueOf("Number of Quiz: " + myScorecardChannel.getTotalNumberOfQuiz()));
        totalUserQuizTextView.setText(String.valueOf("You Participated: " + myScorecardChannel.getTotalUserQuiz()));

        notifyDataSetChanged();

        return convertView;

    }

}
