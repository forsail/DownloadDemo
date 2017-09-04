package com.downloaddemo.cache;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.downloaddemo.activity.AvqUtils;
import com.downloaddemo.activity.StringUtils;

public class MemSafeWeakMap<T>
{
	private Map<String, WeakReference<T>> mMap = new HashMap<String, WeakReference<T>>();
	private Lock mLock = new ReentrantLock();

	public MemSafeWeakMap()
	{

	}

	public void add(String url, T obj)
	{
		if (StringUtils.isNullOrEmpty(url) || obj == null)
		{
			return;
		}
		mLock.lock();
		mMap.put(url, new WeakReference<T>(obj));
		Set<String> keys = mMap.keySet();
		Map<String, WeakReference<T>> tmpMap = new HashMap<String, WeakReference<T>>();

		for (String tmpkey : keys)
		{
			if (AvqUtils.Weak.isValidWeak(mMap.get(tmpkey)))
			{
				tmpMap.put(tmpkey, mMap.get(tmpkey));
			}
			else
			{
				// System.out.println("frank release buffer:" + tempkey);
			}
		}

		mMap = tmpMap;

		// System.out.println("frank buffer size:"+m_Buffer.size());

		mLock.unlock();

	}
	
	public T get(String url)
	{
		T obj = null;
		if (!StringUtils.isNullOrEmpty(url))
		{
			mLock.lock();
			WeakReference<T> objWeak = mMap.get(url);
			if (AvqUtils.Weak.isValidWeak(objWeak))
			{
				obj = objWeak.get();
			}
			mLock.unlock();
		}
		return obj;
	}
	
	public void clear()
	{
		mMap.clear();
	}
}
