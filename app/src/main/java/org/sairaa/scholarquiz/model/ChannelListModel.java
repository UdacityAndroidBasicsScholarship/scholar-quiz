package org.sairaa.scholarquiz.model;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class ChannelListModel implements Serializable {
    @PropertyName("Moderator")
    public String Moderator;
    @PropertyName("Name")
    public String Name;
    public ChannelListModel(){

    }

    public ChannelListModel(String Moderator, String Name){
        this.Moderator = Moderator;
        this.Name = Name;
    }
    @PropertyName("Moderator")
    public String getModerator() {
        return Moderator;
    }
    @PropertyName("Name")
    public String getName() {
        return Name;
    }
}
