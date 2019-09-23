package com.swapnil.mvvmstarterkit.utils;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.swapnil.mvvmstarterkit.R;

/**
 * Created by leftrightmind on 12/10/17.
 */

public class FragmentUtils {

    private static final String TAG = "FragmentUtils";


    public static void replaceFragmentInActivityByTag(@NonNull FragmentManager fragmentManager,
                                                      @NonNull Fragment fragment, int frameId, String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.add(frameId, fragment);
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

        transaction.replace(frameId, fragment, tag);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static void replaceFragmentInActivityByTagWithoutTransition(@NonNull FragmentManager fragmentManager,
                                                      @NonNull Fragment fragment, int frameId, String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.add(frameId, fragment);
       // transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);

        transaction.replace(frameId, fragment, tag);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }


}
