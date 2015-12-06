package com.toe.shareyourcuisine.model;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Theon_Z on 10/31/15.
 */
public class Post {
    private String mObjectId;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private ParseUser mCreatedBy;
    private String mContent;
    private ArrayList<ParseFile> mImg;
    private ArrayList<ParseObject> mComments;

    public ArrayList<ParseObject> getComments() {
        return mComments;
    }

    public void setComments(ArrayList<ParseObject> comments) {
        mComments = comments;
    }

    public Post() {
        mImg = new ArrayList<ParseFile>();
    }

    public String getObjectId() {
        return mObjectId;
    }

    public void setObjectId(String objectId) {
        mObjectId = objectId;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public ParseUser getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(ParseUser createdBy) {
        mCreatedBy = createdBy;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public ArrayList<ParseFile> getImg() {
        return mImg;
    }

    public void setImg(ArrayList<ParseFile> img) {
        mImg = img;
    }
}
