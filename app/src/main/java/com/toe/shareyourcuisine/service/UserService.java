package com.toe.shareyourcuisine.service;

import android.content.Context;

import com.parse.LogInCallback;
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
    private UserSignUpListener mUserSignUpListener;
    private UserLoginListener mUserLoginListener;
    private String mAction;
    private boolean isSignUp;

    public interface UserSignUpListener {
        public void signUpSuccess();
        public void signUpFail();
    }

    public interface UserLoginListener {
        public void loginSuccess();
        public void loginFail();
    }

    //Action is to identify different actions and implement different listeners
    public UserService(Context context, String action, Object userListener) {
        mContext = context;
        if(action.equals("SignUp")) {
            mUserSignUpListener = (UserSignUpListener)userListener;
        }
        else if(action.equals("Login")) {
            mUserLoginListener = (UserLoginListener)userListener;
        }
    }

    public void signUp(User user) {
        ParseUser parseUser = new ParseUser();
        parseUser.put("userEmail", user.getUserEmail());
        //Take userEmail as ParseUser username
        parseUser.setUsername(user.getUserEmail());
        parseUser.put("nickName", user.getUserName());
        parseUser.setPassword(user.getUserPwd());
        parseUser.put("userGender", user.getUserGender());
        parseUser.put("userDob", user.getUserDob());
        parseUser.put("userDescription", user.getUserDescription());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //Sign up successfully.
                if (e == null) {
                    mUserSignUpListener.signUpSuccess();
                    isSignUp = true;
                }
                //Sign up failed.
                else {
                    mUserSignUpListener.signUpFail();
                    isSignUp = false;
                }
            }
        });
    }

    public void login(String userEmail, String password) {
        //Take userEmail as ParseUser username
        ParseUser.logInInBackground(userEmail, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                //Login successfully;
                if(parseUser != null) {
                    mUserLoginListener.loginSuccess();
                }
                //Login failed
                else {
                    mUserLoginListener.loginFail();
                }
            }
        });
    }
}
