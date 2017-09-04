package com.downloaddemo.load;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.downloaddemo.activity.AvqUtils;
import com.downloaddemo.thread.Operation;

@SuppressWarnings("deprecation")
public class HttpOperation implements Operation
{
	public final static String HTTPERROR_SDCARDNOTMOUNTED = "SD卡挂载中";
	public final static String HTTPERROR_URLISNULL = "Url is NULL";
	public final static String HTTPERROR_NETWORK_NOT_CONNECTED = "网络未连接";
	public final static String HTTPERROR_NETWORK_CANCELLED = "下载取消";
	public final static int DOWNLOAD_AUDIO = 0x250;
	public final static int DOWNLOAD_IMAGE = 0x450;
	public final static int DOWNLOAD_GIF_CHATROOM = 0x451;
	private final static int HTTP_RECONNECTION_TIMES = 1;
	private final static int CONNECT_TIMEOUT = 60 * 1000;
	private final static int SO_TIMEOUT = 60 * 1000;
	private final static int WHAT_FINISH = 1;
	private final static int WHAT_PROGRESS = 2;
	private final static int WHAT_CANCEL = 3;

	private String loadUrl;
	private List<WeakReference<Object>> weakObjList;
	private HttpOperationCallback mCallback;

	private DefaultHttpClient mHttpClient;
	private Thread mCurrentThread;
	private boolean isCancelOperator = false;
	private byte[] result;

	public HttpOperation(String url, Object obj)
	{
		this.loadUrl = url;
		weakObjList = new ArrayList<WeakReference<Object>>();
		weakObjList.add(new WeakReference<Object>(obj));
	}

	public void addReference(Object obj)
	{
		List<WeakReference<Object>> tmp = new ArrayList<WeakReference<Object>>();
		boolean ishas = false;
		for (WeakReference<Object> reference : weakObjList)
		{
			if (AvqUtils.Weak.isValidWeak(reference))
			{
				tmp.add(reference);
				if (reference.get() == obj)
				{
					ishas = true;
				}
			}

		}
		if (ishas == false)
		{
			tmp.add(new WeakReference<Object>(obj));
		}
		weakObjList = tmp;
	}
	
	public void setCallback(HttpOperationCallback callback)
	{
		this.mCallback = callback;
	}
	
	public String getLoadUrl()
	{
		return loadUrl;
	}

	public interface HttpOperationCallback
	{
		byte[] preExecute(HttpOperation http);

		Object postExecute(HttpOperation http, boolean isSuccess, byte[] result);

		void onProgress(HttpOperation operation, long bytecount, long totalcount);

		void onFinish(HttpOperation operation, byte[] result, Object postObj);

		void onFail(HttpOperation operation, Exception e);

	}

	private Handler handler = new Handler(Looper.getMainLooper())
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (mCallback == null)
			{
				return;
			}
			switch (msg.what)
			{
			case WHAT_PROGRESS:
				break;
			case WHAT_FINISH:
				Object obj;
				for (WeakReference<Object> reference : weakObjList)
				{
					obj = null;
					if (AvqUtils.Weak.isValidWeak(reference))
					{
						obj = reference.get();
					}
					mCallback.onFinish(HttpOperation.this, result, obj);
				}

				break;
			case WHAT_CANCEL:
				break;
			default:
				break;
			}

		}
	};

	@Override
	public void run()
	{
		mCurrentThread = Thread.currentThread();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream inputStream = null;
		try
		{
			mHttpClient = new DefaultHttpClient();
			HttpParams httpParameters = mHttpClient.getParams();

			HttpConnectionParams.setConnectionTimeout(httpParameters,
					CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
			HttpGet httpGet = new HttpGet(loadUrl);
			HttpResponse httpResponse = mHttpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				inputStream = httpResponse.getEntity().getContent();
				long file_length = httpResponse.getEntity().getContentLength();
				int len = 0;
				byte[] data = new byte[1024];
				long total_length = 0;
				int value = 0;
				while ((len = inputStream.read(data)) > 0)
				{
					total_length += len;
					value = (int) ((total_length / (float) file_length) * 100);
					outputStream.write(data, 0, len);
				}
				result = outputStream.toByteArray();
				AvqUtils.handler.send_message(handler, null, WHAT_FINISH);
				System.out.println("near:  " + result.length);
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void cancelOperation()
	{
		if (!isCancelOperator && mHttpClient != null)
		{
			try
			{
				mHttpClient.getConnectionManager().shutdown();
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

}
