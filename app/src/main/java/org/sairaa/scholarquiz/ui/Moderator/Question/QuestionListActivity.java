package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.LessonQuizModel;
import org.sairaa.scholarquiz.model.QuizModel;

import java.util.ArrayList;

public class QuestionListActivity extends AppCompatActivity {


    private Button addNewQuestion, publish;
    private ArrayList<QuizModel> questionListModels;
    private String quizId;
    private ModeratorQuestionListAdapter adapter;
    private int questionNo = 0;

    AlertDialog.Builder alertBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent intent = getIntent();
        final String channelId = intent.getStringExtra("channelId");
        quizId = intent.getStringExtra("quizId");
        final String quizName = intent.getStringExtra("quizName");
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
//                intent.putExtra("channelId",channelId);
                intent.putExtra("quizId",quizId);
//                intent.putExtra("quizName",quizName);
                intent.putExtra("questionNo",questionNo);
//                Toast.makeText(QuestionListActivity.this," llll"+questionNo,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertBuilder = new AlertDialog.Builder(QuestionListActivity.this);
                alertBuilder.setTitle("Publishing Quiz");
                alertBuilder.setMessage("Do you really want to bublish the quiz for now");
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        Toast.makeText(QuestionListActivity.this," Yes"+channelId+":"+quizId+" : "+quizName+" : "+user.getUid().toString(),Toast.LENGTH_SHORT).show();
                        AppInfo.databaseReference.child("ChannelQuiz")
                                .child(channelId).child(quizId)
                                .setValue(new LessonQuizModel(quizName,user.getUid().toString()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(QuestionListActivity.this,"Quiz Published Succesfully",Toast.LENGTH_SHORT).show();
                                    adapter.clear();
                                }
                                else {
                                    Toast.makeText(QuestionListActivity.this,"Quiz Not Published",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialogInterface.dismiss();
                    }
                });
                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(QuestionListActivity.this," NO",Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.show();
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
        AppInfo.databaseReference.child("ChannelQuiz").child(quizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Log.d("****values: ", "" + dataSnapshot);
                    for (final DataSnapshot questionListSnapshot : dataSnapshot.getChildren()) {

                        LessonQuizModel lessonQuizModel = questionListSnapshot.getValue(LessonQuizModel.class);

                        Log.d("****values: " + questionListSnapshot.getKey(), "" + lessonQuizModel.getQuizName());

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
