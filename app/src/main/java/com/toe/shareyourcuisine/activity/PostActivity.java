package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.PostArrayAdapter;
import com.toe.shareyourcuisine.model.Post;
import com.toe.shareyourcuisine.service.PostService;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: Share Your Cuisine
 * Comments: Post screen activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:Theon_Z
 * Modified Date:11/12/2015
 * Why is modified:Complete the class to Show the posts
 */

/**
 * Updated by Theon_Z on 11/30/15.
 * Allow user to see the image they uploaded
 */
public class PostActivity extends BaseActivity implements PostService.GetAllPostsListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "ToePostActivity";
    private FrameLayout mContentView;
    private ListView mPostListView;
    private List<Post> mPostList = new ArrayList<Post>();
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = false;
    PostService mPostService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_post, null);
        mContentView.addView(child);

        mPostListView = (ListView) findViewById(R.id.postListView);
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setScrollBarSize(10);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        //call PostService to get all posts
        mPostService = new PostService(PostActivity.this, PostActivity.this, "getAllPosts");
        mPostService.getAllPosts();

        //Item click listener
        registerClickCallback();
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PostActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_post) {
            if(ParseUser.getCurrentUser() == null) {
                Toast.makeText(PostActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(PostActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateListView() {
        ArrayAdapter<Post> postAdapter = new PostArrayAdapter(PostActivity.this,mPostList);
        mPostListView.setAdapter(postAdapter);
    }

    //Todo the click listener
    private  void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.postListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Post clickedPost = mPostList.get(position);
                //Turn to Single Post activity and use shared preference to assign the post information
                SavePrefs("clickedPostId", clickedPost.getObjectId());

                Intent intent = new Intent(PostActivity.this, SinglePostActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void getAllPostsSuccess(List<ParseObject> postlist) {
        List<Post> tempList = new ArrayList<Post>();
        for(int n=0;n<postlist.size(); n++) {
            Post currentPost = new Post();
            currentPost.setObjectId(postlist.get(n).getObjectId());
            currentPost.setCreatedAt(postlist.get(n).getCreatedAt());
            currentPost.setUpdatedAt(postlist.get(n).getUpdatedAt());
            currentPost.setCreatedBy(postlist.get(n).getParseUser("createdBy"));
            currentPost.setContent(postlist.get(n).getString("content"));

            //how to get the img array and assign it to the Post.mImg
            ArrayList<ParseFile> tempImg = new ArrayList<ParseFile>();

            try {
                tempImg =(ArrayList<ParseFile>) postlist.get(n).fetchIfNeeded().get("img");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            currentPost.setImg(tempImg);
            tempList.add(currentPost);
        }
        mPostList = tempList;

        populateListView();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void getAllPostsFail(String errorMsg) {
        Toast.makeText(PostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void SavePrefs(String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    @Override
    public void onRefresh() {
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
                mPostService.getAllPosts();
                registerClickCallback();
            }
        });
    }
}
