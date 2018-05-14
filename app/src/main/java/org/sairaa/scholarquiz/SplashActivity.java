package org.sairaa.scholarquiz;


import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.sairaa.scholarquiz.ui.Login.LoginActivity;

public class SplashActivity extends Activity {

    Thread splashThread;
    private static int SPLASH_TIME_OUT = 3500;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimation();
    }

    private void StartAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        ConstraintLayout l = findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView tv = findViewById(R.id.splashtagline);
        tv.clearAnimation();
        tv.startAnimation(anim);
        TextView tvDev = findViewById(R.id.splashDeveloper);
        tvDev.clearAnimation();
        tvDev.startAnimation(anim);

        splashThread = new Thread() {
            public void run() {
                try {
                    int waited = 0;
                    //splash screen pause time
                    while (waited < SPLASH_TIME_OUT) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SplashActivity.this.finish();
                }
            }
        };
        splashThread.start();


    }
}




