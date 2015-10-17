package com.toe.shareyourcuisine.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
    private boolean isSignUp;

    public UserService(Context context) {
        mContext = context;
    }

    public boolean signUp(User user) {
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
                    isSignUp = true;
                    Log.d(TAG, "done:" + String.valueOf(isSignUp));
                }
                //Sign up failed.
                else {
                    isSignUp = false;
                }
            }
        });
        Log.d(TAG, String.valueOf(isSignUp));
        return isSignUp;
    }

}
