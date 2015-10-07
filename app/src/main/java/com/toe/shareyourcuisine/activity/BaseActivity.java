package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.toe.shareyourcuisine.R;

/**
 * Project: Share Your Cuisine
 * Comments: Base activity for drawerlayout and actionbar
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/8/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class BaseActivity extends ActionBarActivity {

    protected ListView drawerList;
    private ArrayAdapter<String> drawerAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        drawerList = (ListView)findViewById(R.id.drawer_list);
        addDrawerItems();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();
        setupDrawer();
//        Parse.initialize(this, "azue2TtJIXAFTrqD3SpEjYGjHU8ImgXPatmlZzHg", "DM8kgbiabchxLYealzGJaXmrjmoEVEm8ReGEQszk");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    protected void addDrawerItems() {
        String[] osArray = { "Home", "Log in", "Sign Up", "Menu", "Post", "Activity"};
        drawerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        drawerList.setAdapter(drawerAdapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(BaseActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                System.out.println(position);
                Intent intent = new Intent();
                if (position == 0) {
                    intent.setClass(BaseActivity.this, MainActivity.class);
                }
                else if (position == 1) {
                    intent.setClass(BaseActivity.this, LoginActivity.class);
                }
                else if (position == 2) {
                    intent.setClass(BaseActivity.this, SignUpActivity.class);
                }
                else if (position == 3) {
                    intent.setClass(BaseActivity.this, MenuActivity.class);
                }
                else if (position == 4) {
                    intent.setClass(BaseActivity.this, PostActivity.class);
                }
                else if (position == 5) {
                    intent.setClass(BaseActivity.this, ActivityActivity.class);
                }
                finish();
                startActivity(intent);

            }
        });
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }
}
