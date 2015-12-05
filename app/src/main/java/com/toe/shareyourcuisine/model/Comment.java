package com.toe.shareyourcuisine.model;

import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Theon_Z on 12/4/15.
 */
public class Comment {
    private String mObjectId;
    private Date mCreatedAt;
    private ParseUser mCreatedBy;
    private String mContent;

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
}
