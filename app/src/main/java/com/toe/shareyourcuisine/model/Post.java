package com.toe.shareyourcuisine.model;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Theon_Z on 10/31/15.
 */
public class Post {
    private String mPostId;
    private Date mCreatedAt;
    private Date mUpdatedAt;
    private ParseUser mCreatedBy;
    private String mContent;
    private ArrayList<ParseFile> mImg = new ArrayList<ParseFile>();

    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
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
