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



package com.dany.mobu.databases;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//Class to handle Database data tables and the inserting of items.
public class HardwareDB {
	
	//The DB tables are list of type Hardwareitem
	private ArrayList<HardwareItem> cpu_table;
	private ArrayList<HardwareItem> mb_table;
	private ArrayList<HardwareItem> vga_table;
	private ArrayList<HardwareItem> ram_table;
	private ArrayList<HardwareItem> psu_table;
	private ArrayList<HardwareItem> hdd_table;
	
	//Method that inserts the data in the local DB with SQL statements
	//This basically method creates SQL Strings based on the data
	public void insertDB(SQLiteDatabase db){
		String insertItem = new String();

		/**
		* CPU TABLE CREATION
		* Layout (7 items):
		* ID|NAME|Socket|Speed|Cores|Threads|TDP|UnlockedStatus|
		*/
		
		insertItem = "INSERT INTO cpu_table (id, row1, row2, row3, row4, row5, row6) VALUES ";
		for(int i = 0; i < cpu_table.size(); i++){
			HardwareItem item = cpu_table.get(i);

			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "','" +
					item.getRow(0) + "'," +
					item.getRow(1) + ",'" +
					item.getRow(2) + "'," +
					item.getRow(3) + ",'" +
					item.getRow(4) + "'" +
					")"
			);
		}
			
		/**
		* MB TABLE CREATION
		* Layout (13 items):
		* ID|Name|Socket|Chipset|FSB|Max Memory Spd|Memory Type|Memory Slots|PCI Express x16 Ports|SATA Ports|SATA Spd|RAID Support|Form Factor|
		*/

		insertItem = "INSERT INTO mb_table (id, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10, row11, row12) VALUES ";
		for(int i = 0; i < mb_table.size(); i++){
			HardwareItem item = mb_table.get(i);
			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "','" +
					item.getRow(0) + "','" +
					item.getRow(1) + "','" +
					item.getRow(2) + "'," +
					item.getRow(3) + ",'" +
					item.getRow(4) + "'," +
					item.getRow(5) + "," +
					item.getRow(6) + "," +
					item.getRow(7) + ",'" +
					item.getRow(8).replace(":and:", "&") + "','" +
					item.getRow(9) + "','" +
					item.getRow(10) + "'" +
					")"
			);
		}

		/**
		* VGA TABLE CREATION
		* Layout (10 items):
		* ID|Name|Interface|Video Memory|Memory Type|Core Clock|Memory Clock|Multiple Monitor|APIs|Minimum PSU|
		*/

		insertItem = "INSERT INTO vga_table (id, row1, row2, row3, row4, row5, row6, row7, row8, row9) VALUES ";
		for(int i = 0; i < vga_table.size(); i++){
			HardwareItem item = vga_table.get(i);	
			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "','" +
					item.getRow(0) + "'," +
					item.getRow(1) + ",'" +
					item.getRow(2) + "'," +
					item.getRow(3) + "," +
					item.getRow(4) + ",'" +
					item.getRow(5) + "','" +
					item.getRow(6).replace(":and:", "&") + "'," +
					item.getRow(7) + "" +
					")"
			);
		}
		
		  
		/**
		* RAM TABLE CREATION
		* Layout (9 items):
		* ID|Name|Memory Type|Memory Speed|Memory Speed in MHz|Memory Size (In MB)|Total Memory Size (In GB)|Memory Modules|Timings|
		*/

		insertItem = "INSERT INTO ram_table (id, row1, row2, row3, row4, row5, row6, row7, row8) VALUES ";
		for(int i = 0; i < ram_table.size(); i++){
			HardwareItem item = ram_table.get(i);
			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "','" +
					item.getRow(0) + "','" +
					item.getRow(1) + "'," +
					item.getRow(2) + "," +
					item.getRow(3) + "," +
					item.getRow(4) + "," +
					item.getRow(5) + ",'" +
					item.getRow(6) + "'" +
					")"
			);
		}

		/**
		* PSU TABLE CREATION
		* Layout (12 items):
		* ID|Name|Wattage|Modular Cabling|Energy Efficiency|MB Connector|MB+ Connector|6-Pin PCI Express|8-Pin PCI Express|4-Pin Peripheral|SATA|Certified|
		*/

		insertItem = "INSERT INTO psu_table (id, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10, row11) VALUES ";
		for(int i = 0; i < psu_table.size(); i++){
			HardwareItem item = psu_table.get(i);
			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "'," +
					item.getRow(0) + ",'" +
					item.getRow(1) + "','" +
					item.getRow(2) + "'," +
					item.getRow(3) + ",'" +
					item.getRow(4) + "'," +
					item.getRow(5) + "," +
					item.getRow(6) + "," +
					item.getRow(7) + "," +
					item.getRow(8) + ",'" +
					item.getRow(9).replace(":and:", "&") + "'" +
					")"
			);
		}

		/**
		* HDD TABLE CREATION
		* Layout (6 items):
		* ID|Name|Drive Type|Interface|Write Speed|Read Speed|
		*/

		insertItem = "INSERT INTO hdd_table (id, row1, row2, row3, row4, row5) VALUES ";
		for(int i = 0; i < hdd_table.size(); i++){
			HardwareItem item = hdd_table.get(i);
			db.execSQL(insertItem + "(" + 
					item.getId() + ",'" +
					item.getName() + "','" +
					item.getRow(0) + "','" +
					item.getRow(1) + "'," +
					item.getRow(2) + "," +
					item.getRow(3) + "" +
					")"
			);
		}
		
		Log.d("DEBUG", "Sucessfully inserted DB");
	}
	
	//Setters
	public void setCpu_table(ArrayList<HardwareItem> cpu_table) {
		this.cpu_table = cpu_table;
	}

	public void setMb_table(ArrayList<HardwareItem> mb_table) {
		this.mb_table = mb_table;
	}

	public void setVga_table(ArrayList<HardwareItem> vga_table) {
		this.vga_table = vga_table;
	}

	public void setRam_table(ArrayList<HardwareItem> ram_table) {
		this.ram_table = ram_table;
	}

	public void setPsu_table(ArrayList<HardwareItem> psu_table) {
		this.psu_table = psu_table;
	}

	public void setHdd_table(ArrayList<HardwareItem> hdd_table) {
		this.hdd_table = hdd_table;
	}

}
