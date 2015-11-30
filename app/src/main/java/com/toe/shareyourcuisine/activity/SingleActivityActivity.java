package com.toe.shareyourcuisine.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.JoinedByArrayAdapter;
import com.toe.shareyourcuisine.model.Activity;
import com.toe.shareyourcuisine.service.ActivityService;

/**
 * Created by TommyQu on 11/12/15.
 */
public class SingleActivityActivity extends ActionBarActivity implements ActivityService.GetSingleActivityListener,
        ActivityService.JoinActivityListener, ActivityService.UnJoinActivityListener, ActivityService.CheckJoinActivityListener,
        ActivityService.DeleteActivityListener {

    private static final String TAG = "ToeSingleActivity";
    private String mActivityId;
    private String mActivityTitle;
    private String mUserName;
    private TextView mActivityTitleTextView;
    private ImageView mUserImgImgView;
    private TextView mUserNameTextView;
    private TextView mActivityAddressTextView;
    private TextView mActivityStartTimeTextView;
    private TextView mActivityEndTimeTextView;
    private TextView mActivityContentTextView;
    private Button mJoinBtn;
    private Button mUnJoinBtn;
    private ListView mListView;
    private boolean mIsCurrentUser;
    private JoinedByArrayAdapter mJoinedByArrayAdapter;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_activity);
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        mActivityId = (String) bundle.get("activityId");
        mActivityTitle = (String) bundle.get("activityTitle");
        mUserName = (String) bundle.get("userName");
        if(ParseUser.getCurrentUser().getUsername().equals(mUserName))
            mIsCurrentUser = true;
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
        mUnJoinBtn = (Button) findViewById(R.id.activity_unjoin);
        mListView = (ListView) findViewById(R.id.activity_joined_by_list_view);

        ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "getSingleActivity");
        activityService.getSingleActivity(mActivityId);

        setActionBtnAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mIsCurrentUser == true)
            getMenuInflater().inflate(R.menu.menu_single_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.delete_activity) {
            //Show a dialog for confirm delete activity
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Are you sure to delete this activity?");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "deleteActivity");
                    activityService.deleteActivity(mActivityId);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setActionBtnAction() {
        mJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(SingleActivityActivity.this, "Please log in!", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "joinActivity");
                    activityService.joinActivity(mActivityId, ParseUser.getCurrentUser());
                }
            }
        });
        mUnJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() == null) {
                    Toast.makeText(SingleActivityActivity.this, "Please log in!", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityService activityService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "unJoinActivity");
                    activityService.unJoinActivity(mActivityId, ParseUser.getCurrentUser());
                }
            }
        });
    }

    @Override
    public void getSingleActivitySuccess(Activity activity) {
        mActivityTitleTextView.setText(activity.getmTitle());
        mActivity = activity;
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
        String startTimeStr = activity.getmStartTime().toString().substring(0, 20);
        String endTimeStr = activity.getmEndTime().toString().substring(0, 20);
        mActivityStartTimeTextView.setText(startTimeStr);
        mActivityEndTimeTextView.setText(endTimeStr);
        mActivityContentTextView.setText(activity.getmContent());

        //Check whether the user has joined the activity;
        ActivityService checkJoinService = new ActivityService(SingleActivityActivity.this, SingleActivityActivity.this, "checkJoinActivity");
        checkJoinService.checkJoinActivity(mActivityId, ParseUser.getCurrentUser());

        mJoinedByArrayAdapter = new JoinedByArrayAdapter(SingleActivityActivity.this, activity.getmJoinedBy());
        mListView.setAdapter(mJoinedByArrayAdapter);
    }

    @Override
    public void getSingleActivityFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void joinActivitySuccess() {
        Toast.makeText(SingleActivityActivity.this, "Join Activity Successfully!", Toast.LENGTH_SHORT).show();
        mJoinBtn.setVisibility(View.INVISIBLE);
        mUnJoinBtn.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void joinActivityFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unJoinActivitySuccess() {
        Toast.makeText(SingleActivityActivity.this, "UnJoin Activity Successfully!", Toast.LENGTH_SHORT).show();
        mJoinBtn.setVisibility(View.VISIBLE);
        mUnJoinBtn.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void unJoinActivityFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkJoinActivityListenerSuccess(String response) {
        if(response.equals("isJoined")) {
            mJoinBtn.setVisibility(View.INVISIBLE);
            mUnJoinBtn.setVisibility(View.VISIBLE);
        } else {
            mJoinBtn.setVisibility(View.VISIBLE);
            mUnJoinBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void checkJoinActivityListenerFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteActivityListenerSuccess(String response) {
        Toast.makeText(SingleActivityActivity.this, response, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SingleActivityActivity.this, ActivityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public void deleteActivityListenerFail(String errorMsg) {
        Toast.makeText(SingleActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

}
