package org.sairaa.scholarquiz.model;

public class LessonListModel {

    public String Moderator;
    public String Name;
    public String channelId;

    public LessonListModel() {
        /*Blank default constructor essential for Firebase*/
    }

    public LessonListModel(String Moderator, String Name, String channelId) {

        this.Moderator = Moderator;
        this.Name = Name;
        this.channelId = channelId;

    }

    public String getModeratorName() {
        return Moderator;
    }

    public String getChannelName() {
        return Name;
    }

    public String getChannelId() {
        return channelId;
    }
}
