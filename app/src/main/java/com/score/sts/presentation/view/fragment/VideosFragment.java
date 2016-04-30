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
public class VideosFragment extends Fragment {

    private static final String TAG = VideosFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View videosView = inflater.inflate(R.layout.videos_fragment, container, false);
        ButterKnife.bind(this, getActivity());
        return videosView;
    }
}
