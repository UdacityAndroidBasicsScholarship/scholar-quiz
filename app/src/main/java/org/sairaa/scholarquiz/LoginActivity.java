package org.sairaa.scholarquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.sairaa.scholarquiz.Other.ActivityConstants;
import org.sairaa.scholarquiz.Other.GeneralActions;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,BackgroundLoginTask.AsyncData{
    private final String LOG_LOGIN = "LoginActivity";
    private SharedPreferenceConfig sharedPreferenceConfig;
    private TextView register;
    private EditText email,password;
    private Button signIn;
    private AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityConstants.initiateConstants(this); // initiating constants defined
        // check wheathe the user already logged in or not
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        Log.i(LOG_LOGIN,""+sharedPreferenceConfig.readLoginStatus());
        if (sharedPreferenceConfig.readLoginStatus()){
            startActivity(new Intent(LoginActivity.this,LessonActivity.class));
            this.finish();
        }

        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        signIn = findViewById(R.id.signin_login);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class) );
                break;
            case R.id.signin_login:

                if(email.getText().toString().equals("")
                        || password.getText().equals("")){
                    alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertBuilder.setTitle("User Datails");
                    alertBuilder.setMessage("Please Fill all required field");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else {
                   // BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                    //backgroundLoginTask.execute("login",email.getText().toString(),password.getText().toString());

                    ActivityConstants.URLInfo urlInfo = ActivityConstants.getURLInfoCopy(ActivityConstants.urlList.get(0)); // for login
                    ArrayList<ActivityConstants.ServiceCallObj> parameters = new ArrayList<>();
                    parameters.add(new ActivityConstants.ServiceCallObj("mail_id",email.getText().toString()));
                    parameters.add(new ActivityConstants.ServiceCallObj("password",password.getText().toString()));
                    BackgroundLoginTask request = new BackgroundLoginTask(this,urlInfo,parameters);
                    ActivityConstants.callDataRequest(request);
                }

                break;
            default:
        }
    }

    @Override
    public void onDataReceive(JSONObject jsonObject) {
        String code = null;
        try {
            code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            if(code.equals("login_true")){
                Intent intent = new Intent(this,LessonActivity.class);
                intent.putExtra("message",message);
                startActivity(intent);
                sharedPreferenceConfig = new SharedPreferenceConfig(this);
                sharedPreferenceConfig.writeLoginStatus(true);
                finish();
            }else if(code.equals("login_false")){
               // showDialog("Error in Login",message,code);
                GeneralActions.showDialog(this,"Error in Login", message, "OK", new ActivityConstants.SuccessCallBacks() {
                    @Override
                    public void onSuccess() {
                        email.setText("");
                        password.setText("");
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
