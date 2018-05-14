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

public class CustomLeaderScoreboardAdapter extends ArrayAdapter<QuizList> {

    LayoutInflater inflter;
    QuizList quiz;


    public CustomLeaderScoreboardAdapter(Context context, List<QuizList> quizList){
        super(context,0, quizList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = inflter.from(getContext()).inflate(R.layout.activity_leader_scoreboard,parent,false);
        }

        quiz = getItem(position);

        TextView quizTitleTextView = convertView.findViewById(R.id.textView_LeaderName);
        TextView leaderScoreTextView = convertView.findViewById(R.id.textView_Score);
        TextView totalQuestionsTextView = convertView.findViewById(R.id.textview_TotalQuestions);

        quizTitleTextView.setText(String.valueOf(quiz.getUserName()));
        leaderScoreTextView.setText(String.valueOf("Score: " + quiz.getScore() + " Points"));
        totalQuestionsTextView.setText(String.valueOf("Total Questions: " + quiz.getTotalQuestions()));

        notifyDataSetChanged();

        return convertView;

    }
}