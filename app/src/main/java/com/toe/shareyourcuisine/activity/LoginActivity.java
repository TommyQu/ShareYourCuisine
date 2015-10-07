package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.toe.shareyourcuisine.R;

/**
 * Project: Share Your Cuisine
 * Comments: Home screen activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class LoginActivity extends BaseActivity {

    private FrameLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_login, null);
        mContentView.addView(child);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
