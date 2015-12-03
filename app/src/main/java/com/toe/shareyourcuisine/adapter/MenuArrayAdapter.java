package com.toe.shareyourcuisine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Menu;

import java.util.List;

/**
 * Created by TommyQu on 12/2/15.
 */
public class MenuArrayAdapter extends ArrayAdapter<Menu> {

    private Context mContext;

    private static class ViewHolder {
        ImageView menuImgView;
        TextView menuTitleTextView;
    }

    public MenuArrayAdapter(Context context, List<Menu> menus) {
        super(context, R.layout.row_menu, menus);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Menu menu = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_menu,parent,false);
            viewHolder.menuImgView = (ImageView)convertView.findViewById(R.id.menu_img);
            viewHolder.menuTitleTextView = (TextView)convertView.findViewById(R.id.menu_title);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(menu.getmDisplayImg().getUrl()).into(viewHolder.menuImgView);
        viewHolder.menuTitleTextView.setText(menu.getmTitle());
        return convertView;
    }
}
