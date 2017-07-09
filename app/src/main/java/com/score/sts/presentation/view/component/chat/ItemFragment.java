package com.score.sts.presentation.view.component.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.score.sts.R;

/**
 * Created by Who Dat on 4/8/2017.
 */

public class ItemFragment extends Fragment {

    private static final String POSITION = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURCE = "resource";

    private int screenWidth;
    private int screenHeight;

    private int[] imageArray = new int[]{
            R.drawable.benzo, R.drawable.director, R.drawable.boy, // these colors are different from the sample
            R.drawable.cello_player, R.drawable.default_avatar, R.drawable.cat,
            R.drawable.king_alphonso, R.drawable.bugatti, R.drawable.girl_avatar,
            R.drawable.pics};


    public static Fragment newInstance(ChatActivity context, int pos, float scale){
        Bundle b = new Bundle();
        b.putInt(POSITION, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(context, ItemFragment.class.getName(), b);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(container == null){
            return null;
        }

        final int position = this.getArguments().getInt(POSITION);
        final float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);

        TextView textView = (TextView) linearLayout.findViewById(R.id.text);
        CarouselLinearLayout root = (CarouselLinearLayout) linearLayout.findViewById(R.id.root_container);
        ImageView imageView = (ImageView) linearLayout.findViewById(R.id.pagerImg);

        textView.setText("Caoursel item: " + position);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(imageArray[position]);   // TODO see if this causes an error, setting a color as the image resource


        // handling click event
        imageView.setOnClickListener( v -> {
//            Intent intent = new Intent(getActivity(), ImageDetailsActivity.class); TODO this launches a new Activity; we don't want or need this
//            intent.putExtra(DRAWABLE_RESOURCE, imageArray[position]);
//            startActivity(intent);
        });

        root.setScaleBoth(scale);

        return linearLayout;
    }

    //---get device screen width and height
    private void getWidthAndHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }
}
