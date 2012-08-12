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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dany.mobu.builder.*;

public class HardwareDBManager {

	// the Activity or Application that is creating an object from this class
	Context context;

	private SQLiteDatabase db; // reference to the database manager class
	public static HardwareDB dataSet;
	public static final String DB_NAME = "HardwareDB";
	public static int DB_VERSION; // version of the database (increase these between builds to upgrade)
	
	
	// name of db columns

	public static final String TABLE_ROW_ID = "id";

	public static final String TABLE_NAME_CPU = "cpu_table";
	public static final String TABLE_NAME_MB = "mb_table";
	public static final String TABLE_NAME_VGA = "vga_table";
	public static final String TABLE_NAME_RAM = "ram_table";
	public static final String TABLE_NAME_PSU = "psu_table";
	public static final String TABLE_NAME_HDD = "hdd_table";

	public static final String ROW1_NAME = "row1";
	public static final String ROW2_NAME = "row2";
	public static final String ROW3_NAME = "row3";
	public static final String ROW4_NAME = "row4";
	public static final String ROW5_NAME = "row5";
	public static final String ROW6_NAME = "row6";
	public static final String ROW7_NAME = "row7";
	public static final String ROW8_NAME = "row8";
	public static final String ROW9_NAME = "row9";
	public static final String ROW10_NAME = "row10";
	public static final String ROW11_NAME = "row11";
	public static final String ROW12_NAME = "row12";

	public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

		public CustomSQLiteOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		// The onCreate method creates the local DB
		public void onCreate(SQLiteDatabase db) {

			//Create tables with the data type based on the table being created
			db.execSQL("create table " + TABLE_NAME_CPU + "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " text," 
					+ ROW3_NAME + " integer," 
					+ ROW4_NAME + " text," 
					+ ROW5_NAME + " integer,"
					+ ROW6_NAME + " text" 
					+ ");");
			
			db.execSQL("create table " + TABLE_NAME_MB +  "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " text," 
					+ ROW3_NAME + " text," 
					+ ROW4_NAME + " text," 
					+ ROW5_NAME + " integer,"
					+ ROW6_NAME + " text," 
					+ ROW7_NAME + " integer," 
					+ ROW8_NAME + " integer," 
					+ ROW9_NAME + " integer," 
					+ ROW10_NAME + " text,"
					+ ROW11_NAME + " text," 
					+ ROW12_NAME + " text" 
					+ ");");
			
			db.execSQL("create table " + TABLE_NAME_VGA +  "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " text," 
					+ ROW3_NAME + " integer," 
					+ ROW4_NAME + " text," 
					+ ROW5_NAME + " integer,"
					+ ROW6_NAME + " integer," 
					+ ROW7_NAME + " text," 
					+ ROW8_NAME + " text," 
					+ ROW9_NAME + " integer" 
					+ ");");
			
			db.execSQL("create table " + TABLE_NAME_RAM +  "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " text," 
					+ ROW3_NAME + " integer," 
					+ ROW4_NAME + " integer," 
					+ ROW5_NAME + " integer,"
					+ ROW6_NAME + " integer," 
					+ ROW7_NAME + " integer," 
					+ ROW8_NAME + " text" 
					+ ");");
			
			db.execSQL("create table " + TABLE_NAME_PSU +  "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " integer," 
					+ ROW3_NAME + " text," 
					+ ROW4_NAME + " text," 
					+ ROW5_NAME + " integer,"
					+ ROW6_NAME + " text," 
					+ ROW7_NAME + " integer," 
					+ ROW8_NAME + " integer," 
					+ ROW9_NAME + " integer," 
					+ ROW10_NAME + " integer,"
					+ ROW11_NAME + " text" 
					+ ");");
			
			db.execSQL("create table " + TABLE_NAME_HDD +  "( "
					+ TABLE_ROW_ID + " integer primary key autoincrement not null,"
					+ ROW1_NAME + " text," 
					+ ROW2_NAME + " text," 
					+ ROW3_NAME + " text," 
					+ ROW4_NAME + " integer," 
					+ ROW5_NAME + " integer"
					+ ");");
			
			//Insert the dataSet parsed from the server into the local Database
			if (DB_VERSION != 1)
				dataSet.insertDB(db);
		
		}

		// When the Database Version changes this method is executed, I will be using it to upgrade the database
		// with new information by destroy everything and calling the onCreate method again.
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w("DB Notification", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CPU);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MB);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RAM);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VGA);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PSU);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HDD);
			DB_VERSION = newVersion;
			onCreate(db);
		}
	}

	public HardwareDBManager(Context context, int db_version) {
		HardwareDBManager.DB_VERSION = db_version;
		this.context = context;

		// create or open the database
		CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
		this.db = helper.getWritableDatabase();
	}

	// Gets a Row of a table in the DB and returns it as an ArrayList
	public ArrayList<Object> getRowInTable(String tableName, long rowID) {

		ArrayList<Object> rowArray = new ArrayList<Object>();
		Cursor cursor;

		try {
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			
			//Depending on which item request get all rows
			cursor = db.query(tableName, null, TABLE_ROW_ID + "=" + rowID, null,
					null, null, null, null);

			// move the pointer to position zero in the cursor.
			cursor.moveToFirst();

			// if there is data available after the cursor's pointer, add
			// it to the ArrayList that will be returned by the method.
			if (!cursor.isAfterLast()) {
				do {
					rowArray.add(cursor.getLong(0));
					int k = 0;
					
					//Depending of table request set the amount of columns to get
					if(tableName.equals(TABLE_NAME_CPU)){
						k = 6;
					} else if (tableName.equals(TABLE_NAME_MB)){
						k = 12;
					} else if (tableName.equals(TABLE_NAME_RAM)){
						k = 8;
					} else if (tableName.equals(TABLE_NAME_VGA)){
						k = 9;
					} else if (tableName.equals(TABLE_NAME_PSU)){
						k = 11;
					} else if (tableName.equals(TABLE_NAME_HDD)){
						k = 5;
					}
					
					for (int i = 1; i <= k; i++) {
						if (cursor.getString(i) != null)
							rowArray.add(cursor.getString(i));
					}
				} while (cursor.moveToNext());
			}

			// let java know that you are through with the cursor.
			cursor.close();
		} catch (Exception e) {
			Log.e("DB ERROR", e.toString());
		}

		// return the ArrayList containing the given row from the database.
		return rowArray;
	}

	// Get all rows of a table in the DB (the row only includes the id and the name)
	public ArrayList<ArrayList<Object>> getAllRowsInTable(String tableName) {
		// create an ArrayList that will hold all of the data collected from the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		// this is a database call that creates a "cursor" object.
		// the cursor object store the information collected from the
		// database and is used to iterate through the data.
		Cursor cursor;

		try {
			// ask the database object to create the cursor.
			cursor = db.query(tableName,
					new String[] { TABLE_ROW_ID, ROW1_NAME }, null, null, null,
					null, ROW1_NAME);

			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		return dataArrays;
	}

	//Search table method
	public ArrayList<ArrayList<Object>> searchTable(String tableName,
			String searchText) {
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		Cursor cursor;

		try {
			cursor = db.query(tableName, new String[] { TABLE_ROW_ID, ROW1_NAME }, 
					ROW1_NAME + " LIKE '%" + searchText + "%'", 
					null, null, null, null);

			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));

					dataArrays.add(dataList);
				}

				while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
		}

		return dataArrays;
	}

	//Search database method
	public ArrayList<ArrayList<Object>> searchDatabase(String searchText) {
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		Cursor cursor;

		try {
			for (String tableName : CompabilityTest.tableNames) {
				cursor = db.query(tableName, new String[] { TABLE_ROW_ID, ROW1_NAME },
						ROW1_NAME + " LIKE '%" + searchText + "%'", 
						null, null, null, null);

				cursor.moveToFirst();
				if (!cursor.isAfterLast()) {
					do {
						ArrayList<Object> dataList = new ArrayList<Object>();

						dataList.add(cursor.getLong(0));
						dataList.add(cursor.getString(1));
						dataList.add(tableName);

						dataArrays.add(dataList);
					}

					while (cursor.moveToNext());
				}
				cursor.close();
			}
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
		}

		return dataArrays;
	}

	//Search for parts based on a Condition String
	public ArrayList<ArrayList<Object>> getCompatibleRows(String tableName,
			String conditionString) {
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

		Cursor cursor;

		try {
			cursor = db.query(tableName,
					new String[] { TABLE_ROW_ID, ROW1_NAME }, 
					conditionString,
					null, null, null, ROW1_NAME);

			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getLong(0));
					dataList.add(cursor.getString(1));

					dataArrays.add(dataList);
				}

				while (cursor.moveToNext());
			}
			cursor.close();
		} catch (Exception e) {
			Log.e("DB Error", e.toString());
		}

		return dataArrays;
	}
}
