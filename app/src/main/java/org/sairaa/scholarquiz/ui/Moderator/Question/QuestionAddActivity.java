package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.sairaa.scholarquiz.ui.Register.RegisterActivity;

public class QuestionAddActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView questionNo;
    private EditText question, option1,option2,option3,option4;
    private Spinner answerSpinner;
    private Button save,exit;
    private String answer;
    private String quizId;
    private int questionNumber;

    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);

        Intent intent = getIntent();
        questionNumber = intent.getIntExtra("questionNo",0);

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

        questionNo.setText(String.valueOf(questionNumber+1));
        Toast.makeText(QuestionAddActivity.this," an: "+questionNumber,Toast.LENGTH_SHORT).show();
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

                //check all the field empty or not
                if(question.getText().toString().trim().length() == 0  && option1.getText().toString().length() == 0 &&
                        option2.getText().toString().length() == 0 && option3.getText().toString().length() == 0 &&
                        option4.getText().toString().length() == 0 ){
                    alertBuilder = new AlertDialog.Builder(QuestionAddActivity.this);
                    alertBuilder.setTitle("Something Wrong");
                    alertBuilder.setMessage("Please Fill all required field");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else{
                    AppInfo.databaseReference.child("Quiz").child(quizId).child(questionNo.getText().toString())
                            .setValue(new QuestionAnswerModel(question.getText().toString().trim(),
                            option1.getText().toString().trim(),
                                    option2.getText().toString().trim(),
                                    option3.getText().toString().trim(),
                                    option4.getText().toString().trim(),
                                    Integer.parseInt(answer))).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(QuestionAddActivity.this,""+answer+" number of question saved",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Toast.makeText(QuestionAddActivity.this," an: "+answer,Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
            case  R.id.mod_add_exit:
                // back to Question List activity
                finish();
                break;

        }
    }
}
