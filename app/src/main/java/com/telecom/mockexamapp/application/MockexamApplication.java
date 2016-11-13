package com.telecom.mockexamapp.application;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2016/8/19.
 */

public class MockexamApplication extends Application {
    private static Context APPLACTION_CONTEXT;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(this);
        APPLACTION_CONTEXT = getApplicationContext();
    }
    public static Context getContext(){
        return APPLACTION_CONTEXT;
    }
}
