package org.sairaa.scholarquiz;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class QuestionList {

    private String QuizKey;
    private String Answer1;
    private Boolean Answer1Flag;
    private String Answer2;
    private Boolean Answer2Flag;
    private String Answer3;
    private Boolean Answer3Flag;
    private String Answer4;
    private Boolean Answer4Flag;
    private String AnswerUserInput;
    private String ChannelID;
    private String Question;
    private String QuestionType;
    private String QuizListKey;


    public QuestionList() {
        /*Blank default constructor essential for Firebase*/
    }

    public QuestionList(String QuizKey, String Answer1, Boolean Answer1Flag, String Answer2, Boolean Answer2Flag, String Answer3, Boolean Answer3Flag,
                        String Answer4, Boolean Answer4Flag, String AnswerUserInput, String ChannelID, String Question, String QuestionType, String QuizListKey) {

        this.QuizKey = QuizKey;
        this.Answer1 = Answer1;
        this.Answer1Flag = Answer1Flag;
        this.Answer2 = Answer2;
        this.Answer2Flag = Answer2Flag;
        this.Answer3 = Answer3;
        this.Answer3Flag = Answer3Flag;
        this.Answer4 = Answer4;
        this.Answer4Flag = Answer4Flag;
        this.AnswerUserInput = AnswerUserInput;
        this.ChannelID = ChannelID;
        this.Question = Question;
        this.QuestionType = QuestionType;
        this.QuizListKey = QuizListKey;

    }

    public void setQuizKey(String QuizKey) { this.QuizKey = QuizKey; }
    public void setAnswer1(String Answer1) { this.Answer1 = Answer1; }
    public void setAnswer1Flag(Boolean Answer1Flag) { this.Answer1Flag = Answer1Flag; }
    public void setAnswer2(String Answer2) { this.Answer2 = Answer2; }
    public void setAnswer2Flag(Boolean Answer2Flag) { this.Answer2Flag = Answer2Flag; }
    public void setAnswer3(String Answer3) { this.Answer3 = Answer3;}
    public void setAnswer3Flag(Boolean Answer3Flag) { this.Answer3Flag = Answer3Flag; }
    public void setAnswer4(String Answer4) { this.Answer4 = Answer4; }
    public void setAnswer4Flag(Boolean Answer4Flag) { this.Answer4Flag = Answer4Flag; }
    public void setAnswerUserInput(String AnswerUserInput) { this.AnswerUserInput = AnswerUserInput; }
    public void setChannelID(String ChannelID) { this.ChannelID = ChannelID; }
    public void setQuestion(String Question) { this.Question = Question; }
    public void setQuestionType(String QuestionType) { this.QuestionType = QuestionType; }
    public void setQuizListKey(String QuizListKey) { this.QuizListKey = QuizListKey; }

    public String getQuizKey() { return QuizKey; }
    public String getAnswer1() {return Answer1;}
    public String getAnswer1Flag() { return String.valueOf(Answer1Flag); }
    public String getAnswer2() { return Answer2; }
    public String getAnswer2Flag() { return String.valueOf(Answer2Flag); }
    public String getAnswer3() {return Answer3;}
    public String getAnswer3Flag() { return String.valueOf(Answer3Flag); }
    public String getAnswer4() { return Answer4; }
    public String getAnswer4Flag() { return String.valueOf(Answer4Flag); }
    public String getAnswerUserInput() {return AnswerUserInput;}
    public String getChannelID() { return ChannelID; }
    public String getQuestion() { return Question; }
    public String getQuestionType() { return QuestionType; }
    public String getQuizListKey() { return QuizListKey; }


}
