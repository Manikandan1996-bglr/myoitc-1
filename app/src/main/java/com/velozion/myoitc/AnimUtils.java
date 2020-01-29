package com.velozion.myoitc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class AnimUtils {

    public static void animate(View viewToAnimate, boolean goesdown) {

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewToAnimate, "translationY", goesdown ? 200 : -200, 0);

        objectAnimator.setDuration(1000);

        animatorSet.playTogether(objectAnimator);

        animatorSet.start();//TEST

    }
}
