package com.downloaddemo.load;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import com.downloaddemo.activity.AvqUtils;
import com.downloaddemo.load.HttpOperation.HttpOperationCallback;
import com.downloaddemo.thread.OperationQueque;

public class HttpDownloader implements HttpOperationCallback
{
	private HttpDownloaderCallback mCallback;
	private OperationQueque executor = new OperationQueque();
	private Map<String, WeakReference<HttpOperation>> mDownloadMap;

	public interface HttpDownloaderCallback
	{
		public void onProgress(String url, Object obj, long bytecount,
				long totalcount);

		public void onFinish(String url, Object obj, byte[] result, boolean isfromCache);

		public void onFail(String url, Object obj, Exception e);
	}

	public HttpDownloader()
	{
		mDownloadMap = new HashMap<String, WeakReference<HttpOperation>>();
	}
	
	public void setCallback(HttpDownloaderCallback callback)
	{
		this.mCallback = callback;
	}

	public void add(String url, Object obj)
	{
		WeakReference<HttpOperation> reference = mDownloadMap.get(url);
		HttpOperation operator = null;
		if (AvqUtils.Weak.isValidWeak(reference))
		{
			operator = reference.get();
		}
		if (operator != null)
		{
			operator.addReference(obj);
		}
		else
		{
			HttpOperation operation = new HttpOperation(url, obj);
			operation.setCallback(this);
			executor.addOperation(operation);
			mDownloadMap.put(url, new WeakReference<HttpOperation>(operation));
		}
	}

	@Override
	public byte[] preExecute(HttpOperation http)
	{
		return null;
	}

	@Override
	public Object postExecute(HttpOperation http, boolean isSuccess,
			byte[] result)
	{
		return null;
	}

	@Override
	public void onProgress(HttpOperation operation, long bytecount,
			long totalcount)
	{
		
	}

	@Override
	public void onFinish(HttpOperation operation, byte[] result, Object postObj)
	{
		mCallback.onFinish(operation.getLoadUrl(), postObj, result, false);
	}

	@Override
	public void onFail(HttpOperation operation, Exception e)
	{
		
	}
}
