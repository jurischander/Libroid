package com.schander.libroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import com.schander.libroid.MetatataManager.MetaData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataStore extends SQLiteOpenHelper{
	
//	private final Context myContext;
	private static DataStore instance;
	private static SQLiteDatabase database = null;
	
//	private static final String DATABASE_NAME = "libroidDataBase.db";
	private static String DATABASE_NAME = null;

//	private static final int DATABASE_VERSION = 1;
	private static final String BOOKS_TABLE = "books";
//	private static final String AUTHORS_TABLE = "authors";
//	private static final String BOOKS_AUTHORS_LINK_TABLE = "books";

	// Column names
	public static final String BOOKS_KEY_ID = "id";
	public static final String BOOKS_TITLE = "title";
	public static final String BOOKS_SORT = "sort";
	public static final String BOOKS_TIME_STAMP = "timestamp";
	public static final String BOOKS_PUBDATE = "pubdate";
	public static final String BOOKS_SERIES_INDEX = "series_index";
	public static final String BOOKS_AUTHOR_SORT = "author_sort";
	public static final String BOOKS_ISBN = "isbn";
	public static final String BOOKS_ICCN = "iccn";
	public static final String BOOKS_PATH = "path";
	public static final String BOOKS_FLAGS = "flags";
	public static final String BOOKS_UUID = "uuid";
	public static final String BOOKS_HAS_COVER = "has_cover";
	public static final String BOOKS_LAST_MODIFIED = "last_modified";
	
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
			+ BOOKS_ICCN + " TEXT, "
			+ BOOKS_PATH + " TEXT, "
			+ BOOKS_FLAGS + " TEXT, "
			+ BOOKS_UUID + " TEXT, "
			+ BOOKS_HAS_COVER + " TEXT, "
			+ BOOKS_LAST_MODIFIED + " TEXT);";
	
	private DataStore(Context context){
		super(context, DATABASE_NAME, null, 21);
//		this.myContext = context;
		database = this.getWritableDatabase();
	}
	
	public static DataStore getInstance(Context context, String dataBase){
		if((instance == null) || (DATABASE_NAME == null) ||(!DATABASE_NAME.equals(dataBase))){
			DATABASE_NAME = dataBase;
			DataStore.instance = new DataStore(context);
		}
		return DataStore.instance;
	}

	public static DataStore getInstance(){
		return DataStore.instance;
	}
	
	public ArrayList<BookEntry> getStoredBooks(){
		MetaData metaData = null;
    	ArrayList<BookEntry> books = new ArrayList<BookEntry>();
    	Cursor cursor = database.query(BOOKS_TABLE, null, null, null, null, null, null);
    	if(cursor.getCount() == 0){
    		books.add(new BookEntry(null, "", "Empty ", "library", "annotation"));
    	}else{
    		String pathToImage = "";
    		cursor.moveToFirst();
    		try{
    		while(cursor.moveToNext()){
    			String bookPath = cursor.getString(cursor.getColumnIndex(BOOKS_PATH));
    			int lastIndex = DATABASE_NAME.lastIndexOf('/');
    			try{
    				MetatataManager metadataManager = new MetatataManager();
    				FileInputStream inputStream = 
    						new FileInputStream(new File(DATABASE_NAME.substring(0, lastIndex) + "/" + bookPath + "/metadata.opf"));
    				metaData = metadataManager.parse(inputStream);
    			}catch(Exception e){
    				metaData = null;
    				Log.e("DATA_STORE", e.toString());
    			}
    			
    			if(cursor.getInt(cursor.getColumnIndex(BOOKS_HAS_COVER)) == 1){
    				pathToImage = getPathToImage(bookPath);
    			}else{
    				pathToImage = "";
    			}
    			
    			File bookFile = getBookFile(bookPath);
    			if(metaData == null){
        			books.add(new BookEntry(bookFile, pathToImage, 
					cursor.getString(cursor.getColumnIndex(BOOKS_TITLE)),
					cursor.getString(cursor.getColumnIndex(BOOKS_AUTHOR_SORT)), ""));
    			}else{
    				books.add(new BookEntry(bookFile, pathToImage, metaData.title, metaData.creator, metaData.description));
    			}
    		}
    		}catch(Exception e){
    			return null;
    		}
    	}
    	return books;		
	}
	
	private String getPathToImage(String relPath){
		// get last index for '.' char
        int lastIndex = DATABASE_NAME.lastIndexOf('/');
        String str = DATABASE_NAME.substring(0, lastIndex) + "/" + relPath;
        File dir = new File(str);
		FilenameFilter fileNameFilter = new ExtentionFilter(".jpg");     
        File[] fileList = dir.listFiles(fileNameFilter);
		return fileList[0].getAbsolutePath();
	}
	
	private File getBookFile(String relPath){
		File file = null;
        int lastIndex = DATABASE_NAME.lastIndexOf('/');
        String str = DATABASE_NAME.substring(0, lastIndex) + "/" + relPath;
        File dir = new File(str);
		FilenameFilter fileNameFilter = new ExtentionFilter(".mobi");   
        File[] fileList = dir.listFiles(fileNameFilter);
        if((fileList != null) && (fileList.length != 0)){
        	file = fileList[0];
        }
        return file;
	}
	
	private class ExtentionFilter implements FilenameFilter{
		
		private String extention;
		
		public ExtentionFilter(String extention){
			this.extention = extention;
		}

		@Override
		public boolean accept(File dir, String filename) {
            if(filename.lastIndexOf('.')>0)
            {
               // get last index for '.' char
               int lastIndex = filename.lastIndexOf('.');
               
               // get extension
               String str = filename.substring(lastIndex);
               
               // match path name extension
               if(str.equals(extention))
               {
                  return true;
               }
            }
            return false;		
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
}
