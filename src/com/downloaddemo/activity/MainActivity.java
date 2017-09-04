package com.downloaddemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.downloaddemo.R;


public class MainActivity extends FragmentActivity
{
	// public static final String IAMEG_URL =
	// "http://img.wallpapersking.com/d7/2015-4/2015041507225.jpg";
	public static final String IAMEG_URL1 = "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=e66a46bf18950a7b75601d846cec56eb/0ff41bd5ad6eddc4fbb8a9723cdbb6fd536633df.jpg";
	public static final String IAMEG_URL2 = "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=d103d46a522c11dfde84ec63051a56e2/b7fd5266d016092418c2b192d10735fae7cd3457.jpg";
	public static final String IAMEG_URL3 = "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=006364b16b061d957d1364781dc93ee3/d31b0ef41bd5ad6e74a6796384cb39dbb7fd3cdf.jpg";
	public static final String IAMEG_URL4 = "https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=c6b8e465d400baa1ba7914fb212d8d20/2fdda3cc7cd98d10586c25b8243fb80e7aec90fa.jpg";
	public static final String IAMEG_URL5 = "https://ss3.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=841d913bbc014a90816b15fdcf4a0d20/241f95cad1c8a786531d64876209c93d70cf5007.jpg";
	public static final String IAMEG_URL6 = "https://ss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=c40d126867d9f2d3204477afcfd1be21/80cb39dbb6fd52666fef119dae18972bd5073676.jpg";
	public static final String IAMEG_URL7 = "https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=667b8884fddcd100cdc9ab6114b67322/adaf2edda3cc7cd9d0ac16503c01213fb90e91b1.jpg";
	public static final String IAMEG_URL8 = "https://ss1.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=2c732c7e9a3df8dca668dcd1ab2c46b9/4a36acaf2edda3cc20659aa904e93901203f92b1.jpg";
	private ListView lvMain;
	private MyAdapter mAdapter;
	private List<String> mImageList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mImageList.add(IAMEG_URL1);
		mImageList.add(IAMEG_URL2);
		mImageList.add(IAMEG_URL3);
		mImageList.add(IAMEG_URL4);
		mImageList.add(IAMEG_URL5);
		mImageList.add(IAMEG_URL6);
		mImageList.add(IAMEG_URL7);
		mImageList.add(IAMEG_URL8);
		
		lvMain = (ListView) findViewById(R.id.lvMain);
		mAdapter = new MyAdapter();
		lvMain.setAdapter(mAdapter);
	}
	
	public class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return mImageList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = new RecyclingImageView(MainActivity.this);
			}
			
			RecyclingImageView imageView = (RecyclingImageView) convertView;
			imageView.loadImage(mImageList.get(position), R.drawable.ic_launcher);
			
			return convertView;
		}
		
	}

}
