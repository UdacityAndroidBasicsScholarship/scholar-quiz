package org.sairaa.scholarquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LessonSubscriptionAdapter extends ArrayAdapter<LessonInfo> {

    private static final String LOG_ADAPTER = LessonSubscriptionAdapter.class.getName();

    public LessonSubscriptionAdapter(@NonNull Context context, ArrayList<LessonInfo> lessonList) {
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
        LessonInfo dataToDisplay = getItem(position);
        TextView lName = listItemView.findViewById(R.id.lesson_name);
        // set lesson name
        lName.setText(dataToDisplay.getlName());
        Log.i(LOG_ADAPTER,dataToDisplay.getlName());
        TextView subscribeText = listItemView.findViewById(R.id.subscribe_list);
        subscribeText.setText("Subscribe");
        return listItemView;
        //return super.getView(position, convertView, parent);
    }
}
