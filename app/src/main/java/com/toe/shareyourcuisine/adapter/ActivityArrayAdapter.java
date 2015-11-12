package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Activity;

import java.util.List;

/**
 * Created by TommyQu on 11/11/15.
 */
public class ActivityArrayAdapter extends ArrayAdapter<Activity> {

    private static class ViewHolder {
        ImageView userImgView;
        TextView activityIdTextView;
        TextView userNameTextView;
        TextView activityTitleTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;
        TextView addressTextView;
        TextView cityTextView;
        TextView stateTextView;
    }

    public ActivityArrayAdapter(Context context, List<Activity> activities) {
        super(context, R.layout.row_activity, activities);
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
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.activityTitleTextView = (TextView) convertView.findViewById(R.id.activity_title);
            viewHolder.startTimeTextView = (TextView) convertView.findViewById(R.id.activity_start_time);
            viewHolder.endTimeTextView = (TextView) convertView.findViewById(R.id.activity_end_time);
            viewHolder.addressTextView = (TextView) convertView.findViewById(R.id.activity_address);
            viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.activity_city);
            viewHolder.stateTextView = (TextView) convertView.findViewById(R.id.activity_state);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.userImgView.setImageBitmap((Bitmap) activity.getmCreatedBy().get("userImg"));
        viewHolder.activityIdTextView.setText(activity.getmObjectId());
        viewHolder.userNameTextView.setText(activity.getmCreatedBy().get("nickName").toString());
        viewHolder.activityTitleTextView.setText(activity.getmTitle());
        viewHolder.startTimeTextView.setText(activity.getmStartTime().toString());
        viewHolder.endTimeTextView.setText(activity.getmEndTime().toString());
        viewHolder.addressTextView.setText(activity.getmAddress());
        viewHolder.cityTextView.setText(activity.getmCity());
        viewHolder.stateTextView.setText(activity.getmState());
        return convertView;
    }

}
