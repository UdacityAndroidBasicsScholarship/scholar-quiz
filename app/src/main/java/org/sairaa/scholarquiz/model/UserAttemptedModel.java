package org.sairaa.scholarquiz.model;

/**
 * Created by Vinay Gupta on 04-05-2018.
 */

public class UserAttemptedModel {

    public String publishedOn;
    public String quizName;
    public String quizId;
    String status;
    int correctQuestions, totalQuestions;

    public UserAttemptedModel(){

    }

    public UserAttemptedModel(String quizName, String publishedOn){
//        this.quizId = quizId;
        this.quizName = quizName;
        this.publishedOn = publishedOn;
    }


//    public String getQuizId() {
//        return quizId;
//    }

    public String getQuizName() {
        return quizName;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCorrectQuestions() {
        return correctQuestions;
    }

    public void setCorrectQuestions(int correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
