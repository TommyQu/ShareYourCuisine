package com.toe.shareyourcuisine;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by TommyQu on 10/8/15.
 */
public class SycApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "azue2TtJIXAFTrqD3SpEjYGjHU8ImgXPatmlZzHg", "DM8kgbiabchxLYealzGJaXmrjmoEVEm8ReGEQszk");
    }

}
