package org.sairaa.scholarquiz.Interfaces;

/**
 * Created by Vinay Gupta on 11-04-2018.
 */

public interface DataLoad_Interface {

    void successful(String response);        //provide the reponse of an api.

    void onError();                         //TODO  later add an exception according to api rsult
}
