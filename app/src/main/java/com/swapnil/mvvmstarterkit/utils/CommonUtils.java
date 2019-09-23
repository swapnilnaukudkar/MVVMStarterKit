package com.swapnil.mvvmstarterkit.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import com.swapnil.mvvmstarterkit.R;
import com.swapnil.mvvmstarterkit.base.BaseActivity;
import com.swapnil.mvvmstarterkit.custom.AlertDialogButtonsClickListener;
import com.swapnil.mvvmstarterkit.helper.LogHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by pravin.gaikwad on 20/02/18.
 */

public class CommonUtils {
    private static final String TAG = "CommonUtils";
    public static HashMap<Integer, Boolean> hmIsFavorite = new HashMap<>();


    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static void vibrateDevice(Context context, int durationInMillisec) {
        Vibrator vibrateTouch = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrateTouch != null) {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                vibrateTouch.vibrate(VibrationEffect.createOneShot(100,
                        VibrationEffect.DEFAULT_AMPLITUDE));

            } else {
                //deprecated in API 26
                vibrateTouch.vibrate(durationInMillisec);
            }
        }
    }



    public static int byteSizeOf(Bitmap bitmap) {
        try {
            int bitmapSize = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                bitmapSize = bitmap.getAllocationByteCount();
            } else {
                bitmapSize = bitmap.getByteCount();
            }
            LogHelper.printLog(TAG, "byteSizeOf: :" + bitmap);
            return bitmapSize;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;


    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {

        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);


    }

    public static boolean isNumber(String data) {
        return data.matches("\\d+(?:\\.\\d+)?");


    }



    public static void openPhoneDialer(Context context, String nutritionistNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + nutritionistNumber));
        context.startActivity(intent);
    }

  /*  public static void addClickEffectToImage(ImageView imageView) {
        //set the ontouch listener
        imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
    }*/

    /*public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }*/

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void hideKeypad(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }


    /**
     * @param activity - use this if you want show keyboard from whole activity and view is sure
     */
    public static void hideKeypad(Activity activity) {
        try {
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                view = new View(activity);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }


    public static void showCommonAlertDialog(final Context context,
                                             String title, String msg,
                                             String negativeButtonText,
                                             String positiveButtonText,
                                             final AlertDialogButtonsClickListener alertDialogButtonsClickListener,
                                             boolean isCancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!title.isEmpty()) {
            builder.setTitle(title);
        }
        if (msg != null && msg.length() != 0) {
            builder.setMessage(msg);
        }

        builder.setCancelable(isCancellable);

        if (negativeButtonText.length() > 0) {
            builder.setNegativeButton(negativeButtonText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialogButtonsClickListener.onAlertDialogButtonClick(/*type,*/ false);
                            dialog.dismiss();
                        }
                    });
        }


        if (positiveButtonText.length() > 0) {
            builder.setPositiveButton(positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialogButtonsClickListener.onAlertDialogButtonClick(/*type,*/ true);
                            dialog.dismiss();
                        }
                    });
        }


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public static Calendar getDefaultCalendar(final TextView tvDate, final TextView tvMonth, final TextView tvYear) {
        Calendar newCalendar = Calendar.getInstance();
        String defaultDate = tvDate.getText().toString();
        String defaultMonth = tvMonth.getText().toString();
        String defaultYear = tvYear.getText().toString();

        LogHelper.printLog(TAG, "getDefaultCalendar: defaultDate: " + defaultDate + " , defaultMonth: " + defaultMonth + " ,defaultYear: " + defaultYear);

        if (!defaultDate.isEmpty() && isNumber(defaultDate)) {
            newCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(defaultDate));
        }

        if (!defaultMonth.isEmpty() && isNumber(defaultMonth)) {
            newCalendar.set(Calendar.MONTH, Integer.parseInt(defaultMonth) - 1);
        }

        if (!defaultYear.isEmpty() && isNumber(defaultYear)) {
            newCalendar.set(Calendar.YEAR, Integer.parseInt(defaultYear));
        }

        LogHelper.printLog(TAG, "getDefaultCalendar: newCalendar: " + newCalendar.getTime());

        return newCalendar;
    }

    /* public static void showSpinnerCalendarPopup(final Context context, final TextView textView,
                                                 int dateRange, long maxDate, long minDate,
                                                 String pattern) {
         //<0 is pastDatesOnly
         //0 is all dates
         //>0 is future dates only

         final Calendar newDate = Calendar.getInstance();
         System.out.println(newDate.getTimeInMillis());

         final SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern, Locale.US);
         final Calendar newCalendar = Calendar.getInstance();

         DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                 R.style.CustomDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

             @Override
             public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                 newDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                 String dateS = dateFormatter.format(newDate.getTime());
                 textView.setText(dateS);
             }

         }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
         if (dateRange < 0) {
             datePickerDialog.getDatePicker().setMaxDate(maxDate);
         } else if (dateRange == 0) {
             datePickerDialog.getDatePicker();
         } else {
             //>0
             datePickerDialog.getDatePicker().setMinDate(minDate);
         }
         datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         datePickerDialog.getDatePicker().setSpinnersShown(true);
         datePickerDialog.getDatePicker().setCalendarViewShown(false);
         datePickerDialog.setCanceledOnTouchOutside(true);
         datePickerDialog.show();
     }
 */
    public static AlertDialog showCustomDialog(final Context context, View promptsView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(promptsView);

        AlertDialog dialog = builder.create();
        if (promptsView.getParent() != null) {
            ((ViewGroup) promptsView.getParent()).removeView(promptsView);
            builder.setView(promptsView);
        }

        // display dialog
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        return dialog;
    }

    public static void setupToolbarForActivity(BaseActivity baseActivity, Toolbar toolbar,
                                               String title, String subtitle, boolean isRightMenuItemAvailable) {
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        TextView tvToolbarRightOption = toolbar.findViewById(R.id.tv_right_option);
        if (isRightMenuItemAvailable && subtitle != null) {
            tvToolbarRightOption.setText(subtitle);
        }
        tvToolbarTitle.setText(title);
        baseActivity.setSupportActionBar(toolbar);
        if (baseActivity.getSupportActionBar() != null) {
           // baseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          /*  Drawable drawable = toolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(
                    baseActivity, R.color.white), PorterDuff.Mode.SRC_ATOP);*/
        }
        //  CommonUtils.centerAlignToolbarTitle(baseActivity, tvToolbarTitle, isRightMenuItemAvailable);
    }

    public static void setupToolbarForWithColorActivity(BaseActivity baseActivity, Toolbar toolbar,
                                                        String title, String subtitle, boolean isRightMenuItemAvailable) {
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        TextView tvToolbarRightOption = toolbar.findViewById(R.id.tv_right_option);
        if (isRightMenuItemAvailable && subtitle != null) {
            tvToolbarRightOption.setText(subtitle);
            tvToolbarRightOption.setTextColor(baseActivity.getResources().getColor(R.color.colorPrimary));
        }
        tvToolbarTitle.setText(title);
        baseActivity.setSupportActionBar(toolbar);
        if (baseActivity.getSupportActionBar() != null && title.equals("Print")) {
         //   baseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void setupToolbarForActivity(BaseActivity baseActivity, Toolbar toolbar,
                                               String title, String rightText, String leftText,
                                               boolean isRightMenuItemAvailable, boolean isLeftMenuItemAvailable) {
        TextView tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        TextView tvToolbarRightOption = toolbar.findViewById(R.id.tv_right_option);
        TextView tvToolbarLeftOption = toolbar.findViewById(R.id.tv_left_option);
        if (isRightMenuItemAvailable && rightText != null) {
            tvToolbarRightOption.setText(rightText);
        }
        if (isLeftMenuItemAvailable && leftText != null) {
            tvToolbarLeftOption.setText(leftText);
            tvToolbarLeftOption.setVisibility(View.VISIBLE);
        }
        tvToolbarTitle.setText(title);
        baseActivity.setSupportActionBar(toolbar);
        if (baseActivity.getSupportActionBar() != null) {
           // baseActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          /*  Drawable drawable = toolbar.getNavigationIcon();
            drawable.setColorFilter(ContextCompat.getColor(
                    baseActivity, R.color.white), PorterDuff.Mode.SRC_ATOP);*/
        }
        CommonUtils.centerAlignToolbarTitle(baseActivity, tvToolbarTitle, isRightMenuItemAvailable);
    }


    public static void centerAlignToolbarTitle(Context context, TextView tvtoolbarTitle, boolean isRightMenuItemAvailable) {

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tvtoolbarTitle.getLayoutParams();
        if (isRightMenuItemAvailable) {
            lp.setMargins(0, 0, 0, 0);
        } else {
            //calcFragment
            lp.setMargins(0, 0, ScreenUtils.getActionBarSizeInPx(context), 0);
        }
        tvtoolbarTitle.setLayoutParams(lp);
    }

    public static void setupToolbarForFragment(FragmentActivity baseActivity, String title,
                                               boolean isRightMenuItemAvailable) {
        TextView tvToolbarTitle = baseActivity.findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
        CommonUtils.centerAlignToolbarTitle(baseActivity, tvToolbarTitle,
                isRightMenuItemAvailable);
        tvToolbarTitle.setVisibility(View.VISIBLE);
    }

    public static void viewClickEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }


    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public static void showCalendarPopup(final Context context,
                                         final TextView textView,
                                         int dateRange, long maxDate,
                                         long minDate, String pattern) {
        //<0 is pastDatesOnly
        //0 is all dates
        //>0 is future dates only
        hideKeypad(context, textView);


        final Calendar newDate = Calendar.getInstance();

        System.out.println(newDate.getTimeInMillis());
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern, Locale.US);
        final Calendar newCalendar = Calendar.getInstance();

        if (textView != null && !textView.getText().toString().isEmpty()) {
            //get the date using simple date format
            try {
                newCalendar.setTime(dateFormatter.parse(textView.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                String dateS = dateFormatter.format(newDate.getTime());
                textView.setText(dateS);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        if (dateRange < 0) {
            datePickerDialog.getDatePicker().setMaxDate(maxDate);
        } else if (dateRange == 0) {
            datePickerDialog.getDatePicker();
        } else {
            datePickerDialog.getDatePicker().setMinDate(minDate);
        }

        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.show();
    }

    public static String convertToCamelCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder converted = new StringBuilder();
        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
        LogHelper.printLog(TAG, "convertToCamelCase: " + converted.toString());
        return converted.toString();
    }

    public static void openAppInfoScreen(Context context) {

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            //Open the generic Apps page:
            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);

        }
    }


}

