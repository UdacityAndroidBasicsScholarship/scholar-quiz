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
import android.widget.Toast;

import org.sairaa.scholarquiz.ui.Lesson.LessonActivity;
import org.sairaa.scholarquiz.ui.Register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
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

                if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                    backgroundLoginTask.execute("login", email.getText().toString(), password.getText().toString());
                    break;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Email or Password.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.forgotPassword_TextView:


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
                    BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                    backgroundLoginTask.execute("login",email.getText().toString(),password.getText().toString());
                }

                break;
            default:
        }
    }
}