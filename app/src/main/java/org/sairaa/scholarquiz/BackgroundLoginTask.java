package org.sairaa.scholarquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sairaa.scholarquiz.ui.Lesson.LessonActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundLoginTask extends AsyncTask<String, Void, String> {

    private final String LOG_BACKGROUNDLOGINTASK = "BackgroundLoginTask";

    private SharedPreferenceConfig sharedPreferenceConfig;

    Context context;
    Activity activity;
    ProgressDialog progressDialog;
    AlertDialog.Builder alertBuilder;

    public BackgroundLoginTask(Context ctx) {
        this.context = ctx;
        activity = (Activity) ctx;
    }

    @Override
    protected void onPreExecute() {
        alertBuilder = new AlertDialog.Builder(activity);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Connecting to Server");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {
        String method = params[0];

        if (method.equals("register")) {
            try {
                URL register_url = new URL("http://sairaa.org/ScholarQuiz/register_test.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) register_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String name = params[1];
                String email = params[2];
                String slack = params[3];
                String password = params[4];
                String info = params[5];
                Log.i("background", info);
                String data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" + URLEncoder.encode("mail_id", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("slack_id", "UTF-8") + "=" + URLEncoder.encode(slack, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" + URLEncoder.encode("info", "UTF-8") + "=" + URLEncoder.encode(info, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    Log.i("background", line);
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (method.equals("login")) {
            try {
                URL register_url = new URL("http://sairaa.org/ScholarQuiz/login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) register_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String email = params[1];
                String password = params[2];
                Log.i(LOG_BACKGROUNDLOGINTASK, email);

                String data = URLEncoder.encode("mail_id", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                    Log.i("background", line);
                }
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String json) {

        try {
            progressDialog.dismiss();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            JSONObject object = jsonArray.getJSONObject(0);
            String code = object.getString("code");
            String message = object.getString("message");

            if (code.equals("reg_true")) {
                showDialog("Registration Successfull", message, code);
            } else if (code.equals("reg_false")) {
                showDialog("Registration Failed", message, code);
            } else if (code.equals("login_true")) {

                Intent intent = new Intent(activity, LessonActivity.class);
                intent.putExtra("message", message);
                activity.startActivity(intent);
                sharedPreferenceConfig = new SharedPreferenceConfig(activity);
                sharedPreferenceConfig.writeLoginStatus(true);
                activity.finish();

            } else if (code.equals("login_false")) {
                showDialog("Error in Login", message, code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showDialog(String result_message, String message, String code) {

        alertBuilder.setTitle(result_message);
        alertBuilder.setMessage(message);
        if (code.equals("reg_true") || code.equals("reg_false")) {
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    activity.finish();
                }
            });


        } else if (code.equals("login_false")) {
            alertBuilder.setMessage(message);
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditText email = activity.findViewById(R.id.email_login);
                    EditText password = activity.findViewById(R.id.password_login);
                    email.setText("");
                    password.setText("");
                    dialogInterface.dismiss();
                }
            });


        }

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();

    }
}
