package com.downloaddemo.cache;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;

import com.downloaddemo.activity.AvqUtils;
import com.downloaddemo.activity.ContextUtils;
import com.downloaddemo.thread.Command;
import com.downloaddemo.thread.IOController;

public class CacheManager
{
	private static CacheManager instance;

	private static final String ApplicationBasePath = Environment
			.getExternalStorageDirectory().getPath()
			+ "/Android/data/"
			+ ContextUtils.getSharedContext().getPackageName() + "/cache";
	private static final String ApplicationCrashLogPath = "/log";
	private static final String ApplicationImagePath = "/thumbs";

	private CacheManager()
	{

	}

	public static synchronized CacheManager get()
	{
		if (instance == null)
		{
			instance = new CacheManager();
		}

		return instance;
	}

	public String getCrashLogPath()
	{
		String path = ApplicationBasePath + ApplicationCrashLogPath
				+ File.separator;
		this.CheckOrCreateFolder(path);
		return path;
	}

	private void CheckOrCreateFolder(String fileName)
	{

		File file = new File(fileName.substring(0,
				fileName.lastIndexOf(File.separator)));
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	private String getHttpImagePath()
	{
		String path = ApplicationBasePath + ApplicationImagePath
				+ File.separator;
		this.CheckOrCreateFolder(path);
		return path;
	}

	public String getCacheFile(String url)
	{
		return getHttpImagePath() + AvqUtils.Encode.encodeByMD5(url);
	}

	public void saveImageCache(final String url, final byte[] data)
	{
		IOController.get().executeCmdOnThread(new Command()
		{

			@Override
			public void execute()
			{
				FileOutputStream out = null;
				try
				{
					File file = new File(getCacheFile(url));
					if (file.exists())
					{
						file.delete();
					}
					out = new FileOutputStream(file);
					out.write(data);
					out.close();
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
				}
				finally
				{
					try
					{
						if (out != null)
						{
							out.close();
						}
					}
					catch (Exception e)
					{
						System.out.println(e.getMessage());
					}
				}

			}

		});
	}
}
