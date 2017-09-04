package com.downloaddemo.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;

public class MemObjectList<T>
{
	public static final int LIMIT_SIZE = 512 * 1024;
	private List<T> mList = new ArrayList<T>();
	private Map<T, Integer> mMap = new HashMap<T, Integer>();
	private Lock mlock = new ReentrantLock();
	private int mLimitLength = -1;
	private int currentDataLength = 0;

	public MemObjectList(int size)
	{
		this.mLimitLength = size;
	}

	@SuppressLint("NewApi")
	public void add(T obj, int size)
	{
		if (size > LIMIT_SIZE)
		{
			return;
		}
		if (obj != null)
		{
			if (mMap.get(obj) != null)
			{
				return;
			}
			mlock.lock();
			refreshDataLenth(size);
			mMap.put(obj, size);
			currentDataLength += size;
			mlock.unlock();
		}
	}

	private void refreshDataLenth(int size)
	{
		if (currentDataLength + size > mLimitLength)
		{
			T tmp = mList.get(0);
			mList.remove(0);
			currentDataLength -= mMap.get(tmp);
			mMap.remove(tmp);
			refreshDataLenth(size);
		}
	}

}
