package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.service.UserService;

/**
 * Project: Share Your Cuisine
 * Comments: Home screen activity
 * Compile SDK version: 22
 * Author: Tommy Qu
 * Created Date: 09/14/2015
 * Modified By:
 * Modified Date:
 * Why is modified:
 */
public class LoginActivity extends BaseActivity implements UserService.UserLoginListener{

    private final String TAG = "ToeLoginActivity";
    private FrameLayout mContentView;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private EditText mUserEmailValue;
    private EditText mUserPwdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_login, null);
        mContentView.addView(child);
        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mUserEmailValue = (EditText)findViewById(R.id.user_email_value);
        mUserPwdValue = (EditText)findViewById(R.id.user_pwd_value);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mUserEmailValue.getText().toString();
                String userPwd = mUserPwdValue.getText().toString();
                UserService userService = new UserService(LoginActivity.this, "Login", LoginActivity.this);
                userService.login(userEmail, userPwd);
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(LoginActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void loginFail() {
        Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
    }
}
