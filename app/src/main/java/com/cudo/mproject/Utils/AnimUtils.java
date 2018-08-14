package com.cudo.mproject.Utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by adsxg on 12/12/2017.
 */

public class AnimUtils {
    public static void animate(View v, boolean visible)
    {
        if(visible)
        {
            Animation aa = new AlphaAnimation(0,1);
            Animation ss = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);


            AnimationSet asoy = new AnimationSet(true);
            asoy.addAnimation(aa);
            asoy.addAnimation(ss);
            asoy.setDuration(900);
            asoy.setFillAfter(true);
            //v.clearAnimation();
            v.startAnimation(asoy);
        }
        else
        {
            Animation aa = new AlphaAnimation(1,0);
            // Animation ss = new ScaleAnimation(1,0,1,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);


            AnimationSet asoy = new AnimationSet(true);
            asoy.addAnimation(aa);
            //asoy.addAnimation(ss);
            asoy.setDuration(500);
            asoy.setFillAfter(true);
            //v.clearAnimation();
            v.startAnimation(asoy);
        }
    }
    public static void animateRotate(View v)
    {
        int duration = 1000;

        RotateAnimation rotateAnimation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
           /* rotateAnimation.setRepeatCount(5);
            rotateAnimation.setDuration(duration);*/

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.setRepeatCount(-1);
        animationSet.setDuration(duration);
        v.startAnimation(animationSet);
        //img_checkin.startAnimation(scaleAnimation);
    }
}

