package com.downloaddemo.activity;

import android.app.Activity;

public class DownloadApplication extends ContextUtils
{
	public long lastTimeStamp;

	@Override
	public void onCreate()
	{
		super.onCreate();
	}

	@Override
	protected void ApplicationDidEnterForeground(Activity act)
	{
		super.ApplicationDidEnterForeground(act);
	}

	@Override
	protected void ApplicationDidEnterBackground(Activity act)
	{
		super.ApplicationDidEnterBackground(act);
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
	}
}