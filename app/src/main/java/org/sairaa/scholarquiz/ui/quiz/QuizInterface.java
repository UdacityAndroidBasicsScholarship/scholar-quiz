package org.sairaa.scholarquiz.ui.quiz;

/**
 * Created by Vinay Gupta on 02-05-2018.
 */

public interface QuizInterface {

    void showDialog();

    void hideDialog();

    void loadQuestion();

    void complete();

    void onError(Exception e);
}
