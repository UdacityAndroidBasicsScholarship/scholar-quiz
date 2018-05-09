package org.sairaa.scholarquiz.ui.attemptedquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttemptedQuizActivity extends AppCompatActivity {

    String channelId, quizId;

    @BindView(R.id.listView_userattempted)
    ListView listView;

    AttemptedQuizAdapter attemptedQuizAdapter;
    ArrayList<QuizModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempted_quiz);

        ButterKnife.bind(this);

        channelId = getIntent().getStringExtra("channelId");
        quizId = getIntent().getStringExtra("quizId");

        fetchQuizQuestion();


        attemptedQuizAdapter = new AttemptedQuizAdapter(list, this);
        listView.setAdapter(attemptedQuizAdapter);
    }


    private void fetchQuizQuestion(){

        AppInfo.databaseReference.child("Quiz").child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    int x = 1;
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        QuizModel questionAnswerModel = snapshot.getValue(QuizModel.class);
                        questionAnswerModel.setQuestionNo(Integer.parseInt(snapshot.getKey()));

                        Log.d("**question "+ questionAnswerModel.getQuestion(), " "+questionAnswerModel.getAnswerOption());

                        list.add(questionAnswerModel);
                        fetchUserResponse(x);
                        x++;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String> userAnswerList = new ArrayList<>();




    @SuppressLint("RestrictedApi")
    private void fetchUserResponse(final int quesNo){

        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child(""+quesNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                Log.d("**question position"+quesNo, ""+dataSnapshot);

                    String value = (String) dataSnapshot.getValue();
                    userAnswerList.add(value);
                    Log.d("**question position" + quesNo, "" + value);

                    QuizModel quizModel = list.get(quesNo-1);
                    quizModel.setUserOption(Integer.parseInt(value));

                    list.set(quesNo-1, quizModel);

                    attemptedQuizAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
