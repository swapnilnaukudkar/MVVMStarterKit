package com.swapnil.mvvmstarterkit.helper;

import android.util.Log;

public class LogHelper {
    static boolean isLogEnabled = true;

    public static void printLog(String TAG, String msg) {
        if (isLogEnabled) {
            Log.d(TAG, msg);
        }
    }

    public static void debug(String TAG, String msg) {
        if (isLogEnabled) {
            Log.d(TAG, msg);
        }
    }

    public static void info(String TAG, String msg) {
        if (isLogEnabled) {
            Log.i(TAG, msg);
        }
    }

    public static void error(String TAG, String msg) {
        if (isLogEnabled) {
            Log.e(TAG, msg);
        }
    }

    public static void verbose(String TAG, String msg) {
        if (isLogEnabled) {
            Log.v(TAG, msg);
        }
    }


}
