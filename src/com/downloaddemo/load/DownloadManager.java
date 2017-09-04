package com.downloaddemo.load;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.downloaddemo.activity.AvqUtils;
import com.downloaddemo.activity.StringUtils;
import com.downloaddemo.cache.CacheManager;
import com.downloaddemo.cache.MemCacheManager;
import com.downloaddemo.load.HttpDownloader.HttpDownloaderCallback;

public class DownloadManager implements HttpDownloaderCallback
{
	private static DownloadManager instance = null;
	private HttpDownloader mHttploader = null;
	
	public static synchronized DownloadManager get()
	{
		if (instance == null)
		{
			instance = new DownloadManager();
		}
		
		return instance;
	}
	
	private DownloadManager()
	{
		mHttploader = new HttpDownloader();
		mHttploader.setCallback(this);
	}
	
	public void loadImage(String url, int defaultResid, ImageView view)
	{
		if (StringUtils.isNullOrEmpty(url))
		{
			onFail(url, view, new Exception("Load url must not be null"));
			return;
		}
		
		if (defaultResid != 0)
		{
			view.setImageResource(defaultResid);
		}
		
		Bitmap bmp = MemCacheManager.get().getCacheImage(url);
		
		if (bmp == null)
		{
			mHttploader.add(url, view);
		}
		else
		{
			view.setImageBitmap(bmp);
		}
	}

	@Override
	public void onProgress(String url, Object obj, long bytecount, long totalcount)
	{
		
	}

	@Override
	public void onFinish(String url, Object obj, byte[] result, boolean isfromCache)
	{
		Bitmap bmp = AvqUtils.bitmap.decodeByteArray(result);
		if (!isfromCache)
		{
			MemCacheManager.get().addCacheImage(url, bmp);
			CacheManager.get().saveImageCache(url, result);
		}
		System.out.println("near: result size " + result.length);
		System.out.println("near: bitmap size " + AvqUtils.bitmap.sizeOf(bmp));
		if (obj != null && obj instanceof ImageView)
		{
			ImageView view = (ImageView) obj;
			view.setImageBitmap(bmp);
		}
	}

	@Override
	public void onFail(String url, Object obj, Exception e)
	{
		
	}

}
