package com.score.sts.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.score.sts.R;

import butterknife.ButterKnife;

/**
 * Created by Who Dat on 2/9/2016.
 */
public class MusicFragment extends Fragment {

    public static final String TAG = MusicFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View musicView = inflater.inflate(R.layout.music_fragment, container, false);
        ButterKnife.bind(this, getActivity());
        return musicView;
    }
}
