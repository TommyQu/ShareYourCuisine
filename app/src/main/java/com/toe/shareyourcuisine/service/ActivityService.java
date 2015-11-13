package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.GetCallback;
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
    private GetSingleActivityListener mGetSingleActivityListener;
    private JoinActivityListener mJoinActivityListener;

    public interface AddActivityListener {
        public void addActivitySuccess();
        public void addActivityFail(String errorMsg);
    }

    public interface GetAllActivitiesListener {
        public void getAllActivitiesSuccess(List<Activity> activities);
        public void getAllActivitiesFail(String errorMsg);
    }

    public interface GetSingleActivityListener {
        public void getSingleActivitySuccess(Activity activity);
        public void getSingleActivityFail(String errorMsg);
    }

    public interface JoinActivityListener {
        public void joinActivitySuccess();
        public void joinActivityFail(String errorMsg);
    }

    public ActivityService(Context context, Object activityListener, String action) {
        mContext = context;
        if (action.equals("addActivity")) {
            mAddActivityListener = (AddActivityListener)activityListener;
        }
        else if (action.equals("getAllActivities")) {
            mGetAllActivitiesListener = (GetAllActivitiesListener)activityListener;
        }
        else if (action.equals("getSingleActivity")) {
            mGetSingleActivityListener = (GetSingleActivityListener)activityListener;
        }
        else if (action.equals("joinActivity")) {
            mJoinActivityListener = (JoinActivityListener) activityListener;
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
                    for (int i = 0; i < list.size(); i++) {
                        Activity activity = new Activity();
                        activity.setmObjectId(list.get(i).getObjectId());
                        activity.setmCreatedBy(list.get(i).getParseUser("createdBy"));
                        activity.setmTitle((String) list.get(i).get("title"));
                        activity.setmStartTime((Date) list.get(i).get("startTime"));
                        activity.setmEndTime((Date) list.get(i).get("endTime"));
                        activity.setmAddress((String) list.get(i).get("address"));
                        activity.setmCity((String) list.get(i).get("city"));
                        activity.setmState((String) list.get(i).get("state"));
                        activities.add(activity);
                    }
                    mGetAllActivitiesListener.getAllActivitiesSuccess(activities);
                } else {
                    mGetAllActivitiesListener.getAllActivitiesFail(e.getMessage().toString());
                }
            }
        });
    }

    public void getSingleActivity(String activityId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Activity");
        query.include("createdBy");
        query.whereEqualTo("objectId", activityId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Activity activity = new Activity();
                    activity.setmTitle(parseObject.get("title").toString());
                    activity.setmCreatedBy((ParseUser) parseObject.get("createdBy"));
                    activity.setmAddress((String) parseObject.get("address"));
                    activity.setmCity((String) parseObject.get("city"));
                    activity.setmState((String) parseObject.get("state"));
                    activity.setmZipCode((String) parseObject.get("zipCode"));
                    activity.setmStartTime((Date) parseObject.get("startTime"));
                    activity.setmEndTime((Date) parseObject.get("endTime"));
                    activity.setmContent((String) parseObject.get("content"));
                    mGetSingleActivityListener.getSingleActivitySuccess(activity);
                } else {
                    mGetSingleActivityListener.getSingleActivityFail(e.getMessage().toString());
                }
            }
        });
    }

    public void joinActivity(String activityId, final ParseUser parseUser) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Activity");
        query.whereEqualTo("objectId", activityId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.addUnique("joinedBy", parseUser);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null)
                                mJoinActivityListener.joinActivitySuccess();
                            else
                                mJoinActivityListener.joinActivityFail(e.getMessage().toString());
                        }
                    });
                    mJoinActivityListener.joinActivitySuccess();
                } else {
                    mJoinActivityListener.joinActivityFail(e.getMessage().toString());
                }
            }
        });
    }
}
