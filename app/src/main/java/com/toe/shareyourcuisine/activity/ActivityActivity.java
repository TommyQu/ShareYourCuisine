package com.toe.shareyourcuisine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.ActivityArrayAdapter;
import com.toe.shareyourcuisine.model.Activity;
import com.toe.shareyourcuisine.service.ActivityService;

import java.util.List;

/**
 * Project: Share Your Cuisine
 * Comments: Activity screen's activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class ActivityActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        ActivityService.GetAllActivitiesListener{

    private final String TAG = "ToePostActivity";
    private FrameLayout mContentView;
    private ListView mListView;
    private ActivityArrayAdapter mActivityArrayAdapter;
    private ActivityService mActivityService;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = false;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout) findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_activity, null);
        mContentView.addView(child);
        mProgressDialog = ProgressDialog.show(this, "Loading", "Loading data...");
        mProgressDialog.setCancelable(true);
        mListView = (ListView) findViewById(R.id.activity_list_view);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setScrollBarSize(10);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mActivityService = new ActivityService(ActivityActivity.this, ActivityActivity.this, "getAllActivities");
        mActivityService.getAllActivities(0);
        swipeLayout.setRefreshing(true);
   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_activity) {
            if(ParseUser.getCurrentUser() == null) {
                Toast.makeText(ActivityActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(ActivityActivity.this, AddActivityActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActivityItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)view.findViewById(R.id.activity_id);
                TextView titleTextView = (TextView)view.findViewById(R.id.activity_title);
                TextView userNameTextView = (TextView)view.findViewById(R.id.user_name);
                Intent intent = new Intent(ActivityActivity.this, SingleActivityActivity.class);
                intent.putExtra("activityId", textView.getText().toString());
                intent.putExtra("activityTitle", titleTextView.getText().toString());
                intent.putExtra("userName", userNameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void getAllActivitiesSuccess(List<Activity> activities) {
        mActivityArrayAdapter = new ActivityArrayAdapter(ActivityActivity.this, activities);
        mListView.setAdapter(mActivityArrayAdapter);
        swipeLayout.setRefreshing(false);
        mProgressDialog.dismiss();
        setActivityItemClick();
    }

    @Override
    public void getAllActivitiesFail(String errorMsg) {
        Toast.makeText(ActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
    }

    @Override
    public void onRefresh() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                mActivityService.getAllActivities(0);
                setActivityItemClick();
            }
        });
    }
}
