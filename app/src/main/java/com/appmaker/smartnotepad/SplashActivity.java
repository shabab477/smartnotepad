package com.appmaker.smartnotepad;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appmaker.smartnotepad.activities.MainActivity;

//This class is responsible for showing the Splash Screen
//How long the animation lasts and when I will go to the
//the next screen is implemented here.
public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView logoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        logoSplash = findViewById(R.id.logo_splash);
        logoSplash.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(SplashActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC_IN );

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(()->{
            logoSplash.setVisibility(View.VISIBLE);
            Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.logo_splash_anim);
            myAnim.setAnimationListener(new SplashAnimationListener());
            logoSplash.setAnimation(myAnim);

        }, 100);

    }

    //Listener for events in the animation frames
    private class SplashAnimationListener implements Animation.AnimationListener
    {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.enter_animation, R.anim.exit_animation);

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }



}
