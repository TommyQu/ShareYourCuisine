package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.toe.shareyourcuisine.model.Activity;

/**
 * Created by TommyQu on 11/5/15.
 */
public class ActivityService {

    private static final String TAG = "ToeActivityService";
    private Context mContext;
    private String mAction;
    private AddActivityListener mAddActivityListener;

    public interface AddActivityListener {
        public void addActivitySuccess();
        public void addActivityFail(String errorMsg);
    }

    public ActivityService(Context context, Object activityListener, String action) {
        mContext = context;
        if(action.equals("addActivity")) {
            mAddActivityListener = (AddActivityListener)activityListener;
        }
    }

    public void addActivity(Activity activity) {
        ParseObject activityObject = new ParseObject("Activity");
        activityObject.put("title", activity.getmTitle());
        activityObject.put("address", activity.getmAddress());
        activityObject.put("city", activity.getmCity());
        activityObject.put("state", activity.getmState());
        activityObject.put("zipCode", activity.getmZipCode());
        activityObject.put("content", activity.getmContent());
        activityObject.put("startTime", activity.getmStartTime());
        activityObject.put("endTime", activity.getmEndTime());
        activityObject.put("geoPoint", activity.getmGeoPoint());
        activityObject.put("createdBy", activity.getmCreatedBy());
        activityObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    mAddActivityListener.addActivitySuccess();
                }
                //Add post failed.
                else {
                    mAddActivityListener.addActivityFail(e.getMessage().toString());
                }
            }
        });
    }
}
