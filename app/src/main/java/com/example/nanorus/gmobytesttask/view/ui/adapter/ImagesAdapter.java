package com.example.nanorus.gmobytesttask.view.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.image.ImageGetterAsyncTask;
import com.example.nanorus.gmobytesttask.image.ImageManager;
import com.example.nanorus.gmobytesttask.image.ImageMapper;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private List<String> urls;
    private ImageManager mImageManager;
    private ImageMapper mImageMapper;
    private boolean mIsSwipe;

    public ImagesAdapter(List<String> urls, boolean isSwipe) {
        this.urls = urls;
        mImageManager = new ImageManager();
        mImageMapper = new ImageMapper();
        mIsSwipe = isSwipe;
        if (mIsSwipe) {
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
        holder.mImageView.setImageResource(R.mipmap.ic_launcher_round);
        holder.image_list_item_tv_url.setText(String.valueOf(position));
        if (holder.mImageGetterAsyncTask != null) {
            //System.out.println("adapter: pos " + position + ": cancel asynctask");
            holder.mImageGetterAsyncTask.cancel(true);
        }
        //System.out.println("adapter: pos " + position + ": new asynctask");
        holder.mImageGetterAsyncTask = new ImageGetterAsyncTask(holder.mImageView);
        holder.mImageGetterAsyncTask.execute(url);

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

        ImageGetterAsyncTask mImageGetterAsyncTask;


        public ImagesViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_list_item_image);
            image_list_item_tv_url = (TextView) itemView.findViewById(R.id.image_list_item_tv_url);
        }
    }


}
