package com.score.sts.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.BioProfileFragment;
import com.score.sts.presentation.view.fragment.ContactsProfileFragment;
import com.score.sts.presentation.view.fragment.MessageChatProfileFragment;
import com.score.sts.presentation.view.fragment.MusicProfileFragment;
import com.score.sts.presentation.view.fragment.RegisterWorkProfileFragment;
import com.score.sts.presentation.view.fragment.PicturesProfileFragment;
import com.score.sts.presentation.view.fragment.VideosProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    public static final String SHOW_SNACK = "signup complete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();
    }

    public static Intent getCallingIntent(Context context){
        Intent callingIntent = new Intent(context, ProfileActivity.class);
        return callingIntent;
    }


    protected void init(){
        setOrientationBasedLayout();

        if(getIntent().hasExtra(SHOW_SNACK)) {
            showFingerPrintAuthenticationPrompt(getIntent().getBooleanExtra(SHOW_SNACK, false));
        }
    }

    private void setOrientationBasedLayout(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        PicturesProfileFragment picturesProfileFragment = new PicturesProfileFragment();
        MusicProfileFragment musicProfileFragment = new MusicProfileFragment();
        ContactsProfileFragment contactsProfileFragment = new ContactsProfileFragment();
        RegisterWorkProfileFragment registerWorkProfileFragment = new RegisterWorkProfileFragment();
        BioProfileFragment bioProfileFragment = new BioProfileFragment();
        VideosProfileFragment videosProfileFragment = new VideosProfileFragment();
        MessageChatProfileFragment messageChatProfileFragment = new MessageChatProfileFragment();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ft.add(R.id.fl_profile_fragment, picturesProfileFragment);
            ft.add(R.id.fl_profile_music_fragment, musicProfileFragment);
            ft.add(R.id.fl_profile_contacts_fragment, contactsProfileFragment);
            ft.add(R.id.fl_profile_register_work_fragment, registerWorkProfileFragment);
            ft.add(R.id.fl_profile_bio_fragment, bioProfileFragment);
            ft.add(R.id.fl_profile_videos_fragment, videosProfileFragment);
            ft.add(R.id.fl_profile_msg_chat_facetime, messageChatProfileFragment);
            ft.commit();
        }
    }

    private void showFingerPrintAuthenticationPrompt(boolean show){
        if(show) {
            Snackbar.make(findViewById(android.R.id.content), "Account successfully setup", Snackbar.LENGTH_LONG).show();
        }
    }

}
