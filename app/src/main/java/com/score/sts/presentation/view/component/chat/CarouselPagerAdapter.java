package com.score.sts.presentation.view.component.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.score.sts.R;

/**
 * Created by Who Dat on 4/9/2017.
 */

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private ChatActivity context;
    private FragmentManager fragmentManager;
    private float scale;

    public CarouselPagerAdapter(ChatActivity context, FragmentManager fm){
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than other
        try{
            if(position == ChatActivity.FIRST_PAGE){ // TODO understand these constants
                scale = BIG_SCALE;
            }else{
                scale = SMALL_SCALE;
            }

            position = position % ChatActivity.count;
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(CarouselPagerAdapter.class.getSimpleName(), ex.getMessage());
        }
        return ItemFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        int count = 0;
        try{
            count = ChatActivity.count * ChatActivity.LOOPS;
        }catch (Exception ex){
            ex.printStackTrace();
            Log.e(CarouselPagerAdapter.class.getSimpleName(), ex.getMessage());
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try{
            if(positionOffset >= 0f && positionOffset <= 1f){
                CarouselLinearLayout cur = getRootView(position);
                CarouselLinearLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE - DIFF_SCALE * positionOffset);
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(CarouselPagerAdapter.class.getSimpleName(), ex.getLocalizedMessage());
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private CarouselLinearLayout getRootView(int position){
        return ((CarouselLinearLayout) fragmentManager.findFragmentByTag(this.getFragmentTag(position))
                .getView().findViewById(R.id.root_container));
    }

    private String getFragmentTag(int position){
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}
