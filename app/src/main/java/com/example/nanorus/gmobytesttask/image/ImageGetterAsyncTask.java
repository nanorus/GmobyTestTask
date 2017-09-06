package com.example.nanorus.gmobytesttask.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageGetterAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private HttpURLConnection mConnection;
    private BufferedInputStream mBufStream;
    private ImageView mImageView;
    private int mWidthPx = 0;
    private int mHeightPx = 0;

    public ImageGetterAsyncTask(ImageView imageView) {
        mImageView = imageView;
        mWidthPx = mImageView.getLayoutParams().width;
        mHeightPx = mImageView.getLayoutParams().height;
    }


    private Bitmap downloadImage(String url) {
        if (mConnection != null) {
            mConnection.disconnect();
        }
        if (mBufStream != null) {
            try {
                mBufStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap image = null;
        mConnection = null;
        mBufStream = null;
        try {
            mConnection = (HttpURLConnection) new URL(url).openConnection();
            mConnection.setDoInput(true);
            // mConnection.setRequestProperty("Connection", "Keep-Alive");
            mConnection.connect();
            if (!isCancelled())
                mBufStream = new BufferedInputStream(mConnection.getInputStream(), 8192);
            if (!isCancelled())
                image = BitmapFactory.decodeStream(mBufStream);

            mBufStream.close();
            mConnection.disconnect();
            mBufStream = null;
            mConnection = null;

        } catch (IOException ignored) {
        } catch (OutOfMemoryError e) {
            return null;
        } finally {
            if (mBufStream != null)
                try {
                    mBufStream.close();
                } catch (IOException ignored) {
                }
            if (mConnection != null)
                mConnection.disconnect();
        }
        return image;
    }

    private void cancelDownloading() {
        if (mConnection != null) {
            mConnection.disconnect();
            if (mBufStream != null) {
                try {
                    mBufStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mConnection = null;
        mBufStream = null;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];

        ImageManager imageManager = new ImageManager();
        ImageMapper imageMapper = new ImageMapper();
        Bitmap cachedImage;
        Bitmap onlineImage;
        Bitmap finalImage = null;

        cachedImage = imageManager.loadImageFromCache(url);
        if (cachedImage == null) {
            //System.out.println("task: no cache");
            // online
            if (!isCancelled()) {
                //System.out.println("task: download");
                onlineImage = downloadImage(url);
                finalImage = imageMapper.reduceImage(onlineImage, mWidthPx, mHeightPx);
                if (!isCancelled())
                    imageManager.saveImageToCache(url, finalImage);
                //System.out.println("task: online loaded");
            }
        } else {
            //System.out.println("task: cache loaded");
            // offline
            finalImage = cachedImage;
        }
        return finalImage;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCancelled() {
        //System.out.println("task: onCancelled");
        super.onCancelled();
        cancelDownloading();
    }
}
