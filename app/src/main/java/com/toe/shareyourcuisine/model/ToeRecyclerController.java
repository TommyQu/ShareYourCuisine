package com.toe.shareyourcuisine.model;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by TommyQu on 12/3/15.
 */
public class ToeRecyclerController {

    Context mContext;
    ImageView mImgView;

    public ToeRecyclerController(Context context, ImageView imageView) {
        mContext = context;
        mImgView = imageView;
    }

    public void setmImgView(String imgPath) {
        Glide.with(mContext).load(imgPath).fitCenter().into(mImgView);
    }

}
