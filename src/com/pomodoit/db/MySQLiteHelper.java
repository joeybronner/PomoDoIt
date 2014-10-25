package com.pomodoit.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper
{	 
	/* database version & name */
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SessionsDB";

	public MySQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);  
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		/* =========================== SESSIONS =========================== */
		/* SQL statement to create sessions table */
		String CREATE_SESSIONS_TABLE = "CREATE TABLE SESSIONS ( " +
				"id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"name TEXT, "+
				"mark REAL, "+
				"datesession TEXT)"; // YYYYMMJJ
		/* command to create it. */
		db.execSQL(CREATE_SESSIONS_TABLE);

		/* =========================== USERSETS =========================== */
		/* SQL statement to create sessions table */
		String CREATE_SESSIONS_USERSETS = "CREATE TABLE USERSETS ( " +
				"key TEXT, "+
				"value TEXT)";
		/* command to create it. */
		db.execSQL(CREATE_SESSIONS_USERSETS);
		
		// Initialize database
		initializeUserSets(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop tables
		//db.execSQL("DROP TABLE IF EXISTS SESSIONS");

		// create fresh books table
		this.onCreate(db);
	}

	/* #################################################### */
	/* #################### SESSIONS #################### */
	/* #################################################### */
	// Table configuration
	private static final String TABLE_SESSIONS = "SESSIONS"; 	// table name
	private static final String KEY_ID = "id";				// column 1
	private static final String KEY_NAME = "name";			// column 2
	private static final String KEY_MARK = "mark";			// column 3
	private static final String KEY_DATE = "datesession";	// column 4
	// End configuration

	/* #################################################### */
	/* #################### USERSETS #################### */
	/* #################################################### */
	// Table configuration
	private static final String TABLE_USERSETS = "USERSETS"; 	// table name
	private static final String KEY_KEY = "key";				// column 1
	private static final String KEY_VALUE = "value";			// column 2
	// End configuration

	/* ### METHODS ### */

	public void initializeUserSets(SQLiteDatabase db) {

		// 1. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_KEY, "plane"); 
		values.put(KEY_VALUE, "no");
	}

	public void addSession(Session ses)
	{		
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, ses.getName()); 
		values.put(KEY_MARK, ses.getMark());
		values.put(KEY_DATE, ses.createDate());

		// 3. insert
		db.insert(TABLE_SESSIONS,null,values); 

		// 4. close
		db.close(); 
	}

	public List<Session> getAllSessions()
	{
		List<Session> sess = new LinkedList<Session>();

		// 1. build the query
		String query = "SELECT * FROM " + TABLE_SESSIONS;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build session and add it to list
		Session ses = null;
		if (cursor.moveToFirst())
		{
			do {
				ses = new Session();
				ses.setId(Integer.parseInt(cursor.getString(0)));
				ses.setName(cursor.getString(1));
				ses.setMark(Float.parseFloat(cursor.getString(2)));
				ses.setDate(cursor.getString(3));

				// Add ses to sess
				sess.add(ses);
			} while (cursor.moveToNext());
		}

		// return all sessions
		return sess;
	} 
}