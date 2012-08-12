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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

//This class parses the XML files from te S3 server
public class DBHandler extends org.xml.sax.helpers.DefaultHandler {
	private boolean in_table = false;
	private boolean in_item = false;
	private boolean in_id = false;
	private boolean in_name = false;
	private boolean in_row = false;
	private boolean in_db_version = false;
	
	private String tableReading;

	private int tableCount = 0;
	private int itemCount = 0;
	private int db_version;
	
	private HardwareDB  db = new HardwareDB();
	private HardwareItem item;
	private ArrayList<HardwareItem> table;

	public int getDBVersion() {
		return db_version;
	}

	// Method run when the document is about to start reading
	@Override
	public void startDocument() throws SAXException {
		// Do some startup if needed
	}

	// Method to run when encountered with a new element (ej: <item>)
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if (localName.equals("DB_Version")) {
			this.in_db_version = true;
		} else if (localName.equals("table")) {
			this.in_table = true;
			table = new ArrayList<HardwareItem>();
			String readin = atts.getValue("name");
			if(readin != null)
				tableReading = atts.getValue("name");
		} else if (localName.equals("item")) {
			this.in_item = true;
			item = new HardwareItem();
		} else if (localName.equals("id")) {
			this.in_id = true;
		} else if (localName.equals("name")) {
			this.in_name = true;
		} else if (localName.contains("row")) {
			this.in_row = true;
		}
	}

	// Method to read what inside between element tags (ej: <item> This is what is being read </item>)
	@Override
	public void characters(char ch[], int start, int length) {
		if (this.in_db_version){
			db_version = Integer.parseInt(new String(ch, start, length));
		} else if (this.in_id){			
			item.setId(Integer.parseInt(new String(ch, start, length)));
		} else if (this.in_name){
			item.setName(new String(ch, start, length));
		} else if (this.in_row){
			item.addRow(new String(ch, start, length));
		}
	}

	// Method to run when encountered with the ending of an element (ej: </item>)
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equals("DB_Version")) {
			this.in_db_version = false;
		} else if (localName.equals("table")) {
			this.in_table = false;
			tableCount++;
			
			if (tableReading.equals("CPU")){
				db.setCpu_table(table);
			} else if (tableReading.equals("MB")){
				db.setMb_table(table);
			} else if (tableReading.equals("VGA")){
				db.setVga_table(table);
			} else if (tableReading.equals("RAM")){
				db.setRam_table(table);
			} else if (tableReading.equals("PSU")){
				db.setPsu_table(table);
			} else if (tableReading.equals("HDD")){
				db.setHdd_table(table);
			}
			
		} else if (localName.equals("item")) {
			this.in_item = false;
			itemCount++;
			table.add(item);
		} else if (localName.equals("id")) {
			this.in_id = false;
		} else if (localName.equals("name")) {
			this.in_name = false;
		}  else if (localName.contains("row")) {
			this.in_row = false;
		}
	}

	// Method to run when the document has finished reading
	@Override
	public void endDocument() throws SAXException {
		// Do some finishing work if needed
		Log.d("DEBUG", "Finished XML Database Doc");
			
		if (db_version != 0){
			Log.d("DEBUG", "XML was Version FILE");
			Log.d("DEBUG", "DB_Version: " + db_version);
		} else {
			Log.d("DEBUG", "XML was Database FILE");
			Log.d("DEBUG", "Tables found: " + tableCount);
			Log.d("DEBUG", "Items found: " + itemCount);
		}
	}

	public HardwareDB getDB() {
		return db;
	}
}
