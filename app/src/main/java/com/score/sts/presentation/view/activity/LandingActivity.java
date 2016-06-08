package com.score.sts.presentation.view.activity;

import android.animation.Animator;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.ContactFragment;
import com.score.sts.presentation.view.fragment.FingerprintDialogFragment;
import com.score.sts.presentation.view.fragment.MusicFragment;
import com.score.sts.presentation.view.fragment.SignUpDialogFragment;
import com.score.sts.presentation.view.fragment.SignUpFragment;
import com.score.sts.presentation.view.fragment.VideosFragment;


public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global);
        setSupportActionBar(toolbar);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_search:
                Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item_settings:
                Toast.makeText(getApplicationContext(), "I chose Settings", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item_log_out:
                Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void init(){
        // TODO attach the fragments here
        FragmentManager supportFragmentMgr = getSupportFragmentManager();
        FragmentTransaction supportFragmentTransaction = supportFragmentMgr.beginTransaction();

        MusicFragment mMusicFragment = new MusicFragment();
        SignUpFragment mSignUpFragment = new SignUpFragment();
        ContactFragment mContactFragment = new ContactFragment();
        VideosFragment mVideosFragment = new VideosFragment();

        //--- set fragments according to orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentTransaction.add(R.id.fl_music_frame_container, mMusicFragment);
            supportFragmentTransaction.add(R.id.fl_sign_up_frame_container, mSignUpFragment);
            supportFragmentTransaction.add(R.id.fl_contacts_fragment_container, mContactFragment);
            supportFragmentTransaction.add(R.id.fl_video_frame_container, mVideosFragment);
            supportFragmentTransaction.commit();
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            supportFragmentTransaction.add(R.id.fl_sign_up_frame_container, mSignUpFragment);
            supportFragmentTransaction.commit();
        }

        showCreateAccountDialog();
        showFingerprintDialogFragment();
    } // end method init

    private void showCreateAccountDialog(){

        final DialogFragment signupDialog = new SignUpDialogFragment();
        final FrameLayout signUpFragContainer = (FrameLayout) findViewById(R.id.fl_sign_up_frame_container);
        if (signUpFragContainer != null) {
            signUpFragContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signupDialog.show(getFragmentManager(), "sign up fragment");
                }
            });
        }
    }

    private void showFingerprintDialogFragment(){
        final DialogFragment fingerprintLoginDialog = new FingerprintDialogFragment();
        ImageView imageLogin = (ImageView) findViewById(R.id.image_login);
        if(imageLogin != null){
            imageLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fingerprintLoginDialog.show(getFragmentManager(), "fingerprint fragment");
                    setCircularReveal(findViewById(R.id.flLogoFrameContainer));
                }
            });
        }

    }

    private void setCircularReveal(View view){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            float finalRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            anim.setDuration(1000);
            view.setVisibility(View.VISIBLE);
            anim.start();
        }
    }
}
