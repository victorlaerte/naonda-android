package com.victorlaerte.na_onda.model.impl;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.victorlaerte.na_onda.R;

public class CustomPagerAdapter extends PagerAdapter {

	private static final String LOG_TAG = CustomPagerAdapter.class.getName();
	private Context context;
	private List<String> imagesUrl;
	private LayoutInflater mLayoutInflater;
	private ImageLoader imageLoader;

	public CustomPagerAdapter(Context context, List<String> imagesUrl) {

		this.context = context;
		this.imagesUrl = imagesUrl;
		mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {

		return imagesUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		DisplayImageOptions options = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).build();

		Log.d(LOG_TAG, imagesUrl.get(position));

		final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.graphProgressBar);
		final TextView errorMsgLbl = (TextView) itemView.findViewById(R.id.erroLoadImageLabel);

		ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
		imageLoader.displayImage(imagesUrl.get(position), imageView, options, new SimpleImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {

				progressBar.setVisibility(View.VISIBLE);
				errorMsgLbl.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

				progressBar.setVisibility(View.GONE);
				errorMsgLbl.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				progressBar.setVisibility(View.GONE);
			}
		});

		container.addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		container.removeView((LinearLayout) object);
	}
}
