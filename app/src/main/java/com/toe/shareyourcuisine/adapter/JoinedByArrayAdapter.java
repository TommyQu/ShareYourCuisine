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

import java.util.List;

/**
 * Created by TommyQu on 11/17/15.
 */
public class JoinedByArrayAdapter extends ArrayAdapter<ParseUser> {

    private Context mContext;

    private static class ViewHolder {
        ImageView userImgView;
        TextView userNameTextView;
    }

    public JoinedByArrayAdapter(Context context, List<ParseUser> users) {
        super(context, R.layout.row_joined_by, users);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ParseUser parseUser = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_joined_by,parent,false);
            viewHolder.userImgView = (ImageView) convertView.findViewById(R.id.user_img);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseFile userImg = parseUser.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(mContext).load(imageUri.toString()).into(viewHolder.userImgView);
        viewHolder.userNameTextView.setText((String)parseUser.get("nickName"));
        return convertView;
    }
}
