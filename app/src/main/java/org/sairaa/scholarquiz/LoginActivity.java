package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferenceConfig sharedPreferenceConfig;
    private TextView register;
    private EditText email,password;
    private Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check wheathe the user already logged in or not
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
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
                BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                backgroundLoginTask.execute("login",email.getText().toString(),password.getText().toString());
                break;
            default:
        }
    }
}
