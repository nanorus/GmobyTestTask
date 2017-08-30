package com.example.nanorus.gmobytesttask.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nanorus.gmobytesttask.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageActivity extends AppCompatActivity {
    private final static String TAG = "ImageManager";

    ImageView activity_image_iv_image;
    Button activity_image_btn_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        activity_image_iv_image = (ImageView) findViewById(R.id.activity_image_iv_image);
        activity_image_btn_list = (Button) findViewById(R.id.activity_image_btn_list);
        activity_image_btn_list.setOnClickListener(view -> startActivity(new Intent(this, ImageListActivity.class)));

        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(ImageActivity.this, "loading image", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                return downloadImage("http://elitefon.ru/images/201503/elitefon.ru_38831.jpg");
            }

            @Override
            protected void onPostExecute(Bitmap image) {
                activity_image_iv_image.setImageBitmap(image);
                Toast.makeText(ImageActivity.this, "image loaded", Toast.LENGTH_SHORT).show();
            }
        };
        asyncTask.execute();

    }


    private Bitmap downloadImage(String url) {

        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;

        try {
            Log.v(TAG, "Starting loading image by URL: " + url);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);

            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;

        } catch (MalformedURLException ex) {
            Log.e(TAG, "Url parsing was failed: " + url);
        } catch (IOException ex) {
            Log.d(TAG, url + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Out of memory!!!");
            return null;
        } finally {
            if (buf_stream != null)
                try {
                    buf_stream.close();
                } catch (IOException ex) {
                }
            if (conn != null)
                conn.disconnect();
        }
        return bitmap;

    }

}
