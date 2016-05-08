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

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.BioProfileFragment;
import com.score.sts.presentation.view.fragment.ContactsProfileFragment;
import com.score.sts.presentation.view.fragment.MessageChatProfileFragment;
import com.score.sts.presentation.view.fragment.MusicProfileFragment;
import com.score.sts.presentation.view.fragment.RegisterWorkProfileFragment;
import com.score.sts.presentation.view.fragment.PicturesProfileFragment;
import com.score.sts.presentation.view.fragment.VideosProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String SHOW_SNACK = "signup complete";
    private static final String GOT_IT = "got it";
    private static final String CONFIRMED = "Confirmed";
    private String gotIt;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

//        snackbar = getFingerPrintSnackbarNotification();
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GOT_IT, gotIt);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gotIt = savedInstanceState.getString(GOT_IT);

        if(snackbar.isShown() && gotIt.equals(CONFIRMED)){
            snackbar.dismiss();
        }
    }

    public static Intent getCallingIntent(Context context){
        Intent callingIntent = new Intent(context, ProfileActivity.class);
        return callingIntent;
    }


    private void init(){
        setOrientationBasedLayout();
        snackbar = getFingerPrintSnackbarNotification();
        /**
         *  if the user clicked join, notification is sent here
         *  through the intent with instructions to display the snackbar.
         */
        if(getIntent().hasExtra(SHOW_SNACK)) {
            snackbar.show();
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

    private Snackbar getFingerPrintSnackbarNotification(){
        Snackbar snackingBar = Snackbar
              .make(findViewById(android.R.id.content), R.string.snackbar_fingerprint_setup_directions, Snackbar.LENGTH_INDEFINITE)
              .setAction(R.string.snackbar_fingerprint_setup_confirmation, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gotIt = CONFIRMED;    // used to save state
                                    }
                             });
        return snackingBar;
    }
}
