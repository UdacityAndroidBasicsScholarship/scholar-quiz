package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuestionAnswerModel;
import org.sairaa.scholarquiz.model.QuizModel;
import org.sairaa.scholarquiz.ui.Moderator.QuizModeratorActivity;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {


    private Button addNewQuestion, publish;
    private ArrayList<QuizModel> questionListModels;
    private String quizId;
    private ModeratorQuestionListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent = getIntent();
        String channelId = intent.getStringExtra("channelId");
        quizId = intent.getStringExtra("quizId");
        String quizName = intent.getStringExtra("quizName");
//        Toast.makeText(QuestionListActivity.this,"channel Id "+channelId+"quiz id : "+quizId+" quiz name : "+quizName,Toast.LENGTH_SHORT).show();


        addNewQuestion = findViewById(R.id.mod_question_list_add_new_question);
        publish = findViewById(R.id.mod_quiz_publish);
        // setting adapter
        final ListView questionListView = (ListView)findViewById(R.id.mod_quiz_question_listview);
        questionListModels = new ArrayList<>();

//        questionListModels.add(new QuizModel(1,"How are you ?","Fine","Well","Good","Very Fine",1));
//        questionListModels.add(new QuizModel(2,"How are you ?","Fine","Well","Good","Very Fine",1));

        // retriveing question from quiz database structure and add it to List view
//        adapter.clear();
//        addQuestionToList();
        adapter = new ModeratorQuestionListAdapter(QuestionListActivity.this,questionListModels);
        questionListView.setAdapter(adapter);

        //
        addNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionListActivity.this,QuestionAddActivity.class);
                intent.putExtra("questionNo",1);
                intent.putExtra("quizId",quizId);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        addQuestionToList();
        adapter.notifyDataSetChanged();
        Toast.makeText(QuestionListActivity.this,"onResume question",Toast.LENGTH_SHORT).show();
    }

    private void addQuestionToList() {
        AppInfo.databaseReference.child("Quiz").child(quizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot questionListSnapshot : dataSnapshot.getChildren()) {
                    QuizModel qModel = questionListSnapshot.getValue(QuizModel.class);
                    qModel.questionNo = Integer.parseInt(questionListSnapshot.getKey().toString());
//                    Toast.makeText(QuestionListActivity.this,"quiz "+qModel.getQuestionNo()+qModel.getQuestion(),Toast.LENGTH_SHORT).show();
                    questionListModels.add(qModel);
                    adapter.notifyDataSetChanged();

//                    QuestionAnswerModel qModel = questionListSnapshot.getValue(QuestionAnswerModel.class);
//                    Toast.makeText(QuestionListActivity.this,"quiz "+qModel.getQuestion(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
