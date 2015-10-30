package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.User;
import com.toe.shareyourcuisine.service.UserService;

import java.util.Date;

/**
 * Created by kukentaira on 10/5/15.
 */
public class SignUpActivity extends BaseActivity implements UserService.UserSignUpListener{

    private static final String TAG = "ToeSignUpActivity";
    private FrameLayout mContentView;

    private EditText mUserEmailValue;
    private EditText mUserNickNameValue;
    private EditText mUserPwdValue;
    private EditText mUserConfirmPwdValue;
    private RadioGroup mBtnGroup;
    private String mUserGender;
    private DatePicker mDobPicker;
    private Date mUserDob;
    private EditText mUserDesValue;
    private Button mSubmitBtn;
    private Button mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_sign_up, null);
        mContentView.addView(child);

        mUserEmailValue = (EditText)findViewById(R.id.user_email_value);
        mUserNickNameValue = (EditText)findViewById(R.id.user_nick_name_value);
        mUserPwdValue = (EditText)findViewById(R.id.user_pwd_value);
        mUserConfirmPwdValue = (EditText)findViewById(R.id.user_confirm_pwd_value);
        mBtnGroup = (RadioGroup)findViewById(R.id.btn_group);
        mDobPicker = (DatePicker)findViewById(R.id.dob_picker);
        mUserDesValue = (EditText)findViewById(R.id.user_des_value);

        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check() == true) {
                    User user = new User();
                    user.setUserEmail(mUserEmailValue.getText().toString());
                    user.setUserName(mUserNickNameValue.getText().toString());
                    user.setUserPwd(mUserPwdValue.getText().toString());
                    user.setUserGender(mUserGender);
                    user.setUserDob(mUserDob);
                    user.setUserDescription(mUserDesValue.getText().toString());
                    UserService userService = new UserService(SignUpActivity.this, SignUpActivity.this, "SignUp");
                    userService.signUp(user);
                }
            }
        });

        mCancelBtn = (Button)findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private boolean check() {
        if(TextUtils.isEmpty(mUserEmailValue.getText().toString())
                || TextUtils.isEmpty(mUserNickNameValue.getText().toString())
                || TextUtils.isEmpty(mUserPwdValue.getText().toString())
                || mBtnGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SignUpActivity.this, "Please fill required text fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mUserPwdValue.getText().toString().equals(mUserConfirmPwdValue.getText().toString()) == false) {
            Toast.makeText(SignUpActivity.this, "Password doesn't match confirm password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            RadioButton radioButton = (RadioButton) findViewById(mBtnGroup.getCheckedRadioButtonId());
            mUserGender = radioButton.getText().toString();
//            mUserDob = (mDobPicker.getYear()-1900)+"-"+mDobPicker.getMonth()+""
            mUserDob = new Date();
            mUserDob.setYear(mDobPicker.getYear()-1900);
            mUserDob.setMonth(mDobPicker.getMonth());
            mUserDob.setDate(mDobPicker.getDayOfMonth());
//            Log.d(TAG, mUserDob.toString());
            return true;
        }
    }

    @Override
    public void signUpSuccess() {
        Toast.makeText(SignUpActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void signUpFail() {
        Toast.makeText(SignUpActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
    }
}
