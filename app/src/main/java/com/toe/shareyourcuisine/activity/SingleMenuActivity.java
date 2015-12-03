package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.toe.shareyourcuisine.R;

/**
 * Created by TommyQu on 12/3/15.
 */
public class SingleMenuActivity extends ActionBarActivity {

    private static final String TAG = "ToeSingleMenu";
    private String mMenuId;
    private String mMenuTitle;
    private String mUserName;

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
}
