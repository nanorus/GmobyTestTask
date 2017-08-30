package com.example.nanorus.gmobytesttask.view.ui.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.image.ImageManager;
import com.example.nanorus.gmobytesttask.image.ImageMapper;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private List<String> urls;
    private ImageManager mImageManager;
    private ImageMapper mImageMapper;
    private AsyncTask<String, Void, Bitmap> mLoadAsyncTask;
    private boolean mIsSwipe;

    public ImagesAdapter(List<String> urls, boolean isSwipe) {
        this.urls = urls;
        mImageManager = new ImageManager();
        mImageMapper = new ImageMapper();
        mIsSwipe = isSwipe;
        if (mIsSwipe){
            mImageManager.clearCache();
        }
    }

    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, int position) {
        String url = urls.get(position);

        int widthPX = holder.mImageView.getLayoutParams().width;
        int heightPX = holder.mImageView.getLayoutParams().height;
        holder.image_list_item_tv_url.setText(String.valueOf(position));
        holder.mImageView.setImageResource(R.color.cardview_light_background);

        System.out.println("OnBindViewHolder");
        mLoadAsyncTask = new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                // load from cache
                System.out.println("load from cache");
                return mImageManager.loadImageFromCache(url);
            }

            @Override
            protected void onPostExecute(Bitmap cachedImage) {
                super.onPostExecute(cachedImage);
                if (cachedImage != null) {
                    // cache loaded
                    System.out.println("cache loaded");
                    holder.mImageView.setImageBitmap(cachedImage);

                } else {
                    // no cache
                    new AsyncTask<String, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... strings) {
                            String url = strings[0];
                            // download and save
                            System.out.println("download and save");
                            Bitmap image = downloadImage(url);
                            Bitmap reducedImage = mImageMapper.reduceImage(image, widthPX, heightPX);
                            mImageManager.saveImageToCache(url, reducedImage);
                            return reducedImage;
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmaps) {
                            super.onPostExecute(bitmaps);
                            System.out.println("set image");
                            holder.mImageView.setImageBitmap(bitmaps);
                        }
                    }.execute(url);

                }
            }
        };
        mLoadAsyncTask.execute(url);
    }

    @Override
    public int getItemCount() {
        if (urls != null)
            return urls.size();
        else
            return 0;
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView image_list_item_tv_url;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_list_item_image);
            image_list_item_tv_url = (TextView) itemView.findViewById(R.id.image_list_item_tv_url);
        }
    }

    private Bitmap downloadImage(String url) {
        return mImageManager.downloadImage(url);

    }


}
