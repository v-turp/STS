package com.score.sts.presentation.view.activity;

import android.animation.Animator;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.score.sts.presentation.view.fragment.FingerprintDialogFragment;
import com.score.sts.presentation.view.fragment.SignUpDialogFragment;


public class LandingActivity extends AppCompatActivity {

    public static final String TAG = LandingActivity.class.getSimpleName();
    static DialogFragment signupDialog;
    LandingImageLoadHelper imageLoadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        imageLoadHelper = new LandingImageLoadHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global);
        setSupportActionBar(toolbar);
        init();
    }

    // TODO load images here
    @Override
    protected void onStart() {
        super.onStart();
        imageLoadHelper.loadLandingImages();
    }

    // TODO remove images here
    @Override
    protected void onStop() {
        super.onStop();
        imageLoadHelper.removeLandingImages();
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
        showCreateAccountDialog();
        showFingerprintDialogFragment();
    } // end method init

    private void showCreateAccountDialog(){

        /*static DialogFragment*/ signupDialog = new SignUpDialogFragment();
        final ImageView imageSignUp = (ImageView) findViewById(R.id.image_signup);
        if (imageSignUp != null) {
            imageSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signupDialog.show(getFragmentManager(), "sign up fragment");
                }
            });
        }
    } // end method showCreateAccountDialog

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

    }// end method showFingerprintDialogFragment

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
    } // end method setCircularReveal

    public static class LandingImageLoadHelper{

        LandingActivity landingActivity;
        Drawable star;
        FrameLayout logoFrameLsyout;
        ImageView imageLoginStar;
        ImageView imageLogin;
        ImageView imageSignup;
        ImageView imageSignupStar;
        ImageView imageRegisterWork;
        ImageView imageRegisterWorkStar;
        ImageView imageContacts;
        ImageView imageRandom1;
        ImageView imageRandom2;

        public LandingImageLoadHelper(LandingActivity landingActivity){
            this.landingActivity = landingActivity;
            star = ContextCompat.getDrawable(landingActivity, R.drawable.ic_star_border_white_18dp);
            logoFrameLsyout = (FrameLayout) landingActivity.findViewById(R.id.flLogoFrameContainer); // logo
            imageLoginStar = (ImageView) landingActivity.findViewById(R.id.ivLoginStar);
            imageLogin = (ImageView) landingActivity.findViewById(R.id.image_login);
            imageSignup = (ImageView) landingActivity.findViewById(R.id.image_signup);
            imageSignupStar = (ImageView) landingActivity.findViewById(R.id.ivSignUpStar);
            imageRegisterWork = (ImageView) landingActivity.findViewById(R.id.image_register_work);
            imageRegisterWorkStar = (ImageView) landingActivity.findViewById(R.id.ivRegisterWorkStar);
            imageContacts = (ImageView) landingActivity.findViewById(R.id.image_contacts);
            imageRandom1 = (ImageView) landingActivity.findViewById(R.id.image_random1);
            imageRandom2 = (ImageView) landingActivity.findViewById(R.id.image_random2);
        }

        // TODO check the cache directory and load images from cache if they are in cache
        public void loadLandingImages(){

            // logo
            if(logoFrameLsyout != null) {
                logoFrameLsyout.setForeground(ContextCompat.getDrawable(landingActivity, R.drawable.mjl_logo));
            }

            // login image and star
            if(imageLoginStar != null){
                imageLoginStar.setImageDrawable(star);
            }
            if(imageLogin != null){
                imageLogin.setImageResource(R.drawable.mj_cert);
            }

            // signup
            if(imageSignup != null){
                imageSignup.setImageResource(R.drawable.mjl1);
            }
            if(imageSignupStar != null){
                imageSignupStar.setImageDrawable(star);
            }

            // register work
            if(imageRegisterWork != null){
                imageRegisterWork.setImageResource(R.drawable.mjl2);
            }
            if(imageRegisterWorkStar != null){
                imageRegisterWorkStar.setImageDrawable(star);
            }

            if(landingActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // contacts
                if (imageContacts != null) {
                    imageContacts.setImageResource(R.drawable.mjl3);
                }
                // bottom row columns 2 & 3 - could not think of a suitable name
                if (imageRandom1 != null) {
                    imageRandom1.setImageResource(R.drawable.mjl4);
                }
                if(imageRandom2 != null){
                    imageRandom2.setImageResource(R.drawable.mjl5);
                }
            }

        } // end method loadLandingImages

        // TODO set up a disk cache and load the images in the cache before removing in case we need to use them again
        public void removeLandingImages(){
            logoFrameLsyout.setForeground(null); // logo
            //login
            imageLogin.setImageResource(0);
            imageLoginStar.setImageDrawable(null);
            // signup
            imageSignup.setImageResource(0);
            imageSignupStar.setImageDrawable(null);
            // register work
            imageRegisterWork.setImageResource(0);
            imageRegisterWorkStar.setImageDrawable(null);
            // contacts
            if(imageContacts != null) {
                imageContacts.setImageResource(0);
            }
            // bottom row columns 2 & 3 - could not think of a suitable name
            if(imageRandom1 != null){
                imageRandom2.setImageResource(0);
            }
            if(imageRandom2 != null){
                imageRandom2.setImageResource(0);
            }
        } // end method removeLandingImages

    } // end inner class LandingImageLoadHelper

} // end class LandingActivity
