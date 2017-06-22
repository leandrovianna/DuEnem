package com.alpha2.duenem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class ImageViewPicassoTarget implements Target {
    private ImageView mView;
    private Context mContext;

    ImageViewPicassoTarget(Context context, ImageView avatarView) {
        this.mView = avatarView;
        this.mContext = context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), bitmap);
        drawable.setCircular(true);
        mView.setImageDrawable(drawable);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        mView.setImageResource(R.drawable.default_user_photo);
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        mView.setImageResource(R.drawable.default_user_photo);
    }
}
