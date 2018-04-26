package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.model.LessonQuizModel;

import java.util.ArrayList;
import java.util.List;

public class QuizModeratorActivity extends AppCompatActivity {

//    private ListView modListView;
//    private ArrayAdapter adapter;
//    private ArrayList<String> quizList;
    public LessonQuizModel quizModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_moderator);

        Intent intent = getIntent();
        String channelId = intent.getStringExtra("channelId");
        Toast.makeText(QuizModeratorActivity.this,"Moderator Page "+channelId,Toast.LENGTH_SHORT).show();

//        ArrayAdapter<String> itemsAdapter =
//                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quizList);
//
//        modListView = findViewById(R.id.moderator_lesson_list);
//        quizList.add("Quiz 1");
//        quizList.add("Quiz 2");
//        adapter = new ArrayAdapter<String>(this,R.layout.subscrived_lesson_list,quizList);
//        modListView.setAdapter(adapter);

        final ArrayList<String> arrayOfQuiz = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference().child("ChannelQuiz").child(channelId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//
                        for (final DataSnapshot quizListSnapshot : dataSnapshot.getChildren()) {
//
                            String createdBy = quizListSnapshot.child("createdBy").getValue(String.class);
                            String quizName = quizListSnapshot.child("quizName").getValue(String.class);
                            arrayOfQuiz.add(quizName);
//                              quizModel = dataSnapshot.getValue(LessonQuizModel.class);
//                              Toast.makeText(QuizModeratorActivity.this,"quiz "+createdBy,Toast.LENGTH_SHORT).show();
//
                        }
//                        LessonQuizModel quizModel = dataSnapshot.getChildren(LessonQuizModel)
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//        arrayOfQuiz.add("Quiz 1");
//        arrayOfQuiz.add("Quiz 2");
//        arrayOfQuiz.add("Quiz 3");
        QuizAdapter quizAdapter = new QuizAdapter(this, arrayOfQuiz);
        ListView listView = findViewById(R.id.moderator_lesson_list);
        listView.setAdapter(quizAdapter);

    }

    private class QuizAdapter extends ArrayAdapter<String>{

        public QuizAdapter(Context context, ArrayList<String> quizes) {
            super(context, 0, quizes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String quizName = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.mod_quiz_list, parent, false);
            }

            TextView quizNameTextView = convertView.findViewById(R.id.moderator_quiz_name);

            quizNameTextView.setText(quizName);

            notifyDataSetChanged();

            return convertView;

        }
    }
}
