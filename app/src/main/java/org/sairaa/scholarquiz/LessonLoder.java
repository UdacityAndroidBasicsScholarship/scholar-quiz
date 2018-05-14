package org.sairaa.scholarquiz;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class LessonLoder extends AsyncTaskLoader<List<LessonInfo>> {

    private static final String LOG_LESSONLOADER = LessonLoder.class.getName();

    private String mUrl;

    public LessonLoder(Context context) {
        super(context);
    }
//    public LessonLoder(Context context, String lessonUrl) {
//
//        super(context);
//        mUrl = lessonUrl;
//        Log.i(LOG_LESSONLOADER,"Constructor called..");
//    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<LessonInfo> loadInBackground() {
        /*if (mUrl == null) {
            return null;
        }*/
        Log.i(LOG_LESSONLOADER, "loadInBackground() called..");

        // Network Operation to be made here to retribe data (Unsubscribed Lesson) from server


        List<LessonInfo> lInfo = new ArrayList<>();
        //LessonInfo lessonInfo = new LessonInfo("Hello");
        // Demo Data
        lInfo.add(new LessonInfo(2001, "Lesson 1", 1001));
        lInfo.add(new LessonInfo(2001, "Lesson 2", 1001));
        lInfo.add(new LessonInfo(2001, "Lesson 3", 1001));
        lInfo.add(new LessonInfo(2001, "Lesson 4", 1001));
        lInfo.add(new LessonInfo(2001, "Lesson 5", 1001));
        //lInfo.add(new LessonInfo("Hello"));
//        lInfo.add(new LessonInfo(Integer.parseInt("2002"),"Lesson 2",Integer.parseInt("1001")));
//        lInfo.add(new LessonInfo(Integer.parseInt("2003"),"Lesson 3",Integer.parseInt("1001")));

        return lInfo;
    }
}
