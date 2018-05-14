package org.sairaa.scholarquiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class QuizActivity extends AppCompatActivity {

    String channelId;
    String channelName;
    String moderatorName;
    String moderatorId;
    String quizListKey;
    String quizTitle;
    String userId;

    // Flags to keep track of which answer button was selected
    boolean answer1Clicked = false;
    boolean answer2Clicked = false;
    boolean answer3Clicked = false;
    boolean answer4Clicked = false;

    // Variable to keep track of current question number on screen
    int currentQuestionNumber = 0;
    int totalQuestions = 0;

    QuestionList questionList = new QuestionList();
    List<QuestionList> quizQuestionList;
    ArrayList<QuestionList> questionArray = new ArrayList<QuestionList>();
    String question;
    String questionType;

    Integer totalCorrectAnswers = 0;
    Integer totalWrongAnswers = 0;
    Integer totalNotAttemptedAnswers = 0;
    Integer score = 0;

    RadioButton answer1RadioButton;
    RadioButton answer2RadioButton;
    RadioButton answer3RadioButton;
    RadioButton answer4RadioButton;

    CheckBox answer1CheckBox;
    CheckBox answer2CheckBox;
    CheckBox answer3CheckBox;
    CheckBox answer4CheckBox;

    Boolean correctAnswer1;
    Boolean correctAnswer2;
    Boolean correctAnswer3;
    Boolean correctAnswer4;

    String msg;
    Boolean quizCompleteFlag = false;
    String userName;
    String userEmail;

    Dialog menuDialog;

    //Database Reference
    private DatabaseReference mDatabase;
    private DatabaseReference mQuizRef;
    private DatabaseReference mScoreRef;
    private DatabaseReference mUserRef;
    private FirebaseUser user;

    StorageReference downloadImageStorageReference;
    FirebaseStorage storage;
    Bitmap bitmap;
    ImageView imageView_UserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        menuDialog = new Dialog(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Declare variable to get values passed from channelActivity
        Bundle channelBundle = getIntent().getExtras();

        channelId = channelBundle.getString("channelId", "Channel ID Default");
        channelName = channelBundle.getString("channelName", "Channel Name Default");
        moderatorName = channelBundle.getString("moderatorName", "Moderator Name Default");
        moderatorId = channelBundle.getString("moderatorId", "Moderator Id Default");
        quizListKey = channelBundle.getString("quizListKey", "Quiz Key");
        quizTitle = channelBundle.getString("quizTitle", "Quiz Title");
        userName = channelBundle.getString("userName", "User Name");

        // Show Message if Network is not Available
        if (!isNetworkAvailable()) {
            Toast.makeText(QuizActivity.this, "To participate in the quiz, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();
        }

        // Set the Title of the Quiz
        TextView quizTitleTextView = findViewById(R.id.textView_QuizTitle);
        quizTitleTextView.setText(quizTitle);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mQuizRef = mDatabase.child("SQ_Quiz/" + quizListKey + "/");

        // Get user information
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = String.valueOf(user.getUid());
        mUserRef = mDatabase.child("SQ_Users").child(String.valueOf(user.getUid()));

        storage = FirebaseStorage.getInstance();
        downloadImageStorageReference = storage.getReferenceFromUrl("gs://scholar-quiz.appspot.com").child("images/").child(userId);

        mUserRef.child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = String.valueOf(dataSnapshot.getValue());
                userEmail = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mQuizRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot quizSnapshot) {

                // If there is no Question
                if (quizSnapshot.getChildrenCount() < 1) {
                    Toast.makeText(QuizActivity.this, "No Quiz Available right now. Please try back later!!", Toast.LENGTH_LONG).show();

                } else {

                    // Loop through all the quiz
                    for (DataSnapshot questionListSnapshot : quizSnapshot.getChildren()) {

                        if (questionListSnapshot.child("QuizListKey").getValue().equals(quizListKey)) {

                            questionList = questionListSnapshot.getValue(QuestionList.class);
                            questionArray.add(questionList);

                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        // When Back Button is Pressed
        findViewById(R.id.button_Back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent quizIntent = new Intent(QuizActivity.this, QuizListActivity.class);

                quizIntent.putExtra("channelId", channelId);
                quizIntent.putExtra("channelName", channelName);
                quizIntent.putExtra("moderatorName", moderatorName);
                quizIntent.putExtra("moderatorId", moderatorId);
                quizIntent.putExtra("quizListKey", quizListKey);
                quizIntent.putExtra("quizTitle", quizTitle);
                quizIntent.putExtra("userName", userName);

                startActivity(quizIntent);
                finish();
            }
        });


    }

    public void buttonStartQuiz(View view) {

        // Make Quiz Buttons Layout Available
        Button buttonNextButton = findViewById(R.id.button_Next);

        // Make Start Quiz Button Invisible
        Button buttonStartQuiz = findViewById(R.id.button_StartQuiz);

        // Make Back Button Invisible
        Button buttonBack = findViewById(R.id.button_Back);

        totalQuestions = questionArray.size();

        if (totalQuestions > 0) {
            buttonNextButton.setVisibility(View.VISIBLE);
            buttonBack.setVisibility(View.INVISIBLE);
            buttonStartQuiz.setVisibility(View.INVISIBLE);

            showFirebaseQuestion();
        } else {
            Toast.makeText(QuizActivity.this, "No Questions Available in Quiz!!", Toast.LENGTH_LONG).show();
        }

    }

    public void showFirebaseQuestion() {

        // Set questions in the Question text view
        TextView radioButtonQuestion = findViewById(R.id.radiobutton_question_textview);
        TextView checkBoxQuestion = findViewById(R.id.checkbox_question_textview);
        TextView userInputQuestion = findViewById(R.id.userinput_question_textview);

        LinearLayout radioButtonLayout = findViewById(R.id.radio_button_layout);
        LinearLayout checkBoxLayout = findViewById(R.id.checkbox_layout);
        LinearLayout userInputLayout = findViewById(R.id.userinput_layout);

        question = questionArray.get(currentQuestionNumber).getQuestion();
        questionType = questionArray.get(currentQuestionNumber).getQuestionType();

        if (questionType.equals("RadioButton")) {

            // Make Radio Button Answers Layout Visible and show the answers option
            radioButtonQuestion.setText(question);

            radioButtonLayout.setVisibility(View.VISIBLE);
            checkBoxLayout.setVisibility(View.INVISIBLE);
            userInputLayout.setVisibility(View.INVISIBLE);

            showRadioButtonAnswers();

        } else if (questionType.equals("CheckBox")) {

            // Make CheckBox Answers Layout Visible and show the answers option
            checkBoxQuestion.setText(question);

            checkBoxLayout.setVisibility(View.VISIBLE);
            radioButtonLayout.setVisibility(View.INVISIBLE);
            userInputLayout.setVisibility(View.INVISIBLE);

            showCheckBoxAnswers();

        } else if (questionType.equals("UserInput")) {

            // Make UserInput Answers Layout Visible and show the answer option
            userInputQuestion.setText(question);

            userInputLayout.setVisibility(View.VISIBLE);
            radioButtonLayout.setVisibility(View.INVISIBLE);
            checkBoxLayout.setVisibility(View.INVISIBLE);

        }

    }

    /**
     * Function to Show Radio Button Answers
     */
    public void showRadioButtonAnswers() {

        // Set answers in answer radio button
        answer1RadioButton = findViewById(R.id.answer1_Radio);
        answer2RadioButton = findViewById(R.id.answer2_Radio);
        answer3RadioButton = findViewById(R.id.answer3_Radio);
        answer4RadioButton = findViewById(R.id.answer4_Radio);

        //Reset the Radio Buttons of Previous Values
        answer1RadioButton.setChecked(false);
        answer2RadioButton.setChecked(false);
        answer3RadioButton.setChecked(false);
        answer4RadioButton.setChecked(false);

        String answer1 = questionArray.get(currentQuestionNumber).getAnswer1();
        String answer2 = questionArray.get(currentQuestionNumber).getAnswer2();
        String answer3 = questionArray.get(currentQuestionNumber).getAnswer3();
        String answer4 = questionArray.get(currentQuestionNumber).getAnswer4();

        answer1RadioButton.setText(answer1);
        answer2RadioButton.setText(answer2);
        answer3RadioButton.setText(answer3);
        answer4RadioButton.setText(answer4);

    }

    /**
     * Function to Show Check Box Answers options
     */
    public void showCheckBoxAnswers() {

        // Set answers in answer radio button
        answer1CheckBox = findViewById(R.id.answer1_checkbox);
        answer2CheckBox = findViewById(R.id.answer2_checkbox);
        answer3CheckBox = findViewById(R.id.answer3_checkbox);
        answer4CheckBox = findViewById(R.id.answer4_checkbox);

        // Reset the Checkboxes of previous values
        answer1CheckBox.setChecked(false);
        answer2CheckBox.setChecked(false);
        answer3CheckBox.setChecked(false);
        answer4CheckBox.setChecked(false);

        String answer1 = questionArray.get(currentQuestionNumber).getAnswer1();
        String answer2 = questionArray.get(currentQuestionNumber).getAnswer2();
        String answer3 = questionArray.get(currentQuestionNumber).getAnswer3();
        String answer4 = questionArray.get(currentQuestionNumber).getAnswer4();

        answer1CheckBox.setText(answer1);
        answer2CheckBox.setText(answer2);
        answer3CheckBox.setText(answer3);
        answer4CheckBox.setText(answer4);

    }

    /**
     * Function that is called when a radio button is selected
     * Check which radio button was selected as answer
     * - Make its flag as true and other answer flag as false
     */

    public void onRadioButtonClicked(View view) {

        switch (view.getId()) {

            case R.id.answer1_Radio:
                answer1Clicked = true;

                answer2Clicked = false;
                answer3Clicked = false;
                answer4Clicked = false;
                break;
            case R.id.answer2_Radio:
                answer2Clicked = true;

                answer1Clicked = false;
                answer3Clicked = false;
                answer4Clicked = false;
                break;
            case R.id.answer3_Radio:
                answer3Clicked = true;

                answer2Clicked = false;
                answer1Clicked = false;
                answer4Clicked = false;
                break;
            case R.id.answer4_Radio:
                answer4Clicked = true;

                answer1Clicked = false;
                answer2Clicked = false;
                answer3Clicked = false;
                break;

        }

    }

    /**
     * Function that is called when a Check Box is selected
     * Check which Checkbox was selected as answer
     * - Make its flag as true and other answer flag as false
     */

    public void onCheckBoxClicked(View view) {

        switch (view.getId()) {

            case R.id.answer1_checkbox:
                answer1Clicked = true;
                break;
            case R.id.answer2_checkbox:
                answer2Clicked = true;
                break;
            case R.id.answer3_checkbox:
                answer3Clicked = true;
                break;
            case R.id.answer4_checkbox:
                answer4Clicked = true;
                break;
        }

    }

    public void updateScore() {

        correctAnswer1 = Boolean.valueOf(questionArray.get(currentQuestionNumber).getAnswer1Flag());
        correctAnswer2 = Boolean.valueOf(questionArray.get(currentQuestionNumber).getAnswer2Flag());
        correctAnswer3 = Boolean.valueOf(questionArray.get(currentQuestionNumber).getAnswer3Flag());
        correctAnswer4 = Boolean.valueOf(questionArray.get(currentQuestionNumber).getAnswer4Flag());

        if (questionType.equals("RadioButton")) {

            if (answer1Clicked && correctAnswer1) {
                totalCorrectAnswers++;
            } else if (answer2Clicked && correctAnswer2) {
                totalCorrectAnswers++;
            } else if (answer3Clicked && correctAnswer3) {
                totalCorrectAnswers++;
            } else if (answer4Clicked && correctAnswer4) {
                totalCorrectAnswers++;
            }

            // Count Unanswered Questions
            if (!answer1Clicked && !answer2Clicked && !answer3Clicked && !answer4Clicked) {
                totalNotAttemptedAnswers++;
            }

            score = totalCorrectAnswers * 10;

        } else if (questionType.equals("CheckBox")) {

            int wrongFlag = 0;
            Boolean answerClickedFlag = false;

            if (wrongFlag == 0) {

                if (correctAnswer1) {
                    if (answer1Clicked) {
                        answerClickedFlag = true;
                    } else {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                } else {
                    if (answer1Clicked) {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                }

            }

            if (wrongFlag == 0) {

                if (correctAnswer2) {
                    if (answer2Clicked) {
                        answerClickedFlag = true;
                    } else {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                } else {
                    if (answer2Clicked) {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                }

            }

            if (wrongFlag == 0) {

                if (correctAnswer3) {
                    if (answer3Clicked) {
                        answerClickedFlag = true;
                    } else {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                } else {
                    if (answer3Clicked) {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                }

            }

            if (wrongFlag == 0) {

                if (correctAnswer4) {
                    if (answer4Clicked) {
                        answerClickedFlag = true;
                    } else {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                } else {
                    if (answer4Clicked) {
                        answerClickedFlag = false;
                        wrongFlag = 1;
                    }
                }

            }

            if (answerClickedFlag && wrongFlag == 0) {
                totalCorrectAnswers++;
                answerClickedFlag = false;
            }

            // Count Unanswered Questions
            if (!answer1Clicked && !answer2Clicked && !answer3Clicked && !answer4Clicked) {
                totalNotAttemptedAnswers++;
            }

            score = totalCorrectAnswers * 10;


        } else if (questionType.equals("UserInput")) {

            EditText userAnswerEditText = findViewById(R.id.answer_edittext);
            String userAnswer = String.valueOf(userAnswerEditText.getText()).toUpperCase();

            String correctUserAnswer = questionArray.get(currentQuestionNumber).getAnswerUserInput().toUpperCase();

            if (userAnswer.isEmpty()) {
                totalNotAttemptedAnswers++;
            } else if (userAnswer.equals(correctUserAnswer)) {
                totalCorrectAnswers++;
            } else {
                totalWrongAnswers++;
            }

            score = totalCorrectAnswers * 10;
        }

    }

    /**
     * When Next button is clicked
     */
    public void buttonNext(View view) {


        if (currentQuestionNumber < (totalQuestions - 1)) {
            updateScore();
            currentQuestionNumber++;

            correctAnswer1 = false;
            correctAnswer2 = false;
            correctAnswer3 = false;
            correctAnswer4 = false;

            answer1Clicked = false;
            answer2Clicked = false;
            answer3Clicked = false;
            answer4Clicked = false;

            showFirebaseQuestion();
        } else {

            updateScore();

            quizCompleteFlag = true;

            correctAnswer1 = false;
            correctAnswer2 = false;
            correctAnswer3 = false;
            correctAnswer4 = false;

            answer1Clicked = false;
            answer2Clicked = false;
            answer3Clicked = false;
            answer4Clicked = false;
        }

        if (quizCompleteFlag) {

            // Save the score to Firebase
            totalWrongAnswers = totalQuestions - (totalCorrectAnswers + totalNotAttemptedAnswers);
            saveScoreToFirebase();

            // Move the user to Score Screen
            Intent quizScoreIntent = new Intent(QuizActivity.this, QuizScoreActivity.class);
            quizScoreIntent.putExtra("channelId", channelId);
            quizScoreIntent.putExtra("channelName", channelName);
            quizScoreIntent.putExtra("moderatorName", moderatorName);
            quizScoreIntent.putExtra("moderatorId", moderatorId);
            quizScoreIntent.putExtra("quizListKey", quizListKey);
            quizScoreIntent.putExtra("quizTitle", quizTitle);
            quizScoreIntent.putExtra("totalQuestions", String.valueOf(totalQuestions));
            quizScoreIntent.putExtra("correctAnswers", String.valueOf(totalCorrectAnswers));
            quizScoreIntent.putExtra("totalNotAttemptedAnswers", String.valueOf(totalNotAttemptedAnswers));
            quizScoreIntent.putExtra("score", String.valueOf(score));
            quizScoreIntent.putExtra("userName", String.valueOf(userName));
            quizScoreIntent.putExtra("userEmail", String.valueOf(userEmail));

            startActivity(quizScoreIntent);

        }

    }

    /**
     * Function to Save Score to Firebase
     */

    public void saveScoreToFirebase() {

        mScoreRef = mDatabase.child("SQ_Score/").child(channelId).child(quizListKey).child(user.getUid());

        // Save the Users information in Users table in Firebase
        //mScoreRef = mDatabase.child("QuizList/"+ quizListKey);
        mScoreRef.child("Score").setValue(score);
        mScoreRef.child("Correct").setValue(totalCorrectAnswers);
        mScoreRef.child("NotAttempted").setValue(totalNotAttemptedAnswers);
        mScoreRef.child("Wrong").setValue(totalWrongAnswers);
        mScoreRef.child("TotalQuestions").setValue(totalQuestions);

    }

    /**
     * Menu Functions
     * */

    /**
     * 1. Function to Show Popup Menu
     * */
    public void showPopUp(View view) {

        menuDialog.setContentView(R.layout.menupopup);
        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txtUserName = (TextView) menuDialog.getWindow().findViewById(R.id.textview_UserName);
        txtUserName.setText(userName);

        // Download User Image from Firebase and show it to User.
        final long ONE_MEGABYTE = 1024 * 1024;
        downloadImageStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView_UserPhoto.setImageBitmap(bitmap);

            }
        });

        imageView_UserPhoto = menuDialog.getWindow().findViewById(R.id.imageview_UserImage);

        if(bitmap != null) {
            imageView_UserPhoto.setImageBitmap(bitmap);
        }else {
            imageView_UserPhoto.setImageResource(R.drawable.userimage_default);
        }


        menuDialog.show();
    }

    /**
     * 2. Function to close Popup Menu
     * */
    public void closeMenu(View view) {
        menuDialog.dismiss();
    }

    /**
     * 3. Function to Show logout user
     * */

    public void logout(View view) {
        if (isNetworkAvailable()) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(QuizActivity.this, SignInActivity.class));
            finish();
        } else {

            Toast.makeText(QuizActivity.this, "To Log Out, Please Connect your Phone to Internet..", Toast.LENGTH_SHORT).show();

        }
    }


    public void editProfilePressed(View view) {

        Intent editProfileIntent = new Intent(QuizActivity.this, UserProfileActivity.class);
        startActivity(editProfileIntent);

        finish();

    }

    /**
     * 4. Function to execute when user presses MyChannel
     * */

    public void myChannelPressed(View view) {

        Intent myChannelIntent = new Intent(QuizActivity.this, UserChannelActivity.class);

        myChannelIntent.putExtra("userName", userName);
        startActivity(myChannelIntent);

        finish();
    }

    /**
     * 5. Function to execute when user presses AllChannel
     * */

    public void allChannelPressed(View view) {

        Intent allChannelIntent = new Intent(QuizActivity.this, AllChannelListActivity.class);

        allChannelIntent.putExtra("userName", userName);
        startActivity(allChannelIntent);

        finish();
    }

    /**
     * 6. Function to execute when user presses MyScorecard
     * */

    public void myScorecardPressed(View view) {

        Intent myScorecardIntent = new Intent(QuizActivity.this, MyScorecardChannelActivity.class);

        myScorecardIntent.putExtra("userName", userName);
        startActivity(myScorecardIntent);

        finish();
    }

    /**
     * 7. Function to execute when user presses Leaderboard
     * */

    public void leaderboardPressed(View view) {

        Intent myLeaderboardIntent = new Intent(QuizActivity.this, LeaderboardChannelActivity.class);

        myLeaderboardIntent.putExtra("userName", userName);
        startActivity(myLeaderboardIntent);

        finish();
    }

    /**
     * Function to check if Device is connected to Internet
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void homeButton(View view){
        Intent homeIntent = new Intent(QuizActivity.this, HomeActivity.class);
        homeIntent.putExtra("userName", userName);
        startActivity(homeIntent);
    }

}
