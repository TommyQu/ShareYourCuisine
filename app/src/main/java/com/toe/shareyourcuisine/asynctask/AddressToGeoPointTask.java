package com.toe.shareyourcuisine.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseGeoPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TommyQu on 11/9/15.
 */
public class AddressToGeoPointTask extends AsyncTask<String, Integer, ParseGeoPoint>{

    private static final String TAG = "ToeToGeoPointTask";
    private Context mContext;
    private AddressToGeoPointListener mAddressToGeoPointListener;
    private ParseGeoPoint mGeoPoint;
    private String mErrorMsg;

    public AddressToGeoPointTask(Context context, AddressToGeoPointListener addressToGeoPointListener) {
        mContext = context;
        mAddressToGeoPointListener = addressToGeoPointListener;
        mGeoPoint = new ParseGeoPoint();
    }

    public interface AddressToGeoPointListener {
        public void getGeoPointSuccess(ParseGeoPoint geoPoint);
        public void getGeoPointFail(String errorMsg);
    }

    @Override
    protected ParseGeoPoint doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer locationSB = new StringBuffer("");
            String totalInfo;
            while((line = br.readLine())!=null) {
                locationSB.append(line);
            }
            totalInfo = locationSB.toString();
            JSONObject jsonObject = new JSONObject(totalInfo);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            if(resultsArray.length() == 0) {
                mErrorMsg = "Please enter valid address";
            }
            else {
                JSONObject resultObject = resultsArray.getJSONObject(0);
                JSONObject geometryObject = resultObject.getJSONObject("geometry");
                JSONObject locationObject = geometryObject.getJSONObject("location");
                mGeoPoint.setLatitude(Double.parseDouble((locationObject.getString("lat"))));
                mGeoPoint.setLongitude(Double.parseDouble((locationObject.getString("lng"))));
            }
        }
        catch (Exception e) {
            mErrorMsg = e.getMessage().toString();
        }
        return mGeoPoint;
    }

    @Override
    protected void onPostExecute(ParseGeoPoint parseGeoPoint) {
        super.onPostExecute(parseGeoPoint);
        if(mAddressToGeoPointListener != null) {
            if(parseGeoPoint.getLatitude() != 0 && parseGeoPoint.getLongitude() != 0)
                mAddressToGeoPointListener.getGeoPointSuccess(parseGeoPoint);
            else
                mAddressToGeoPointListener.getGeoPointFail(mErrorMsg);
        }
    }
}
