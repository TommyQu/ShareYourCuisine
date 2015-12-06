package com.toe.shareyourcuisine.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.CommentArrayAdapter;
import com.toe.shareyourcuisine.model.Comment;
import com.toe.shareyourcuisine.model.Post;
import com.toe.shareyourcuisine.service.CommentService;
import com.toe.shareyourcuisine.service.PostService;

import java.util.ArrayList;

/**
 * Created by Theon_Z on 10/31/15.
 */

public class SinglePostActivity extends ActionBarActivity implements PostService.GetSinglePostListener,CommentService.AddCommentToPostListener{
    private String mClickedPostId;
    private Context mContext;
    private ImageView mUserImageView;
    private TextView mUserNameTextView;
    private TextView mCreatedAtTextView;
    private TextView mContentTextView;
    private ImageView mPostImageView1;
    private ImageView mPostImageView2;
    private ImageView mPostImageView3;
    private ImageButton mAddCommentImgBtn;
    private RelativeLayout mIuputContent;
    private EditText mCommentContent;
    private Button mAddCommentBtn;
    private CommentService mCommentService;
    private ListView mCommentListView;
    private ArrayList<Comment> mCommentList = new ArrayList<Comment>();
    private CommentArrayAdapter mCommentAdapter;
    private Post mClickedPost;
    private PostService mPostService;

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
        mAddCommentImgBtn = (ImageButton) findViewById(R.id.addCommentImgBtn);
        mIuputContent = (RelativeLayout) findViewById(R.id.inputContent);
        mCommentContent = (EditText) findViewById(R.id.commentContent);
        mAddCommentBtn = (Button) findViewById(R.id.addCommentBtn);
        mCommentListView = (ListView) findViewById(R.id.spCommentList);


        mCommentService = new CommentService(SinglePostActivity.this, SinglePostActivity.this, "addCommentToPost");

        mAddCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCommentContent.getText().equals(null)) {
                    Toast.makeText(SinglePostActivity.this, "Comment content can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SinglePostActivity.this, "Commenting Succeed!", Toast.LENGTH_SHORT).show();
                    Comment comment = new Comment();
                    comment.setContent(mCommentContent.getText().toString());
                    comment.setCreatedBy(ParseUser.getCurrentUser());
                    mCommentService.addCommentToPost(mClickedPostId, comment);
                    mCommentContent.clearComposingText();
                    mIuputContent.setVisibility(View.INVISIBLE);
                }
            }
        });

        mAddCommentImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIuputContent.getVisibility() == View.VISIBLE) {
                    mIuputContent.setVisibility(View.INVISIBLE);
                } else if(mIuputContent.getVisibility() == View.INVISIBLE) {
                    mIuputContent.setVisibility(View.VISIBLE);
                }
            }
        });

        mPostService = new PostService(SinglePostActivity.this, SinglePostActivity.this,
                "getSinglePost");
        mPostService.getSinglePost(mClickedPostId);

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

    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mClickedPostId = sp.getString("clickedPostId", "crj6VzCQN6");
    }

    @Override
    public void getSinglePostSuccess(Post post) {
        mClickedPost = post;
        ParseUser parseUser = post.getCreatedBy();
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
        mContentTextView.setText(post.getContent());

        ArrayList<ParseFile> postImg = new ArrayList<ParseFile>();
        postImg = post.getImg();

        ArrayList<ImageView> postImgList = new ArrayList<ImageView>();
        postImgList.add(mPostImageView1);
        postImgList.add(mPostImageView2);
        postImgList.add(mPostImageView3);
        for(int i=0;i<postImg.size();i++) {
            String postImageUrl = postImg.get(i).getUrl() ;//live url
            Uri postImageUri = Uri.parse(postImageUrl);

            Picasso.with(mContext).load(postImageUri.toString()).into(postImgList.get(i));
        }
        populateComment(post);
        if(mCommentList.size() != 0) {
            populateListView();
        }
    }

    @Override
    public void getSinglePostFail(String errorMsg) {
        Toast.makeText(SinglePostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void addCommentToPostSuccess() {
        mPostService.getSinglePost(mClickedPostId);
    }

    @Override
    public void addCommentToPostFail(String errorMsg) {
        Toast.makeText(SinglePostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void populateListView() {
        mCommentAdapter = new CommentArrayAdapter(SinglePostActivity.this,mCommentList);
        mCommentListView.setAdapter(mCommentAdapter);
    }

    private void populateComment(Post post) {
        ArrayList<Comment> tempComentList = new ArrayList<Comment>();

        for(int i = 0;i<post.getComments().size();i++) {
            Comment currentComment = new Comment();
            currentComment.setObjectId(post.getComments().get(i).getObjectId());
            currentComment.setCreatedAt(post.getComments().get(i).getCreatedAt());
            currentComment.setContent(post.getComments().get(i).getString("content").toString());
            currentComment.setCreatedBy((ParseUser) post.getComments().get(i).get("createdBy"));
            tempComentList.add(currentComment);
        }
        mCommentList = tempComentList;
    }
}
