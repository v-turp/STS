package com.score.sts.presentation.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.score.sts.R;
import com.score.sts.presentation.view.activity.ProfileActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpDialogFragment extends DialogFragment {

    private static final String TAG = SignUpDialogFragment.class.getSimpleName();
    private static final String JOIN = "JOIN";
    private static final String CANCEL = "CANCEL";

    public SignUpDialogFragment() {
        // Required empty public constructor
    }

/*

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_account_dialog_fragment, container, false);
    }
*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        alertDialog.setView(layoutInflater.inflate(R.layout.sign_up_dialog_fragment, null))
                    .setPositiveButton(JOIN, (dialog, which)->{
                            //---launch the profile page
//                            Intent intent = ProfileActivity.getCallingIntent(getActivity());
                            Intent intent = ProfileActivity.getCallingIntent(getActivity());
                            intent.putExtra(ProfileActivity.SHOW_SNACK, true);
                            startActivity(intent);
                            dialog.dismiss();
                    }).setNegativeButton(CANCEL, (dialog, which)->{
                            dialog.dismiss();
                    });
        return alertDialog.create();
    }
}
