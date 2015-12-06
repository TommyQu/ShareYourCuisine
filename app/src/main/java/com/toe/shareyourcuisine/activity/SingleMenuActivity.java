package com.toe.shareyourcuisine.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.ToeRecyclerAdapter;
import com.toe.shareyourcuisine.model.ToeRecyclerController;
import com.toe.shareyourcuisine.service.MenuService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TommyQu on 12/3/15.
 */
public class SingleMenuActivity extends ActionBarActivity implements MenuService.GetSingleMenuListener,
    MenuService.DeleteMenuListener{

    private static final String TAG = "ToeSingleMenu";
    private String mMenuId;
    private String mMenuTitle;
    private String mUserName;
    private ImageView mDisplayImgView;
    private TextView mTitleTextView;
    private CircleImageView mUserImgView;
    private TextView mUserNickNameTextView;
    private TextView mMenuContentTextView;
    private boolean mIsCurrentUser;
    private CallbackManager mCallbackManager;
    private com.toe.shareyourcuisine.model.Menu mMenu;

    private ImageView mMenuImgView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ToeRecyclerAdapter mToeRecyclerAdapter;
    private ToeRecyclerController mToeRecyclerController;
    private ArrayList<ParseFile> mImgFiles = new ArrayList<ParseFile>();
    private ArrayList<Bitmap> mImgBitmaps = new ArrayList<Bitmap>();
    private ArrayList<String> mImgPaths = new ArrayList<>();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_menu);
        mCallbackManager = CallbackManager.Factory.create();
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        mMenuId = (String) bundle.get("menuId");
        mMenuTitle = (String) bundle.get("menuTitle");
        mUserName = (String) bundle.get("userName");
        if(ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().getUsername().equals(mUserName))
            mIsCurrentUser = true;
        getSupportActionBar().setTitle(mMenuTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        mProgressDialog = ProgressDialog.show(this, "Loading", "Loading data...");
        mProgressDialog.setCancelable(true);

        mDisplayImgView = (ImageView)findViewById(R.id.menu_display_img);
        mTitleTextView = (TextView)findViewById(R.id.menu_title);
        mUserImgView = (CircleImageView)findViewById(R.id.user_img);
        mUserNickNameTextView = (TextView)findViewById(R.id.user_nick_name);
        mMenuContentTextView = (TextView)findViewById(R.id.menu_content);
        mMenuImgView = (ImageView)findViewById(R.id.menu_img);
        mRecyclerView = (RecyclerView)findViewById(R.id.menu_img_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mToeRecyclerController = new ToeRecyclerController(this, mMenuImgView);
        mToeRecyclerAdapter = new ToeRecyclerAdapter(this, mToeRecyclerController, mImgPaths);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mToeRecyclerAdapter);

        MenuService menuService = new MenuService(SingleMenuActivity.this, SingleMenuActivity.this, "getSingleMenu");
        menuService.getSingleMenu(mMenuId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mIsCurrentUser == true)
            getMenuInflater().inflate(R.menu.menu_single_menu, menu);
        else
            getMenuInflater().inflate(R.menu.menu_single_menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else if (id == R.id.delete_menu) {
            //Show a dialog for confirm delete activity
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Are you sure to delete this menu?");
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MenuService menuService = new MenuService(SingleMenuActivity.this, SingleMenuActivity.this, "deleteMenu");
                    menuService.deleteMenu(mMenuId);
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
        else if ( id == R.id.share_menu ) {
            ShareDialog shareDialog;
            shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(SingleMenuActivity.this, "Share to Facebook successfully!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(SingleMenuActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            if (ShareDialog.canShow(ShareLinkContent.class)) {
//                String description = "Activity: The activity is located at " + mActivity.getmAddress() + ", " + mActivity.getmCity()
//                        + ", " + mActivity.getmState() + "/n from " + mActivity.getmStartTime() + " to " + mActivity.getmEndTime()
//                        + "./nThe content is " + mActivity.getmContent() + ".";
                Bitmap image = null;
                try {
                    image = BitmapFactory.decodeByteArray(mMenu.getmDisplayImg().getData(), 0, mMenu.getmDisplayImg().getData().length);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void getSingleMenusSuccess(com.toe.shareyourcuisine.model.Menu menu) {
        mMenu = menu;
        ParseFile displayImg = menu.getmDisplayImg();
        String imageUrl = displayImg.getUrl() ;//live url
        Uri imageUri = Uri.parse(imageUrl);
        Picasso.with(SingleMenuActivity.this).load(imageUri.toString()).into(mDisplayImgView);
        mTitleTextView.setText(menu.getmTitle());

        displayImg = menu.getmCreatedBy().getParseFile("img");
        imageUrl = displayImg.getUrl() ;//live url
        imageUri = Uri.parse(imageUrl);
        Picasso.with(SingleMenuActivity.this).load(imageUri.toString()).into(mUserImgView);
        mUserNickNameTextView.setText((String) menu.getmCreatedBy().get("nickName"));
        mMenuContentTextView.setText(menu.getmContent());

        for(int i = 0; i < menu.getmImg().size(); i++) {
            mImgPaths.add(menu.getmImg().get(i).getUrl());
        }
        mToeRecyclerAdapter.changePath(mImgPaths);
        mProgressDialog.dismiss();
    }

    @Override
    public void getSingleMenusFail(String errorMsg) {
        Toast.makeText(SingleMenuActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
    }

    @Override
    public void deleteMenuListenerSuccess(String response) {
        Toast.makeText(SingleMenuActivity.this, response, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SingleMenuActivity.this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    @Override
    public void deleteMenuListenerFail(String errorMsg) {
        Toast.makeText(SingleMenuActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
