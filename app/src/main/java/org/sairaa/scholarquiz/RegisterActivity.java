package org.sairaa.scholarquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name,emailId,slackId,password,conPasword,info;
    private Button registerB;
    // Alert dialog
    AlertDialog.Builder alertBuilder;
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
        registerB = findViewById(R.id.register_reg);
        //set register to onClick event
        registerB.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_reg:
                // Check all requir field empty or not
                if(name.getText().toString().equals("")
                        || emailId.getText().toString().equals("")
                        || slackId.getText().toString().equals("")
                        || password.getText().toString().equals("")
                        || conPasword.getText().toString().equals("")) {
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
                }else if(!(password.getText().toString().equals(conPasword.getText().toString()))){
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
                }else{
                    // Background task to insert user information into database
                    BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(RegisterActivity.this);
                    backgroundLoginTask.execute("register",name.getText().toString(),
                                                emailId.getText().toString(),
                                                slackId.getText().toString(),
                                                password.getText().toString(),
                                                info.getText().toString());
                }
                break;
        }
    }
}
