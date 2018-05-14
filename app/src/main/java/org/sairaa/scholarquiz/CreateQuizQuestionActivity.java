package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateQuizQuestionActivity extends AppCompatActivity {

    String question;
    String questionType;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String answerUserInput;

    Boolean answer1Clicked;
    Boolean answer2Clicked;
    Boolean answer3Clicked;
    Boolean answer4Clicked;

    // Variable for Channel Information
    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    String quizListKey;
    String quizTitle;

    private DatabaseReference mQuestionRef;
    private DatabaseReference mDatabase;
    private DatabaseReference mQuizRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz_question);

        // Set Database Reference to Channel List
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Declare variable to get values passed from channelActivity
        Bundle channelBundle = getIntent().getExtras();

        channelId = channelBundle.getString("channelId","Channel ID Default");
        channelName = channelBundle.getString("channelName","Channel Name Default");
        moderatorName = channelBundle.getString("moderatorName","Moderator Name Default");
        moderatorId = channelBundle.getString("moderatorId","Moderator ID Default");
        quizListKey = channelBundle.getString("quizListKey","Quiz List Key");
        quizTitle = channelBundle.getString("quizTitle","Quiz Title");


        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addQuestion();

            }




        });


        findViewById(R.id.button_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quizIntent = new Intent(CreateQuizQuestionActivity.this,QuizListActivity.class);

                quizIntent.putExtra("channelId",channelId);
                quizIntent.putExtra("channelName",channelName);
                quizIntent.putExtra("moderatorName", moderatorName);
                quizIntent.putExtra("moderatorId", moderatorId);
                quizIntent.putExtra("quizListKey",quizListKey);
                quizIntent.putExtra("quizTitle",quizTitle);

                startActivity(quizIntent);
                finish();
            }
        });

    }

    /**
     * Function to Save Question and Answer to Firebase
     *
     * **/

    public void addQuestion() {

        // Question EditText
        EditText questionEditText = findViewById(R.id.question);

        //Answers
        EditText answer1EditText = findViewById(R.id.answer1);
        EditText answer2EditText = findViewById(R.id.answer2);
        EditText answer3EditText = findViewById(R.id.answer3);
        EditText answer4EditText = findViewById(R.id.answer4);
        EditText userInputAnswerEditText = findViewById(R.id.userInputAnswer);

        //Correct Answer Checkbox
        CheckBox answer1CheckBox = findViewById(R.id.checkbox_answer1);
        CheckBox answer2CheckBox = findViewById(R.id.checkbox_answer2);
        CheckBox answer3CheckBox = findViewById(R.id.checkbox_answer3);
        CheckBox answer4CheckBox = findViewById(R.id.checkbox_answer4);

        question = String.valueOf(questionEditText.getText());
        answer1 = String.valueOf(answer1EditText.getText());
        answer2 = String.valueOf(answer2EditText.getText());
        answer3 = String.valueOf(answer3EditText.getText());
        answer4 = String.valueOf(answer4EditText.getText());
        answerUserInput = String.valueOf(userInputAnswerEditText.getText());

        mQuizRef = mDatabase.child("Quiz/" + quizListKey + "/").push();

        String quizKey = String.valueOf(mQuizRef.getKey());

        if (answer1CheckBox.isChecked()) {
            answer1Clicked = true;
        }else {
            answer1Clicked = false;
        }

        if (answer2CheckBox.isChecked()) {
            answer2Clicked = true;
        }else {
            answer2Clicked = false;
        }

        if (answer3CheckBox.isChecked()) {
            answer3Clicked = true;
        }else {
            answer3Clicked = false;
        }

        if (answer4CheckBox.isChecked()) {
            answer4Clicked = true;
        }else {
            answer4Clicked = false;
        }

        Boolean validData = true;

        if (question.isEmpty()) {
            validData = false;
            Toast.makeText(CreateQuizQuestionActivity.this,"Please Enter Question to be added to Quiz!!",Toast.LENGTH_LONG).show();
        }

        if (questionType.equals("UserInput") && answerUserInput.isEmpty()){
            validData = false;
            Toast.makeText(CreateQuizQuestionActivity.this,"Please Enter Answer to the User Input Question be added to Quiz!!",Toast.LENGTH_LONG).show();
        } else if ((questionType.equals("RadioButton") || questionType.equals("CheckBox"))) {

            if (answer1.isEmpty() || answer2.isEmpty() || answer3.isEmpty() || answer4.isEmpty()) {
                validData = false;
                Toast.makeText(CreateQuizQuestionActivity.this,"Please Enter Answer to all 4 options for Question be added to Quiz!!",Toast.LENGTH_LONG).show();
            }

            if(!answer1Clicked && !answer2Clicked && !answer3Clicked && !answer4Clicked) {
                validData = false;
                Toast.makeText(CreateQuizQuestionActivity.this,"Please Select Correct Answer/s option for Question be added to Quiz!!",Toast.LENGTH_LONG).show();
            }

        }

        if (validData) {

            // Save the Question in Questions table in Firebase
            mQuestionRef = mDatabase.child("SQ_Quiz/" + quizListKey + "/" + quizKey);
            mQuestionRef.child("QuizListKey").setValue(quizListKey);
            mQuestionRef.child("ChannelID").setValue(channelId);
            mQuestionRef.child("Question").setValue(question);
            mQuestionRef.child("QuestionType").setValue(questionType);
            mQuestionRef.child("Answer1").setValue(answer1);
            mQuestionRef.child("Answer2").setValue(answer2);
            mQuestionRef.child("Answer3").setValue(answer3);
            mQuestionRef.child("Answer4").setValue(answer4);
            mQuestionRef.child("AnswerUserInput").setValue(answerUserInput);
            mQuestionRef.child("Answer1Flag").setValue(answer1Clicked);
            mQuestionRef.child("Answer2Flag").setValue(answer2Clicked);
            mQuestionRef.child("Answer3Flag").setValue(answer3Clicked);
            mQuestionRef.child("Answer4Flag").setValue(answer4Clicked);

            resetFields();
        }else {
            Toast.makeText(CreateQuizQuestionActivity.this,"Please Verify Option selected for Question to be Added!!",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Function to reset Fields after Data has been saved into Firebase
     * */

    public void resetFields() {

        // Question EditText
        EditText questionEditText = findViewById(R.id.question);

        //Answers
        EditText answer1EditText = findViewById(R.id.answer1);
        EditText answer2EditText = findViewById(R.id.answer2);
        EditText answer3EditText = findViewById(R.id.answer3);
        EditText answer4EditText = findViewById(R.id.answer4);
        EditText userInputAnswerEditText = findViewById(R.id.userInputAnswer);

        //Layouts containing answer options
        LinearLayout optionAnswersLayout = findViewById(R.id.option_answers_layout);
        LinearLayout userinputAnswerLayout = findViewById(R.id.userinput_answer_layout);
        LinearLayout correctAnswerLayout = findViewById(R.id.correct_answer_layout);

        // Question Type Radio Buttons
        RadioButton radioCheckBox = findViewById(R.id.radio_CheckBox);
        RadioButton radioRadioButton = findViewById(R.id.radio_RadioButton);
        RadioButton radioUserInput = findViewById(R.id.radio_UserInput);

        //Correct Answer Checkbox
        CheckBox answer1CheckBox = findViewById(R.id.checkbox_answer1);
        CheckBox answer2CheckBox = findViewById(R.id.checkbox_answer2);
        CheckBox answer3CheckBox = findViewById(R.id.checkbox_answer3);
        CheckBox answer4CheckBox = findViewById(R.id.checkbox_answer4);

        // Reset Question TextField to Empty and set focus on it
        questionEditText.setText("");
        questionEditText.requestFocus();

        // Reset Question Type Radio buttons to blank
        radioCheckBox.setChecked(false);
        radioRadioButton.setChecked(false);
        radioUserInput.setChecked(false);

        // Reset Answer TextFields to Empty
        answer1EditText.setText("");
        answer2EditText.setText("");
        answer3EditText.setText("");
        answer4EditText.setText("");
        userInputAnswerEditText.setText("");

        // Reset Correct Answer Checkboxes to blank
        answer1CheckBox.setChecked(false);
        answer2CheckBox.setChecked(false);
        answer3CheckBox.setChecked(false);
        answer4CheckBox.setChecked(false);

        // Hide the Type of Answers Layout
        optionAnswersLayout.setVisibility(View.INVISIBLE);
        correctAnswerLayout.setVisibility(View.INVISIBLE);
        userinputAnswerLayout.setVisibility(View.INVISIBLE);

    }

    /**
     * Function that is called when a radio button is selected
     * Check which radio button was selected as answer type
     *
     */
    public void onRadioButtonClicked(View view) {

        //Layouts containing answer options
        LinearLayout optionAnswersLayout = findViewById(R.id.option_answers_layout);
        LinearLayout userinputAnswerLayout = findViewById(R.id.userinput_answer_layout);
        LinearLayout correctAnswerLayout = findViewById(R.id.correct_answer_layout);

        switch (view.getId()) {

            case R.id.radio_CheckBox:
                questionType = "CheckBox";
                optionAnswersLayout.setVisibility(View.VISIBLE);
                correctAnswerLayout.setVisibility(View.VISIBLE);
                userinputAnswerLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.radio_RadioButton:
                questionType = "RadioButton";
                optionAnswersLayout.setVisibility(View.VISIBLE);
                correctAnswerLayout.setVisibility(View.VISIBLE);
                userinputAnswerLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.radio_UserInput:
                questionType = "UserInput";
                optionAnswersLayout.setVisibility(View.INVISIBLE);
                correctAnswerLayout.setVisibility(View.INVISIBLE);
                userinputAnswerLayout.setVisibility(View.VISIBLE);
                break;

        }

    }

    /**
     * Function that is called when a Check Box is selected
     * Check which Checkbox was selected as answer
     *
     */

    public void onCheckBoxClicked(View view) {

        switch (view.getId()) {

            case R.id.checkbox_answer1:
                answer1Clicked = true;
                break;
            case R.id.checkbox_answer2:
                answer2Clicked = true;
                break;
            case R.id.checkbox_answer3:
                answer3Clicked = true;
                break;
            case R.id.checkbox_answer4:
                answer4Clicked = true;
                break;
        }

    }


}
