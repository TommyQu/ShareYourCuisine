package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.adapter.MenuArrayAdapter;
import com.toe.shareyourcuisine.service.MenuService;

import java.util.List;

/**
 * Project: Share Your Cuisine
 * Comments: Menu screen's activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class MenuActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        MenuService.GetAllMenusListener{

    private final String TAG = "ToeMenuActivity";
    private FrameLayout mContentView;
    private MenuArrayAdapter mMenuArrayAdapter;
    private ListView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private MenuService mMenuService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_menu, null);
        mContentView.addView(child);
        mListView = (ListView)findViewById(R.id.menu_list_view);
        mSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorScheme(android.R.color.white,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mMenuService = new MenuService(MenuActivity.this, MenuActivity.this, "getAllMenus");
        mMenuService.getAllMenus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.new_menu) {
            if(ParseUser.getCurrentUser() == null) {
                Toast.makeText(MenuActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MenuActivity.this, AddMenuActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void getAllMenusSuccess(List<com.toe.shareyourcuisine.model.Menu> menus) {
        mMenuArrayAdapter = new MenuArrayAdapter(MenuActivity.this, menus);
        mListView.setAdapter(mMenuArrayAdapter);
    }

    @Override
    public void getAllMenusFail(String errorMsg) {
        Toast.makeText(MenuActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
