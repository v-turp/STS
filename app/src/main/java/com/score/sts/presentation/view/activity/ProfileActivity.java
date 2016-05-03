package com.score.sts.presentation.view.activity;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.BioProfileFragment;
import com.score.sts.presentation.view.fragment.ContactsProfileFragment;
import com.score.sts.presentation.view.fragment.MusicProfileFragment;
import com.score.sts.presentation.view.fragment.RegisterWorkProfileFragment;
import com.score.sts.presentation.view.fragment.TbdProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        init();
    }

    protected void init(){
        setOrientationBasedLayout();
    }

    private void setOrientationBasedLayout(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        TbdProfileFragment tbdProfileFragment = new TbdProfileFragment();
        MusicProfileFragment musicProfileFragment = new MusicProfileFragment();
        ContactsProfileFragment contactsProfileFragment = new ContactsProfileFragment();
        RegisterWorkProfileFragment registerWorkProfileFragment = new RegisterWorkProfileFragment();
        BioProfileFragment bioProfileFragment = new BioProfileFragment();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            ft.add(R.id.fl_profile_tbd_fragment, tbdProfileFragment);
            ft.add(R.id.fl_profile_music_fragment, musicProfileFragment);
            ft.add(R.id.fl_profile_contacts_fragment, contactsProfileFragment);
            ft.add(R.id.fl_profile_register_work_fragment, registerWorkProfileFragment);
            ft.add(R.id.fl_profile_bio_fragment, bioProfileFragment);
            ft.commit();
        }
    }



}
