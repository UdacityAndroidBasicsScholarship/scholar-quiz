package org.sairaa.scholarquiz.model;

/**
 * Created by Vinay Gupta on 21-04-2018.
 */

public class RegisterModel {

    String slack_id, name, phoneno="";

    public RegisterModel(String slack_id, String name, String phoneno) {
        this.slack_id = slack_id;
        this.name = name;
        this.phoneno = phoneno;
    }

    public String getSlack_id() {
        return slack_id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }
}
