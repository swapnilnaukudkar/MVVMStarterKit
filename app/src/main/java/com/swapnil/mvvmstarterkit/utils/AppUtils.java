package com.swapnil.mvvmstarterkit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public final class AppUtils {

    private AppUtils() {
        // This class is not publicly instantiable
    }

    public static void openPlayStoreForApp(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(/*context
                            .getResources()
                            .getString(R.string.app_market_link)*/ "" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(/*context
                            .getResources()
                            .getString(R.string.app_google_play_store_link)*/"" + appPackageName)));
        }
    }
    /*public static void changeStatusBarColor(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
    }*/

}
