package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferenceConfig sharedPreferenceConfig;
    private TextView registerTextView;
    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check whether the user already logged in or not
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        if (sharedPreferenceConfig.readLoginStatus()){
            startActivity(new Intent(LoginActivity.this,LessonActivity.class));
            this.finish();
        }

        registerTextView = findViewById(R.id.register);
        registerTextView.setOnClickListener(this);

        emailEditText = findViewById(R.id.email_login);
        passwordEditText = findViewById(R.id.password_login);
        signInButton = findViewById(R.id.signin_login);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class) );
                break;
            case R.id.signin_login:
                BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
                backgroundLoginTask.execute(getString(R.string.LOGIN_METHOD), emailEditText.getText().toString(), passwordEditText.getText().toString());
                break;
            default:
        }
    }
}
