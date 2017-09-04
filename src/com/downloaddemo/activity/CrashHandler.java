package com.downloaddemo.activity;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.downloaddemo.cache.CacheManager;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Process;
import android.util.Log;

public class CrashHandler implements UncaughtExceptionHandler
{

	@Override
	public void uncaughtException(Thread thread, Throwable tr)
	{
		try
		{
			// CacheManager.get().writeLog("ShanliaoUncaughtExceptionHandler on uncaughtException");
			//
			// if (TcLog.getIns().needPrint())
			// {
			writeToFile(thread, tr);
			// }

			System.err.println(Log.getStackTraceString(tr));
			// UploaderManager.get().storeJavaBehindErrorLog("Crash" +
			// Log.getStackTraceString(tr));

			Thread.sleep(3500);
		}
		catch (InterruptedException e)
		{

		}

		Process.killProcess(Process.myPid());
	}

	@SuppressLint("DefaultLocale")
	private File writeToFile(Thread thread, Throwable tr)
	{
		String logMessage;
		File log = new File(CacheManager.get().getCrashLogPath()
				+ getFileName());

		PrintWriter printWriter = null;
		try
		{
			printWriter = new PrintWriter(new FileWriter(log, true));

			PackageInfo pinfo = DownloadApplication
					.getSharedContext()
					.getPackageManager()
					.getPackageInfo(
							DownloadApplication.getSharedContext()
									.getPackageName(), 0);

			logMessage = String
					.format("%s\r\n\r\nThread: %d\r\n\r\nMessage: %s\r\n\r\nManufacturer: %s\r\nModel: %s\r\nProduct: %s\r\n\r\nAndroid Version: %s\r\nAPI Level: %d\r\nHeap Size: %sMB\r\n\r\nVersion Code: %s\r\nVersion Name: %s\r\n\r\nStack Trace:\r\n\r\n%s",
							new Date(), thread.getId(), tr.getMessage(),
							Build.MANUFACTURER, Build.MODEL, Build.PRODUCT,
							Build.VERSION.RELEASE, Build.VERSION.SDK_INT,
							Runtime.getRuntime().maxMemory() / 1024 / 1024,
							pinfo.versionCode, pinfo.versionName,
							Log.getStackTraceString(tr));

			printWriter.print(logMessage);
			printWriter
					.print("\n\n---------------------------------------------------------------------------\n\n");
			return log;
		}
		catch (Throwable tr2)
		{
			tr2.printStackTrace();
			return null;
		}
		finally
		{
			if (printWriter != null)
			{
				printWriter.close();
			}
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String getFileName()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return df.format(new Date()) + ".txt";
	}
}
