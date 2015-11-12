package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.toe.shareyourcuisine.model.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TommyQu on 11/5/15.
 */
public class ActivityService {

    private static final String TAG = "ToeActivityService";
    private Context mContext;
    private String mAction;
    private AddActivityListener mAddActivityListener;
    private GetAllActivitiesListener mGetAllActivitiesListener;

    public interface AddActivityListener {
        public void addActivitySuccess();
        public void addActivityFail(String errorMsg);
    }

    public interface GetAllActivitiesListener {
        public void getAllActivitiesSuccess(List<Activity> activities);
        public void getAllActivitiesFail(String errorMsg);
    }

    public ActivityService(Context context, Object activityListener, String action) {
        mContext = context;
        if (action.equals("addActivity")) {
            mAddActivityListener = (AddActivityListener)activityListener;
        }
        else if (action.equals("getAllActivities")) {
            mGetAllActivitiesListener = (GetAllActivitiesListener)activityListener;
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

    public void getAllActivities() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Activity");
        query.include("createdBy");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    List<Activity> activities = new ArrayList<Activity>();
                    for(int i = 0; i < list.size(); i++) {
                        Activity activity = new Activity();
                        activity.setmObjectId(list.get(i).getObjectId());
                        activity.setmCreatedBy((ParseUser) list.get(i).get("createdBy"));
                        activity.setmTitle((String) list.get(i).get("title"));
                        activity.setmStartTime((Date) list.get(i).get("startTime"));
                        activity.setmEndTime((Date) list.get(i).get("endTime"));
                        activity.setmAddress((String) list.get(i).get("address"));
                        activity.setmCity((String) list.get(i).get("city"));
                        activity.setmState((String) list.get(i).get("state"));
                        activities.add(activity);
                    }
                    mGetAllActivitiesListener.getAllActivitiesSuccess(activities);
                }
                else {
                    mGetAllActivitiesListener.getAllActivitiesFail(e.getMessage().toString());
                }
            }
        });
    }
}
