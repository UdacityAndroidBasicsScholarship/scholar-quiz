package org.sairaa.scholarquiz.ui.Register;

import org.sairaa.scholarquiz.model.RegisterModel;

/**
 * Created by Vinay Gupta on 21-04-2018.
 */

public interface RegisterMVPView {

    void showDialog();

    void hideDialog();

    void authtication(String email, String password);

    void authenticationSucced(String udid, RegisterModel model);

    void authenticationFailed(Exception e);

}
