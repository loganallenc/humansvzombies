package com.crusoe.humanzombie;


import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    //sprivate static GPSTracker tracker;
    
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

        Parse.initialize(this, "v4bNZol2bilO5zypEAw7utrlB97XadvqCWrWBzaw", "ZgQQxUNWdVZE7wo59g3RmXU9881zOEmwT1uAaqno");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();
        
        //tracker = new GPSTracker(this);
    }
    
    //public static GPSTracker getTracker(){
    //	return tracker;
    //}
}
