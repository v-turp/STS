package com.score.sts.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String SHOW_SNACK = "signup complete";
    private static final String GOT_IT = "got it";
    private static final String CONFIRMED = "Confirmed";
    private String gotIt;
    private Snackbar snackbar;
    public static ProfileImageLoadHelper imageLoadHelper;

    // setup cache
    private static LruCache<String, Bitmap> imageMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global);
        setSupportActionBar(toolbar);

        // TODO setup the LruCache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // use 1/8th  of the available memory for this memory cach
        final int cacheSize = maxMemory / 8;

        if(imageMemoryCache == null){
            imageMemoryCache = new LruCache<String, Bitmap>(cacheSize){
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // cache size is measured in kilobytes
                    return bitmap.getByteCount() / 1024 ;
                }
            };

            // load the images with the asyncTask
            imageLoadHelper = new ProfileImageLoadHelper(this);
            imageLoadHelper.execute();
        }

        Log.d(TAG, "Max Memory Size: " + Runtime.getRuntime().maxMemory() / 1024 );
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadImagesFromCache();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(imageLoadHelper != null) {
            imageLoadHelper.cancel(true);
            imageLoadHelper = null; // release resources
        }
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

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if( getBitmapFromMemCache(key) == null){
            imageMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key){
            return imageMemoryCache.get(key);
    }

    private void loadImagesFromCache(){
        // TODO redo the naming convention for the portrait layout for the profile pages so that I don't have to constantly load the configuration dynamically
        findViewById(R.id.fl_profile_photo).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.PROFILE_PICTURE)));
        findViewById(R.id.fl_partial_profile_bio).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.BIO)));
        findViewById(R.id.fl_partial_profile_music).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.MUSIC)));
        findViewById(R.id.fl_partial_profile_pictures).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.PICTURES)));
        findViewById(R.id.fl_partial_profile_msg_cht).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.MESSAGE_AND_CHAT)));
        findViewById(R.id.fl_partial_profile_videos).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.VIDEOS)));
        findViewById(R.id.fl_partial_profile_contacts).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.CONTACTS)));
        findViewById(R.id.fl_partial_profile_register_work).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.REGISTER_MATERIAL)));
        findViewById(R.id.image_pictures_ic_edit).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.EDIT_ICON)));
        findViewById(R.id.image_videos_ic_upload).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.UPLOAD_ICON)));
        findViewById(R.id.image_profile_message_chat).setBackground( new BitmapDrawable(getResources(), getBitmapFromMemCache(ProfileImageLoadHelper.STAR_ICON)));
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
     * This class is created for dual purpose to be use as an asyncTask of not asynchronously
     * If used asynchronously, the configuration will change quicker during rotation but the images will load much slower.
     * however if not used asynchronously, the images will load quicker but will take longer to rotate.
     */
    public static class ProfileImageLoadHelper extends AsyncTask<Context, Map<String, Bitmap>, Map<String, Bitmap>>{
        public static final String PROFILE_PICTURE = "profile picture";
        public static final String BIO = "bio";
        public static final String MUSIC = "music";
        public static final String PICTURES = "pictures";
        public static final String MESSAGE_AND_CHAT = "message and chat";
        public static final String VIDEOS = "videos";
        public static final String CONTACTS = "contacts";
        public static final String REGISTER_MATERIAL = "register material";
        public static final String EDIT_ICON = "edit icon";
        public static final String UPLOAD_ICON = "upload icon";
        public static final String STAR_ICON = "star icon";
        public static final String ADD_PERSON_ICON = "add person icon";


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

        // TODO implement the onProgressUpdate method and add a spinner to it.

        @Override
        protected Map<String, Bitmap> doInBackground(Context... context) {
            // the argument for doInBackground method is an array of context objects.
            // we can use the first element in the context array or use the global context object.
            // in this case we are using the first element in the context array
//            Context theContext = context[0]; // with this uncommented, I get a Runtime error, ArrayIndexOutOfVBounds. A context is not really necessary but don't remove this line
            return initializeProfileImages(null);

        }

        @Override
        protected void onPostExecute(Map<String, Bitmap> drawables) {
            super.onPostExecute(drawables);
            Toast.makeText(profileActivity, "onPostExecute is executed", Toast.LENGTH_LONG).show();
            loadImages(drawables);
        }

        /**
         * This method initializes the views for the profile page. The images are then decoded, resized and added to
         * their respective view.
         * @param context
         * @return return a map of all the resized and decoded images. all images will be decoded and resized
         *         before bind added to the map
         */
        public Map<String, Bitmap> initializeProfileImages(@Nullable Context context){
            Map<String, Bitmap> imageList = new HashMap<>();

            // initialize the views for the profile page
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


            // after decoding and resizing, initialize the images
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

            // add the decoded and resized images to the map for storage
            // icon drawables
            imageList.put(EDIT_ICON, edit);
            imageList.put(UPLOAD_ICON, upload);
            imageList.put(STAR_ICON, star);
            imageList.put(ADD_PERSON_ICON, personAdd);
            // image drawables
            imageList.put(PROFILE_PICTURE, girlAvatar);
            imageList.put(MUSIC, boy);
            imageList.put(PICTURES, rmfmk);
            imageList.put(BIO, rmfmkTshirt);
            imageList.put(MESSAGE_AND_CHAT, starWars);
            imageList.put(VIDEOS, bugatti);
            imageList.put(CONTACTS, pics);
            imageList.put(REGISTER_MATERIAL, nightCloud);

            // output total size of all images
            getTotalSizeOfAllImages(imageList);

            return imageList;
        } // end method initializeProfileImages



        /**
         *
         * @param drawables this argument is the list of resized images. these images will be set to their respective view
         *                  and then added to the cache.
         */
        public void loadImages( Map<String, Bitmap> drawables){
            // profile image
            if(flProfilePic != null) {
                flProfilePic.setForeground(new BitmapDrawable(profileActivity.getResources(), drawables.get(PROFILE_PICTURE)));
                ProfileActivity.addBitmapToMemoryCache(PROFILE_PICTURE, drawables.get(PROFILE_PICTURE));
            }
            // bio images
            if(flProfileBio != null){
                flProfileBio.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(BIO)));
                ProfileActivity.addBitmapToMemoryCache(BIO, drawables.get(BIO));
            }
            if( imageBioEdit != null){
                imageBioEdit.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)) );
                ProfileActivity.addBitmapToMemoryCache(EDIT_ICON, drawables.get(EDIT_ICON));
            }
            // music images
            if(flProfileMusic != null){
                flProfileMusic.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(MUSIC)));
                ProfileActivity.addBitmapToMemoryCache(MUSIC, drawables.get(MUSIC));
            }
            // pictures images
            if(flProfilePictures != null){
                flProfilePictures.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(PICTURES)));
                ProfileActivity.addBitmapToMemoryCache(PICTURES, drawables.get(PICTURES));
            }
            if(imagePictures != null ){
                imagePictures.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)));
                // already added to cache
            }

            // message and chat
            if(flProfileMessageChat != null){
                flProfileMessageChat.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(MESSAGE_AND_CHAT)));
                ProfileActivity.addBitmapToMemoryCache(MESSAGE_AND_CHAT, drawables.get(MESSAGE_AND_CHAT));
            }
            if(imageMessageChat != null){
                imageMessageChat.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(STAR_ICON)));
                ProfileActivity.addBitmapToMemoryCache(STAR_ICON, drawables.get(STAR_ICON));
            }
            // vidoes
            if(flProfileVideos != null){
                flProfileVideos.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(VIDEOS)));
                ProfileActivity.addBitmapToMemoryCache(VIDEOS, drawables.get(VIDEOS));
            }
            if(imageVideos != null){
                imageVideos.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(UPLOAD_ICON)));
                ProfileActivity.addBitmapToMemoryCache(UPLOAD_ICON, drawables.get(UPLOAD_ICON));
            }
            // contacts
            if(flProfileContacts != null){
                flProfileContacts.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(CONTACTS)));
                ProfileActivity.addBitmapToMemoryCache(CONTACTS, drawables.get(CONTACTS));
            }
            if(imageContacts != null){
                imageContacts.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(ADD_PERSON_ICON)));
                ProfileActivity.addBitmapToMemoryCache(ADD_PERSON_ICON, drawables.get(ADD_PERSON_ICON));
            }
            // register work
            if(flProfileRegisterWork != null){
                flProfileRegisterWork.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(REGISTER_MATERIAL)));
                ProfileActivity.addBitmapToMemoryCache(REGISTER_MATERIAL, drawables.get(REGISTER_MATERIAL));
            }
        }


        /**
         *
         * @param map this is the map of resized images.
         *            we get the set of keys from the map and iterate through the map getting each image's size
         */
        private void getTotalSizeOfAllImages( Map map){
            Map<String, Bitmap> imageList = map;
            Set imageListKeySet = imageList.keySet();
            Iterator iterator = imageListKeySet.iterator();
            int totalSizeOfAllImages = 0;
            while(iterator.hasNext()){
                totalSizeOfAllImages+= ( imageList.get(iterator.next()).getByteCount() ) / 1024;   // get the list of images and, using the iterator, get the next image, its byte count/size and convert it to megs.
            }
            Log.d(TAG, "Size of all images: " + totalSizeOfAllImages);
        }

        public static int whatIsTheImageSize(BitmapDrawable drawable, String imageName){
            Log.d(TAG, "Drawable Size: " + imageName + " " + drawable.getBitmap().getByteCount() / 1024);

            return drawable.getBitmap().getByteCount() / 1024;
        }

    } // end class ProfileImageLoadHelper

} // end class ProfileActivity
