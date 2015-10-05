package com.toe.shareyourcuisine;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

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
}
