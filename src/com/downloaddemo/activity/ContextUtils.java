package com.downloaddemo.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class ContextUtils extends Application
{
    private static Context context;

    public static Context getSharedContext()
    {
        return context;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
    }
    
    protected void ApplicationDidEnterForeground(Activity act)
    {
    }

    protected void ApplicationDidEnterBackground(Activity act)
    {
    }

}
