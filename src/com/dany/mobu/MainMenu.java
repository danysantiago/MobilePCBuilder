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



package com.dany.mobu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.dany.mobu.builder.CompabilityTest;
import com.dany.mobu.catalog.HardwarePartsMenu;
import com.dany.mobu.guide.GuideMenu;
import com.dany.mobu.news.News;

public class MainMenu extends Activity {
	
	//Package info object
	PackageInfo info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar (for some reason it must be before layout set)

		setContentView(R.layout.main);
		
		// Read package name and version number from manifest
		try {
			PackageManager manager = this.getPackageManager();
			info = manager.getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void NewsButton(View v) {
		Intent i = new Intent(MainMenu.this, News.class);
		startActivity(i);
	}

	public void GuideButton(View v) {
		Intent i = new Intent(MainMenu.this, GuideMenu.class);
		startActivity(i);
	}

	public void HardwareCatalogButton(View v) {
		Intent i = new Intent(MainMenu.this, HardwarePartsMenu.class);
		startActivity(i);
	}

	public void CompabilityTestButton(View v) {
		Intent i = new Intent(MainMenu.this, CompabilityTest.class);
		startActivity(i);
	}

	// Creates Option Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	// Handles menu item selections
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.about_item) {
			showAboutDialog();
		} else if (item.getItemId() == R.id.contact_item){
			showContactDialog();
		}
		return true;
	}

	private void showContactDialog() {
		Builder aboutDialog = new AlertDialog.Builder(this);
		aboutDialog.setTitle("Contact MoBu's Creator:");
		aboutDialog.setIcon(R.drawable.icon_contact);
		aboutDialog.setMessage("Daniel Santiago Rivera\ndanyboricua91@gmail.com");
		aboutDialog.show();
	}

	private void showAboutDialog() {
		Builder aboutDialog = new AlertDialog.Builder(this);
		aboutDialog.setTitle("About MoBu:");
		aboutDialog.setIcon(R.drawable.icon);
		aboutDialog.setMessage(""
						+ "Version: " + info.versionName + " \n"
						+ "By: Dany\n\n"
						+ "Disclaimer:\n\n"
						+ "About the PC Builder:\n"
						+ "I am not responsible if you try to build a computer and it blows up. This app is intended to be as a guide. "
						+ "Before ordering the components to one's computer one should have done enough research to know that it will all work together, even though "
						+ "many hardware companies will exchange your part for the desired one, it is time consuming and they only do it if no harm was done to the part."
						+ "\n\n"
						+ "About the Guide:\n"
						+ "The information provided in this guide is verified by me (A CS Student AND a Human being (Humans make errors)) and it is intended as an introduction "
						+ "to the knowledge needed to build a computer. There is ALOT more about computer that there is in these guides."
						+ "\n\n"
						+ "About the Catalog:\n"
						+ "The catalog only stores computer hardware that haven't been more than just a few years in the market, that is, do not pretend to find an AGP based Video Card "
						+ "(A very old video card) in this catalog. The database is stored online and so if it is updated, which I will try to update it periodically, there will be no need "
						+ "to update/reinstall the app, the app automatically looks for the latest version at startup, this process can be skipped thou, by pressing the back key."
						+ "\n\n"
						+ "About the News:\n"
						+ "The news are provided by the sources mentioned there, these news are simple RSS that they provide freely, so I'm not breaking any law there."
						+ "\n\n"
						+ "Other things:\n"
						+ "I hope you like the app, please rate it (but don't kill it), it's my first app and is under development, I kindly receive any suggestion made at: "
						+ "danyboricua91@gmail.com");
		aboutDialog.show();
	}
}
