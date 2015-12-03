package com.toe.shareyourcuisine.model;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TommyQu on 12/2/15.
 */

public class Menu {

    private String mObjectId;
    private String mTitle;
    private ParseFile mDisplayImg;
    private String mContent;
    private ArrayList<ParseFile> mImg;
    private Date mCreatedAt;
    private ParseUser mCreatedBy;
    private ArrayList<ParseObject> mComments;

    public Menu() {
        mImg = new ArrayList<ParseFile>();
        mCreatedBy = new ParseUser();
        mComments = new ArrayList<ParseObject>();
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

    public ParseFile getmDisplayImg() {
        return mDisplayImg;
    }

    public void setmDisplayImg(ParseFile mDisplayImg) {
        this.mDisplayImg = mDisplayImg;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public ArrayList<ParseFile> getmImg() {
        return mImg;
    }

    public void setmImg(ArrayList<ParseFile> mImg) {
        this.mImg = mImg;
    }

    public Date getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(Date mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public ParseUser getmCreatedBy() {
        return mCreatedBy;
    }

    public void setmCreatedBy(ParseUser mCreatedBy) {
        this.mCreatedBy = mCreatedBy;
    }

    public ArrayList<ParseObject> getmComments() {
        return mComments;
    }

    public void setmComments(ArrayList<ParseObject> mComments) {
        this.mComments = mComments;
    }
}
