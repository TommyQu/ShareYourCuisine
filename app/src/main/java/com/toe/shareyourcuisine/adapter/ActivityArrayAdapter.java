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
import com.toe.shareyourcuisine.model.Activity;

import java.util.List;

/**
 * Created by TommyQu on 11/11/15.
 */
public class ActivityArrayAdapter extends ArrayAdapter<Activity> {

    private Context mContext;

    private static class ViewHolder {
        ImageView userImgView;
        TextView activityIdTextView;
        TextView nickNameTextView;
        TextView userNameTextView;
        TextView activityTitleTextView;
        TextView startTimeTextView;
        TextView cityTextView;
        TextView stateTextView;
        TextView joinedNumView;
    }

    public ActivityArrayAdapter(Context context, List<Activity> activities) {
        super(context, R.layout.row_activity, activities);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Activity activity = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_activity,parent,false);
            viewHolder.activityIdTextView = (TextView) convertView.findViewById(R.id.activity_id);
            viewHolder.userImgView = (ImageView) convertView.findViewById(R.id.user_img);
            viewHolder.nickNameTextView = (TextView) convertView.findViewById(R.id.nick_name);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.activityTitleTextView = (TextView) convertView.findViewById(R.id.activity_title);
            viewHolder.startTimeTextView = (TextView) convertView.findViewById(R.id.activity_start_time);
            viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.activity_city);
            viewHolder.stateTextView = (TextView) convertView.findViewById(R.id.activity_state);
            viewHolder.joinedNumView = (TextView) convertView.findViewById(R.id.activity_joined_num);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Fetch user img
        ParseUser parseUser = activity.getmCreatedBy();
        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseFile userImg = parseUser.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(mContext).load(imageUri.toString()).into(viewHolder.userImgView);

        String startTime = activity.getmStartTime().getMonth()+"/"+activity.getmStartTime().getDay()+"/"+
            activity.getmStartTime().getYear()+" "+activity.getmStartTime().getHours()+":"+activity.getmStartTime().getMinutes();
        String endTime = activity.getmEndTime().getMonth()+"/"+activity.getmEndTime().getDay()+"/"+
                activity.getmEndTime().getYear()+" "+activity.getmEndTime().getHours()+":"+activity.getmEndTime().getMinutes();
        viewHolder.activityIdTextView.setText(activity.getmObjectId());
        viewHolder.nickNameTextView.setText(activity.getmCreatedBy().get("nickName").toString());
        viewHolder.userNameTextView.setText(activity.getmCreatedBy().getUsername());
        viewHolder.activityTitleTextView.setText(activity.getmTitle());
        viewHolder.startTimeTextView.setText(startTime);
        viewHolder.cityTextView.setText(activity.getmCity());
        viewHolder.stateTextView.setText(activity.getmState());
        viewHolder.joinedNumView.setText(String.valueOf(activity.getmJoinedNum()));
        return convertView;
    }

}
