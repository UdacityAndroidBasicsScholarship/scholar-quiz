package org.sairaa.scholarquiz;

public class ChannelList {

    public String Moderator;
    public String Name;
    public String channelId;

    public ChannelList() {
        /*Blank default constructor essential for Firebase*/
    }

    public ChannelList(String Moderator, String Name, String channelId) {

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
