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

public class CustomLeaderboardQuizActivity extends ArrayAdapter<QuizList> {

    LayoutInflater inflter;
    QuizList quiz;


    public CustomLeaderboardQuizActivity(Context context, List<QuizList> quizList){
        super(context,0, quizList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_quiz_list,parent,false);
        }

        quiz = getItem(position);

        TextView quizTitleTextView = convertView.findViewById(R.id.textView_QuizTitle);
        TextView moderatorTextView = convertView.findViewById(R.id.textView_Moderator);

        quizTitleTextView.setText(String.valueOf(quiz.getQuizTitle()));
        moderatorTextView.setText(String.valueOf("Moderator: " + quiz.getModerator()));

        notifyDataSetChanged();

        return convertView;

    }
}