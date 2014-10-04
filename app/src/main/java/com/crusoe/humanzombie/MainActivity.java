package com.crusoe.humanzombie;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import java.util.List;


public class MainActivity extends Activity {

    ParseUser user;
    ParseGeoPoint geo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpened(getIntent());

        user = ParseUser.getCurrentUser();

        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");

        for (String i : subscribedChannels) {
            Log.d("channels", i);
        }

        nearMe();
        authTwitter();
    }

    public void nearMe() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        geo = new ParseGeoPoint(latitude, longitude);

        //geo = user.getParseGeoPoint("location");
        user.put("location", geo);
        user.saveEventually();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("location");
        query.whereNear("location", geo);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (ParseObject p : parseObjects) {
                        Log.d("near", p.getObjectId());
                    }
                } else {
                    System.out.println(e);
                }
            }
        });
    }


    public void authTwitter() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("TwitterAuth", "Uh oh. The user cancelled the Twitter login.");
                } else if (user.isNew()) {
                    Log.d("TwitterAuth", "User signed up and logged in through Twitter!");
                } else {
                    Log.d("TwitterAuth", "User logged in through Twitter!");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
