package org.sairaa.scholarquiz;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import org.sairaa.scholarquiz.data.QuizContract.subscriptionEntry;

public class LessonCursorAdapter extends CursorAdapter {
    public LessonCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        return LayoutInflater.from(context).inflate(R.layout.subscrived_lesson_list, parent,false);


    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView lessonNameText = view.findViewById(R.id.sub_lesson_name);
        Button navigationButton = view.findViewById(R.id.subscribe_nav);

        int nameCoulmnIndex = cursor.getColumnIndex(subscriptionEntry.L_NAME);

        String lessonName = cursor.getString(nameCoulmnIndex);

        lessonNameText.setText(lessonName);

    }
}
