package com.score.sts.presentation.view.fragment;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.score.sts.presentation.Platform;
import com.score.sts.presentation.model.GeneralContentDescription;
import com.score.sts.presentation.model.IGeneralContentDescription;
import com.score.sts.presentation.model.TrackInfo;
import com.score.sts.presentation.view.adapter.DataListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Who Dat on 10/3/2016.
 */
public class FeatureListFragment extends Fragment {

    private static final String TAG = FeatureListFragment.class.getSimpleName();
    private Platform buildFlavor;
    private FeatureListHandler featureListHandler;
    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.component_hub, container, false);
        featureListHandler = new FeatureListHandler();

        // setup the recyclerview with the adapter and sample data
        RecyclerView dataListRecyclerView = (RecyclerView) view.findViewById(R.id.rv_data_list);
        DataListRecyclerViewAdapter
                dataListRecyclerViewAdapter = new DataListRecyclerViewAdapter(setMockDataForDataListView(getArguments().getString(Platform.FLAVOR)), featureListHandler);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }
    public void setBuildFlavor(Platform buildFlavor){
        this.buildFlavor = buildFlavor;
    }


    public class FeatureListHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.d("FeatureListFragment", msg.getData().getString(TrackInfo.CREATED_BY.toString()));

            int positionNumber = Integer.valueOf(msg.getData().getString(TrackInfo.POSITION_NUMBER.toString()));

            switch(positionNumber){
                case 0:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.benzo));
                    break;
                case 1:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.cat));
                    break;
                case 2:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.cello_player));
                    break;
                case 3:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.default_avatar));
                    break;
                case 4:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.king_alphonso));
                    break;
                case 5:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bitcoin_fab));
                    break;
                case 6:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.motorcycle));
                    break;
                case 7:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.director));
                    break;
                case 8:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.accept_btc));
                    break;
                case 9:
                    view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.imagine_dragons));
                    break;
              default:
                  view.findViewById(R.id.rl_data_list_header_primary_area).setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bugatti));
                  break;
            }
        }
    }
    // TODO remove this method once the live data is available
    public List<IGeneralContentDescription> setMockDataForDataListView(String buildFlavor){

        List<IGeneralContentDescription> mjLocatorMockDataList = new ArrayList<>();
        List<IGeneralContentDescription> nfldlMockDataList = new ArrayList<>();
        List<IGeneralContentDescription> reoMockDataList = new ArrayList<>();
        List<IGeneralContentDescription> rmfmkMockDataList = new ArrayList<>();
        List<IGeneralContentDescription> sktsMockDataList = new ArrayList<>();

        List<IGeneralContentDescription> listData = new ArrayList<>();

        switch (buildFlavor){
            case Platform.SOUNDTRACKS_AND_SCORES:
                    //---sts mock data
                    IGeneralContentDescription stsMockData  =
                            new GeneralContentDescription("1", getString(R.string.taylor_swift), getString(R.string.t_swift_look_what_u_made_me_do),  "5:00");
                    IGeneralContentDescription stsMockData1 =
                            new GeneralContentDescription("2", getString(R.string.cardi_b), getString(R.string.cardi_b_bodak_yellow), "6:00");
                    IGeneralContentDescription stsMockData2 =
                            new GeneralContentDescription("3", getString(R.string.logic_ft_alessia_and_khalid), "???", "3:23");
                    IGeneralContentDescription stsMockData3 =
                            new GeneralContentDescription("4", getString(R.string.l_fonsi_d_yankee_ft_jb), getString(R.string.l_fonsi_d_yankee_despacito), "4:53");
                    IGeneralContentDescription stsMockData4 =
                            new GeneralContentDescription("5", getString(R.string.sam_smith), getString(R.string.s_smith_2_good_at_gb), "3:05");
                    IGeneralContentDescription stsMockData5 =
                            new GeneralContentDescription("6", getString(R.string.dj_khaled_ri_b_tiller), getString(R.string.dj_khaled_ri_d_tiller_wild_thoughts),"6:43");
                    IGeneralContentDescription stsMockData6 =
                            new GeneralContentDescription("7", getString(R.string.french_mon_ft_sw_lee), getString(R.string.french_mon_ft_sw_lee_unforgettable),"3:43");
                    IGeneralContentDescription stsMockData7 =
                            new GeneralContentDescription("8", getString(R.string.charlie_puth), getString(R.string.charlie_puth_attention), "4:60");
                    IGeneralContentDescription stsMockData8 =
                            new GeneralContentDescription("9", getString(R.string.imagine_dragons), getString(R.string.imagine_dragons_believer), "3:33");
                    IGeneralContentDescription stsMockData9 =
                            new GeneralContentDescription("10", getString(R.string.shawn_mendes), getString(R.string.shawn_mendes_theres_nothing_holding), "2:24");

                    List<IGeneralContentDescription> stsMockDataList = new ArrayList<>();

                    stsMockDataList.add(stsMockData);
                    stsMockDataList.add(stsMockData1);
                    stsMockDataList.add(stsMockData2);
                    stsMockDataList.add(stsMockData3);
                    stsMockDataList.add(stsMockData4);
                    stsMockDataList.add(stsMockData5);
                    stsMockDataList.add(stsMockData6);
                    stsMockDataList.add(stsMockData7);
                    stsMockDataList.add(stsMockData8);
                    stsMockDataList.add(stsMockData9);

                    listData = stsMockDataList;
                break;
            case Platform.MJ_LOCATOR:
                    //---mj locator mock data
                    IGeneralContentDescription mjlMockData = new GeneralContentDescription("1", "4.9 BY 90 REVIEWERS", "Whittier Daily Greens - 1.2 Miles", "Open until 8 pm");
                    IGeneralContentDescription mjlMockData1 = new GeneralContentDescription("2", "3.2 BY 80 REVIEWERS", "Flower Givers - 2.4 Miles", "Open until 8 pm");
                    IGeneralContentDescription mjlMockData2 = new GeneralContentDescription("3", "4.5 BY 380 REVIEWERS", "Happy Days - 3.3 Miles", "Open until 2 am");
                    IGeneralContentDescription mjlMockData3 = new GeneralContentDescription("4", "5.0 BY 230 REVIEWERS", "Natural Givers - 4.2 miles", "Open until 11 pm");
                    IGeneralContentDescription mjlMockData4 = new GeneralContentDescription("5", "3.9 BY 490 REVIEWERS", "Garden Friends - 5 Miles", "Closed Today");

                    mjLocatorMockDataList.add(mjlMockData);
                    mjLocatorMockDataList.add(mjlMockData1);
                    mjLocatorMockDataList.add(mjlMockData2);
                    mjLocatorMockDataList.add(mjlMockData3);
                    mjLocatorMockDataList.add(mjlMockData4);

                    listData = mjLocatorMockDataList;
                break;
            case Platform.SMIRK_TA_SELF:
                    //---skts mock data
                    IGeneralContentDescription sktsMockData = new GeneralContentDescription("1", "330K", "#IcantStopLaughing", "23K STSLikes");
                    IGeneralContentDescription sktsMockData1 = new GeneralContentDescription("2", "1.7M", "#SondtracksAndScores", "758K STSLikes");
                    IGeneralContentDescription sktsMockData2 = new GeneralContentDescription("3", "15K", "#TMZ", "43K STSLikes");
                    IGeneralContentDescription sktsMockData3 = new GeneralContentDescription("4", "5K", "#OnlyMyChild", "3K STSLikes");
                    IGeneralContentDescription sktsMockData4 = new GeneralContentDescription("5", "5.2M", "#AbundantLifeGroup", "1.5M STSLikes");

                    sktsMockDataList.add(sktsMockData);
                    sktsMockDataList.add(sktsMockData1);
                    sktsMockDataList.add(sktsMockData2);
                    sktsMockDataList.add(sktsMockData3);
                    sktsMockDataList.add(sktsMockData4);
                    listData = sktsMockDataList;
                break;
            case Platform.NFL_DRAFT_LEAGUE:
                    //---nfldl mock data
                    IGeneralContentDescription nfldlMockData = new GeneralContentDescription("1", "Nuk and friends", "DeAndre Hopkins", "1,298.02");
                    IGeneralContentDescription nfldlMockData1 = new GeneralContentDescription("2", "Lucky stars", "Andrew Luck", "1,290.83");
                    IGeneralContentDescription nfldlMockData2 = new GeneralContentDescription("3", "Winning is my Forte", "Matt Forte", "1,110.83");
                    IGeneralContentDescription nfldlMockData3 = new GeneralContentDescription("4", "Running up that Hill", "Jeremy Hill", "1,110.50");
                    IGeneralContentDescription nfldlMockData4 = new GeneralContentDescription("5", "Lockett and loaded", "Tyler Lockett", "1000.98");

                    nfldlMockDataList.add(nfldlMockData);
                    nfldlMockDataList.add(nfldlMockData1);
                    nfldlMockDataList.add(nfldlMockData2);
                    nfldlMockDataList.add(nfldlMockData3);
                    nfldlMockDataList.add(nfldlMockData4);

                    listData = nfldlMockDataList;
                break;
            case Platform.REO:
                    //---reo mock data
                    IGeneralContentDescription reoMockData = new GeneralContentDescription("1", "Tim Marks", "1743 West Broak Ave","3 Star Find");
                    IGeneralContentDescription reoMockData1 = new GeneralContentDescription("2", "Monica Chap", "4436 N Place", "4 Star Find");
                    IGeneralContentDescription reoMockData2 = new GeneralContentDescription("3", "Tina Winds", "11955 57th St","2.5 Star Find");
                    IGeneralContentDescription reoMockData3 = new GeneralContentDescription("4", "Mark Tao", "9037 Harper Dr", "3 Star Find");
                    IGeneralContentDescription reoMockData4 = new GeneralContentDescription("5", "Chris Chang", "4586 El Monte Dr",  "5 Star Find");

                    reoMockDataList.add(reoMockData);
                    reoMockDataList.add(reoMockData1);
                    reoMockDataList.add(reoMockData2);
                    reoMockDataList.add(reoMockData3);
                    reoMockDataList.add(reoMockData4);

                    listData = reoMockDataList;
                break;
            case Platform.RMFMK:
                    //---rmfmk mock data
                    IGeneralContentDescription rmfmkMockData = new GeneralContentDescription("1", "Veteran Care Day", "Disabled American Veterans", "June 1st");
                    IGeneralContentDescription rmfmkMockData1 = new GeneralContentDescription("2", "Share Love Cleanup", "United Nations Children's Fund", "June 3rd");
                    IGeneralContentDescription rmfmkMockData2 = new GeneralContentDescription("3", "Art Leader", "St. Jude's Children's Research Hospital", "June 5th");
                    IGeneralContentDescription rmfmkMockData3 = new GeneralContentDescription("4", "Food Drive", "Mega Care", "June 8th");
                    IGeneralContentDescription rmfmkMockData4 = new GeneralContentDescription("5", "School Tutors", "Role Model For Mankind", "June 10th");

                    rmfmkMockDataList.add(rmfmkMockData);
                    rmfmkMockDataList.add(rmfmkMockData1);
                    rmfmkMockDataList.add(rmfmkMockData2);
                    rmfmkMockDataList.add(rmfmkMockData3);
                    rmfmkMockDataList.add(rmfmkMockData4);

                    listData = rmfmkMockDataList;
                break;
        }
        return listData;
    }
}
