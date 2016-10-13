package com.score.sts.presentation.view.fragment;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.score.sts.R;
import com.score.sts.presentation.model.IContentDescription;
import com.score.sts.presentation.model.VideoContent;
import com.score.sts.presentation.view.adapter.DataListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Who Dat on 10/3/2016.
 */
public class ComponentHubFragment extends Fragment {

    private static final String TAG = ComponentHubFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.component_hub, container, false);

        // setup the recyclerview with the adapter and sample data
        RecyclerView dataListRecyclerView = (RecyclerView) view.findViewById(R.id.rv_data_list);
        DataListRecyclerViewAdapter dataListRecyclerViewAdapter = new DataListRecyclerViewAdapter(setMockDataForDataListView());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dataListRecyclerView.setLayoutManager(layoutManager);
        dataListRecyclerView.setAdapter(dataListRecyclerViewAdapter);

        // TODO setting bottom sheet behavior
        LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.ll_bottom_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(500);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // lambda for the animation started in the GlobalLayoutListener
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = () -> {

            int cx = view.getWidth();
            int cy = view.getHeight();

            // get the hypothenuse so the radius is from one corner to the other
            int radius = (int) Math.hypot(view.getHeight(), view.getWidth());

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Animator reveal = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
                reveal.setInterpolator(new DecelerateInterpolator(2f));view.setVisibility(View.VISIBLE);
                reveal.setDuration(2000);
                reveal.start();
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        return view;
    } // end method onCreateView

    // TODO remove this method once the live data is available
    private List<IContentDescription> setMockDataForDataListView(){
        List<IContentDescription> videoContentList = new ArrayList<>();

        IContentDescription videoContentDescription = new VideoContent(1, " Marylyn Monro", "Pharrell Williams", 500);
        IContentDescription videoContentDescription1 = new VideoContent(2, " How You Do That There", "Master P", 600);
        IContentDescription videoContentDescription2 = new VideoContent(3, " Whisper Song", "Yin Yang Twins", 323);
        IContentDescription videoContentDescription3 = new VideoContent(4, " Commas", "Future", 453);
        IContentDescription videoContentDescription4 = new VideoContent(5, " Jealous", "Labrynth", 305);
        IContentDescription videoContentDescription5 = new VideoContent(6, " Save a horse ride a cowboy", "Big and Rich", 670);
        IContentDescription videoContentDescription6 = new VideoContent(7, " Beat It", "Michael Jackson", 343);
        IContentDescription videoContentDescription7 = new VideoContent(8, " Is this Love", "Bob Marley", 460);
        IContentDescription videoContentDescription8= new VideoContent(9, " I Shot the Sheriff", "Bob Marley", 333);
        IContentDescription videoContentDescription9 = new VideoContent(10, "Dear Mama", "Tupac", 224);

        videoContentList.add(videoContentDescription);
        videoContentList.add(videoContentDescription1);
        videoContentList.add(videoContentDescription2);
        videoContentList.add(videoContentDescription3);
        videoContentList.add(videoContentDescription4);
        videoContentList.add(videoContentDescription5);
        videoContentList.add(videoContentDescription6);
        videoContentList.add(videoContentDescription7);
        videoContentList.add(videoContentDescription8);
        videoContentList.add(videoContentDescription9);

        return videoContentList;
    }
}
