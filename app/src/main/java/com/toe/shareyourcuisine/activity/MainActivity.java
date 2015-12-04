package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.ActivityArrayAdapter;
import com.toe.shareyourcuisine.adapter.MenuArrayAdapter;
import com.toe.shareyourcuisine.model.Activity;
import com.toe.shareyourcuisine.model.Menu;
import com.toe.shareyourcuisine.service.ActivityService;
import com.toe.shareyourcuisine.service.MenuService;

import java.util.List;

/**
 * Project: Share Your Cuisine
 * Comments: Home screen's activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/8/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, MenuService.GetAllMenusListener,
        ActivityService.GetAllActivitiesListener{

    private FrameLayout mContentView;
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mMenuListView;
    private ListView mPostListView;
    private ListView mActivityListView;

    private MenuArrayAdapter mMenuArrayAdapter;
    private ActivityArrayAdapter mActivityArrayAdapter;
    private MenuService mMenuService;
    private ActivityService mActivityService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout) findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_main, null);
        mContentView.addView(child);
        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setScrollBarSize(10);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mMenuListView = (ListView)findViewById(R.id.menu_list_view);
        mPostListView = (ListView)findViewById(R.id.post_list_view);
        mActivityListView = (ListView)findViewById(R.id.activity_list_view);

        mMenuService = new MenuService(MainActivity.this, MainActivity.this, "getAllMenus");
        mMenuService.getAllMenus();
        mSwipeLayout.setRefreshing(true);
    }

    @Override
    protected void addDrawerItems() {
        super.addDrawerItems();
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
                mMenuService.getAllMenus();
            }
        });
    }

    private void setMenuItemClick() {
        mMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTextView = (TextView) view.findViewById(R.id.menu_id);
                TextView titleTextView = (TextView) view.findViewById(R.id.menu_title);
                TextView userNameTextView = (TextView) view.findViewById(R.id.user_name);
                Intent intent = new Intent(MainActivity.this, SingleMenuActivity.class);
                intent.putExtra("menuId", idTextView.getText().toString());
                intent.putExtra("menuTitle", titleTextView.getText().toString());
                intent.putExtra("userName", userNameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setActivityItemClick() {
        mActivityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.activity_id);
                TextView titleTextView = (TextView) view.findViewById(R.id.activity_title);
                TextView userNameTextView = (TextView) view.findViewById(R.id.user_name);
                Intent intent = new Intent(MainActivity.this, SingleActivityActivity.class);
                intent.putExtra("activityId", textView.getText().toString());
                intent.putExtra("activityTitle", titleTextView.getText().toString());
                intent.putExtra("userName", userNameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void getAllMenusSuccess(List<Menu> menus) {
        mMenuArrayAdapter = new MenuArrayAdapter(MainActivity.this, menus);
        mMenuListView.setAdapter(mMenuArrayAdapter);
        setMenuItemClick();
        mActivityService = new ActivityService(MainActivity.this, MainActivity.this, "getAllActivities");
        mActivityService.getAllActivities();
    }

    @Override
    public void getAllMenusFail(String errorMsg) {
        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void getAllActivitiesSuccess(List<Activity> activities) {
        mActivityArrayAdapter = new ActivityArrayAdapter(MainActivity.this, activities);
        mActivityListView.setAdapter(mActivityArrayAdapter);
        setActivityItemClick();
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void getAllActivitiesFail(String errorMsg) {
        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        mSwipeLayout.setRefreshing(false);
    }
}
