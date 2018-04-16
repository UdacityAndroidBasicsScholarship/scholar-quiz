package org.sairaa.scholarquiz;

/**
 * This class is used to interact with API server.
 *
 * Created by Vinay Gupta on 11-04-2018.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class APIHandler {

    private static String api_host = "http://sairaa.org/ScholarQuiz/";

    public static String LOGIN_URL = "login.php";
    public static String REGISTER_URL = "register_test.php";

    public static String sendPostRequest(String requestURL,
                                         HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(api_host + requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String requestParam = getPostDataString(postDataParams);
            Log.d("API_request" , "Request: " + requestURL + "?" + requestParam);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os , "UTF-8"));
            writer.write(requestParam);

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
            }
            else {
                response="Error Registering";
            }
        } catch (Exception e) {
            response="Error While receiveing httpOk";
            e.printStackTrace();
        }
        Log.d("API_request" , "Response: " + response.toString());
        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            Log.d("editchildtest_return", entry.getKey());
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            Log.d("editchildtest_return", entry.getValue());
        }
        return result.toString();

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.d("Api_Request_Image", src);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
