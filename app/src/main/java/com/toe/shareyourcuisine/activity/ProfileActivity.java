package com.toe.shareyourcuisine.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;

/**
 * Created by TommyQu on 11/29/15.
 */
public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ToeProfileActivity";
    private FrameLayout mContentView;
    private Button mSignOutBtn;
    private ImageView mUserImgImageView;
    private TextView mUserNameTextView;
    private TextView mMenuNumTextView;
    private TextView mPostNumTextView;
    private TextView mHostedActivityNumTextView;
    private TextView mJoinedActivityNumTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout) findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_profile, null);
        mContentView.addView(child);
        mProgressDialog = ProgressDialog.show(this, "Loading", "Loading data...");
        mProgressDialog.setCancelable(true);
        mUserImgImageView = (ImageView)findViewById(R.id.user_img);
        mUserNameTextView = (TextView)findViewById(R.id.user_name);
        mSignOutBtn = (Button) findViewById(R.id.sign_out_btn);
        mMenuNumTextView = (TextView)findViewById(R.id.menu_num);
        mPostNumTextView = (TextView)findViewById(R.id.post_num);
        mHostedActivityNumTextView = (TextView)findViewById(R.id.hosted_activity_num);
        mJoinedActivityNumTextView = (TextView)findViewById(R.id.joined_activity_num);

        //Input user's personal information
        ParseUser parseUser = ParseUser.getCurrentUser();
        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseFile userImg = parseUser.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(ProfileActivity.this).load(imageUri.toString()).into(mUserImgImageView);
        mUserNameTextView.setText(parseUser.get("nickName").toString());

        //Set up sign out button
        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure to sign out?");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null) {
                                    Toast.makeText(ProfileActivity.this, "Sign Out successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(ProfileActivity.this, "Error handling!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
        });
        setUpContent();
    }

    private void setUpContent() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Menu");
        query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null)
                    mMenuNumTextView.setText(String.valueOf(i));
                else
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ParseQuery<ParseObject> queryPost = ParseQuery.getQuery("Post");
        queryPost.whereEqualTo("createdBy", ParseUser.getCurrentUser());
        queryPost.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null)
                    mPostNumTextView.setText(String.valueOf(i));
                else
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ParseQuery<ParseObject> queryActivity = ParseQuery.getQuery("Activity");
        queryActivity.whereEqualTo("createdBy", ParseUser.getCurrentUser());
        queryActivity.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null)
                    mHostedActivityNumTextView.setText(String.valueOf(i));
                else
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        ParseQuery<ParseUser> userJoined = ParseUser.getQuery();
        userJoined.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        ParseQuery<ParseObject> queryJoinedActivity = ParseQuery.getQuery("Activity");
        queryJoinedActivity.whereMatchesQuery("joinedBy", userJoined);
        queryJoinedActivity.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                if (e == null)
                    mJoinedActivityNumTextView.setText(String.valueOf(i));
                else
                    Toast.makeText(ProfileActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        mJoinedActivityNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ActivityJoinedActivity.class);
                startActivity(intent);
            }
        });

        mProgressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
