package org.sairaa.scholarquiz;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ChannelList {

    public String mModerator;
    public String mModeratorID;
    public String mName;
    public String mchannelId;
    public String mtotalNumberOfQuiz;
    public String mtotalUserQuiz;

    public ChannelList() {
        /*Blank default constructor essential for Firebase*/
    }

    public ChannelList(String Moderator, String ModeratorID, String Name, String channelId) {

        this.mModerator = Moderator;
        this.mModeratorID = ModeratorID;
        this.mName = Name;
        this.mchannelId = channelId;

    }

    public ChannelList(String Moderator, String ModeratorID, String Name, String channelId, String totalNumberOfQuiz, String totalUserQuiz) {

        this.mModerator = Moderator;
        this.mModeratorID = ModeratorID;
        this.mName = Name;
        this.mchannelId = channelId;
        this.mtotalNumberOfQuiz = totalNumberOfQuiz;
        this.mtotalUserQuiz = totalUserQuiz;
    }

    public String getModeratorName() {
        return mModerator;
    }
    public String getModeratorID() {
        return mModeratorID;
    }
    public String getChannelName() {
        return mName;
    }
    public String getChannelId() {
        return mchannelId;
    }

    public String getTotalNumberOfQuiz() {
        return mtotalNumberOfQuiz;
    }

    public String getTotalUserQuiz() {
        return mtotalUserQuiz;
    }
}
