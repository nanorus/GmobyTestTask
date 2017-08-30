package com.example.nanorus.gmobytesttask.image;

import android.graphics.Bitmap;

public class ImageMapper {

    private static ImageMapper sInstance;

    public static ImageMapper getInstance() {
        if (sInstance == null) {
            sInstance = new ImageMapper();
        }
        return sInstance;
    }

    public Bitmap reduceImage(Bitmap originalImage, int widthPx, int heightPx) {
        Bitmap reduceImage = Bitmap.createScaledBitmap(originalImage, widthPx, heightPx, true);
        return reduceImage;
    }

}
