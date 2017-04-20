package com.score.sts.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.score.sts.presentation.BitmapUtil;
import com.score.sts.presentation.Platform;
import com.score.sts.presentation.Transition;
import com.score.sts.presentation.view.adapter.ProfileRecyclerViewAdapter;
import com.score.sts.presentation.view.fragment.ComponentHubFragment;
import com.score.sts.R;

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
    public static final String DISPENSARY_LOCATOR = "dispensary locator";    // todo this used to be BIO with value bio
    public static final String DELIVERY_SERVICES = "delivery services"; // todo this used to be MUSIC with value music
    public static final String DOCTORS = "doctors";   // todo this used to be PICTURES with value pictures
    public static final String STRAIN_GUIDE = "strain guide";   // todo this used to be MESSAGE_AND_CHAT with value message and chat
    public static final String ACCESSORIES = "accessories";       // todo this used to be VIDEOS with value videos
    public static final String DAILY_DEALS = "daily deals";   // todo this used to be CONTACTS with value contacts
    public static final String NORML_NEWS = "NORML news";     // todo used to be REGISTER_MATERIAL with value register material
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

    // image sizes
    private static final int BASE_IMAGE_HEIGHT = R.dimen.base_image_height;
    private static final int BASE_IMAGE_WIDTH = R.dimen.base_image_width;
    private static final int BASE_ICON_WIDTH = R.dimen.base_icon_width;
    private static final int BASE_ICON_HEIGHT = R.dimen.base_icon_height;
    private static final int IMAGE_SUPER_WIDTH = R.dimen.image_super_width;

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

        //--- setup recycler view for portrait layout in the Profile screen
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
//        CoordinatorLayout componentHub = (CoordinatorLayout) findViewById(R.id.cl_component_hub);
//        FrameLayout layoutToCover = (FrameLayout) findViewById(R.id.fl_frame_to_cover);
        ComponentHubFragment componentHubFragment = new ComponentHubFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Platform.FLAVOR, Platform.MJ_LOCATOR);
        componentHubFragment.setArguments(bundle); // this is to pass the string to the ComponentHubFragment to allow it to display the correct data in the list
        FragmentManager fm = getSupportFragmentManager();
        int id = view.getId();

        // TODO use this for testing until the transition is satisfactory
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            componentHubFragment.setReenterTransition(new Fade());
            componentHubFragment.setEnterTransition(new Fade());
            componentHubFragment.setSharedElementReturnTransition(new Transition());
            componentHubFragment.setSharedElementEnterTransition(new Transition());
        }
        switch(id){

            case R.id.fl_partial_profile_bio:
//                        Toast.makeText(this, "Bio was clicked", Toast.LENGTH_LONG).show();
                        ImageView sharedImage = (ImageView) findViewById(R.id.image_profile_bio_ic_edit);
                        this.findViewById(R.id.image_pictures_ic_edit).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DISPENSARY_LOCATOR)));
                        fm.beginTransaction()
                        .add(R.id.fl_fragment_container, componentHubFragment )
                        .addSharedElement(sharedImage, "profileImage")
                        .commit();
                // TODO remove this when finished testing. this is only used so i don't have to go back and forth to the loadImagesFromCache method
//                findViewById(R.id.fl_partial_profile_bio).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DISPENSARY_LOCATOR)));
                break;

            case R.id.fl_partial_profile_music:
//                        Toast.makeText(this, "Music was clicked", Toast.LENGTH_LONG).show();
                        fm.beginTransaction().add(R.id.fl_fragment_container, componentHubFragment).commit();
                break;

            case R.id.fl_partial_profile_pictures:
//                        Toast.makeText(this, "Pictures was clicked", Toast.LENGTH_LONG).show();
                        fm.beginTransaction().add(R.id.fl_fragment_container, componentHubFragment).commit();
                break;

            case R.id.fl_partial_profile_msg_cht:
//                        Toast.makeText(this, "Message/Chat was clicked", Toast.LENGTH_LONG).show();
                        fm.beginTransaction().add(R.id.fl_fragment_container, componentHubFragment).commit();
                break;

            case R.id.fl_partial_profile_videos:
//                        Toast.makeText(this, "Videos was clicked", Toast.LENGTH_LONG).show();
                        fm.beginTransaction().add(R.id.fl_fragment_container, componentHubFragment).commit();
                break;

            case R.id.fl_partial_profile_contacts:
//                        Toast.makeText(this, "Contacts was clicked", Toast.LENGTH_LONG).show();
                        fm.beginTransaction().add(R.id.fl_fragment_container, componentHubFragment).commit();
                break;

            default:
                break;
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
        findViewById(R.id.fl_partial_profile_bio).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DISPENSARY_LOCATOR)));
        findViewById(R.id.fl_partial_profile_music).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DELIVERY_SERVICES)));
        findViewById(R.id.fl_partial_profile_pictures).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DOCTORS)));
        findViewById(R.id.fl_partial_profile_msg_cht).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(STRAIN_GUIDE)));
        findViewById(R.id.fl_partial_profile_videos).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(ACCESSORIES)));
        findViewById(R.id.fl_partial_profile_contacts).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(DAILY_DEALS)));
        findViewById(R.id.fl_partial_profile_register_work).setBackground(new BitmapDrawable(getResources(), getBitmapFromMemCache(NORML_NEWS)));
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
        // TODO remove the evaluation once the setup is complete for portrait mode
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            LinearLayout bottomSheet = (LinearLayout) findViewById(R.id.ll_bottom_bottom_sheet);
//            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
//            behavior.setPeekHeight(200);
//            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

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
        Bitmap bmpMjlLogo;          // used to be girlAvatar
        Bitmap bmpDeliveryServices; // used to be boy
        Bitmap bmpDispensaryLocator;// used to be rmfmk tshirt
        Bitmap bmpDoctors;          // used to be rmfmk
        Bitmap bmpStrainGuide;      // used to be starWars
        Bitmap bmpAccessories;      // used to be bugatti
        Bitmap bmpDailyDeals;       // used to be pics
        Bitmap bmpNormlNews;        // used to be nightCloud
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
            edit = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_edit_white_24dp, BASE_ICON_WIDTH, BASE_ICON_HEIGHT);
            upload = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_file_upload_white_24dp, BASE_ICON_WIDTH, BASE_ICON_HEIGHT);
            star = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_star_border_white_18dp, BASE_ICON_WIDTH, BASE_ICON_HEIGHT);
            personAdd = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_person_add_white_24dp, BASE_ICON_WIDTH, BASE_ICON_HEIGHT);
            // image drawables
            bmpMjlLogo = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl12, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDeliveryServices = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl2, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDoctors = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl13, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDispensaryLocator = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl1, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpStrainGuide = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl8, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpAccessories = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl5, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDailyDeals = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl6, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpNormlNews = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl11, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);

            // add the decoded and resized images to the map for storage
            // icon drawables
            imageMap.put(EDIT_ICON, edit);
            imageMap.put(UPLOAD_ICON, upload);
            imageMap.put(STAR_ICON, star);
            imageMap.put(ProfileActivity.ADD_PERSON_ICON, personAdd);
            // image drawables
            imageMap.put(PROFILE_PICTURE, bmpMjlLogo);
            imageMap.put(DELIVERY_SERVICES, bmpDeliveryServices);
            imageMap.put(DOCTORS, bmpDoctors);
            imageMap.put(DISPENSARY_LOCATOR, bmpDispensaryLocator);
            imageMap.put(STRAIN_GUIDE, bmpStrainGuide);
            imageMap.put(ACCESSORIES, bmpAccessories);
            imageMap.put(DAILY_DEALS, bmpDailyDeals);
            imageMap.put(NORML_NEWS, bmpNormlNews);

            // load the images into cache. piggy-backing of the work that was done here...loading the decoded images into a map
            loadImagesIntoCache(imageMap);

            return imageMap;
        } // end method initializeProfileImages

        /**
         * @param drawables the drawables passed in here should already have been resized before adding them to cache
         */
        private void loadImagesIntoCache(Map<String, Bitmap> drawables) {
            ProfileActivity.addBitmapToMemoryCache(PROFILE_PICTURE, drawables.get(PROFILE_PICTURE));
            ProfileActivity.addBitmapToMemoryCache(DISPENSARY_LOCATOR, drawables.get(DISPENSARY_LOCATOR));
            ProfileActivity.addBitmapToMemoryCache(EDIT_ICON, drawables.get(EDIT_ICON));
            ProfileActivity.addBitmapToMemoryCache(DELIVERY_SERVICES, drawables.get(DELIVERY_SERVICES));
            ProfileActivity.addBitmapToMemoryCache(DOCTORS, drawables.get(DOCTORS));
            ProfileActivity.addBitmapToMemoryCache(STRAIN_GUIDE, drawables.get(STRAIN_GUIDE));
            ProfileActivity.addBitmapToMemoryCache(STAR_ICON, drawables.get(STAR_ICON));
            ProfileActivity.addBitmapToMemoryCache(ACCESSORIES, drawables.get(ACCESSORIES));
            ProfileActivity.addBitmapToMemoryCache(UPLOAD_ICON, drawables.get(UPLOAD_ICON));
            ProfileActivity.addBitmapToMemoryCache(DAILY_DEALS, drawables.get(DAILY_DEALS));
            ProfileActivity.addBitmapToMemoryCache(ADD_PERSON_ICON, drawables.get(ADD_PERSON_ICON));
            ProfileActivity.addBitmapToMemoryCache(NORML_NEWS, drawables.get(NORML_NEWS));
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

            Map<String, Bitmap> dispensaryMap = new HashMap<>();
            dispensaryMap.put(DISPENSARY_LOCATOR, theMapBucket.get(DISPENSARY_LOCATOR));
            dispensaryMap.put(EDIT_ICON, theMapBucket.get(EDIT_ICON));
            imageBucket.add(0, dispensaryMap);

            Map<String, Bitmap> musicMap = new HashMap<>();
            musicMap.put(DELIVERY_SERVICES, theMapBucket.get(DELIVERY_SERVICES));
            musicMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(musicMap);

            Map<String, Bitmap> picturesMap = new HashMap<>();
            picturesMap.put(DOCTORS, theMapBucket.get(DOCTORS));
            picturesMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(picturesMap);

            Map<String, Bitmap> messageAndChatMap = new HashMap<>();
            messageAndChatMap.put(STRAIN_GUIDE, theMapBucket.get(STRAIN_GUIDE));
            imageBucket.add(messageAndChatMap);

            Map<String, Bitmap> videosMap = new HashMap<>();
            videosMap.put(ACCESSORIES, theMapBucket.get(ACCESSORIES));
            videosMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(videosMap);

            Map<String, Bitmap> contactsMap = new HashMap<>();
            contactsMap.put(DAILY_DEALS, theMapBucket.get(DAILY_DEALS));
            contactsMap.put(UPLOAD_ICON, theMapBucket.get(UPLOAD_ICON));
            imageBucket.add(contactsMap);

            Map<String, Bitmap> registerMaterialMap = new HashMap<>();
            registerMaterialMap.put(NORML_NEWS, theMapBucket.get(NORML_NEWS));
            imageBucket.add(registerMaterialMap);

            return imageBucket;
        }

        public void loadPortraitProfileImage() {
            Bitmap mjRx = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl12, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            FrameLayout flProfilePic = (FrameLayout) profileActivity.findViewById(R.id.fl_profile_photo);
            // profile image
            if (flProfilePic != null) {
                flProfilePic.setForeground(new BitmapDrawable(profileActivity.getResources(), mjRx));
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


//        Bitmap bmpMjlLogo; // TODO all references to girlAvatar after mj logo is in place
//        Bitmap bmpDeliveryServices; // used to be boy
//        Bitmap bmpDispensaryLocator; // used to be rmfmk tshirt
//        Bitmap bmpDoctors;  // used to rmfmk
        //TODO only used for debugging to get the size of the pics
        Bitmap bmpMjlLogo;
        Bitmap bmpDoctors ;
        Bitmap bmpDispensaryLocator;
        Bitmap bmpDeliveryServices;
        Bitmap bmpStrainGuide;
        Bitmap bmpAccessories;
        Bitmap bmpDailyDeals;
        Bitmap bmpNormlNews;
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

            // TODO Since we are adding several flavors for demo purposes, the view ids
            // TODO will remain the same but be mapped to the relevant section/box
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
            edit = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_edit_white_24dp, BASE_IMAGE_WIDTH, BASE_ICON_HEIGHT);
            upload = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_file_upload_white_24dp, BASE_IMAGE_WIDTH, BASE_ICON_HEIGHT);
            star = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_star_border_white_18dp, BASE_IMAGE_WIDTH, BASE_ICON_HEIGHT);
            personAdd = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.ic_person_add_white_24dp, BASE_IMAGE_WIDTH, BASE_ICON_HEIGHT);
            // image drawables /*NOTE TODO THIS IS WHERE THE IMAGES WILL CHANGE. THIS MAY NEED TO BE THE ONLY CHANGE*/
            bmpMjlLogo = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl12, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDispensaryLocator = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl1, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDeliveryServices = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl2, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDoctors = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl13, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpStrainGuide = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl8, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);
            bmpAccessories = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl5, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpDailyDeals = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl6, BASE_IMAGE_WIDTH, BASE_IMAGE_HEIGHT);
            bmpNormlNews = BitmapUtil.decodeBitmapFromResource(profileActivity.getResources(), R.drawable.mjl11, IMAGE_SUPER_WIDTH, BASE_IMAGE_HEIGHT);

            // add the decoded and resized images to the map for storage
            // icon drawables
            imageMap.put(EDIT_ICON, edit);
            imageMap.put(UPLOAD_ICON, upload);
            imageMap.put(STAR_ICON, star);
            imageMap.put(ADD_PERSON_ICON, personAdd);
            // image drawables  // TODO NOTE: Update this map. swap the new images for the current build specific
            imageMap.put(PROFILE_PICTURE, bmpMjlLogo);
            imageMap.put(DELIVERY_SERVICES, bmpDeliveryServices);
            imageMap.put(DOCTORS, bmpDoctors);
            imageMap.put(DISPENSARY_LOCATOR, bmpDispensaryLocator);
            imageMap.put(STRAIN_GUIDE, bmpStrainGuide);
            imageMap.put(ACCESSORIES, bmpAccessories);
            imageMap.put(DAILY_DEALS, bmpDailyDeals);
            imageMap.put(NORML_NEWS, bmpNormlNews);

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
            // dispensary images...bio images in main source set
            if (flProfileBio != null) {
                flProfileBio.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(DISPENSARY_LOCATOR)));
            }
            if (imageBioEdit != null) {
                imageBioEdit.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)));
            }
            // dispensary services...music images in main source set
            if (flProfileMusic != null) {
                flProfileMusic.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(DELIVERY_SERVICES)));
            }
            // pictures images
            if (flProfilePictures != null) {
                flProfilePictures.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(DOCTORS)));
            }
            if (imagePictures != null) {
                imagePictures.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(EDIT_ICON)));
                // already added to cache
            }

            // strain guide...message and chat in main source set
            if (flProfileMessageChat != null) {
                flProfileMessageChat.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(STRAIN_GUIDE)));
            }
            if (imageMessageChat != null) {
                imageMessageChat.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(STAR_ICON)));
            }
            // accessories...vidoes in main source set
            if (flProfileVideos != null) {
                flProfileVideos.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(ACCESSORIES)));
            }
            if (imageVideos != null) {
                imageVideos.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(UPLOAD_ICON)));
            }
            // daily deals...contacts in main source set
            if (flProfileContacts != null) {
                flProfileContacts.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(DAILY_DEALS)));
            }
            if (imageContacts != null) {
                imageContacts.setImageDrawable(new BitmapDrawable(profileActivity.getResources(), drawables.get(ADD_PERSON_ICON)));
            }
            // NORML news...register work in main source set
            if (flProfileRegisterWork != null) {
                flProfileRegisterWork.setBackground(new BitmapDrawable(profileActivity.getResources(), drawables.get(NORML_NEWS)));
            }
        }

        /***
         * @param drawables the drawables passed in here should already have been resized before adding them to cache
         */ // TODO NOTE: since these names will be refactored globally, these values may not need to changed. keep an eye on thhen,
        private void loadImagesIntoCache(Map<String, Bitmap> drawables) {
            ProfileActivity.addBitmapToMemoryCache(PROFILE_PICTURE, drawables.get(PROFILE_PICTURE));
            ProfileActivity.addBitmapToMemoryCache(DISPENSARY_LOCATOR, drawables.get(DISPENSARY_LOCATOR));
            ProfileActivity.addBitmapToMemoryCache(EDIT_ICON, drawables.get(EDIT_ICON));
            ProfileActivity.addBitmapToMemoryCache(DELIVERY_SERVICES, drawables.get(DELIVERY_SERVICES));
            ProfileActivity.addBitmapToMemoryCache(DOCTORS, drawables.get(DOCTORS));
            ProfileActivity.addBitmapToMemoryCache(STRAIN_GUIDE, drawables.get(STRAIN_GUIDE));
            ProfileActivity.addBitmapToMemoryCache(STAR_ICON, drawables.get(STAR_ICON));
            ProfileActivity.addBitmapToMemoryCache(ACCESSORIES, drawables.get(ACCESSORIES));
            ProfileActivity.addBitmapToMemoryCache(UPLOAD_ICON, drawables.get(UPLOAD_ICON));
            ProfileActivity.addBitmapToMemoryCache(DAILY_DEALS, drawables.get(DAILY_DEALS));
            ProfileActivity.addBitmapToMemoryCache(ADD_PERSON_ICON, drawables.get(ADD_PERSON_ICON));
            ProfileActivity.addBitmapToMemoryCache(NORML_NEWS, drawables.get(NORML_NEWS));
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
