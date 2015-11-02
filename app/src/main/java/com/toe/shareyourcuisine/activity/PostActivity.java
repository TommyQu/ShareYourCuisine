package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.toe.shareyourcuisine.R;

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

    private final String TAG = "ToePostActivity";
    private FrameLayout mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_post, null);
        mContentView.addView(child);
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.new_post) {
            Intent intent = new Intent(PostActivity.this, NewPostActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
