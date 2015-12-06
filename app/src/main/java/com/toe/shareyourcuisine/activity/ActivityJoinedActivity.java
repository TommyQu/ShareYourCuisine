package com.toe.shareyourcuisine.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by TommyQu on 12/6/15.
 */
public class ActivityJoinedActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener,
        ActivityService.GetJoinedActivityByUserListener {

    private final String TAG = "ToeJoinedActivity";
    private ListView mListView;
    private ActivityArrayAdapter mActivityArrayAdapter;
    private ActivityService mActivityService;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = false;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_activity);
        getSupportActionBar().setTitle("Check Joined Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarRed)));
        mListView = (ListView) findViewById(R.id.activity_list_view);
        mProgressDialog = ProgressDialog.show(this, "Loading", "Loading data...");
        mProgressDialog.setCancelable(true);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setScrollBarSize(10);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mActivityService = new ActivityService(ActivityJoinedActivity.this, ActivityJoinedActivity.this, "getJoinedActivityByUser");
        mActivityService.getJoinedActivityByUser(ParseUser.getCurrentUser().getObjectId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setActivityItemClick() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.activity_id);
                TextView titleTextView = (TextView) view.findViewById(R.id.activity_title);
                TextView userNameTextView = (TextView) view.findViewById(R.id.user_name);
                Intent intent = new Intent(ActivityJoinedActivity.this, SingleActivityActivity.class);
                intent.putExtra("activityId", textView.getText().toString());
                intent.putExtra("activityTitle", titleTextView.getText().toString());
                intent.putExtra("userName", userNameTextView.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void getJoinedActivityByUserSuccess(List<Activity> activities) {
        mActivityArrayAdapter = new ActivityArrayAdapter(ActivityJoinedActivity.this, activities);
        mListView.setAdapter(mActivityArrayAdapter);
        mProgressDialog.dismiss();
        setActivityItemClick();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void getJoinedActivityByUserFail(String errorMsg) {
        Toast.makeText(ActivityJoinedActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                mActivityService.getJoinedActivityByUser(ParseUser.getCurrentUser().getObjectId());
                setActivityItemClick();
            }
        });
    }
}
