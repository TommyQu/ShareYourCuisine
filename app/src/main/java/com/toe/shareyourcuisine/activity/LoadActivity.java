package com.toe.shareyourcuisine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.toe.shareyourcuisine.R;

/**
 * Created by TommyQu on 11/7/15.
 */
public class LoadActivity extends Activity {

    private static final int LOAD_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_load);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.load_main_anim_in, R.anim.load_main_anim_out);
                finish();
            }
        }, LOAD_TIME);
    }

}
