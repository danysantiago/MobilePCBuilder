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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dany.mobu.R;
import com.dany.mobu.StartUpActivity;
import com.dany.mobu.databases.HardwareDBManager;

public class HardwarePartsList extends ListActivity {

	protected Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bundle = this.getIntent().getExtras();

		// Sets windows title
		setTitle(bundle.getString("table_name").substring(0, 3).toUpperCase());

		// DBManager
		HardwareDBManager db = StartUpActivity.db;

		// Stores full query in array, for performance purposed this should be done only once.
		ArrayList<ArrayList<Object>> data = db.getAllRowsInTable(bundle.getString("table_name"));

		// Creates list based on name column of the array data created from the
		// query
		String[] menuList = new String[data.size()];
		final long[] menuListId = new long[data.size()]; // Array for list Id related to database id
		for (int i = 0; i < data.size(); i++) {
			menuListId[i] = (Long) data.get(i).get(0); // Stores _Id in the array
			menuList[i] = (String) data.get(i).get(1); // Stores the string on the column "name" to the array
		}

		setListAdapter(new ArrayAdapter<String>(this,R.layout.normal_text_listview, menuList)); // Displays a ListView with the array

		ListView hardwareListView = getListView(); // Gets list view trough method
		hardwareListView.setTextFilterEnabled(true); // Some kind of filter

		final Bundle bundleSpecs = new Bundle();

		hardwareListView.setOnItemClickListener(new OnItemClickListener() { // OnClickListener
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

				//Log.d("DEBUG", "I'm clicking! ID:" + id + " which corresponds to item _Id:" + menuListId[(int) id]); // Debug print to console

				bundleSpecs.putString("table_name",bundle.getString("table_name"));
				bundleSpecs.putLong("_Id", menuListId[(int) id]); //Creates some kind of object that can be passed trough activities

				Intent i = new Intent(HardwarePartsList.this, SpecsViewer.class); //Creates intent to start activity

				i.putExtras(bundleSpecs); // Passes the object (with the desired data) to the new activity

				startActivity(i);
			}
		});
	}

	// Act when a search is requested (this can be by search button or calling the method)
	@Override
	public boolean onSearchRequested() {

		Intent j = new Intent(HardwarePartsList.this, CatalogSearch.class);
		bundle.putBoolean("fullSearch", false);
		j.putExtras(bundle);
		startActivity(j);

		return false;
	}
}
