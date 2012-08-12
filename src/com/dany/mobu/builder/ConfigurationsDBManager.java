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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ConfigurationsDBManager {

	public static final String ROW_ID = "_id";
	public static final String[] ROW_NAMES = new String[] { "name", "mb_id",
			"cpu_id", "ram_id", "vga_id", "psu_id", "hdd_id" };

	private static final String TAG = "ConfigurationsDBManager";

	private static final String DB_NAME = "ConfigurationsDB";
	private static final String TABLE_NAME = "Configs";
	private static final int DB_VERSION = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ " ("
			+ ROW_ID + " integer primary key autoincrement not null,"
			+ ROW_NAMES[0] + " text,"
			+ ROW_NAMES[1] + " integer,"
			+ ROW_NAMES[2] + " integer,"
			+ ROW_NAMES[3] + " integer,"
			+ ROW_NAMES[4] + " integer,"
			+ ROW_NAMES[5] + " integer,"
			+ ROW_NAMES[6] + " integer"
			+ ");";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public ConfigurationsDBManager(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			db.execSQL("INSERT INTO Configs VALUES (1,'Example Config',50,14,150,100,200,250)");
			db.execSQL("INSERT INTO Configs VALUES (2,'Example Config 2',50,12,150,100,200,251)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			// Below are 2 lines for testing purposes that recreates the table
			// deleting it completely
			// and then calling onCreate
			// db.execSQL("DROP TABLE IF EXISTS titles");
			// onCreate(db);
		}
	}

	//Opens the DB
	public ConfigurationsDBManager open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	//Closes the DB
	public void close() {
		DBHelper.close();
	}

	//Insert new COnfig to DB
	public void saveNewConfig(String name, long[] componentsListId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ROW_NAMES[0], name);
		for (int i = 1; i < ROW_NAMES.length; i++) {
			initialValues.put(ROW_NAMES[i], (int) componentsListId[i - 1]);
		}
		db.insert(TABLE_NAME, null, initialValues);
	}

	//Delete a certain Config from DB
	public void deleteConfig(long rowId) {
		db.delete(TABLE_NAME, ROW_ID + "=" + rowId, null);
	}

	//Get all Configs
	public Cursor getAllConfigs() {
		return db.query(TABLE_NAME, new String[] { ROW_ID, ROW_NAMES[0],
				ROW_NAMES[1] }, null, null, null, null, null);
	}

	//Gets a certain Config
	public Cursor getConfig(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, TABLE_NAME, new String[] { ROW_ID,
				ROW_NAMES[0], ROW_NAMES[1], ROW_NAMES[2], ROW_NAMES[3],
				ROW_NAMES[4], ROW_NAMES[5], ROW_NAMES[6] }, ROW_ID + "="
				+ rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//Rename a certain Config in the DB
	public void renameConfig(long rowId, String name) {
		ContentValues args = new ContentValues();
		args.put(ROW_NAMES[0], name);
		db.update(TABLE_NAME, args, ROW_ID + "=" + rowId, null);
	}
}
