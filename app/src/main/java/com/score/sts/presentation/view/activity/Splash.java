package com.score.sts.presentation.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;

import com.score.sts.R;

import butterknife.ButterKnife;

/**
 * Created by Who Dat on 3/18/2016.
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);

        init();
    } // end method onCreate

    private void init() {

        //--- this this enables fullscreen for version 4.1+  set view hidden step 1
        View decorView = getWindow().getDecorView();
        //--- hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // --- end fullscreen impl 4.1+


        //--- set the animation based on version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        //--- set the delay for calling the next activity, set transition for v21+
        new View(this).postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), LandingActivity.class);
                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle());
                }else
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
        }, 4000);
    }
} // end class Splash


