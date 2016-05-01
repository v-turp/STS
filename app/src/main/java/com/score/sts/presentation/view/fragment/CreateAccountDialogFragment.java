package com.score.sts.presentation.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import com.score.sts.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountDialogFragment extends DialogFragment {

    private static final String TAG = CreateAccountDialogFragment.class.getSimpleName();
    private static final String JOIN = "JOIN";
    private static final String CANCEL = "CANCEL";

    public CreateAccountDialogFragment() {
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

        alertDialog.setView(layoutInflater.inflate(R.layout.create_account_dialog_fragment, null))
                    .setPositiveButton(JOIN, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
