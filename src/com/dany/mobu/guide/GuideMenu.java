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



package com.dany.mobu.guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.dany.mobu.R;

public class GuideMenu extends Activity {
	private Intent i;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_menu);
		
		i = new Intent(GuideMenu.this, GuideDisplay.class);
	}
	
	public void CpuGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 0);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void MbGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 1);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void VgaGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 2);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void RamGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 3);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void PsuGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 4);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void HddGMenuButton(View v){
		Bundle bundle = new Bundle();
		bundle.putInt("guideID", 5);
		i.putExtras(bundle);
		startActivity(i);
	}

}