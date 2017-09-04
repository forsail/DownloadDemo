package com.downloaddemo.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AvqUtils
{
	public static class Weak
	{
		public static boolean isValidWeak(WeakReference<?> weak)
		{
			return weak != null && weak.get() != null;
		}

	}

	public static class context
	{

		public static boolean isWifi(Context c)
		{
			if (c == null)
			{
				return false;
			}

			ConnectivityManager cm = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if (null != activeNetwork)
			{
				int nType = activeNetwork.getType();

				if (nType == ConnectivityManager.TYPE_WIFI
						|| nType == ConnectivityManager.TYPE_WIMAX)
				{
					return true;
				}
			}

			return false;
		}

		public static boolean is3G(Context c)
		{
			if (c == null)
			{
				return false;
			}

			ConnectivityManager cm = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if (null != activeNetwork)
			{
				int nType = activeNetwork.getType();

				if (nType == ConnectivityManager.TYPE_MOBILE)
				{
					return true;
				}
			}

			return false;
		}

		public static boolean isConnected(Context c)
		{
			if (c == null)
			{
				return false;
			}

			ConnectivityManager cm = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if (null != activeNetwork)
			{
				return true;
			}

			return false;
		}

		public static void showLongToast(Context c, String text)
		{
			Toast.makeText(c, text, Toast.LENGTH_LONG).show();
		}

		public static void showShortToast(Context c, String text)
		{
			Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
		}

		public static void setEditTextSelectionToRightEnd(EditText etext)
		{
			if (etext == null)
			{
				return;
			}

			int position = etext.length();
			Selection.setSelection(etext.getText(), position);
		}

		public static float getScreenDensity(Context c)
		{
			return c.getResources().getDisplayMetrics().density;
		}

		public static int getScreenWidth(Context c)
		{
			return c.getResources().getDisplayMetrics().widthPixels;
		}

		public static int getScreenHeight(Context c)
		{
			return c.getResources().getDisplayMetrics().heightPixels;
		}

		public static void vibrator(Context c, long duration)
		{
			if (duration <= 0 || c == null)
			{
				return;
			}

			Vibrator vibrator = (Vibrator) c
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(duration);
		}

		public static void PrintMemeryInfo(Context c)
		{
			if (c != null)
			{
				System.out.println("memory:" + getFreeMemoryInfo(c) + "/"
						+ Debug.getNativeHeapAllocatedSize() / 1024);
			}
		}

		public static int getFreeMemoryInfo(Context c)
		{

			// Debug.getNativeHeapAllocatedSize()/1024

			if (c == null)
			{
				return 0;
			}

			ActivityManager am = (ActivityManager) c
					.getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
			am.getMemoryInfo(outInfo);
			long a = outInfo.availMem / 1024;

			return (int) a;
		}

		public static String getVersionName(Context c)
		{
			if (c == null)
			{
				return null;
			}

			PackageManager manager = c.getPackageManager();

			try
			{
				PackageInfo info = manager
						.getPackageInfo(c.getPackageName(), 0);
				return info.versionName;
			}
			catch (android.content.pm.PackageManager.NameNotFoundException e)
			{
				e.printStackTrace();
			}

			return null;

		}

		public static String getPackageName(Context c)
		{
			if (c == null)
			{
				return null;
			}

			PackageManager manager = c.getPackageManager();

			try
			{
				PackageInfo info = manager
						.getPackageInfo(c.getPackageName(), 0);
				return info.packageName;
			}
			catch (android.content.pm.PackageManager.NameNotFoundException e)
			{
				e.printStackTrace();
			}

			return null;

		}

		public static float autoResizeFont(float originTextSize, String text,
				int constraitWidth, int constraintHeight)
		{

			String tempText = "0";
			if (text != null && text.length() != 0)
			{
				tempText = text;
			}

			float tempSize = new Float(originTextSize);
			if (tempSize <= 5)
			{
				return tempSize;
			}

			for (float i = tempSize; i < 100; i++)
			{
				int width = getTextRect(i, tempText).width();
				int height = getTextRect(i, tempText).height();
				if (width > constraitWidth || height > constraintHeight)
				{
					break;
				}
				tempSize = new Float(i);
			}

			for (float i = tempSize; i >= 5; i--)
			{
				int width = getTextRect(i, tempText).width();
				int height = getTextRect(i, tempText).height();
				if (width < constraitWidth && height < constraintHeight)
				{
					return i;
				}
			}

			return tempSize;
		}

		public static Rect getTextRect(float fontsize, String textString)
		{
			String text = null;
			if (textString == null)
			{
				text = "";
			}
			else
			{
				text = new String(textString);
			}

			Paint textPaint = new Paint();
			textPaint.setTextSize(fontsize);
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			return bounds;
		}

		public static void hideSoftKeyBoard(View view)
		{
			if (view == null)
			{
				return;
			}
			InputMethodManager imm = (InputMethodManager) view.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive())
			{
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}

		public static void showSoftKeyBoard(View view)
		{
			if (view == null)
			{
				return;
			}
			InputMethodManager imm = (InputMethodManager) view.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, 0);
		}

		// public static void showLongToast(Context c,String text){
		// Toast.makeText(c, text, Toast.LENGTH_LONG).show();
		// }
		//
		// public static void showShortToast(Context c,String text){
		// Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
		// }

		public static Bitmap getBitmap(Context c, int resid)
		{
			BitmapDrawable bd = (BitmapDrawable) c.getResources().getDrawable(
					resid);
			if (bd != null)
			{
				return bd.getBitmap();
			}
			return null;
		}

		public static boolean isNetworkConnectionAvailable(Context c)
		{
			ConnectivityManager cm = (ConnectivityManager) c
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null)
			{
				return false;
			}

			State network = info.getState();

			return (network == NetworkInfo.State.CONNECTED);
		}

		public static boolean isAboveVersionICE_CREAM_SANDWICH()
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			{
				return true;
			}

			return false;
		}

		public static boolean isAboveVersionHONEYCOMB()
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				return true;
			}

			return false;
		}

		public static boolean isAboveVersionJellyBean()
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
			{
				return true;
			}

			return false;
		}

		public static void writeToSPref(Context c, String filename, String key,
				String value)
		{
			if (c == null)
			{
				return;
			}

			SharedPreferences.Editor edit = c.getSharedPreferences(filename,
					Context.MODE_PRIVATE).edit();
			edit.putString(key, value);
			edit.commit();
		}

		public static String readFromSPref(Context c, String filename,
				String key)
		{
			if (c == null)
			{
				return null;
			}
			SharedPreferences pre = c.getSharedPreferences(filename,
					Context.MODE_PRIVATE);
			String value = pre.getString(key, null);
			return value;
		}

		public static void writeToSPrefBoolean(Context c, String filename,
				String key, boolean b)
		{
			if (c == null)
			{
				return;
			}

			SharedPreferences.Editor edit = c.getSharedPreferences(filename,
					Context.MODE_PRIVATE).edit();
			edit.putBoolean(key, b);
			edit.commit();
		}

		public static boolean readFromSPrefBoolean(Context c, String filename,
				String key, boolean bDefault)
		{
			if (c == null)
			{
				return bDefault;
			}
			SharedPreferences pre = c.getSharedPreferences(filename,
					Context.MODE_PRIVATE);
			boolean value = pre.getBoolean(key, bDefault);
			return value;
		}
	}

	public static class Encode
	{
		private static final String ALGORITHM = "MD5";

		private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4',
				'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		/**
		 * encode string
		 * 
		 * @param algorithm
		 * @param str
		 * @return String
		 */
		public static String encode(String algorithm, String str)
		{
			if (str == null)
			{
				return null;
			}
			try
			{
				MessageDigest messageDigest = MessageDigest
						.getInstance(algorithm);
				messageDigest.update(str.getBytes());
				return getFormattedText(messageDigest.digest());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}

		}

		/**
		 * encode By MD5
		 * 
		 * @param str
		 * @return String
		 */
		public static String encodeByMD5(String str)
		{
			if (str == null)
			{
				return null;
			}
			try
			{
				MessageDigest messageDigest = MessageDigest
						.getInstance(ALGORITHM);
				messageDigest.update(str.getBytes());
				return getFormattedText(messageDigest.digest());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}

		}

		/**
		 * encode By MD5
		 * 
		 * @param str
		 * @return String
		 */
		public static String endcodeByMD5(InputStream is)
		{
			if (is == null)
			{
				return null;
			}

			try
			{
				MessageDigest messageDigest = MessageDigest
						.getInstance(ALGORITHM);
				byte[] buffer = new byte[1024 * 32];
				int k = 0;

				while ((k = is.read(buffer)) > 0)
				{
					messageDigest.update(buffer, 0, k);
				}

				return getFormattedText(messageDigest.digest());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		public static String encodeByMD5(byte[] bytes)
		{
			if (bytes == null)
			{
				return null;
			}
			try
			{
				MessageDigest messageDigest = MessageDigest
						.getInstance(ALGORITHM);
				messageDigest.update(bytes);
				return getFormattedText(messageDigest.digest());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}

		}

		/**
		 * Takes the raw bytes from the digest and formats them correct.
		 * 
		 * @param bytes
		 *            the raw bytes from the digest.
		 * @return the formatted bytes.
		 */
		private static String getFormattedText(byte[] bytes)
		{
			int len = bytes.length;
			StringBuilder buf = new StringBuilder(len * 2);
			for (int j = 0; j < len; j++)
			{
				buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
				buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
			}
			return buf.toString();
		}

		// public static void main(String[] args) {
		// System.out.println("111111 MD5  :"
		// + EncoderHandler.encodeByMD5("111111"));
		// System.out.println("111111 MD5  :"
		// + EncoderHandler.encode("MD5", "111111"));
		// System.out.println("111111 SHA1 :"
		// + EncoderHandler.encode("SHA1", "111111"));
		// }
	}

	public static class bitmap
	{
		public static int sizeOf(Bitmap data)
		{
			int size = 0;
			
			if (data != null)
			{
				size = data.getRowBytes() * data.getHeight();
			}
			
			return size;

		}

		/**
		 * 
		 * @param byteArray
		 * @return
		 */
		public static Bitmap decodeByteArray(byte[] byteArray)
		{
			Bitmap bitmap = null;
			long startTime = System.currentTimeMillis();
			InputStream input = null;
			try
			{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inPurgeable = true;
				options.inInputShareable = true;
				input = new ByteArrayInputStream(byteArray);
				bitmap = BitmapFactory.decodeStream(input, null, options);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (input != null)
				{
					try
					{
						input.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			System.out.println("near: decode time " + (System.currentTimeMillis() - startTime));
			return bitmap;
		}

	}

	public static class handler
	{
		public static void send_message(Handler handler, Object content,
				int msg_what)
		{
			if (handler == null)
			{
				return;
			}

			Message msg = handler.obtainMessage();
			msg.what = msg_what;
			msg.obj = content;
			handler.sendMessage(msg);
		}
	}
	
	public static class file
	{
		@SuppressWarnings("resource")
		public static byte[] getFileBuffer(File file)
		{
			byte[] result = null;
			try
			{
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				InputStream in = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int length = 0;
				while ((length = in.read(buffer)) > 0)
				{
					outputStream.write(buffer, 0, length);
				}
				result = outputStream.toByteArray();
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return result;
		}
	}
}
