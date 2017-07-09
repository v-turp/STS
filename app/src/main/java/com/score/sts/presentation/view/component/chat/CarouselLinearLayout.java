package com.score.sts.presentation.view.component.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Who Dat on 4/6/2017.
 */

public class CarouselLinearLayout extends LinearLayout {

    private float scale = CarouselPagerAdapter.BIG_SCALE;

    public CarouselLinearLayout(Context context) {
        super(context);
    }

    public CarouselLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setScaleBoth(float scale){
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // The main mechanism to display scale animation, you can customize it to your needs
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w/2, h/2);

    }
}


