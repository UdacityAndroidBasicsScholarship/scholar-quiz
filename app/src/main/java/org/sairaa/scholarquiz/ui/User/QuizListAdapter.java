package org.sairaa.scholarquiz.ui.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.UserAttemptedModel;

import java.util.ArrayList;

/**
 * Created by Vinay Gupta on 29-04-2018.
 */

public class QuizListAdapter extends BaseAdapter {

    ArrayList<UserAttemptedModel> list;
    LayoutInflater inflater;

    public QuizListAdapter(ArrayList<UserAttemptedModel> list, Context context){

        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        UserAttemptedModel lessonListModel = list.get(i);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.quizlist_layout, viewGroup, false);
        }

        TextView quizNameTextView = convertView.findViewById(R.id.quiz_name);
        TextView quizStatusTextView = convertView.findViewById(R.id.quiz_status);
        TextView progressBar = convertView.findViewById(R.id.questionStatus);

        quizNameTextView.setText(lessonListModel.getQuizName());
        quizStatusTextView.setText(lessonListModel.getStatus());

        if(lessonListModel.getStatus().equalsIgnoreCase("completed") ||
                lessonListModel.getStatus().equalsIgnoreCase("attempted")){

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setText(lessonListModel.getCorrectQuestions()  + " / " + lessonListModel.getTotalQuestions());

        } else {
            progressBar.setVisibility(View.GONE);
        }
        return convertView;
    }
}
