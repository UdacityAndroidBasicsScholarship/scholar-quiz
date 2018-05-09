package org.sairaa.scholarquiz.ui.User;

/**
 * Created by Vinay Gupta on 29-04-2018.
 */

public interface QuizActivityInterface {

    void hideDialog();
    void showDialog();
    void loadQuestion();
    void complete();
    void onError(Exception e);
}
