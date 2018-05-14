package org.sairaa.scholarquiz;

public class LessonInfo {
    private int lid;
    private String lName;
    private int modId;

    public LessonInfo(int lid, String lName, int modId) {
        this.lid = lid;
        this.lName = lName;
        this.modId = modId;
    }

    public int getLid() {
        return lid;
    }

    public String getlName() {
        return lName;
    }

    public int getModId() {
        return modId;
    }
}
