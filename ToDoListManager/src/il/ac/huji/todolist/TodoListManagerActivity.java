package il.ac.huji.todolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TodoListManagerActivity extends Activity {

	
	
	private ArrayList<String> memosList;
	private ListView list;
	private ColoredArrayAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		memosList=new ArrayList<String>();
		list=(ListView) findViewById(R.id.listTodoItems);
		adapter=new ColoredArrayAdapter(this,android.R.layout.simple_list_item_1 ,memosList);
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
		EditText memoView = (EditText) findViewById(R.id.edtNewItem);
		String memo = memoView.getText().toString();
		memo=memo.trim();
		if(!memo.equals(""))
			memosList.add(memo);
		memoView.setText("");
		adapter.notifyDataSetChanged();
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v, ContextMenuInfo menuInfo){
		
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater m =getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(memosList.get(info.position));
        m.inflate(R.menu.context_menu, menu);
				
	}
	
	public boolean onContextItemSelected(MenuItem item){	
		switch (item.getItemId()){
			case R.id.menuItemDelete:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				int pos = (int) info.id;
				memosList.remove(pos);
				adapter.notifyDataSetChanged();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
}
