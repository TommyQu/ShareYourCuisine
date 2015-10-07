package com.toe.shareyourcuisine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.toe.shareyourcuisine.R;

/**
 * Created by kukentaira on 10/5/15.
 */
public class SignUpActivity extends BaseActivity {

    private FrameLayout mContentView;
    private BootstrapButton mCancelBtn;
    private DatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentView = (FrameLayout)findViewById(R.id.content);
        View child = getLayoutInflater().inflate(R.layout.activity_sign_up, null);
        mContentView.addView(child);

        mDatePicker = (DatePicker)findViewById(R.id.dob_picker);
        mCancelBtn = (BootstrapButton)findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mDatePicker.init(2000, 0, 0, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
//                Toast.makeText(SignUpActivity.this, format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
