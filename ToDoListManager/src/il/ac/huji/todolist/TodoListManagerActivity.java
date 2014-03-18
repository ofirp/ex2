package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class TodoListManagerActivity extends Activity {

	
	
	private ArrayList<listItem> memosList;
	private ListView list;
	private MyArrayAdapter adapter;
	final int RETURN_CODE=13;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		memosList=new ArrayList<listItem>();
		list=(ListView) findViewById(R.id.listTodoItems);
		adapter=new MyArrayAdapter(this,memosList);
		list.setAdapter(adapter);
		registerForContextMenu(list);
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
				String title = data.getStringExtra("title");
				Date date = (Date) data.getExtras().get("dueDate");
				memosList.add(new listItem(title,date));
				adapter.notifyDataSetChanged();
			}
			else if(resultCode==RESULT_CANCELED){
				return;
			}
			
		}
		
		
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v, ContextMenuInfo menuInfo){
		
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater m =getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String title = memosList.get(info.position).getTitle();
		menu.setHeaderTitle(title);
        m.inflate(R.menu.context_menu, menu);
        MenuItem call = menu.findItem(R.id.menuItemCall);
        
        if(title.startsWith("Call ") || title.startsWith("call ")){
        	call.setTitle(memosList.get(info.position).getTitle());
        	call.setVisible(true);
        }
				
	}
	
	public boolean onContextItemSelected(MenuItem item){
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int pos = (int) info.id;
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				memosList.remove(pos);
				adapter.notifyDataSetChanged();
				return true;
			case R.id.menuItemCall:
				String number=memosList.get(pos).title.split("\\s+")[1];
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+number));
				startActivity(intent);
				
			default:
				return super.onContextItemSelected(item);
		}
	}
}
