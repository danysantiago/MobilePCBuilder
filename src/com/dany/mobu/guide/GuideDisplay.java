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
import android.os.Bundle;

import com.dany.mobu.R;

public class GuideDisplay extends Activity {
	
	public static final String[] guideTitles = new String[]{
		"CPU - Central Processing Unit",
		"MB - Motherboard",
		"VGA - Video Graphics Acelerator",
		"RAM - Random Access Memory",
		"PSU - Power Supply Unit",
		"HDD - Hard Disk Drive"
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getIntent().getExtras();

		// Sets windows title
		setTitle(guideTitles[bundle.getInt("guideID")]);

		if (bundle.getInt("guideID") == 0) {
			setContentView(R.layout.guide_cpu);
		} else if (bundle.getInt("guideID") == 1) {
			setContentView(R.layout.guide_mb);
		} else if (bundle.getInt("guideID") == 2) {
			setContentView(R.layout.guide_vga);
		} else if (bundle.getInt("guideID") == 3) {
			setContentView(R.layout.guide_ram);
		} else if (bundle.getInt("guideID") == 4) {
			setContentView(R.layout.guide_psu);
		} else if (bundle.getInt("guideID") == 5) {
			setContentView(R.layout.guide_hdd);
		}

	}
}