package com.downloaddemo.thread;

public abstract class DefaultOperation implements Runnable
{
	private Thread currentThread;
	
	public abstract void doInBackground();
	
	@Override
	public void run()
	{
		currentThread = Thread.currentThread();
		doInBackground();
		onFinish();
	}
	
	private void onFinish()
	{
		
	}
}