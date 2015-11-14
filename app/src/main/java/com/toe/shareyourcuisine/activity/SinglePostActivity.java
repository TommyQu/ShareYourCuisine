package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.service.PostService;

import java.util.ArrayList;

/**
 * Created by Theon_Z on 10/31/15.
 */

public class SinglePostActivity extends ActionBarActivity implements PostService.GetSinglePostListener{
    private String mClickedPostId;

    private ImageView mUserImageView;
    private TextView mUserNameTextView;
    private TextView mCreatedAtTextView;
    private TextView mContentTextView;
    private ImageView mPostImageView1;
    private ImageView mPostImageView2;
    private ImageView mPostImageView3;
    private Button mBtnComment;

    private static final String TAG = "ToeSinglePostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        getSupportActionBar().setTitle("Single Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

        LoadPrefs();

        //wire up widgts
        mUserImageView = (ImageView) findViewById(R.id.spUserImage);
        mUserNameTextView = (TextView) findViewById(R.id.spUserName);
        mCreatedAtTextView = (TextView) findViewById(R.id.spCreatedAt);
        mContentTextView = (TextView) findViewById(R.id.spContent);
        mPostImageView1 = (ImageView) findViewById(R.id.spPostImage1);
        mPostImageView2 = (ImageView) findViewById(R.id.spPostImage2);
        mPostImageView3 = (ImageView) findViewById(R.id.spPostImage3);

        mBtnComment = (Button) findViewById(R.id.spCommentBtn);



        PostService postService = new PostService(SinglePostActivity.this, SinglePostActivity.this,
                "getSinglePost");
        postService.getSinglePost(mClickedPostId);
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
        Intent intent = new Intent(SinglePostActivity.this, PostActivity.class);
        finish();
        startActivity(intent);
    }

    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mClickedPostId = sp.getString("clickedPostId", "crj6VzCQN6");
    }

    @Override
    public void getSinglePostSuccess(ParseObject post) {
        //fetch post image
        try {
            post.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseUser parseUser = post.getParseUser("createdBy");

        //fetch user image
        try {
            parseUser.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseFile userImg = parseUser.getParseFile("img");
        String imageUrl = userImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(SinglePostActivity.this).load(imageUri.toString()).into(mUserImageView);

        mUserNameTextView.setText(parseUser.getString("nickName").toString());
        mCreatedAtTextView.setText(post.getCreatedAt().toString());
        mContentTextView.setText(post.getString("content"));

        ArrayList<ParseFile> postImg = new ArrayList<ParseFile>();
        try {
            postImg =(ArrayList<ParseFile>) post.fetchIfNeeded().get("img");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(int n = 0; n < postImg.size(); n++) {
            String currentImageUrl = postImg.get(n).getUrl() ;//live url
            Uri currentImageUri = Uri.parse(currentImageUrl);
            if(n == 0) {
                Picasso.with(SinglePostActivity.this).load(imageUri.toString()).into(mPostImageView1);
            } else if(n==1) {
                Picasso.with(SinglePostActivity.this).load(imageUri.toString()).into(mPostImageView2);
            } else if(n==2) {
                Picasso.with(SinglePostActivity.this).load(imageUri.toString()).into(mPostImageView3);
            }
        }
    }

    @Override
    public void getSinglePostFail(String errorMsg) {
        Toast.makeText(SinglePostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
