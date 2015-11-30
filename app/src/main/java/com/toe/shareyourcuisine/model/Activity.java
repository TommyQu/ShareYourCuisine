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
    Date mCreatedAt;
    Date mStartTime;
    Date mEndTime;
    String mContent;
    String mAddress;
    String mCity;
    String mState;
    String mZipCode;
    ParseGeoPoint mGeoPoint;
    ParseUser mCreatedBy;
    ArrayList<ParseUser> mJoinedBy;
    Integer mJoinedNum;
    String mStatus;

    public Activity() {
        mGeoPoint = new ParseGeoPoint();
        mCreatedBy = new ParseUser();
        mJoinedBy = new ArrayList<ParseUser>();
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

    public Date getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public Date getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Date getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(Date mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public ParseGeoPoint getmGeoPoint() {
        return mGeoPoint;
    }

    public void setmGeoPoint(ParseGeoPoint mGeoPoint) {
        this.mGeoPoint = mGeoPoint;
    }

    public ArrayList<ParseUser> getmJoinedBy() {
        return mJoinedBy;
    }

    public void setmJoinedBy(ArrayList<ParseUser> mJoinedBy) {
        this.mJoinedBy = mJoinedBy;
    }

    public ParseUser getmCreatedBy() {
        return mCreatedBy;
    }

    public void setmCreatedBy(ParseUser mCreatedBy) {
        this.mCreatedBy = mCreatedBy;
    }

    public Integer getmJoinedNum() {
        return mJoinedNum;
    }

    public void setmJoinedNum(Integer mJoinedNum) {
        this.mJoinedNum = mJoinedNum;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
