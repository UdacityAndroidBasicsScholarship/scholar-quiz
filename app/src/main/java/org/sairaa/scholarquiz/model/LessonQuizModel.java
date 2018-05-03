package org.sairaa.scholarquiz.model;

public class LessonQuizModel {
//    public String quizId;
    public String publishedOn;
    public String quizName;

    public LessonQuizModel(){

    }

    public LessonQuizModel(String quizName, String publishedOn){
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
}
