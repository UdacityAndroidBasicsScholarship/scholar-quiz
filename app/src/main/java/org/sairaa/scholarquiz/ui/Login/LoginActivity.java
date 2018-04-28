package org.sairaa.scholarquiz.ui.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.ForgotPasswordActivity;
import org.sairaa.scholarquiz.ui.Lesson.LessonActivity;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.SharedPreferenceConfig;
import org.sairaa.scholarquiz.ui.Register.RegisterActivity;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginActivity extends AppCompatActivity implements LoginMVPView {

    private final String LOG_LOGIN = "LoginActivity";
    private SharedPreferenceConfig sharedPreferenceConfig;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String username, pass;
    private TextView register, forgotpassword_TextView;
    private EditText email, password;
    private Button signIn;
    private AlertDialog.Builder alertBuilder;
    LoginMVPView loginMVPView = this;
    ProgressDialog progressDialog;

    Unbinder unbinder;
    @BindViews({R.id.email_login, R.id.password_login})
    List<EditText> edittexts;


    private boolean checkEmptyField(){

        for(EditText editText: edittexts){

            if(editText.getText().toString().isEmpty())
                return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unbinder = ButterKnife.bind(this);

        // check wheather the user already logged in or not

        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());

        Log.i(LOG_LOGIN, "" + sharedPreferenceConfig.readLoginStatus());

//        if (sharedPreferenceConfig.readLoginStatus()) {
//            startActivity(new Intent(LoginActivity.this, LessonActivity.class));
//            this.finish();
//            Toast.makeText(LoginActivity.this, "sharedPreerence", Toast.LENGTH_SHORT).show();
//        }

        if(AppInfo.firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(LoginActivity.this, LessonActivity.class));
            Toast.makeText(LoginActivity.this, "firebase", Toast.LENGTH_SHORT).show();
        }



        //Use this checkBox ID
        saveLoginCheckBox = findViewById(R.id.rememberMe_CheckBox);
//        signIn = findViewById(R.id.signin_login);
//        signIn.setOnClickListener(this);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        if (saveLogin == true) {
            edittexts.get(0).setText(loginPreferences.getString("username", ""));
            edittexts.get(1).setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
    }


    @OnClick(R.id.register)
    public void registerClickButton(View view){

        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.forgotPassword_TextView)
    public void forgotPasswordTextView (View view) {

        startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
    }

    @OnClick(R.id.signin_login)
    public void signInTextView(View view){

        if(checkEmptyField()){
            ButterKnife.apply(edittexts, EMPTY_FIELD);
        } else {

            loginMVPView.showDialog();
            loginMVPView.authtication(edittexts.get(0).getText().toString(), edittexts.get(1).getText().toString());
        }
    }

    final ButterKnife.Action<View> EMPTY_FIELD = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {

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
        }
    };

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(LoginActivity.this, "Login", "Please wait", true, false);
    }

    @Override
    public void hideDialog() {

        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void authtication(String email, String password) {
        AppInfo.firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loginMVPView.authenticationSucced();
                } else {
                    loginMVPView.authenticationFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void authenticationSucced() {

        loginMVPView.hideDialog();
        finish();
        startActivity(new Intent(LoginActivity.this, LessonActivity.class));
    }

    @Override
    public void authenticationFailed(Exception e) {
        loginMVPView.hideDialog();
        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        loginMVPView.hideDialog();
        unbinder.unbind();

        super.onDestroy();
    }
}

