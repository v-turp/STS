package com.score.sts.presentation.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.score.sts.R;

/**
 * Created by Who Dat on 9/11/2016.
 * This class is the list portion of the data list
 */
public class DataList extends LinearLayout {

    public DataList(Context context) {
        super(context);
        init();
    }

    public DataList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DataList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.data_list, this);
    }
}
