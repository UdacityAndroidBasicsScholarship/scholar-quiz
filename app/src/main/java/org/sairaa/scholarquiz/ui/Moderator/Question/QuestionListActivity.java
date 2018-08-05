package org.sairaa.scholarquiz.ui.Moderator.Question;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.SharedPreferenceConfig;
import org.sairaa.scholarquiz.model.LessonQuizModel;
import org.sairaa.scholarquiz.model.QuestionAnswerModel;
import org.sairaa.scholarquiz.model.QuizModel;
import org.sairaa.scholarquiz.ui.Lesson.LessonActivity;
import org.sairaa.scholarquiz.ui.Moderator.QuizModeratorActivity;
import org.sairaa.scholarquiz.util.CheckConnection;
import org.sairaa.scholarquiz.util.DialogAction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class
QuestionListActivity extends AppCompatActivity {


    private static final String LOG_QUESTION_LIST = "QuestinListActivity";
    private Button addNewQuestion, publish;
    private ArrayList<QuizModel> questionListModels;
    private String quizId;
    private ModeratorQuestionListAdapter adapter;
    private int questionNo = 0;

    private SharedPreferenceConfig sharedPreferenceConfig;
    private final String NOTIFYURL = "http://sairaa.org/ScholarQuizApp/trialindex.php";
    AlertDialog.Builder alertBuilder;

    public CheckConnection connection;
    public DialogAction dialogAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        connection = new CheckConnection(QuestionListActivity.this);
        dialogAction = new DialogAction(QuestionListActivity.this);

        Intent intent = getIntent();
        // the read write option to check whether the buttons AddNewQuiz and Publish
        // to remain active or not
        // 0 to write operation
        int readWrite = intent.getIntExtra("readWrite",0);
        final String channelId = intent.getStringExtra("channelId");
        final String channelName = intent.getStringExtra("channelName");
        quizId = intent.getStringExtra("quizId");
        final String quizName = intent.getStringExtra("quizName");
        Toast.makeText(QuestionListActivity.this,"channel Id "+channelName+"quiz id : "+quizId+" quiz name : "+quizName,Toast.LENGTH_SHORT).show();

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());


        addNewQuestion = findViewById(R.id.mod_question_list_add_new_question);
        publish = findViewById(R.id.mod_quiz_publish);

        if(readWrite == 200){
            // inactive the buttons
            // no edit or not allowed to add new question to published quiz
            addNewQuestion.setVisibility(View.INVISIBLE);
            publish.setVisibility(View.INVISIBLE);
        }else{
            addNewQuestion.setVisibility(View.VISIBLE);
            publish.setVisibility(View.VISIBLE);
        }


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

        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Check readWrite or Only Read
                if(addNewQuestion.getVisibility() == View.VISIBLE){
                    // Moderator can edit the question
                    QuizModel questionAnswer = (QuizModel)questionListView.getItemAtPosition(position);
                    Intent intent = new Intent(QuestionListActivity.this,QuestionAddActivity.class);
                    intent.putExtra("edit",111);
                    intent.putExtra("questionNo",questionAnswer.getQuestionNo());
                    intent.putExtra("question",questionAnswer.getQuestion());
                    intent.putExtra("option1",questionAnswer.getOption1());
                    intent.putExtra("option2",questionAnswer.getOption2());
                    intent.putExtra("option3",questionAnswer.getOption3());
                    intent.putExtra("option4",questionAnswer.getOption4());
                    intent.putExtra("answerOption",questionAnswer.getAnswerOption());
                    intent.putExtra("quizId",quizId);
                    startActivity(intent);
                }else{
                    Toast.makeText(QuestionListActivity.this,"Already Published Can't edited",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
        addNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionListActivity.this,QuestionAddActivity.class);
//                intent.putExtra("channelId",channelId);
                intent.putExtra("edit",222);
                intent.putExtra("quizId",quizId);
//                intent.putExtra("quizName",quizName);
                intent.putExtra("questionNo",questionNo+1);
//                Toast.makeText(QuestionListActivity.this," llll"+questionNo,Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!questionListModels.isEmpty()){
                    alertBuilder = new AlertDialog.Builder(QuestionListActivity.this);
                    alertBuilder.setTitle("Publishing Quiz");
                    alertBuilder.setMessage("Do you really want to bublish the quiz for now");
                    alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        Toast.makeText(QuestionListActivity.this," Yes"+channelId+":"+quizId+" : "+quizName+" : "+user.getUid().toString(),Toast.LENGTH_SHORT).show();
                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                            AppInfo.databaseReference.child("ChannelQuiz")
                                    .child(channelId)
                                    .child(quizId)
                                    .setValue(new LessonQuizModel(quizName,currentDateTimeString))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(QuestionListActivity.this,"Quiz Published Succesfully",Toast.LENGTH_SHORT).show();
                                                adapter.clear();
                                                sharedPreferenceConfig.writePublishedOrNot(true);
                                                sharedPreferenceConfig.writeNewQuizName(null);
                                                if(connection.isConnected()){
                                                    new backgroundTask(this).execute(quizName, channelName,channelId);
                                                }

                                                finish();
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
//                            Toast.makeText(QuestionListActivity.this," NO",Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(QuestionListActivity.this," No Question Added. Can not be published",Toast.LENGTH_SHORT).show();
                }


            }
        });




    }

    private void notifySubscriber(String quizName, String channelName, String channelId) throws IOException {
//        Toast.makeText(QuestionListActivity.this,"Quiz Published Succesfully"+quizName+channelName,Toast.LENGTH_SHORT).show();
        URL url = createUrl(NOTIFYURL);
        String jsonResponse = null;
        jsonResponse = makeHttpRequest(url,quizName,channelName,channelId);
        Log.e(LOG_QUESTION_LIST,jsonResponse);
    }

    private String makeHttpRequest(URL url, String quizName, String channelName, String channelId) throws IOException {
        String jsonResponse = "";
        Log.i(LOG_QUESTION_LIST,"Test : makeHttp( ) is called");
        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(60000 /* milliseconds */);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            // This findLastRecord() method finds last record of SQLite database table
            // @param context is passed
//            String channelName = channelName ;
            String post_data = URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode("A New Quiz ( "+quizName+" )"+
                    "published in channel ( "+channelName+" )","UTF-8");
            post_data += "&" + URLEncoder.encode("channelId", "UTF-8") + "="
                    + URLEncoder.encode(channelId, "UTF-8");
//                    URLEncoder.encode("quizName","UTF-8")+"="+URLEncoder.encode(quizName,"UTF-8")+
//                    URLEncoder.encode("channelId","UTF-8")+"="+URLEncoder.encode(channelId,"UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_QUESTION_LIST, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_QUESTION_LIST, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private URL createUrl(String urlNews) {
        URL url2 = null;
        try{
            url2 = new URL(urlNews);
        }catch (MalformedURLException e) {
            Log.e("QUESTIONlISTaCTIVITY : ", "Error with creating URL ", e);
        }
        return url2;
    }

    @Override
    protected void onDestroy() {
        dialogAction.hideDialog();
        super.onDestroy();
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
        if(connection.isConnected()){
            dialogAction.showDialog("","Fatching Questions");
            addQuestionToList();
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(QuestionListActivity.this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

//        Toast.makeText(QuestionListActivity.this,"onResume question",Toast.LENGTH_SHORT).show();
    }

    private void addQuestionToList() {
        AppInfo.databaseReference.child("Quiz").child(quizId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot questionListSnapshot : dataSnapshot.getChildren()) {
                    QuizModel qModel = questionListSnapshot.getValue(QuizModel.class);
                    qModel.questionNo = Integer.parseInt(questionListSnapshot.getKey().toString());
                    //question number is global and passed to the questionAddActivity to know the question no
                    questionNo = qModel.questionNo;
//                    Toast.makeText(QuestionListActivity.this,"quiz "+qModel.getQuestionNo()+qModel.getQuestion(),Toast.LENGTH_SHORT).show();
                    questionListModels.add(qModel);
                    adapter.notifyDataSetChanged();

//                    QuestionAnswerModel qModel = questionListSnapshot.getValue(QuestionAnswerModel.class);
//                    Toast.makeText(QuestionListActivity.this,"quiz "+qModel.getQuestion(),Toast.LENGTH_SHORT).show();
                }
                dialogAction.hideDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class backgroundTask extends AsyncTask<String, Void, String>{
        public backgroundTask(OnCompleteListener<Void> onCompleteListener) {
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                notifySubscriber(params[0], params[1],params[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(LOG_QUESTION_LIST,params[0]+params[1]+params[2]);
            return null;
        }
    }
}
