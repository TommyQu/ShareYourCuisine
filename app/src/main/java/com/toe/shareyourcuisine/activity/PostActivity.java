package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
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
 * Modified By:Theon_Z
 * Modified Date:11/12/2015
 * Why is modified:Complete the class to Show the posts
 */

/**
 * Updated by Theon_Z on 11/30/15.
 * Allow user to see the image they uploaded
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
            ImageView userImageView = (ImageView) itemView.findViewById(R.id.UserImage);
            ParseUser user = currentPost.getCreatedBy();
            try {
                user.fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ParseFile userImg = user.getParseFile("img");

            //userImageView.setImageDrawable();
            String imageUrl = userImg.getUrl() ;//live url
            Uri imageUri = Uri.parse(imageUrl);

            Picasso.with(PostActivity.this).load(imageUri.toString()).into(userImageView);


            //userNameTextView
            TextView userNameTextView = (TextView) itemView.findViewById(R.id.UserName);
            userNameTextView.setText(user.getString("nickName"));

            //createdAtTextView
            TextView createdAtTextView = (TextView) itemView.findViewById(R.id.CreatedAt);
            //how does parse Date looks like when toString() is applied
            createdAtTextView.setText(currentPost.getCreatedAt().toString());

            //contentTextView
            TextView contentTextView = (TextView) itemView.findViewById(R.id.Content);
            contentTextView.setText(currentPost.getContent());

            //Todo show post images
            ImageView postImageView1 = (ImageView) itemView.findViewById(R.id.plPostImageView1);
            ImageView postImageView2 = (ImageView) itemView.findViewById(R.id.plPostImageView2);
            ImageView postImageView3 = (ImageView) itemView.findViewById(R.id.plPostImageView3);
            ArrayList<ImageView> postImgList = new ArrayList<ImageView>();
            postImgList.add(postImageView1);
            postImgList.add(postImageView2);
            postImgList.add(postImageView3);

            for(int i=0;i<currentPost.getImg().size();i++) {
                String postImageUrl = currentPost.getImg().get(i).getUrl() ;//live url
                Uri postImageUri = Uri.parse(postImageUrl);

                Picasso.with(PostActivity.this).load(postImageUri.toString()).into(postImgList.get(i));
            }

            /*if(currentPost.getImg().size()>0) {
                String postImageUrl1 = currentPost.getImg().get(0).getUrl() ;//live url
                Uri postImageUri1 = Uri.parse(postImageUrl1);

                ImageView postImageView1 = (ImageView) itemView.findViewById(R.id.plPostImageView1);
                Picasso.with(PostActivity.this).load(postImageUri1.toString()).into(postImageView1);

            }
*/
            return itemView;
        }
    }

    //Todo the click listener
    private  void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.postListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Post clickedPost = mPostList.get(position);
                //Turn to Single Post activity and use shared preference to assign the post information
                SavePrefs("clickedPostId", clickedPost.getPostId());

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
            currentPost.setPostId(postlist.get(n).getObjectId());
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

}
