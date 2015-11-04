package com.toe.shareyourcuisine.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.toe.shareyourcuisine.R;

import java.util.Calendar;

/**
 * Created by TommyQu on 11/3/15.
 */
public class NewActivityActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "ToeNewActivityActivity";
    private EditText mTitleValue;
    private EditText mAddressValue;
    private EditText mContentValue;
    private Button mStartDateBtn;
    private Button mStartTimeBtn;
    private Button mEndDateBtn;
    private Button mEndTimeBtn;
    private TextView mStartDateValue;
    private TextView mStartTimeValue;
    private TextView mEndDateValue;
    private TextView mEndTimeValue;
    private Button mSubmitBtn;
    private Button mCancelBtn;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private String mDateStr;
    private String mTimeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);
        getSupportActionBar().setTitle("New Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        mTimePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        mTitleValue = (EditText)findViewById(R.id.activity_title_value);
        mAddressValue = (EditText)findViewById(R.id.activity_address_value);
        mContentValue = (EditText)findViewById(R.id.activity_content_value);
        mStartDateBtn = (Button)findViewById(R.id.activity_start_date_btn);
        mStartTimeBtn = (Button)findViewById(R.id.activity_start_time_btn);
        mEndDateBtn = (Button)findViewById(R.id.activity_end_date_btn);
        mEndTimeBtn = (Button)findViewById(R.id.activity_end_time_btn);
        mStartDateValue = (TextView)findViewById(R.id.activity_start_date_value);
        mStartTimeValue = (TextView)findViewById(R.id.activity_start_time_value);
        mEndDateValue = (TextView)findViewById(R.id.activity_end_date_value);
        mEndTimeValue = (TextView)findViewById(R.id.activity_end_time_value);
        mSubmitBtn = (Button)findViewById(R.id.submit_btn);
        mCancelBtn = (Button)findViewById(R.id.cancel_btn);

        setDateTimePicker();

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag("datePicker");
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag("timePicker");
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if(mDateStr.equals("start"))
            mStartDateValue.setText(year + "-" + month + "-" + day);
        if(mDateStr.equals("end"))
            mEndDateValue.setText(year + "-" + month + "-" + day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if(mTimeStr.equals("start"))
            mStartTimeValue.setText(hourOfDay + "-" + minute);
        if(mTimeStr.equals("end"))
            mEndTimeValue.setText(hourOfDay + "-" + minute);
    }

    private boolean check() {
        return true;
    }

    private void setDateTimePicker() {
        mStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateStr = "start";
                mDatePickerDialog.setVibrate(false);
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mStartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeStr = "start";
                mTimePickerDialog.setVibrate(false);
                mTimePickerDialog.show(getSupportFragmentManager(), "timePicker");
            }
        });
        mEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateStr = "end";
                mDatePickerDialog.setVibrate(false);
                mDatePickerDialog.setYearRange(1985, 2028);
                mDatePickerDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });
        mEndTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeStr = "end";
                mTimePickerDialog.setVibrate(false);
                mTimePickerDialog.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }
}
