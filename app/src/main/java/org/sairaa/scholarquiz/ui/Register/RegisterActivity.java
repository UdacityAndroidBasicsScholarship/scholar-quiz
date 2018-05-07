package org.sairaa.scholarquiz.ui.Register;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import org.sairaa.scholarquiz.AppInfo;
import org.sairaa.scholarquiz.ui.Login.LoginActivity;
import org.sairaa.scholarquiz.R;
import org.sairaa.scholarquiz.model.RegisterModel;

import java.text.DateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener, RegisterMVPView{

    private EditText name,emailId,slackId,password,conPasword,info;
    private Button registerB, registerSingIn;
    // Alert dialog
    AlertDialog.Builder alertBuilder;
    ProgressDialog progressDialog;

    RegisterMVPView registerMVPView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name_reg);
        emailId = findViewById(R.id.email_reg);
        slackId = findViewById(R.id.slackid_reg);
        password = findViewById(R.id.password_reg);
        conPasword = findViewById(R.id.confirm_password_reg);
        info = findViewById(R.id.info_reg);
        registerB = findViewById(R.id.register_button);
        registerSingIn = findViewById(R.id.register_sign_in);
        registerSingIn.setOnClickListener(this);
        //set register to onClick event
        registerB.setOnClickListener(this);
        registerMVPView = (RegisterMVPView) this;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_button:

                // Check all requir field empty or not
                //Apply the validation in each field including slack Id
                if (name.getText().toString().length() == 0) {
                    name.setError("Name cannot be blank");
                }
                if (emailId.getText().toString().equals("")) {
                    emailId.setError("Email cannot be blank");
                }
                if (!slackId.getText().toString().contains("@")) {
                    slackId.setError("@ is essential");
                }
                if (password.getText().toString().equals("")) {
                    password.setError("password cannot be blank");
                }
                if (conPasword.getText().toString().equals("")) {
                    conPasword.setError("confirm password cannot be blank");
                    // if any of the required field empty "Show Dialog to fill the required field
                    alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alertBuilder.setTitle("Something Wrong");
                    alertBuilder.setMessage("Please Fill all required field");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                } else if (!(password.getText().toString().equals(conPasword.getText().toString()))) {
                    //check pasword and confirm pasword mismatch
                    alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alertBuilder.setTitle("Something Wrong");
                    alertBuilder.setMessage("Pasword Mismatch");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            password.setText("");
                            conPasword.setText("");
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                } else {
                    // Background task to insert user information into database
                    // BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(RegisterActivity.this);
                    /*backgroundLoginTask.execute("register",name.getText().toString(),
                            emailId.getText().toString(),
                            slackId.getText().toString(),
                            password.getText().toString(),
                            info.getText().toString());*/

                    registerMVPView.showDialog();
                    registerMVPView.authtication(emailId.getText().toString().trim(),
                            password.getText().toString().trim());


                }
                break;
            case R.id.register_sign_in:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            break;

        }

    };

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(RegisterActivity.this, "Login", "Please wait", true, false);
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
        AppInfo.firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String udid = task.getResult().getUser().getUid();
                    AppInfo.firebaseAuth.signOut();
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    RegisterModel registerModel = new RegisterModel(slackId.getText().toString().trim(),
                            name.getText().toString().trim(),currentDateTimeString);
                    registerMVPView.authenticationSucced(udid,registerModel);
                }else {
                    //display some message here
                    registerMVPView.authenticationFailed(task.getException());
                }
            }
        });
    }

    @Override
    public void authenticationSucced(String udid, final RegisterModel model) {

        AppInfo.databaseReference.child("Users").child(udid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    registerMVPView.hideDialog();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                    Toast.makeText(RegisterActivity.this, "login Email: "+model.getName(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

   /* @Override
    public void authtication(String email, String password) {
        AppInfo.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here
//                                    Toast.makeText(RegisterActivity.this, "login", Toast.LENGTH_LONG).show();

                            String udid = task.getResult().getUser().getUid();

                            AppInfo.firebaseAuth.signOut();
                            RegisterModel registerModel = new RegisterModel(slackId.getText().toString().trim(),
                                    name.getText().toString().trim(),
                                    info.getText().toString().trim());

                            registerMVPView.authenticationSucced(udid, registerModel);
//                            Toast.makeText(RegisterActivity.this, udid, Toast.LENGTH_LONG).show();
//                                    finish();
//                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            //display some message here
                            registerMVPView.authenticationFailed(task.getException());
                        }
                    }
                });
    }*/

   /* @Override
    public void authenticationSucced(String udid, RegisterModel model) {

        AppInfo.databaseReference.child("user_info").child(udid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    registerMVPView.hideDialog();
                    finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
    }*/


    @Override
    public void authenticationFailed(Exception e) {

        registerMVPView.hideDialog();
        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}