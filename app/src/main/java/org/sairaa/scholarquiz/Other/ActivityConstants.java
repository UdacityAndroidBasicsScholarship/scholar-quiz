package org.sairaa.scholarquiz.Other;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.sairaa.scholarquiz.BackgroundLoginTask;

import java.util.ArrayList;

/**
 * Created by AndroidDreamz on 14-04-2018.
 */

public class ActivityConstants {
    public static ArrayList<URLInfo> urlList = new ArrayList<>();
    private final String LOG_BACKGROUNDLOGINTASK = "BackgroundLoginTask";
    private static ProgressDialog progressDialog;
    public static final int connectionTimeout = 30000;

    public static void initiateConstants(Context context){
        String domainURL = "http://sairaa.org/ScholarQuiz/";
        urlList.add(new URLInfo(domainURL+"login.php",false,true)); // 0 login
        urlList.add(new URLInfo(domainURL+"register_test.php",false,true)); // 1 register
    }

    public static void showProgressDialog(Context context,String title,String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideProgressDialog(){
        progressDialog.dismiss();
    }

    public static URLInfo getURLInfoCopy(URLInfo urlInfo){
        return new URLInfo(urlInfo.getUrl(),urlInfo.isGetRequestMethod(),urlInfo.isEncodingEnabled());
    }

    public static void callDataRequest(BackgroundLoginTask request){
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class URLInfo{
        String url;
        boolean isGetRequestMethod;
        boolean isEncodingEnabled;

        public URLInfo(String url, boolean isGetRequestMethod, boolean isEncodingEnabled) {
            this.url = url;
            this.isGetRequestMethod = isGetRequestMethod;
            this.isEncodingEnabled = isEncodingEnabled;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isGetRequestMethod() {
            return isGetRequestMethod;
        }

        public void setGetRequestMethod(boolean getRequestMethod) {
            isGetRequestMethod = getRequestMethod;
        }

        public boolean isEncodingEnabled() {
            return isEncodingEnabled;
        }

        public void setEncodingEnabled(boolean encodingEnabled) {
            isEncodingEnabled = encodingEnabled;
        }
    }

    public static class ServiceCallObj{
        String key;
        String value;

        public ServiceCallObj(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public interface SuccessCallBacks{
        void onSuccess();
        void onError();
    }
}


