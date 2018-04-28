package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuestionAnswerModel;

public class QuestionAddActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView questionNo;
    private EditText question, option1,option2,option3,option4;
    private Spinner answerSpinner;
    private Button save,exit;
    private String answer;
    private String quizId;
    private String questionNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);

        Intent intent = getIntent();
        questionNumber = intent.getStringExtra("questionNo");
        quizId = intent.getStringExtra("quizId");

        questionNo = findViewById(R.id.mod_add_question_no);
        question = findViewById(R.id.mod_add_question);
        option1 = findViewById(R.id.mod_add_option1);
        option2 = findViewById(R.id.mod_add_option2);
        option3 = findViewById(R.id.mod_add_option3);
        option4 = findViewById(R.id.mod_add_option4);

        save = findViewById(R.id.mod_question_save);
        save.setOnClickListener(this);

        exit = findViewById(R.id.mod_add_exit);
        exit.setOnClickListener(this);



        answerSpinner = findViewById(R.id.mod_answer_spinner);

        answerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                answer = String.valueOf(adapterView.getItemAtPosition(pos));
//                Toast.makeText(QuestionAddActivity.this," an: "+answer,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.mod_correct_answer,android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        answerSpinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mod_question_save:
                // Insert the question answer to firebase
                AppInfo.databaseReference.child("Quiz").child(quizId).child("2").setValue(new QuestionAnswerModel("What is the minimum size",
                        "48sp", "56sp","60sp","16sp",1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(QuestionAddActivity.this,"Inserted quiz",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(QuestionAddActivity.this," an: "+answer,Toast.LENGTH_SHORT).show();
                break;
            case  R.id.mod_add_exit:
                // back to Question List activity
                finish();
                break;
        }
    }
}
