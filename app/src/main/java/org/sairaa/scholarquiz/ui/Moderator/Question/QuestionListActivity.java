package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {

    ListView questionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        questionListView = (ListView)findViewById(R.id.mod_quiz_question_listview);
        ArrayList<QuizModel> questionListModels = new ArrayList<>();

        ModeratorQuestionListAdapter adapter = new ModeratorQuestionListAdapter(QuestionListActivity.this,questionListModels);

        questionListView.setAdapter(adapter);

        questionListModels.add(new QuizModel(1,"How are you ?","Fine","Well","Good","Very Fine",1));

        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String quizId = intent.getStringExtra("quizId");
        String quizName = intent.getStringExtra("quizName");
        Toast.makeText(QuestionListActivity.this,"quiz id : "+quizId+" quiz name : "+quizName,Toast.LENGTH_SHORT).show();
    }
}
