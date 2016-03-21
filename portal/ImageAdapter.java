package com.snapcrap.bmanica.portal;

import java.io.IOException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ImageAdapter extends ArrayAdapter<String> {
	private String[] imageURLArray;
	private LayoutInflater inflater;

	public ImageAdapter(Context context, int textViewResourceId,
			String[] imageArray) {
		super(context, textViewResourceId, imageArray);
		// TODO Auto-generated constructor stub

		inflater = ((Activity)context).getLayoutInflater();
		imageURLArray = imageArray;
	}

	private static class ViewHolder {
		ImageView imageView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.postitem, null);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.postThumb);
			convertView.setTag(viewHolder);
		}

		viewHolder = (ViewHolder)convertView.getTag();
		
		//load image directly
		Bitmap imageBitmap = null;
		try {
			URL imageURL = new URL(imageURLArray[position]);
            imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
			viewHolder.imageView.setImageBitmap(imageBitmap);
		} catch (IOException e) {
			// TODO: handle exception
			Log.e("error", "Downloading Image Failed");
			viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
		}
		
		return convertView;
	}
	
}
