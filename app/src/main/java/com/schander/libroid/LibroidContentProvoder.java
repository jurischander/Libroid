package com.schander.libroid;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class LibroidContentProvoder extends ContentProvider {
	
	LibroidDatabaseHelper dbHelper;
	
	public static final Uri CONTENT_URI = Uri.parse("content://com.schander.libroid");
	
	// Column names
	public static final String BOOKS_KEY_ID = "id";
	public static final String BOOKS_TITLE = "title";
	public static final String BOOKS_SORT = "sort";
	public static final String BOOKS_TIME_STAMP = "timestamp";
	public static final String BOOKS_PUBDATE = "pubdate";
	public static final String BOOKS_SERIES_INDEX = "series_index";
	public static final String BOOKS_AUTHOR_SORT = "author_sort";
	public static final String BOOKS_ISBN = "isbn";
	public static final String BOOKS_ICON = "icon";
	public static final String BOOKS_PATH = "path";
	public static final String BOOKS_FLAGS = "flags";
	public static final String BOOKS_UUID = "uuid";
	public static final String BOOKS_HAS_COVER = "has_cover";
	public static final String BOOKS_LAST_MODIFIED = "last_modified";
	
	private static class LibroidDatabaseHelper extends SQLiteOpenHelper {
		
		private static final String TAG = "LibroidBooksProvider";
		
		private static final String DATABASE_NAME = "metadata.db";
		private static final int DATABASE_VERSION = 1;
		private static final String BOOKS_TABLE = "books";
//		private static final String AUTHORS_TABLE = "authors";
//		private static final String BOOKS_AUTHORS_LINK_TABLE = "books";
		
		private static final String DATABASE_CREATE = 
				"create table " + BOOKS_TABLE + " ("
				+ BOOKS_KEY_ID + " integer primary key autoincrement, "
				+ BOOKS_TITLE + " TEXT, "
				+ BOOKS_SORT + " TEXT, "
				+ BOOKS_TIME_STAMP + " TEXT, "
				+ BOOKS_PUBDATE + " TEXT, "
				+ BOOKS_SERIES_INDEX + " TEXT, "
				+ BOOKS_AUTHOR_SORT + " TEXT, "
				+ BOOKS_ISBN + " TEXT, "
				+ BOOKS_ICON + " TEXT, "
				+ BOOKS_PATH + " TEXT, "
				+ BOOKS_FLAGS + " TEXT, "
				+ BOOKS_UUID + " TEXT, "
				+ BOOKS_HAS_COVER + " TEXT, "
				+ BOOKS_LAST_MODIFIED;
		
		private SQLiteDatabase booksDatabase;
		
		public LibroidDatabaseHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + " , which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + BOOKS_TABLE);
			onCreate(db);
		}
		
	}
	
	@Override
	public boolean onCreate(){
		Context context = getContext();
		
		dbHelper = new LibroidDatabaseHelper(context, 
				LibroidDatabaseHelper.DATABASE_NAME, null, 
				LibroidDatabaseHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	// Create the constants used to differentiate between the different URI
	// requests
	private static final int BOOKS = 1;
	private static final int BOOK_ID = 2;
	
	private static final UriMatcher uriMatcher;
	
	// Allocate the UriMatcher object , where a URI ensing in 'books' will
	// correspond to a request for all books, and 'books' with a 
	// trailing '/[rowID]' will represent a single book row.
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("schander.libroid.LibroidContentProvoder", "books", BOOKS);
		uriMatcher.addURI("schander.libroid.LibroidContentProvoder", "books/#", BOOK_ID);
	}

	
	@Override
	public String getType(Uri uri) {
		switch(uriMatcher.match(uri)){
			case BOOKS: return "";
			case BOOK_ID: return "";
			default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
