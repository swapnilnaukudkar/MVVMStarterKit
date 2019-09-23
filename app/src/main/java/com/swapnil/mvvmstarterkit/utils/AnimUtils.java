package com.swapnil.mvvmstarterkit.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


public class AnimUtils {
    public static void hideFabWithObjectAnimator(final View fab) {
        AnimatorSet scaleSet = new AnimatorSet();

        ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 1);
        ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 0);

        scaleSet.setDuration(200);
        scaleSet.setInterpolator(new LinearInterpolator());

        scaleSet.playTogether(xScaleAnimator, yScaleAnimator);
        scaleSet.start();

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.setVisibility(View.GONE);
            }
        }, 200);*/
    }

    public static void showFabWithObjectAnimator(final View fab) {
        AnimatorSet scaleSet = new AnimatorSet();

        ObjectAnimator xScaleAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_X, 1);
        ObjectAnimator yScaleAnimator = ObjectAnimator.ofFloat(fab, View.SCALE_Y, 1);

        scaleSet.setDuration(200);
        scaleSet.setInterpolator(new LinearInterpolator());

        scaleSet.playTogether(xScaleAnimator, yScaleAnimator);
        scaleSet.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.setVisibility(View.VISIBLE);
            }
        }, 200);

    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetWidth = v.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().width = 1;
        v.setVisibility(View.VISIBLE);
        android.view.animation.Animation a = new android.view.animation.Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v, boolean isDoImmediately) {
        final int initialWidth = v.getMeasuredWidth();

        android.view.animation.Animation a = new android.view.animation.Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };


        if (isDoImmediately) {
            a.setDuration(0);
        } else {
            // 1dp/ms
            a.setDuration((int) (initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        }
        v.startAnimation(a);
    }
}
