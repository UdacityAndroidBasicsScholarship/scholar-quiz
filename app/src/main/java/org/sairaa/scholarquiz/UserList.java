package org.sairaa.scholarquiz;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserList {

    private String mAdminFlag;
    private String mEmailId;
    private String mModeratorFlag;
    private String mName;
    private String mSlackId;
    private String mUserId;
    private String mChannelId;
    private String mChannelName;

    public UserList() {
        /*Blank default constructor essential for Firebase*/
    }

    public UserList(String AdminFlag, String EmailId, String ModeratorFlag, String Name, String SlackId, String UserId, String channelId, String channelName) {

        this.mAdminFlag = AdminFlag;
        this.mEmailId = EmailId;
        this.mModeratorFlag = ModeratorFlag;
        this.mName = Name;
        this.mSlackId = SlackId;
        this.mUserId = UserId;
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    public String getAdminFlag() {
        return mAdminFlag;
    }

    public String getEmailId() {
        return mEmailId;
    }

    public String getModeratorFlag() {
        return mModeratorFlag;
    }

    public String getName() {
        return mName;
    }

    public String getSlackId() {
        return mSlackId;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getChannelName() {
        return mChannelName;
    }

}
