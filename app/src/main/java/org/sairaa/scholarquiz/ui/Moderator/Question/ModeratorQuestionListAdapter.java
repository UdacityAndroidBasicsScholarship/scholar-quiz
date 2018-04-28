package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;
import java.util.List;

public class ModeratorQuestionListAdapter extends ArrayAdapter<QuizModel> {


    public ModeratorQuestionListAdapter(@NonNull Context context, ArrayList<QuizModel> questionList) {
        super(context, 0, questionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.question_list_item, parent, false);
        }

        QuizModel dataToDisplay = getItem(position);

        TextView questionNo = listItemView.findViewById(R.id.mod_question_no);
        questionNo.setText(String.valueOf(dataToDisplay.getQuestionNo()));

        TextView question = listItemView.findViewById(R.id.mod_question);
        question.setText(dataToDisplay.getQuestion());

        TextView option1 = listItemView.findViewById(R.id.mod_question_option1);
        option1.setText("1. "+dataToDisplay.getOption1());

        TextView option2 = listItemView.findViewById(R.id.mod_question_option2);
        option2.setText("2. "+dataToDisplay.getOption2());

        TextView option3 = listItemView.findViewById(R.id.mod_question_option3);
        option3.setText("3. "+dataToDisplay.getOption3());

        TextView option4 = listItemView.findViewById(R.id.mod_question_option4);
        option4.setText("4. "+dataToDisplay.getOption4());

        notifyDataSetChanged();

        return listItemView;
    }
}
