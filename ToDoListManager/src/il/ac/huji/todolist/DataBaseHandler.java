package il.ac.huji.todolist;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DataBaseHandler extends SQLiteOpenHelper {

	public static final String DB_NAME = "todo_db";
	public static final String CLASS_NAME = "todo";
	public static final String TITLE_COL = "title";
	public static final String DUE_COL = "due";
	private static final int DB_VERSOIN = 1;
	// Contacts Table Columns names
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_DUE = "due";
	private int idCount;
	private final Context appContext;
	public DataBaseHandler(Context context){
		super(context, DB_NAME, null,DB_VERSOIN);
		this.appContext =context;
		idCount=1;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTableStr = "CREATE TABLE "+DB_NAME+"( "+KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TITLE + " TEXT, "
				+ KEY_DUE + " LONG" + ")";
		db.execSQL(createTableStr);


	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}

	public void addRecord(String title,long due){

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID,idCount);
		values.put(KEY_TITLE, title);
		values.put(KEY_DUE, due);

		db.insert(DB_NAME, null, values);
		db.close();
		ParseObject obj = new ParseObject(CLASS_NAME);
		obj.put(TITLE_COL, title);
		obj.put(DUE_COL, due);
		obj.saveInBackground();
		idCount++;
		
	}

	public void deleteRecord(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		
		//get title
		String sql = "SELECT "+ DataBaseHandler.KEY_TITLE +" FROM "+DataBaseHandler.DB_NAME +" WHERE _id="+(id);
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		String title = cursor.getString(0);
		
		//get due date
		sql = "SELECT "+ DataBaseHandler.KEY_DUE +" FROM "+DataBaseHandler.DB_NAME +" WHERE _id="+(id);
		cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		long due = cursor.getLong(0);
		
		//deleting from parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME);
		query.whereEqualTo(TITLE_COL, title);
		query.whereEqualTo(DUE_COL, due);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		    public void done(ParseObject obj , ParseException e) {
		        if (e == null) {
		            obj.deleteInBackground();
		        } else {
		        	Log.i("error in delete","no object was found (delete)");
		        }
		    }
		});
		
		//deleting from sqlite
		db.delete(DB_NAME, KEY_ID + "=?", new String[] { Integer.toString(id) });
		db.close();
		updateIds(id);
		
	}


	private void updateIds(int id) {
		id++;
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i=id;i<idCount;i++){
			ContentValues values = new ContentValues();
			values.put(KEY_ID,i-1);
			db.update(DB_NAME,values,KEY_ID+"= ?",new String[] { String.valueOf(i)});
		}
		idCount--;
		db.close();
	}
	public void printDB(){
		// Select All Query
		String selectQuery = "SELECT  * FROM " + DB_NAME;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {


				String log = "Id: "+Integer.parseInt(cursor.getString(0))+" ,title: " + cursor.getString(1) + " ,date: " + cursor.getLong(2);
				// Writing Contacts to log
				Log.d("Name: ", log);

			} while (cursor.moveToNext());
		}
	}

	public void setIdCount(int num){
		idCount=num;
	}
	
	
	/*
	public void syncDBs() {
		//deletes all rows from the class
		deleteClass();
		//add all rows from SQLite db
		updateParseClass();
	}
	public void deleteClass(){
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(CLASS_NAME);
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> rows, ParseException e) {
				if(e == null){
					for(int i=0;i<rows.size();i++){
						Toast.makeText(appContext, Integer.toString(i), Toast.LENGTH_SHORT).show();
							rows.get(i).deleteInBackground();
						
					}
					}else{
					Toast.makeText(appContext, "something went wrog. probably no internet connection", Toast.LENGTH_LONG).show();
				}				
			}
		});
	}
	
	public void updateParseClass(){
		String selectQuery = "SELECT  * FROM " + DB_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		printDB();
		ParseObject obj;
		if (cursor.moveToFirst()) {
			do {
				String title = cursor.getString(1);
				long date = cursor.getLong(2);
				obj = new ParseObject(CLASS_NAME);
				obj.put(TITLE_COL, title);
				obj.put(DUE_COL, date);
				obj.saveInBackground();
				
			} while (cursor.moveToNext());
			
		}	
	}*/
	
	
}
