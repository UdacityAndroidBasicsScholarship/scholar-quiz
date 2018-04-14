package org.sairaa.scholarquiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sairaa.scholarquiz.Other.ActivityConstants;
import org.sairaa.scholarquiz.Other.GeneralActions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BackgroundLoginTask extends AsyncTask<String, Void, String> {

    private SharedPreferenceConfig sharedPreferenceConfig;

    Context context;
    Activity activity;
    ActivityConstants.URLInfo urlInfo;
    ArrayList<ActivityConstants.ServiceCallObj> parameters = new ArrayList<>();
    String encodedData = "";
    AsyncData listener;
    AlertDialog.Builder alertBuilder;

    public interface AsyncData {
        public void onDataReceive(JSONObject data);
    }

    public BackgroundLoginTask(Context context, ActivityConstants.URLInfo urlInfo, ArrayList<ActivityConstants.ServiceCallObj> parameters) {
        this.context = context;
        this.urlInfo = urlInfo;
        this.parameters = parameters;
        listener = (AsyncData) context;
        getEncodedData();
        getEncodedURL();
    }

    @Override
    protected void onPreExecute() {
        ActivityConstants.showProgressDialog(context, "Please Wait", "Connecting to Server");
    }


    @Override
    protected String doInBackground(String... params) {

        try {
            if (GeneralActions.checkNetwork(context)) {
                URL url = new URL(urlInfo.getUrl().trim());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if (urlInfo.isGetRequestMethod()) {
                    httpURLConnection.setRequestMethod("GET");
                } else {
                    httpURLConnection.setRequestMethod("POST");
                }
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(ActivityConstants.connectionTimeout);

                if (!encodedData.equals("")) {
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter;
                    if (urlInfo.isEncodingEnabled()) {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    } else {
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS));
                    }
                    bufferedWriter.write(encodedData);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                }

                InputStream IS;

                if (httpURLConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    IS = httpURLConnection.getInputStream();
                } else {
                /* error from server */
                    IS = httpURLConnection.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                    Log.i("background", line);
                }

            /* disconnecting opened endpoints */
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } else {
                return null;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String response) {

        try {
            ActivityConstants.hideProgressDialog();
            if (checkResponse(context, response)) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                JSONObject object = jsonArray.getJSONObject(0);

                listener.onDataReceive(object);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkResponse(Context context, String response) {
        if (response == null) {
            Toast.makeText(context, context.getResources().getString(R.string.networkMsg), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void getEncodedData() {
        try {
            if (!urlInfo.isGetRequestMethod()) {
                if (urlInfo.isEncodingEnabled()) {
                    for (int i = 0; i < parameters.size(); i++) {
                        if (i == parameters.size() - 1) {
                            encodedData = encodedData + URLEncoder.encode(parameters.get(i).getKey(), "UTF-8") + "=" + URLEncoder.encode(parameters.get(i).getValue(), "UTF-8");
                        } else {
                            encodedData = encodedData + URLEncoder.encode(parameters.get(i).getKey(), "UTF-8") + "=" + URLEncoder.encode(parameters.get(i).getValue(), "UTF-8") + "&";
                        }
                    }
                } else {
                    for (int i = 0; i < parameters.size(); i++) {
                        if (i == parameters.size() - 1) {
                            encodedData = encodedData + parameters.get(i).getKey() + "=" + parameters.get(i).getValue();
                        } else {
                            encodedData = encodedData + parameters.get(i).getKey() + "=" + parameters.get(i).getValue() + "&";
                        }
                    }
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getEncodedURL() {
        if (urlInfo.isGetRequestMethod()) {
            String url = urlInfo.getUrl();
            if (parameters.size() > 0) {
                url = url + "?";
            }
            for (int i = 0; i < parameters.size(); i++) {
                if (i == parameters.size() - 1) {
                    url = url + parameters.get(i).getKey() + "=" + parameters.get(i).getValue();
                } else {
                    url = url + parameters.get(i).getKey() + "=" + parameters.get(i).getValue() + "&";
                }
            }
            urlInfo.setUrl(url);
        }
    }
}
