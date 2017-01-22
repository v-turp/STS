package com.score.sts.presentation.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.score.sts.R;
import com.score.sts.presentation.view.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Who Dat on 7/11/2016.
 */
public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileViewHolder> {

    private static final String TAG = ProfileRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<Map<String, Bitmap>> profileStation;
    Context context;
    private LruCache<String, Bitmap> imageCache;

    public ProfileRecyclerViewAdapter(ArrayList< Map<String, Bitmap> > profileStation, Context context){
        this.profileStation = profileStation;
        this.context = context;
    }

    public ProfileRecyclerViewAdapter(LruCache<String, Bitmap> imageCache, Context context){
        this.imageCache = imageCache;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        // TODO bind the ProfileViewHolder
        // TODO create an if statement that determines the name of the frame in the list. if the list is bio, set the String description accordingly...bio, pictures, music, videos etc
        Log.d(TAG, "ViewHolder. Is it null? " + (holder == null));
        Log.d(TAG, "Profile Station. Is it null?  " + ( profileStation == null ) );
//        Log.d(TAG, "Any objects in the ArrayList. Is it null? " + profileStation.size() );
//        Log.d(TAG, "What is the ArrayList contents? Is it null? " + profileStation.get(position).keySet());

        switch(position){
            case 0:
                    if(imageCache != null){
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.REO_LISTINGS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.EDIT_ICON)));
                    }else if(profileStation != null) {
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.REO_LISTINGS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.EDIT_ICON)));
                    }
                    holder.textView.setText(R.string.profile_music_fragment);
                break;

            case 1:
                    if(imageCache != null) {
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.NATIONAL_REO_BANKS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.UPLOAD_ICON)));
                    }else if( profileStation != null){
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.NATIONAL_REO_BANKS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.UPLOAD_ICON)));
                    }
                    holder.textView.setText(R.string.profile_bio_fragment);
                break;

            case 2:
                    if(imageCache != null) {
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.REGIONAL_REO_BANKS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.UPLOAD_ICON)));
                    }else if(profileStation != null){
                        holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.REGIONAL_REO_BANKS)));
                        holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.UPLOAD_ICON)));
                    }
                    holder.textView.setText(R.string.profile_fragment_pictures);
                break;

            case 3:
                if(imageCache != null) {
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.REO_SITES)));
                }else if(profileStation != null){
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.REO_SITES)));
                }
                holder.textView.setText(R.string.profile_message_chat_fragment);
                break;

            case 4:
                if(imageCache != null) {
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.VIRTUAL_AGENTS)));
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.UPLOAD_ICON)));
                }else if(profileStation != null){
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.VIRTUAL_AGENTS)));
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.UPLOAD_ICON)));
                }
                holder.textView.setText(R.string.profile_videos_fragment);
                break;

            case 5:
                if(imageCache != null) {
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.DAILY_FINDS)));
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.UPLOAD_ICON)));
                }else if(profileStation != null){
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.DAILY_FINDS)));
                    holder.imageView.setImageDrawable(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.UPLOAD_ICON)));
                }
                holder.textView.setText(R.string.profile_contacts_fragment);
                break;

            case 6:
                if(imageCache != null) {
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), imageCache.get(ProfileActivity.REO_RESOURCES)));
                }else if(profileStation != null){
                    holder.frameLayout.setBackground(new BitmapDrawable(context.getResources(), profileStation.get(position).get(ProfileActivity.REO_RESOURCES)));
                }
                holder.textView.setText(R.string.profile_register_work_frag);
                break;
        }

    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item view and pass it to the view holder constructor
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public int getItemCount() {
        // TODO return the number of items in the list
        return 7;
    }

    // TODO create a click listener for the list items for the circular reveal

    public static class ProfileViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        public ImageView imageView;
        public FrameLayout frameLayout;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_profile_item);
            textView = (TextView) itemView.findViewById(R.id.text_profile_description_item);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.fl_profile_item);
        }


    }
}
