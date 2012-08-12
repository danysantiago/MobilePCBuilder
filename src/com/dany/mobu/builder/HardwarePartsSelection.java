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



package com.dany.mobu.builder;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dany.mobu.R;
import com.dany.mobu.StartUpActivity;
import com.dany.mobu.catalog.SpecsViewer;
import com.dany.mobu.databases.HardwareDBManager;

public class HardwarePartsSelection extends Activity {

	protected Bundle bundle;
	protected Bundle bundleSpecs;

	protected ListView hardwarelist;

	protected String[] menuList;
	protected long[] menuListId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.selection_layout);

		// Gets the LisView used in layout and sets some properties
		hardwarelist = (ListView) findViewById(R.id.listselection);
		hardwarelist.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Makes it single choice list
		hardwarelist.setLongClickable(true);
		registerForContextMenu(hardwarelist); // This ones registers it for use with a context menu

		bundle = this.getIntent().getExtras();

		// Sets the windows title
		setTitle(bundle.getString("table_name").substring(0, 3).toUpperCase());

		// DBManager
		HardwareDBManager db = StartUpActivity.db;

		// SQL WHERE string created from the Compability Algorithm
		String conditionString = CompabilityAlgorithms.getConditionString(bundle.getString("table_name"));
		//Log.d("DEBUG", "My Condition String is: " + conditionString);
		// Get the components based on the WHERE string
		ArrayList<ArrayList<Object>> data = db.getCompatibleRows(bundle.getString("table_name"), conditionString);

		// Creates list based on name column of the array data created from the query
		menuList = new String[data.size()];
		menuListId = new long[data.size()]; // Array for list Id related to database id
		for (int i = 0; i < data.size(); i++) {
			menuListId[i] = (Long) data.get(i).get(0); // Stores _Id in the array
			menuList[i] = (String) data.get(i).get(1); // Stores the string on the column "name" to the array
		}

		hardwarelist.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, menuList));
		hardwarelist.setItemChecked(0, true); // Selects the first item, making sure something is always selected.
	}

	// Override method so when I hit back button... (needs working)
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// Method for when "Select" button is clicked
	public void Selection(View v) {
		// setResult puts the data to be send in an intent (it's like a bundle)
		setResult(
				RESULT_OK,
				new Intent().putExtra("resultId",menuListId[hardwarelist.getCheckedItemPosition()]));
		//Log.d("DEBUG","Sending Id: "+ menuListId[(int) hardwarelist.getSelectedItemId()]);
		finish();
	}

	// Method for creating ContextMenu (The menu that appears on long clicks)
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listselection) { // Conditions to make sure context menu comes from the list view
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(menuList[info.position]); // Title of context menu is the name of the item long clicked
			menu.add(Menu.NONE, 0, 0, "View Specifications"); // Adds to the context menu the item
		}
	}

	// Method for context menu selection
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// This brings the Id of the ListArray Long Clicked On
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo(); 
		int menuItemIndex = item.getItemId(); // Gets which of the context menu item was selected
		//Log.d("DEBUG", "I'm long clicking! ID:" + info.position + " which corresponds to item _Id:" + menuListId[info.position]);

		// Responds to the context menu item at position 0 (I got only 1 item but this is for general purposes)
		// And by the way the method is the same of Specs View calling in HardwarePartList
		if (menuItemIndex == 0) {
			bundleSpecs = new Bundle();
			bundleSpecs.putString("table_name", bundle.getString("table_name"));
			bundleSpecs.putLong("_Id", menuListId[info.position]);
			Intent i = new Intent(HardwarePartsSelection.this,SpecsViewer.class);
			i.putExtras(bundleSpecs);
			startActivity(i);
		}

		return true;
	}

}
