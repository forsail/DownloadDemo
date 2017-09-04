package com.downloaddemo.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.downloaddemo.activity.AvqUtils;

public class MemCacheManager
{
	private final static int MaxBufferLength = AvqUtils.context.isAboveVersionHONEYCOMB() ? 4 * 1024 * 1024
			: 2 * 1024 * 1024;

	private LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(MaxBufferLength)
	{
		protected int sizeOf(String key, Bitmap bmp)
		{
			return bmp.getRowBytes() * bmp.getHeight();
		}
	};
	private static MemCacheManager sharedInstance = null;

	public static synchronized MemCacheManager get()
	{
		if (sharedInstance == null)
		{
			sharedInstance = new MemCacheManager();
		}

		return sharedInstance;
	}

	private MemCacheManager()
	{

	}

	public Bitmap getCacheImage(String url)
	{
		Bitmap bmp = null;
		synchronized (mImageCache)
		{
			bmp = mImageCache.get(url);
		}
		return bmp;
	}

	public void addCacheImage(String url, Bitmap bmp)
	{
		synchronized (mImageCache)
		{
			mImageCache.put(url, bmp);
		}
	}

}
