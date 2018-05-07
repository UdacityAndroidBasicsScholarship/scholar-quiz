package org.sairaa.scholarquiz.model;

public class QuizModel {

    public int questionNo;
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public int answerOption;

    public QuizModel(){

    }
    public QuizModel(int questionNo,String question,String option1,String option2,
                     String option3,String option4,int answerOption){

        this.questionNo = questionNo;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerOption = answerOption;

    }

    public int getQuestionNo() {
        return questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getAnswerOption() {
        return answerOption;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }
}
