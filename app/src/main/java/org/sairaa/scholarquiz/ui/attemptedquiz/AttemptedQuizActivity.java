package org.sairaa.scholarquiz.ui.attemptedquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;
import org.sairaa.scholarquiz.util.Question_Utility;

public class AttemptedQuizActivity extends AppCompatActivity {

    String channelId, quizId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempted_quiz);

        channelId = getIntent().getStringExtra("channelId");
        quizId = getIntent().getStringExtra("quizId");

        fetchUserResponse();

    }

    private void fetchUserResponse(){

        AppInfo.databaseReference.child("Quiz").child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    getInstance().clearList();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        QuizModel questionAnswerModel = snapshot.getValue(QuizModel.class);
                        questionAnswerModel.setQuestionNo(Integer.parseInt(snapshot.getKey()));

                        Log.d("**question "+ questionAnswerModel.getQuestion(), " "+questionAnswerModel.getAnswerOption());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Question_Utility getInstance(){

        return Question_Utility.getArraylistInstance();
    }
}
