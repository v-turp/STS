package com.score.sts.presentation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.util.AttributeSet;

/**
 * Created by Who Dat on 10/29/2016.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Transition extends TransitionSet {

    public Transition(){}

    public Transition(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    private void init(){
        setOrdering(ORDERING_TOGETHER);
        addTransition( new ChangeBounds())
        .addTransition(new ChangeTransform())
        .setDuration(2000)
        .addTransition(new ChangeImageTransform());
    }
}
