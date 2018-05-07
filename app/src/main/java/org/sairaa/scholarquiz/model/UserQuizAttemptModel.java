package org.sairaa.scholarquiz.model;

/**
 * Created by Vinay Gupta on 03-05-2018.
 */

public class UserQuizAttemptModel {

    String status, answerOption, timeTaken;

    public UserQuizAttemptModel(String answerOption, String timeTaken) {
        this.answerOption = answerOption;
        this.timeTaken = timeTaken;
    }

    public String getStatus() {
        return status;
    }

    public String getAnswerOption() {
        return answerOption;
    }

    public String getTimeTaken() {
        return timeTaken;
    }
}
