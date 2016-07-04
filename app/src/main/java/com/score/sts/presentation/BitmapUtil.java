package com.score.sts.presentation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Who Dat on 7/3/2016.
 */
public class BitmapUtil {

    public static final String TAG = BitmapUtil.class.getSimpleName();

    public BitmapUtil(){}

    /**
     *
     * @param imageResourceId
     * The imageResourceId is the id of the image that whose details will be displayed in the log.
     * This method returns the bitmap options which can be used to calculate the subsample
     */
    public BitmapFactory.Options displayImageDetailsInLog(int imageResourceId, Context context){
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inJustDecodeBounds = true; //This technique allows me to read the dimensions and type of the image data prior to construction (and memory allocation) of the bitmap.
        BitmapFactory.decodeResource(context.getResources(), imageResourceId, bitmapOption);
        int imageHeight = bitmapOption.outHeight;
        int imageWidth = bitmapOption.outWidth;
        String imageMimeType = bitmapOption.outMimeType;
        Log.d(TAG, "Image height, width and mime type: " + imageHeight + ", " + imageWidth + ", " + imageMimeType);

        return bitmapOption;
    }

    /**
     *
     * @param options holds the details of the bitmap that will be sub-sampled
     * @param reqWidth  is the width or max width the image being sampled should be
     * @param reqHeight is the height or max height the image being sampled should be
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        options.inJustDecodeBounds = true;
        int inSampleSize = 1;

        final int height = options.outHeight;
        final int width = options.outWidth;

        if( height > reqHeight || width > reqWidth){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }

    public static Bitmap decodeBitmapFromResource(Resources resources, int resourceId, int reqWidth, int reqHeight){

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourceId, options);

        // calculate the inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // decode bitmap with inSampleSize
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeResource(resources, resourceId, options);
        Log.d(TAG, "New Image height and width: " + options.inSampleSize + ", " + options.outWidth );
        return BitmapFactory.decodeResource(resources, resourceId, options);
    }
}
