package com.example.nanorus.gmobytesttask.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nanorus.gmobytesttask.R;

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


    }


}
