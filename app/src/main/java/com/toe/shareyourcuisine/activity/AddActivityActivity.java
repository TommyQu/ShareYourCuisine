package com.toe.shareyourcuisine.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.toe.shareyourcuisine.R;
import com.toe.shareyourcuisine.model.Activity;
import com.toe.shareyourcuisine.sensor.LocationSensor;
import com.toe.shareyourcuisine.service.ActivityService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by TommyQu on 11/3/15.
 */
public class AddActivityActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, ActivityService.AddActivityListener, LocationSensor.LocationSensorListener{

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
    private Date mStartTime;
    private Date mEndTime;
    private String mDateStr;
    private String mTimeStr;
    private ParseGeoPoint mGeoPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        getSupportActionBar().setTitle("New Activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Calendar calendar = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        mTimePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        mGeoPoint = new ParseGeoPoint();
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
                //If user hasn't logged in
                if(ParseUser.getCurrentUser() == null) {
                    Toast.makeText(AddActivityActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Implement add activity
                if(check() == true) {
                    LocationSensor locationSensor = new LocationSensor(AddActivityActivity.this, AddActivityActivity.this);
                    locationSensor.getLocation();
                    Activity activity = new Activity();
                    activity.setmTitle(mTitleValue.getText().toString());
                    activity.setmAddress(mAddressValue.getText().toString());
                    activity.setmContent(mContentValue.getText().toString());
                    activity.setmStartTime(mStartTime);
                    activity.setmEndTime(mEndTime);
                    activity.setmGeoPoint(mGeoPoint);
                    activity.setmCreatedBy(ParseUser.getCurrentUser());
                    ActivityService activityService = new ActivityService(AddActivityActivity.this, AddActivityActivity.this, "addActivity");
                    activityService.addActivity(activity);
                }
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

    //Check the validation of input parameters
    private boolean check() {
        if(mTitleValue.getText().toString().equals("")
                || mAddressValue.getText().toString().equals("")
                || mContentValue.getText().toString().equals("")) {
            Toast.makeText(AddActivityActivity.this, "Please fill required text fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mStartDateValue.getText().toString().equals("Remain to be set")
                || mStartTimeValue.getText().toString().equals("Remain to be set")
                || mEndDateValue.getText().toString().equals("Remain to be set")
                || mEndTimeValue.getText().toString().equals("Remain to be set")) {
            Toast.makeText(AddActivityActivity.this, "Please set date and time!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            mStartTime = new Date();
            mEndTime = new Date();
            mStartTime.setYear(Integer.valueOf(mStartDateValue.getText().toString().split("-")[0]));
            mStartTime.setMonth(Integer.valueOf(mStartDateValue.getText().toString().split("-")[1]));
            mStartTime.setDate(Integer.valueOf(mStartDateValue.getText().toString().split("-")[2]));
            mStartTime.setHours(Integer.valueOf(mStartTimeValue.getText().toString().split("-")[0]));
            mStartTime.setMinutes(Integer.valueOf(mStartTimeValue.getText().toString().split("-")[1]));
            mEndTime.setYear(Integer.valueOf(mEndDateValue.getText().toString().split("-")[0]));
            mEndTime.setMonth(Integer.valueOf(mEndDateValue.getText().toString().split("-")[1]));
            mEndTime.setDate(Integer.valueOf(mEndDateValue.getText().toString().split("-")[2]));
            mEndTime.setHours(Integer.valueOf(mEndTimeValue.getText().toString().split("-")[0]));
            mEndTime.setMinutes(Integer.valueOf(mEndTimeValue.getText().toString().split("-")[1]));
            //Check whether end time is after start time
            if(mEndTime.before(mStartTime)) {
                Toast.makeText(AddActivityActivity.this, "End time must after start time!", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
                return true;
        }
    }

    //Set start date and time, end date and time
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

    @Override
    public void addActivitySuccess() {
        Toast.makeText(AddActivityActivity.this, "Add activity successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void addActivityFail(String errorMsg) {
        Toast.makeText(AddActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getLocationSuccess(Location location) {
        mGeoPoint.setLatitude(location.getLatitude());
        mGeoPoint.setLongitude(location.getLongitude());
    }

    @Override
    public void getLocationFail(String errorMsg) {
        Toast.makeText(AddActivityActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
