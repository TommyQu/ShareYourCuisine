package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toe.shareyourcuisine.R;

/**
 * Created by TommyQu on 11/11/15.
 */
public class DrawerItemArrayAdapter extends ArrayAdapter<String> {

    private Context mContext;

    private static class ViewHolder {
        ImageView drawerItemImgView;
        TextView drawerItemTextView;
    }

    public DrawerItemArrayAdapter(Context context, String[] array) {
        super(context, R.layout.drawer_item, array);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.drawer_item,parent,false);
            viewHolder.drawerItemImgView = (ImageView) convertView.findViewById(R.id.drawer_item_img);
            viewHolder.drawerItemTextView = (TextView) convertView.findViewById(R.id.drawer_item_text);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}
