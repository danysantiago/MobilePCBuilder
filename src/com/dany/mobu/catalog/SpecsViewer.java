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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dany.mobu.R;
import com.dany.mobu.StartUpActivity;
import com.dany.mobu.databases.HardwareDBManager;

public class SpecsViewer extends Activity {

	protected ListView specsListView;

	protected String[] specsList;

	protected String partName;

	protected int hardwareSearchSourceId;

	public static String[] hardwareSourceNamesList = new String[] {
		"Newegg", "Amazon", "TigerDirect" };

	public static String[] hardwareSearchURL = new String[] {
		"http://m.newegg.com/Product/ProductList.aspx?Submit=ENE&DEPA=0&Description=",
		"http://www.amazon.com/gp/aw/s/ref=is_box?k=",
		"http://m.tigerdirect.com/m/http/www.tigerdirect.com/applications/SearchTools/search.asp?keywords=" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.specs_viewer_layout);

		Bundle bundle = this.getIntent().getExtras(); // Creates the object from the object brought from the last activity (ie HardwareParts_CPU)

		HardwareDBManager db = StartUpActivity.db;; // Reads DB

		// Stores array with the row brought from the query with the id brought from the last activity
		ArrayList<Object> data = db.getRowInTable(bundle.getString("table_name"), bundle.getLong("_Id"));

		// Gets the Name of the part (for header text and search url)
		partName = (String) data.get(1);

		// Gets the SharedPreferences object used to maintain persistent state (Saves Price Search Source)
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		hardwareSearchSourceId = preferences.getInt("hardwareSearchId", 0);

		// Prepares the ListView
		specsListView = (ListView) findViewById(R.id.specsViewerListView);
		// specsListView.setDivider(null); //Sets the ListView without divider lines

		// Prepares a TextView that will serve as the title of the ListView
		TextView header = new TextView(this.getBaseContext());
		header.setTextSize(30);
		header.setText(partName);

		specsListView.addHeaderView(header); // Insert TextView into ListView as header

		// Conditions for the kind of data to be displayed
		if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_CPU)) {
			specsList = new String[5]; // Creates string from the strings of the row stores in data
			specsList[0] = "Socket: " + data.get(2);
			specsList[1] = "Speed: " + data.get(3) + "MHz";
			specsList[2] = "# Cores (Threads): " + data.get(4);
			specsList[3] = "TDP: " + data.get(5) + "W";
			specsList[4] = "Unlocked: " + data.get(6);
		} else if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_MB)) {
			specsList = new String[11];
			specsList[0] = "Socket: " + data.get(2);
			specsList[1] = "Chipset: " + data.get(3);
			specsList[2] = "FSB: " + data.get(4);
			specsList[3] = "Max Memory Speed: " + data.get(5) + "MHz";
			specsList[4] = "Memory Type: " + data.get(6);
			specsList[5] = "Memory Slots: " + data.get(7);
			specsList[6] = "PCI Express x16 Slots: " + data.get(8);
			specsList[7] = "SATA Ports: " + data.get(9);
			specsList[8] = "SATA Speed: " + data.get(10) + "GB/s";
			specsList[9] = "RAID Support: " + data.get(11);
			specsList[10] = "Form Factor: " + data.get(12);
		} else if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_VGA)) {
			specsList = new String[8];
			specsList[0] = "Interface: " + data.get(2);
			specsList[1] = "Memory Size: " + data.get(3) + "MB";
			specsList[2] = "Memory Type: " + data.get(4);
			specsList[3] = "Core Clock: " + data.get(5) + "MHz";
			specsList[4] = "Memory Clock: " + data.get(6) + "MHz";
			specsList[5] = "Multiple Monitor Support: " + data.get(7);
			specsList[6] = "APIs : " + data.get(8);
			specsList[7] = "Minimum PSU : " + data.get(9) + "W";
		} else if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_RAM)) {
			specsList = new String[7];
			specsList[0] = "Memory Type: " + data.get(2);
			specsList[1] = "Memory Speed: " + data.get(3);
			specsList[2] = "Memory Speed in MHz: " + data.get(4) + "MHz";
			specsList[3] = "Memory Size: " + data.get(5) + "MB";
			specsList[4] = "Total Memory Size: " + data.get(6) + "GB";
			specsList[5] = "Memory Modules : " + data.get(7);
			specsList[6] = "Timings : " + data.get(8);
		} else if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_PSU)) {
			specsList = new String[10];
			specsList[0] = "Wattage: " + data.get(2) + "W";
			specsList[1] = "Modular Cableing: " + data.get(3);
			specsList[2] = "Energy Efficient: " + data.get(4);
			specsList[3] = "Motherboard Connector: " + data.get(5) + "-Pin";
			specsList[4] = "+12V MB Connector: " + data.get(6) + "-Pin";
			specsList[5] = "6-Pin PCI Express: x" + data.get(7);
			specsList[6] = "8-Pin PCI Express: x" + data.get(8);
			specsList[7] = "4-Pin Peripheral: x" + data.get(9);
			specsList[8] = "SATA Connectors: x" + data.get(10);
			specsList[9] = "Certified: " + data.get(11);
		} else if (bundle.getString("table_name").equals(HardwareDBManager.TABLE_NAME_HDD)) {
			specsList = new String[4];
			specsList[0] = "Drive Type: " + data.get(2);
			if (data.get(3).equals("IDE"))
				specsList[1] = "Interface: " + data.get(3);
			else
				specsList[1] = "Interface: SATA " + data.get(3) + "Gb/s";
			specsList[2] = "Write Speed: " + data.get(4) + "MB/s";
			specsList[3] = "Read Speed: " + data.get(5) + "MB/s";
		}

		specsListView.setAdapter(new ArrayAdapter<String>(this,R.layout.small_text_listview, specsList));
	}

	// onPause override to save preferences for persistent state activity
	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("hardwareSearchId", hardwareSearchSourceId);
		editor.commit();

	}

	// Price button selected
	public void WebSearch(View view) {
		//Log.d("DEBUG","Perfoming Part Search with URL: " + Uri.parse(hardwareSearchURL[hardwareSearchSourceId]+ partName.replaceAll(" ", "+")));
		Intent browserIntent = new Intent("android.intent.action.VIEW",Uri.parse(hardwareSearchURL[hardwareSearchSourceId] + partName.replaceAll(" ", "+")));
		startActivity(browserIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.specs_menu, menu);
		return true;
	}

	// Handles menu item selections
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.change_search_source_item) {
			//Log.d("DEBUG", "Change Search Source Selected");
			// Single Choice Dialog Builder for selecting Search Source
			Builder sourceDialog = new AlertDialog.Builder(this);
			sourceDialog.setTitle("Search Source:");
			sourceDialog.setSingleChoiceItems(hardwareSourceNamesList,hardwareSearchSourceId,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// Register selection when user selects new source
					//Log.d("DEBUG", "Source Id changed to: " + hardwareSearchSourceId);
					hardwareSearchSourceId = id;
				}
			});
			sourceDialog.setPositiveButton("Done", null);
			sourceDialog.setCancelable(true);
			sourceDialog.show();
		}
		return true;
	}

	@Override
	public boolean onSearchRequested() {
		Builder searchDialog = new AlertDialog.Builder(this);
		searchDialog.setMessage("Perform a Web Search for this component?");
		searchDialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				WebSearch(specsListView);
			}
		});
		searchDialog.setNegativeButton("No",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		searchDialog.setCancelable(true);
		searchDialog.show();
		return false;
	}

}
