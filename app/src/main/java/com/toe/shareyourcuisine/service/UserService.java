package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.toe.shareyourcuisine.model.User;

/**
 * Created by TommyQu on 10/16/15.
 */
public class UserService {

    private static final String TAG = "ToeUserService";
    private Context mContext;
    private UserServiceListener mUserServiceListener;
    private String mAction;
    private boolean isSignUp;

    public interface UserServiceListener {
        public void signUpSuccess();
        public void signUpFail();
    }

    public UserService(Context context, UserServiceListener userServiceListener) {
        mContext = context;
        mUserServiceListener = userServiceListener;
    }

    public void signUp(User user) {
        ParseUser parseUser = new ParseUser();
        parseUser.put("userEmail", user.getUserEmail());
        parseUser.setUsername(user.getUserName());
        parseUser.setPassword(user.getUserPwd());
        parseUser.put("userGender", user.getUserGender());
        parseUser.put("userDob", user.getUserDob());
        parseUser.put("userDescription", user.getUserDescription());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //Sign up successfully.
                if (e == null) {
                    mUserServiceListener.signUpSuccess();
                    isSignUp = true;
                }
                //Sign up failed.
                else {
                    mUserServiceListener.signUpFail();
                    isSignUp = false;
                }
            }
        });
    }

}
