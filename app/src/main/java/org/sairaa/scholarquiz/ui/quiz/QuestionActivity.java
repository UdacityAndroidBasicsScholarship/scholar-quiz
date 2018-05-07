package org.sairaa.scholarquiz.ui.quiz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.QuizModel;
import org.sairaa.scholarquiz.model.UserQuizAttemptModel;
import org.sairaa.scholarquiz.ui.User.QuizActivity;
import org.sairaa.scholarquiz.util.Question_Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<QuizModel> allQuestion = Question_Utility.getArraylistInstance().getList();

    ArrayList<UserQuizAttemptModel> userAttemptedList = new ArrayList<>();

    int i = 0, answer = 0, actualAnswer;

    String channelid, quizid;

    @BindView(R.id.tv_timer_question)
    ProgressBar progressBar;

    CountDownTimer countDownTimer;
    long timeDuraton;
    long totalTimeForQuiz = 100;
    int totalCorrectAnswer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        ButterKnife.bind(this);

        loadQuestionToUser(i++);

        Intent intent = getIntent();

        channelid = intent.getStringExtra("channelid");
        quizid = intent.getStringExtra("quizid");

        countDownTimer = new CountDownTimer(totalTimeForQuiz * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                timeDuraton = (millisUntilFinished) / 1000;
                long seconds = timeDuraton % 60;
                long barVal = (100 * timeDuraton) / totalTimeForQuiz;
                progressBar.setProgress((int) (barVal));

            }

            public void onFinish() {

                timeCompleteInterface.timeComplete();
            }

        };

        countDownTimer.start();

    }

    @BindView(R.id.tv_question)
    TextView question_TextView;

    @BindViews({R.id.option1_tv, R.id.option2_tv, R.id.option3_tv, R.id.option4_tv})
    List<TextView> listTextView;

    @SuppressLint("RestrictedApi")
    @OnClick(R.id.tv_next_question)
    public void onNextClick(){

        if(answer == 0){
            Toast.makeText(this,"Please select an option.", Toast.LENGTH_LONG).show();
            return;
        }

        if(allQuestion.size() > i){
            answer = 0;
            loadQuestionToUser(i);

            i++;
        } else {

            Toast.makeText(this, "nomore question left", Toast.LENGTH_LONG).show();

            AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelid).child(quizid).child("status").setValue("completed");
            AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelid).child(quizid).child("timeTaken").setValue(String.valueOf(totalTimeForQuiz - timeDuraton));

            quizOverDialog();
        }
    }

    private void loadQuestionToUser(int quesindex){

        QuizModel quizModel = allQuestion.get(quesindex);

        question_TextView.setText(quizModel.getQuestion());

        listTextView.get(0).setText(quizModel.getOption1());
        listTextView.get(0).setBackgroundColor(Color.WHITE);

        listTextView.get(1).setText(quizModel.getOption2());
        listTextView.get(1).setBackgroundColor(Color.WHITE);

        listTextView.get(2).setText(quizModel.getOption3());
        listTextView.get(2).setBackgroundColor(Color.WHITE);

        listTextView.get(3).setText(quizModel.getOption4());
        listTextView.get(3).setBackgroundColor(Color.WHITE);

        actualAnswer = quizModel.getAnswerOption();
    }


    @SuppressLint("RestrictedApi")
    @OnClick({R.id.option1_tv, R.id.option2_tv, R.id.option3_tv, R.id.option4_tv})
    public void onClick(View view){

        switch (view.getId()){

            case R.id.option1_tv:

                if(answer != 0){
                  Toast.makeText(this, "you can't reselect the answer.", Toast.LENGTH_LONG).show();
                } else
                    hightlightAnswer(1);

                /*answer = 1;
                Toast.makeText(this, "option1", Toast.LENGTH_LONG).show();

                if(actualAnswer == 1){
                    listTextView.get(0).setBackgroundColor(Color.GREEN);
                    totalCorrectAnswer++;
                    increaseCountOnFirebase(totalCorrectAnswer);

                } else
                    listTextView.get(0).setBackgroundColor(Color.RED);*/

                break;

            case R.id.option2_tv:
                if(answer != 0){
                    Toast.makeText(this, "you can't reselect the answer.", Toast.LENGTH_LONG).show();
                } else
                    hightlightAnswer(2);



                /*answer = 2;
                if(actualAnswer == 2){
                    listTextView.get(0).setBackgroundColor(Color.GREEN);
                    totalCorrectAnswer++;
                    increaseCountOnFirebase(totalCorrectAnswer);
                } else
                    listTextView.get(0).setBackgroundColor(Color.RED);
*/
                break;

            case R.id.option3_tv:

                if(answer != 0){
                    Toast.makeText(this, "you can't reselect the answer.", Toast.LENGTH_LONG).show();
                } else
                    hightlightAnswer(3);

//                answer = 3;
//
//                if(actualAnswer == 3){
//                    listTextView.get(0).setBackgroundColor(Color.GREEN);
//                    totalCorrectAnswer++;
//                    increaseCountOnFirebase(totalCorrectAnswer);
//                } else
//                    listTextView.get(0).setBackgroundColor(Color.RED);

                break;

            case R.id.option4_tv:

//                answer = 4;
                if(answer != 0){
                    Toast.makeText(this, "you can't reselect the answer.", Toast.LENGTH_LONG).show();
                } else
                    hightlightAnswer(4);


//                if(actualAnswer == 4){
//                    listTextView.get(0).setBackgroundColor(Color.GREEN);
//                    totalCorrectAnswer++;
//                    increaseCountOnFirebase(totalCorrectAnswer);
//
//                } else
//                    listTextView.get(0).setBackgroundColor(Color.RED);
//
                break;

        }

        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid())
                .child(channelid).child(quizid).child(String.valueOf(i)).setValue(String.valueOf(answer));

    }

    TimeCompleteInterface timeCompleteInterface = new TimeCompleteInterface() {
        @Override
        public void timeComplete() {

            buildDialog();
        }

        @Override
        public void onError() {

        }
    };

    private void hightlightAnswer(int userOption){

        answer = userOption;

        if(actualAnswer == userOption) {
            listTextView.get(actualAnswer - 1).setBackgroundColor(Color.GREEN);
            totalCorrectAnswer++;
            increaseCountOnFirebase(totalCorrectAnswer);
        }else {
            listTextView.get(actualAnswer-1).setBackgroundColor(Color.GREEN);
            listTextView.get(userOption-1).setBackgroundColor(Color.RED);
        }
    }

    AlertDialog.Builder builder;

    private void buildDialog(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Time's up")
                .setMessage("Time for this quiz has been completed.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelid).child(quizid).child("status").setValue("completed");
                        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelid).child(quizid).child("timeTaken").setValue(String.valueOf(totalTimeForQuiz));

                        dialog.dismiss();
                        Intent intent = new Intent(QuestionActivity.this, QuizActivity.class);
                        intent.putExtra("channelId", channelid);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                }).show();
    }

    @SuppressLint("RestrictedApi")
    private void increaseCountOnFirebase(int count){

        AppInfo.databaseReference.child("Result").child(AppInfo.firebaseAuth.getUid()).child(channelid).child(quizid).child("correctAnswer").setValue(String.valueOf(count));

    }

    private void quizOverDialog(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Question finished")
                .setMessage("All the quiestion has been attempted successfully.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        Intent intent = new Intent(QuestionActivity.this, QuizActivity.class);
                        intent.putExtra("channelId", channelid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                }).show();
    }
}
