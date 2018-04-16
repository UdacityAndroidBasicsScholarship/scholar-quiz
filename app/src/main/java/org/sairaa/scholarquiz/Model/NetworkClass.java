package org.sairaa.scholarquiz.Model;

import android.os.AsyncTask;

import org.sairaa.scholarquiz.APIHandler;
import org.sairaa.scholarquiz.Interfaces.DataLoad_Interface;

import java.util.HashMap;

/**
 * Created by Vinay Gupta on 11-04-2018.
 */

public class NetworkClass extends AsyncTask<Void, Void, Void> {

    String url;
    DataLoad_Interface dataLoad_interface;
    HashMap<String, String> params;

    public NetworkClass(String url, DataLoad_Interface dataLoad_interface, HashMap<String, String> params){

        this.url = url;
        this.dataLoad_interface = dataLoad_interface;
        this.params = params;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        APIHandler apiHandler = new APIHandler();

        String result = apiHandler.sendPostRequest(url, params);

        if(result != null)
            dataLoad_interface.successful(result);
        else
            dataLoad_interface.onError();

        return null;
    }
}
