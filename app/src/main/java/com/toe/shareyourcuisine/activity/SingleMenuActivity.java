package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.squareup.picasso.Picasso;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.service.MenuService;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by TommyQu on 12/3/15.
 */
public class SingleMenuActivity extends ActionBarActivity implements MenuService.GetSingleMenuListener {

    private static final String TAG = "ToeSingleMenu";
    private String mMenuId;
    private String mMenuTitle;
    private String mUserName;
    private ImageView mDisplayImgView;
    private TextView mTitleTextView;
    private CircleImageView mUserImgView;
    private TextView mUserNickNameTextView;
    private TextView mMenuContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_menu);
        Intent currentIntent = getIntent();
        Bundle bundle = currentIntent.getExtras();
        mMenuId = (String) bundle.get("menuId");
        mMenuTitle = (String) bundle.get("menuTitle");
        mUserName = (String) bundle.get("userName");

        getSupportActionBar().setTitle(mMenuTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));

        mDisplayImgView = (ImageView)findViewById(R.id.menu_display_img);
        mTitleTextView = (TextView)findViewById(R.id.menu_title);
        mUserImgView = (CircleImageView)findViewById(R.id.user_img);
        mUserNickNameTextView = (TextView)findViewById(R.id.user_nick_name);
        mMenuContentTextView = (TextView)findViewById(R.id.menu_content);

        MenuService menuService = new MenuService(SingleMenuActivity.this, SingleMenuActivity.this, "getSingleMenu");
        menuService.getSingleMenu(mMenuId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
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
    public void getSingleMenusSuccess(com.toe.shareyourcuisine.model.Menu menu) {
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
    }

    @Override
    public void getSingleMenusFail(String errorMsg) {
        Toast.makeText(SingleMenuActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
