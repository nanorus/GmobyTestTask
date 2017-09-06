package com.example.nanorus.gmobytesttask.image;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity_image_list_rv_gallery.getContext(),
                layoutManager.getOrientation());
        activity_image_list_rv_gallery.addItemDecoration(dividerItemDecoration);
        urls = new ArrayList<>();
        urls.add("http://corochann.com/wp-content/uploads/2015/07/CARD_TYPE_INFO_OVER-2015-07-02.png");
        urls.add("https://sevia.ru/foto/foto-na-avu/foto-na-avu-08.jpg");
        urls.add("http://elitefon.ru/images/201503/elitefon.ru_38831.jpg");
        urls.add("https://dreem-pics.com/uploads/posts/2017-03/1490573950_2.jpg");
        urls.add("https://i.pinimg.com/236x/bf/16/be/bf16be5cafe96d30f98ee31941672949--psychedelic.jpg");
        urls.add("http://corochann.com/wp-content/uploads/2015/09/search3-2015-09-02-210633.png");
        urls.add("https://i.mycdn.me/image?id=835935809347&t=56&plc=WEB&tkn=*3jCIbtmvfLpP5zr_Y90jlTnpR0Y");
        urls.add("https://dreem-pics.com/uploads/posts/2017-02/1487294016_2.jpg");
        urls.add("https://i.stack.imgur.com/uPzIK.png");
        urls.add("http://pm1.narvii.com/6452/233cb2a69c6f95af9ad2a59d1f28b7ffab216d12_hq.jpg");
        urls.add("http://i47.ltalk.ru/17/20/102017/27/3210327/1226947558_080126hororingosh8.gif");
        urls.add("http://s57.radikal.ru/i157/1205/be/09afc8ffbea1.jpg");
        urls.add("http://www.gameforgirl.ru/thumb/gfa-072.png");
        urls.add("https://static.pexels.com/photos/207962/pexels-photo-207962.jpeg");
        urls.add("https://static.pexels.com/photos/46710/pexels-photo-46710.jpeg");
        urls.add("https://static.pexels.com/photos/132037/pexels-photo-132037.jpeg");
        urls.add("https://static.pexels.com/photos/196667/pexels-photo-196667.jpeg");
        urls.add("http://www.istockphoto.com/resources/images/PhotoFTLP/img_67920257.jpg");
        urls.add("https://iso.500px.com/wp-content/uploads/2016/06/stock-photo-142869191-1-1500x1000.jpg");
        urls.add("https://static.pexels.com/photos/338936/pexels-photo-338936.jpeg");
        urls.add("https://lh4.googleusercontent.com/-OowXWkgMSHI/AAAAAAAAAAI/AAAAAAAAANE/rOf2DCA2AXo/photo.jpg");
        urls.add("https://static.pexels.com/photos/272178/pexels-photo-272178.jpeg");

        imagesAdapter = new ImagesAdapter(urls, false);
        activity_image_list_rv_gallery.setLayoutManager(layoutManager);
        activity_image_list_rv_gallery.setAdapter(imagesAdapter);
    }

}
