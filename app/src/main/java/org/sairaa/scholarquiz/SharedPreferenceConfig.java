package org.sairaa.scholarquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_prferance), Context.MODE_PRIVATE);

    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.login_status_preference), status);
        Log.i("SharedPreferanceWrite: ", "" + status);
        editor.commit();
    }

    public boolean readLoginStatus() {
        boolean status = false;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.login_status_preference), false);
        Log.i("SharedPreferanceRead: ", "" + status);
        return status;
    }

    public void writeQuizId(String quizId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.quizId_moderator), quizId);
        editor.commit();

    }

    public String readQuizId() {
        String quizId = sharedPreferences.getString(context.getResources().getString(R.string.quizId_moderator), null);
        return quizId;
    }

    public void writeNewQuizName(String quizName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.quizName_moderator), quizName);
        editor.commit();
    }

    public String readNewQuizName() {
        String quizName = sharedPreferences.getString(context.getResources().getString(R.string.quizName_moderator), null);
        return quizName;
    }

    public void writePublishedOrNot(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.quiz_published_or_not), status);
        editor.commit();
    }

    public boolean readPublishedOrNot() {
        boolean status = true;
        status = sharedPreferences.getBoolean(context.getResources().getString(R.string.quiz_published_or_not), true);
        return status;
    }

}
