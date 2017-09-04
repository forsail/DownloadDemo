package com.downloaddemo.activity;

import com.downloaddemo.load.DownloadManager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RecyclingImageView extends ImageView
{
	public RecyclingImageView(Context context)
	{
		this(context, null);
	}

	public RecyclingImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RecyclingImageView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
	}
	
	public void loadImage(String url, int defaultResid)
	{
		DownloadManager.get().loadImage(url, defaultResid, this);
	}

}
