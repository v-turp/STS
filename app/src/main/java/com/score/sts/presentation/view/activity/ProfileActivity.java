package com.score.sts.presentation.view.activity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.score.sts.R;
import com.score.sts.presentation.BitmapUtil;
import com.score.sts.presentation.view.adapter.ProfileRecyclerViewAdapter;
import com.score.sts.presentation.view.component.HubCoordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ProfileActivity.class.getSimpleName();
    public static final String SHOW_SNACK = "signup complete";
    private static final String GOT_IT = "got it";
    private static final String CONFIRMED = "Confirmed";
    public static final String PROFILE_ACTIVITY_INTENT_FILTER = "profile recycler view";
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
    private String gotIt;
    private Snackbar snackbar;
    public static ProfileLandscapeLayoutTask landscapeLayoutTask;
    public static ProfilePortraitLayoutTask portraitLayoutTask;
    private static RecyclerView rvProfile;
    private ArrayList<Map<String, Bitmap>> recyclerViewImageList;   // this list is primarily for the recycler view adapter
    private static ProfileRecyclerViewAdapter profileRecyclerViewAdapter;   // this is static so it can be called in the onPostExecute of the AsyncTask
    // setup cache
    private static LruCache<String, Bitmap> imageMemoryCache;

    // views for circular reveal
    FrameLayout flBio;
    FrameLayout flMusic;
    FrameLayout flPictures;
    FrameLayout flMessageChat;
    FrameLayout flVideos;
    FrameLayout flContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global);
        setSupportActionBar(toolbar);

        // TODO create in the adapter a click listener for the circular reveal. this is for the portrait layout
        // layouts for circular reveal. this is for landscape layout.
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            flBio = (FrameLayout) findViewById(R.id.fl_partial_profile_bio);
            flBio.setOnClickListener(this);
            flMusic = (FrameLayout) findViewById(R.id.fl_partial_profile_music);
            flMusic.setOnClickListener(this);
            flPictures = (FrameLayout) findViewById(R.id.fl_partial_profile_pictures);
            flPictures.setOnClickListener(this);
            flMessageChat = (FrameLayout) findViewById(R.id.fl_partial_profile_msg_cht);
            flMessageChat.setOnClickListener(this);
            flVideos = (FrameLayout) findViewById(R.id.fl_partial_profile_videos);
            flVideos.setOnClickListener(this);
            flContacts = (FrameLayout) findViewById(R.id.fl_partial_profile_contacts);
            flContacts.setOnClickListener(this);
        }

        //--- setup recycler view
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvProfile = (RecyclerView) findViewById(R.id.rvProfile);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);
            rvProfile.setLayoutManager(layoutManager);
        }

        //--- setup the LruCache
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // use 1/8th  of the available memory for this memory cach
        final int cacheSize = maxMemory / 8;

        if (imageMemoryCache == null) {
            imageMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    // cache size is measured in kilobytes
                    return bitmap.getByteCount() / 1024;
                }
            };

            //---load the images with the asyncTask if the cache is empty. this is only for landscape orientation b/c the views that will be instantiated do not exits in portrait
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                landscapeLayoutTask = new ProfileLandscapeLayoutTask(this);
                landscapeLayoutTask.execute();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                portraitLayoutTask = new ProfilePortraitLayoutTask(this);
                portraitLayoutTask.execute();
                portraitLayoutTask.loadPortraitProfileImage();
            }
        } else {
            //---if the cache is not null and the size is still 0, launch the async task.SOLVED[if app starts in portrait and is then rotated to landscape, async task will not execute. now it will]
            if (imageMemoryCache != null /*&& imageMemoryCache.size() == 0*/) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    landscapeLayoutTask = new ProfileLandscapeLayoutTask(this);
//                    landscapeLayoutTask.execute();
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                        profileRecyclerViewAdapter = new ProfileRecyclerViewAdapter(imageMemoryCache, this);
//                        rvProfile.setAdapter(profileRecyclerViewAdapter);
                }
            }
        }

        Log.d(TAG, "Max Memory Size: " + Runtime.getRuntime().maxMemory() / 1024);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //---this is only for landscape orientation b/c the views that will be instantiated do not exits in portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            loadImagesFromCache();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            profileRecyclerViewAdapter = new ProfileRecyclerViewAdapter(imageMemoryCache, this);
            rvProfile.setAdapter(profileRecyclerViewAdapter);
            loadPortraitProfileImageFromCache();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(landscapeLayoutTask != null) {
            landscapeLayoutTask.cancel(true);
            landscapeLayoutTask = null; // release resources
        }
        if (portraitLayoutTask != null) {
            portraitLayoutTask.cancel(true);
            portraitLayoutTask = null; // release resources
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

        if (snackbar.isShown() && gotIt.equals(CONFIRMED)) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        CoordinatorLayout musicLayout = (CoordinatorLayout) findViewById(R.id.cl_component_hub);
        FrameLayout layoutToCover = (FrameLayout) findViewById(R.id.fl_frame_to_cover);
        int id = view.getId();

        switch(id){

            case R.id.fl_partial_profile_bio:
                        Toast.makeText(this, "Bio was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            case R.id.fl_partial_profile_music:
                        Toast.makeText(this, "Music was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            case R.id.fl_partial_profile_pictures:
                        Toast.makeText(this, "Pictures was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            case R.id.fl_partial_profile_msg_cht:
                        Toast.makeText(this, "Message/Chat was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            case R.id.fl_partial_profile_videos:
                        Toast.makeText(this, "Videos was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            case R.id.fl_partial_profile_contacts:
                        Toast.makeText(this, "Contacts was clicked", Toast.LENGTH_LONG).show();
                        administerCircularReveal(layoutToCover, musicLayout, 1000);
                break;

            default:
                break;
        }
    }

    // administers the circular reveal
    private void administerCircularReveal(View viewToCover, View viewToReveal, int duration){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = viewToCover.getWidth();
            int cy = viewToCover.getHeight();

            float finalRadius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(viewToReveal, cx, cy, 0, finalRadius);
            viewToReveal.setVisibility(View.VISIBLE);
            anim.setDuration(duration);
            anim.start();
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
        switch (item.getItemId()) {
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

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            imageMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return imageMemoryCache.get(key);
    }

    /**
     * this method is streamlined and does not create any objects. it simply trusts the cache and fetches images from the cache
     * to load images into their respective views. NOTE: The images from the cache have already been resized.
     */
    @SuppressWarnings("ConstantConditions")
    private void loadImagesFromCache() {
        // TODO redo the naming convention for the portrait layout for the profile pages so that I don't have to constantly load the configuration dynamically
        findViewById(R.id.fl_profile_photo).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(PROFILE_PICTURE)));
        //noinspection ConstantConditions,ConstantConditions
        findViewById(R.id.fl_partial_profile_bio).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(BIO)));
        findViewById(R.id.fl_partial_profile_music).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(MUSIC)));
        findViewById(R.id.fl_partial_profile_pictures).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(PICTURES)));
        findViewById(R.id.fl_partial_profile_msg_cht).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(MESSAGE_AND_CHAT)));
        findViewById(R.id.fl_partial_profile_videos).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(VIDEOS)));
        findViewById(R.id.fl_partial_profile_contacts).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(CONTACTS)));
        findViewById(R.id.fl_partial_profile_register_work).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(REGISTER_MATERIAL)));
        findViewById(R.id.image_pictures_ic_edit).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(EDIT_ICON)));
        findViewById(R.id.image_videos_ic_upload).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(UPLOAD_ICON)));
        findViewById(R.id.image_profile_message_chat).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(STAR_ICON)));
    }

    private void loadPortraitProfileImageFromCache() {
        findViewById(R.id.fl_profile_photo).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(PROFILE_PICTURE)));
    }

    private void init() {
        snackbar = getFingerPrintSnackbarNotification();
        /**
         *  if the user clicked join, notification is sent here
         *  through the intent with instructions to display the snackbar.
         */
        if (getIntent().hasExtra(SHOW_SNACK)) {
            snackbar.show();
        }
        // TODO if this works then clean up the code. if not remove it
        LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.ll_bottom_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(200);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private Snackbar getFingerPrintSnackbarNotification() {
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


    public static class ProfilePortraitLayoutTask extends AsyncTask<Void, Void, Map<String, Bitmap>> {

        ArrayList<Map<String, Bitmap>> imageBucket; // this will be sent in onPostExecute the ProfileActivity bucketList

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
        //--- end debug pics

//        FrameLayout flItemLayout;

        public ProfilePortraitLayoutTask(Context context) {
            this.context = context;
            this.profileActivity = (ProfileActivity) context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Map<String, Bitmap> doInBackground(Void... voids) {
            return initializeProfileImages(null);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Map<String, Bitmap> drawables) {
            super.onPostExecute(drawables);
            imageBucket = createImageBucketForRecyclerView(drawables);
            profileRecyclerViewAdapter = new ProfileRecyclerViewAdapter(imageBucket, profileActivity);
            rvProfile = (RecyclerView) profileActivity.findViewById(R.id.rvProfile);
            if (rvProfile != null) {
                rvProfile.setAdapter(profileRecyclerViewAdapter);
            }
            Toast.makeText(context, "Cache is setup from the PortraitLayoutTask", Toast.LENGTH_LONG).show();
        }

        /**
         * This method initializes the views for the profile page. The images are then decoded, resized and added to
         * their respective view.
         *
         * @param context
         * @return return a map of all the resized and decoded images. all images will be decoded and resized
         * before bind added to the map
         */
        public Map<String, Bitmap> initializeProfileImages(@Nullable Context context) {
            Map<String, Bitmap> imageMap = new HashMap<>();

//            flItemLayout = (FrameLayout) profileActivity.findViewById(R.id.fl_profile_item);
            // after decoding and resizing, initialize the images and add them to the cache
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
            imageMap.put(EDIT_ICON, edit);
            imageMap.put(UPLOAD_ICON, upload);
            imageMap.put(STAR_ICON, star);
            imageMap.put(ProfileActivity.ADD_PERSON_ICON, personAdd);
            // image drawables
            imageMap.put(PROFILE_PICTURE, girlAvatar);
            imageMap.put(MUSIC, boy);
            imageMap.put(PICTURES, rmfmk);
            imageMap.put(BIO, rmfmkTshirt);
            imageMap.put(MESSAGE_AND_CHAT, starWars);
            imageMap.put(VIDEOS, bugatti);
            imageMap.put(CONTACTS, pics);
            imageMap.put(REGISTER_MATERIAL, nightCloud);

            // load the images into cache. piggy-backing of the work that was done here...loading the decoded images into a map
            loadImagesIntoCache(imageMap);

            return imageMap;
        } // end method initializeProfileImages

        /**
         * @param drawables the drawables passed in here should already have been resized before adding them to cache
         */
        private void loadImagesIntoCache(Map<String, Bitmap> drawables) {
            ProfileActivity.addBitmapToMemoryCache(PROFILE_PICTURE, drawables.get(PROFILE_PICTURE));
            ProfileActivity.addBitmapToMemoryCache(BIO, drawables.get(BIO));
            ProfileActivity.addBitmapToMemoryCache(EDIT_ICON, drawables.get(EDIT_ICON));
            ProfileActivity.addBitmapToMemoryCache(MUSIC, drawables.get(MUSIC));
            ProfileActivity.addBitmapToMemoryCache(PICTURES, drawables.get(PICTURES));
            ProfileActivity.addBitmapToMemoryCache(MESSAGE_AND_CHAT, drawables.get(MESSAGE_AND_CHAT));
            ProfileActivity.addBitmapToMemoryCache(STAR_ICON, drawables.get(STAR_ICON));
            ProfileActivity.addBitmapToMemoryCache(VIDEOS, drawables.get(VIDEOS));
            ProfileActivity.addBitmapToMemoryCache(UPLOAD_ICON, drawables.get(UPLOAD_ICON));
            ProfileActivity.addBitmapToMemoryCache(CONTACTS, drawables.get(CONTACTS));
            ProfileActivity.addBitmapToMemoryCache(ADD_PERSON_ICON, drawables.get(ADD_PERSON_ICON));
            ProfileActivity.addBitmapToMemoryCache(REGISTER_MATERIAL, drawables.get(REGISTER_MATERIAL));
        }

        /***
         * @param mapBucket this argument is the set of resized images and icons used for the profile page
         * @return we are returning a bucket of resized images in a format that is usable for the recycler view
         * each map should contain a background image and the icon to display.
         * this method reconstructs and builds upon the data that is already contained in the mapBucket and generates
         * a usable arraylist for the recycler view.
         */
        private ArrayList<Map<String, Bitmap>> createImageBucketForRecyclerView(Map<String, Bitmap> mapBucket) {
            ArrayList<Map<String, Bitmap>> imageBucket = new ArrayList<>();
            Map<String, Bitmap> theMapBucket = mapBucket;

            Map<String, Bitmap> bioMap = new HashMap<>();
            bioMap.put(BIO, theMapBucket.get(BIO));
            bioMap.put(EDIT_ICON, theMapBucket.get(EDIT_ICON));
            imageBucket.add(0, bioMap);

            Map<String, Bitmap> musicMap = new HashMap<>();
            musicMap.put(MUSIC, theMapBucket.get(MUSIC));
            musicMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(musicMap);

            Map<String, Bitmap> picturesMap = new HashMap<>();
            picturesMap.put(PICTURES, theMapBucket.get(PICTURES));
            picturesMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(picturesMap);

            Map<String, Bitmap> messageAndChatMap = new HashMap<>();
            messageAndChatMap.put(MESSAGE_AND_CHAT, theMapBucket.get(MESSAGE_AND_CHAT));
            imageBucket.add(messageAndChatMap);

            Map<String, Bitmap> videosMap = new HashMap<>();
            videosMap.put(VIDEOS, theMapBucket.get(VIDEOS));
            videosMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(videosMap);

            Map<String, Bitmap> contactsMap = new HashMap<>();
            contactsMap.put(CONTACTS, theMapBucket.get(CONTACTS));
            contactsMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(contactsMap);

            Map<String, Bitmap> registerMaterialMap = new HashMap<>();
            registerMaterialMap.put(REGISTER_MATERIAL, theMapBucket.get(REGISTER_MATERIAL));
            imageBucket.add(registerMaterialMap);

            return imageBucket;
        }

        public void loadPortraitProfileImage() {
            Bitmap girlAvatar = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.girl_avatar, 100, 100);
            FrameLayout flProfilePic = (FrameLayout) profileActivity.findViewById(R.id.fl_profile_photo);
            // profile image
            if (flProfilePic != null) {
                flProfilePic.setForeground(new BitmapDrawable(profileActivity.getResources(), girlAvatar));
            }
        }
    } // end class ProfilePortraitLayoutTask

    /**
     * This is a helper class to lazy load images
     * This class is created for dual purpose to be use as an asyncTask of not asynchronously
     * If used asynchronously, the configuration will change quicker during rotation but the images will load much slower.
     * however if not used asynchronously, the images will load quicker but will take longer to rotate.
     */
    public static class ProfileLandscapeLayoutTask extends AsyncTask<ArrayList<Map<String, Bitmap>>, Map<String, Bitmap>, Map<String, Bitmap>> {

        ArrayList<Map<String, Bitmap>> imageBucket; // this will be sent in onPostExecute the ProfileActivity bucketList

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

        public ProfileLandscapeLayoutTask(Context context) {
//            this.context = context;
            this.profileActivity = (ProfileActivity) context; // this is context of the ProfileActivity
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // TODO implement the onProgressUpdate method and add a spinner to it.

        /***
         * @param imageList the argument for doInBackground method is an array of context objects.
         *                  we can use the first element in the context array or use the global context object.
         *                  in this case we are using the first element in the context array
         * @return
         */
        @Override
        protected Map<String, Bitmap> doInBackground(ArrayList<Map<String, Bitmap>>... imageList) {

            Map<String, Bitmap> theMap = initializeProfileImages(null);
            return theMap;
        }

        @Override
        protected void onPostExecute(Map<String, Bitmap> drawables) {
            super.onPostExecute(drawables);
//            Toast.makeText(profileActivity, "onPostExecute is executed", Toast.LENGTH_LONG).show();
            loadImages(drawables);
        }

        /**
         * This method initializes the views for the profile page. The images are then decoded, resized and added to
         * their respective view.
         *
         * @param context
         * @return return a map of all the resized and decoded images. all images will be decoded and resized
         * before bind added to the map
         */
        public Map<String, Bitmap> initializeProfileImages(@Nullable Context context) {
            Map<String, Bitmap> imageMap = new HashMap<>();

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


            // after decoding and resizing, initialize the images and add them to the cache
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
            imageMap.put(EDIT_ICON, edit);
            imageMap.put(UPLOAD_ICON, upload);
            imageMap.put(STAR_ICON, star);
            imageMap.put(ADD_PERSON_ICON, personAdd);
            // image drawables
            imageMap.put(PROFILE_PICTURE, girlAvatar);
            imageMap.put(MUSIC, boy);
            imageMap.put(PICTURES, rmfmk);
            imageMap.put(BIO, rmfmkTshirt);
            imageMap.put(MESSAGE_AND_CHAT, starWars);
            imageMap.put(VIDEOS, bugatti);
            imageMap.put(CONTACTS, pics);
            imageMap.put(REGISTER_MATERIAL, nightCloud);

            // output total size of all images for logging purposes
            getTotalSizeOfAllImages(imageMap);

            // load the images into cache. piggy-backing of the work that was done here...loading the decoded images into a map
            loadImagesIntoCache(imageMap);

            // initialize the image bucket that will be sent to the recycler view. this method is piggy-backing off the image list created here.
//            imageBucket = createImageBucketForRecyclerView(imageMap);

            return imageMap;
        } // end method initializeProfileImages


        /**
         * @param drawables this argument is the list of resized images. these images will be set to their respective view
         *                  which is the background or the icon(upload, edit, add etc...)
         */
        public void loadImages(Map<String, Bitmap> drawables) {
            // profile image
            if (flProfilePic != null) {
                flProfilePic.setForeground(new BitmapDrawable(profileActivity.getResources(), drawables.get(PROFILE_PICTURE)));
            }
            // bio images
            if (flProfileBio != null) {
                flProfileBio.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(BIO)));
            }
            if (imageBioEdit != null) {
                imageBioEdit.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)));
            }
            // music images
            if (flProfileMusic != null) {
                flProfileMusic.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(MUSIC)));
            }
            // pictures images
            if (flProfilePictures != null) {
                flProfilePictures.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(PICTURES)));
            }
            if (imagePictures != null) {
                imagePictures.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)));
                // already added to cache
            }

            // message and chat
            if (flProfileMessageChat != null) {
                flProfileMessageChat.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(MESSAGE_AND_CHAT)));
            }
            if (imageMessageChat != null) {
                imageMessageChat.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(STAR_ICON)));
            }
            // vidoes
            if (flProfileVideos != null) {
                flProfileVideos.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(VIDEOS)));
            }
            if (imageVideos != null) {
                imageVideos.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(UPLOAD_ICON)));
            }
            // contacts
            if (flProfileContacts != null) {
                flProfileContacts.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(CONTACTS)));
            }
            if (imageContacts != null) {
                imageContacts.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(ADD_PERSON_ICON)));
            }
            // register work
            if (flProfileRegisterWork != null) {
                flProfileRegisterWork.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(REGISTER_MATERIAL)));
            }
        }

        /***
         * @param drawables the drawables passed in here should already have been resized before adding them to cache
         */
        private void loadImagesIntoCache(Map<String, Bitmap> drawables) {
            ProfileActivity.addBitmapToMemoryCache(PROFILE_PICTURE, drawables.get(PROFILE_PICTURE));
            ProfileActivity.addBitmapToMemoryCache(BIO, drawables.get(BIO));
            ProfileActivity.addBitmapToMemoryCache(EDIT_ICON, drawables.get(EDIT_ICON));
            ProfileActivity.addBitmapToMemoryCache(MUSIC, drawables.get(MUSIC));
            ProfileActivity.addBitmapToMemoryCache(PICTURES, drawables.get(PICTURES));
            ProfileActivity.addBitmapToMemoryCache(MESSAGE_AND_CHAT, drawables.get(MESSAGE_AND_CHAT));
            ProfileActivity.addBitmapToMemoryCache(STAR_ICON, drawables.get(STAR_ICON));
            ProfileActivity.addBitmapToMemoryCache(VIDEOS, drawables.get(VIDEOS));
            ProfileActivity.addBitmapToMemoryCache(UPLOAD_ICON, drawables.get(UPLOAD_ICON));
            ProfileActivity.addBitmapToMemoryCache(CONTACTS, drawables.get(CONTACTS));
            ProfileActivity.addBitmapToMemoryCache(ADD_PERSON_ICON, drawables.get(ADD_PERSON_ICON));
            ProfileActivity.addBitmapToMemoryCache(REGISTER_MATERIAL, drawables.get(REGISTER_MATERIAL));
        }

        /**
         * @param map this is the map of resized images.
         *            we get the set of keys from the map and iterate through the map getting each image's size
         */
        private void getTotalSizeOfAllImages(Map map) {

            Map<String, Bitmap> imageList = map;
            Set imageListKeySet = imageList.keySet();
            Iterator iterator = imageListKeySet.iterator();
            int totalSizeOfAllImages = 0;
            while (iterator.hasNext()) {
                totalSizeOfAllImages += (imageList.get(iterator.next()).getByteCount()) / 1024;   // get the list of images and, using the iterator, get the next image, its byte count/size and convert it to megs.
            }
            Log.d(TAG, "Size of all images: " + totalSizeOfAllImages);
        }

        public static int whatIsTheImageSize(BitmapDrawable drawable, String imageName) {
            Log.d(TAG, "Drawable Size: " + imageName + " " + drawable.getBitmap().getByteCount() / 1024);

            return drawable.getBitmap().getByteCount() / 1024;
        }

    } // end class ProfileLandscapeLayoutTask

} // end class ProfileActivity
