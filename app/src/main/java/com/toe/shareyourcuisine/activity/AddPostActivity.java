package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Post;
import com.toe.shareyourcuisine.service.PostService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by TommyQu on 10/29/15.
 */

/**
 * Updated by Theon_Z on 10/31/15.
 *
 */

/**
 * Updated by Theon_Z on 11/30/15.
 * Allow user to upload image
 */
public class AddPostActivity extends ActionBarActivity implements PostService.AddPostListener{
    private  static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "ToeNewPostActivity";
    private EditText mPostContentValue;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private ArrayList<ImageView> mPostImage = new ArrayList<ImageView>();
    private ImageButton mAddImage;
    private int mImageCount = 0;

    private ArrayList<Bitmap> mBitmapImg = new ArrayList<Bitmap>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionBarRed)));
        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mPostContentValue = (EditText)findViewById(R.id.post_content_value);

        ImageView currentView1 = (ImageView) findViewById(R.id.postImageView1);
        ImageView currentView2 = (ImageView) findViewById(R.id.postImageView2);
        ImageView currentView3 = (ImageView) findViewById(R.id.postImageView3);
        mPostImage.add(currentView1);
        mPostImage.add(currentView2);
        mPostImage.add(currentView3);

        mAddImage = (ImageButton) findViewById(R.id.addImageButton);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPostContentValue.getText().toString().equals("")) {
                    Toast.makeText(AddPostActivity.this, "Please fill required fields!", Toast.LENGTH_SHORT).show();
                } else {
                    String postContent = mPostContentValue.getText().toString();
                    Post post = new Post();
                    post.setContent(postContent);
                    post.setCreatedBy(ParseUser.getCurrentUser());
                    ArrayList<ParseFile> postImage = new ArrayList<ParseFile>();
                    ParseFile currentImg;
                    for (int i = 0; i < mBitmapImg.size(); i++) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        mBitmapImg.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        currentImg = new ParseFile("postImg.jpg", byteArray);
                        try {
                           currentImg.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        postImage.add(currentImg);
                    }
                    post.setImg(postImage);

                    PostService postService = new PostService(AddPostActivity.this, AddPostActivity.this, "addPost");
                    postService.addPost(post);
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageCount<4) {
                    mImageCount++;
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

                } else {
                    Toast.makeText(AddPostActivity.this, "You can upload no more than 3 images!", Toast.LENGTH_SHORT).show();
                }
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
        Toast.makeText(AddPostActivity.this, "Add post successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddPostActivity.this, PostActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void addPostFail(String errorMsg) {
        Toast.makeText(AddPostActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            //get the image
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePath,null,null,null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap currentImg = BitmapFactory.decodeFile(imagePath,options);
            mBitmapImg.add(currentImg);


            cursor.close();

            switch (mImageCount) {
                case 1:
                    mPostImage.get(0).setImageURI(selectedImage);
                    break;
                case 2:
                    mPostImage.get(1).setImageURI(selectedImage);
                    break;
                case 3:
                    mPostImage.get(2).setImageURI(selectedImage);
                    break;
            }
        }
    }
}
