package com.score.sts.presentation.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.score.sts.R;
import com.score.sts.presentation.view.activity.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FingerprintDialogFragment extends DialogFragment {

    private static final String TAG = FingerprintDialogFragment.class.getSimpleName();
    private static final String USE_PASSWORD = "USE PASSWORD";
    private static final String CANCEL = "CANCEL";

    public FingerprintDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fingerprint_dialog_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
//        int width = getResources().getDimensionPixelSize(android.R.dimen.popup_width);
//        getDialog().getWindow().setLayout(700, 500);
    }
}
