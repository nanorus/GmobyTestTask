package com.example.nanorus.gmobytesttask.image;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.view.ui.adapter.ImagesAdapter;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView activity_image_list_rv_gallery;
    SwipeRefreshLayout activity_image_list_swipe_refresh;

    ArrayList<String> urls;
    ImagesAdapter imagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        activity_image_list_rv_gallery = (RecyclerView) findViewById(R.id.activity_image_list_rv_gallery);
        activity_image_list_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.activity_image_list_swipe_refresh);
        activity_image_list_swipe_refresh.setOnRefreshListener(() -> {
            imagesAdapter = new ImagesAdapter(urls, true);
            activity_image_list_rv_gallery.setAdapter(imagesAdapter);
            if (activity_image_list_swipe_refresh.isRefreshing())
                activity_image_list_swipe_refresh.setRefreshing(false);
        });


        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        urls = new ArrayList<>();
        urls.add("https://scontent-lga3-1.cdninstagram.com/t51.2885-19/s150x150/20838631_125433344756435_2081451927438098432_a.jpg");
        urls.add("https://sevia.ru/foto/foto-na-avu/foto-na-avu-08.jpg");
        urls.add("http://elitefon.ru/images/201503/elitefon.ru_38831.jpg");
        urls.add("https://dreem-pics.com/uploads/posts/2017-03/1490573950_2.jpg");
        urls.add("https://i.pinimg.com/236x/bf/16/be/bf16be5cafe96d30f98ee31941672949--psychedelic.jpg");
        urls.add("http://pristor.ru/wp-content/uploads/2017/03/%D0%A1%D0%BC%D0%B5%D1%88%D0%BD%D1%8B%D0%B5-%D1%84%D0%BE%D1%82%D0%BA%D0%B8-%D0%BD%D0%B0-%D0%B0%D0%B2%D1%83-%D0%BA%D1%80%D1%83%D1%82%D1%8B%D0%B5-%D0%BF%D1%80%D0%B8%D0%BA%D0%BE%D0%BB%D1%8C%D0%BD%D1%8B%D0%B5-%D0%BA%D0%BB%D0%B0%D1%81%D1%81%D0%BD%D1%8B%D0%B5-%D0%B2%D0%B5%D1%81%D0%B5%D0%BB%D1%8B%D0%B5-7.jpg");
        urls.add("https://i.mycdn.me/image?id=835935809347&t=56&plc=WEB&tkn=*3jCIbtmvfLpP5zr_Y90jlTnpR0Y");
        urls.add("https://dreem-pics.com/uploads/posts/2017-02/1487294016_2.jpg");
        urls.add("http://pristor.ru/wp-content/uploads/2017/03/%D0%9A%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B8-%D0%BD%D0%B0-%D0%B0%D0%B2%D1%83-%D1%81-%D0%BD%D0%B0%D0%B4%D0%BF%D0%B8%D1%81%D1%8F%D0%BC%D0%B8-%D1%81%D0%BE-%D1%81%D0%BC%D1%8B%D1%81%D0%BB%D0%BE%D0%BC-%D0%BA%D1%80%D0%B0%D1%81%D0%B8%D0%B2%D1%8B%D0%B5-%D0%BA%D0%BB%D0%B0%D1%81%D1%81%D0%BD%D1%8B%D0%B5-%D0%BA%D1%80%D1%83%D1%82%D1%8B%D0%B5-15.jpg");
        urls.add("http://pm1.narvii.com/6452/233cb2a69c6f95af9ad2a59d1f28b7ffab216d12_hq.jpg");
        urls.add("http://i47.ltalk.ru/17/20/102017/27/3210327/1226947558_080126hororingosh8.gif");
        urls.add("http://s57.radikal.ru/i157/1205/be/09afc8ffbea1.jpg");
        urls.add("http://pristor.ru/wp-content/uploads/2017/05/%D0%9A%D1%80%D0%B0%D1%81%D0%B8%D0%B2%D1%8B%D0%B5-%D0%B0%D0%BD%D0%B8%D0%BC%D0%B5-%D0%BA%D0%B0%D1%80%D1%82%D0%B8%D0%BD%D0%BA%D0%B8-%D0%BD%D0%B0-%D0%B0%D0%B2%D0%B0%D1%82%D0%B0%D1%80%D0%BA%D1%83-%D1%81%D0%BA%D0%B0%D1%87%D0%B0%D1%82%D1%8C-%D1%81%D0%BC%D0%BE%D1%82%D1%80%D0%B5%D1%82%D1%8C-%D0%B1%D0%B5%D1%81%D0%BF%D0%BB%D0%B0%D1%82%D0%BD%D0%BE-4.jpg");
        urls.add("http://www.gameforgirl.ru/thumb/gfa-072.png");
        urls.add("https://files4.adme.ru/files/news/part_79/793310/10092760-7f39e393d96eae85cc92d48a40450aca_970x-1000-8e173efc7b-1484579184.jpg");
        urls.add("https://photooboi.com.ua/uploads/images/360b360/6802.jpg");
        urls.add("http://www.vseznaika.org/wp-content/uploads/2016/03/pic-00892.jpg");

        imagesAdapter = new ImagesAdapter(urls, false);
        activity_image_list_rv_gallery.setLayoutManager(layoutManager);
        activity_image_list_rv_gallery.setAdapter(imagesAdapter);
    }
}
