package com.toe.shareyourcuisine.model;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TommyQu on 11/3/15.
 */
public class Activity {

    String mObjectId;
    String mTitle;
    Date createdAt;
    Date startTime;
    Date endTime;
    String content;
    String address;
    ParseGeoPoint geoPoint;
    ParseUser hostedBy;
    ArrayList<ParseUser> joinedBy;

    public Activity() {
        joinedBy = new ArrayList<ParseUser>();
    }

    public String getmObjectId() {
        return mObjectId;
    }

    public void setmObjectId(String mObjectId) {
        this.mObjectId = mObjectId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ParseGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(ParseGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public ParseUser getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(ParseUser hostedBy) {
        this.hostedBy = hostedBy;
    }

    public ArrayList<ParseUser> getJoinedBy() {
        return joinedBy;
    }

    public void setJoinedBy(ArrayList<ParseUser> joinedBy) {
        this.joinedBy = joinedBy;
    }
}
