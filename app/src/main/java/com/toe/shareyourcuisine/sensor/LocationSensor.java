package com.toe.shareyourcuisine.sensor;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by TommyQu on 11/7/15.
 */
public class LocationSensor implements LocationListener{

    private static final String TAG = "ToeLocationSensor";
    private Context mContext;
    private LocationSensorListener mLocationSensorListener;
    private LocationManager mLocationManager;
    private Location mLocation;

    public LocationSensor(Context context, LocationSensorListener locationSensorListener) {
        mContext = context;
        mLocationSensorListener = locationSensorListener;
    }

    public interface LocationSensorListener {
        public void getLocationSuccess(Location location);
        public void getLocationFail(String errorMsg);
    }

    public void getLocation() {
        if(mLocationManager == null)
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(mLocation != null)
            mLocationSensorListener.getLocationSuccess(mLocation);
        else
            mLocationSensorListener.getLocationFail("Fail to get GPS location!");
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
