package com.swapnil.mvvmstarterkit.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.widget.ImageView;

public class BitmapAlignmentHelper {
    private static final String TAG = "BitmapAlignmentHelper";
    static int frontImageSize = DatFileHelper.BI_HEIGHT;
    static int imageHeightWidth = 320;
    static int smallImageSize = 140;
    static int mediumImageSize = 210;
    static int largeImageSize = 240;

    static int marginLeft = 0;
    static int marginTop = 0;
    static Bitmap scaled;


    public static Bitmap getAlignedBitmap(String alignment, Bitmap bmOriginal) {
        //top and bottom logic is reversed as printer always print the exact mirror of final image.
        switch (alignment) {
            case "SmallBottom":
                //rescale the bitmap to small first...!!!
                marginLeft = (frontImageSize / 2) - (smallImageSize / 2);
                marginTop = 0;
                break;

            case "MediumBottom":
                marginLeft = (frontImageSize / 2) - (mediumImageSize / 2);
                marginTop = 0;
                break;

            case "LargeBottom":
                marginLeft = (frontImageSize / 2) - (largeImageSize / 2);
                marginTop = 0;
                break;


            case "SmallCenter":
                marginLeft = (frontImageSize / 2) - (smallImageSize / 2);
                marginTop = (frontImageSize / 2) - (smallImageSize / 2);
                break;

            case "MediumCenter":
                marginLeft = (frontImageSize / 2) - (mediumImageSize / 2);
                marginTop = (frontImageSize / 2) - (mediumImageSize / 2);
                break;

            case "LargeCenter":
                marginLeft = (frontImageSize / 2) - (largeImageSize / 2);
                marginTop = (frontImageSize / 2) - (largeImageSize / 2);
                break;


            case "SmallTop":
                marginLeft = (frontImageSize / 2) - (smallImageSize / 2);
                marginTop = frontImageSize - smallImageSize;
                break;

            case "MediumTop":
                marginLeft = (frontImageSize / 2) - (mediumImageSize / 2);
                marginTop = frontImageSize - mediumImageSize;
                break;

            case "LargeTop":
                marginLeft = (frontImageSize / 2) - (largeImageSize / 2);
                marginTop = frontImageSize - largeImageSize;
                break;

            default:
                break;
        }

        LogHelper.printLog(TAG, "getAlignedBitmap()" +
                " alignment = [" + alignment + "]" +
                ", marginLeft = [" + marginLeft + "]" +
                ", marginTop = [" + marginTop + "]" +
                ", marginTop = [" + marginTop + "]");

        if (alignment.contains("Small")) {
            scaled = ThumbnailUtils.extractThumbnail(bmOriginal, smallImageSize, smallImageSize);
        } else if (alignment.contains("Medium")) {
            scaled = ThumbnailUtils.extractThumbnail(bmOriginal, mediumImageSize, mediumImageSize);
        } else {
            scaled = ThumbnailUtils.extractThumbnail(bmOriginal, largeImageSize, largeImageSize);

        }

        return mergeBitmaps(scaled, getEmptyBitmap(), marginLeft, marginTop);

    }

    public static Bitmap mergeBitmaps(Bitmap bitmap1, Bitmap bitmap2,
                                      int marginLeft, int marginTop) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();

        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();


        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap2Width, bitmap2Height, bitmap2.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, marginLeft, marginTop, null);
        canvas.drawBitmap(bitmap2, new Matrix(), null);
        return overlayBitmap;
    }

    public static Bitmap getEmptyBitmap() {
        int w = DatFileHelper.BI_WIDTH, h = DatFileHelper.BI_HEIGHT;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(bmp);
        return bmp;
    }

    public static Bitmap overlayBitmapToCenter(Bitmap bitmap1, Bitmap bitmap2) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();


        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);

        Bitmap overlayBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, marginLeft, marginTop, null);
        return overlayBitmap;
    }

    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 10, 10, null);
        return result;
    }

    public static Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage, ImageView secondImageView) {

        Bitmap result = Bitmap.createBitmap(firstImage.getWidth(), firstImage.getHeight(), firstImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, secondImageView.getX(), secondImageView.getY(), null);

        return result;
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}
