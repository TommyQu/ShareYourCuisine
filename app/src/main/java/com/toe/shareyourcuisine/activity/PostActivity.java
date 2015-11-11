package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
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
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class PostActivity extends BaseActivity implements PostService.GetAllPostsListener{
    private final String TAG = "ToePostActivity";
    private FrameLayout mContentView;

    private List<Post> mPostList = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_post, null);
        mContentView.addView(child);

        //call PostService to get all posts
        PostService PostService = new PostService(PostActivity.this, PostActivity.this, "getAllPosts");
        PostService.getAllPosts();

        //Todo deal with the click listener
        registerClickCallback();
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.new_post) {
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
        ArrayAdapter<Post> postAdapter = new PostListAdapter();
        ListView postListView = (ListView) findViewById(R.id.postListView);
        postListView.setAdapter(postAdapter);
    }

    private class PostListAdapter extends ArrayAdapter<Post> {
        public PostListAdapter() {
            super(PostActivity.this, R.layout.post_item, mPostList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.post_item, parent, false);
            }

            //find a post to work with
            Post currentPost = mPostList.get(position);

            //fill the view
            //userImageView
            ImageView userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
            ParseUser user = currentPost.getCreatedBy();
            ParseFile userImg = user.getParseFile("img");
            //Todo how fill the userImageView with the user image which is a ParseFile type
            //userImageView.setImageDrawable();
            String imageUrl = userImg.getUrl() ;//live url
            Uri imageUri = Uri.parse(imageUrl);

            Picasso.with(PostActivity.this).load(imageUri.toString()).into(userImageView);


            //userNameTextView
            TextView userNameTextView = (TextView) itemView.findViewById(R.id.userNameTextView);
            userNameTextView.setText(user.getString("nickName"));

            //createdAtTextView
            TextView createdAtTextView = (TextView) itemView.findViewById(R.id.createdAtTextView);
            //how does parse Date looks like when toString() is applied
            createdAtTextView.setText(currentPost.getCreatedAt().toString());

            //contentTextView
            TextView contentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
            contentTextView.setText(currentPost.getContent());

            //Todo show post images


            return itemView;
        }
    }

    //Todo the click listener
    private  void registerClickCallback() {
    }

    @Override
    public void getAllPostsSuccess(List<ParseObject> postlist) {
        List<Post> tempList = new ArrayList<Post>();
        for(int n=0;n<postlist.size(); n++) {
            Post currentPost = new Post();
            currentPost.setPostId(postlist.get(n).getObjectId());
            currentPost.setCreatedAt(postlist.get(n).getCreatedAt());
            currentPost.setUpdatedAt(postlist.get(n).getUpdatedAt());
            currentPost.setCreatedBy(postlist.get(n).getParseUser("createdBy"));
            currentPost.setContent(postlist.get(n).getString("content"));

            //how to get the img array and assign it to the Post.mImg
            ArrayList<ParseFile> tempImg = new ArrayList<ParseFile>();
            tempImg =(ArrayList<ParseFile>) postlist.get(n).get("img");
            currentPost.setImg(tempImg);
            tempList.add(currentPost);
        }
        mPostList = tempList;

        populateListView();
    }

    @Override
    public void getAllPostsFail(String errorMsg) {
        Toast.makeText(PostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

}
