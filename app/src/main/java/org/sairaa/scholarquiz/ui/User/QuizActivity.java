package org.sairaa.scholarquiz.ui.User;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.LessonListModel;
import org.sairaa.scholarquiz.model.QuizModel;
import org.sairaa.scholarquiz.model.UserAttemptedModel;
import org.sairaa.scholarquiz.ui.quiz.QuestionActivity;
import org.sairaa.scholarquiz.util.Question_Utility;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class QuizActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ArrayList<UserAttemptedModel> list;

    @BindView(R.id.noquiz_tv)
    TextView NoQuiz_Tv;

    @BindView(R.id.quizlist_lv)
    ListView quizListView;

    String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        channelId = intent.getStringExtra("channelId");

        Toast.makeText(QuizActivity.this,"User Page "+channelId,Toast.LENGTH_SHORT).show();

//        mFirebaseDatabase = FirebaseDatabase.getInstance();

        fetchChannelName(channelId);
        fetchQuizs(channelId);

    }

    private void fetchChannelName(final String channelId){

        AppInfo.databaseReference.child("ChannelList").child(channelId).orderByKey()
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot channelSnapshot) {

                        LessonListModel lessonListModel = channelSnapshot.getValue(LessonListModel.class);

                        String channelName = lessonListModel.getChannelName();

                        getSupportActionBar().setTitle(channelName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @OnItemClick(R.id.quizlist_lv)
    public void onItemClisk(int position){

        UserAttemptedModel lessonQuizModel = list.get(position);

        if(lessonQuizModel.getStatus().equalsIgnoreCase("attempted") ||
                lessonQuizModel.getStatus().equalsIgnoreCase("completed")){

            Toast.makeText(this,"you can't attempt any quiz more than once.", Toast.LENGTH_LONG).show();
            return;
        }

        final String quizId = lessonQuizModel.getQuizId();

        new Thread(){
            @SuppressLint("RestrictedApi")
            @Override
            public void run(){
                AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child("status").setValue("attempted");
                AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child("timeTaken").setValue("0");
                AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child("correctAnswer").setValue("0");

            }
        }.start();

        loadQuizQuestion(quizId);
    }

    private void fetchQuizs(final String channelId){

       list = new ArrayList<>();

        final QuizListAdapter quizListAdapter = new QuizListAdapter(list, QuizActivity.this);
        quizListView.setAdapter(quizListAdapter);

        AppInfo.databaseReference.child("ChannelQuiz").child(channelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    quizListView.setVisibility(View.VISIBLE);
                    NoQuiz_Tv.setVisibility(View.INVISIBLE);
                    final int x=0;
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        final UserAttemptedModel lessonQuizModel = snapshot.getValue(UserAttemptedModel.class);

                        Log.d("****values1: " + snapshot.getKey(), "" + lessonQuizModel.getQuizName());

                        final String quizId = snapshot.getKey();

                        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String status = "", correctAnswer="0", totalQuestions = "0";
                                        if(dataSnapshot.exists()){

                                            Object value = dataSnapshot.getValue();
                                            status = ((HashMap<String, String>) value).get("status");
                                            correctAnswer = ((HashMap<String, String>) value).get("correctAnswer");
                                            totalQuestions = ((HashMap<String, String>) value).get("totalQuestions");
                                        }
                                        Log.d("****values1: " + dataSnapshot.getKey(), "" +status);

                                        lessonQuizModel.setStatus(status);
                                        lessonQuizModel.setQuizId(quizId);
                                        lessonQuizModel.setCorrectQuestions(Integer.parseInt(correctAnswer));
                                        lessonQuizModel.setTotalQuestions(Integer.parseInt(totalQuestions));

                                        list.add(lessonQuizModel);
                                        quizListAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }


                } else {
                    quizListView.setVisibility(View.INVISIBLE);
                    NoQuiz_Tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadQuizQuestion(final String quizId){

        AppInfo.databaseReference.child("Quiz").child(quizId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    int x = 0;
                    getInstance().clearList();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        QuizModel questionAnswerModel = snapshot.getValue(QuizModel.class);
                        questionAnswerModel.setQuestionNo(Integer.parseInt(snapshot.getKey()));

                        getInstance().setList(questionAnswerModel);

                        x++;
                        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child(String.valueOf(x)).setValue("7");

                    }
                }

                AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelId).child(quizId).child("totalQuestions").setValue(String.valueOf(getInstance().getListSize()));

                Intent intent = new Intent(QuizActivity.this, QuestionActivity.class);
                intent.putExtra("channelid", channelId);
                intent.putExtra("quizid", quizId);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchStatus(){

    }

    private Question_Utility getInstance(){
        return Question_Utility.getArraylistInstance();
    }
}


