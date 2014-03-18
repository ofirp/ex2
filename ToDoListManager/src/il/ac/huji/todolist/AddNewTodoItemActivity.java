package il.ac.huji.todolist;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_todo_item);

		setTitle("Add New Item");

		final Button cancelButton=(Button) findViewById(R.id.btnCancel);
		final Button okButton = (Button) findViewById(R.id.btnOk);

		//cancel button implementation
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED, returnIntent);        
				finish();				
			}
		});


		//OK button implementation
		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar cal = Calendar.getInstance();
				DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
				EditText editText = (EditText) findViewById(R.id.edtNewItem);

				if(!editText.getText().toString().trim().equals("")){

					int day = datePicker.getDayOfMonth();
					int month = datePicker.getMonth()+1;
					int year=datePicker.getYear();

					cal.set(Calendar.YEAR,year);
					cal.set(Calendar.MONTH,month);
					cal.set(Calendar.DAY_OF_MONTH,day);

					Date date = cal.getTime();

					Intent returnIntent = new Intent();
					returnIntent.putExtra("dueDate",date);
					returnIntent.putExtra("title",editText.getText().toString());
					setResult(RESULT_OK,returnIntent);
					finish();
				}else{
					Intent returnIntent = new Intent();
					setResult(RESULT_CANCELED, returnIntent);        
					finish();
				}


			}
		});



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_todo_item, menu);
		return true;
	}

}
