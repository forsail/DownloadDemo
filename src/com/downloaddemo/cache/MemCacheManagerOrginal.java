package com.downloaddemo.cache;

import java.io.File;

import com.downloaddemo.activity.AvqUtils;

public class MemCacheManagerOrginal
{
	private final static int MaxBufferLength = AvqUtils.context.isAboveVersionHONEYCOMB() ? 3 * 1024 * 1024 : 1 * 1024 * 1024;
	
	private MemObjectList<byte[]> mStrongList = new MemObjectList<byte[]>(MaxBufferLength);
	private MemSafeWeakMap<byte[]> mWeakMap = new MemSafeWeakMap<byte[]>();

	private static MemCacheManagerOrginal sharedInstance = null;

	public static synchronized MemCacheManagerOrginal get()
	{
		if (sharedInstance == null)
		{
			sharedInstance = new MemCacheManagerOrginal();
		}

		return sharedInstance;
	}

	private MemCacheManagerOrginal()
	{
		
	}
	
	public byte[] getCacheImage(String url)
	{
		byte[] result = mWeakMap.get(url);
		if (result == null)
		{
			System.out.println("near: Native Cache load");
			String path = CacheManager.get().getCacheFile(url);
			File file = new File(path);
			if (file.exists())
			{
				result = AvqUtils.file.getFileBuffer(file);
			}
		}
		else
		{
			System.out.println("near: Memory Cache load");
		}
		return result;
	}
	
//	public void addCacheImage(String url, Bitmap bmp)
//	{
//		
//		mStrongList.add(bmp, AvqUtils.bitmap.sizeOf(bmp));
//		mWeakMap.add(url, bmp);
//	}
	
	public void addCacheImage(String url, byte[] data)
	{
		
		mStrongList.add(data, data.length);
		mWeakMap.add(url, data);
	}
	
}
