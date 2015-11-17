package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
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
        return super.getView(position, convertView, parent);
    }
}
