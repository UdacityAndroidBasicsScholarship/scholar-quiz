package org.sairaa.scholarquiz.ui.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.sairaa.scholarquiz.R;

public class QuizActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReferance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        String channelId = intent.getStringExtra("channelId");
        Toast.makeText(QuizActivity.this,"User Page "+channelId,Toast.LENGTH_SHORT).show();

        mFirebaseDatabase = FirebaseDatabase.getInstance();


//        FirebaseDatabase.getInstance().getReference().child("Quiz").push().getKey();
//        String channelKey = FirebaseDatabase.getInstance().getReference().child("ChannelQuiz").push().getKey();

//        mMessageDatabaseReferance = mFirebaseDatabase.getReference().child("Quiz");
//        mMessageDatabaseReferance.push().push().setValue(new QuizModel(1,"How are you ?","Fine","Well","Good","Very Fine",1));

//        Toast.makeText(QuizActivity.this,"Q "+channelKey,Toast.LENGTH_SHORT).show();

//        mMessageDatabaseReferance = mFirebaseDatabase.getReference().child("ChannelQuiz").child(channelId);
//        mMessageDatabaseReferance.push().setValue(new LessonQuizModel("Quiz 1","Praful"));

//        mMessageDatabaseReferance = mFirebaseDatabase.getReference().child("ChannelQuiz").child("-LAYtZvh3d3ECaP7yIdd").child("-LB1ryDQxZKu6WcPcVHe");
//        mMessageDatabaseReferance.setValue(new LessonQuizModel("Quiz 2","Praful Nayak"));

    }
}
