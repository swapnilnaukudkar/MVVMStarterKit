package com.swapnil.mvvmstarterkit.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.swapnil.mvvmstarterkit.R;
import com.swapnil.mvvmstarterkit.custom.NetStateInterface;
import com.swapnil.mvvmstarterkit.helper.LogHelper;
import com.swapnil.mvvmstarterkit.utils.CommonUtils;

import com.swapnil.mvvmstarterkit.utils.NetworkUtils;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity implements BaseFragment.Callback, NetStateInterface {
    private static final String TAG = "BaseActivity";


    private ProgressDialog mProgressDialog;

    private static final String MSG_NET_OFFLINE = "The internet connection appears to be offline.";
    private static final String MSG_NET_ONLINE = "Connected to internet";
    private static InputFilter filter;

    // private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    /* Display display;
     int rotation ;
     Point size = new Point();
     int lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                LogHelper.printLog(TAG, "filter() called with: source = [" + source + "], start = [" + start + "], end = [" + end + "], dest = [" + dest + "], dstart = [" + dstart + "], dend = [" + dend + "]");
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_blue_grey));
        }
    }

    /**
     * this method will used to default toolbar or any other intialisations or setup
     */
    protected abstract void setUp();

    public static InputFilter getFilter() {
        return filter;
    }

    public void setFilter(InputFilter filter) {
        this.filter = filter;
    }

    //incase any class needed activity componenet


    //permission related
    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void showSnackBar(String message) {
        LogHelper.printLog(TAG, "showSnackBar() called with: message = [" + message + "]");
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        if (message.equals(MSG_NET_OFFLINE)) {
            sbView.setBackground(getResources().getDrawable(android.R.color.holo_red_dark));
        } else if (message.equals(MSG_NET_ONLINE)) {
            sbView.setBackground(getResources().getDrawable(android.R.color.holo_green_dark));
        }

        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackbar.show();
    }


    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }




    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //below this all are MvpView methods...

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }


    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    public void onError(String message) {
        if (message != null) {
            // showSnackBar(message);
        } else {
            //showSnackBar(getString(R.string.err_some_error));
        }
    }


    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }


    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.err_some_error), Toast.LENGTH_SHORT).show();
        }
    }


    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }


    public void hideKeyboard() {


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }



    public Context getContext() {
        return this;
    }

    @Override
    public void updateUIOnNetChange(boolean isNetAvaialble) {

        LogHelper.printLog(TAG, "updateUIOnNetChange() NetworkSts :" + isNetAvaialble + ":::: getConnectivityStatus: " + NetworkUtils.getConnectivityStatus(BaseActivity.this));

        if (isNetAvaialble) {
            //show green colored network connected snackbar...
            //showSnackBar(MSG_NET_ONLINE);

        } else {
            // showSnackBar(MSG_NET_OFFLINE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
