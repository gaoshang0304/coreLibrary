package com.daydream.corelibrary.photo.photopicker.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.daydream.corelibrary.R;
import com.daydream.corelibrary.photo.photopicker.entity.Photo;
import com.daydream.corelibrary.photo.photopicker.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {

    private List<Photo> paths = new ArrayList<>();
    private RequestManager mGlide;

    public PhotoPagerAdapter(RequestManager glide, List<Photo> paths) {
        this.paths = paths;
        this.mGlide = glide;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.__picker_picker_item_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_pager);
        final ProgressBar progressView = (ProgressBar) itemView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        final String path = paths.get(position).getPath();
        final Uri uri = Utils.getUri(path);
        mGlide.load(uri)
                .thumbnail(0.1f)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri uri, Target<GlideDrawable> target, boolean b) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, Uri uri, Target<GlideDrawable> target, boolean b, boolean b1) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .dontAnimate()
                .dontTransform()
                .override(900, 1300)
                .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                .error(R.drawable.__picker_ic_broken_image_black_48dp)
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
