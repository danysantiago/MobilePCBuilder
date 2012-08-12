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

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.dany.mobu.databases.DBHandler;
import com.dany.mobu.databases.HardwareDBManager;

//Startup activity to get the hardware db from the server
public class StartUpActivity extends Activity{
	
	//1 static db is created here and used throughout the app
	public static HardwareDBManager db;
	
	//Names of bucket and files
	private static final String BUCKET = "MoBu";
	private static final String DB_FILE_NAME = "HardwareDB.xml";
	private static final String DB_VER_FILE_NAME = "HardwareDBVersion.xml";
	
	private Context context;
	
	//S3 objects
	private AmazonS3 S3 = null;
	private BasicAWSCredentials credentials = null;
	
	private boolean startUpCompleted = false;
	private boolean connectionError = false;
	
	private SharedPreferences preferences;
	
	ProgressDialog progressDialog;
	
	private int taskAttemps = 0;
	
	private GetDB_AsyncTask task;
	
	static {
		System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar (for some reason it must be before layout set)

		setContentView(R.layout.startup);
		
		context = this;
		
		Log.d("DEBUG","StartUp Activity Sucessful");
		
		//Gets the local SQL DB version
		preferences = getPreferences(MODE_PRIVATE);
		db = new HardwareDBManager(context, preferences.getInt("db_version", 1));
		
		//If DB version is 1 (which means first time setup-up) then ask nicely for download
		if(HardwareDBManager.DB_VERSION == 1){
			AlertDialog.Builder adb = new AlertDialog.Builder(context);
			adb.setTitle("Welcome");
			adb.setIcon(R.drawable.icon);
			adb.setMessage("MoBu needs a little bit of internet to download the Database from which it depends on, please make sure you have an active internet connection. Don't worry, this is a one-time only thing.");
			adb.setPositiveButton("I'm ready!", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					//Download is performed in a different thread
					task = new GetDB_AsyncTask();
					task.execute();
				}
			});
			adb.setNegativeButton("Give me a second",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			});
			adb.show();
		} else {
			//If DB version is different than 1, meaning there is already a local SQL DB then go straight to update check
			task = new GetDB_AsyncTask();
			task.execute();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(progressDialog != null)
			progressDialog.dismiss();
	}

	//ASync Task to get the DB
	private class GetDB_AsyncTask extends AsyncTask<URL, Integer, Long> {
		
		@Override
		protected void onPreExecute() {
			if(progressDialog == null){
				//Shows dialog while we work with a few things
				progressDialog = ProgressDialog.show(StartUpActivity.this, "","Starting Up MoBu. Please wait...", true);
				//Dialog only cancel-able if not first time set-up
				if(HardwareDBManager.DB_VERSION == 1){
					progressDialog.setCancelable(false);
				} else {
					progressDialog.setCancelable(true);
				}
				progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						task.cancel(false);
						
						Intent i = new Intent(StartUpActivity.this, MainMenu.class);
						startActivity(i);
					}
				});
				progressDialog.show();
			}
			
			
		}

		@Override
		protected Long doInBackground(URL... arg0) {
			try {
				//Get AWS credentials from local properties file (not very secure)
				Properties properties = new Properties();
				properties.load( getClass().getResourceAsStream( "AwsCredentials.properties" ) );
				credentials = new BasicAWSCredentials( properties.getProperty( "accessKey" ), properties.getProperty( "secretKey" ) );
				
				//Open connection using credentials
				S3 = new AmazonS3Client(credentials);
				
				//Get HardwareDBVersion.xml file
				InputStream inStream = S3.getObject(BUCKET, DB_VER_FILE_NAME).getObjectContent();
				
				//Create SAX parser
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				//Create XML reader
				XMLReader xr = sp.getXMLReader();
				//Set my XML handler
				DBHandler DBHandler = new DBHandler();
				xr.setContentHandler(DBHandler);
				//Let the parsing start!
				xr.parse(new InputSource(inStream));
				//Get the DB version
				int dbversion = DBHandler.getDBVersion();
				//Check if version in server is newer than local
				if(dbversion > HardwareDBManager.DB_VERSION){
					Log.d("DEBUG", "DB Update Available");
					Log.d("DEBUG", "Current ver: " + HardwareDBManager.DB_VERSION + " Server ver: " + dbversion);
					//Get HardwareDB.xml file
					inStream = S3.getObject(BUCKET, DB_FILE_NAME).getObjectContent();
					
					spf = SAXParserFactory.newInstance();
					sp = spf.newSAXParser();
					
					xr = sp.getXMLReader();
					
					DBHandler = new DBHandler();
					xr.setContentHandler(DBHandler);

					xr.parse(new InputSource(inStream));
					//Give the extracted dataSet to the DBManager so it can create the local SQL DB
					HardwareDBManager.dataSet = DBHandler.getDB();
					db = new HardwareDBManager(context, dbversion);
					//Save the new local DB version
					SharedPreferences.Editor editor = preferences.edit();
					editor.putInt("db_version", dbversion);
					editor.commit();
				} else {
					//If local DB is up-to-date, do nothing
					Log.d("DEBUG", "DB is Up-To-Date");
				}
				
				startUpCompleted = true;
			} catch(Exception e) {
				//Error handling, if not first time set-up, skipping update is ok
				if(e.toString().contains("Unable to execute HTTP request") && HardwareDBManager.DB_VERSION != 1){
					startUpCompleted = true;
					connectionError = true;
				} else {
					HardwareDBManager.DB_VERSION = 1;
					startUpCompleted = false;
				}
				e.printStackTrace();
				Log.d("DEBUG", "Error: " + e.toString());
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPostExecute(Long result) {
			//Once threads finishes without any problems, start MainMenu activity
			if(startUpCompleted){
				
				if(connectionError)
					Toast.makeText(context, "DB Update Skipped.\nConnection to Internet Unsuccessful", Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(StartUpActivity.this, MainMenu.class);
				startActivity(i);
			} else if (taskAttemps < 10){
				//If there was a problem during thread execution, start a new one and try again
				taskAttemps++;
				task = new GetDB_AsyncTask();
				task.execute();
			} else {
				Toast.makeText(context, "Unable to complete MoBu's first time set-up, make sure you have an active internet connection", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
	}

}
