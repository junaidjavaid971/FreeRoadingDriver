package com.apps.freeroadingdriver.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.widget.ImageView;

import com.apps.freeroadingdriver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;


/**
 * Created by craterzone on 24/9/16.
 */

public class GlideUtil {


    /**
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadImageCirculer(final Context context, final ImageView imageView, String url) {
        Glide.with(context).asBitmap().load(url).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }


    public static void loadImageCirculer(final Context context, final ImageView imageView, Uri uri) {
        Glide.with(context).asBitmap().load(new File(uri.getPath()))
                .centerCrop()
                .error(R.drawable.ic_profile_placeholder)
                .fitCenter()
                .placeholder(R.drawable.ic_profile_placeholder).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public static void loadImageWithDefaultImage(final Context context, final ImageView imageView, String url, int defaultImage) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .centerCrop()
                .error(defaultImage)
                .fitCenter()
                .placeholder(defaultImage)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public static void loadImageWithDefaultImageFromDrawable(final Context context, final ImageView imageView, Drawable drawable, int defaultImage) {
        Glide.with(context)
                .asBitmap()
                .load("")
                .placeholder(drawable)
                .centerCrop()
                .error(defaultImage)
                .fitCenter()
                .placeholder(defaultImage)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }


    public static void loadCircleImage(final Context context, final ImageView imageView, String url, int defaultImage) {
        Glide.with(context).asBitmap().load(url).centerCrop().placeholder(defaultImage).fitCenter().into(imageView);
    }

    public static void loadCircleImage(final Context context, final ImageView imageView, String url) {
        Glide.with(context).asBitmap().load(url).centerCrop().fitCenter().into(imageView);
    }
}
