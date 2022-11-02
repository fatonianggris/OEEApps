package com.project.oee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "OEEcalculator";
	// Login table name
	private static final String TABLE_FAC = "factory";
	private static final String TABLE_CAL = "calculation";
	// Login Table Columns names
	private static final String KEY_ID = "id_factory";
	private static final String KEY_FACTORY = "factory";
	private static final String KEY_USER = "username";
	private static final String KEY_DATE = "date";

	private static final String KEY_IDCAL = "id_cal";
	private static final String KEY_IDF = "id_factory";
	private static final String KEY_MACHINE= "machine";
	private static final String KEY_OP = "operating_time";
	private static final String KEY_TT = "total_time";
	private static final String KEY_PA = "processed_amount";
	private static final String KEY_IDC = "ideal_cycle";
	private static final String KEY_GC = "good_count";
	private static final String KEY_TC = "total_count";
	private static final String KEY_AVAILABILITY = "availability";
	private static final String KEY_PEFORMANCE = "peformance";
	private static final String KEY_ROC = "rate_of_quality";
	private static final String KEY_OEE = "oee";
	private static final String KEY_DATEC = "datec";
	private static final String KEY_DTIME = "downtime";
	private static final String KEY_TDTIME = "total_downtime";
	private static final String KEY_PDTIME = "planned_downtime";
	private static final String KEY_LTIME = "loading_time";
	private static final String KEY_TDELAY = "total_delay";
	private static final String KEY_WHOURS = "working_hours";

	private Context mContext;
	private List<Factory> listFac;
	private List<Record> listRec;

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		listFac = new ArrayList<Factory>();
		listRec = new ArrayList<Record>();
		this.mContext = context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_FAC =
				"CREATE TABLE " + TABLE_FAC + " ("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_FACTORY+ " TEXT,"
				+ KEY_USER + " TEXT,"
				+ KEY_DATE + " TEXT " + ");";

		String CREATE_TABLE_CAL ="CREATE TABLE " + TABLE_CAL + " ("
				+ KEY_IDCAL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_IDF+ " TEXT, "
				+ KEY_MACHINE+ " TEXT, "
				+ KEY_OP + " TEXT, "
				+ KEY_TT + " TEXT, "
				+ KEY_PA + " TEXT, "
				+ KEY_IDC + " TEXT, "
				+ KEY_GC + " TEXT, "
				+ KEY_TC + " TEXT, "
				+ KEY_AVAILABILITY + " TEXT, "
				+ KEY_PEFORMANCE + " TEXT, "
				+ KEY_ROC + " TEXT, "
				+ KEY_OEE + " TEXT, "
				+ KEY_DTIME + " TEXT, "
				+ KEY_TDTIME + " TEXT, "
				+ KEY_PDTIME + " TEXT, "
				+ KEY_LTIME + " TEXT, "
				+ KEY_TDELAY + " TEXT, "
				+ KEY_WHOURS + " TEXT, "
				+ KEY_DATEC + " TEXT " + ")";

		db.execSQL(CREATE_TABLE_FAC);
		db.execSQL(CREATE_TABLE_CAL);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAC +";" + " DROP TABLE IF EXISTS " + TABLE_CAL +";");
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addFactory(ProgressDialog dialog, SessionManager session, String factory, String username, String date) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_FACTORY, factory); // iduser
		values.put(KEY_USER, username); // idposisi
		values.put(KEY_DATE, date); // nama petugas

		// Inserting Row
		long id = db.insert(TABLE_FAC, null, values);
		db.close(); // Closing database connection

		if(!String.valueOf(id).toString().isEmpty()){

			dialog.dismiss();
			session.setLogin(true);
			Intent intent = new Intent(mContext, OEECalculation.class);
			((Activity) mContext).startActivity(intent);
			((Activity) mContext).finish();
		}
		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Storing user details in database
	 * */
	public void addCalculation(Context Ct, String idFactory, String machine, String operatingTime,
							   String totalTime, String processedAmount, String idealCycle,
							   String goodCount, String totalCount, String availability,
							   String performance, String roq, String oee, String dtime,
							   String tdtime, String pdtime, String ltime, String tdelay,
							   String whours, String datec) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_IDF, idFactory); // iduser
		values.put(KEY_MACHINE, machine); // idposisi
		values.put(KEY_OP, operatingTime); // nama petugas
		values.put(KEY_TT, totalTime); // iduser
		values.put(KEY_PA, processedAmount); // idposisi
		values.put(KEY_IDC, idealCycle); // nama petugas
		values.put(KEY_GC, goodCount); // iduser
		values.put(KEY_TC, totalCount); // idposisi
		values.put(KEY_AVAILABILITY, availability); // nama petugas
		values.put(KEY_PEFORMANCE, performance); // iduser
		values.put(KEY_ROC, roq); // idposisi
		values.put(KEY_OEE, oee); // idposisi
		values.put(KEY_DTIME, dtime); // iduser
		values.put(KEY_TDTIME, tdtime); // idposisi
		values.put(KEY_PDTIME, pdtime); // nama petugas
		values.put(KEY_LTIME, ltime); // iduser
		values.put(KEY_TDELAY, tdelay); // idposisi
		values.put(KEY_WHOURS, whours); // idposisi
		values.put(KEY_DATEC, datec); // nama petugas

		// Inserting Row
		long id = db.insert(TABLE_CAL, null, values);
		db.close(); // Closing database connection

	   if(id>0){
		   Toast.makeText(Ct, "Data has been saved.", Toast.LENGTH_LONG).show();
	   }
	}

	/**
	 * Getting user data from database
	 * */
	public void getFacDetail() {
		String selectQuery = "SELECT  * FROM " + TABLE_FAC + " ORDER BY "+KEY_ID+" DESC;";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row

		while (cursor.moveToNext()) {
			String idfac = cursor.getString(cursor.getColumnIndex(KEY_ID));
			String fact = cursor.getString(cursor.getColumnIndex(KEY_FACTORY)).toUpperCase();
			String usr = cursor.getString(cursor.getColumnIndex(KEY_USER)).substring(0, 1).toUpperCase() + cursor.getString(cursor.getColumnIndex(KEY_USER)).substring(1);;
			String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
			Factory m = new Factory(idfac, fact, usr, date);
			listFac.add(m);
		}
		cursor.close();
		db.close();
	}

	public List<Factory> getListFac() {
		return listFac;
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getFacDetailLast() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_FAC + " ORDER BY "+KEY_ID+" DESC LIMIT 1; ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			user.put(KEY_ID, cursor.getString(0));
			user.put(KEY_FACTORY, cursor.getString(1));
			user.put(KEY_USER, cursor.getString(2));
			user.put(KEY_DATE, cursor.getString(3));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	public HashMap<String, String> getFacIDLast() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT "+KEY_ID+" FROM " + TABLE_FAC + " ORDER BY "+KEY_ID+" DESC LIMIT 1; ";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put(KEY_ID, cursor.getString(0));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	public HashMap<String, String> getFacByID(String idf) {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_FAC + " WHERE id_factory ="+ idf +";";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			user.put(KEY_FACTORY, cursor.getString(1));
			user.put(KEY_USER, cursor.getString(2));
			user.put(KEY_DATE, cursor.getString(3));

		}
		cursor.close();
		//db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	public HashMap<String, String> getRecByID(String idc) {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_CAL + " WHERE id_cal ="+ idc +";";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {

			user.put(KEY_MACHINE, cursor.getString(2));
			user.put(KEY_OP, cursor.getString(3));
			user.put(KEY_TT, cursor.getString(4));
			user.put(KEY_PA, cursor.getString(5));
			user.put(KEY_IDC, cursor.getString(6));
			user.put(KEY_GC, cursor.getString(7));
			user.put(KEY_TC, cursor.getString(8));
			user.put(KEY_AVAILABILITY, cursor.getString(9));
			user.put(KEY_PEFORMANCE, cursor.getString(10));
			user.put(KEY_ROC, cursor.getString(11));
			user.put(KEY_OEE, cursor.getString(12));
			user.put(KEY_DTIME, cursor.getString(13)); // iduser
			user.put(KEY_TDTIME, cursor.getString(14)); // idposisi
			user.put(KEY_PDTIME, cursor.getString(15)); // nama petugas
			user.put(KEY_LTIME, cursor.getString(16)); // iduser
			user.put(KEY_TDELAY, cursor.getString(17)); // idposisi
			user.put(KEY_WHOURS, cursor.getString(18)); // idposisi
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	public Cursor getRecByIDFac(String idf) {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_CAL + " WHERE id_factory =" + idf+";";
		Cursor res = db.rawQuery(selectQuery, null);
		return res;
	}
	/**
	 * Getting user data from database
	 * */
	public void getCalDetail(String idf) {
		String selectQuery = "SELECT  * FROM " + TABLE_CAL + " WHERE id_factory =" + idf + " ORDER BY "+KEY_IDCAL+" DESC;";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		int no=1;
		while (cursor.moveToNext()) {

			String idcal = cursor.getString(cursor.getColumnIndex(KEY_IDCAL));
			String idfac = cursor.getString(cursor.getColumnIndex(KEY_IDF));
			String mac = cursor.getString(cursor.getColumnIndex(KEY_MACHINE));
			String op = cursor.getString(cursor.getColumnIndex(KEY_OP))+" hr";
			String tt = cursor.getString(cursor.getColumnIndex(KEY_TT))+" hr";
			String pa = cursor.getString(cursor.getColumnIndex(KEY_PA))+" kg";
			String idc = cursor.getString(cursor.getColumnIndex(KEY_IDC))+" kg/hr";
			String gc = cursor.getString(cursor.getColumnIndex(KEY_GC))+" kg";
			String tc = cursor.getString(cursor.getColumnIndex(KEY_TC))+" kg";
			String ava = cursor.getString(cursor.getColumnIndex(KEY_AVAILABILITY))+"%";
			String per = cursor.getString(cursor.getColumnIndex(KEY_PEFORMANCE))+"%";
			String roc = cursor.getString(cursor.getColumnIndex(KEY_ROC))+"%";
			String oee = cursor.getString(cursor.getColumnIndex(KEY_OEE))+"%";
			String dtime = cursor.getString(cursor.getColumnIndex(KEY_DTIME))+" %";;
			String tdtime = cursor.getString(cursor.getColumnIndex(KEY_TDTIME))+" hr";;
			String pdtime = cursor.getString(cursor.getColumnIndex(KEY_PDTIME))+" hr";;
			String ltime = cursor.getString(cursor.getColumnIndex(KEY_LTIME))+" hr";;
			String tdelay = cursor.getString(cursor.getColumnIndex(KEY_TDELAY))+" hr";;
			String whours = cursor.getString(cursor.getColumnIndex(KEY_WHOURS))+"%";
			Record rec = new Record(idcal, idfac, mac, op, tt, pa, idc, gc, tc, ava, per, roc, oee, dtime, tdtime, pdtime, ltime, tdelay, whours);
			rec.setNo(Integer.toString(no));
			listRec.add(rec);
			no++;
		}
		cursor.close();
		db.close();
	}

	public List<Record> getListRec() {
		return listRec;
	}
	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteFac(String idf) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_FAC,  KEY_ID +" = "+ idf + ";", null);
		db.delete(TABLE_CAL,  KEY_IDF +" = "+ idf + ";", null);
		db.close();

	}

	public void deleteCal(String idc) {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_CAL, KEY_IDCAL +" = "+ idc + ";", null);
		db.close();
	}

}
