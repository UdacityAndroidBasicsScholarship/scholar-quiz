package org.sairaa.scholarquiz.ui.attemptedquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;

/**
 * Created by Vinay Gupta on 10-05-2018.
 */

public class AttemptedQuizAdapter extends BaseAdapter {

    ArrayList<QuizModel> list;
    LayoutInflater inflater;

    public AttemptedQuizAdapter(ArrayList<QuizModel> list, Context context){

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

        QuizModel quizModel = list.get(i);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.userattempt_layout, viewGroup, false);
        }

        TextView questionTextView = convertView.findViewById(R.id.attemped_question);
        TextView answerTextView = convertView.findViewById(R.id.attemped_answer);
        TextView userAnswerTextView = convertView.findViewById(R.id.attemped_useranswer);

        questionTextView.setText(quizModel.getQuestion());
        int rightAnswer = Integer.parseInt(String.valueOf(quizModel.getAnswerOption()));
        switch (rightAnswer){
            case 1:
                answerTextView.setText("Correct Answer : "+quizModel.getOption1());
                break;
            case 2:
                answerTextView.setText("Correct Answer : "+quizModel.getOption2());
                break;
            case 3:
                answerTextView.setText("Correct Answer : "+quizModel.getOption3());
                break;
            case 4:
                answerTextView.setText("Correct Answer : "+quizModel.getOption4());
                break;
            default:
                answerTextView.setText("Correct Answer : "+"not Attempted");
        }
//        answerTextView.setText("answer: ->" +getOptionString(i,quizModel.getAnswerOption()));
//        answerTextView.setText(quizModel.getAnswerOption());
        int userOption = Integer.parseInt(String.valueOf(quizModel.getUserOption()));
        switch (userOption){
            case 1:
                userAnswerTextView.setText("Your Choice : "+quizModel.getOption1());
                break;
            case 2:
                userAnswerTextView.setText("Your Choice : "+quizModel.getOption2());
                break;
            case 3:
                userAnswerTextView.setText("Your Choice : "+quizModel.getOption3());
                break;
            case 4:
                userAnswerTextView.setText("Your Choice : "+quizModel.getOption4());
                break;
            default:
                userAnswerTextView.setText("Your Choice : Not answered");
        }
//        answerTextView.setText("answer: ->" +getOptionString(i,quizModel.getAnswerOption()));
//        userAnswerTextView.setText("your choice: ->"+getOptionString(i,quizModel.getUserOption()));

        return convertView;
    }

    private String getOptionString(int position, int optionNo){

        switch (optionNo){

            case 1:
                return list.get(position).getOption1();


            case 2:
                return list.get(position).getOption1();

            case 3:
                return list.get(position).getOption2();

            case 4:
                return list.get(position).getOption3();

            default:
                return "not attempted";

        }
    }

}
