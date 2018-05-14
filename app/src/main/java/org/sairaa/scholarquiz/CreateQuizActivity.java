package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateQuizActivity extends AppCompatActivity {

    private DatabaseReference mQuizRef;
    private DatabaseReference mDatabase;
    private DatabaseReference mChannelListRef;

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    String quizListKey;
    String quizTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Set Database Reference to QuizList
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Declare variable to get values passed from channelActivity
        Bundle channelBundle = getIntent().getExtras();

        channelId = channelBundle.getString("channelId","Channel ID Default");
        channelName = channelBundle.getString("channelName","Channel Name Default");
        moderatorName = channelBundle.getString("moderatorName","Moderator Name Default");
        moderatorId = channelBundle.getString("moderatorId","Moderator ID Default");

        findViewById(R.id.quizTitleSubmit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createQuiz();

            }
        });
    }

    /**
     * Function to Create Quiz in Firebase
     * */

    private void createQuiz() {

        EditText quizTitle_EditText = findViewById(R.id.quizTitle_textview);
        quizTitle = String.valueOf(quizTitle_EditText.getText());

        if (quizTitle.isEmpty()) {
            Toast.makeText(CreateQuizActivity.this,"Please Enter Quiz Name!!", Toast.LENGTH_LONG).show();
        }else {

            // Call function to create Quiz in Firebase
            mQuizRef = mDatabase.child("SQ_QuizList/" + channelId +"/").push();

            quizListKey = String.valueOf(mQuizRef.getKey());

            // Get the values entered by user


            // Save the Users information in Users table in Firebase
            mChannelListRef = mDatabase.child("SQ_QuizList/" + channelId + "/" + quizListKey);
            mChannelListRef.child("Title").setValue(quizTitle);
            mChannelListRef.child("Moderator").setValue(moderatorName);
            mChannelListRef.child("ModeratorID").setValue(moderatorId);
            mChannelListRef.child("ChannelID").setValue(channelId);

            // Call Function to move to Questions activity to insert questions in the Quiz
            moveToCreateQuestionsActivity();
        }

    }

    /**
     * Function to Move the User Create Question Activity
     * */

    public void moveToCreateQuestionsActivity(){

        Intent createQuestionsIntent = new Intent(CreateQuizActivity.this, CreateQuizQuestionActivity.class);
        createQuestionsIntent.putExtra("channelId",channelId);
        createQuestionsIntent.putExtra("channelName",channelName);
        createQuestionsIntent.putExtra("moderatorName",moderatorName);
        createQuestionsIntent.putExtra("moderatorId",moderatorId);

        createQuestionsIntent.putExtra("quizListKey",quizListKey);
        createQuestionsIntent.putExtra("quizTitle",quizTitle);
        startActivity(createQuestionsIntent);

    }

    /**
     * Function to Move User back to Quiz List
     * */
    public void backButton(View view) {

        Intent backIntent = new Intent(CreateQuizActivity.this, QuizListActivity.class);
        backIntent.putExtra("channelId",channelId);
        backIntent.putExtra("channelName",channelName);
        backIntent.putExtra("moderatorName",moderatorName);
        backIntent.putExtra("moderatorId",moderatorId);

        startActivity(backIntent);
    }

}
