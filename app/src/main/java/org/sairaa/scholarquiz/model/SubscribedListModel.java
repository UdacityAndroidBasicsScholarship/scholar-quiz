package org.sairaa.scholarquiz.model;

public class SubscribedListModel {
    public String channelId;
    public String exesistance;

    public SubscribedListModel(String channelId, String exesistance){
        this.channelId = channelId;
        this.exesistance = exesistance;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getExesistance() {
        return exesistance;
    }
}
