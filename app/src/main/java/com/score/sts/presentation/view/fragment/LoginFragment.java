package com.score.sts.presentation.view.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.score.sts.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        setPortraitOrientationMargins(view);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * The star is not aligned correctly on portrait mode. this method
     * dynamically sets the margin for the star icon
     */
    private void setPortraitOrientationMargins(View view){
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ImageView imageView = (ImageView) view.findViewById(R.id.ivLoginStar);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(55,10,0,0);
            imageView.setLayoutParams(layoutParams);
        }
    }

}
