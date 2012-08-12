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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dany.mobu.R;
import com.dany.mobu.databases.HardwareDBManager;

public class HardwarePartsMenu extends Activity {
	
	private Bundle bundle;
	private Intent i;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.catalog_menu_layout);
		
		bundle = new Bundle();
		i = new Intent(HardwarePartsMenu.this,HardwarePartsList.class);
	}
	
	public void CpuMenuButton(View v) {
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_CPU);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void MbMenuButton(View v){
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_MB);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void VgaMenuButton(View v){
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_VGA);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void RamMenuButton(View v){
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_RAM);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void PsuMenuButton(View v){
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_PSU);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void HddMenuButton(View v){
		bundle.putString("table_name",HardwareDBManager.TABLE_NAME_HDD);
		i.putExtras(bundle);
		startActivity(i);
	}

	// Search Button Pressed
	public void SearchButton(View v) {
		onSearchRequested();
	}

	// Act when a search is requested (this can be by search button or calling
	// the method)
	@Override
	public boolean onSearchRequested() {

		Intent j = new Intent(HardwarePartsMenu.this, CatalogSearch.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean("fullSearch", true);
		j.putExtras(bundle);
		startActivity(j);

		return false;
	}
}
