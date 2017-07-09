package com.score.sts.presentation.view.component.chat;

import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.score.sts.R;


public class ChatActivity extends AppCompatActivity {

    public final static int LOOPS = 1000;
    public CarouselPagerAdapter adapter;
    public ViewPager pager;
    public static int count = 10; // ViewPager items size
    /**
     * You shouldn't define first page = 0.
     * Lets define firstpage = 'number viewpager size' to make endless carousel
     */
    public static int FIRST_PAGE = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        pager = (ViewPager) findViewById(R.id.vp_chat_carousel);

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = ((metrics.widthPixels / 4) * 2);
        pager.setPageMargin(-pageMargin);

        adapter = new CarouselPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(adapter);

        // set current item to the middle page so we can fling to both
        // directions left and right
        pager.setCurrentItem(FIRST_PAGE);
        pager.setOffscreenPageLimit(3);
    }
}
