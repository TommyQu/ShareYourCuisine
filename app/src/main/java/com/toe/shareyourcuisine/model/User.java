package com.toe.shareyourcuisine.model;

import com.parse.ParseFile;

import java.util.Date;

/**
 * Created by TommyQu on 10/16/15.
 */
public class User {

    private String userId;
    private String userEmail;
    private String userName;
    private String userPwd;
    private String userGender;
    private Date userDob;
    private String userDescription;
    private ParseFile userImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Date getUserDob() {
        return userDob;
    }

    public void setUserDob(Date userDob) {
        this.userDob = userDob;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public ParseFile getUserImg() {
        return userImg;
    }

    public void setUserImg(ParseFile userImg) {
        this.userImg = userImg;
    }
}
