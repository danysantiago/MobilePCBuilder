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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dany.mobu.R;
import com.dany.mobu.StartUpActivity;
import com.dany.mobu.catalog.SpecsViewer;
import com.dany.mobu.databases.HardwareDBManager;

public class CompabilityTest extends Activity {

	public static HardwareDBManager db = StartUpActivity.db;

	// Constant array of the table names to iterated over them
	public static final String[] tableNames = new String[] {
			HardwareDBManager.TABLE_NAME_MB, HardwareDBManager.TABLE_NAME_CPU,
			HardwareDBManager.TABLE_NAME_RAM, HardwareDBManager.TABLE_NAME_VGA,
			HardwareDBManager.TABLE_NAME_PSU, HardwareDBManager.TABLE_NAME_HDD };

	// Constant used to identify what to get
	public static final int[] GET_COMPONENT = new int[] { 0, 1, 2, 3, 4, 5 };
	public static final int GET_CONFIG = 6;
	public static final int GET_SAVE = 7;

	// Array used to identify if component is installed
	protected static boolean[] installedComponent = new boolean[6];

	protected Bundle bundle;
	protected Bundle bundleSpecs;
	protected Intent i;

	// Buttons used in the layout
	protected Button cpuButton;
	protected Button ramButton;
	protected Button vgaButton;
	protected Button psuButton;
	protected Button hddButton;

	protected int contextMenuViewID;

	// Arrays used in the ListView and reference to the component's _Id
	protected ListView componentsListView;
	protected final String[] componentsListDefault = new String[] { "MB: ", "CPU: ", "RAM: ", "VGA: ", "PSU: ", "HDD: " };
	protected String[] componentsList = new String[] { "MB: ", "CPU: ", "RAM: ", "VGA: ", "PSU: ", "HDD: " };
	public static long[] componentsListId = new long[6];

	// Boolean used for persistent state activity
	protected boolean newConfig;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.compability_test_layout);

		// Bundle to pass table names to components selector
		bundle = new Bundle();
		i = new Intent(CompabilityTest.this, HardwarePartsSelection.class);

		cpuButton = (Button) findViewById(R.id.button_cpu);
		ramButton = (Button) findViewById(R.id.button_ram);
		vgaButton = (Button) findViewById(R.id.button_vga);
		psuButton = (Button) findViewById(R.id.button_psu);
		hddButton = (Button) findViewById(R.id.button_hdd);

		// Register Buttons for LongClick menu
		registerForContextMenu(cpuButton);
		registerForContextMenu(ramButton);
		registerForContextMenu(vgaButton);
		registerForContextMenu(psuButton);
		registerForContextMenu(hddButton);

		componentsListView = (ListView) findViewById(R.id.listView_components);
		componentsListView.setClickable(true);
		componentsListView.setLongClickable(true);
		registerForContextMenu(componentsListView); // Registers the ListView
													// for the LongClick context
													// menu

		// Component List View Click Listener
		componentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> av, View v, int pos,
							long id) {
						onListItemClick(v, pos, id);
					}
				});

		// Gets the SharedPreferences object used to maintain persistent state
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);

		// Persistent state activity getters
		newConfig = preferences.getBoolean("newConfig", true);
		for (int i = 0; i < componentsListId.length; i++) {
			componentsListId[i] = preferences.getLong("draftId" + i, 0);
		}

		if (newConfig) {
			startLoadDialog();
		}

		updateGUI();
	}

	// onPause override to save preferences for persistent state activity
	@Override
	public void onPause() {
		super.onPause();

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("newConfig", newConfig);
		for (int i = 0; i < componentsListId.length; i++) {
			editor.putLong("draftId" + i, componentsListId[i]);
		}
		editor.commit(); // Some kind of editor is used to save in the SharedPreferences object

	}

	// Method to handle click on the ListView
	protected void onListItemClick(View v, int pos, long id) {
		// Based on component clicks open desired selection menu
		if (id == 0) {
			// Message for editing motherboard, it's better to start from zero
			AlertDialog.Builder adb = new AlertDialog.Builder(CompabilityTest.this);
			adb.setTitle("Note");
			adb.setIcon(R.drawable.icon_stop);
			adb.setMessage("The motherboard is the base component of your configuration if you would like to change it please start a new configuration.");
			adb.setPositiveButton("Ok", null);
			adb.show();
		} else if (id == 1) {
			cpu_button(componentsListView); // When clicked item is starts the button corresponding to that item
		} else if (id == 2) {
			ram_button(componentsListView);
		} else if (id == 3) {
			vga_button(componentsListView);
		} else if (id == 4) {
			psu_button(componentsListView);
		} else if (id == 5) {
			hdd_button(componentsListView);
		}
	}

	// Method to handle received result from other activities
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handle result for Get MB
		if (requestCode == GET_COMPONENT[0]) {
			if (resultCode == RESULT_OK) {
				componentsListId[GET_COMPONENT[0]] = data.getExtras().getLong("resultId");
				newConfig = false;
			} else if (requestCode == RESULT_CANCELED) {
				finish();
			}
		}

		// Handles the rest of the result from the other GETs Component
		if (requestCode > GET_COMPONENT[0] || requestCode <= GET_COMPONENT.length) {
			for (int i = 1; i < GET_COMPONENT.length; i++) {
				if (requestCode == GET_COMPONENT[i]) {
					if (resultCode == RESULT_OK) {
						componentsListId[GET_COMPONENT[i]] = data.getExtras().getLong("resultId");
					}
				}
			}
		}

		// Handles load of configurations
		if (requestCode == GET_CONFIG) {
			if (resultCode == RESULT_OK) {
				int[] ids = data.getExtras().getIntArray("configIds");
				for (int i = 0; i < GET_COMPONENT.length; i++) {
					componentsListId[i] = ids[i];
				}
				newConfig = false;
			} else {
				// If Load is canceled and mb is never installed close activity
				if (componentsListId[0] == 0)
					finish();
			}
		}

		// Handle saving configurations
		if (requestCode == GET_SAVE) {
			if (resultCode == RESULT_OK) {
				ConfigurationsDBManager db = new ConfigurationsDBManager(this);
				db.open();
				db.saveNewConfig(data.getExtras().getString("renameString"),componentsListId);
				db.close();
				Toast.makeText(this, "Saving sucessful", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Saving canceled", Toast.LENGTH_SHORT).show();
			}
		}

		updateGUI();
	}

	// Creates Option Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater(); // Some kind of menu inflater
		inflater.inflate(R.menu.cpt_menu, menu); // Inflates the menu with my desired xml menu
		return true;
	}

	// Method for creating ContextMenu (The menu that appears on long clicks)
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		// Depending on view long clicked set an ID
		if (v.getId() == R.id.listView_components) { // Check which view isbeing long clicked to create menu
			// Gets the ArrayList Id being clicked on
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			contextMenuViewID = info.position;
		} else if (v.getId() == R.id.button_cpu) {
			contextMenuViewID = 1;
		} else if (v.getId() == R.id.button_ram) {
			contextMenuViewID = 2;
		} else if (v.getId() == R.id.button_vga) {
			contextMenuViewID = 3;
		} else if (v.getId() == R.id.button_psu) {
			contextMenuViewID = 4;
		} else if (v.getId() == R.id.button_hdd) {
			contextMenuViewID = 5;
		}

		// Creates Context Menu if there is an item present
		if (componentsListId[contextMenuViewID] != 0) {
			menu.setHeaderTitle(componentsListDefault[contextMenuViewID]); // Title of context menu is the name of the item long clicked
			menu.add(Menu.NONE, 0, 0, "View Specifications");
			menu.add(Menu.NONE, 1, 1, "Remove Component"); // Adds to the context menu the item
		}
	}

	// Methods called when buttons are pressed
	public void cpu_button(View v) {
		bundle.putString("table_name", HardwareDBManager.TABLE_NAME_CPU);
		i.putExtras(bundle);
		startActivityForResult(i, GET_COMPONENT[1]); // Starts activity to retrive result
	}

	public void ram_button(View v) {
		bundle.putString("table_name", HardwareDBManager.TABLE_NAME_RAM);
		i.putExtras(bundle);
		startActivityForResult(i, GET_COMPONENT[2]);
	}

	public void vga_button(View v) {
		bundle.putString("table_name", HardwareDBManager.TABLE_NAME_VGA);
		i.putExtras(bundle);
		startActivityForResult(i, GET_COMPONENT[3]);
	}

	public void psu_button(View v) {
		bundle.putString("table_name", HardwareDBManager.TABLE_NAME_PSU);
		i.putExtras(bundle);
		startActivityForResult(i, GET_COMPONENT[4]);
	}

	public void hdd_button(View v) {
		bundle.putString("table_name", HardwareDBManager.TABLE_NAME_HDD);
		i.putExtras(bundle);
		startActivityForResult(i, GET_COMPONENT[5]);
	}

	// Handles menu item selections
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.load_item) { // Load Item
			Intent j = new Intent(CompabilityTest.this, ConfigsManager.class);
			startActivityForResult(j, GET_CONFIG);

		} else if (item.getItemId() == R.id.save_item) { // Save Item
			Intent j = new Intent(CompabilityTest.this, ConfigsNameEntry.class);
			bundle.putString("renameString", new String("New Config"));
			bundle.putLong("renameId", -1);
			j.putExtras(bundle);
			startActivityForResult(j, GET_SAVE);

		} else if (item.getItemId() == R.id.clear_item) {// Clear Item
			startLoadDialog();

		} else if (item.getItemId() == R.id.help_item) {// Help Item
			Intent h = new Intent(CompabilityTest.this,CompabilityTestHelp.class);
			startActivity(h);
		}
		return true;
	}

	// Method for context menu selection
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int menuItemIndex = item.getItemId(); // Gets which of the context menu item was selected

		if (menuItemIndex == 0) {// Responds to the context menu item at position 0 (View Specs)
			bundleSpecs = new Bundle();
			bundleSpecs.putString("table_name", tableNames[contextMenuViewID]);
			bundleSpecs.putLong("_Id", componentsListId[contextMenuViewID]);
			Intent i = new Intent(CompabilityTest.this, SpecsViewer.class);
			i.putExtras(bundleSpecs);
			startActivity(i);
		} else if (menuItemIndex == 1) { // Responds to the context menu item at position 1 (Remove)
			if (contextMenuViewID == 0) { // Check to see if removing item is not the MB
				Toast toast = Toast.makeText(this,"Motherboard cannot be removed, please start a new configuration.",Toast.LENGTH_SHORT);
				toast.show();
			} else { // If not the motherboard remove item
				componentsListId[contextMenuViewID] = 0;
				updateGUI();
			}
		}

		return true;
	}

	// A method created to set some parameters when a newConfig is being made
	public void startNew() {
		newConfig = true;
		installedComponent = new boolean[] { false, false, false, false, false, false };
		componentsListId = new long[] { 0, 0, 0, 0, 0, 0 };
	}

	// Dialog starter
	public void startLoadDialog() {

		// Alert dialog builder
		AlertDialog.Builder newConfigDialog = new AlertDialog.Builder(this);
		newConfigDialog.setMessage("Load or Start a new configuration?");
		newConfigDialog.setCancelable(true);
		newConfigDialog.setPositiveButton("Load", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Method for "Load" button clicked on alert dialog
						startNew();
						Intent j = new Intent(CompabilityTest.this,ConfigsManager.class);
						startActivityForResult(j, GET_CONFIG);
					}
				});
		newConfigDialog.setNegativeButton("Start New",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Method for "Start New" button clicked on alert dialog
						startNew();
						bundle.putString("table_name",HardwareDBManager.TABLE_NAME_MB);
						i.putExtras(bundle);
						startActivityForResult(i, GET_COMPONENT[0]);
					}
				});
		newConfigDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						if (newConfig == true) {// If new config and dialog is canceled finish activity
							finish();
						}
					}
				});
		AlertDialog dialogLoadStart = newConfigDialog.create(); // Creates dialog based on builder (builder above)
		dialogLoadStart.show(); // Shows dialog
	}

	// My method for updating the graphics interface
	public void updateGUI() {

		// Makes the arrays necessary for the update
		for (int i = 0; i < componentsListId.length; i++) {
			if (componentsListId[i] != 0) {
				ArrayList<Object> data = new ArrayList<Object>(db.getRowInTable(tableNames[i], componentsListId[i]));
				componentsList[i] = componentsListDefault[i] + data.get(1);
				installedComponent[i] = true;
			} else {
				componentsList[i] = componentsListDefault[i] + "-";
				installedComponent[i] = false;
			}
		}

		// Sets the new array to the ListView
		componentsListView.setAdapter(new ArrayAdapter<String>(this,R.layout.small_text_listview, componentsList));

		if (installedComponent[1] == true) {
			cpuButton.setBackgroundResource(R.drawable.button_cpu);
		} else {
			cpuButton.setBackgroundResource(R.drawable.button_cpu_uninstalled);
		}

		if (installedComponent[2] == true) {
			ramButton.setBackgroundResource(R.drawable.button_ram);
		} else {
			ramButton.setBackgroundResource(R.drawable.button_ram_uninstalled);
		}

		if (installedComponent[3] == true) {
			vgaButton.setBackgroundResource(R.drawable.button_vga);
		} else {
			vgaButton.setBackgroundResource(R.drawable.button_vga_uninstalled);
		}

		if (installedComponent[4] == true) {
			psuButton.setBackgroundResource(R.drawable.button_psu);
		} else {
			psuButton.setBackgroundResource(R.drawable.button_psu_uninstalled);
		}

		if (installedComponent[5] == true) {
			hddButton.setBackgroundResource(R.drawable.button_hdd);
		} else {
			hddButton.setBackgroundResource(R.drawable.button_hdd_uninstalled);
		}

	}

	// Method called when search key pressed
	@Override
	public boolean onSearchRequested() {

		return false;
	}
}
