package com.toe.shareyourcuisine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.toe.shareyourcuisine.R;

/**
 * Project: Share Your Cuisine
 * Comments: Home screen's activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/8/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */

public class MainActivity extends BaseActivity {
    private FrameLayout mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = (FrameLayout) findViewById(R.id.content);

        View child = getLayoutInflater().inflate(R.layout.activity_main, null);
        mContentView.addView(child);

//        ParseUser user = new ParseUser();
//        user.setUsername("my name");
//        user.setPassword("my pass");
//        user.setEmail("email@example.com");
//
//// other fields can be set just like with ParseObject
//        user.put("phone", "650-555-0000");
//
//        user.signUpInBackground(new SignUpCallback() {
//            public void done(ParseException e) {
//                if (e == null) {
//                    // Hooray! Let them use the app now.
//                } else {
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                }
//            }
//        });
    }

    @Override
    protected void addDrawerItems() {
        super.addDrawerItems();
    }
}
