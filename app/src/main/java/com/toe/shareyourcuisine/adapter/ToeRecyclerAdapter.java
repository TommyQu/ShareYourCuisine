package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.ToeRecyclerController;

import java.util.ArrayList;

/**
 * Created by TommyQu on 12/3/15.
 */
public class ToeRecyclerAdapter extends RecyclerView.Adapter<ToeRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<String> mImgPaths;
    private ToeRecyclerController mToeRecyclerController;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String imagePath = mImgPaths.get(position);
        Glide.with(mContext).load(imagePath).centerCrop().into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToeRecyclerController.setmImgView(imagePath);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImgPaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_item);
        }
    }

    public ToeRecyclerAdapter(Context context, ToeRecyclerController toeRecyclerController, ArrayList<String> imgPaths) {
        mContext = context;
        mToeRecyclerController = toeRecyclerController;
        mImgPaths = imgPaths;
    }

    public void changePath(ArrayList<String> imagePaths) {
        this.mImgPaths = imagePaths;
        mToeRecyclerController.setmImgView(imagePaths.get(0));
        notifyDataSetChanged();
    }

}


