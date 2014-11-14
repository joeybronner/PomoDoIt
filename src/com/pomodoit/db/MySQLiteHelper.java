package com.pomodoit.db;

import java.util.LinkedList;
import java.util.List;

import com.pomodoit.util.Toaster;
import com.pomodoit.views.SettingsActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
		addSoundMode(db);
		addScreenMode(db);
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

	public void addSoundMode(SQLiteDatabase db) {

		//SQLiteDatabase db = this.getWritableDatabase();

		// 1. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_KEY, "sound"); 
		values.put(KEY_VALUE, "no");

		// 3. insert
		db.insert(TABLE_USERSETS,null,values); 
	}

	public int getSizeUserSets() {
		return getSizeTable(TABLE_USERSETS);
	}

	public void addScreenMode(SQLiteDatabase db) {

		//SQLiteDatabase db = this.getWritableDatabase();

		// 1. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_KEY, "screen"); 
		values.put(KEY_VALUE, "no");

		// 3. insert
		db.insert(TABLE_USERSETS,null,values); 
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
	}

	public void deleteAllSessions()
	{
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. delete
		db.delete(TABLE_SESSIONS,
				KEY_ID + " >= ?",
				new String[] { "0" });
	}

	public boolean getSoundMode()
	{
		String query = 	"SELECT * FROM " + TABLE_USERSETS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst())
		{
			do {
				if (cursor.getString(0).equals("sound") && 
						cursor.getString(1).equals("yes")) {
					return true;
				}
				else if (cursor.getString(0).equals("sound") && 
						cursor.getString(1).equals("no")) {
					return false;
				}
			}while (cursor.moveToNext());
		}
		return true;
	}

	public boolean getScreenMode()
	{
		String query = 	"SELECT * FROM " + TABLE_USERSETS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst())
		{
			do {
				if (cursor.getString(0).equals("screen") && 
						cursor.getString(1).equals("yes")) {
					return true;
				}
				else if (cursor.getString(0).equals("screen") && 
						cursor.getString(1).equals("no")) {
					return false;
				}
			}while (cursor.moveToNext());
		}
		return true;
	}

	public List<String> getTableUserSetsValues() {

		List<String> s = new LinkedList<String>();

		String query = 	"SELECT * FROM " + TABLE_USERSETS;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build session and add it to list
		if (cursor.moveToFirst())
		{
			do {

				s.add(cursor.getString(0) + cursor.getString(1));

			} while (cursor.moveToNext());
		}

		db.close();

		// return all sessions
		return s;

	}

	public void updateSoundMode(String value)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_VALUE, value);
		db.update(TABLE_USERSETS, values, KEY_KEY + " = ?", new String[] { "sound" });
	}

	public void updateScreenMode(String value)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_VALUE, value);
		db.update(TABLE_USERSETS, values, KEY_KEY + " = ?", new String[] { "screen" });
	}

	public int getSizeTable(String tableName) {
		SQLiteDatabase db = this.getWritableDatabase();
		return (int) DatabaseUtils.queryNumEntries(db, tableName);
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