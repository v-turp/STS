package com.score.sts.presentation.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.score.sts.R;
import com.score.sts.presentation.view.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Who Dat on 7/11/2016.
 */
public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileViewHolder> {

    private static final String TAG = ProfileRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<Map<String, Bitmap>> profileStation;
    Context context;
    public ProfileRecyclerViewAdapter(ArrayList< Map<String, Bitmap> > profileStation, Context context){
        this.profileStation = profileStation;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        // TODO bind the ProfileViewHolder
        // TODO create an if statement that determines the name of the frame in the list. if the list is bio, set the String description accordingly...bio, pictures, music, videos etc
        Log.d(TAG, "ViewHolder. Is it null? " + (holder == null));
        Log.d(TAG, "Profile Station. Is it null?  " + ( profileStation == null ) );
        Log.d(TAG, "Any objects in the ArrayList. Is it null? " + profileStation.size() );
        Log.d(TAG, "What is the ArrayList contents? Is it null? " + profileStation.get(position).keySet());

        switch(position){

            case 1:
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.ProfileImageLoadHelper.BIO)));
                    holder.textView.setText(R.string.profile_bio_fragment);
                break;
            case 0:
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.ProfileImageLoadHelper.MUSIC)));
                    holder.textView.setText(R.string.profile_music_fragment);
                break;
        }

    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item view and pass it to the view holder constructor
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        ProfileViewHolder profileViewHolder = new ProfileViewHolder(view);
        return profileViewHolder;
    }

    @Override
    public int getItemCount() {
        // TODO return the number of items in the list
        return 2;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_profile_item);
            textView = (TextView) itemView.findViewById(R.id.text_profile_description_item);
        }


    }
}
