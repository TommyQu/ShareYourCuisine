package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Activity;
import com.toe.shareyourcuisine.service.ActivityService;

/**
 * Created by TommyQu on 11/12/15.
 */
public class SingleActivityActivity extends ActionBarActivity implements ActivityService.GetSingleActivityListener,
        ActivityService.JoinActivityListener{

    private static final String TAG = "ToeSingleActivity";
    private String mActivityId;
    private String mActivityTitle;
    private TextView mActivityTitleTextView;
    private ImageView mUserImgImgView;
    private TextView mUserNameTextView;
    private TextView mActivityAddressTextView;
    private TextView mActivityStartTimeTextView;
    private TextView mActivityEndTimeTextView;
    private TextView mActivityContentTextView;
    private Button mJoinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_activity);
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        mActivityId = (String) bundle.get("activityId");
        mActivityTitle = (String) bundle.get("activityTitle");
        getSupportActionBar().setTitle(mActivityTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

        mActivityTitleTextView = (TextView) findViewById(R.id.activity_title);
        mUserImgImgView = (ImageView) findViewById(R.id.user_img);
        mUserNameTextView = (TextView) findViewById(R.id.user_name);
        mActivityAddressTextView = (TextView) findViewById(R.id.activity_address);
        mActivityStartTimeTextView = (TextView) findViewById(R.id.activity_start_time);
        mActivityEndTimeTextView = (TextView) findViewById(R.id.activity_end_time);
        mActivityContentTextView = (TextView) findViewById(R.id.activity_content);
        mJoinBtn = (Button) findViewById(R.id.activity_join);

        ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "getSingleActivity");
        activityService.getSingleActivity(mActivityId);

        setJoinBtnAction();
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

    private void setJoinBtnAction() {
        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ParseUser.getCurrentUser() == null) {
                    Toast.makeText(SingleActivityActivity.this, "Please log in!", Toast.LENGTH_SHORT).show();
                }
                else {
                    ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "joinActivity");
                    activityService.joinActivity(mActivityId, ParseUser.getCurrentUser());
                }
            }
        });
    }

    @Override
    public void getSingleActivitySuccess(Activity activity) {
        mActivityTitleTextView.setText(activity.getmTitle());
        //Fetch user img into ImageView
        ParseUser parseUser = activity.getmCreatedBy();
        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseFile userImg = parseUser.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(SingleActivityActivity.this).load(imageUri.toString()).into(mUserImgImgView);

        mUserNameTextView.setText(activity.getmCreatedBy().get("nickName").toString());
        mActivityAddressTextView.setText(activity.getmAddress() + ", " + activity.getmCity() + ", " + activity.getmState() + ", " + activity.getmZipCode());
        mActivityStartTimeTextView.setText(activity.getmStartTime().toString());
        mActivityEndTimeTextView.setText(activity.getmEndTime().toString());
        mActivityContentTextView.setText(activity.getmContent());
    }

    @Override
    public void getSingleActivityFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void joinActivitySuccess() {
        Toast.makeText(SingleActivityActivity.this, "Join Activity Successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void joinActivityFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
