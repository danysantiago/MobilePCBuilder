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

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dany.mobu.R;

public class News extends Activity {

	protected ListView newsListView;
	protected TextView newsErrorTextView;

	protected NewsListAdapter newsListAdapter;

	protected ProgressDialog progressDialog;

	protected ArrayList<NewsDataSet> parsedDataSet;

	protected boolean connectionError = true;

	protected String errorMessage;

	// Constant containing the sources Titles and URLs
	public final CharSequence[] sourceNamesList = new CharSequence[] {
			"Tom's Hardware", "XBitLabs", "AnandTech" };

	public final String[] sourceURLList = new String[] {
			"http://www.tomshardware.com/feeds/rss2/tom-s-hardware-us-components,18-1-1.xml",
			"http://www.xbitlabs.com/misc/rss/news",
			"http://www.anandtech.com/rss/" };

	protected String sourceURL;
	protected int sourceId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_layout); // Draws GUI based on the defined xml

		setTitle("News");

		// Create a new ListView to display the parsing result later.
		newsListView = (ListView) findViewById(R.id.newsListView);
		newsErrorTextView = (TextView) findViewById(R.id.newsErrorTextView);

		// Gets the SharedPreferences object used to maintain persistent state
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		sourceURL = preferences.getString("sourceURL", sourceURLList[0]);
		sourceId = preferences.getInt("sourceId", 0);

		// This calls and execute the news getter in another thread
		new GetNews_AsyncTask().execute();

		// Click listener (This one passes the parameters to my other method
		newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> av, View v, int pos,long id) {
						onListItemClick(v, pos, id);
					}
				});
	}

	// Method to handle click on the ListView
	protected void onListItemClick(View v, int pos, long id) {
		//Create a browser intent with the desires URL (Url taken from the ListView Data)
		Intent browserIntent = new Intent("android.intent.action.VIEW",Uri.parse(parsedDataSet.get((int) id).getLink()));
		startActivity(browserIntent);
	}

	// onPause override to save preferences for persistent state activity (Saves the desired source)
	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("sourceURL", sourceURL);
		editor.putInt("sourceId", sourceId);
		editor.commit();

	}
	
	// onDestroy to kill any loading image thread that was not finished
	@Override
	public void onDestroy() {
		super.onDestroy();

		if (!connectionError) {
			newsListAdapter.imageLoader.stopThread();
			newsListView.setAdapter(null);
		}
	}

	// This is the subclass that gets the news, it is an AsyncTask Thread
	private class GetNews_AsyncTask extends AsyncTask<URL, Integer, Long> {

		// Method called on pre execution of task
		@Override
		protected void onPreExecute() {
			// Progress dialog is shown
			progressDialog = ProgressDialog.show(News.this, "","Loading. Please wait...", true);
			// This is my dialog cancel listener (It cancels the loading and closes the activity)
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							finish();
						}
					});
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		// This is the work done in the new thread different from the UI thread
		// The method is called when the task starts
		@Override
		protected Long doInBackground(URL... params) {
			try {
				// Create a URL we want to load some xml-data from.
				URL url = new URL(sourceURL);

				// Get a SAXParser from the SAXPArserFactory.
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				// Get the XMLReader of the SAXParser we created.
				XMLReader xr = sp.getXMLReader();
				// Create a new ContentHandler and apply it to the XML-Reader
				NewsHandler NewsHandler = new NewsHandler();
				xr.setContentHandler(NewsHandler);

				// Parse the xml-data from our URL.
				xr.parse(new InputSource(url.openStream()));
				// Parsing has finished.

				// Our ExampleHandler now provides the parsed data to us.
				parsedDataSet = NewsHandler.getParsedData();

				connectionError = false;

			} catch (Exception e) {
				// Handles errors and saves the message to be displayed in GUI
				connectionError = true;
				// "Connection Error" string
				errorMessage = "Unable to connect.\nPlease make sure you have an active Internet Connection."; 
				Log.d("DEBUG", "Error: " + e);
			}
			return null;
		}

		// Method called on post execution of task
		@Override
		protected void onPostExecute(Long result) {
			// Dismisses the progress dialog
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			// Updates list
			updateGUI();
		}
	}

	// Creates Option Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); // Some kind of menu inflater
		inflater.inflate(R.menu.news_menu, menu); // Inflates the menu with my desired xml menu
		return true;
	}

	// My method to update the news list
	public void updateGUI() {
		// Depending of connection error or not hide or show Views
		if (connectionError) {
			newsErrorTextView.setVisibility(View.VISIBLE);
			newsListView.setVisibility(View.GONE);
			newsErrorTextView.setText(errorMessage);
		} else {
			newsErrorTextView.setVisibility(View.GONE);
			newsListView.setVisibility(View.VISIBLE);
			// The list view uses my custom list adapter
			newsListAdapter = new NewsListAdapter(this,R.layout.news_custom_listview, parsedDataSet);
			newsListView.setAdapter(newsListAdapter);
		}
	}

	// Handles menu item selections
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.refresh_item) { // Refresh Item
			//Log.d("DEBUG", "Refresh Item Selected");
			new GetNews_AsyncTask().execute();
		} else if (item.getItemId() == R.id.source_item) {
			//Log.d("DEBUG", "Refresh Source Selected");
			// Single Choice Dialog Builder for selecting News Source
			Builder sourceDialog = new AlertDialog.Builder(this);
			sourceDialog.setTitle("News Source:");
			sourceDialog.setSingleChoiceItems(sourceNamesList, sourceId,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// Register selection when user selects new source
							Log.d("DEBUG", "Source Id changed to: " + sourceId); sourceId = id;
							sourceURL = sourceURLList[sourceId];
						}
					});
			sourceDialog.setPositiveButton("Done", null); // This creates the "Done" button without any listener
			sourceDialog.setCancelable(true);
			sourceDialog.show();
		} else if (item.getItemId() == R.id.clear_cache_item) {
			if (!connectionError) {
				newsListAdapter.imageLoader.clearCache();
				newsListAdapter.notifyDataSetChanged();
			}
		}
		return true;
	}
}
