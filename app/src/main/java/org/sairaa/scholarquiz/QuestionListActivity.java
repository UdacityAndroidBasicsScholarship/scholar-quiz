package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class QuestionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent = getIntent();
        String quizId = intent.getStringExtra("quizId");
        String quizName = intent.getStringExtra("quizName");
        Toast.makeText(QuestionListActivity.this,"quiz id : "+quizId+" quiz name : "+quizName,Toast.LENGTH_SHORT).show();
    }
}
