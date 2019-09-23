package com.swapnil.mvvmstarterkit.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.ColorInt;

import com.swapnil.mvvmstarterkit.R;
import com.swapnil.mvvmstarterkit.utils.Constants;
import com.swapnil.mvvmstarterkit.utils.SharedPrefUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatFileHelper {


    private static final String TAG = "DatFileHelper";
    // Location of input ICC profile
    private static final String INPUT_ICC_FILENAME = "sRGBColorSpaceProfile.icm";
    private static final String WATERMARK_IMAGE_NAME = "waternark_logo.png";

    // Currently points to sRGB, a common RGB ICC profile

    // TODO: 2019-07-09 Change ICC Profile

    /**
     * for BetaBowa --> Use "NailbotBowa2.icm" + native-lib.cpp - scaling = 4
     *
     * for Beta 2.0 --> Use "NailbotX2modYscale.icm" + native-lib - scaling = 0
     */

    private static final String ADJUST_ICC_FILENAME_OLD = "nailbot1.icm";

    private static String ADJUST_ICC_FILENAME = "NailbotBowa2.icm";
   // private static final String ADJUST_ICC_FILENAME = "NailbotX2modYscale.icm";

    private static int scaling = 4;

    // Location of output ICC profile
// Currently points to RGB-IR, a CYMK ICC profile where the K values are set to zero (effictively, CMY)
    private static final String OUTPUT_ICC_FILENAME = "RGBIR.icc";

    // Location of RGB data
    //private static final String RGB_DATA_FILENAME = "rgb.dat";
    // Location of adjusted RGB data
    // private static final String ADJUST_DATA_FILENAME = "adjust.dat";
    // Location of CMYK data
    //private static final String CMYK_DATA_FILENAME = "cmyk.dat";
    // Location of halftoned data
    //private static final String HALFTONED_DATA_FILENAME = "halftoned.dat";
    // Location of staggered data
    //private static final String STAGGERED_DATA_FILENAME = "staggered.dat";

    //public static final String OUTPUT_COLUMN_DATA_FILENAME = "Final_" + System.currentTimeMillis() + ".dat";
    public static final String OUTPUT_COLUMN_DATA_FILENAME = "Final.dat";
    private static final String SAMPLE_BMP_FILENAME = "";
    static int BI_HEIGHT = 320;                  /* Image height in bytes */
    static int BI_WIDTH = 320;                   /* Image width in bytes */

    Context context;
    Bitmap selectedOriginalBitmap;
    Bitmap resizedBitmap;

    public DatFileHelper(Context context, Bitmap selectedOriginalBitmap, String sizePosition) {
        this.context = context;
        this.selectedOriginalBitmap = selectedOriginalBitmap;
        //  resizedBitmap = getResizedBitmap();
        Bitmap flipped = verticalFlip(selectedOriginalBitmap);
        resizedBitmap = BitmapAlignmentHelper.getAlignedBitmap(sizePosition, flipped);
    }


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public boolean createNailbotDatFile() {
        String stsMsg = "Processing....";

        /**
         * select icc profile according  to user's selection
         *
         * set scaling in native Lib at time of final release
         */

        /**
         * for BetaBowa --> Use "NailbotBowa2.icm" + native-lib.cpp - scaling = 4
         *
         * for Beta 2.0 --> Use "NailbotX2modYscale.icm" + native-lib - scaling = 0
         */

        if(SharedPrefUtils.getBoolean(context, Constants.SPK_INK_SETTING, Constants.INK_DEFAULT))
        {
            ADJUST_ICC_FILENAME =  "NailbotBowa2.icm";
            scaling = 4;
        }else
        {
            ADJUST_ICC_FILENAME = "NailbotX2modYscale.icm";
            scaling=0;
        }

        try {

            if (copyICCAssetsToStorage(WATERMARK_IMAGE_NAME) != null &&
                    copyICCAssetsToStorage(INPUT_ICC_FILENAME) != null &&
                    copyICCAssetsToStorage(ADJUST_ICC_FILENAME) != null &&
                    copyICCAssetsToStorage(OUTPUT_ICC_FILENAME) != null) {
                //create debug files...!!
                if (createDatFileInStorage(OUTPUT_COLUMN_DATA_FILENAME) != null) {

                    // if (copyBitmapFileToStorage(bitmap, SAMPLE_BMP_FILENAME) != null) {
                    //if (copyICCAssetsToStorage(SAMPLE_BMP_FILENAME) != null) {
                    if (createFinalNailbotColumnData(getAbsolutePathOfFile(INPUT_ICC_FILENAME),
                            getAbsolutePathOfFile(ADJUST_ICC_FILENAME),
                            getAbsolutePathOfFile(OUTPUT_ICC_FILENAME),
                            getAbsolutePathOfFile("dat/" + OUTPUT_COLUMN_DATA_FILENAME),
                            rgbValuesFromBitmap(resizedBitmap),scaling)
                            .equalsIgnoreCase("success")) {

                        stsMsg = "Image pipeline executed successfully";
                        return true;

                    } else {
                        stsMsg = "Something went wrong in C code";
                    }

                } else {
                    stsMsg = "Error while reading dat files";
                }

            } else {
                stsMsg = "Error while reading ICC profile files.";
            }


        } catch (UnsatisfiedLinkError | Exception e) {
            // TODO: 02/01/19 Possible cause of error - In c code check that function name has exact same path of DatFileHelper class..!!!
            stsMsg = "Can't process file conversion";

        }
        Toast.makeText(context, stsMsg, Toast.LENGTH_SHORT).show();
        return false;
    }


    public String copyICCAssetsToStorage(String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            File outFile = createFileInStorage(filename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            LogHelper.printLog(TAG, "copyICCAssetsToStorage: fileName: " + outFile.getAbsolutePath());
            return outFile.getAbsolutePath();
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
            e.printStackTrace();
        }
        return null;

    }

    public String getAbsolutePathOfFile(String filename) {
        //String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPrintFolderPath() + File.separator; // "/Nailbot/.print/";
        String outDir = context.getExternalFilesDir(null) + File.separator + getPrintFolderPath() + File.separator; // "/Nailbot/.print/";
        File outFile = new File(outDir, filename);
        //Log.d(TAG, "getAbsolutePathOfFile: "+outFile);
        return outFile.getAbsolutePath();
    }

    private String getPrintFolderPath() {
        return context.getString(R.string.app_name) + File.separator + ".print";
    }

    private File createFileInStorage(String filename) {
        //context.getExternalFilesDir(
        //            Environment.DIRECTORY_PICTURES)
        try {
            File root = new File(context.getExternalFilesDir(null), getPrintFolderPath());
            //File root = new File(Environment.getExternalStorageDirectory(), getPrintFolderPath());
            if (!root.exists()) {
                root.mkdirs();
            }

            File outFile = new File(root, filename);
            if (!outFile.exists()) {
                root.createNewFile();
            }

            //Log.d(TAG, "createFileInStorage: outFile- "+outFile);
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private File createDatFileInStorage(String filename) {
        try {
            File root = new File(context.getExternalFilesDir(null), getPrintFolderPath() + File.separator + "dat");
            //File root = new File(Environment.getExternalStorageDirectory(), getPrintFolderPath() + File.separator + "dat");
            if (!root.exists()) {
                root.mkdirs();
            }

            File outFile = new File(root, filename);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            Log.d(TAG, "createDatFileInStorage: " + outFile);
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    private static byte[] rgbValuesFromBitmap(Bitmap bitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Bitmap argbBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        argbBitmap.eraseColor(Color.WHITE);

        Canvas canvas = new Canvas(argbBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int componentsPerPixel = 3;
        int totalPixels = width * height;
        int totalBytes = totalPixels * componentsPerPixel;


        byte[] rgbValues = new byte[totalBytes];
        @ColorInt int[] argbPixels = new int[totalPixels];
        argbBitmap.getPixels(argbPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < totalPixels; i++) {
             /*//if (argbPixels[i] == 0xFFffffff) {
           if (argbPixels[i] == Color.WHITE) {
                // We'll set the alpha value to 0 for to make it fully transparent.
                argbPixels[i] = Color.TRANSPARENT;
            }*/
            @ColorInt int argbPixel = argbPixels[i];

            int red = Color.red(argbPixel);
            int green = Color.green(argbPixel);
            int blue = Color.blue(argbPixel);
            int alpha = Color.alpha(argbPixel);

            rgbValues[i * componentsPerPixel] = (byte) blue;
            rgbValues[i * componentsPerPixel + 1] = (byte) green;
            rgbValues[i * componentsPerPixel + 2] = (byte) red;
        }
        LogHelper.printLog(TAG, "rgbValuesFromBitmap: width- " + width + " , height- " + height + " rgbValues size: " + rgbValues.length);

        return rgbValues;
    }


    private Bitmap verticalFlip(Bitmap bInput) {
        Bitmap bOutput;
        boolean isFlipped = SharedPrefUtils.getBoolean(context, Constants.SPK_NAIL_SETTING, Constants.SPV_IS_FLIPPED);
        Matrix matrix = new Matrix();

        if (isFlipped) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            matrix.preScale(1.0f, -1.0f);

        }
        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);

        return bOutput;

    }

    private Bitmap verticalFlip_Mirror(Bitmap bInput) {
        Bitmap bOutput;
        boolean isFlipped = SharedPrefUtils.getBoolean(context, Constants.SPK_NAIL_SETTING, Constants.SPV_IS_FLIPPED);

        if (isFlipped) {
            bOutput = bInput;
            //dont do anything as by default image is flipped...!!
        } else {

            Matrix matrix = new Matrix();
            matrix.preScale(1.0f, -1.0f);
            bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);

        }
        return bOutput;

    }


    private Bitmap horizontalFlip(Bitmap bInput) {
        Bitmap bOutput;
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);

        return bOutput;
    }


    // TODO: 21/12/18 - IMPORTANT -  this method name is native-c class should have exact same Name of activity including path..!!
    //this method is defined in  native-lib
    public native String createFinalNailbotColumnData(String INPUT_ICC_FILENAME,
                                                      String ADJUST_ICC_FILENAME,
                                                      String OUTPUT_ICC_FILENAME,
                                                      String OUTPUT_COLUMN_DATA_FILENAME,
                                                      byte[] RGBVALUES,int INK_SCALING);


}
