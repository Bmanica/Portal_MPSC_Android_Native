package com.snapcrap.bmanica.portal;

/**
 * PostItemAdapter.java
 * 
 * Adapter Class which configs and returns the View for ListView
 * 
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import com.snapcrap.bmanica.portal.PostData;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostItemAdapter extends ArrayAdapter<PostData> {
	private Activity myContext;
	private ArrayList<PostData> datas;

	public PostItemAdapter(Context context, int textViewResourceId,ArrayList<PostData> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		myContext = (Activity) context;
		datas = objects;
	}

	static class ViewHolder {
		TextView postTitleView;
		TextView postDateView;
		ImageView postThumbView;
		ImageView imageView;
		String imageURL;
		Bitmap bitmap;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		PostData post = datas.get(position);
		if (convertView == null) {
			LayoutInflater inflater = myContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.postitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.postThumb);
			viewHolder.postTitleView = (TextView) convertView.findViewById(R.id.postTitleLabel);
			viewHolder.postDateView = (TextView) convertView.findViewById(R.id.postDateLabel);
			convertView.setTag(viewHolder);
			convertView.setBackgroundResource(R.drawable.rounded_corners);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		} 
		viewHolder.imageView.setTag(post.postThumbUrl);
		viewHolder.postTitleView.setText(post.postTitle);
		viewHolder.postDateView.setText(post.postDate);

		Bitmap imageBitmap = null;
		try {
			URL imageURL = new URL(post.postThumbUrl);
			imageBitmap = BitmapFactory.decodeStream(imageURL.openStream());
			viewHolder.imageView.setImageBitmap(imageBitmap);
		} catch (IOException e) {
			// TODO: handle exception
			Log.e("error", "Downloading Image Failed");
			switch (post.n) {
				default:
					viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
			}			
		}
		if(datas.get(position) != null ) {
			viewHolder.postTitleView.setTextColor(Color.DKGRAY);
			// viewHolder.postTitleView.setText(datas.get(position));

			//int color = Color.argb( 200, 255, 64, 64 );
			/* viewHolder.postTitleView.setBackgroundColor(color);
            viewHolder.postDateView.setBackgroundColor(color);*/

		}
		return convertView;
	}
}
