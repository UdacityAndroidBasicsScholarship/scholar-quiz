package org.sairaa.scholarquiz;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class QuizList {

    private String ChannelID;
    private String Moderator;
    private String ModeratorId;
    private String Title;
    private String quizListId;

    String correct;
    String notAttempted;
    String score;
    String totalQuestions;
    String wrong;

    String userName;

    public QuizList() {
        /*Blank default constructor essential for Firebase*/
    }

    public QuizList(String ChannelID, String Moderator, String ModeratorId, String Title, String quizListId) {

        this.ChannelID = ChannelID;
        this.Moderator = Moderator;
        this.ModeratorId = ModeratorId;
        this.Title = Title;
        this.quizListId = quizListId;

    }

    public QuizList(String ChannelID, String Moderator, String ModeratorId, String Title, String quizListId,String correct, String notAttempted, String score, String totalQuestions, String wrong) {

        this.ChannelID = ChannelID;
        this.Moderator = Moderator;
        this.ModeratorId = ModeratorId;
        this.Title = Title;
        this.quizListId = quizListId;
        this.correct = correct;
        this.notAttempted = notAttempted;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.wrong = wrong;
    }

    public QuizList(String ChannelID, String Moderator, String ModeratorId, String Title, String quizListId,String correct, String notAttempted, String score, String totalQuestions, String wrong, String userName) {

        this.ChannelID = ChannelID;
        this.Moderator = Moderator;
        this.ModeratorId = ModeratorId;
        this.Title = Title;
        this.quizListId = quizListId;
        this.correct = correct;
        this.notAttempted = notAttempted;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.wrong = wrong;
        this.userName = userName;
    }

    public String getChannelID() { return ChannelID; }
    public String getModerator() {return Moderator;}
    public String getModeratorID() {return ModeratorId;}
    public String getQuizTitle() { return Title; }
    public String getquizListId() { return quizListId; }

    public String getTitle() {
        return Title;
    }

    public String getCorrect() {
        return correct;
    }

    public String getNotAttempted() {
        return notAttempted;
    }

    public String getScore() {
        return score;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public String getWrong() {
        return wrong;
    }
    public String getUserName() {
        return userName;
    }

}
