package com.score.sts.presentation.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.score.sts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageChatProfileFragment extends Fragment {


    public MessageChatProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.message_chat_profile_fragment, container, false);
    }

}