package com.score.sts.presentation.view.fragment;


import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.score.sts.R;

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
        View view = inflater.inflate(R.layout.fingerprint_dialog_fragment, container, false);
        // TODO uncomment this if you don't want to have the title area. be aware that you will have
        // to adjust the sytles for the buttons because the text on the cancel button goes to the next line
        /*if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
