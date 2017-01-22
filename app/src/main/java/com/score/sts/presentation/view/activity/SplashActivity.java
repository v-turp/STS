package com.score.sts.presentation.view.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;

import com.score.sts.R;
import com.score.sts.data.net.OkNetworkConnect;

import butterknife.ButterKnife;

/**
 * Created by Who Dat on 3/18/2016.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);

        init();
    } // end method onCreate

    private void init() {

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
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
                }else
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //--- this this enables fullscreen for version 4.1+  set view hidden step 1
        View decorView = getWindow().getDecorView();
        //--- hide the status bar and navigation bar.
        int uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
        // immersive_sticky is only for 19+
        int uiOptions19 = (View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            decorView.setSystemUiVisibility(uiOptions19);
        }else {
            decorView.setSystemUiVisibility(uiOptions);
        }
        // --- end fullscreen impl 4.1+
    }
} // end class SplashActivity


