package com.downloaddemo.thread;

public interface Operation extends Runnable
{
	void cancelOperation();
}
