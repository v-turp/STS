package com.score.sts.presentation.view.component;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.score.sts.R;

/**
 * Created by Who Dat on 8/16/2016.
 */
public class HubCoordinator extends CoordinatorLayout {

    private static final String KEY_SUPER_STATE ="super_state";
    DataListHeader dataListHeader;
    DataList dataList;

    public HubCoordinator(Context context) {
        super(context);
        init();
    }

    public HubCoordinator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HubCoordinator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.component_hub, this);
        dataListHeader = (DataListHeader) findViewById(R.id.dlh_dlh_header);
        dataList = (DataList) findViewById(R.id.dl_data_list);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
        }else{
            super.onRestoreInstanceState(state);
        }

    }
}
