package com.velozion.myoitc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationSet;

/**
 * Created by JAGADISH on 10/9/2018.
 */

public class AnimUtils {

  public static void animate(View viewToAnimate, boolean goesdown)
    {

        AnimatorSet animatorSet=new AnimatorSet();


        ObjectAnimator objectAnimator=ObjectAnimator.ofFloat(viewToAnimate,"translationY",goesdown==true?200:-200,0);

        objectAnimator.setDuration(1000);

        animatorSet.playTogether(objectAnimator);

        animatorSet.start();//TEST

    }
}
