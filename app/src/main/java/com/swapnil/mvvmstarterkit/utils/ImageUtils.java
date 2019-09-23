package com.swapnil.mvvmstarterkit.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.swapnil.mvvmstarterkit.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    public static void loadImage(final Context context, final String imageURL, final ImageView imageView) {
        Picasso.get()
                .load(Uri.parse(imageURL))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
//                        Picasso.get()
//                                .load(Uri.parse(imageURL))
//                                .placeholder(R.drawable.bg_rect_green)
//                                .error(R.drawable.bg_rect_grey)
//                                .into(imageView);
                    }
                });
    }


    /*new tested code...*/
    public static Bitmap getBitmap(String photoPath) {

        Bitmap b = null;
        FileInputStream fis = null;

        try {
            final ExifInterface exifInterface = new ExifInterface(photoPath);
            final int currentRotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            final Matrix matrix = new Matrix();
            switch (currentRotation) {
                case ExifInterface.ORIENTATION_NORMAL: {
                    matrix.postRotate(0);
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    matrix.postRotate(90);
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_180: {
                    matrix.postRotate(180);
                    break;
                }
                case ExifInterface.ORIENTATION_ROTATE_270: {
                    matrix.postRotate(270);
                    break;
                }
            }

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            // bmOptions.inJustDecodeBounds = true;
            //BitmapFactory.decodeFile(photoPath, bmOptions);

            //int photoW = bmOptions.outWidth;
            //int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            //int scaleFactor = Math.min(photoW/mWidth, photoH/mHeight);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            //bmOptions.inSampleSize = calculateInSampleSize(bmOptions, 800, 800);
            bmOptions.inSampleSize = 2;

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap;

        } catch (Exception e) {
            return null;
        }

    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //QQ0

        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        if (path == null) {
            path = "";
        }
        return Uri.parse(path);
    }

    public static Bitmap convertBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Bitmap generateCircularBitmap(Bitmap input) {

        Bitmap output = Bitmap.createBitmap(input.getWidth(),
                input.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, input.getWidth(), input.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(input.getWidth() / 2, input.getHeight() / 2,
                input.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, rect, rect, paint);

        return output;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            return pictureFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return null;
        }
    }

    private static File getOutputMediaFile(){
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator
                + System.currentTimeMillis() + ".jpg");
    }

}
