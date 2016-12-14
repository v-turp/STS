package com.score.sts.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.score.sts.R;
import com.score.sts.presentation.view.fragment.ComponentHubFragment;
import com.score.sts.presentation.view.fragment.ProfileHomeFragment;

public class ProfileDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);
        ProfileHomeFragment profileHomeFragment = new ProfileHomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.phf, profileHomeFragment,  "")
                .addToBackStack("Component_Reveal")
                .commit();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProfileDisplayActivity.class);
    }
}
