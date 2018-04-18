package org.sairaa.scholarquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class splashscreen extends AppCompatActivity implements View.OnClickListener {

    Button joinNow, login;
    ImageView appIcon;
    TextView punchLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        punchLine = findViewById(R.id.punchLine);
        appIcon = findViewById(R.id.appIcon);
        joinNow = findViewById(R.id.joinNow);
        login = findViewById(R.id.login);

        Animation animpunchLine = AnimationUtils.loadAnimation(splashscreen.this, R.anim.anim);
        animpunchLine.setInterpolator((new AccelerateDecelerateInterpolator()));
        animpunchLine.setFillAfter(true);
        punchLine.setAnimation(animpunchLine);

        Animation animAppIcon = AnimationUtils.loadAnimation(splashscreen.this, R.anim.scaleup);
        animAppIcon.setInterpolator((new AccelerateDecelerateInterpolator()));
        animAppIcon.setFillAfter(true);
        appIcon.setAnimation(animAppIcon);

        Animation animJoinNow = AnimationUtils.loadAnimation(splashscreen.this, R.anim.lefttorightanim);
        animJoinNow.setInterpolator((new AccelerateDecelerateInterpolator()));
        animJoinNow.setFillAfter(true);
        joinNow.setAnimation(animJoinNow);

        Animation animRegister = AnimationUtils.loadAnimation(splashscreen.this, R.anim.animdowntoup);
        animRegister.setInterpolator((new AccelerateDecelerateInterpolator()));
        animRegister.setFillAfter(true);
        login.setAnimation(animRegister);

        joinNow.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.joinNow:
                startActivity(new Intent(splashscreen.this, RegisterActivity.class) );
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                break;

            case R.id.login:
                startActivity(new Intent(splashscreen.this, LoginActivity.class) );
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
        }
    }
}
