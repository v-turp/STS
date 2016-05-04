package com.score.sts.presentation.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.score.sts.R;
import com.score.sts.presentation.navigation.Navigator;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDialogFragment extends DialogFragment {

    private static final String TAG = LoginDialogFragment.class.getSimpleName();
    private static final String LOGIN = "LOGIN";
    private static final String CANCEL = "CANCEL";


    public LoginDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return createLoginDialog(null);
    }

    private Dialog createLoginDialog(@Nullable Bundle savedInstanceState){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //---set the dialog layout and button functions
        alertDialog.setView(inflater.inflate(R.layout.login_dialog_fragment, null))
                .setPositiveButton(LOGIN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //---launch the profile page
                        Navigator.getInstance().navigateToProfilePage(getActivity());
                    }
                })
                .setNegativeButton(CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog.create();
    }

}
