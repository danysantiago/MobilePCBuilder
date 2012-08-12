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



package com.dany.mobu.catalog;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.dany.mobu.R;
import com.dany.mobu.StartUpActivity;
import com.dany.mobu.databases.HardwareDBManager;

public class CatalogSearch extends Activity {

	protected EditText searchTextBox; // Declares android widgets for the GUI
	protected ListView list;
	protected Bundle bundle; // Bundle declared outside methods for complete class use

	protected String[] resultList;
	protected long[] resultListId;
	protected String[] resultListTableNames;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_layout);

		// Make reference the android widgets created with those in the xml
		searchTextBox = (EditText) findViewById(R.id.searchTextBox);
		searchTextBox.setSingleLine(); // Makes the text box a single line
		list = (ListView) findViewById(R.id.list);

		// Sets TextBox focus listener
		searchTextBox.setOnEditorActionListener(editorListener);

		bundle = this.getIntent().getExtras(); // Brings the table being searched on

		// Calls the search so it displays the whole list
		if (!bundle.getBoolean("fullSearch")) {
			performSearch();
		} else {
			list.setAdapter(new ArrayAdapter<String>(this,R.layout.normal_text_listview, new String[0]));
		}

		// SearchTextBox text listener to start searching when the text is
		// changed (just like google search!)
		searchTextBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				performSearch();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
			}
		});

		// Click listener
		list.setOnItemClickListener(new OnItemClickListener() { // OnClickListener
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

				//Log.d("DEBUG", "I'm clicking! ID:" + id + " which corresponds to item _Id:" + resultListId[(int) id]); // Debug print to console
				Bundle bundleSpecs = new Bundle();
				if (bundle.getBoolean("fullSearch")) {
					bundleSpecs.putString("table_name",resultListTableNames[(int) id]);
				} else {
					bundleSpecs.putString("table_name",bundle.getString("table_name"));
				}
				// Creates some kind of object that can be passed trough activities
				bundleSpecs.putLong("_Id", resultListId[(int) id]); 
				// Creates intent to start activity
				Intent i = new Intent(CatalogSearch.this, SpecsViewer.class); 
				// Passes the object (with the desired data) to the new activity
				i.putExtras(bundleSpecs);

				startActivity(i);
			}
		});
	}

	// Method that handles editor changes (For when pressing enter (Return Key
	// or Next Key) performSearch is called)
	private OnEditorActionListener editorListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			//Log.d("DEBUG", "ActionId: " + actionId);
			if (actionId == EditorInfo.IME_ACTION_DONE
					|| actionId == EditorInfo.IME_ACTION_NEXT) {
				performSearch();
				// This hides the soft keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchTextBox.getWindowToken(), 0);

			}
			return true;
		}
	};

	// Search method
	public void performSearch() {

		HardwareDBManager db = StartUpActivity.db; // DBManager

		ArrayList<ArrayList<Object>> data = null;

		// Calls the search function of the DBManager which is a SQL Query and
		// stores the data in the ArrayList
		if (bundle.getBoolean("fullSearch")) {
			data = db.searchDatabase(searchTextBox.getText().toString());
		} else {
			data = db.searchTable(bundle.getString("table_name"), searchTextBox.getText().toString());
		}

		resultList = new String[data.size()];
		resultListId = new long[data.size()]; // Array for list Id related to
		// database id
		if (bundle.getBoolean("fullSearch"))
			resultListTableNames = new String[data.size()];
		for (int i = 0; i < data.size(); i++) {
			resultListId[i] = (Long) data.get(i).get(0); // Stores _Id in the result array
			resultList[i] = (String) data.get(i).get(1); // Stores the string on the column "name" to the result array
			if (bundle.getBoolean("fullSearch"))
				resultListTableNames[i] = (String) data.get(i).get(2);
		}

		list.setAdapter(new ArrayAdapter<String>(this,
				// Display in the list view the resultList array
				R.layout.normal_text_listview, resultList)); 
	}

}
