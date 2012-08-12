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
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dany.mobu.R;

public class ConfigsManager extends Activity {

	// Configurations DB
	protected ConfigurationsDBManager db = new ConfigurationsDBManager(this);

	protected ListView hardwarelist;

	protected Bundle bundle;

	protected String[] menuList;
	protected long[] menuListId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.selection_layout);

		bundle = new Bundle();

		hardwarelist = (ListView) findViewById(R.id.listselection);
		hardwarelist.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Makes it single choice list
		hardwarelist.setLongClickable(true);
		registerForContextMenu(hardwarelist); // This ones registers it for usewith a context menu

		getList();
	}

	// Method to handle received result from other activities
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				db.open();
				db.renameConfig(data.getExtras().getLong("renameId"), data.getExtras().getString("renameString"));
				db.close();
				//Log.d("DEBUG","Renamed Id: " + data.getExtras().getLong("renameId")+ " to :" + data.getExtras().getString("renameString"));
				getList();
			} else {
				Toast.makeText(this, "Rename canceled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Method for when "Select" button is clicked
	public void Selection(View v) {
		if (hardwarelist.getCheckedItemPosition() != AdapterView.INVALID_POSITION) {
			ArrayList<Integer> ids = new ArrayList<Integer>();

			//Gets config from DB
			db.open();
			Cursor c = db.getConfig(menuListId[hardwarelist.getCheckedItemPosition()]);
			if (c.moveToFirst()) {
				for (int i = 2; i <= 7; i++) {
					ids.add(c.getInt(i));
				}
			}
			db.close();
			//Save config Ids on array
			int[] iDs = new int[] { ids.get(0), ids.get(1), ids.get(2),ids.get(3), ids.get(4), ids.get(5) };
			//Pass the array as a result to the other activity
			setResult(RESULT_OK, new Intent().putExtra("configIds", iDs));
			finish();
		} else {
			Toast.makeText(this, "Nothing to Load", Toast.LENGTH_SHORT).show();
		}
	}

	// Method for creating ContextMenu (The menu that appears on long clicks)
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listselection) { // Conditions to make sure context menu comes from the list view
			// Creates an adapter to pass the Id click of the ListView
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(menuList[info.position]); // Title of context menu is the name of the item long clicked
			menu.add(Menu.NONE, 0, 0, "Rename");
			menu.add(Menu.NONE, 1, 1, "Delete");
		}
	}

	// Method for context menu selection
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo(); // This brings the Id of the ListArray LongClicked On
		int menuItemIndex = item.getItemId(); // Gets which of the context menu item was selected

		//Log.d("DEBUG", "I'm long clicking! ID:" + info.position + " which corresponds to item _Id:" + menuListId[info.position]);

		// Long Click Rename
		if (menuItemIndex == 0) {
			Intent i = new Intent(ConfigsManager.this, ConfigsNameEntry.class);
			bundle.putString("renameString", menuList[info.position]);
			bundle.putLong("renameId", menuListId[info.position]);
			i.putExtras(bundle);
			startActivityForResult(i, 0);
		}

		// Long Click Delete
		if (menuItemIndex == 1) {
			db.open();
			db.deleteConfig(menuListId[info.position]);
			db.close();
			getList();
		}

		return true;
	}

	// Method for refreshing/getting the DB configs to the List
	public void getList() {

		//Get all Configs
		db.open();
		Cursor c = db.getAllConfigs();
		ArrayList<String> arrayListNames = new ArrayList<String>();
		ArrayList<Integer> arrayListId = new ArrayList<Integer>();
		if (c.moveToFirst()) {
			do {
				arrayListId.add(c.getInt(0));
				arrayListNames.add(c.getString(1));
			} while (c.moveToNext());
		}
		db.close();

		menuList = new String[arrayListNames.size()];
		menuListId = new long[arrayListId.size()];
		for (int i = 0; i < arrayListNames.size(); i++) {
			menuListId[i] = arrayListId.get(i);
			menuList[i] = arrayListNames.get(i);
		}

		hardwarelist.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, menuList));
	}
}
