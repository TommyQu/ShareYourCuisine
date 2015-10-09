package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.toe.shareyourcuisine.R;

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
public class LoginActivity extends BaseActivity {

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
                String id = "123";
                String userEmail = mUserEmailValue.getText().toString();
                String userPwd = mUserPwdValue.getText().toString();
                try {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                    query.whereEqualTo("user_email", userEmail);
                    query.whereEqualTo("user_pwd", userPwd);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (parseObject == null)
                                Toast.makeText(LoginActivity.this, "User email or password is incorrect!", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(LoginActivity.this, parseObject.getString("user_name") + " log in successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
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
}
