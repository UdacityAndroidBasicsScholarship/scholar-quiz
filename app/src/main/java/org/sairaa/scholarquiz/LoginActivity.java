package org.sairaa.scholarquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sairaa.scholarquiz.Interfaces.DataLoad_Interface;
import org.sairaa.scholarquiz.Model.NetworkClass;

import java.util.HashMap;

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

                HashMap<String, String> params = parseHashmap(email.getText().toString().trim(),password.getText().toString().trim());

                NetworkClass networkClassInstance = new NetworkClass(APIHandler.LOGIN_URL, dataLoad_interface, params);

                networkClassInstance.execute();
//                BackgroundLoginTask backgroundLoginTask = new BackgroundLoginTask(LoginActivity.this);
//                backgroundLoginTask.execute("login",email.getText().toString(),password.getText().toString());
                break;
            default:
        }
    }


    private HashMap<String, String> parseHashmap(String email, String password){

        HashMap<String, String> entry = new HashMap<String,String>();

        entry.put("mail_id",email);
        entry.put("password",password);

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

                if (code.equals("login_true")) {

                    Intent intent = new Intent(LoginActivity.this, LessonActivity.class);
                    intent.putExtra("message", message);
                    startActivity(intent);

                    sharedPreferenceConfig = new SharedPreferenceConfig(LoginActivity.this);
                    sharedPreferenceConfig.writeLoginStatus(true);
                    finish();

                } else if (code.equals("login_false")) {
                    Toast.makeText(LoginActivity.this, "some error occur", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e){

            }
        }



        @Override
        public void onError() {
            Toast.makeText(LoginActivity.this, "some error occur", Toast.LENGTH_SHORT).show();
        }
    };
}

