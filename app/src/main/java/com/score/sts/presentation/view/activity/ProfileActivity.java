package com.score.sts.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.BitmapUtil;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String SHOW_SNACK = "signup complete";
    private static final String GOT_IT = "got it";
    private static final String CONFIRMED = "Confirmed";
    private String gotIt;
    private Snackbar snackbar;
    public static ProfileImageLoadHelper imageLoadHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global);
        setSupportActionBar(toolbar);

        imageLoadHelper = new ProfileImageLoadHelper(this);
        imageLoadHelper.execute();
        Log.d(TAG, "Max Memory Size: " + Runtime.getRuntime().maxMemory() / 1024 );
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        imageLoadHelper.loadProfileImages();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        imageLoadHelper.removeProfileImages();
        imageLoadHelper = null; // release resources
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GOT_IT, gotIt);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gotIt = savedInstanceState.getString(GOT_IT);

        if(snackbar.isShown() && gotIt.equals(CONFIRMED)){
            snackbar.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_search:
                Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item_settings:
                Toast.makeText(getApplicationContext(), "I chose Settings", Toast.LENGTH_LONG).show();
                return true;

            case R.id.item_log_out:
                Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent getCallingIntent(Context context){
        Intent callingIntent = new Intent(context, ProfileActivity.class);
        return callingIntent;
    }

    private void init(){
        snackbar = getFingerPrintSnackbarNotification();
        /**
         *  if the user clicked join, notification is sent here
         *  through the intent with instructions to display the snackbar.
         */
        if(getIntent().hasExtra(SHOW_SNACK)) {
            snackbar.show();
        }

    }

    private Snackbar getFingerPrintSnackbarNotification(){
        Snackbar snackingBar = Snackbar
              .make(findViewById(android.R.id.content), R.string.snackbar_fingerprint_setup_directions, Snackbar.LENGTH_INDEFINITE)
              .setAction(R.string.snackbar_fingerprint_setup_confirmation, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gotIt = CONFIRMED;    // used to save state
                                    }
                             });
        return snackingBar;
    }

    /**
     * This is a helper class to lazy load images
     */
    public static class ProfileImageLoadHelper extends AsyncTask<Context, ArrayList<Bitmap> , ArrayList<Bitmap>>{
        ProfileActivity profileActivity;
        Context context;
        Bitmap edit;
        Bitmap upload;
        Bitmap star;
        Bitmap personAdd;

        //TODO only used for debugging to get the size of the pics
        Bitmap girlAvatar;
        Bitmap boy;
        Bitmap rmfmkTshirt;
        Bitmap rmfmk;
        Bitmap starWars;
        Bitmap bugatti;
        Bitmap pics;
        Bitmap nightCloud;
        // TODO end debug pics
        FrameLayout flProfilePic;
        FrameLayout flProfileBio;
        FrameLayout flProfileMusic;
        FrameLayout flProfilePictures;
        FrameLayout flProfileMessageChat;
        FrameLayout flProfileVideos;
        FrameLayout flProfileContacts;
        FrameLayout flProfileRegisterWork;
        ImageView imageBioEdit;
        ImageView imagePictures;
        ImageView imageMessageChat;
        ImageView imageVideos;
        ImageView imageContacts;

        public ProfileImageLoadHelper(Context context){
//            this.context = context;
            this.profileActivity = (ProfileActivity) context; // this is context of the ProfileActivity
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(Context... context) {

            ArrayList<Bitmap> imageList = new ArrayList<>();

            flProfilePic = (FrameLayout) profileActivity.findViewById(R.id.fl_profile_photo);
            flProfileBio = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_bio);
            imageBioEdit = (ImageView) profileActivity.findViewById(R.id.image_profile_bio_ic_edit);
            flProfileMusic = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_music);
            flProfilePictures = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_pictures);
            imagePictures = (ImageView) profileActivity.findViewById(R.id.image_pictures_ic_edit);
            flProfileMessageChat = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_msg_cht);
            imageMessageChat = (ImageView) profileActivity.findViewById(R.id.image_profile_message_chat);
            flProfileVideos = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_videos);
            imageVideos = (ImageView) profileActivity.findViewById(R.id.image_videos_ic_upload);
            flProfileContacts = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_contacts);
            imageContacts = (ImageView) profileActivity.findViewById(R.id.image_profile_contacts);
            flProfileRegisterWork = (FrameLayout) profileActivity.findViewById(R.id.fl_partial_profile_register_work);



            // icon drawables
            edit = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_edit_white_24dp, 100, 100);
            upload = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_file_upload_white_24dp, 100, 100);
            star = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_star_border_white_18dp, 100, 100);
            personAdd = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_person_add_white_24dp, 100, 100);
            // image drawables
            girlAvatar = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.girl_avatar, 100, 100);
            boy = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.boy, 100, 100);
            rmfmk = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.rmfmk, 100, 100);
            rmfmkTshirt = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.rmfmk_tshirt, 100, 100);
            starWars = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.star_wars, 100, 100);
            bugatti = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.bugatti, 100, 100);
            pics = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.pics, 100, 100);
            nightCloud = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.night_cloud, 100, 100);

            // icon drawables
            imageList.add(edit);        // 0
            imageList.add(upload);      // 1
            imageList.add(star);        // 2
            imageList.add(personAdd);   // 3
            // image drawables
            imageList.add(girlAvatar);  // 4
            imageList.add(boy);         // 5
            imageList.add(rmfmk);       // 6
            imageList.add(rmfmkTshirt); // 7
            imageList.add(starWars);    // 8
            imageList.add(bugatti);     // 9
            imageList.add(pics);        // 10
            imageList.add(nightCloud);  // 11

            return imageList;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> drawables) {
            super.onPostExecute(drawables);
            Toast.makeText(profileActivity, "onPostExecute is executed", Toast.LENGTH_LONG).show();

            // profile image
            if(flProfilePic != null) {
                flProfilePic.setForeground(ContextCompat.getDrawable(profileActivity, R.drawable.girl_avatar));
            }
            // bio images
            if(flProfileBio != null){
                flProfileBio.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(6)));
            }
            if( imageBioEdit != null){
                imageBioEdit.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(0)) );
            }
            // music images
            if(flProfileMusic != null){
                flProfileMusic.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(5)));
            }
            // pictures images
            if(flProfilePictures != null){
                flProfilePictures.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(7)));
            }
            if(imagePictures != null ){
                imagePictures.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(0)));
            }

            // message and chat
            if(flProfileMessageChat != null){
                flProfileMessageChat.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(8)));
            }
            if(imageMessageChat != null){
                imageMessageChat.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(2)));
            }
            // vidoes
            if(flProfileVideos != null){
                flProfileVideos.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(9)));
            }
            if(imageVideos != null){
                imageVideos.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(1)));
            }
            // contacts
            if(flProfileContacts != null){
                flProfileContacts.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(10)));
            }
            if(imageContacts != null){
                imageContacts.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(3)));
            }
            // register work
            if(flProfileRegisterWork != null){
                flProfileRegisterWork.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(11)));
            }
        }

        public void loadProfileImages(){

        } // end method loadProfileImages

        public void removeProfileImages(){
            // profile picture
            flProfilePic.setForeground(null);
            flProfilePic = null;
            // bio imaages
            flProfileBio.setForeground(null);
            imageBioEdit.setImageResource(0);
            flProfileBio = null;
            imageBioEdit = null;
            // music images
            flProfileMusic.setForeground(null);
            flProfileMusic = null;
        } // end method removeProfileImages

        public static int whatIsTheImageSize(BitmapDrawable drawable, String imageName){
            Log.d(TAG, "Drawable Size: " + imageName + " " + drawable.getBitmap().getByteCount() / 1024);

            return drawable.getBitmap().getByteCount() / 1024;
        }

    } // end class ProfileImageLoadHelper

} // end class ProfileActivity
