package com.toe.shareyourcuisine.activity;

import android.app.Activity;
import android.content.Intent;

/**
 * Project: Share Your Cuisine
 * Comments: Activity screen's activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class ActivityActivity extends BaseActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
