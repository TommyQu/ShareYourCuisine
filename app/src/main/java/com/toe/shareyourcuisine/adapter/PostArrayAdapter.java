package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Theon_Z on 12/4/15.
 */
public class PostArrayAdapter extends ArrayAdapter<Post> {
    private Context mContext;

    private static class ViewHolder {
        ImageView userImgView;
        TextView userNickNameTextView;
        TextView createdAtTextView;
        TextView contentTextView;
        ImageView postImageView1;
        ImageView postImageView2;
        ImageView postImageView3;
    }

    public PostArrayAdapter(Context context, List<Post> PostList) {
        super(context, R.layout.post_item, PostList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //find a post to work with
        Post currentPost = getItem(position);
        ViewHolder viewHolder;

        //make sure we have a view to work with
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_item,parent,false);

            //userImageView
            viewHolder.userImgView = (ImageView) convertView.findViewById(R.id.UserImage);
            //userNickNameTextView
            viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.UserName);
            //createdAtTextView
            viewHolder.createdAtTextView = (TextView) convertView.findViewById(R.id.CreatedAt);
            //contentTextView
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.Content);
            //postImageViews
            viewHolder.postImageView1 = (ImageView) convertView.findViewById(R.id.plPostImageView1);
            viewHolder.postImageView2 = (ImageView) convertView.findViewById(R.id.plPostImageView2);
            viewHolder.postImageView3 = (ImageView) convertView.findViewById(R.id.plPostImageView3);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //fill the view

        //userImageView
        ParseUser user = currentPost.getCreatedBy();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseFile userImg = user.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(mContext).load(imageUri.toString()).into(viewHolder.userImgView);


        //userNameTextView
        viewHolder.userNickNameTextView.setText(user.getString("nickName"));

        //createdAtTextView
        viewHolder.createdAtTextView.setText(currentPost.getCreatedAt().toString());

        //contentTextView
        viewHolder.contentTextView.setText(currentPost.getContent());

        //show post images
        ArrayList<ImageView> postImgList = new ArrayList<ImageView>();
        postImgList.add(viewHolder.postImageView1);
        postImgList.add(viewHolder.postImageView2);
        postImgList.add(viewHolder.postImageView3);
        for(int i=0;i<currentPost.getImg().size();i++) {
            String postImageUrl = currentPost.getImg().get(i).getUrl() ;//live url
            Uri postImageUri = Uri.parse(postImageUrl);

            Picasso.with(mContext).load(postImageUri.toString()).into(postImgList.get(i));
        }

        return convertView;
    }
}

