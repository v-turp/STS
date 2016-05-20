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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        alertDialog.setView(layoutInflater.inflate(R.layout.sign_up_dialog_fragment, null))
                .setPositiveButton(USE_PASSWORD, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //---launch the profile page
                        Intent intent = ProfileActivity.getCallingIntent(getActivity());
                        intent.putExtra(ProfileActivity.SHOW_SNACK, true);
                        startActivity(intent);
                    }
                }).setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alertDialog.create();
    }

}
