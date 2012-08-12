/**
MoBu - Mobile PC Builder, and android application about computer hardware.
    Copyright (C) 2011  Daniel Santiago

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/



package com.dany.mobu.news;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dany.mobu.R;

//This is the news custom list view that can display text and images in a list
public class NewsListAdapter extends ArrayAdapter<NewsDataSet> {

	private ArrayList<NewsDataSet> newsDataSetArrayList; // ArrayList to be filled into the list
	private static LayoutInflater mInflater = null; // Some kind of layout inflater
	private Activity activity;
	public NewsImageLoader imageLoader;

	// Constructor for the adapter receives the activity, the R.layout resource id and the ArrayList of NewsDataSet
	public NewsListAdapter(Activity a, int textViewResourceId,ArrayList<NewsDataSet> data) {
		super(a, textViewResourceId, data);

		activity = a;
		newsDataSetArrayList = data;
		mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new NewsImageLoader(activity.getApplicationContext());
	}

	// View holder stores references to the views
	public static class ViewHolder {
		public TextView title;
		public TextView pubDate;
		public ImageView image;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {// If View null create references
			vi = mInflater.inflate(R.layout.news_custom_listview, null);
			holder = new ViewHolder();
			holder.title = (TextView) vi.findViewById(R.id.newsTitleTextView);
			holder.pubDate = (TextView) vi.findViewById(R.id.newsPubDateTextView);
			holder.image = (ImageView) vi.findViewById(R.id.newsImageView);
			vi.setTag(holder);
		} else {// else get some kind of tag
			holder = (ViewHolder) vi.getTag();
		}

		// Set the strings to the TextViews taken from the arraylist
		holder.title.setText(newsDataSetArrayList.get(position).getTitle());
		holder.pubDate.setText(newsDataSetArrayList.get(position).getPubDate());
		// Sets the images if there is images to set, otherwise hide the imageView
		if (newsDataSetArrayList.get(position).getImgUrl() != null) {
			// This calls our image loader thread
			holder.image.setTag(newsDataSetArrayList.get(position).getImgUrl());
			imageLoader.DisplayImage(newsDataSetArrayList.get(position).getImgUrl(), activity, holder.image);
		} else {
			holder.image.setVisibility(View.GONE);
		}

		return vi;
	}
}
