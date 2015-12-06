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
import com.toe.shareyourcuisine.model.Comment;

import java.util.List;

/**
 * Created by Theon_Z on 12/5/15.
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {
    private Context mContext;

    private static class ViewHolder {
        ImageView userImgView;
        TextView userNickNameTextView;
        TextView createdAtTextView;
        TextView contentTextView;
    }

    public CommentArrayAdapter(Context context, List<Comment> CommentList) {
        super(context, R.layout.comment_item, CommentList);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //find a post to work with
        Comment currentComment = getItem(position);
        ViewHolder viewHolder;

        //make sure we have a view to work with
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_item,parent,false);

            //userImageView
            viewHolder.userImgView = (ImageView) convertView.findViewById(R.id.userImageImgView);
            //userNickNameTextView
            viewHolder.userNickNameTextView = (TextView) convertView.findViewById(R.id.userNickNameTextView);
            //createdAtTextView
            viewHolder.createdAtTextView = (TextView) convertView.findViewById(R.id.createdAtTextView);
            //contentTextView
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //fill the view

        //userImageView
        ParseUser user = currentComment.getCreatedBy();
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
        viewHolder.createdAtTextView.setText(currentComment.getCreatedAt().toString());

        //contentTextView
        viewHolder.contentTextView.setText(currentComment.getContent());

        return convertView;
    }
<<<<<<< HEAD
}
=======
}


>>>>>>> origin/master
