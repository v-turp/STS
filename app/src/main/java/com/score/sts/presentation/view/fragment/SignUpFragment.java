package com.score.sts.presentation.view.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.score.sts.R;

import butterknife.ButterKnife;

/**
 * Created by Who Dat on 2/9/2016.
 */
public class SignUpFragment extends Fragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileView = inflater.inflate(R.layout.sign_up_fragment, container, false);
        ButterKnife.bind(this, getActivity());
        setPortraitOrientationMargins(profileView);
        return profileView;
    }

    /**
     * The star is not aligned correctly on portrait mode. this method
     * dynamically sets the margin for the star icon
     */
    private void setPortraitOrientationMargins(View view){
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            ImageView imageView = (ImageView) view.findViewById(R.id.ivSignUpStar);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(35,10,0,0);
            imageView.setLayoutParams(layoutParams);
        }
    }
}
