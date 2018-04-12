package org.sairaa.scholarquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sairaa.scholarquiz.Interfaces.DataLoad_Interface;
import org.sairaa.scholarquiz.Model.NetworkClass;

import java.util.HashMap;

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
                } else {
                    // Background task to insert user information into database
                    /*BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(RegisterActivity.this);
                    backgroundLoginTask.execute("register",name.getText().toString(),
                                                emailId.getText().toString(),
                                                slackId.getText().toString(),
                                                password.getText().toString(),
                                                info.getText().toString());*/

                    HashMap<String, String> params = parseHashmap(name.getText().toString(),
                            emailId.getText().toString(),
                            slackId.getText().toString(),
                            password.getText().toString(),
                            info.getText().toString());

                    NetworkClass networkClassInstance = new NetworkClass(APIHandler.REGISTER_URL, dataLoad_interface, params);

                    networkClassInstance.execute();
                }
                break;
        }
    }

    private HashMap<String, String> parseHashmap(String name, String emailId, String slackId, String password, String info){

        HashMap<String, String> entry = new HashMap<String,String>();

        entry.put("user_name",name);
        entry.put("mail_id",emailId);
        entry.put("slack_id",slackId);
        entry.put("password",password);
        entry.put("info",info);

        return entry;

    }

    DataLoad_Interface dataLoad_interface = new DataLoad_Interface() {
        @Override
        public void successful(String response) {

            try {
                Log.d("response", response);
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject object = jsonArray.getJSONObject(0);
                String code = object.getString("code");
                String message = object.getString("message");

                if(code.equals("reg_true")){
                    finish();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }else if(code.equals("reg_false")){
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e){

            }
        }



        @Override
        public void onError() {
            Toast.makeText(RegisterActivity.this, "some error occur", Toast.LENGTH_SHORT).show();
        }
    };
}
