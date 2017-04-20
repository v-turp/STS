package com.score.sts.presentation.view.component;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.score.sts.R;

/**
 * Created by Who Dat on 9/11/2016.
 * This is the header portion of the primary list that will
 * display all the data from all the categories
 */
public class DataListHeader extends CoordinatorLayout{

    public DataListHeader(Context context) {
        super(context);
        init();
    }

    public DataListHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DataListHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.data_list_header, this);
    }
}
