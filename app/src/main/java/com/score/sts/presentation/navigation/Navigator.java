package com.score.sts.presentation.navigation;


import android.content.Context;
import android.content.Intent;

import com.score.sts.presentation.view.activity.ProfileActivity;

/**
 * Created by Who Dat on 5/1/2016.
 * Class used to navigate through the application
 */

public class Navigator {

     private static Navigator navigator = null;

     private Navigator(){
     }

     public static Navigator getInstance(){

         if( navigator == null ){
             navigator = new Navigator();
         }
         return navigator;
     }

     public void navigateToProfilePage(Context context){
         Intent intent = new Intent();
         intent.setClass(context, ProfileActivity.class);
         context.startActivity(intent);
     }

}
