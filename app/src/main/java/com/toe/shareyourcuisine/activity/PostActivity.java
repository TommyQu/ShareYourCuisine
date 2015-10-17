package com.toe.shareyourcuisine.activity;

import android.content.Intent;

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
public class PostActivity extends BaseActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PostActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
