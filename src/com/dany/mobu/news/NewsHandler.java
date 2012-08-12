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



package com.dany.mobu.news;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

//This class handles the parsing of the XML documents, it uses SAX (Simple API XML)
public class NewsHandler extends org.xml.sax.helpers.DefaultHandler {

	// booleans to identify how deep we are in the XML
	private boolean in_item = false;
	private boolean in_title = false;
	private boolean in_link = false;
	private boolean in_pubDate = false;

	private int itemCount = 0;

	// Array that will be filled and returned
	private ArrayList<NewsDataSet> parsedNewsDataSetArray = new ArrayList<NewsDataSet>();
	// DataSets that will be added to the array
	private NewsDataSet parsedNewsDataSet = new NewsDataSet();;

	// We will return the array of DataSets once parsing is finished
	public ArrayList<NewsDataSet> getParsedData() {
		return this.parsedNewsDataSetArray;
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

		if (localName.equals("item")) {
			this.in_item = true;
			itemCount++;
			parsedNewsDataSet = new NewsDataSet();
		} else if (localName.equals("title")) {
			this.in_title = true;
		} else if (localName.equals("link")) {
			this.in_link = true;
		} else if (localName.equals("pubDate")) {
			this.in_pubDate = true;
		} else if (localName.equals("enclosure")) {
			// Extracts an Attribute
			String attrValue = atts.getValue("url");
			parsedNewsDataSet.setImgUrl(attrValue);
		}
	}

	// Method to read what inside between element tags (ej: <item> This is what is being read </item>)
	@Override
	public void characters(char ch[], int start, int length) {
		if (this.in_title) {
			parsedNewsDataSet.setTitle(new String(ch, start, length));
		} else if (this.in_link) {
			parsedNewsDataSet.setLink(new String(ch, start, length));
		} else if (this.in_pubDate) {
			parsedNewsDataSet.setPubDate(new String(ch, start, length));
		}
	}

	// Method to run when encountered with the ending of an element (ej: </item>)
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if (localName.equals("item")) {
			this.in_item = false;
			parsedNewsDataSetArray.add(parsedNewsDataSet);
		} else if (localName.equals("title")) {
			this.in_title = false;
		} else if (localName.equals("link")) {
			this.in_link = false;
		} else if (localName.equals("pubDate")) {
			this.in_pubDate = false;
		}
	}

	// Method to run when the document has finished reading
	@Override
	public void endDocument() throws SAXException {
		// Do some finishing work if needed
		Log.d("DEBUG", "Finished XML Doc");
		Log.d("DEBUG", "Items: " + itemCount);
	}

}
