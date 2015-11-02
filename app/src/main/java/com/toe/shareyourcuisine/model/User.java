package com.toe.shareyourcuisine.model;

import com.parse.ParseFile;

import java.util.Date;

/**
 * Created by TommyQu on 10/16/15.
 */

/**
 * Modified by Theon_Z on 10/31/15.
 */

public class User {

    private String mUserId;
    private String mUserEmail;
    private String mUserName;
    private String mUserPwd;
    private String mUserGender;
    private Date mUserDob;
    private String mUserDescription;
    private ParseFile mUserImg;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPwd() {
        return mUserPwd;
    }

    public void setUserPwd(String userPwd) {
        mUserPwd = userPwd;
    }

    public String getUserGender() {
        return mUserGender;
    }

    public void setUserGender(String userGender) {
        mUserGender = userGender;
    }

    public Date getUserDob() {
        return mUserDob;
    }

    public void setUserDob(Date userDob) {
        mUserDob = userDob;
    }

    public String getUserDescription() {
        return mUserDescription;
    }

    public void setUserDescription(String userDescription) {
        mUserDescription = userDescription;
    }

    public ParseFile getUserImg() {
        return mUserImg;
    }

    public void setUserImg(ParseFile userImg) {
        mUserImg = userImg;
    }
}
