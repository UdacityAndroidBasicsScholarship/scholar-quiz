package org.sairaa.scholarquiz.util;

import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;

/**
 * Created by Vinay Gupta on 02-05-2018.
 */

public class Question_Utility {

    ArrayList<QuizModel> list = new ArrayList<>();

    public static Question_Utility question_utilityInstane;

    private Question_Utility(){}

    public static Question_Utility getArraylistInstance(){
        if(question_utilityInstane == null)
            question_utilityInstane = new Question_Utility();

        return question_utilityInstane;
    }

    public ArrayList<QuizModel> getList() {
        return list;
    }

    public int getListSize(){
        return list.size();
    }
    public void setList(QuizModel quizModel) {
        list.add(quizModel);
    }

    public void clearList(){
        list = new ArrayList<>();
    }

}
