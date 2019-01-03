package com.daydream.corelibrary.utils.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daydream.corelibrary.R;
import java.io.File;

/**
 * 加载图片 && 基于Glide4.0 &&
 * 后期用到再继续添加功能
 *
 * @author gjc
 * @version 1.0.0
 * @since 2018-04-02
 */

public class ImageLoader {

//    private RequestOptions options;
    private static ImageLoader Instance;

    public static ImageLoader getInstance() {
        if (Instance == null) {
            synchronized (ImageLoader.class){
                if (Instance == null) {
                    Instance = new ImageLoader();
                }
            }
        }
        return Instance;
    }

//    private ImageLoader() {
//        options = new RequestOptions()
//                .placeholder(com.hogolife.library.R.drawable.imagepicker_image_placeholder)
//                .error(com.hogolife.library.R.drawable.imagepicker_image_placeholder)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//    }

    public void display(String path, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(imageView);
    }

    public void loadRoundImage(Context context, String path, ImageView imageView, int radius) {
        Glide.with(imageView.getContext())
                .load(path)
                .transform(new GlideRoundTransform(context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .crossFade()
                .into(imageView);

    }

    public void display(File path, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(imageView);
    }

    public void loadRoundImage(Context context, File path, ImageView imageView, int radius) {
        Glide.with(imageView.getContext())
                .load(path)
                .transform(new GlideRoundTransform(context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .crossFade()
                .into(imageView);
    }
}
