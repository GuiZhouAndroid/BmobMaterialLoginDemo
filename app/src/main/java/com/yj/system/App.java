package com.yj.system;

import android.app.Application;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;

public class App extends Application {
    private static App myApp;

    public static ClearableCookieJar cookie;


    public static App getInstance() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
