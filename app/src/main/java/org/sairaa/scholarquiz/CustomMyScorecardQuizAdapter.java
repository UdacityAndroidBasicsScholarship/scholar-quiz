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

public class CustomMyScorecardQuizAdapter extends ArrayAdapter<QuizList> {

    LayoutInflater inflter;
    QuizList quiz;


    public CustomMyScorecardQuizAdapter(Context context, List<QuizList> quizList){
        super(context,0, quizList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_my_score_quiz_list,parent,false);
        }

        quiz = getItem(position);

        TextView quizTitleTextView = convertView.findViewById(R.id.textView_QuizTitle);
        TextView myScoreTextView = convertView.findViewById(R.id.textView_MyScore);

        quizTitleTextView.setText(String.valueOf(quiz.getQuizTitle()));
        myScoreTextView.setText(String.valueOf("My Score: " + quiz.getScore()));

        notifyDataSetChanged();

        return convertView;

    }
}