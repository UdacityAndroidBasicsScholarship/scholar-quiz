package org.sairaa.scholarquiz.ui.Subscription;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.LessonListModel;

import java.util.ArrayList;

public class LessonSubscriptionAdapter extends ArrayAdapter<LessonListModel> {

    private static final String LOG_ADAPTER = LessonSubscriptionAdapter.class.getName();

    public LessonSubscriptionAdapter(@NonNull Context context, ArrayList<LessonListModel> lessonList) {
        super(context, 0, lessonList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lesson_list, parent, false);
        }
        // getting object of lesson info
        LessonListModel dataToDisplay = getItem(position);
        TextView lName = listItemView.findViewById(R.id.lesson_name);
        // set lesson name
        lName.setText(dataToDisplay.getChannelName());
        Log.i(LOG_ADAPTER,dataToDisplay.getChannelName());
        TextView subscribeText = listItemView.findViewById(R.id.subscribe_list);
        subscribeText.setText(dataToDisplay.getModeratorName());
        notifyDataSetChanged();
        return listItemView;
        //return super.getView(position, convertView, parent);
    }
}
