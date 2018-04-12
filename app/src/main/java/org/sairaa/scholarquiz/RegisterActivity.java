package org.sairaa.scholarquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText, emailIdEditText, slackIdEditText, passwordEditText, confirmPaswordEditText, infoEditText;
    private Button registerButton;
    // Alert dialog
    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.name_reg);
        emailIdEditText = findViewById(R.id.email_reg);
        slackIdEditText = findViewById(R.id.slackid_reg);
        passwordEditText = findViewById(R.id.password_reg);
        confirmPaswordEditText = findViewById(R.id.confirm_password_reg);
        infoEditText = findViewById(R.id.info_reg);
        registerButton = findViewById(R.id.register_reg);
        //set register to onClick event
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_reg:
                // Check all require field empty or not
                if(nameEditText.getText().toString().equals("")
                        || emailIdEditText.getText().toString().equals("")
                        || slackIdEditText.getText().toString().equals("")
                        || passwordEditText.getText().toString().equals("")
                        || confirmPaswordEditText.getText().toString().equals("")) {
                    // if any of the required field empty "Show Dialog to fill the required field
                    alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alertBuilder.setTitle(R.string.some_thing_wrong_msg);
                    alertBuilder.setMessage(R.string.all_fields_required_msg);
                    alertBuilder.setPositiveButton(R.string.OK_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else if(!(passwordEditText.getText().toString().equals(confirmPaswordEditText.getText().toString()))){
                    //check password and confirm password mismatch
                    alertBuilder = new AlertDialog.Builder(RegisterActivity.this);
                    alertBuilder.setTitle(R.string.some_thing_wrong_msg);
                    alertBuilder.setMessage(R.string.password_mismatch_msg);
                    alertBuilder.setPositiveButton(R.string.OK_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            passwordEditText.setText("");
                            confirmPaswordEditText.setText("");
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }else{
                    // Background task to insert user information into database
                    BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(RegisterActivity.this);
                    backgroundLoginTask.execute(getString(R.string.REGISTER_METHOD), nameEditText.getText().toString(),
                                                emailIdEditText.getText().toString(),
                                                slackIdEditText.getText().toString(),
                                                passwordEditText.getText().toString(),
                                                infoEditText.getText().toString());
                }
                break;
        }
    }
}
