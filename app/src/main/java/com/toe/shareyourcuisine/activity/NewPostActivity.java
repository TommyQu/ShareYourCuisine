package com.toe.shareyourcuisine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Post;
import com.toe.shareyourcuisine.service.PostService;

/**
 * Created by TommyQu on 10/29/15.
 */

/**
 * Updated by Theon_Z on 10/31/15.
 *
 */
public class NewPostActivity extends ActionBarActivity implements PostService.AddPostListener{

    private static final String TAG = "ToeNewPostActivity";
    private EditText mPostContentValue;
    private Button mSubmitBtn;
    private Button mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mPostContentValue = (EditText)findViewById(R.id.post_content_value);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postContent = mPostContentValue.getText().toString();
                Post post = new Post();
                post.setContent(postContent);
                post.setCreatedBy(ParseUser.getCurrentUser());
                PostService postService = new PostService(NewPostActivity.this, NewPostActivity.this, "addPost");
                postService.addPost(post);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void addPostSuccess() {
        Toast.makeText(NewPostActivity.this, "Add post successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void addPostFail(String errorMsg) {
        Toast.makeText(NewPostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
