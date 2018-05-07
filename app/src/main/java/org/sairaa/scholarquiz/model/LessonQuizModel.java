package org.sairaa.scholarquiz.model;

public class LessonQuizModel {
//    public String quizId;
    public String createdBy;
    public String quizName;
    public String quizId;

    public LessonQuizModel(){

    }

    public LessonQuizModel(String quizName, String createdBy){
//        this.quizId = quizId;
        this.quizName = quizName;
        this.createdBy = createdBy;
    }


//    public String getQuizId() {
//        return quizId;
//    }

    public String getQuizName() {
        return quizName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
}
