package org.sairaa.scholarquiz.ui.Login;

/**
 * Created by Vinay Gupta on 22-04-2018.
 */

public interface LoginMVPView {
    void showDialog();

    void hideDialog();

    void authtication(String email, String password);

    void authenticationSucced();

    void authenticationFailed(Exception e);
}
