package il.ac.huji.todolist;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class TodoListManagerActivity extends Activity {

	private ListView list;
	private MyCursorAdapter adapter;
	private DataBaseHandler handler;
	final int RETURN_CODE=13;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		handler = new DataBaseHandler(getApplicationContext());
		list=(ListView) findViewById(R.id.listTodoItems);
		LoadTask loadTask = new LoadTask();
		loadTask.execute();
		registerForContextMenu(list);
		
	}
	
	
	
	private class LoadTask extends AsyncTask<Void, Void, Cursor>{

		@Override
		protected Cursor doInBackground(Void... params) {
			SQLiteDatabase db = handler.getReadableDatabase();
			String sqlStr="SELECT * FROM "+DataBaseHandler.DB_NAME;
			Cursor cursor=db.rawQuery(sqlStr,null);
			return cursor;
		}
		
		protected void onPostExecute(Cursor cursor){
			handler.setIdCount(cursor.getCount());
			adapter = new MyCursorAdapter(getApplicationContext(), cursor);
			list.setAdapter(adapter);
		}
	}
	
	
	private class AddTask extends AsyncTask<Intent, Void, Cursor>{

		@Override
		protected Cursor doInBackground(Intent... data) {
			String title = data[0].getStringExtra("title");
			long dueLong = data[0].getLongExtra("dueLong",0);
			handler.addRecord(title, dueLong);
			SQLiteDatabase db = handler.getReadableDatabase();
			String sqlStr="SELECT * FROM "+DataBaseHandler.DB_NAME;
			Cursor cursor=db.rawQuery(sqlStr,null);
			return cursor;
		}
		
		protected void onPostExecute(Cursor cursor){
			adapter.changeCursor(cursor);
		}	
	}
	
	private class DeleteTask extends AsyncTask<Integer, Void, Cursor>{

		@Override
		protected Cursor doInBackground(Integer...pos) {
			handler.deleteRecord(pos[0]);
			SQLiteDatabase db = handler.getReadableDatabase();
			String sqlStr="SELECT * FROM "+DataBaseHandler.DB_NAME;
			Cursor cursor=db.rawQuery(sqlStr,null);
			return cursor;
		}
		
		protected void onPostExecute(Cursor cursor){
			adapter.changeCursor(cursor);
		}	
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.menuItemAdd:
			addClick();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	
	public void addClick(){
		//opens a new activity
		Intent intent = new Intent(getApplicationContext(),AddNewTodoItemActivity.class);
		startActivityForResult(intent, RETURN_CODE);
	}
	
	protected void onActivityResult(int requestCode,int resultCode, Intent data){
		
		if (requestCode==RETURN_CODE){
			
			if(resultCode==RESULT_OK){
				AddTask addTask = new AddTask();
				addTask.execute(new Intent[]{data});
			}
			else if(resultCode==RESULT_CANCELED){
				return;
			}
			
		}
		
		
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		SQLiteDatabase db = handler.getReadableDatabase();
		MenuInflater m =getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		String sql = "SELECT "+ DataBaseHandler.KEY_TITLE +" FROM "+DataBaseHandler.DB_NAME +" WHERE _id="+(info.position);
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		String title = cursor.getString(0);
		menu.setHeaderTitle(title);
        m.inflate(R.menu.context_menu, menu);
        MenuItem call = menu.findItem(R.id.menuItemCall);
        
        if(title.startsWith("Call ") || title.startsWith("call ")){
        	call.setTitle(title);
        	call.setVisible(true);
        }
        db.close();
        cursor.close();
				
	}
	
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = (int) info.id;
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				DeleteTask deleteTask=new DeleteTask();
				deleteTask.execute(new Integer[]{pos});
				return true;
			case R.id.menuItemCall:
				SQLiteDatabase db = handler.getReadableDatabase();
				String sql = "SELECT "+DataBaseHandler.KEY_TITLE +" FROM "+DataBaseHandler.DB_NAME +" WHERE _id="+(info.position);
				Cursor cur = db.rawQuery(sql,null);
				cur.moveToFirst();				
				String title = cur.getString(0);
				String number=title.split("\\s+")[1].trim();
				if(number=="")
					number="100";
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				return true;
				
			default:
				return super.onContextItemSelected(item);
		}
	}
}
