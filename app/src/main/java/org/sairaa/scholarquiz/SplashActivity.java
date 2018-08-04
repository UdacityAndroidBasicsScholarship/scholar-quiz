package org.sairaa.scholarquiz;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.sairaa.scholarquiz.app.Config;
import org.sairaa.scholarquiz.ui.Login.LoginActivity;
import org.sairaa.scholarquiz.utils.NotificationUtils;

public class SplashActivity extends Activity {

    Thread splashThread;
    private static int SPLASH_TIME_OUT = 3500;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window=getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mRegistrationBroadcastReceiver = new BroadcastReceiver(){

            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    Toast.makeText(getApplicationContext(), "Push notification: " + "123456", Toast.LENGTH_LONG).show();
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();
        StartAnimation();
    }
    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("Firebase", "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }
    private void StartAnimation(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        ConstraintLayout l=(ConstraintLayout)findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim=AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();
        TextView tv=(TextView) findViewById(R.id.splashtagline);
        tv.clearAnimation();
        tv.startAnimation(anim);
        TextView tvDev=(TextView) findViewById(R.id.splashDeveloper);
        tvDev.clearAnimation();
        tvDev.startAnimation(anim);

        splashThread= new  Thread(){
            public void  run(){
                try{
                    int waited=0;
                    //splash screen pause time
                    while(waited<SPLASH_TIME_OUT){
                        sleep(100);
                        waited+=100;
                    }
                    Intent intent= new Intent(SplashActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    SplashActivity.this.finish();
                }
            }
        };
        splashThread.start();


    }
}




