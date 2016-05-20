package com.score.sts.presentation.view.activity;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.ContactFragment;
import com.score.sts.presentation.view.fragment.FingerprintDialogFragment;
import com.score.sts.presentation.view.fragment.LoginDialogFragment;
import com.score.sts.presentation.view.fragment.LoginFragment;
import com.score.sts.presentation.view.fragment.MusicFragment;
import com.score.sts.presentation.view.fragment.RegisterWorkFragment;
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
        RegisterWorkFragment mRegisterWorkFragment = new RegisterWorkFragment();
        LoginFragment mLoginFragment = new LoginFragment();

        //--- set fragments according to orientation
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentTransaction.add(R.id.flMusicFragmentContainer, mMusicFragment);
            supportFragmentTransaction.add(R.id.flSignUpFragmentContainer, mSignUpFragment);
            supportFragmentTransaction.add(R.id.flContactsFragmentContainer, mContactFragment);
            supportFragmentTransaction.add(R.id.flVideoFragmentContainer, mVideosFragment);
            supportFragmentTransaction.add(R.id.flRegisterWorksFragmentContainer, mRegisterWorkFragment);
            supportFragmentTransaction.add(R.id.flLoginFragmentContainer, mLoginFragment);
            supportFragmentTransaction.commit();
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            supportFragmentTransaction.add(R.id.flSignUpFragmentContainer, mSignUpFragment);
            supportFragmentTransaction.add(R.id.flRegisterWorksFragmentContainer, mRegisterWorkFragment);
            supportFragmentTransaction.add(R.id.flLoginFragmentContainer, mLoginFragment);
            supportFragmentTransaction.commit();
        }

        showCreateAccountDialog();
//        showLoginDialogFragment();
        showFingerprintDialogFragment();
    } // end method init

    private void showCreateAccountDialog(){

        final DialogFragment createAccountDialog = new SignUpDialogFragment();
        final FrameLayout signUpFragContainer = (FrameLayout) findViewById(R.id.flSignUpFragmentContainer);
        if (signUpFragContainer != null) {
            signUpFragContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createAccountDialog.show(getFragmentManager(), "sign up fragment");
                }
            });
        }
    }

    private void showLoginDialogFragment(){
        final DialogFragment loginDialog = new LoginDialogFragment();
        final FrameLayout loginFracContainer = (FrameLayout) findViewById(R.id.flLoginFragmentContainer);
        if(loginFracContainer != null){
            loginFracContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginDialog.show(getFragmentManager(), "login fragment");
                }
            });
        }

    }

    private void showFingerprintDialogFragment(){
        final DialogFragment fingerprintDialog = new FingerprintDialogFragment();
        final FrameLayout loginFracContainer = (FrameLayout) findViewById(R.id.flLoginFragmentContainer);
        if(loginFracContainer != null){
            loginFracContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fingerprintDialog.show(getFragmentManager(), "fingerprint fragment");
                }
            });
        }

    }
}
